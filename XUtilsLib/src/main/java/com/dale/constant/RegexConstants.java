package com.dale.constant;

/**
 * 正则表达式常量
 */
public final class RegexConstants {

    /**
     * Regex of simple mobile.
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * Regex of exact mobile.
     * <p>china mobile: 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198</p>
     * <p>china unicom: 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186</p>
     * <p>china telecom: 133, 153, 173, 177, 180, 181, 189, 199, 191</p>
     * <p>global star: 1349</p>
     * <p>virtual operator: 170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    /**
     * Regex of telephone number.
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 15位身份证正则表达式
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 18位身份证正则表达式
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * email正则表达式。
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * URL正则表达式。
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * 汉字正则表达式。
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 用户名的regex。
     * <p>范围 for "a-z", "A-Z", "0-9", "_", "Chinese character"</p>
     * <p>不能以结尾"_"</p>
     * <p>6-20位之间</p>
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 年月日 "yyyy-MM-dd".
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * IP地址的regex。
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    ///////////////////////////////////////////////////////////////////////////
    // The following come from http://tool.oschina.net/regex
    ///////////////////////////////////////////////////////////////////////////

    /**
     * QQ号码的regex。
     */
    public static final String REGEX_QQ_NUM = "[1-9][0-9]{4,}";
    /**
     * 中国邮政编码法规
     */
    public static final String REGEX_CHINA_POSTAL_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正整数的正则表达式。
     */
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 负整数的正则表达式。
     */
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 整数的正则表达式。
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";

}
