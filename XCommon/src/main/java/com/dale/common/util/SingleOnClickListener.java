package com.dale.common.util;

import android.view.View;

public abstract class SingleOnClickListener implements View.OnClickListener {

    private int spaceTime = 500;
    private long lastClickTime;

    //是否是双击
    private boolean isDoubleClick() {
        return isDoubleClick(spaceTime);
    }

    private boolean isDoubleClick(int spaceTime) {
        this.spaceTime = spaceTime;
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime > spaceTime || lastClickTime == 0) {
            isClick2 = false;
            lastClickTime = currentTime;
        } else {
            isClick2 = true;
        }
        return isClick2;
    }

    @Override
    public final void onClick(View v) {
        if (!isDoubleClick()) {
            onSingleClick(v);
        }
    }

    //单次点击
    public abstract void onSingleClick(View view);
}
