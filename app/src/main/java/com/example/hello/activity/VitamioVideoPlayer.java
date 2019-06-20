package com.example.hello.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.example.hello.domain.MediaItem;
import com.example.hello.util.MyUtil;
import com.example.hello.view.VitamioVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class VitamioVideoPlayer extends Activity implements View.OnClickListener{

    private static final String TAG = "VitamioVideoPlayer";

    private VitamioVideoView videoView;
    private Uri uri;
    private LinearLayout mediaControllerTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar voiceSeekBar;
    private Button btnSwitchPlayer;
    private LinearLayout mediaControllerBottom;
    private TextView tvCurrentTime;
    private SeekBar videoSeekBar;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    // 视频进度的更新
    private static final int PROGRESS = 1;
    // 监听电量的变化
    MyBroadcastReceiver receiver;
    // 视频列表
    private ArrayList<MediaItem> mediaItems;
    // 列表中的视频位置
    private int position;
    // 定义手势识别器
    private GestureDetector detector;
    // 是否显示控制面板
    private boolean isshowMediaController = false;
    // 隐藏消息
    private static final int HIDE_MESSAGE = 2;
    // 是否全屏
    private boolean isFullScreen;
    // 全屏
    private final int FULL_SCREEN = 1;
    // 默认
    private final int DEFAULT_SCREEN = 0;
    // 视频宽
    private int screenWidth = 0;
    // 视频高
    private int screenHeight = 0;
    // 视频真实宽
    private int videoWidth;
    // 视频真实高
    private int videoHeight;
    // 音量
    private AudioManager audioManager;
    // 当前音量
    private int currentVoice;
    // 最大音量
    private int maxVoice;
    // 是否静音
    private boolean isMute = false;

    private float startY;
    // 屏幕的高
    private float touchRange;
    // 按下的音量
    private int mVoice;
    // 是否是网络uri
    private boolean isNetUri;
    // 视频缓冲
    private TextView tvNetspeed;
    private LinearLayout videoBuffer;
    private boolean isUseSystem = true;
    // 上一次播放进度
    private int precurrentPosition;
    // 视频加载
    private TextView tvLoadingNetspeed;
    private LinearLayout loading;
    // 显示网速
    private static final int SHOW_SPEED = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        setListener();
        getData();
        setData();

    }

    private void setData() {
        if(uri != null){
            // 设置视频名称
            tvName.setText(uri.toString());
            isNetUri = MyUtil.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        }else if(mediaItems != null && mediaItems.size() > 0){
            MediaItem mediaItem = mediaItems.get(position);
            // 设置视频名称
            tvName.setText(mediaItem.getName());
            isNetUri = MyUtil.isNetUri(mediaItem.getData());
            // 设置播放地址
            videoView.setVideoPath(mediaItem.getData());
        }else{
            Toast.makeText(this, "没有视频资源", Toast.LENGTH_SHORT).show();
        }
        // 设置播放按钮的状态
        setButtonState();
    }

    // TODO
    private void getData() {
        // 获取播放地址
        mediaItems = (ArrayList<MediaItem>)getIntent().getSerializableExtra("videoList");
        uri = getIntent().getData();
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData() {
        // 注册电量广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        // 电量变化的时候发送广播
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        // 得到宽和高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        // 得到音量
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(isshowMediaController){
                    hideMediaController();
                    // 把隐藏消息移除
                    handler.removeMessages(HIDE_MESSAGE);
                }else{
                    showMediaController();
                    // 发消息隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MESSAGE,3000);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullAndDefaultScreen();
                return super.onDoubleTap(e);
            }
        });
    }

    private void setFullAndDefaultScreen() {
        if(isFullScreen){
            // 默认
            setVideoType(DEFAULT_SCREEN);
        }else{
            // 全屏
            setVideoType(FULL_SCREEN);
        }
    }

    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN:
                // 设置视频画面的大小
                videoView.setVideoSize(screenWidth, screenHeight);
                // 设置按钮的状态
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_default_screen_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                // 设置视频画面的大小
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                int width = screenWidth;
                int height = screenHeight;

                if(mVideoWidth * height < width * mVideoHeight){
                    width = height * mVideoWidth / mVideoHeight;
                }else if(mVideoWidth * height > width * mVideoHeight){
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width, height);
                // 设置按钮的状态
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_full_screen_selector);
                isFullScreen = false;
                break;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            // 设置电量
            setBattery(level);
        }
    }

    private void setBattery(int level){
        if(level <= 0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level <= 10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level <=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level <= 40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level <= 60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level <= 80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        // 准备好了的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());
        // 播放出错的监听
        videoView.setOnErrorListener(new MyOnErrorListener());
        // 播放完成的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        // 设置SeekBar状态变化监听
        videoSeekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        voiceSeekBar.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
        if(isUseSystem){
            // 监听视频播放卡, 17版本以上, 系统api
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                videoView.setOnInfoListener(new MyOnInfoListener());
            }
        }
    }

    private void findViews() {
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_vitamio_video_player);
        mediaControllerTop = findViewById(R.id.media_controller_top);
        tvName = findViewById(R.id.tv_name);
        ivBattery = findViewById(R.id.iv_battery);
        tvSystemTime = findViewById(R.id.tv_system_time);
        btnVoice = findViewById(R.id.btn_voice);
        voiceSeekBar = findViewById(R.id.voice_seekBar);
        btnSwitchPlayer = findViewById(R.id.btn_switch_player);
        mediaControllerBottom = findViewById(R.id.media_controller_bottom);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        videoSeekBar = findViewById(R.id.video_seekBar);
        tvDuration = findViewById(R.id.tv_duration);
        btnExit = findViewById(R.id.btn_exit);
        btnVideoPre = findViewById(R.id.btn_video_pre);
        btnVideoStartPause = findViewById(R.id.btn_video_start_pause);
        btnVideoNext = findViewById(R.id.btn_video_next);
        btnVideoSwitchScreen = findViewById(R.id.btn_video_switch_screen);
        videoView = findViewById(R.id.videoView);
        tvNetspeed = findViewById(R.id.tv_netspeed);
        videoBuffer = findViewById(R.id.video_buffer);
        tvLoadingNetspeed = findViewById(R.id.tv_loading_netspeed);
        loading = findViewById(R.id.loading);

        //tvName.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvName.setSelected(true);
        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwitchScreen.setOnClickListener(this);

        // 音量与SeekBar关联
        voiceSeekBar.setMax(maxVoice);
        // 设置当前进度
        voiceSeekBar.setProgress(currentVoice);

        // 更新网络速度
        handler.sendEmptyMessage(SHOW_SPEED);

    }

    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            isMute = !isMute;
            updateVoice(currentVoice, isMute);
        } else if ( v == btnSwitchPlayer ) {
            showSwitchDialog();
        } else if ( v == btnExit ) {
            finish();
        } else if ( v == btnVideoPre ) {
            playVideoPre();
        } else if ( v == btnVideoStartPause ) {
            startAndPause();
        } else if ( v == btnVideoNext ) {
            playVideoNext();
        } else if ( v == btnVideoSwitchScreen ) {
            setFullAndDefaultScreen();
        }
        handler.removeMessages(HIDE_MESSAGE);
        handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
    }

    private void showSwitchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("如果画面不清楚，请切换系统播放器尝试!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSystemPlay();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void startSystemPlay() {
        if(videoView != null){
            videoView.stopPlayback();
        }

        Intent intent = new Intent(this, SystemVideoPlayer.class);
        if(mediaItems != null && mediaItems.size() > 0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoList", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
        }else if(uri != null){
            intent.setData(uri);
        }
        startActivity(intent);
        finish();
    }

    private void startAndPause() {
        if(videoView.isPlaying()){
            // 播放 -> 暂停
            videoView.pause();
            // 按钮状态设置为播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        }else{
            // 暂停 -> 播放
            videoView.start();
            // 暂停状态
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private void playVideoPre() {
        if(mediaItems != null && mediaItems.size() > 0){
            // 播放上一个
            position--;
            if (position >= 0){
                loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = MyUtil.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                // 设置按钮状态
                setButtonState();
            }
        }else if(uri != null){
            // 上一个和下一个按钮设置成不可点击
            setButtonState();
        }
    }

    private void playVideoNext() {
        if(mediaItems != null && mediaItems.size() > 0){
            // 播放下一个
            position++;
            if (position < mediaItems.size()){
                loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = MyUtil.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
                // 设置按钮状态
                setButtonState();
            }
        }else if(uri != null){
            // 上一个和下一个按钮设置成不可点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if(mediaItems != null && mediaItems.size() > 0){
            if(mediaItems.size() == 1){
                // 两个按钮设置为灰色，不可点击
                setEnableFalse(false);
            }else if(mediaItems.size() == 2){
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }
            }else{
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                }else if(position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }else{
                    setEnableFalse(true);
                }
            }
        }else if(uri != null){
            setEnableFalse(false);
        }
    }

    private void setEnableFalse(boolean flag) {
        if (!flag){
            // 两个按钮设置为灰色，不可点击
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(flag);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(flag);
        }else{
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(flag);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(flag);
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    // 得到当前的视频播放进程
                    int currentPosition = (int) videoView.getCurrentPosition();
                    // 设置当前进度
                    videoSeekBar.setProgress(currentPosition);
                    // 更新文本播放进度
                    tvCurrentTime.setText(MyUtil.stringForTime(currentPosition));
                    // 更新系统的时间
                    tvSystemTime.setText(getSystemTime());
                    // 网络视频的缓冲进度
                    if(isNetUri){
                        int buffer = videoView.getBufferPercentage();
                        int totalBuffer = buffer * videoSeekBar.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        videoSeekBar.setSecondaryProgress(secondaryProgress);
                    }else{
                        videoSeekBar.setSecondaryProgress(0);
                    }
                    // 自定义监听卡
                    if(!isUseSystem && videoView.isPlaying()){
                        if(videoView.isPlaying()){
                            int buffer = currentPosition - precurrentPosition;
                            if(buffer < 500){
                                // 视频卡了
                                videoBuffer.setVisibility(View.VISIBLE);
                            }else{
                                videoBuffer.setVisibility(View.GONE);
                            }
                        }else{
                            videoBuffer.setVisibility(View.GONE);
                        }
                    }
                    precurrentPosition = currentPosition;
                    // 每秒更新
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                case HIDE_MESSAGE:
                    hideMediaController();
                    break;
                case SHOW_SPEED:
                    // 得到网速
                    String netSpeed = MyUtil.getNetSpeed(VitamioVideoPlayer.this);
                    // 显示网速
                    tvNetspeed.setText("视频正在缓冲..." + netSpeed);
                    tvLoadingNetspeed.setText("视频正在缓冲..." + netSpeed);
                    // 每隔两秒更新
                    removeMessages(SHOW_SPEED);
                    sendEmptyMessageDelayed(PROGRESS, 2000);
                    break;
                default:
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{
        // 当解码准备完成
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 开始播放
            videoView.start();
            // 视频总时长
            int duration = (int) videoView.getDuration();
            videoSeekBar.setMax(duration);
            // 设置文本总时长
            tvDuration.setText(MyUtil.stringForTime(duration));
            // 隐藏控制面板
            hideMediaController();
            // 发消息
            handler.sendEmptyMessage(PROGRESS);

            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            // 默认屏幕
            setVideoType(DEFAULT_SCREEN);
            // 加载页面消失
            loading.setVisibility(View.GONE);
        }
    }

    private class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // 播放的视频格式不支持
            showErrorDialog();
            // 播放网络视频时, 网络中断
            
            // 播放的时候本地文件中间有空白

            return true;
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("无法播放该视频!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // 播放下一个视频
            playVideoNext();
        }
    }

    private class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * 手指滑动时
         * @param seekBar
         * @param progress
         * @param fromUser 用户引起 true
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                videoView.seekTo(progress);
            }
        }

        /**
         * 手指触碰时，回调该方法
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MESSAGE);
        }

        /**
         * 手指离开时
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 移除所有消息
        handler.removeCallbacksAndMessages(null);
        // 取消广播注册
        if(receiver != null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    /**
     * 监听物理键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice--;
            updateVoice(currentVoice, false);
            handler.removeMessages(HIDE_MESSAGE);
            handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice++;
            updateVoice(currentVoice, false);
            handler.removeMessages(HIDE_MESSAGE);
            handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 把事件传递给手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()){
            // 手指按下
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                mVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRange = Math.min(screenHeight, screenWidth);
                handler.removeMessages(HIDE_MESSAGE);
                break;
            // 手指离开
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
                break;
            // 手指滑动
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distanceY = startY - endY;
                float delta = (distanceY / touchRange) * maxVoice;
                int voice = (int)Math.min(Math.max(mVoice+delta, 0), maxVoice);
                if(delta != 0){
                    isMute = false;
                    updateVoice(voice, isMute);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     *  显示控制面板
     */
    private void showMediaController(){
        mediaControllerTop.setVisibility(View.VISIBLE);
        mediaControllerBottom.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController(){
        mediaControllerTop.setVisibility(View.GONE);
        mediaControllerBottom.setVisibility(View.GONE);
        isshowMediaController = false;
    }

    private class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                if (progress > 0){
                    isMute = false;
                }else{
                    isMute = true;
                }
                updateVoice(progress, isMute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MESSAGE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
        }
    }

    /**
     * 更新音量
     * @param progress
     */
    private void updateVoice(int progress, boolean isMute) {
        if(isMute){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            voiceSeekBar.setProgress(0);
        }else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            voiceSeekBar.setProgress(progress);
            currentVoice = progress;
        }
    }

    /**
     * 监听视频播放卡
     */
    private class MyOnInfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    videoBuffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    videoBuffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }
}
