package com.dale.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dale.constant.LibApplication;
import com.dale.utils.toast.ToastAdapter;
import com.dale.utils.toast.ToastConfig;
import com.dale.utils.toast.ToastViewHolder;

/**
 * Toast工具类
 */
public final class ToastUtils {

    private static Toast myToast;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Runnable task = new Runnable() {

        @Override
        public void run() {
            if (myToast != null) {
                myToast.cancel();
                myToast = null;
            }
        }
    };

    private ToastUtils() {
    }

    /**
     * 显示toast
     *
     * @param msg 信息
     */
    public static void showLong(String msg) {
        if (msg == null) {
            return;
        }
        showMyToast(msg, Toast.LENGTH_LONG, ToastConfig.getToastAdapter());
    }

    /**
     * 显示toast
     *
     * @param stringId string资源id
     */
    public static void show(int stringId) {
        if (stringId <= 0) {
            return;
        }
        showMyToast(ResUtils.getString(stringId), Toast.LENGTH_LONG, ToastConfig.getToastAdapter());
    }

    /**
     * 显示短toast
     *
     * @param msg 字符串
     */
    public static void showToast(String msg) {
        showMyToast(msg, Toast.LENGTH_LONG, ToastConfig.getToastAdapter());
    }

    /**
     * show自定义toast
     *
     * @param msg  字符串
     * @param time 0为短时间，1为场时间
     */
    public static void showMyToast(String msg, int time) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        showMyToast(msg, time, ToastConfig.getToastAdapter());
    }

    /**
     * show自定义toast
     *
     * @param msg     实体类
     * @param adapter toast的适配器,可以是静态实例,不用担心内存溢出
     */
    public static void show(Object msg, ToastAdapter adapter) {
        showMyToast(msg, Toast.LENGTH_LONG, adapter);
    }


    /**
     * show自定义toast
     *
     * @param msg     实体类
     * @param time    0为短时间，1为场时间
     * @param adapter toast的适配器,可以是静态实例,不用担心内存溢出
     */
    public static void showMyToast(Object msg, int time, ToastAdapter adapter) {
        if (msg == null) {
            return;
        }

        try {
            mHandler.removeCallbacks(task);
            if (myToast != null) {
                myToast.cancel();
                myToast = null;
            }
            ToastViewHolder holder = adapter.getTag();
            if (holder == null) {
                View view = LayoutInflater.from(LibApplication.getApp()).inflate(adapter.getLayoutId(), null);
                holder = new ToastViewHolder(view);
                adapter.setTag(holder);
            }
            myToast = new Toast(LibApplication.getApp());
            myToast.setGravity(Gravity.CENTER, 0, 0);
            myToast.setDuration(time);
            myToast.setView(holder.getView());
            adapter.onBindViewHolder(holder, msg);
            int delay = 1500;
            if (time != Toast.LENGTH_LONG && time != Toast.LENGTH_SHORT) {
                delay = time;
            }
            mHandler.postDelayed(task, delay);
            myToast.show();

        } catch (Exception e) {
        }
    }
}