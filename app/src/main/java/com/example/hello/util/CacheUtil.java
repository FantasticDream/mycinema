package com.example.hello.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hello.service.MusicPlayerService;

public class CacheUtil {

    /**
     * 视频缓存
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context, String key, String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("keysky",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();
    }

    /**
     * 视频缓存
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("keysky",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");
    }

    /**
     * 音频模式
     * @param context
     * @param key
     * @param values
     */
    public static void putPlaymode(Context context, String key, int values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("keysky",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, values).commit();
    }

    /**
     * 音频模式
     * @param context
     * @param key
     * @return
     */
    public static int getPlaymode(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("keysky",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, MusicPlayerService.REPEAT_NORMAL);
    }
}
