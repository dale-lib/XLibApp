package com.dale.common.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dale.common.R;
import com.dale.fragment.ui.SupportActivity;
import com.dale.utils.SizeUtils;

public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_right;
    private String centreTitle;
    private String leftTitle;
    private String rightTitle;
    private boolean showRight;
    private boolean showLeft;
    private boolean whiteStyle;//true:图标，字体都是白色
    private Drawable left_drawable;
    private Drawable right_drawable;
    private int backRes;
    private LeftBtnOnClickListener leftOnClickListener;
    private RightBtnOnClickListener rightOnClickListener;
    private TitleOnClickListener titleOnClickListener;
    private Context mContext;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        centreTitle = mTypedArray.getString(R.styleable.TitleBar_centreTitle);
        leftTitle = mTypedArray.getString(R.styleable.TitleBar_leftTitle);
        rightTitle = mTypedArray.getString(R.styleable.TitleBar_rightTitle);
        whiteStyle = mTypedArray.getBoolean(R.styleable.TitleBar_whiteStyle, false);
        showRight = mTypedArray.getBoolean(R.styleable.TitleBar_showRight, false);
        showLeft = mTypedArray.getBoolean(R.styleable.TitleBar_showLeft, true);

        int _id_left = mTypedArray.getResourceId(R.styleable.TitleBar_leftDrawable, whiteStyle ? R.drawable.x_titlebar_back_white_normal : R.drawable.x_titlebar_back_normal);
        left_drawable = context.getResources().getDrawable(_id_left);

        int _id_right = mTypedArray.getResourceId(R.styleable.TitleBar_rightDrawable, -1);
        if (_id_right != -1) {
            right_drawable = context.getResources().getDrawable(_id_right);
        }

        backRes = mTypedArray.getResourceId(R.styleable.TitleBar_barBg, R.drawable.x_title_bg);

        mTypedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setPadding(0, SizeUtils.dp2px(28), 0, 0);
        setBackgroundResource(backRes);
        View mlLinearLayout = LayoutInflater.from(mContext).inflate(R.layout.x_view_titlebar, this, true);
        tv_back = mlLinearLayout.findViewById(R.id.backView);
        tv_title = mlLinearLayout.findViewById(R.id.tv_title);
        tv_right = mlLinearLayout.findViewById(R.id.tv_right);
        if (whiteStyle) {
            tv_back.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tv_right.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }
        setTextView();
    }

    private void setTextView() {
        tv_back.setVisibility(showLeft ? View.VISIBLE : View.GONE);
        if (leftTitle != null) {
            tv_back.setText(leftTitle);
        } else {
            tv_back.setText("返回");
        }

        if (left_drawable != null) {
            left_drawable.setBounds(0, 0, left_drawable.getMinimumWidth(), left_drawable.getMinimumHeight());
            tv_back.setCompoundDrawables(left_drawable, null, null, null);
        }

        if (centreTitle != null) {
            tv_title.setText(centreTitle);
        }

        tv_right.setVisibility(showRight ? View.VISIBLE : View.GONE);
        if (rightTitle != null) {
            tv_right.setText(rightTitle);
        }

        if (right_drawable != null) {
            right_drawable.setBounds(0, 0, right_drawable.getMinimumWidth(), right_drawable.getMinimumHeight());
            tv_right.setCompoundDrawables(null, null, right_drawable, null);
        }
        tv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }


    public void setRightBackgroundResource(int resid) {
        tv_right.setBackgroundResource(resid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backView) {
            if (leftOnClickListener == null) {
                try {
                    Context context = v.getContext();
                    while (context instanceof ContextWrapper) {
                        if (context instanceof SupportActivity) {
                            ((SupportActivity) context).onBackPressedSupport();
                        }
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                } catch (Exception e) {
                }

            } else {
                leftOnClickListener.onLeftClick(v);
            }
        } else if (v.getId() == R.id.tv_right) {
            if (rightOnClickListener != null) {
                rightOnClickListener.onRightClick(v);
            }
        } else if (v.getId() == R.id.tv_title) {
            if (titleOnClickListener != null) {
                titleOnClickListener.onTitleClick(v);
            }
        }
    }

    public void setTitleRightDrawable(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_title.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * 设置左边按钮监听
     *
     * @param
     */
    public void setLeftOnClickListener(LeftBtnOnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
    }

    public void setTitleOnClickListener(TitleOnClickListener titleOnClickListener) {
        this.titleOnClickListener = titleOnClickListener;
    }

    /**
     * @param rightOnClickListener 设置右边按钮监听
     */
    public void setRightOnClickListener(RightBtnOnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
    }

    /**
     * @param text 设置左边文字
     */
    public void setLeftTextView(CharSequence text) {
        tv_back.setText(text);
    }

    /**
     * @param text 设置中间标题文字
     */
    public void setTiteTextView(CharSequence text) {
        tv_title.setText(text);
    }

    /**
     * @param text 设置右边按钮文字
     */
    public void setRightTextView(CharSequence text) {
        tv_right.setText(text);
    }

    /**
     * 动态设置左边按钮是否显示
     */
    public void setShowLeft(int visibility) {
        tv_back.setVisibility(visibility);
    }

    /**
     * 动态设置右边按钮是否显示
     */
    public void setShowRight(int visibility) {
        tv_right.setVisibility(visibility);
    }


    public interface LeftBtnOnClickListener {
        void onLeftClick(View view);
    }

    public interface RightBtnOnClickListener {
        void onRightClick(View view);
    }

    public interface TitleOnClickListener {
        void onTitleClick(View view);
    }
}
