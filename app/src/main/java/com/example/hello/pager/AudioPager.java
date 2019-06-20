package com.example.hello.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.example.hello.activity.AudioPlayer;
import com.example.hello.adapter.VideoPagerAdapter;
import com.example.hello.domain.MediaItem;
import com.example.hello.util.MyUtil;

import java.util.ArrayList;

public class AudioPager extends BasePager {

    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    // 装取视频
    public ArrayList<MediaItem> mediaItems;
    // 发消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0){
                // 设置适配器，隐藏文本
                videoPagerAdapter = new VideoPagerAdapter(context, mediaItems, false);
                listView.setAdapter(videoPagerAdapter);
                tv_nomedia.setVisibility(View.GONE);
            }else{
                // 显示文本
                tv_nomedia.setVisibility(View.VISIBLE);
                tv_nomedia.setText("没有发现音频...");
            }
            // 隐藏ProgressBar
            pb_loading.setVisibility(View.GONE);
        }
    };
    private VideoPagerAdapter videoPagerAdapter;

    public AudioPager(Context context) {
        super(context);
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager, null);
        listView = view.findViewById(R.id.listView);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        // 设置item点击事件
        listView.setOnItemClickListener(new AudioPager.MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 调用自己播放器, 传递列表数据
            Intent intent = new Intent(context, AudioPlayer.class);
            intent.putExtra("audioPosition", position);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            getDataFromLocal();
        }
    }

    /**
     * 从本地获取数据, 内容提供器获取
     */
    private void getDataFromLocal() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                mediaItems = new ArrayList<>();
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] obj = {
                        // 视频名称
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        // 视频时长
                        MediaStore.Audio.Media.DURATION,
                        // 视频大小
                        MediaStore.Audio.Media.SIZE,
                        // 视频绝对地址
                        MediaStore.Audio.Media.DATA,
                        // 艺术家
                        MediaStore.Audio.Media.ARTIST
                };
                Cursor cursor = contentResolver.query(uri, obj, null, null, null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        MediaItem m = new MediaItem();
                        String name = cursor.getString(0);
                        m.setName(name);
                        long duration = cursor.getLong(1);
                        m.setDuration(duration);
                        long size = cursor.getLong(2);
                        m.setSize(size);
                        String data = cursor.getString(3);
                        m.setData(data);
                        String artist = cursor.getString(4);
                        m.setArtist(artist);
                        mediaItems.add(m);
                    }
                    MyUtil.mediaItems = mediaItems;
                    cursor.close();
                }
                // 发消息 handler
                handler.sendEmptyMessage(8);
            }
        }.start();
    }

    // 权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getDataFromLocal();
                }else{
                    Toast.makeText(context, "请授予权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
