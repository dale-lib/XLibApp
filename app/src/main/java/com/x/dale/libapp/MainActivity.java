package com.x.dale.libapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dale.adapter.BaseDataBindingQuickAdapter;
import com.dale.adapter.BaseQuickAdapter;
import com.dale.adapter.BaseViewHolder;
import com.dale.common.databinding.XActivityRefreshBinding;
import com.dale.common.ui.ABRefreshActivity;
import com.dale.common.ui.Mode;
import com.dale.refresh.api.RefreshLayout;
import com.x.dale.libapp.databinding.ItemAdapterTestBinding;

import java.util.List;


public class MainActivity extends ABRefreshActivity<Person, XActivityRefreshBinding> {

    MainViewModel mMainViewModel;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Person person = new Person();
            person.setUserName("kaka" + System.currentTimeMillis());
            mMainViewModel.setData(person);
            mHandler.sendEmptyMessageDelayed(0, 1500);
        }
    };

    @Override
    protected void initViewModel() {
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        mHandler.sendEmptyMessageDelayed(0, 1500);

        mMainViewModel.liveData.observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                listAdapter.setNewData(people);
            }
        });
    }

    @Override
    public BaseQuickAdapter<Person, BaseViewHolder> getListAdapter() {
        return new BaseDataBindingQuickAdapter<Person, ItemAdapterTestBinding>(R.layout.item_adapter_test) {
            @Override
            protected void convert(ItemAdapterTestBinding binding, Person item) {
                binding.setVariable(BR.person, item);
            }
        };
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public int getMode() {
        return Mode.DISABLED;
    }
}