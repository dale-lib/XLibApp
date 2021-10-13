package com.dale.common.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dale.adapter.BaseQuickAdapter;
import com.dale.adapter.BaseViewHolder;
import com.dale.refresh.api.RefreshLayout;
import com.dale.refresh.listener.OnLoadMoreListener;
import com.dale.refresh.listener.OnRefreshListener;

public interface IRefresh<T> extends OnRefreshListener, OnLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    RefreshLayout getRefreshLayout();

    RecyclerView getRecyclerView();

    BaseQuickAdapter<T, BaseViewHolder> getListAdapter();

    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView.ItemDecoration getItemDecoration();

    /**
     * 刷新模式设置
     *
     * @return
     */
    @Mode.BaseMode
    int getMode();

    View getEmptyView();

    View getHeaderView();

    View getFooterView();

    /**
     * 点击item回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    void onItemClick(BaseQuickAdapter adapter, View view, int position);

    /**
     * 加载更多回调
     *
     * @param refreshLayout
     */
    void onLoadMore(@NonNull RefreshLayout refreshLayout);

    /***
     * 下拉刷新回调
     * @param refreshLayout
     */
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
