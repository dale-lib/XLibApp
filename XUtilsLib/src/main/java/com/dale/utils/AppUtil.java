package com.dale.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dale.constant.LibApplication;

import java.util.List;

/**
 * App信息相关
 * 1.getVersionName ：获取版本名字
 * 2.getVersionCode ：获取版号
 * 3.getPackgeName ： 得到软件包名
 * 4.getProcessName ：获取当前进程的名字
 * 5.isAppForeground：判断程序是否在前台运行
 */
public class AppUtil {
    /**
     * 获取版本名字
     */
    public synchronized static String getVersionName() {
        String version = "0";
        // 获取packagemanager的实例
        PackageManager packageManager = LibApplication.getApp().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(LibApplication.getApp().getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != packInfo) {
            version = packInfo.versionName;
        }
        return version;
    }

    /**
     * 获取版号
     */
    public synchronized static int getVersionCode() {
        int versionCode = 0;
        // 获取packagemanager的实例
        PackageManager packageManager = LibApplication.getApp().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(LibApplication.getApp().getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != packInfo) {
            versionCode = packInfo.versionCode;
        }
        return versionCode;
    }


    /**
     * 得到软件包名
     */
    public synchronized static String getPackgeName() {
        String packgename = "unknow";
        try {
            packgename = LibApplication.getApp().getPackageManager().getPackageInfo(
                    LibApplication.getApp().getPackageName(), PackageManager.GET_CONFIGURATIONS).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packgename;
    }

    /**
     * 获取当前进程的名字
     */
    public static String getProcessName(int pid) {
        ActivityManager am = (ActivityManager) LibApplication.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }


}
