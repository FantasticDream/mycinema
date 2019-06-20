package com.example.hello.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.example.hello.domain.MvsBean;
import com.example.hello.domain.SearchBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<SearchBean.ResultBean.MvsBean>{

    private static final String TAG = "SearchAdapter";

    private int resourceId;

    public SearchAdapter(Context context, int resourceId, List<SearchBean.ResultBean.MvsBean> mediaItems){
        super(context, resourceId, mediaItems);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SearchBean.ResultBean.MvsBean mediaItem = getItem(position);
        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder.iv_icon = view.findViewById(R.id.iv_neticon);
            viewHolder.tv_name = view.findViewById(R.id.tv_search_netname);
            viewHolder.tv_desc = view.findViewById(R.id.tv_search_netdesc);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_desc.setText(mediaItem.getName());
        //3.使用Picasso 请求图片
        Picasso.get().load(mediaItem.getCover())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);

        return view;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}

/*
public class SearchAdapter extends BaseAdapter {

    private static final String TAG = "SearchAdapter";

    private  Context context;
    private  List<SearchBean.ResultBean.MvsBean> mediaItems;

    public SearchAdapter(Context context, List<SearchBean.ResultBean.MvsBean> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_netvideo_pager,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_neticon);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_desc = convertView.findViewById(R.id.tv_netdesc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        Log.d(TAG, "getView: ******^^^^^^^^^^" + mediaItems.get(0).getCover());
        SearchBean.ResultBean.MvsBean mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_desc.setText(mediaItem.getDesc().toString());
        //3.使用Picasso 请求图片
        Picasso.get().load(mediaItem.getCover())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);

        return convertView;
    }


    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }

}
*/

