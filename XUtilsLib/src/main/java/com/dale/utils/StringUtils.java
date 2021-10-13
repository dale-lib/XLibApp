package com.dale.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 字符串工具类
 */
public class StringUtils {

    // 换行字符串
    public static final String NEW_LINE_STR = System.getProperty("line.separator");


    /**
     * 获取字符串长度
     *
     * @param str 待校验的字符串
     * @return 字符串长度, 如果字符串为 null, 则返回 0
     */
    public static int length(final String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 待校验的字符串
     * @return true: 空  false:不为空
     */
    public static boolean isEmpty(final String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 清空字符串全部空格
     *
     * @param str 待处理字符串
     * @return 处理后的字符串
     */
    public static String toClearSpace(final String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll(" ", "");
    }

    /**
     * 追加换行
     *
     * @param number 换行数量
     * @return 指定数量的换行字符串
     */
    public static String appendLine(final int number) {
        StringBuilder builder = new StringBuilder();
        if (number > 0) {
            for (int i = 0; i < number; i++) {
                builder.append(NEW_LINE_STR);
            }
        }
        return builder.toString();
    }

    /**
     * 获取格式化后的字符串
     *
     * @param format 待格式化字符串
     * @param args   格式化参数
     * @return 格式化后的字符串
     */
    public static String getFormatString(final String format, final Object... args) {
        if (format == null) return null;
        try {
            if (args != null && args.length != 0) {
                return String.format(format, args);
            } else {
                return format;
            }
        } catch (Exception e) {
            LogUtils.e("getFormatString", "");
        }
        return null;
    }

    /**
     * 忽略大小写对比两个字符串是否相等
     *
     * @param a
     * @param b
     * @return true:相等  false:不相等
     */
    public static boolean equalsIgnoreCase(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return ((String) a).equalsIgnoreCase((String) b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 安全转换int类型
     *
     * @param value
     * @return 字符串转换后的int
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 安全转换long类型
     *
     * @param value
     * @return 字符串转换后的long
     */
    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 安全转换float类型
     *
     * @param value
     * @return 字符串转换后的float
     */
    public static float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0f;
        }
    }

    /**
     * 安全转换double类型
     *
     * @param value
     * @return 字符串转换后的double
     */
    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 安全转换Boolean类型
     *
     * @param value
     * @return 字符串转换后的Boolean
     */
    public static boolean parseBoolean(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 字符串拼接,线程不安全
     *
     * @param params 需要拼接的内容
     * @return 拼接后的字符串
     */
    public static String builder(Object... params) {
        StringBuilder buffer = new StringBuilder();
        for (Object o : params) {
            buffer.append(o);
        }
        return buffer.toString();
    }


    /**
     * 字符串拼接,线程安全
     *
     * @param params 需要拼接的内容
     * @return 拼接后的字符串
     */
    public static String buffer(Object... params) {
        StringBuffer buffer = new StringBuffer();
        for (Object o : params) {
            buffer.append(o);
        }
        return buffer.toString();
    }

    /**
     * 进行 URL 编码, 默认 UTF-8
     *
     * @param str 待处理字符串
     * @return UTF-8 编码格式 URL 编码后的字符串
     */
    public static String toUrlEncode(final String str) {
        return toUrlEncode(str, "UTF-8");
    }

    /**
     * 进行 URL 编码
     *
     * @param str 待处理字符串
     * @param enc 编码格式
     * @return 指定编码格式 URL 编码后的字符串
     */
    public static String toUrlEncode(final String str, final String enc) {
        if (isEmpty(str)) return "";
        try {
            return URLEncoder.encode(str, enc);
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * 进行 URL 解码, 默认 UTF-8
     *
     * @param str 待处理字符串
     * @return UTF-8 编码格式 URL 解码后的字符串
     */
    public static String toUrlDecode(final String str) {
        return toUrlDecode(str, "UTF-8");
    }

    /**
     * 进行 URL 解码
     *
     * @param str 待处理字符串
     * @param enc 解码格式
     * @return 指定编码格式 URL 解码后的字符串
     */
    public static String toUrlDecode(final String str, final String enc) {
        if (isEmpty(str)) return "";
        try {
            return URLDecoder.decode(str, enc);
        } catch (Exception e) {
        }
        return "";
    }

}
