package com.dale.common.tab;


import android.view.View;

import androidx.annotation.ColorRes;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;

import com.dale.common.R;
import com.dale.common.databinding.XActivityTabMainBinding;
import com.dale.common.ui.ABBaseFragment;

import magicindicator.FragmentContainerHelper;
import magicindicator.MagicIndicator;
import magicindicator.buildins.commonnavigator.CommonNavigator;
import magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

/**
 * create by Dale
 * create on 2019/5/30
 * description: Tab 基类
 */
public abstract class ABMainTabFragment extends ABBaseFragment<XActivityTabMainBinding> implements TabAdapter.OnTabClickListener {
    TabAdapter commonAdapter;
    FragmentContainerHelper tabHelper = new FragmentContainerHelper();
    MagicIndicator mainTabView;
    MainTab[] mainTabs;
    ABBaseFragment[] fragments;
    ArrayMap<Integer, Fragment> supportFragments;
    int currentIndex = 0;

    protected abstract MainTab[] getMainTabs();

    protected abstract ABBaseFragment[] getFragments();

    protected @ColorRes
    int getTextColor() {
        return R.color.x_tab_color;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.x_activity_tab_main;
    }

    @Override
    protected void initViewsAndEvents() {
        mainTabView = rootView.findViewById(R.id.mainTabView);
        mainTabs = getMainTabs();
        fragments = getFragments();
        supportFragments = new ArrayMap<>(mainTabs.length);
        loadMultipleRootFragment(R.id.fragment_content, currentIndex, fragments);
        initMagicIndicators();
    }

    private void initMagicIndicators() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonAdapter = new TabAdapter(mContext, mainTabs);
        commonAdapter.setTextColor(getTextColor());
        commonAdapter.setTabClickListener(this);
        commonNavigator.setAdapter(commonAdapter);
        mainTabView.setNavigator(commonNavigator);
        tabHelper.attachMagicIndicator(mainTabView);
        tabHelper.handlePageSelected(currentIndex, false);
    }

    @Override
    public void onClick(CommonNavigatorAdapter adapter, View view, int postion) {
        if (postion == currentIndex) {
            return;
        }
        ABBaseFragment tabFragment = fragments[postion];
        ABBaseFragment preTabFragment = fragments[currentIndex];
        showHideFragment(tabFragment, preTabFragment);
        currentIndex = postion;
        tabHelper.handlePageSelected(currentIndex, false);
    }

}
