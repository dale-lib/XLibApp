package com.dale.utils;

import java.math.BigDecimal;

public final class MathUtil {
    private static final int DEFAULT_DIV_SCALE = 10;
    private static final int digit = 2;

    public MathUtil() {
    }

    public static String twoNumber(int number) {
        BigDecimal bigDecimal = new BigDecimal(Integer.toString(number));
        return bigDecimal.setScale(2, 4).toString();
    }

    public static String twoNumber(float number) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(number));
        return bigDecimal.setScale(2, 4).toString();
    }

    public static String twoNumber(double number) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
        return bigDecimal.setScale(2, 4).toString();
    }

    public static String twoNumber(String number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2, 4).toString();
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    public static double divide(double v1, double v2) {
        return divide(v1, v2, 10);
    }

    public static double divide(double v1, double v2, int scale) {
        return divide(v1, v2, scale, 6);
    }

    public static double divide(double v1, double v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, round_mode).doubleValue();
        }
    }

    public static String divide(String v1, String v2) {
        return divide(v1, v2, 10);
    }

    public static String divide(String v1, String v2, int scale) {
        return divide(v1, v2, 10, 6);
    }

    public static String divide(String v1, String v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.divide(b2, scale, round_mode).toString();
        }
    }

    public static double round(double v, int scale) {
        return round(v, scale, 6);
    }

    public static double round(double v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.setScale(scale, round_mode).doubleValue();
        }
    }

    public static String round(String v, int scale) {
        return round(v, scale, 6);
    }

    public static String round(String v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(v);
            return b.setScale(scale, round_mode).toString();
        }
    }
}
