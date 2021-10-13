package com.dale.common.util;

import android.view.View;

import androidx.core.widget.NestedScrollView;

/**
 * create by Dale
 * create on 2019/8/19
 * description: 标题滚动透明变化帮助类
 */
public class ScrollChangeHelper implements NestedScrollView.OnScrollChangeListener {

    private int lastScrollY = 0;
    private int mScrollY = 0;
    private int scrollHeight;
    private View alphaView;
    private onMoveRatioListener onMoveRatioListener;

    public ScrollChangeHelper(Builder builder) {
        init(builder);
    }

    void init(Builder builder) {
        scrollHeight = builder.scrollHeight;
        alphaView = builder.alphaView;
        onMoveRatioListener = builder.onMoveRatioListener;
        builder.scrollView.setOnScrollChangeListener(this);
        alphaView.setAlpha(0);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (lastScrollY < scrollHeight) {
            scrollY = Math.min(scrollHeight, scrollY);
            mScrollY = scrollY > scrollHeight ? scrollHeight : scrollY;
            float ratio = 1f * mScrollY / scrollHeight;
            if (onMoveRatioListener != null) {
                onMoveRatioListener.onMoveRatio(ratio);
            } else {
                alphaView.setAlpha(ratio);
            }
        }
        lastScrollY = scrollY;
    }


    public interface onMoveRatioListener {
        /**
         * @param ratio 比率 （ratio * 255 百分百透明度）
         */
        void onMoveRatio(float ratio);
    }

    public static class Builder {
        private int scrollHeight;//指定偏移量
        private NestedScrollView scrollView;
        private View alphaView;//如果设置了此view将自行处理，没有设置回调处理
        private onMoveRatioListener onMoveRatioListener;

        public Builder scrollHeight(int scrollHeight) {
            this.scrollHeight = scrollHeight;
            return this;
        }

        public Builder setNestedScrollView(NestedScrollView scrollView) {
            this.scrollView = scrollView;
            return this;
        }

        public Builder setAlphaView(View alphaView) {
            this.alphaView = alphaView;
            return this;
        }

        public Builder setOnMoveRatioListener(onMoveRatioListener onMoveRatioListener) {
            this.onMoveRatioListener = onMoveRatioListener;
            return this;
        }

        public ScrollChangeHelper build() {
            return new ScrollChangeHelper(this);
        }

    }
}

////白->红 可设置一个图片颜色变化
//        mIvSearch.setColorFilter(new ColorMatrixColorFilter(new float[]{
//                0,0,0,0,255-scale*12,
//                0,0,0,0,255-scale*153,
//                0,0,0,0,255-scale*172,
//                0,0,0,1,0
//                }));