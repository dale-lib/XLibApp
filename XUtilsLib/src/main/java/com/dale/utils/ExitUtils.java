package com.dale.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by zw on 2016/10/26 0026.
 * 记录打开的act  当程序出现异常  finish掉所有的act
 */
public class ExitUtils {

    private static ExitUtils instance;
    private Stack<Activity> stack;

    public static ExitUtils getInstance() {
        if (instance == null) {
            synchronized (ExitUtils.class) {
                if (instance == null) {
                    instance = new ExitUtils();
                }
            }
        }
        return instance;
    }

    public ExitUtils() {
        stack = new Stack<>();
    }

    public void addActivity(Activity activity) {
        stack.push(activity);
    }

    public void removeActivity(Activity activity) {
        stack.remove(activity);
    }

    public void finishAll() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
        instance = null;
    }
}
