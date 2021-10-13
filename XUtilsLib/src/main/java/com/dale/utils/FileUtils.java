package com.dale.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public final class FileUtils {

    /**
     * 缓存目录
     * 1.有sdcard: sdcard/Android/包名/cache/...
     * 2.无sdcard:/data/data/包名/cache/...
     * 3.preference 保存目录 ：/data/data/包名/shared_prefs/...
     */

    //语音存放目录（如语音录制）
    public static final String AUDIO = "audio";
    //视频存放位目录（如视频录制）
    public static final String VIDEO = "video";
    //图片存放位置（Glide图片加载）
    public static final String PIC = "pic";
    //SharedPreferences 保存目录
    public static final String PREFERENCE = "cache";

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(SDCardUtils.getDiskCacheDir());
        sb.append(File.separator);
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createOrExistsDir(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 根据路径获取File
     *
     * @param filePath 文件；路径
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 根据路径判断文件是否存在
     *
     * @param filePath 文件路径
     * @return true: 存在  false: 不存在
     */
    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断File存在
     *
     * @return rue: 存在  false: 不存在
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }


    /**
     * 判断一个路径是否是目录
     *
     * @param dirPath 待判定目录路径
     * @return ture:是一个目录
     */
    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * 判断一个文件是否是目录
     *
     * @param file 待判定文件
     * @return ture:是一个目录
     */
    public static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 判断否是文件
     *
     * @param filePath 文件路径
     * @return ture:是文件 false:不是文件
     */
    public static boolean isFile(final String filePath) {
        return isFile(getFileByPath(filePath));
    }


    /**
     * 判断否是文件
     *
     * @param file
     * @return ture:是文件 false:不是文件
     */
    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 创建一个目录
     *
     * @param dirPath 目录路径
     * @return true:创建成功
     */
    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }


    /**
     * 创建一个目录
     *
     * @param file 目录
     * @return true:创建成功
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 创建一个文件
     *
     * @param filePath 文件路径
     * @return true:创建成功
     */
    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }


    /**
     * 创建一个文件
     *
     * @param file
     * @return true:创建成功
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除一个文件或目录
     *
     * @param filePath 待删除的文件或目录路径
     * @return true:删除成功
     */
    public static boolean delete(final String filePath) {
        return delete(getFileByPath(filePath));
    }


    /**
     * 删除一个文件或目录
     *
     * @param file 待删除的文件或目录
     * @return true:删除成功
     */
    public static boolean delete(final File file) {
        if (file == null) return false;
        if (file.isDirectory()) {
            return deleteDir(file);
        }
        return deleteFile(file);
    }

    /**
     * 删除一个目录
     *
     * @param dirPath 待删除的目录路径
     * @return true:删除成功
     */
    public static boolean deleteDir(final String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }


    /**
     * 删除一个目录
     *
     * @param dir 待删除的目录
     * @return true:删除成功
     */
    public static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除文件
     *
     * @param srcFilePath 待删除的文件路径
     * @return true:删除成功
     */
    public static boolean deleteFile(final String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }


    /**
     * 删除文件
     *
     * @param file 待删除的文件
     * @return true:删除成功
     */
    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }


    /**
     * 返回一个目录的大小
     *
     * @param dirPath 目录
     * @return 文件大小（如：KB,MB）
     */
    public static String getDirSize(final String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }


    /**
     * 返回一个目录的大小
     *
     * @param dir 目录
     * @return 文件大小（如：KB,MB）
     */
    public static String getDirSize(final File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }


    /**
     * 返回一个文件的大小
     *
     * @param file 文件
     * @return 返回文件大小
     */
    public static String getFileSize(final File file) {
        long len = isFile(file) ? file.length() : 0;
        return len == -1 ? "" : byte2FitMemorySize(len);
    }


    /**
     * 返回一个目录的大小
     *
     * @param dirPath 目录路径
     * @return 目录文件大小
     */
    public static long getDirLength(final String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    /**
     * 返回一个目录的大小
     *
     * @param dir 目录路径
     * @return 目录文件大小
     */
    public static long getDirLength(final File dir) {
        if (!isDir(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    /**
     * 获取目录的名字
     *
     * @param file 文件
     * @return 文件目录名字
     */
    public static String getDirName(final File file) {
        if (file == null) return "";
        return getDirName(file.getAbsolutePath());
    }


    /**
     * 获取目录的名字
     *
     * @param filePath 文件路径
     * @return 文件目录名字
     */
    public static String getDirName(final String filePath) {
        if (isSpace(filePath)) return "";
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    /**
     * 获取文件的名字
     *
     * @param file 文件
     * @return 返回文件的名字
     */
    public static String getFileName(final File file) {
        if (file == null) return "";
        return getFileName(file.getAbsolutePath());
    }


    /**
     * 获取文件的名字
     *
     * @param filePath 文件的路径
     * @return 返回文件的名字
     */
    public static String getFileName(final String filePath) {
        if (isSpace(filePath)) return "";
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }


    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.3fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.3fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.3fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.3fGB", (double) byteNum / 1073741824);
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
