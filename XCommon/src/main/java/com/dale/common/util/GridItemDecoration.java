package com.dale.common.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    Paint mVerPaint, mHorPaint;
    Builder mBuilder;


    public GridItemDecoration(Builder builder) {
        init(builder);
    }

    void init(Builder builder) {
        this.mBuilder = builder;
        mVerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVerPaint.setStyle(Paint.Style.FILL);
        mVerPaint.setColor(builder.verColor);
        mHorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHorPaint.setStyle(Paint.Style.FILL);
        mHorPaint.setColor(builder.horColor);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (mBuilder.isExistHeadView && i == 0)
                continue;
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mBuilder.dividerVerSize;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mBuilder.dividerHorSize;
            c.drawRect(left, top, right, bottom, mHorPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (mBuilder.isExistHeadView && i == 0)
                continue;

            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mBuilder.dividerVerSize;

            c.drawRect(left, top, right, bottom, mVerPaint);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // ??????
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager ???????????????
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;

                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager ???????????????
            {

                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mBuilder.isExistHeadView)
            itemPosition -= 1;
        if (itemPosition < 0)
            return;

        int column = itemPosition % spanCount;
        int bottom = 0;

        int left = column * mBuilder.dividerVerSize / spanCount;
        int right = mBuilder.dividerVerSize - (column + 1) * mBuilder.dividerVerSize / spanCount;

        if (!(isLastRaw(parent, itemPosition, spanCount, childCount) && !mBuilder.isShowLastDivider))
            bottom = mBuilder.dividerHorSize;

        outRect.set(left, 0, right, bottom);
        marginOffsets(outRect, spanCount, itemPosition);
    }

    private void marginOffsets(Rect outRect, int spanCount, int itemPosition) {
        if (mBuilder.marginRight == 0 && mBuilder.marginLeft == 0)
            return;

        int itemShrink = (mBuilder.marginLeft + mBuilder.marginRight) / spanCount;
        outRect.left += (mBuilder.marginLeft - (itemPosition % spanCount) * itemShrink);

        outRect.right += ((itemPosition % spanCount) + 1) * itemShrink - mBuilder.marginLeft;
    }

    public static class Builder {
        private Context c;
        int horColor;
        int verColor;
        int dividerHorSize;
        int dividerVerSize;
        int marginLeft, marginRight;
        boolean isShowLastDivider = false;
        boolean isExistHeadView = false;

        public Builder(Context c) {
            this.c = c;
        }

        /**
         * ??????divider?????????
         *
         * @param color
         * @return
         */
        public Builder color(@ColorRes int color) {
            this.horColor = c.getResources().getColor(color);
            this.verColor = c.getResources().getColor(color);
            return this;
        }

        /**
         * ??????????????????divider?????????
         *
         * @param horColor
         * @return
         */
        public Builder horColor(@ColorRes int horColor) {
            this.horColor = c.getResources().getColor(horColor);
            return this;
        }

        /**
         * ??????????????????divider?????????
         *
         * @param verColor
         * @return
         */
        public Builder verColor(@ColorRes int verColor) {
            this.verColor = c.getResources().getColor(verColor);
            return this;
        }

        /**
         * ??????divider?????????
         *
         * @param size
         * @return
         */
        public Builder size(int size) {
            this.dividerHorSize = size;
            this.dividerVerSize = size;
            return this;
        }

        /**
         * ????????????divider?????????
         *
         * @param horSize
         * @return
         */
        public Builder horSize(int horSize) {
            this.dividerHorSize = horSize;
            return this;
        }

        /**
         * ????????????divider?????????
         *
         * @param verSize
         * @return
         */
        public Builder verSize(int verSize) {
            this.dividerVerSize = verSize;
            return this;
        }

        /**
         * ????????????HeadView???RecyclerView????????????????????????
         *
         * @param marginLeft
         * @param marginRight
         * @return
         */
        public Builder margin(int marginLeft, int marginRight) {
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            return this;
        }

        /**
         * ????????????divider??????????????????
         *
         * @param isShow
         * @return
         */
        public Builder showLastDivider(boolean isShow) {
            this.isShowLastDivider = isShow;
            return this;
        }

        /**
         * ????????????HeadView
         *
         * @param isExistHead
         * @return
         */
        public Builder isExistHead(boolean isExistHead) {
            this.isExistHeadView = isExistHead;
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(this);
        }

    }
}
