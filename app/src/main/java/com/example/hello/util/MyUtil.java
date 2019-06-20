package com.example.hello.util;

import android.content.Context;
import android.net.TrafficStats;

import com.example.hello.domain.MediaItem;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class MyUtil {

    private static long lastTotalRxBytes = 0;
    private static long lastTimeStamp = 0;

    public static String stringForTime(long duration){
       long totalSeconds = duration / 1000;
       long hours = totalSeconds / 3600;
       long minutes = totalSeconds % 3600 / 60;
       long seconds = totalSeconds % 3600 % 60;
       StringBuilder builder = new StringBuilder();
       Formatter formatter = new Formatter(builder, Locale.getDefault());
       builder.setLength(0);
       if(hours > 0){
           return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
       }else{
           return formatter.format("%02d:%02d", minutes, seconds).toString();
       }
    }

    /**
     * 判断是否是网络资源
     * @param uri
     * @return
     */
    public static boolean isNetUri(String uri){
        boolean result = false;
        if(uri != null){
            if(uri.toLowerCase().startsWith("http") || uri.toLowerCase().startsWith("mms")){
                result = true;
            }
        }
        return result;
    }

    /**
     * 得到网络速度
     * 每隔两秒调用一次
     * @param context
     * @return
     */
    public static String getNetSpeed(Context context) {
        String netSpeed = "0 kb/s";
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        netSpeed  = String.valueOf(speed) + " kb/s";
        return  netSpeed;
    }

    public static ArrayList<MediaItem> mediaItems;

}
