package com.example.hello.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.example.hello.IMusicPlayerService;
import com.example.hello.R;
import com.example.hello.activity.AudioPlayer;
import com.example.hello.domain.MediaItem;
import com.example.hello.util.CacheUtil;
import com.example.hello.util.MyUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 创建服务类
 */
public class MusicPlayerService extends Service {

    public static final String OPENAUDIO = "com.example.hello_OPENAUDIO";
    private ArrayList<MediaItem> mediaItems;
    private int position;
    private MediaItem mediaItem;
    // 播放音乐
    private MediaPlayer mediaPlayer;

    private NotificationManager manager;
    // 顺序播放
    public static final int REPEAT_NORMAL = 1;
    // 单曲循环
    public static final int REPEAT_SINGLE = 2;
    // 全部循环
    public static final int REPEAT_ALL = 3;

    private int mode = REPEAT_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取模式
        mode = CacheUtil.getPlaymode(this, "playmode");
       // 加载本地音乐列表
        mediaItems = MyUtil.mediaItems;
    }

    private IMusicPlayerService.Stub sub = new IMusicPlayerService.Stub() {

        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPostion() throws RemoteException {
            return service.getCurrentPostion();
        }

        @Override
        public long getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            mediaPlayer.seekTo(progress);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return sub;
    }


    /**
     * 根据位置获取音频
     * @param position
     */
    private void openAudio(int position){
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0){
            mediaItem = mediaItems.get(position);
            if(mediaPlayer != null){
                mediaPlayer.reset();
            }
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();

                if (mode == MusicPlayerService.REPEAT_SINGLE){
                    // 循环
                    mediaPlayer.setLooping(true);
                }else{
                    mediaPlayer.setLooping(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放
     */
    private void start(){
        mediaPlayer.start();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayer.class);
        intent.putExtra("Notification", true);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notification_music_playing)
                    .setContentTitle("我的音乐")
                    .setContentText("正在播放: " + getName())
                    .setContentIntent(pendingIntent)
                    .build();
        }
        manager.notify(1, notification);
    }

    /**
     * 暂停
     */
    private void pause(){
        mediaPlayer.pause();
        manager.cancel(1);
    }

    /**
     * 停止
     */
    private void stop(){

    }

    /**
     * 获取当前播放进度
     * @return
     */
    private int getCurrentPostion(){
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获取当前播放总时长
     * @return
     */
    private long getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 获取艺术家
     * @return
     */
    private String getArtist(){
        return mediaItem.getArtist();
    }

    /**
     * 当前的歌曲的名字
     * @return
     */
    private String getName(){
        return mediaItem.getName();
    }

    /**
     * 歌曲播放路径
     * @return
     */
    private String getAudioPath(){
        return mediaItem.getData();
    }

    /**
     * 播放下一个
     */
    private void next(){
        // 根据当前的播放模式，设置下一个的位置
        setNextPosition();
        // 根据当前的播放模式和位置去播放音频
        openNextAudio();
    }

    private void openNextAudio() {

        int mode = getPlayMode();
        if(mode == MusicPlayerService.REPEAT_NORMAL){
            if (position < mediaItems.size()){
                openAudio(position);
            }else{
                position = mediaItems.size() - 1;
            }
        }else if(mode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(mode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if (position < mediaItems.size()){
                openAudio(position);
            }else{
                position = mediaItems.size() - 1;
            }
        }
    }

    private void setNextPosition() {
        int mode = getPlayMode();
        if(mode == MusicPlayerService.REPEAT_NORMAL){
            position++;
        }else if(mode == MusicPlayerService.REPEAT_SINGLE){
            position++;
            if(position >= mediaItems.size()){
                position = 0;
            }
        }else if(mode == MusicPlayerService.REPEAT_ALL){
            position++;
            if(position >= mediaItems.size()){
                position = 0;
            }
        }else{
            position++;
        }
    }

    /**
     * 播放上一个
     */
    private void pre(){
        setPrePosition();
        openPreAudio();
    }

    private void openPreAudio() {
        int mode = getPlayMode();
        if(mode == MusicPlayerService.REPEAT_NORMAL){
            if (position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }else if(mode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(mode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if (position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }
    }

    private void setPrePosition() {
        int mode = getPlayMode();
        if(mode == MusicPlayerService.REPEAT_NORMAL){
            position--;
        }else if(mode == MusicPlayerService.REPEAT_SINGLE){
            position--;
            if(position < 0){
                position = mediaItems.size() - 1;
            }
        }else if(mode == MusicPlayerService.REPEAT_ALL){
            position--;
            if(position < 0){
                position = mediaItems.size() - 1;
            }
        }else{
            position--;
        }
    }

    /**
     * 设置播放的模式
     * @param mode
     */
    private void setPlayMode(int mode){
        this.mode = mode;
        CacheUtil.putPlaymode(this, "playmode", mode);
        if (mode == MusicPlayerService.REPEAT_SINGLE){
            // 循环
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.setLooping(false);
        }
    }

    /**
     * 获取播放的模式
     * @return
     */
    private int getPlayMode(){
        return mode;
    }

    private class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 通知Activity获取信息, 发广播
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    /**
     * 根据动作发广播
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    private class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

}
