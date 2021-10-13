package com.dale.constant;

import android.app.Application;
import android.content.Context;

import androidx.core.content.FileProvider;

import com.dale.utils.FileUtils;

public class FileProvider4UtilCode extends FileProvider {
    static Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        LibApplication.init((Application) mContext.getApplicationContext(), FileUtils.PREFERENCE);
        return true;
    }
}
