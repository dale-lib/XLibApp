package com.dale.utils;

import android.text.TextUtils;

import com.dale.constant.TimeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static final SimpleDateFormat DATE_FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");

    private TimeUtils() {
    }


    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private static SimpleDateFormat getDefaultFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    /**
     * 毫秒转年月日时分秒
     *
     * @param millis 毫秒
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String millis2String(final long millis) {
        return getDefaultFormat().format(new Date(millis));
    }


    /**
     * 字符串年月日时分秒转毫秒
     *
     * @param time 年月日时分秒时间
     * @return 毫秒数
     */
    public static long string2Millis(final String time) {
        try {
            return getDefaultFormat().parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Data转毫秒
     *
     * @param date 时间
     * @return 毫秒值
     */
    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * 毫秒值转Data时间
     *
     * @param millis 毫秒值
     * @return Data
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * 将年月日的int转成date
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     */
    public static Date getDate(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day);
        return mCalendar.getTime();
    }

    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy/MM/dd
     * @param end   终止日期，格式yyyy/MM/dd
     * @return 两个日期相差天数
     */
    public static int getIntervalDays2(Date strat, Date end) {
        return (int) ((strat.getTime() - end.getTime()) / (3600 * 24 * 1000));
    }

    /**
     * desc: 获取当月的第几个月 如上个月 amount 传-1
     *
     * @author Jeff created on 2018/10/7 11:46
     */
    public static Date getMonth(int amount) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, amount);
        return c.getTime();
    }

    /**
     * desc: 一个月的第几天
     *
     * @author Jeff created on 2018/10/7 18:52
     */
    public static Date getMonthDay(int day) {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        //设置为1号,当前日期既为本月第day天
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * desc: 获取当月的第一天
     */
    public static Date getMonthFirst() {
        return getMonthDay(1);
    }

    /**
     * 日期转相应模板格式的字符串
     *
     * @param date   Date
     * @param format 模板
     *               y 年
     *               Y 周年
     *               M 月
     *               w 一年中的第几周
     *               W 一个月中的第几周
     *               d 一年中的第几天
     *               D 一个月中的第几天
     *               F 所在周是当月的第几周
     *               E 星期的英文名称
     *               u 一星期中的第几天 星期一是1
     *               a 上午还是下午的标记 AM是上午 PM是下午
     *               H 一天中的小时 0 - 23
     *               k 一天中的小时 1 - 24
     *               K 一天中的小时 0 - 11
     *               h 一天中的小时 1 - 12
     *               m 一小时中的分钟
     *               s 一分钟的秒
     *               S 毫秒
     *               z 时区 GMT-08:00
     *               Z 时区号码 -0800
     *               X 时区 -08;-0800;-08:00
     * @return 时间模板
     * 例子 :
     * Date and Time Pattern	               Result
     * "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
     * "EEE, MMM d, ''yy"	              Wed, Jul 4, '01
     * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"	2001-07-04T12:08:56.235-07:00
     */
    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (TextUtils.isEmpty(format)) {
            return "";
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }

        return null;
    }

    /**
     * String模板格式 的时间转成Date实例
     *
     * @param strDate 时间字符串
     * @param format  时间字符串的格式
     * @return Date实例
     */
    public static Date toDate(String strDate, String format) {
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        if (TextUtils.isEmpty(format)) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            LogUtils.e(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 年月日时分秒转Date
     *
     * @param time 年月日时间
     * @return Data
     */
    public static Date string2Date(final String time) {
        try {
            return getDefaultFormat().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得今天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static Date getToday() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.getTime();
    }

    /**
     * 获得昨天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static Date getYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -1);
        return mCalendar.getTime();
    }

    /**
     * 获得前天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static Date getBeforeYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -2);
        return mCalendar.getTime();
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return date实例
     */
    public static Date getOtherDay(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return mCalendar.getTime();
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return date实例
     */
    public static Date getOtherDate(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return mCalendar.getTime();
    }


    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static Date getCalcDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得一个计算十分秒之后的日期对象
     *
     * @param date    date实例
     * @param hOffset 时偏移量，可为负
     * @param mOffset 分偏移量，可为负
     * @param sOffset 秒偏移量，可为负
     * @return date实例
     */
    public static Date getCalcTime(Date date, int hOffset, int mOffset, int sOffset) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.HOUR_OF_DAY, hOffset);
        cal.add(Calendar.MINUTE, mOffset);
        cal.add(Calendar.SECOND, sOffset);
        return cal.getTime();
    }

    /**
     * 根据指定的年月日小时分秒，返回一个java.Util.Date对象。
     *
     * @param year      年
     * @param month     月 0-11
     * @param date      日
     * @param hourOfDay 小时 0-23
     * @param minute    分 0-59
     * @param second    秒 0-59
     * @return 一个Date对象
     */
    public static Date getDate(int year, int month, int date, int hourOfDay,
                               int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }


    /**
     * 获得年月日数据
     *
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    public static int[] getYearMonthAndDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] arr = new int[3];
        arr[0] = calendar.get(Calendar.YEAR);
        arr[1] = calendar.get(Calendar.MONTH);
        arr[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return arr;
    }

    /**
     * 获取http返回的请求头中的日期
     *
     * @param dateString 请求头中的日期字符串
     * @return Date实例
     */
    public static Date parseHttpHeaderDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Date date;
        try {
            date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            LogUtils.e(e.getMessage(), e);
        }
        return null;
    }

    /**
     * <li>如果小于 1 秒钟内，显示刚刚</li>
     * <li>如果在 1 分钟内，显示 XXX秒前</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     */
    public static String getFriendlyTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            return String.format("%tc", millis);
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        }
        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("今天%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final String[] ZODIAC = {
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"
    };

    /**
     * 获取星座
     *
     * @param month 月份
     * @param day   月份的多少天
     * @return
     */
    public static String getZodiac(final int month, final int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }

    private static final String[] CHINESE_ZODIAC =
            {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    /**
     * 获取是生肖年
     *
     * @param year
     * @return
     */
    public static String getChineseZodiac(final int year) {
        return CHINESE_ZODIAC[year % 12];
    }
}
