package com.dale.common.ui;


import android.view.View;

import androidx.annotation.CallSuper;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dale.adapter.BaseQuickAdapter;
import com.dale.adapter.BaseViewHolder;
import com.dale.common.R;
import com.dale.common.util.GridItemDecoration;
import com.dale.refresh.api.RefreshLayout;

/**
 * create by Dale
 * create on 2019/5/17
 * description:下拉刷新基类
 */
public abstract class ABRefreshFragment<T, Binding extends ViewDataBinding> extends ABBaseFragment<Binding> implements IRefresh<T> {

    protected RefreshDelegate<T> refreshDelegate;
    protected BaseQuickAdapter<T, BaseViewHolder> listAdapter;
    protected RefreshLayout refreshLayout;
    protected RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.x_activity_refresh;
    }

    @CallSuper
    @Override
    protected void initViewsAndEvents() {
        refreshDelegate = new RefreshDelegate<>(this);
        listAdapter = getListAdapter();
        refreshDelegate.initViews(listAdapter);
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        return refreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recyclerview);
        return recyclerView;
    }


    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new GridItemDecoration
                .Builder(mContext)
                .horColor(R.color.x_line_color)
                .verColor(R.color.x_line_color)
                .showLastDivider(true)
                .size(1)
                .build();
    }

    @Override
    public int getMode() {
        return Mode.BOTH;
    }

    @Override
    public View getEmptyView() {
        return null;
    }

    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    public View getFooterView() {
        return null;
    }

}
