package com.dale.constant;

import android.app.Application;

import com.dale.utils.AppException;
import com.dale.utils.MMKVUtil;
import com.dale.utils.TopActivityManager;


public final class LibApplication {


    private static Application sApplication;

    private LibApplication() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app, String path) {
        sApplication = app;
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(sApplication));
        TopActivityManager.getInstance().init(sApplication);
        MMKVUtil.init(app);
    }

    public static Application getApp() {
        return sApplication;
    }

}
