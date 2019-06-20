package com.example.hello.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.example.hello.domain.MediaItem;
import com.example.hello.util.MyUtil;

import java.util.ArrayList;

public class VideoPagerAdapter extends BaseAdapter {

    private final boolean isVideo;
    private Context context;
    private ArrayList<MediaItem> mediaItems;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems, boolean isVideo){
        this.context = context;
        this.mediaItems = mediaItems;
        this.isVideo = isVideo;
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
            convertView = View.inflate(context, R.layout.item_video_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_video_icon = convertView.findViewById(R.id.iv_video_icon);
            viewHolder.tv_video_name = convertView.findViewById(R.id.tv_video_name);
            viewHolder.tv_video_duration = convertView.findViewById(R.id.tv_video_duration);
            viewHolder.tv_video_size = convertView.findViewById(R.id.tv_video_size);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // 根据position获得数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_video_name.setText(mediaItem.getName());
        viewHolder.tv_video_duration.setText(MyUtil.stringForTime(mediaItem.getDuration()));
        viewHolder.tv_video_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));

        if(!isVideo){
            // 设置音频的图片
            viewHolder.iv_video_icon.setImageResource(R.drawable.music_default_bg);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_video_icon;
        TextView tv_video_name;
        TextView tv_video_duration;
        TextView tv_video_size;
    }
}
