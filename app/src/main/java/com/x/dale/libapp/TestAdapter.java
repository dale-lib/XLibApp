package com.x.dale.libapp;

import com.dale.adapter.BaseDataBindingQuickAdapter;
import com.x.dale.libapp.databinding.ItemAdapterTestBinding;

public class TestAdapter extends BaseDataBindingQuickAdapter<Person, ItemAdapterTestBinding> {


    public TestAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(ItemAdapterTestBinding binding, Person item) {
        binding.setVariable(BR.person,item);
    }
}
