package com.dale.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 栈顶Activity工具类
 */
public class TopActivityManager implements Application.ActivityLifecycleCallbacks {

    private Reference<Activity> mCurActivity = null;

    private static final long CHECK_DELAY = 2000;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<OnAppStatusChangedListener> listeners = new CopyOnWriteArrayList<>();
    private Runnable check;


    /**
     * 监听当前运用是否在前台运行
     */
    public interface OnAppStatusChangedListener {
        void onForeground();

        void onBackground();
    }


    public static TopActivityManager getInstance() {
        return SingltonHolder.mInstance;
    }

    static class SingltonHolder {
        static final TopActivityManager mInstance = new TopActivityManager();
    }

    private TopActivityManager() {

    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    public Activity getCurActivity() {
        if (mCurActivity == null) return null;
        return mCurActivity.get();
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(OnAppStatusChangedListener listener) {
        listeners.add(listener);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mCurActivity != null && mCurActivity.get() != null) {
            mCurActivity.clear();
            mCurActivity = null;
        }

        mCurActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mCurActivity != null && mCurActivity.get() != null && !mCurActivity.get().equals(activity)) {
            mCurActivity.clear();
            mCurActivity = null;
        }

        if (mCurActivity == null || mCurActivity.get() == null) {
            mCurActivity = new WeakReference<>(activity);
        }


        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null) handler.removeCallbacks(check);
        if (wasBackground) {
            for (OnAppStatusChangedListener l : listeners) {
                l.onForeground();
            }
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null) handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    for (OnAppStatusChangedListener l : listeners) {
                        l.onBackground();
                    }
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mCurActivity != null && mCurActivity.get() != null && mCurActivity.get().equals(activity)) {
            mCurActivity.clear();
            mCurActivity = null;
        }
    }


}

