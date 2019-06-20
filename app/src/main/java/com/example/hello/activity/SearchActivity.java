package com.example.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.example.hello.adapter.SearchAdapter;
import com.example.hello.domain.MediaItem;
import com.example.hello.domain.SearchBean;
import com.example.hello.domain.SearchResultBean;
import com.example.hello.util.JsonParser;
import com.example.hello.util.NetVideoAddress;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class SearchActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "SearchActivity";

    private EditText etInput;
    private ImageView ivVoice;
    private TextView search;
    private ListView searchListView;
    private ProgressBar searchProgressBar;
    private TextView noData;

    private SearchAdapter adapter;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private List<SearchBean.ResultBean.MvsBean> items;

    private String mvRealUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
    }


    private void findViews() {
        setContentView(R.layout.activity_search);
        etInput = findViewById(R.id.et_input);
        ivVoice = findViewById(R.id.iv_voice);
        search = findViewById(R.id.search);
        searchListView = findViewById(R.id.search_listView);
        searchProgressBar = findViewById(R.id.search_progressBar);
        noData = findViewById(R.id.noData);

        ivVoice.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_voice:
                voiceInput();
                break;
            case R.id.search:
                searchText();
                break;
        }
    }

    private void searchText() {
        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {

            if(items != null && items.size() >0){
                items.clear();
            }

            try {
                text = URLEncoder.encode(text, "UTF-8");
                String url = NetVideoAddress.SEARCH_URL + text;
                getDataFromNet(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataFromNet(String url) {
        searchProgressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                searchProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                searchProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getData2FromNet(String url) {

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                process2Data(result);
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

    private void processData(String result) {
        SearchBean searchBean = parsedJson(result);
        items = searchBean.getResult().getMvs();
        showData();
    }

    private void process2Data(String result) {
        SearchResultBean searchBean = parsed2Json(result);
        if (searchBean.getData().getBrs().get_$1080() != null){
            mvRealUrl = searchBean.getData().getBrs().get_$1080();
        }else if(searchBean.getData().getBrs().get_$720() != null){
            mvRealUrl = searchBean.getData().getBrs().get_$720();
        }else if (searchBean.getData().getBrs().get_$480() != null){
            mvRealUrl = searchBean.getData().getBrs().get_$480();
        }else{
            mvRealUrl =  searchBean.getData().getBrs().get_$240();
        }
    }

    private void showData() {
        if(items != null && items.size() >0){
            //设置适配器
            adapter = new SearchAdapter(this, R.layout.item_search_pager, items);
            searchListView.setAdapter(adapter);
            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchBean.ResultBean.MvsBean mvsBean = items.get(position);
                    int mvId = mvsBean.getId();
                    String mvUrl = NetVideoAddress.SEARCHRESULT + mvId;
                    getData2FromNet(mvUrl);
                    ArrayList<MediaItem> mediaItems = new ArrayList<>();
                    MediaItem mediaItem = new MediaItem();
                    if(mvRealUrl != null){
                        mediaItem.setData(mvRealUrl);
                        mediaItem.setDuration(mvsBean.getDuration());
                        mediaItem.setName(mvsBean.getName());
                        mediaItem.setArtist(mvsBean.getArtistName());
                        mediaItems.add(mediaItem);
                        // 调用自己播放器, 传递列表数据
                        Intent intent = new Intent(SearchActivity.this, VitamioVideoPlayer.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("videoList", mediaItems);
                        intent.putExtras(bundle);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                }
            });
            noData.setVisibility(View.GONE);
        }else{
            noData.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }

        searchProgressBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     * @param result
     * @return
     *
     */
    private SearchBean parsedJson(String result) {
        return new Gson().fromJson(result, SearchBean.class);
    }

    private SearchResultBean parsed2Json(String result){
        return new Gson().fromJson(result, SearchResultBean.class);
    }

    private void voiceInput() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param recognizerResult
         * @param b  是否说话结束
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
            String text = JsonParser.parseIatResult(result);
            //解析好的

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//拼成一句
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            etInput.setText(resultBuffer.toString());
            etInput.setSelection(etInput.length());

        }

        /**
         * 出错了
         *
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
