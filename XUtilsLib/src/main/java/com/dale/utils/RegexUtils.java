package com.dale.utils;


import androidx.collection.SimpleArrayMap;

import com.dale.constant.RegexConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public final class RegexUtils {

    private final static SimpleArrayMap<String, String> CITY_MAP = new SimpleArrayMap<>();

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 更多正则表达式： http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return whether input matches regex of simple mobile.
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isMobileSimple(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * Return whether input matches regex of exact mobile.
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isMobileExact(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
    }

    /**
     * Return whether input matches regex of telephone number.
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isTel(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_TEL, input);
    }

    /**
     * 返回输入是否与长度为15的身份证号码的regex匹配。
     *
     * @param input 15位身份证号
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isIDCard15(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD15, input);
    }

    /**
     * 返回输入是否与长度为18的身份证号码的regex匹配。
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isIDCard18(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ID_CARD18, input);
    }

    /**
     * 返回输入是否与长度为18的准确身份证号码的regex匹配。
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isIDCard18Exact(final CharSequence input) {
        if (isIDCard18(input)) {
            int[] factor = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            char[] suffix = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            if (CITY_MAP.isEmpty()) {
                CITY_MAP.put("11", "北京");
                CITY_MAP.put("12", "天津");
                CITY_MAP.put("13", "河北");
                CITY_MAP.put("14", "山西");
                CITY_MAP.put("15", "内蒙古");

                CITY_MAP.put("21", "辽宁");
                CITY_MAP.put("22", "吉林");
                CITY_MAP.put("23", "黑龙江");

                CITY_MAP.put("31", "上海");
                CITY_MAP.put("32", "江苏");
                CITY_MAP.put("33", "浙江");
                CITY_MAP.put("34", "安徽");
                CITY_MAP.put("35", "福建");
                CITY_MAP.put("36", "江西");
                CITY_MAP.put("37", "山东");

                CITY_MAP.put("41", "河南");
                CITY_MAP.put("42", "湖北");
                CITY_MAP.put("43", "湖南");
                CITY_MAP.put("44", "广东");
                CITY_MAP.put("45", "广西");
                CITY_MAP.put("46", "海南");

                CITY_MAP.put("50", "重庆");
                CITY_MAP.put("51", "四川");
                CITY_MAP.put("52", "贵州");
                CITY_MAP.put("53", "云南");
                CITY_MAP.put("54", "西藏");

                CITY_MAP.put("61", "陕西");
                CITY_MAP.put("62", "甘肃");
                CITY_MAP.put("63", "青海");
                CITY_MAP.put("64", "宁夏");
                CITY_MAP.put("65", "新疆");

                CITY_MAP.put("71", "台湾");
                CITY_MAP.put("81", "香港");
                CITY_MAP.put("82", "澳门");
                CITY_MAP.put("91", "国外");
            }
            if (CITY_MAP.get(input.subSequence(0, 2).toString()) != null) {
                int weightSum = 0;
                for (int i = 0; i < 17; ++i) {
                    weightSum += (input.charAt(i) - '0') * factor[i];
                }
                int idCardMod = weightSum % 11;
                char idCardLast = input.charAt(17);
                return idCardLast == suffix[idCardMod];
            }
        }
        return false;
    }

    /**
     * 返回输入是否与电子邮件的regex匹配
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isEmail(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_EMAIL, input);
    }

    /**
     * 返回输入是否与URL的regex匹配。
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isURL(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_URL, input);
    }

    /**
     * 返回输入是否与汉字正则表达式匹配。
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isZh(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_ZH, input);
    }

    /**
     * 用户名的regex。
     * <p>范围 for "a-z", "A-Z", "0-9", "_", "Chinese character"</p>
     * <p>不能以结尾"_"</p>
     * <p>6-20位之间</p>
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isUsername(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_USERNAME, input);
    }

    /**
     * 年月日 "yyyy-MM-dd".
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isDate(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_DATE, input);
    }

    /**
     * IP地址的regex。
     *
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isIP(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_IP, input);
    }

    /**
     * 返回匹配内容结果
     *
     * @param regex 正则表达式
     * @param input 待验证字符串
     * @return true: 验证通过  false :验证失败
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 返回与regex匹配的输入列表。
     *
     * @param regex 正则表达式
     * @param input 待验证字符串
     * @return 匹配到的结果集
     */
    public static List<String> getMatches(final String regex, final CharSequence input) {
        if (input == null) return Collections.emptyList();
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }


}
