package com.dale.utils;

import java.util.Random;


/**
 * 随机工具类
 */
public final class RandomUtils {

    private RandomUtils() {
    }

    // 0123456789
    public static final char[] NUMBERS = new char[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57};

    // abcdefghijklmnopqrstuvwxyz
    public static final char[] LOWER_CASE_LETTERS = new char[]{97, 98, 99,
            100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112,
            113, 114, 115, 116, 117, 118, 119, 120, 121, 122};

    // ABCDEFGHIJKLMNOPQRSTUVWXYZ
    public static final char[] CAPITAL_LETTERS = new char[]{65, 66, 67, 68,
            69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85,
            86, 87, 88, 89, 90};

    // abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
    public static final char[] LETTERS = new char[]{97, 98, 99, 100, 101,
            102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114,
            115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71,
            72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88,
            89, 90};

    // 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
    public static final char[] NUMBERS_AND_LETTERS = new char[]{48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104,
            105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117,
            118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
            75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};


    /**
     * 获取伪随机 boolean 值
     *
     * @return 随机 boolean 值
     */
    public static boolean nextBoolean() {
        return new Random().nextBoolean();
    }

    /**
     * 获取伪随机 byte[]
     *
     * @param data 随机数据源
     * @return 随机 byte[]
     */
    public static byte[] nextBytes(final byte[] data) {
        if (data == null) return null;
        try {
            new Random().nextBytes(data);
        } catch (Exception e) {
        }
        return data;
    }

    /**
     * 获取伪随机 double 值
     *
     * @return 随机 double 值
     */
    public static double nextDouble() {
        return new Random().nextDouble();
    }

    /**
     * 获取伪随机高斯分布值
     *
     * @return 伪随机高斯分布值
     */
    public static double nextGaussian() {
        return new Random().nextGaussian();
    }

    /**
     * 获取伪随机 float 值
     *
     * @return 随机 float 值
     */
    public static float nextFloat() {
        return new Random().nextFloat();
    }

    /**
     * 获取伪随机 int 值
     *
     * @return 随机 int 值
     */
    public static int nextInt() {
        return new Random().nextInt();
    }

    /**
     * 获取伪随机 int 值 - 该值介于 [0, n) 的区间
     *
     * @param number 最大随机值
     * @return 随机介于 [0, n) 的区间值
     */
    public static int nextInt(final int number) {
        if (number <= 0) return 0;
        return new Random().nextInt(number);
    }

    /**
     * 获取伪随机 long 值
     *
     * @return 随机 long 值
     */
    public static long nextLong() {
        return new Random().nextLong();
    }

    // =

    /**
     * 获取数字自定义长度的随机数
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomNumbers(final int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 获取小写字母自定义长度的随机数
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomLowerCaseLetters(final int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 获取大写字母自定义长度的随机数
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomCapitalLetters(final int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 获取大小写字母自定义长度的随机数
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomLetters(final int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 获取数字、大小写字母自定义长度的随机数
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomNumbersAndLetters(final int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 获取自定义数据自定义长度的随机数
     *
     * @param source 随机的数据源
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandom(final String source, final int length) {
        if (source == null) return null;
        return getRandom(source.toCharArray(), length);
    }

    /**
     * 获取 char[] 内的随机数
     *
     * @param chars  随机的数据源
     * @param length 需要最终长度
     * @return 随机字符串
     */
    public static String getRandom(final char[] chars, final int length) {
        if (length > 0 && chars != null && chars.length != 0) {
            StringBuilder builder = new StringBuilder(length);
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                builder.append(chars[random.nextInt(chars.length)]);
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 获取 0 - 最大随机数之间的随机数
     *
     * @param max 最大随机数
     * @return 随机介于 [0, max) 的区间值
     */
    public static int getRandom(final int max) {
        return getRandom(0, max);
    }

    /**
     * 获取两个数之间的随机数 ( 不含最大随机数, 需要 + 1)
     *
     * @param min 最小随机数
     * @param max 最大随机数
     * @return 随机介于 [min, mHax) 的区间值
     */
    public static int getRandom(final int min, final int max) {
        if (min > max) {
            return 0;
        } else if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

}