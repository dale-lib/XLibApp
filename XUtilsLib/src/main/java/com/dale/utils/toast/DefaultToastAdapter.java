package com.dale.utils.toast;


import com.dale.utils.R;

public class DefaultToastAdapter extends ToastAdapter {

    @Override
    public int getLayoutId() {
        return R.layout.custom_toast;
    }

    @Override
    public void onBindViewHolder(ToastViewHolder holder, Object obj) {
        if (obj instanceof String) {
            holder.setText(R.id.tv_msg, (String) obj);
        }
    }

}

