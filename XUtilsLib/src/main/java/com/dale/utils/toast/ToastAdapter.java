package com.dale.utils.toast;

import androidx.annotation.LayoutRes;

public abstract class ToastAdapter {

    private ToastViewHolder mTag = null;

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void onBindViewHolder(ToastViewHolder holder, Object obj);

    public ToastViewHolder getTag() {
        return mTag;
    }

    public void setTag(ToastViewHolder tag) {
        mTag = tag;
    }
}

