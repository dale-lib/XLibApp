package com.dale.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dale.common.R;


public class ValueView extends FrameLayout {

    private TextView tv_key;
    private TextView tv_value;
    private int mDrawableSize;//drawable大小

    public ValueView(Context context) {
        this(context, null);
    }

    public ValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ValueView);
        View mlLinearLayout = LayoutInflater.from(context).inflate(R.layout.x_value_item_layout, this, true);
        tv_key = mlLinearLayout.findViewById(R.id.tv_key);
        tv_value = mlLinearLayout.findViewById(R.id.tv_value);


        if (mTypedArray.hasValue(R.styleable.ValueView_keyImgSizes)) {
            mDrawableSize = mTypedArray.getDimensionPixelSize(R.styleable.ValueView_keyImgSizes, 30);//获取drawable大小，没设置时默认为50dp
        }
        if (mTypedArray.hasValue(R.styleable.ValueView_valueHintColor)) {
            int valueHintColor = mTypedArray.getColor(R.styleable.ValueView_valueHintColor, ContextCompat.getColor(context, R.color.huise));
            tv_value.setHintTextColor(valueHintColor);
        }

        if (mTypedArray.hasValue(R.styleable.ValueView_valueTextColor)) {
            int valueTextColor = mTypedArray.getColor(R.styleable.ValueView_valueTextColor, ContextCompat.getColor(context, R.color.huise));
            tv_value.setTextColor(valueTextColor);
        }

        if (mTypedArray.hasValue(R.styleable.ValueView_valueHintText)) {
            String valueHintText = mTypedArray.getString(R.styleable.ValueView_valueHintText);
            tv_value.setHint(valueHintText);
        }
        if (mTypedArray.hasValue(R.styleable.ValueView_valueText)) {
            String valueText = mTypedArray.getString(R.styleable.ValueView_valueText);
            setTextValue(valueText);
        }

        if (mTypedArray.hasValue(R.styleable.ValueView_valueImg)) {
            setTextValueImg(ContextCompat.getDrawable(context, mTypedArray.getResourceId(R.styleable.ValueView_valueImg, 0)));
        }

        if (mTypedArray.hasValue(R.styleable.ValueView_keyText)) {
            String keyText = mTypedArray.getString(R.styleable.ValueView_keyText);
            setTextKey(keyText);
        }

        if (mTypedArray.hasValue(R.styleable.ValueView_ketTextColor)) {
            int valueTextColor = mTypedArray.getColor(R.styleable.ValueView_ketTextColor, ContextCompat.getColor(context, R.color.anhei));
            tv_key.setTextColor(valueTextColor);
        }


        if (mTypedArray.hasValue(R.styleable.ValueView_keyImg)) {
            setTextKeyImg(ContextCompat.getDrawable(context, mTypedArray.getResourceId(R.styleable.ValueView_keyImg, 0)));
        }

        mTypedArray.recycle();
    }

    public void setTextValueImg(Drawable drawable) {
        tv_value.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setTextKeyImg(Drawable drawable) {
        drawable.setBounds(0, 0, mDrawableSize, mDrawableSize);
        tv_key.setCompoundDrawables(drawable, null, null, null);
    }

    public void setTextKey(String key) {
        tv_key.setText(key);
    }

    public void setTextValue(String value) {
        tv_value.setText(value);
    }
}
