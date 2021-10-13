package com.dale.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.core.content.ContextCompat;

import com.dale.constant.LibApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public final class ImageUtils {

    /**
     * Bitmap 转字节
     *
     * @param bitmap bitmap.
     * @param format 位图的格式.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节转 bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable 转 bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转 drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(LibApplication.getApp().getResources(), bitmap);
    }

    /**
     * Drawable 转 bytes.
     *
     * @param drawable The drawable.
     * @param format   位图的格式.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * Bytes 转 drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     * View 转 bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * 通过文件获取bitmap
     *
     * @param file The file.
     * @return bitmap
     */
    public static Bitmap getBitmap(final File file) {
        if (file == null) return null;
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /**
     * 通过文件获取bitmap
     *
     * @param file      The file.
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return bitmap
     */
    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * 通过文件路径获取bitmap
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * 通过文件路径获取bitmap
     *
     * @param filePath  文件路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 通过流获取bitmap
     *
     * @param is The input stream.
     * @return bitmap
     */
    public static Bitmap getBitmap(final InputStream is) {
        if (is == null) return null;
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 通过流获取bitmap
     *
     * @param is        The input stream.
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return bitmap
     */
    public static Bitmap getBitmap(final InputStream is, final int maxWidth, final int maxHeight) {
        if (is == null) return null;
        byte[] bytes = input2Byte(is);
        return getBitmap(bytes, 0, maxWidth, maxHeight);
    }


    /**
     * Return bitmap.
     *
     * @param data      The data.
     * @param offset    The offset.
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return bitmap
     */
    public static Bitmap getBitmap(final byte[] data,
                                   final int offset,
                                   final int maxWidth,
                                   final int maxHeight) {
        if (data.length == 0) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, data.length, options);
    }

    /**
     * 通过res获取bitmap
     *
     * @param resId The resource id.
     * @return bitmap
     */
    public static Bitmap getBitmap(@DrawableRes final int resId) {
        Drawable drawable = ContextCompat.getDrawable(LibApplication.getApp(), resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Return bitmap.
     *
     * @param resId     The resource id.
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return bitmap
     */
    public static Bitmap getBitmap(@DrawableRes final int resId,
                                   final int maxWidth,
                                   final int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        final Resources resources = LibApplication.getApp().getResources();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }


    /**
     * 返回缩放后的bitmap
     *
     * @param src       原始 bitmap.
     * @param newWidth  缩放后的宽度.
     * @param newHeight 缩放后的高度.
     * @return 缩放后的 bitmap
     */
    public static Bitmap scale(final Bitmap src, final int newWidth, final int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    /**
     * 返回缩放后的bitmap
     *
     * @param src       原始 bitmap.
     * @param newWidth  缩放后的宽度.
     * @param newHeight 缩放后的高度.
     * @param recycle   如果为true，则回收位图的源，否则为false。
     * @return 缩放后的 bitmap
     */
    public static Bitmap scale(final Bitmap src,
                               final int newWidth,
                               final int newHeight,
                               final boolean recycle) {
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * 返回缩放后的bitmap
     *
     * @param src         缩放的图片
     * @param scaleWidth  宽度的比例
     * @param scaleHeight 高度的比例
     * @return 缩放后的bitmap
     */
    public static Bitmap scale(final Bitmap src, final float scaleWidth, final float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    /**
     * 返回缩放后的bitmap
     *
     * @param src         缩放的图片
     * @param scaleWidth  宽度的比例
     * @param scaleHeight 高度的比例
     * @param recycle     如果为true，则回收位图的源，否则为false。
     * @return 缩放后的bitmap
     */
    public static Bitmap scale(final Bitmap src,
                               final float scaleWidth,
                               final float scaleHeight,
                               final boolean recycle) {

        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * 返回旋转的位图
     *
     * @param src     位图的源
     * @param degrees 旋转角度
     * @param px      轴点的X坐标
     * @param py      轴点的Y坐标
     * @return 旋转的位图
     */
    public static Bitmap rotate(final Bitmap src,
                                final int degrees,
                                final float px,
                                final float py) {
        return rotate(src, degrees, px, py, false);
    }

    /**
     * 返回旋转的位图
     *
     * @param src     位图的源
     * @param degrees The number of degrees.
     * @param px      轴点的X坐标
     * @param py      轴点的Y坐标
     * @param recycle 如果为true，则回收位图的源，否则为false。
     * @return 旋转的位图
     */
    public static Bitmap rotate(final Bitmap src,
                                final int degrees,
                                final float px,
                                final float py,
                                final boolean recycle) {

        if (degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * 返回旋转的度数
     *
     * @param filePath 文件路径
     * @return 旋转的度数
     */
    public static int getRotateDegree(final String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            );
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 返回圆形位图
     *
     * @param src 位图的源
     * @return 圆形位图
     */
    public static Bitmap toRound(final Bitmap src) {
        return toRound(src, 0, 0, false);
    }

    /**
     * 返回圆形位图
     *
     * @param src     位图的源
     * @param recycle 如果为true，则回收位图的源，否则为false
     * @return 圆形位图
     */
    public static Bitmap toRound(final Bitmap src, final boolean recycle) {
        return toRound(src, 0, 0, recycle);
    }

    /**
     * 返回圆形位图
     *
     * @param src         位图的源
     * @param borderSize  边框的大小
     * @param borderColor 边框的颜色
     * @return 圆形位图
     */
    public static Bitmap toRound(final Bitmap src,
                                 @IntRange(from = 0) int borderSize,
                                 @ColorInt int borderColor) {
        return toRound(src, borderSize, borderColor, false);
    }

    /**
     * 返回圆形位图
     *
     * @param src         位图的源
     * @param recycle     如果为true，则回收位图的源，否则为false
     * @param borderSize  边框的大小
     * @param borderColor 边框的颜色
     * @return 圆形位图
     */
    public static Bitmap toRound(final Bitmap src,
                                 @IntRange(from = 0) int borderSize,
                                 @ColorInt int borderColor,
                                 final boolean recycle) {

        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        float center = size / 2f;
        RectF rectF = new RectF(0, 0, width, height);
        rectF.inset((width - size) / 2f, (height - size) / 2f);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        if (width != height) {
            matrix.preScale((float) size / width, (float) size / height);
        }
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        canvas.drawRoundRect(rectF, center, center, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            float radius = center - borderSize / 2f;
            canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * 返回圆角位图
     *
     * @param src    位图的源
     * @param radius 角的半径.
     * @return 圆角位图
     */
    public static Bitmap toRoundCorner(final Bitmap src, final float radius) {
        return toRoundCorner(src, radius, 0, 0, false);
    }

    /**
     * 返回圆角位图
     *
     * @param src     位图的源
     * @param radius  角的半径.
     * @param recycle 如果为true，则回收位图的源，否则为false
     * @return 圆角位图
     */
    public static Bitmap toRoundCorner(final Bitmap src,
                                       final float radius,
                                       final boolean recycle) {
        return toRoundCorner(src, radius, 0, 0, recycle);
    }

    /**
     * 返回圆角位图
     *
     * @param src         位图的源
     * @param radius      角的半径.
     * @param borderSize  边框的大小
     * @param borderColor 边框的颜色
     * @return 圆角位图
     */
    public static Bitmap toRoundCorner(final Bitmap src,
                                       final float radius,
                                       @IntRange(from = 0) int borderSize,
                                       @ColorInt int borderColor) {
        return toRoundCorner(src, radius, borderSize, borderColor, false);
    }

    /**
     * 返回圆角位图
     *
     * @param src         位图的源
     * @param radius      角的半径.
     * @param borderSize  边框的大小
     * @param borderColor 边框的颜色
     * @param recycle     如果为true，则回收位图的源，否则为false
     * @return 圆角位图
     */
    public static Bitmap toRoundCorner(final Bitmap src,
                                       final float radius,
                                       @IntRange(from = 0) int borderSize,
                                       @ColorInt int borderColor,
                                       final boolean recycle) {

        int width = src.getWidth();
        int height = src.getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        RectF rectF = new RectF(0, 0, width, height);
        float halfBorderSize = borderSize / 2f;
        rectF.inset(halfBorderSize, halfBorderSize);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * 返回带边框的圆角位图
     *
     * @param src          位图的源
     * @param borderSize   边框的大小
     * @param color        边框的颜色
     * @param cornerRadius 角的半径.
     * @return 带边框的圆角位图
     */
    public static Bitmap addCornerBorder(final Bitmap src,
                                         @IntRange(from = 1) final int borderSize,
                                         @ColorInt final int color,
                                         @FloatRange(from = 0) final float cornerRadius) {
        return addBorder(src, borderSize, color, false, cornerRadius, false);
    }

    /**
     * 返回带边框的圆角位图
     *
     * @param src          位图的源
     * @param borderSize   边框的大小
     * @param color        边框的颜色
     * @param cornerRadius 角的半径.
     * @param recycle      如果为true，则回收位图的源，否则为false
     * @return 带边框的圆角位图
     */
    public static Bitmap addCornerBorder(final Bitmap src,
                                         @IntRange(from = 1) final int borderSize,
                                         @ColorInt final int color,
                                         @FloatRange(from = 0) final float cornerRadius,
                                         final boolean recycle) {
        return addBorder(src, borderSize, color, false, cornerRadius, recycle);
    }

    /**
     * 返回带边框的圆角位图
     *
     * @param src        位图的源
     * @param borderSize 边框的大小
     * @param color      边框的颜色
     * @return 带边框的圆角位图
     */
    public static Bitmap addCircleBorder(final Bitmap src,
                                         @IntRange(from = 1) final int borderSize,
                                         @ColorInt final int color) {
        return addBorder(src, borderSize, color, true, 0, false);
    }

    /**
     * 返回带边框的圆角位图
     *
     * @param src        位图的源
     * @param borderSize 边框的大小
     * @param color      边框的颜色
     * @param recycle    如果为true，则回收位图的源，否则为false
     * @return 带边框的圆角位图
     */
    public static Bitmap addCircleBorder(final Bitmap src,
                                         @IntRange(from = 1) final int borderSize,
                                         @ColorInt final int color,
                                         final boolean recycle) {
        return addBorder(src, borderSize, color, true, 0, recycle);
    }

    /**
     * 返回带边框的位图。
     *
     * @param src          位图的源
     * @param borderSize   边框的大小
     * @param color        边框的颜色
     * @param isCircle     绘制圆为真，绘制角为假
     * @param cornerRadius 角的半径.
     * @param recycle      如果为true，则回收位图的源，否则为false
     * @return 带边框的位图
     */
    private static Bitmap addBorder(final Bitmap src,
                                    @IntRange(from = 1) final int borderSize,
                                    @ColorInt final int color,
                                    final boolean isCircle,
                                    final float cornerRadius,
                                    final boolean recycle) {

        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        int width = ret.getWidth();
        int height = ret.getHeight();
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        if (isCircle) {
            float radius = Math.min(width, height) / 2f - borderSize / 2f;
            canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        } else {
            int halfBorderSize = borderSize >> 1;
            RectF rectF = new RectF(halfBorderSize, halfBorderSize,
                    width - halfBorderSize, height - halfBorderSize);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        }
        return ret;
    }

    /**
     * 返回alpha位图
     *
     * @param src 位图的源
     * @return alpha位图
     */
    public static Bitmap toAlpha(final Bitmap src) {
        return toAlpha(src, false);
    }

    /**
     * 返回alpha位图
     *
     * @param src     位图的源
     * @param recycle 如果为true，则回收位图的源，否则为false
     * @return alpha位图
     */
    public static Bitmap toAlpha(final Bitmap src, final Boolean recycle) {

        Bitmap ret = src.extractAlpha();
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // about compress
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 使用比例返回压缩位图。
     *
     * @param src       位图的源
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 压缩位图
     */
    public static Bitmap compressByScale(final Bitmap src,
                                         final int newWidth,
                                         final int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    /**
     * 使用比例返回压缩位图。
     *
     * @param src       位图的源
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   如果为true，则回收位图的源，否则为false
     * @return 压缩位图
     */
    public static Bitmap compressByScale(final Bitmap src,
                                         final int newWidth,
                                         final int newHeight,
                                         final boolean recycle) {
        return scale(src, newWidth, newHeight, recycle);
    }

    /**
     * 使用比例返回压缩位图。
     *
     * @param src         位图的源
     * @param scaleWidth  宽度的比例
     * @param scaleHeight 高度的比例
     * @return 压缩位图
     */
    public static Bitmap compressByScale(final Bitmap src,
                                         final float scaleWidth,
                                         final float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    /**
     * 使用比例返回压缩位图。
     *
     * @param src         位图的源
     * @param scaleWidth  宽度的比例
     * @param scaleHeight 高度的比例
     * @param recycle     如果为true，则回收位图的源，否则为false
     * @return 压缩位图
     */
    public static Bitmap compressByScale(final Bitmap src,
                                         final float scaleWidth,
                                         final float scaleHeight,
                                         final boolean recycle) {
        return scale(src, scaleWidth, scaleHeight, recycle);
    }


    /**
     * 返回样本大小。
     *
     * @param options   The options.
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度.
     * @return the sample size
     */
    private static int calculateInSampleSize(final BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    private static byte[] input2Byte(final InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
