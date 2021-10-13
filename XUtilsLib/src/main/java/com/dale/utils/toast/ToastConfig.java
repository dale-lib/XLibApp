package com.dale.utils.toast;

import androidx.annotation.StyleRes;

public class ToastConfig {
    /**
     * 自定义全局Toast适配器
     */
    private static ToastAdapter sToastAdapter = new DefaultToastAdapter();
    private static @StyleRes
    int themeId;

    ToastConfig() {
    }

    public void setToastAdapter(ToastAdapter toastAdapter) {
        sToastAdapter = toastAdapter;
    }

    public static ToastAdapter getToastAdapter() {
        return sToastAdapter;
    }

    public ToastConfig setTheme(int themeId) {
        ToastConfig.themeId = themeId;
        return this;
    }

    public static int getThemeId() {
        return themeId;
    }
}
