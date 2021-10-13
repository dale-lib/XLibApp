package com.dale.common.util;

import android.app.Application;

import com.dale.common.R;
import com.dale.refresh.SmartRefreshLayout;
import com.dale.refresh.footer.ClassicsFooter;
import com.dale.refresh.header.ClassicsHeader;

public class ABApplication extends Application {

    private static ABApplication instance;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            return new ClassicsHeader(context);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));
    }

    public static ABApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
