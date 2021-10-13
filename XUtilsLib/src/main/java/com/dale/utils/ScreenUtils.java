package com.dale.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.dale.constant.LibApplication;

import static android.Manifest.permission.WRITE_SETTINGS;


public final class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @return 返回屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) LibApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * 获取屏幕高度
     *
     * @return 返回屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) LibApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @return 返回屏幕宽度
     */
    public static int getAppScreenWidth() {
        WindowManager wm = (WindowManager) LibApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * 获取屏幕高度
     *
     * @return 返回屏幕高度
     */
    public static int getAppScreenHeight() {
        WindowManager wm = (WindowManager) LibApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * 返回屏幕密度（获取屏幕缩放倍数）
     * 屏幕密度，density和dpi的关系为 density = dpi/160
     *
     * @return
     */
    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 屏幕dpi
     * dpi是Dots Per Inch的缩写, 每英寸点数，即每英寸包含像素个数。
     *
     * @return
     */
    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    /**
     * 设置全屏（去掉状态栏）
     *
     * @param activity The activity.
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置恢复状态栏
     *
     * @param activity The activity.
     */
    public static void setNonFullScreen(@NonNull final Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置有状态栏和没有状态栏切换
     *
     * @param activity The activity.
     */
    public static void toggleFullScreen(@NonNull final Activity activity) {
        boolean isFullScreen = isFullScreen(activity);
        Window window = activity.getWindow();
        if (isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 判定是否是全屏状态（没有状态栏）
     *
     * @return true: 全屏（没有状态栏）
     */
    public static boolean isFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * 设置横屏
     *
     * @param activity The activity.
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置竖屏
     *
     * @param activity The activity.
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否是横屏
     *
     * @return true:横屏
     */
    public static boolean isLandscape() {
        return LibApplication.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是竖屏
     *
     * @return true:竖屏
     */
    public static boolean isPortrait() {
        return LibApplication.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 获取屏幕旋转角度
     *
     * @param activity
     * @return 返回屏幕旋转角度
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 截屏当前界面，返回当前界面bitmap对象（包含状态栏）
     *
     * @param activity The activity.
     * @return bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 截屏当前界面，返回当前界面bitmap对象（包含状态栏）
     *
     * @param activity          The activity.
     * @param isDeleteStatusBar True 不包含状态栏，false:包含状态栏
     * @return bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.setWillNotCacheDrawing(false);
        Bitmap bmp = decorView.getDrawingCache();
        if (bmp == null) return null;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(
                    bmp,
                    0,
                    statusBarHeight,
                    dm.widthPixels,
                    dm.heightPixels - statusBarHeight
            );
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * 判断是否锁屏
     * 返回:true，表示有两种状态：a、屏幕是黑的  b、目前正处于解锁状态  。
     * 返回:false，表示目前未锁屏
     */
    public static boolean isScreenLock() {
        KeyguardManager km =
                (KeyguardManager) LibApplication.getApp().getSystemService(Context.KEYGUARD_SERVICE);
        if (km == null) return false;
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * 判断是否是平板
     *
     * @return true:是平板
     */
    public static boolean isTablet() {
        return (LibApplication.getApp().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 设置休眠时长
     *
     * @param duration
     */
    @RequiresPermission(WRITE_SETTINGS)
    public static void setSleepDuration(final int duration) {
        Settings.System.putInt(
                LibApplication.getApp().getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT,
                duration
        );
    }

    /**
     * 获取进入休眠时长
     *
     * @return 返回休眠时长
     */
    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(
                    LibApplication.getApp().getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }
}
