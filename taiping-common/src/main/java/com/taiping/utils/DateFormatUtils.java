package com.taiping.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *   日期处理工具类
 * </p>
 *
 * @author chaofang@fiberhome.com
 * @since 2019-11-1
 */
@Slf4j
public class DateFormatUtils {
    /**
     * 日期字符串转时间戳
     * @param formatString 日期格式
     * @param dateString 日期字符串
     * @return 时间戳
     * @throws ParseException 解析异常
     */
    public static long dateStringToLong(String formatString, String dateString) throws ParseException {
        SimpleDateFormat format =  new SimpleDateFormat(formatString);
        Date date = format.parse(dateString);
        return date.getTime();
    }

    /**
     * 日期时间戳转字符串
     * @param formatString 日期格式
     * @param date 时间戳
     * @return 日期字符串
     */
    public static String dateLongToString(String formatString, long date) {
        SimpleDateFormat format =  new SimpleDateFormat(formatString);
        return format.format(date);
    }

    public static void main(String[] args) {
        System.out.println(dateLongToString("yyyy年MM月", System.currentTimeMillis()));
    }

    /**
     * 日期字符串转时间戳
     * @param formatString 日期格式
     * @param dateString 日期字符串
     * @return 时间戳
     */
    public static long dateStringToLongShow(String formatString, String dateString) {
        try {
            SimpleDateFormat format =  new SimpleDateFormat(formatString);
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            log.error("程序日期异常", e);
        }
        return 0L;
    }

    /**
     * 获取指定时间所在年份最后一周周一开始时间
     *
     * @param time 指定时间
     * @return 最后一周周一开始时间
     */
    public static Long getLastedWeekOfYear(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(calendar.get(Calendar.YEAR),Calendar.MONDAY,1,0,0,0);
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.getMaximum(Calendar.WEEK_OF_YEAR));
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间所在年份第一周周一开始时间
     *
     * @param time 指定时间
     * @return 第一周周一开始时间
     */
    public static Long getFirstWeekOfYear(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(calendar.get(Calendar.YEAR),Calendar.MONDAY,1,0,0,0);
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.getMinimum(Calendar.WEEK_OF_YEAR));
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间所属周周一开始时间
     *
     * @param time 指定时间
     * @return 指定时间所属周周一开始时间
     */
    public static Long getThisWeekStartTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间下一周周一开始时间
     *
     * @param time 指定时间
     * @return 下一周周一开始时间
     */
    public static Long getNextWeekStartTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE,-1);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间上一周周一开始时间
     *
     * @param time 指定时间
     * @return 上一周周一开始时间
     */
    public static Long getLastWeekStartTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE,-1);
        calendar.add(Calendar.WEEK_OF_YEAR,-1);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定时间下月同周周一开始时间
     *
     * @param time 指定时间
     * @return 下月同周周一开始时间
     */
    public static Long getSameWeekOfNextMonthTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }


    /**
     *
     * 获取指定时间下月最后一周一开始时间
     *
     * @param time 指定时间
     * @return 下月同周周一开始时间
     */
    public static Long getLastWeekOfNextMonthTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定时间下季度同周周一开始时间
     *
     * @param time 指定时间
     * @return 下季度同周周一开始时间
     */
    public static Long getSameWeekOfNextQuarterTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH,3);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定时间下半年同周周一开始时间
     *
     * @param time 指定时间
     * @return 下半年同周周一开始时间
     */
    public static Long getSameWeekOfNextHalfYearTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH,6);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定时间下年同周周一开始时间
     *
     * @param time 指定时间
     * @return 下年同周周一开始时间
     */
    public static Long getSameWeekOfNextYearTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.YEAR,1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }


    /***
     * 获取指定年份最后一周开始时间
     *
     * @param year 指定年份
     * @return 指定年份第一周开始时间
     */
    public static Long getLastWeekTimeOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.getMaximum(Calendar.WEEK_OF_YEAR));
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /***
     * 获取指定年份第一周开始时间
     *
     * @param year 指定年份
     * @return 指定年份最后一周开始时间
     */
    public static Long getFirstWeekTimeOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.getActualMinimum(Calendar.WEEK_OF_YEAR));
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定月下月开始时间
     *
     * @param thisMonthTime 指定月时间
     * @return 下月开始时间
     */
    public static Long getNextMonthTime(Long thisMonthTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(thisMonthTime);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, 1, 0, 0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定月下季度开始时间
     *
     * @param thisMonthTime 指定月时间
     * @return 季度开始时间
     */
    public static Long getNextQuarterTime(Long thisMonthTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(thisMonthTime);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 3, 1, 0, 0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定月下半年开始时间
     *
     * @param thisMonthTime 指定月时间
     * @return 下半年开始时间
     */
    public static Long getNextHalfYearTime(Long thisMonthTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(thisMonthTime);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 6, 1, 0, 0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取当前月开始时间
     *
     * @return 下月开始时间
     */
    public static Long getThisMonthTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), 1, 0, 0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     *
     * 获取指定时间所在月第一周周一开始时间
     *
     * @param time 指定时间
     * @return 指定时间所在月第一周周一开始时间
     */
    public static Long getFristWeekOfThisMonthTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.WEEK_OF_MONTH,1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取去年同月到当前时间戳集合
     * @param time 时间戳
     * @return 时间戳集合
     */
    public static List<Long> getMonthOfYear(Long time) {
        List<Long> timeList = new ArrayList<>();
        Long latest  = getTimeLastYear(time);
        while (latest <= time) {
            latest = getNextMonthStartForTime(latest);
            timeList.add(latest);
        }
        return timeList;
    }
    /**
     * 获取时间戳对应时间月份开始时间
     * @param time 时间戳
     * @return 月份开始时间
     */
    public static Long getMonthStartForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应时间月份结束时间
     * @param time 时间戳
     * @return 月份开始时间
     */
    public static Long getMonthEndForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应时间当天开始时间
     * @param time 时间戳
     * @return 当天开始时间
     */
    public static Long getDayStartForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应时间当天开始时间
     * @param time 时间戳
     * @return 当天开始时间
     */
    public static Long getDayEndForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳下一天时间戳
     * @param time 时间戳
     * @return 下一天时间戳
     */
    public static Long getTimeLongNextDay(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取前一年时间戳
     * @param time 时间戳
     * @return 前一年时间戳
     */
    public static Long getTimeLastYear(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    /**
     * 获取前一月时间戳
     * @param time 时间戳
     * @return 前一月时间戳
     */
    public static Long getTimeLastMonth(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应下个月开始时间
     * @param time 时间戳
     * @return 月份开始时间
     */
    public static Long getNextMonthStartForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应下个月结束时间
     * @param time 时间戳
     * @return 月份开始时间
     */
    public static Long getNextMonthEndForTime(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月天数
     * @param time 时间
     * @return int 当月天数
     */
    public static int getMonthDayNumber(Long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
