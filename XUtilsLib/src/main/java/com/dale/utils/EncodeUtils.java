package com.dale.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public final class EncodeUtils {

    private EncodeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 返回urlencoded字符串
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, "UTF-8");
    }

    /**
     * 返回urlencoded字符串
     */
    public static String urlEncode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            return URLEncoder.encode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 返回urldecode字符串
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * 返回urldecode字符串
     */
    public static String urlDecode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            return URLDecoder.decode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }


}
