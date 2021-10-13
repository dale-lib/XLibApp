package com.dale.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dale.common.R;
import com.dale.dialog.XPopup;
import com.dale.dialog.impl.LoadingPopupView;
import com.dale.fragment.ui.SupportActivity;
import com.dale.utils.ExitUtils;
import com.dale.utils.KeyboardUtils;
import com.dale.utils.StatusBarUtil;
import com.dale.utils.WeakHandler;

/**
 * create by Dale
 * create on 2019/5/17
 * description: 基类
 */
public abstract class ABBaseActivity<Binding extends ViewDataBinding> extends SupportActivity {
    protected Activity mContext;
    protected Bundle bundle;
    protected LoadingPopupView progressDialog;
    protected Binding mBinding;
    private WeakHandler weakHandler = new WeakHandler();

    protected abstract int getLayoutId();

    protected abstract void initViewModel();

    protected abstract void initViewsAndEvents();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ExitUtils.getInstance().addActivity(this);//记录打开的Act
        initSystemBar();
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        bundle = getIntent().getExtras();
        initViewModel();
        initViewsAndEvents();
        mBinding.setLifecycleOwner(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ExitUtils.getInstance().removeActivity(this);//关闭的Act
        //移除所有
        weakHandler.removeCallbacksAndMessages(null);
    }

    // 记录dialog 显示创建时间
    private long dialogCreateTime;

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new XPopup.Builder(this)
                    .asLoading();
        }
        dialogCreateTime = System.currentTimeMillis();
        progressDialog.show();

    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShow()) {
            if (System.currentTimeMillis() - dialogCreateTime < 500) {
                weakHandler.postDelayed(() -> progressDialog.dismiss(), 350);
            } else {
                progressDialog.dismiss();
            }
        }
    }

    public void goActivity(Class<? extends Activity> descClass) {
        this.goActivity(descClass, null);
    }

    public void goActivity(Class<? extends Activity> descClass, Bundle bundle) {
        try {
            Intent intent = new Intent();
            intent.setClass(mContext, descClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            mContext.startActivityForResult(intent, 0);
            mContext.overridePendingTransition(R.anim.x_push_left_in, R.anim.x_push_left_out);
        } catch (Exception e) {
        }
    }


    @Override
    public void finish() {
        super.finish();
        KeyboardUtils.hideSoftInput(this);
        overridePendingTransition(R.anim.x_push_right_in, R.anim.x_push_right_out);
    }

    protected void initSystemBar() {
        StatusBarUtil.setTransparentForWindow(this);
        StatusBarUtil.setLightMode(this);//状态栏图标黑色
    }

}
