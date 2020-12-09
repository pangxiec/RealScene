package com.cj.baidunavi.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * Created by 大头 on 2020/5/6.
 */

public class AfCarousel extends View
{
    final String TAG = "轮播图";

    Bitmap[] images;
    int curIndex = 0;// 当前图片
    MyTimer1 timer1;

    int progress = 0; // 切换效果
    MyTimer2 timer2;

    GestureDetector mGestureDetector ; // 触摸事件支持, 在构造方法里初始化
    public AfCarousel(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        // 滑动事件与点击事件处理
        mGestureDetector = new GestureDetector(this.getContext(), new MyGestureDetector());
    }

    // 设置图片
    public void setImages(Bitmap[] images)
    {
        killTimer1();

        this.images = images;
        invalidate(); // 申请重新绘制

        startTimer1();
    }

    // Timer1: 控制5秒切换一次
    private void startTimer1()
    {
        curIndex= 0;
        timer1 = new MyTimer1();
        timer1.schedule(5000);
    }

    private void killTimer1()
    {
        if(timer1 != null)
        {
            timer1.cancel();
            timer1 = null;
        }
    }

    // Timer2: 切换效果、平滑过渡
    private void startTimer2()
    {
        progress = 0;
        timer2 = new MyTimer2();
        timer2.schedule(50);
    }
    private void killTimer2()
    {
        if(timer2 != null)
        {
            timer2.cancel();
            timer2 = null;
        }
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas); // 绘制layout里的背景

        // View所占空间
        int w = getWidth();
        int h = getHeight();

        if(images== null)return;

        //Log.w(TAG, " 绘制第" + (curIndex+1) + "幅图," + progress + "/100");

        // 每次切换时，绘制2个图片，一个入、一个出
        Bitmap imageIn = images[curIndex];

        // 上一个图片
        int lastIndex = curIndex -1;
        if(lastIndex < 0) lastIndex = images.length -1;
        Bitmap imageOut = images[lastIndex];

        // 滑动方向
        boolean direction = true;

        // 并列显示2幅图 ,例如, 前者显示1/10, 后者显示9/10
        // 淡出
        if(imageOut != null)
        {
            int imgw = imageOut.getWidth();
            int imgh = imageOut.getHeight();
            Rect srcRect = new Rect(imgw*progress/100, 0, imgw, imgh);
            Rect dstRect = new Rect(0, 0, w*(100-progress)/100, h);

            canvas.drawBitmap(imageOut, srcRect, dstRect, new Paint());
        }
        // 淡入
        if(imageIn != null)
        {
            int imgw = imageIn.getWidth();
            int imgh = imageIn.getHeight();
            Rect srcRect = new Rect(0, 0, imgw*progress/100, imgh);
            Rect dstRect = new Rect(w*(100-progress)/100, 0, w, h);
            canvas.drawBitmap(imageIn, srcRect, dstRect, new Paint());
        }

        // 页面指示器
        float space = 60;
        float cx = w - space *images.length - 40;
        float cy = h - space;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for(int i=0; i< images.length; i++)
        {
            // 底色
            paint.setStyle(Paint.Style.FILL);
            if(i==curIndex)
                paint.setColor(0xEEFF0000);
            else
                paint.setColor(0xEE928399);

            canvas.drawCircle(cx + space/2, cy, 12, paint);

            // 黄色边框
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xEEFFFF00);
            paint.setStrokeWidth(2);
            canvas.drawCircle(cx + space/2, cy, 15, paint);

            cx += space;
        }

    }

    // 每5秒切换
    class MyTimer1 extends AfTimer
    {
        @Override
        protected Object doInBackground()
        {
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            if(images == null) return;

            curIndex ++;
            if(curIndex >= images.length)
                curIndex = 0;

            startTimer2();
        }
    }

    // 定时器2： 负责切换效果
    class MyTimer2 extends AfTimer
    {

        @Override
        protected Object doInBackground()
        {
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            progress += 10;
            if(progress >100)
            {
                killTimer2();
            }
            else
            {
                invalidate(); // 切换效果绘制
            }
        }
    }


    ////////// 滑动及点击事件处理 /////////
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 将MotionEvent交给 GestureDetector来处理
        return mGestureDetector.onTouchEvent(event);
    }

    private class MyGestureDetector implements GestureDetector.OnGestureListener
    {
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true; //注：返回true才能接收到滑动事件onFling
        }
        @Override
        public void onShowPress(MotionEvent e)
        {
            Log.w(TAG, "点击轮播图的某一页! 当前页码: " + curIndex);
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            Log.w(TAG, "长按事件: " + curIndex);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            // 滑动事件 (左右、上下)
            if (velocityX > 0)
            {
                Log.w(TAG, "向右滑动, 应翻到下一页!");
            }

            if (velocityX < 0)
            {
                Log.w(TAG, "向左滑动, 应翻到上一页!");
            }
            return false;
        }
    };
}
