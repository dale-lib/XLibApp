package com.dale.dialog.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dale.dialog.R;
import com.dale.dialog.animator.PopupAnimator;
import com.dale.dialog.enums.PopupStatus;
import com.dale.dialog.util.KeyboardUtils;
import com.dale.dialog.util.XPopupUtils;
import com.dale.dialog.widget.SmartDragLayout;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    protected SmartDragLayout bottomPopupContainer;

    public BottomPopupView(@NonNull Context context) {
        super(context);
        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
    }

    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), bottomPopupContainer, false);
        bottomPopupContainer.addView(contentView);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_bottom_popup_view;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (bottomPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        bottomPopupContainer.setDuration(getAnimationDuration());
        bottomPopupContainer.enableDrag(popupInfo.enableDrag);
        bottomPopupContainer.dismissOnTouchOutside(popupInfo.isDismissOnTouchOutside);
        bottomPopupContainer.isThreeDrag(popupInfo.isThreeDrag);

        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);

        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight()
                , getPopupWidth(), getPopupHeight(), null);

        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                beforeDismiss();
                if (popupInfo != null && popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.beforeDismiss(BottomPopupView.this);
                doAfterDismiss();
            }

            @Override
            public void onDrag(int value, float percent, boolean isScrollUp) {
                if (popupInfo == null) return;
                if (popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.onDrag(BottomPopupView.this, value, percent, isScrollUp);
                if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg)
                    setBackgroundColor(shadowBgAnimator.calculateBgColor(percent));
            }

            @Override
            public void onOpen() {
            }
        });

        bottomPopupContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

//    @Override
//    protected void doAfterShow() {
//        handler.removeCallbacks(doAfterShowTask);
//        handler.postDelayed(doAfterShowTask, 0);
//    }

    @Override
    public void doShowAnimation() {
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateShow();
        }
        bottomPopupContainer.open();
    }

    @Override
    public void doDismissAnimation() {
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateDismiss();
        }
        bottomPopupContainer.close();
    }

    protected void doAfterDismiss() {
        if (popupInfo != null && popupInfo.autoOpenSoftInput)
            KeyboardUtils.hideSoftInput(this);
        handler.removeCallbacks(doAfterDismissTask);
        handler.postDelayed(doAfterDismissTask, 0);
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return null;
    }

    @Override
    public void dismiss() {
        if (popupInfo == null) return;
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        clearFocus();
        bottomPopupContainer.close();
    }

    /**
     * 具体实现的类的布局
     *
     * @return
     */
    protected int getImplLayoutId() {
        return 0;
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? XPopupUtils.getAppWidth(getContext())
                : popupInfo.maxWidth;
    }

}
