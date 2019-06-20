// IMusicPlayerService.aidl
package com.example.hello;

// Declare any non-default types here with import statements

interface IMusicPlayerService {


    /**
     * 根据位置获取音频
     * @param position
     */
     void openAudio(int position);

    /**
     * 播放
     */
     void start();

    /**
     * 暂停
     */
     void pause();

    /**
     * 停止
     */
     void stop();

    /**
     * 获取当前播放进度
     * @return
     */
     int getCurrentPostion();

    /**
     * 获取当前播放总时长
     * @return
     */
     long getDuration();

    /**
     * 获取艺术家
     * @return
     */
     String getArtist();

    /**
     * 当前的歌曲的名字
     * @return
     */
     String getName();

    /**
     * 歌曲播放路径
     * @return
     */
     String getAudioPath();

    /**
     * 播放下一个
     */
     void next();

    /**
     * 播放上一个
     */
     void pre();

    /**
     * 设置播放的模式
     * @param mode
     */
     void setPlayMode(int mode);

    /**
     * 获取播放的模式
     * @return
     */
     int getPlayMode();

     /**
     * 是否正在播放
     * @return
     */
     boolean isPlaying();

     /**
      * 拖动音频的进度
      * @param progress
      */
     void seekTo(int progress);
}
