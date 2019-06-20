package com.example.hello.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hello.R;
import com.example.hello.adapter.NetAudioPagerAdapter;
import com.example.hello.domain.NetAudioData;
import com.example.hello.util.CacheUtil;
import com.example.hello.util.NetVideoAddress;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class NetAudioPager extends BasePager {

    @ViewInject(R.id.netaudio_listView)
    private ListView listView;

    @ViewInject(R.id.tv_nonetaudio)
    private TextView nonet;

    @ViewInject(R.id.netaudio_loading)
    private ProgressBar loading;

    // 页面数据
    private List<NetAudioData.ListBean> datas;

    private NetAudioPagerAdapter adapter;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netaudio_pager, null);
        // 视图与类绑定
        x.view().inject(NetAudioPager.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        // 缓存
        String saveData = CacheUtil.getString(context, "MyNetAudio");
        if (!TextUtils.isEmpty(saveData)){
            // 解析数据
            processData(saveData);
        }
        getDataFromNet();
    }


    /**
     * 解析数据
     * @param saveData
     */
    private void processData(String saveData) {
        NetAudioData data = parseJson(saveData);
        datas = data.getList();
        if (datas != null && datas.size() > 0){
            nonet.setVisibility(View.GONE);
            // 设置适配器
            adapter = new NetAudioPagerAdapter(context, datas);
            listView.setAdapter(adapter);
        }else{
            nonet.setText("没有数据");
            nonet.setVisibility(View.VISIBLE);
        }
        loading.setVisibility(View.GONE);
    }

    private NetAudioData parseJson(String saveData) {
        return new Gson().fromJson(saveData, NetAudioData.class);
    }

    private void getDataFromNet() {

        RequestParams params = new RequestParams(NetVideoAddress.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 保存数据
                CacheUtil.putString(context, "MyNetAudio", result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
