package com.cj.baidunavi.ui;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 大头 on 2020/5/6.
 */

public abstract class AfTimer
{
    private Timer timer;
    private TimerTask timerTask = new MyTimerTask();
    private Handler handler = new MyHandler();

    // 子类需重写此方法, 此方法在线程里调用
    protected abstract Object doInBackground();

    // 子类需重写此方法, 此方法里Handler里调用，用于更新UI
    // 此方法需要尽快完成, 否则会引起界面卡顿
    protected abstract void onPostExecute(Object result);

    public AfTimer()
    {
    }

    public void schedule(int interval)
    {
        if(timer !=null) return;
        timer = new Timer();
        timer.schedule(timerTask, interval, interval);
    }

    public void cancel()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            Object result = doInBackground();

            // 在定时器回调里也不能更新UI，需要发消息给Handler
            Message msg = new Message();
            msg.what = 1; // 消息类型
            msg.obj = result;
            handler.sendMessage(msg);
        }
    }

    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            // 在Handler里更新 UI
            Object result = msg.obj;
            onPostExecute(result);
        }
    }
}
