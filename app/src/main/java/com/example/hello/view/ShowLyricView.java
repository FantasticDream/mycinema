package com.example.hello.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.hello.domain.Lyric;
import com.example.hello.util.DensityUtil;

import java.util.ArrayList;

public class ShowLyricView extends AppCompatTextView {

    // 歌词列表
    private ArrayList<Lyric> lyrics;
    private Paint paint;
    private Paint paint2;

    private int width;
    private int height;

    // 歌词列表中索引
    private int index;
    // 每行的高
    private float textHeight;
    // 当前播放
    private float currentPosition;
    private float sleepTime;
    private float timePoint;

    /**
     * 设置歌词列表
     * @param lyrics
     */
    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView(Context context) {
        textHeight = DensityUtil.dip2px(context, 18);
        // 创建画笔
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(DensityUtil.dip2px(context, 16));
        paint.setAntiAlias(true);
        // 设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);

        paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        paint2.setTextSize(DensityUtil.dip2px(context, 16));
        paint2.setAntiAlias(true);
        // 设置居中对齐
        paint2.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics != null && lyrics.size() > 0){

            // 通过平移动画实现歌词上移
            float plush;
            if (sleepTime == 0){
               plush = 0F;
            }else{
                plush = textHeight + ((currentPosition - timePoint)/sleepTime) * textHeight;
            }
            canvas.translate(0, -plush);
            // 绘制歌词
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText, width/2, height/2, paint);

            float tempY = height/2;
            for(int i = index - 1; i >= 0; i--){
                String preText = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if(tempY < 0){
                    break;
                }
                canvas.drawText(preText, width/2, tempY, paint2);
            }

            tempY = height/2;
            for(int i = index + 1; i < lyrics.size(); i++){
                String nextText = lyrics.get(i).getContent();
                tempY = tempY + textHeight;
                if(tempY > height){
                    break;
                }
                canvas.drawText(nextText, width/2, tempY, paint2);
            }

        }else{
            // 没有歌词
            canvas.drawText("没有歌词", width/2, height/2, paint);
        }
    }

    public void setShowLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if(lyrics == null || lyrics.size() == 0){
            return;
        }
        for (int i = 1; i < lyrics.size(); i++){
            if (currentPosition < lyrics.get(i).getTimePoint()){
                int tempIndex = i - 1;
                if(currentPosition >= lyrics.get(tempIndex).getTimePoint()){
                    // 当前正在播放的哪句歌词
                    index = tempIndex;
                    sleepTime = lyrics.get(index).getSleepTime();
                    timePoint = lyrics.get(index).getTimePoint();
                }
            }
        }
        // 重新绘制, 主线程
        invalidate();
    }
}
