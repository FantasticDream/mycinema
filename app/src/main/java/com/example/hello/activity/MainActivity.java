package com.example.hello.activity;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hello.R;
import com.example.hello.pager.AudioPager;
import com.example.hello.pager.BasePager;
import com.example.hello.pager.MyFragment;
import com.example.hello.pager.NetAudioPager;
import com.example.hello.pager.NetVideoPager;
import com.example.hello.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_bottom_tag;

    // 页面集合
    private ArrayList<BasePager> basePagers;

    // 选中的位置
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 隐藏默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));

        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        // 选中首页
        rg_bottom_tag.check(R.id.rb_video);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }
            // 将页面添加到Fragment
            setFragment();
        }
    }

    private void setFragment() {
        // 获取FragmentManager
        FragmentManager manager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction ft = manager.beginTransaction();
        // 替换
        ft.replace(R.id.fl_main_content, new MyFragment().newInstance(getBasePager()));
        // 提交
        ft.commit();
    }


    /**
     *  根据对应位置获取相应页面
     */
    private BasePager getBasePager(){
        BasePager basePager = basePagers.get(position);
        if(basePager != null && !basePager.isInitData){
            basePager.initData();
            basePager.isInitData = true;
        }
        return basePager;
    }

    private boolean isExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if (position != 0){
                position = 0;
                rg_bottom_tag.check(R.id.rb_video);
                return true;
            }else if (!isExit){
                isExit = true;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
