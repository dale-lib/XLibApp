package com.dale.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dale.constant.LibApplication;

/**
 * 软键盘操作
 */
public final class KeyboardUtils {

    /**
     * 打开软键盘
     */
    public static void showSoftInput(final Activity activity) {
        showSoftInput(activity, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 打开软键盘
     *
     * @param flags 操作标志 {@link InputMethodManager#SHOW_IMPLICIT}
     */
    public static void showSoftInput(final Activity activity, final int flags) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        showSoftInput(view, flags);
    }

    /**
     * 打开软键盘
     */
    public static void showSoftInput(final View view) {
        showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 打开软键盘
     *
     * @param flags 操作标志 {@link InputMethodManager#SHOW_IMPLICIT}
     */
    public static void showSoftInput(final View view, final int flags) {
        InputMethodManager imm =
                (InputMethodManager) LibApplication.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput();
                }
            }
        });
    }

    /**
     * 关闭软键盘
     */
    public static void hideSoftInput(final Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        hideSoftInput(view);
    }

    /**
     * 关闭软键盘
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) LibApplication.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN
                        || resultCode == InputMethodManager.RESULT_SHOWN) {
                    toggleSoftInput();
                }
            }
        });
    }

    /**
     * 切换软键盘显示
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) LibApplication.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
