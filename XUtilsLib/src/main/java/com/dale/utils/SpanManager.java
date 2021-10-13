package com.dale.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SpanManager {

    /**
     * 对textview中的文字变色和点击事件
     *
     * @param view 目标view
     * @param s    原始字符串
     * @param sign 标记变色字符串
     * @param l    点击监听
     */
    public static void callServiceSpan(TextView view, String s, int color, String sign, View.OnClickListener l) {

        if (TextUtils.isEmpty(s)) {
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            return;
        }

        if (!s.contains(sign)) {
            return;
        }

        SpannableString ss = new SpannableString(s);

        int st = s.lastIndexOf(sign);

        int ed = st + sign.length();

        ss.setSpan(new ForegroundColorSpan(color), st, ed, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (l != null) {
                    l.onClick(widget);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
            }
        }, st, ed, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        view.setText(ss);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
