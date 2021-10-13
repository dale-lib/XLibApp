package com.dale.dialog.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dale.adapter.BaseQuickAdapter;
import com.dale.adapter.BaseViewHolder;
import com.dale.dialog.R;
import com.dale.dialog.core.AttachPopupView;
import com.dale.dialog.interfaces.OnSelectListener;
import com.dale.dialog.util.XPopupUtils;
import com.dale.dialog.widget.VerticalRecyclerView;

import java.util.Arrays;

/**
 * Description: Attach类型的列表弹窗
 * Create by dance, at 2018/12/12
 */
public class AttachListPopupView extends AttachPopupView {
    RecyclerView recyclerView;
    protected int bindLayoutId;
    protected int bindItemLayoutId;
    protected int contentGravity = Gravity.CENTER;

    /**
     * @param context
     * @param bindLayoutId     layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中有id为iv_image的ImageView（非必须），和id为tv_text的TextView
     */
    public AttachListPopupView(@NonNull Context context, int bindLayoutId, int bindItemLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? R.layout._xpopup_attach_impl_list : bindLayoutId;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        if (bindLayoutId != 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        final BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(bindItemLayoutId == 0 ? R.layout._xpopup_adapter_text : bindItemLayoutId, Arrays.asList(data)) {
            @Override
            protected void convert(BaseViewHolder holder, String s) {
                int position = holder.getLayoutPosition();
                holder.setText(R.id.tv_text, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(R.id.iv_image).setVisibility(VISIBLE);
                    holder.getView(R.id.iv_image).setBackgroundResource(iconIds[position]);
                } else {
                    holder.getView(R.id.iv_image).setVisibility(GONE);
                }

                if (bindItemLayoutId == 0) {
                    if (popupInfo.isDarkTheme) {
                        holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_white_color));
                    } else {
                        holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_dark_color));
                    }
                }
            }
        };

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, (String) adapter.getItem(position));
                }
                if (popupInfo.autoDismiss) dismiss();
            }
        });

        recyclerView.setAdapter(adapter);

        applyTheme();
    }

    protected void applyTheme() {
        if (bindLayoutId == 0) {
            if (popupInfo.isDarkTheme) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
            attachPopupContainer.setBackground(XPopupUtils.createDrawable(getResources().getColor(popupInfo.isDarkTheme ? R.color._xpopup_dark_color
                    : R.color._xpopup_light_color), popupInfo.borderRadius));
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        ((VerticalRecyclerView) recyclerView).setupDivider(true);
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        ((VerticalRecyclerView) recyclerView).setupDivider(false);
    }

    String[] data;
    int[] iconIds;

    public AttachListPopupView setStringData(String[] data, int[] iconIds) {
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    public AttachListPopupView setContentGravity(int gravity) {
        this.contentGravity = gravity;
        return this;
    }

    private OnSelectListener selectListener;

    public AttachListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }
}
