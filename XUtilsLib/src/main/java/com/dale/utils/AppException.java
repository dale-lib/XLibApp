package com.dale.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 */
public class AppException extends Exception implements UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    private AppException(Context ctx) {
        mContext = ctx;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * 获取APP异常崩溃处理对象
     *
     * @param context
     * @return
     */
    public static AppException getAppExceptionHandler(Context context) {
        return new AppException(context);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
        //退出程序
        ExitUtils.getInstance().finishAll();
        System.exit(0);
    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     *
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        if (mContext == null) {
            return false;
        }

        final String crashReport = getCrashReport(ex);
        Log.e("Dream", "程序闪退:->" + crashReport);
        //显示异常信息&发送报告
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常啦", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

        }.start();
        return true;
    }


    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }
}
