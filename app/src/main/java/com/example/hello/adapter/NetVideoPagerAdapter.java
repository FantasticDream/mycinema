package com.example.hello.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.example.hello.domain.MediaItem;

import org.xutils.x;

import java.util.ArrayList;

public class NetVideoPagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mediaItems;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
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
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_netvideo_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_neticon = convertView.findViewById(R.id.iv_neticon);
            viewHolder.tv_netname = convertView.findViewById(R.id.tv_netname);
            viewHolder.tv_netdesc = convertView.findViewById(R.id.tv_netdesc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // 根据position获得数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_netname.setText(mediaItem.getName());
        viewHolder.tv_netdesc.setText(mediaItem.getDesc());
        x.image().bind(viewHolder.iv_neticon, mediaItem.getImgUrl());
        return convertView;
    }

    class ViewHolder{
        ImageView iv_neticon;
        TextView tv_netname;
        TextView tv_netdesc;
    }
}

