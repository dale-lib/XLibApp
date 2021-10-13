package com.dale.utils;

import android.util.Log;


public final class LogUtils {

    public static String customTagPrefix = "Dream";
    private static boolean isDebug = false;

    private LogUtils() {
    }

    public static void isDebug(boolean b) {
        isDebug = b;
    }

    private static String generateTag(StackTraceElement caller, String mtag) {
        String tag = mtag + ": %s.%s(Line:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        return tag;
    }

    public static void d(String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.d(tag, content);
    }

    public static void d(String mtag, String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, mtag);
        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.e(tag, content);
    }

    public static void e(String mtag, String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, mtag);
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.e(tag, content, tr);
    }

    public static void i(String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.i(tag, content);
    }

    public static void i(String mtag, String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, mtag);
        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.v(tag, content);
    }

    public static void v(String mtag, String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, mtag);
        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.w(tag, content);
    }

    public static void w(String mtag, String content) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, mtag);
        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!isDebug) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, customTagPrefix);
        Log.w(tag, content, tr);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

}