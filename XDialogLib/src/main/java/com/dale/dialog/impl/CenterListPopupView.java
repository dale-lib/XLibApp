package com.dale.dialog.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dale.adapter.BaseQuickAdapter;
import com.dale.adapter.BaseViewHolder;
import com.dale.dialog.R;
import com.dale.dialog.XPopup;
import com.dale.dialog.core.CenterPopupView;
import com.dale.dialog.interfaces.OnSelectListener;
import com.dale.dialog.widget.CheckView;
import com.dale.dialog.widget.VerticalRecyclerView;

import java.util.Arrays;

/**
 * Description: 在中间的列表对话框
 * Create by dance, at 2018/12/16
 */
public class CenterListPopupView extends CenterPopupView {
    RecyclerView recyclerView;
    TextView tv_title;

    /**
     * @param context
     * @param bindLayoutId     要求layoutId中必须有一个id为recyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tv_title的TextView
     * @param bindItemLayoutId 条目的布局id，要求布局中有id为iv_image的ImageView（非必须），和id为tv_text的TextView
     */
    public CenterListPopupView(@NonNull Context context, int bindLayoutId, int bindItemLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? R.layout._xpopup_center_impl_list : bindLayoutId;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        if (bindLayoutId != 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        tv_title = findViewById(R.id.tv_title);

        if (tv_title != null) {
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(GONE);
                if (findViewById(R.id.xpopup_divider) != null)
                    findViewById(R.id.xpopup_divider).setVisibility(GONE);
            } else {
                tv_title.setText(title);
            }
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

                // 对勾View
                if (checkedPosition != -1) {
                    if (holder.getView(R.id.check_view) != null) {
                        holder.getView(R.id.check_view).setVisibility(position == checkedPosition ? VISIBLE : GONE);
                        holder.<CheckView>getView(R.id.check_view).setColor(XPopup.getPrimaryColor());
                    }
                    holder.<TextView>getView(R.id.tv_text).setTextColor(position == checkedPosition ?
                            XPopup.getPrimaryColor() : getResources().getColor(R.color._xpopup_title_color));
                } else {
                    if (holder.getView(R.id.check_view) != null)
                        holder.getView(R.id.check_view).setVisibility(GONE);
                    //如果没有选择，则文字居中
                    holder.<TextView>getView(R.id.tv_text).setGravity(Gravity.CENTER);
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
                    if (position >= 0 && position < adapter.getData().size())
                        selectListener.onSelect(position, (String) adapter.getItem(position));
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                if (popupInfo.autoDismiss) dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
        applyTheme();
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        ((VerticalRecyclerView) recyclerView).setupDivider(true);
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        findViewById(R.id.xpopup_divider).setBackgroundColor(getResources().getColor(R.color._xpopup_list_dark_divider));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        ((VerticalRecyclerView) recyclerView).setupDivider(false);
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_dark_color));
        findViewById(R.id.xpopup_divider).setBackgroundColor(getResources().getColor(R.color._xpopup_list_divider));
    }

    CharSequence title;
    String[] data;
    int[] iconIds;

    public CenterListPopupView setStringData(CharSequence title, String[] data, int[] iconIds) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public CenterListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    /**
     * 设置默认选中的位置
     *
     * @param position
     * @return
     */
    public CenterListPopupView setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? super.getMaxWidth() : popupInfo.maxWidth;
    }
}
