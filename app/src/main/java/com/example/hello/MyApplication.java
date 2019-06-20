package com.example.hello;

import android.app.Application;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.x;

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b926351");

    }

}