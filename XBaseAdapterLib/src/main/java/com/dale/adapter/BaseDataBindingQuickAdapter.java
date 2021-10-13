/*
 * Copyright 2013 Joan Zapata
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseDataBindingQuickAdapter<T, Binding extends ViewDataBinding> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseDataBindingQuickAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseDataBindingQuickAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseDataBindingQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutResId, parent, false);
        BaseViewHolder baseViewHolder = new BaseViewHolder(binding.getRoot());
        bindViewClickListener(baseViewHolder);
        baseViewHolder.setAdapter(this);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Binding binding = DataBindingUtil.getBinding(holder.itemView);
        T t = getItem(position);
        convert(holder, t);
        convert(binding, t);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
    }

    protected abstract void convert(Binding binding, T item);
}
