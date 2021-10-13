package com.dale.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.dale.constant.LibApplication;

import java.io.File;

/**
 * SDCard 工具类
 */
public final class SDCardUtils {

    private SDCardUtils() {
    }

    /**
     * 判断内置 SDCard 是否正常挂载
     *
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取内置 SDCard File
     *
     * @return {@link File}
     */
    public static File getSDCardFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取内置 SDCard 绝对路径
     * <pre>
     *     结尾无添加 File.separator
     * </pre>
     *
     * @return 内置 SDCard 绝对路径
     */
    public static String getSDCardPath() {
        return getSDCardFile().getAbsolutePath();
    }


    /**
     * 获取内置 SDCard 空间总大小
     *
     * @return 内置 SDCard 空间总大小
     */
    public static String getAllBlockSizeFormat() {
        try {
            long size = getAllBlockSize(Environment.getExternalStorageDirectory().getPath());
            // 格式化
            return Formatter.formatFileSize(LibApplication.getApp(), size);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取内置 SDCard 空闲空间大小
     *
     * @return 内置 SDCard 空闲空间大小
     */
    public static String getAvailableBlocksFormat() {
        try {
            long size = getAvailableBlocks(Environment.getExternalStorageDirectory().getPath());
            // 格式化
            return Formatter.formatFileSize(LibApplication.getApp(), size);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取内置 SDCard 已使用空间大小
     *
     * @return 内置 SDCard 已使用空间大小
     */
    public static String getUsedBlocksFormat() {
        try {
            long size = getUsedBlocks(Environment.getExternalStorageDirectory().getPath());
            // 格式化
            return Formatter.formatFileSize(LibApplication.getApp(), size);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

    // =

    /**
     * 获取内置 SDCard 空间总大小
     *
     * @return 内置 SDCard 空间总大小
     */
    public static long getAllBlockSize() {
        try {
            return getAllBlockSize(Environment.getExternalStorageDirectory().getPath());
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取内置 SDCard 空闲空间大小
     *
     * @return 内置 SDCard 空闲空间大小
     */
    public static long getAvailableBlocks() {
        try {
            return getAvailableBlocks(Environment.getExternalStorageDirectory().getPath());
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取内置 SDCard 已使用空间大小
     *
     * @return 内置 SDCard 已使用空间大小
     */
    public static long getUsedBlocks() {
        try {
            return getUsedBlocks(Environment.getExternalStorageDirectory().getPath());
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取对应路径的空间总大小
     *
     * @param path 路径
     * @return 对应路径的空间总大小
     */
    public static long getAllBlockSize(final String path) {
        try {
            // 获取路径的存储空间信息
            StatFs statFs = new StatFs(path);
            // 单个数据块的大小、数据块数量
            long blockSize, blockCount;
            // 版本兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.getBlockSizeLong();
                blockCount = statFs.getBlockCountLong();
            } else {
                blockSize = statFs.getBlockSize();
                blockCount = statFs.getBlockCount();
            }
            // 返回空间总大小
            return (blockCount * blockSize);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 获取对应路径的空闲空间大小
     *
     * @param path 路径
     * @return 对应路径的空闲空间大小
     */
    public static long getAvailableBlocks(final String path) {
        try {
            // 获取路径的存储空间信息
            StatFs statFs = new StatFs(path);
            // 单个数据块的大小、空闲的数据块数量
            long blockSize, availableBlocks;
            // 版本兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = statFs.getBlockSize();
                availableBlocks = statFs.getAvailableBlocks();
            }
            // 返回空闲空间
            return (availableBlocks * blockSize);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 获取对应路径已使用空间大小
     *
     * @param path 路径
     * @return 对应路径已使用空间大小
     */
    public static long getUsedBlocks(final String path) {
        try {
            // 获取路径的存储空间信息
            StatFs statFs = new StatFs(path);
            // 单个数据块的大小、数据块数量、空闲的数据块数量
            long blockSize, blockCount, availableBlocks;
            // 版本兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.getBlockSizeLong();
                blockCount = statFs.getBlockCountLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = statFs.getBlockSize();
                blockCount = statFs.getBlockCount();
                availableBlocks = statFs.getAvailableBlocks();
            }
            // 返回已使用空间大小
            return ((blockCount - availableBlocks) * blockSize);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return 0L;
    }


    /**
     * 获取 App Cache 文件夹地址
     *
     * @return App Cache 文件夹地址
     */
    public static String getDiskCacheDir() {
        String cachePath;
        if (isSDCardEnable()) { // 判断 SDCard 是否挂载
            cachePath = LibApplication.getApp().getExternalCacheDir().getPath();//  SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
        } else {
            cachePath = LibApplication.getApp().getCacheDir().getPath();//  /data/data/<application package>/cache目录
        }
        // 防止不存在目录文件, 自动创建
        FileUtils.createOrExistsDir(cachePath);
        // 返回文件存储地址
        return cachePath;
    }

    /**
     * 获取 App Cache 路径 File
     *
     * @param filePath 文件路径
     * @return App Cache 路径 File
     */
    public static File getCacheFile(final String filePath) {
        return FileUtils.getFileByPath(getCachePath(filePath));
    }

    /**
     * 获取 App Cache 路径
     *
     * @param filePath 文件路径
     * @return App Cache 路径
     */
    public static String getCachePath(final String filePath) {
        if (filePath == null) return null;
        // 获取缓存地址
        String cachePath = new File(getDiskCacheDir(), filePath).getAbsolutePath();
        // 防止不存在目录文件, 自动创建
        FileUtils.createOrExistsDir(cachePath);
        // 返回缓存地址
        return cachePath;
    }

}
