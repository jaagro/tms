package com.jaagro.tms.biz.utils;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 时间工具类
 * @author: @Gao.
 * @create: 2019-02-28 13:29
 **/
@Slf4j
public class DateUtil {

    private static Calendar fromCal = Calendar.getInstance();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 日期添加指定天数
     *
     * @return
     * @author: @Gao.
     */
    public static String accumulateDateByDay(Date date, int day) {
        String strDate = threadLocal.get().format(date);
        Date formatDate = stringToDate(strDate);
        fromCal.setTime(formatDate);
        fromCal.add(Calendar.DATE, day - 7);
        return threadLocal.get().format(fromCal.getTime());
    }

    /**
     * 将时间字符串转化为Date
     *
     * @param stringDate
     * @return
     * @Author @Gao.
     */
    public static Date stringToDate(String stringDate) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            log.error("I stringToDate-{}", e);
        }
        return date;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * gavin
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static Date today() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 将date类型转化为String
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        return threadLocal.get().format(date);
    }

    /**
     * 日期添加指定天数
     *
     * @return
     * @author: @gavin
     */
    public static String accumulateDayAge(Date date, int day) {
        String strDate = threadLocal.get().format(date);
        Date formatDate = stringToDate(strDate);
        fromCal.setTime(formatDate);
        fromCal.add(Calendar.DATE, day);
        return threadLocal.get().format(fromCal.getTime());
    }

    public static void main(String[] args) {
        System.out.println(accumulateDateByDay(new Date(), 0));
        System.out.println(accumulateDayAge(new Date(), 0));
    }
}
