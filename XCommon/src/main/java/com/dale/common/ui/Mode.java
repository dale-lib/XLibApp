package com.dale.common.ui;

import androidx.annotation.IntDef;

public interface Mode {

    int DISABLED = 0;

    int PULL_FROM_START = 1;

    int PULL_FROM_END = 2;

    int BOTH = 3;

    @IntDef({DISABLED, PULL_FROM_START, PULL_FROM_END, BOTH})
    @interface BaseMode {
    }

}
