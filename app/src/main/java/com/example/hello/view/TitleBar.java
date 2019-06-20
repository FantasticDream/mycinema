package com.example.hello.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hello.R;
import com.example.hello.activity.SearchActivity;

public class TitleBar extends LinearLayout implements View.OnClickListener{

    private View tv_search;
    private Context context;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 当布局文件加载完成的时候回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search = getChildAt(0);
        tv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索
            case R.id.tv_search:
                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
