package com.dale.utils;

import android.os.Handler;
import android.os.SystemClock;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public abstract class DownTimerHelper implements LifecycleObserver {
    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    /**
     * 间隔时间（多久执行一次）
     */
    private long mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;
    /**
     * 总的倒计时时间
     */
    private long mTotalTime;
    private Handler mHandler = new Handler();

    public DownTimerHelper(long totalTime, long countDownInterval) {
        this.mCountdownInterval = countDownInterval;
        mMillisInFuture = totalTime;
        mTotalTime = totalTime;
    }

    /**
     * 定时返回倒计时
     *
     * @param untilFinished 时间
     */
    public abstract void onTick(long untilFinished);

    /**
     * 倒计时结束的回调
     */
    public abstract void onFinish();


    public long getTotalTime() {
        return mMillisInFuture;
    }

    /**
     * 重新设置总共要倒计时的时间
     */
    public void setTotalTime(long millisInFuture) {
        if (!mCancelled) {
            cancel();
        }
        mMillisInFuture = millisInFuture;
    }

    /**
     * 重置为初始状态重新倒计时
     */
    public void reset() {
        if (!mCancelled) {
            cancel();
        }
        mMillisInFuture = mTotalTime;
    }

    /**
     * desc: 开始
     */
    public void start(Lifecycle lifecycle) {
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        start();
    }

    public void start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.post(handleCountingDown);
    }

    /**
     * desc: 结束
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void cancel() {
        mCancelled = true;
        mHandler.removeCallbacks(handleCountingDown);
    }

    private Runnable handleCountingDown = new Runnable() {
        @Override
        public void run() {
            synchronized (DownTimerHelper.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft / mCountdownInterval);
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;
                    if (millisLeft < mCountdownInterval) {
                        delay = millisLeft - lastTickDuration;
                        if (delay < 0) {
                            delay = 0;
                        }
                    } else {
                        delay = mCountdownInterval - lastTickDuration;
                        while (delay < 0) {
                            delay += mCountdownInterval;
                        }
                    }

                    mHandler.postDelayed(this, delay);
                }
            }
        }
    };
}
