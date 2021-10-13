package com.dale.common.tab;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.dale.common.R;

import magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

public class TabAdapter extends CommonNavigatorAdapter {

    private OnTabClickListener mTabClickListener;
    private Context mContext;
    private MainTab[] mainTabs;
    private TextView[] tvTipCounts;
    private @ColorRes
    int textColor = R.color.x_tab_color;
    private SparseArray<CommonPagerTitleView> mTitleViewsArray = new SparseArray();


    public TabAdapter(Context mContext, MainTab[] mainTabs) {
        this.mContext = mContext;
        this.mainTabs = mainTabs;
        tvTipCounts = new TextView[mainTabs.length];
    }

    public void setTextColor(@ColorRes int textColor) {
        this.textColor = textColor;
    }

    @Override
    public int getCount() {
        return mainTabs.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        CommonPagerTitleView titleView = mTitleViewsArray.get(index);
        MainTab mainTab = mainTabs[index];
        if (titleView == null) {
            titleView = new CommonPagerTitleView(mContext);
            View customLayout = View.inflate(mContext, R.layout.x_item_home_tabhost, null);
            TextView titleText = customLayout.findViewById(R.id.titleText);
            TextView tvTipCount = customLayout.findViewById(R.id.tvTipCount);
            ImageView ivTabIcon = customLayout.findViewById(R.id.ivTabIcon);
            titleText.setText(mainTab.title);
            titleText.setTextColor(mContext.getResources().getColorStateList(textColor));
            ivTabIcon.setImageResource(mainTab.normalIcon);
            tvTipCounts[index] = tvTipCount;
            if (mTabClickListener != null) {
                customLayout.setOnClickListener(v -> mTabClickListener.onClick(TabAdapter.this, v, index));
            }
            titleView.setContentView(customLayout);
            titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                @Override
                public void onSelected(int index, int totalCount) {
                    ivTabIcon.setImageResource(mainTab.selectedIcon);
                    titleText.setSelected(true);
                }

                @Override
                public void onDeselected(int index, int totalCount) {
                    ivTabIcon.setImageResource(mainTab.normalIcon);
                    titleText.setSelected(false);
                }

                @Override
                public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                }

                @Override
                public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                }
            });
            mTitleViewsArray.put(index, titleView);
        }

        if (TextUtils.isEmpty(mainTab.tipCount)) {
            tvTipCounts[index].setVisibility(View.GONE);
        } else {
            tvTipCounts[index].setVisibility(View.VISIBLE);
            tvTipCounts[index].setText(mainTab.tipCount);
        }

        if (titleView.getParent() instanceof ViewGroup) {
            ((ViewGroup) titleView.getParent()).removeView(titleView);
        }
        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

    public final void setTabClickListener(OnTabClickListener tabClickListener) {
        mTabClickListener = tabClickListener;
    }

    public interface OnTabClickListener {
        void onClick(CommonNavigatorAdapter adapter, View view, int postion);
    }

}
