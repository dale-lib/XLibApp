package com.dale.dialog.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dale.dialog.core.ImageViewerPopupView;
import com.dale.dialog.photoview.PhotoView;

import java.io.File;

public interface XPopupImageLoader {

    void loadSnapshot(@NonNull Object uri, @NonNull PhotoView snapshot, @Nullable ImageView srcView);


    View loadImage(int position, @NonNull Object uri, @NonNull ImageViewerPopupView popupView, @NonNull PhotoView snapshot, @NonNull ProgressBar progressBar);

    /**
     * 获取图片对应的文件
     *
     * @param context
     * @param uri
     * @return
     */
    File getImageFile(@NonNull Context context, @NonNull Object uri);
}
