package com.example.hello.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.IMusicPlayerService;
import com.example.hello.R;
import com.example.hello.domain.MediaItem;
import com.example.hello.service.MusicPlayerService;
import com.example.hello.util.LyricUtil;
import com.example.hello.util.MyUtil;
import com.example.hello.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;


public class AudioPlayer extends Activity implements View.OnClickListener{

    private static final String TAG = "AudioPlayer";
    private static final int PROGRESS = 1;
    private static final int SHOW_LYRIC = 2;
    private boolean notification;
    private TextView tvAudioArtist;
    private TextView tvAudioName;
    private TextView audioTime;
    private SeekBar audioSeekBar;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnAudioLyric;
    private int position;
    private IMusicPlayerService iMusicPlayerService;
    private ShowLyricView showLyricView;
    private ServiceConnection conn = new ServiceConnection() {

        /**
         * 连接成功时回调该方法
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicPlayerService = IMusicPlayerService.Stub.asInterface(service);
            if(iMusicPlayerService != null){
                try {
                    if(!notification){
                        iMusicPlayerService.openAudio(position);
                    }else{
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 断开连接时回调该方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (iMusicPlayerService != null){
                try {
                    iMusicPlayerService.stop();
                    iMusicPlayerService = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void findViews() {
        setContentView(R.layout.activity_audioplayer);
        tvAudioArtist = findViewById(R.id.tv_audio_artist);
        tvAudioName = findViewById(R.id.tv_audio_name);
        audioTime = findViewById(R.id.audio_time);
        audioSeekBar = findViewById(R.id.audio_seekBar);
        btnAudioPlaymode = findViewById(R.id.btn_audio_playmode);
        btnAudioPre = findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = findViewById(R.id.btn_audio_next);
        btnAudioLyric = findViewById(R.id.btn_audio_lyric);
        showLyricView = findViewById(R.id.showLyricView);

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnAudioLyric.setOnClickListener(this);
        // 设置音频的拖动
        audioSeekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            setPlayMode();
        } else if (v == btnAudioPre) {
            if(iMusicPlayerService != null){
                try {
                    iMusicPlayerService.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioStartPause) {
            if(iMusicPlayerService != null){
                try {
                    if(iMusicPlayerService.isPlaying()){
                        // 暂停， 按钮设置为播放
                        iMusicPlayerService.pause();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    }else{
                        iMusicPlayerService.start();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioNext) {
            if(iMusicPlayerService != null){
                try {
                    iMusicPlayerService.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        } else if (v == btnAudioLyric) {

        }
    }

    private void setPlayMode() {
        try {
            int mode = iMusicPlayerService.getPlayMode();
            if(mode == MusicPlayerService.REPEAT_NORMAL){
                mode = MusicPlayerService.REPEAT_SINGLE;
            }else if(mode == MusicPlayerService.REPEAT_SINGLE){
                mode = MusicPlayerService.REPEAT_ALL;
            }else if(mode == MusicPlayerService.REPEAT_ALL){
                mode = MusicPlayerService.REPEAT_NORMAL;
            }else{
                mode = MusicPlayerService.REPEAT_NORMAL;
            }
            iMusicPlayerService.setPlayMode(mode);
            // 设置图片
            showPlaymode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showPlaymode() {
        try {
            int mode = iMusicPlayerService.getPlayMode();
            if(mode == MusicPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            }else if(mode == MusicPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
            }else if(mode == MusicPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(this, "全部播放", Toast.LENGTH_SHORT).show();
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkPlaymode() {
        try {
            int mode = iMusicPlayerService.getPlayMode();
            if(mode == MusicPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }else if(mode == MusicPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            }else if(mode == MusicPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }
            // 校验播放状态
            if (iMusicPlayerService.isPlaying()){
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
            }else{
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    try {
                        // 得到当前进度
                        int currentPositon = iMusicPlayerService.getCurrentPostion();
                        // 设置SeekBar进度
                        audioSeekBar.setProgress(currentPositon);
                        // 时间进度更新
                        audioTime.setText(MyUtil.stringForTime(currentPositon)
                                + "/" + MyUtil.stringForTime(iMusicPlayerService.getDuration()));
                        // 每秒更新
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOW_LYRIC:
                    // 得到当前进度
                    try{
                        int currentPosition = iMusicPlayerService.getCurrentPostion();
                        // 把进度传入ShowLyricView控件
                        showLyricView.setShowLyric(currentPosition);
                        // 实时发消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        bindAndStartService();
    }

    private void initData() {
        // 注册广播
        EventBus.getDefault().register(this);
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.example.hello_OPENAUDIO");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 获取数据
     */
    private void getData() {
        notification = getIntent().getBooleanExtra("Notification", false);
        if(!notification){
            position = getIntent().getIntExtra("audioPosition", 0);
        }
    }

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            showData(null);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    // public 权限
    public void showData(MediaItem mediaItem) {
        // 发消息，开始歌词同步
        showLyric();
        showViewData();
        checkPlaymode();
    }

    private void showLyric() {
        boolean isExistsLyric = LyricUtil.isExistsLyric();
        // 传歌词
        try {
            String path = iMusicPlayerService.getAudioPath();
            path = path.substring(0, path.lastIndexOf("."));
            LyricUtil.readLyricFile(new File(path+ ".lrc"));
            showLyricView.setLyrics(LyricUtil.getLyrics());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (isExistsLyric){
            handler.sendEmptyMessage(SHOW_LYRIC);

        }
    }

    private void showViewData() {
        try {
            tvAudioArtist.setText(iMusicPlayerService.getArtist());
            tvAudioName.setText(iMusicPlayerService.getName());
            audioSeekBar.setMax((int) iMusicPlayerService.getDuration());

            // 发消息
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        // 移除消息
        handler.removeCallbacksAndMessages(null);
        // 取消注册
        EventBus.getDefault().unregister(this);
        // 解绑服务
        if(conn != null){
            unbindService(conn);
            conn = null;
        }
        super.onDestroy();
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    iMusicPlayerService.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
