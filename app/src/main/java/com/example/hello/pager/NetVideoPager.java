package com.example.hello.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hello.R;
import com.example.hello.activity.SystemVideoPlayer;
import com.example.hello.adapter.NetVideoPagerAdapter;
import com.example.hello.domain.MediaItem;
import com.example.hello.util.CacheUtil;
import com.example.hello.util.NetVideoAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class NetVideoPager extends BasePager {

    @ViewInject(R.id.net_listView)
    private ListView listView;

    @ViewInject(R.id.tv_nonetmedia)
    private TextView tvNonetmedia;

    @ViewInject(R.id.net_pb_loading)
    private ProgressBar netLoading;

    // 装取数据集合
    private ArrayList<MediaItem> mediaItems;
    // 适配器
    private NetVideoPagerAdapter adapter;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netvideo_pager, null);
        x.view().inject(this, view);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 调用自己播放器, 传递列表数据
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoList", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        String saveJson = CacheUtil.getString(context,NetVideoAddress.NET_ADDRESS);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(NetVideoAddress.NET_ADDRESS);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtil.putString(context,NetVideoAddress.NET_ADDRESS,result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                netLoading.setVisibility(View.GONE);
                showData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 处理数据
     * @param json
     */
    private void processData(String json) {
        // 解析json格式的数据
        mediaItems = parseJson(json);
        showData();

    }

    private void showData() {
        // 设置适配器
        if (mediaItems != null && mediaItems.size() > 0){
            // 设置适配器，隐藏文本
            adapter = new NetVideoPagerAdapter(context, mediaItems);
            listView.setAdapter(adapter);
            tvNonetmedia.setVisibility(View.GONE);
        }else{
            // 显示文本
            tvNonetmedia.setVisibility(View.VISIBLE);
        }
        netLoading.setVisibility(View.GONE);
    }

    private ArrayList<MediaItem> parseJson(String json) {

        ArrayList<MediaItem> m = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            // optJSONArray不会导致崩溃
            JSONArray array = jsonObject.optJSONArray("trailers");
            if (array != null && array.length() > 0){
                for(int i = 0; i < array.length(); i++){
                    JSONObject jsonItem = (JSONObject) array.get(i);
                    if(jsonItem != null){
                        MediaItem mediaItem = new MediaItem();
                        String movieName = jsonItem.optString("movieName");
                        String videoTitle = jsonItem.optString("videoTitle");
                        String imgUrl = jsonItem.optString("coverImg");
                        String url = jsonItem.optString("hightUrl");
                        mediaItem.setName(movieName);
                        mediaItem.setDesc(videoTitle);
                        mediaItem.setImgUrl(imgUrl);
                        mediaItem.setData(url);
                        m.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }
}
