package com.dale.utils.toast;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import static android.view.View.NO_ID;

public class ToastViewHolder {
    private SparseArray<View> mViews = new SparseArray<>();
    private View mView;

    public ToastViewHolder(View view) {
        mView = view;
    }

    @Nullable
    public final <T extends View> T findView(@IdRes int id) {
        if (id == NO_ID) {
            return null;
        }

        try {
            View view = mViews.get(id);
            if (view == null) {
                view = mView.findViewById(id);
                if (view != null) {
                    mViews.put(id, view);
                }
            }
            return (T) view;
        } catch (Exception e) {
            return null;
        }
    }

    public void setText(@IdRes int id, String message) {
        if (message == null) return;
        TextView tv = findView(id);
        if (tv != null) {
            tv.setText(message);
        }
    }


    public View getView() {
        return mView;
    }
}
