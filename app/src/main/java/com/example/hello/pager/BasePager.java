package com.example.hello.pager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BasePager extends AppCompatActivity{

    public final Context context;

    public View rootView;
    public boolean isInitData = false;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 有子类实现特定效果
     * @return
     */
    public abstract View initView();

    /**
     * 页面初始化数据
     */
    public void initData(){

    }

}
