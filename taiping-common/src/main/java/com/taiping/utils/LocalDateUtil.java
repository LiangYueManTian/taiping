package com.taiping.utils;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * LocalDateTimeUtils
 * <p>
 * 关于时间处理的公共方法
 * a UTC 时间格式化转换
 * <p>
 * </P>
 *
 * @author liyj
 * @date 2019/10/30
 */
public class LocalDateUtil {

    /**
     * 格式 yyyy-MM-dd
     */
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 格式 hh:mm:ss
     */
    public final static String HH_MM_SS = "HH:mm:ss";
    /**
     * 格式化 yyyy-MM-dd hh:mm:ss
     */
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化 yyyy
     */
    public static final String YEAR = "yyyy";
    public static final String PARAM_MONTH = "月";
    public static final String PARAM_WEEK = "周";
    /**
     * 格式化 MM
     */
    public static final String MONTH = "MM";
    public static final String DAY = "dd";
    public static final String HOUR = "HH";
    public static final String MINUTE = "mm";
    public static final String SECOND = "ss";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_M_D = "yyyy-M-d";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String TIMEZONE_UTC = "UTC";
    public static final String DAY_START_TIME = "00:00:00";
    public static final String DAY_END_TIME = "23:59:59";
    public static final String LINE_FEED_REPLACE = "\n";


    /**
     * 获取一天的开始
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTimeStamp() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 转化时间戳为Instant 时间戳
     *
     * @param timeStamp
     * @return
     */
    public static Instant parseCurrentTimeStamp(long timeStamp) {
        return Instant.ofEpochMilli(timeStamp);
    }

    /**
     * 获取当前时间 格式 YYYY-MM-dd
     *
     * @return
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 格式化时间
     *
     * @param localDate
     * @param dateFormat
     * @return
     */
    public static String formatDate(LocalDate localDate, String dateFormat) {
        return localDate.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * String 转 LocalDate
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static LocalDate parseDate(String date, String dateFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * 获取当前时间 格式 HH:mm:ss
     *
     * @return
     */
    public static LocalTime getCurrentDateTime() {
        return LocalTime.now();
    }

    /**
     * 把当前时间格式化成指定格式
     *
     * @param dateTimeFormat
     * @return
     */
    public static String formatCurrentDateTime(String dateTimeFormat) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
        return currentDateTime;
    }

    /**
     * 格式化当前时间
     *
     * @param dateTime
     * @param dateTimeFormat
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String dateTimeFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }


    /**
     * Stirng time 转化成LocalDateTime
     *
     * @param localDateTime
     * @param dateTimeFormat
     * @return
     */
    public static LocalDateTime parseDateTime(String localDateTime, String dateTimeFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }


    /**
     * 获取当前服务器时区
     *
     * @return
     */
    public static ZonedDateTime getCurrentZoneDateTime() {
        return ZonedDateTime.now();
    }

    /**
     * 根据时区获取时间
     *
     * @param zoneId
     * @return
     */
    public static ZonedDateTime getZoneDateTime(String zoneId) {
        return ZonedDateTime.now(ZoneId.of(zoneId));
    }


    /**
     * 比较两个时间的前后
     * dateTime2 是否大于 dateTime1
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if ((dateTime1 == null) && (dateTime2 == null)) {
            return false;
        }
        if (dateTime1 == null) {
            return true;
        }
        if (dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if ((dateTime1 == null) && (dateTime2 == null)) {
            return false;
        }
        if (dateTime1 == null) {
            return false;
        }
        if (dateTime2 == null) {
            return true;
        }
        return dateTime1.isAfter(dateTime2);
    }

    public static boolean isEqual(String dateTime1, String dateTime2) {
        if ((dateTime1 == null) && (dateTime2 == null)) {
            return true;
        }
        if ((dateTime1 == null) || (dateTime2 == null)) {
            return false;
        }
        return dateTime1.equals(dateTime2);
    }

    /**
     * 时区时间转化
     *
     * @param dateTime
     * @param fromZoneId
     * @param toZoneId
     * @return
     */
    public static LocalDateTime convertTimezone(LocalDateTime dateTime, String fromZoneId, String toZoneId) {
        return ZonedDateTime.of(dateTime, ZoneId.of(fromZoneId)).withZoneSameInstant(ZoneId.of(toZoneId)).toLocalDateTime();
    }

    /**
     * 获取UTC 时间
     *
     * @return
     */
    public static ZonedDateTime getUTCtime() {
        return ZonedDateTime.now(Clock.systemUTC());
    }

    /**
     * 获取当前UTC LocalDate
     *
     * @return
     */
    public static LocalDate getCurrentUTCDate() {
        return ZonedDateTime.now(Clock.systemUTC()).toLocalDate();
    }

    /**
     * 获取当前UTC LocalDate
     *
     * @return
     */
    public static LocalDateTime getCurrentUTCDateTime() {
        return ZonedDateTime.now(Clock.systemUTC()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    public Date localDateToDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant1 = zonedDateTime.toInstant();
        Date from = Date.from(instant1);
        return from;
    }

    public LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zdt.toLocalDate();
        return localDate;
    }

    /**
     * 格式化位数为整点 00
     *
     * @param dateTime
     * @param unit
     * @return
     */
    public LocalDateTime truncate(LocalDateTime dateTime, ChronoUnit unit) {
        if (dateTime == null) {
            return null;
        }
        if (ChronoUnit.DAYS.equals(unit)) {
            return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0);
        }
        if (ChronoUnit.HOURS.equals(unit)) {
            return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime
                    .getHour(), 0);
        }
        if (ChronoUnit.MINUTES.equals(unit)) {
            return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime
                    .getHour(), dateTime.getMinute());
        }
        if (ChronoUnit.SECONDS.equals(unit)) {
            return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime
                    .getHour(), dateTime.getMinute(), dateTime.getSecond());
        }
        return dateTime;
    }

    /**
     * 获取时间的开始第一天
     *
     * @param date
     * @return
     */
    public LocalDate getStartDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取时间的月份最后一天
     *
     * @param date
     * @return
     */
    public LocalDate getEndDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        LocalDate localDate = LocalDate.from(dateTime);
        return localDate.atStartOfDay();
    }

    public LocalDateTime getEndOfDay(LocalDateTime date) {
        LocalDate localDate = LocalDate.from(date);
        return localDate.atStartOfDay().plusDays(1L).minusNanos(1L);
    }

    public LocalDate getStartDayOfWeek(LocalDate date, boolean isMonday) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (isMonday) {
            return date.minusDays(dayOfWeek.getValue() - 1);
        }
        if (DayOfWeek.SUNDAY.equals(dayOfWeek)) {
            return date;
        }
        return date.minusDays(dayOfWeek.getValue());
    }

    public boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }


    /**
     * 通过当前时间获取周数
     *
     * @param localDateTime
     * @return
     */
    public static int getWeekByLocalDateTime(LocalDateTime localDateTime) {
        // 每周从周一开始 一周七天
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 7);
        return localDateTime.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * 通过年份获取一年的周数
     *
     * @param year
     * @return Map<Long, String>
     */
    public static Map<Long, String> getAllWeekByYear(int year) {
        Map<Long, String> resultList = new HashMap<>();
        // 根据年份获取当前年的所有周
        LocalDateTime time = LocalDateTime.of(
                year, 1, 1, 0, 0, 0
        );
        // 下一年 开始时间
        LocalDateTime nextYear = time.plusYears(1);
        // 获取当前年的第一个周一时间
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 7);
        // 第一周时间
        LocalDateTime firstWeekTime = time.with(weekFields.dayOfWeek(), 1);
        if (firstWeekTime.getYear() < time.getYear()) {
            firstWeekTime = firstWeekTime.plusDays(7);
        }


        int weekCount = 0;
        int monthCount = 1;
        //逻辑开始加7 直到当前时间超过下一年开始时间 退出循环
        while (isBefore(firstWeekTime, nextYear)) {
            if (firstWeekTime.getMonthValue() > monthCount) {
                weekCount = 0;
                monthCount++;
            }
            weekCount++;
            StringBuilder sb = new StringBuilder();
            sb.append(monthCount).append(PARAM_MONTH).append(LINE_FEED_REPLACE).append(weekCount).append(PARAM_WEEK);
            resultList.put(firstWeekTime.toInstant(ZoneOffset.of("+8")).toEpochMilli(), sb.toString());
//            System.out.println("当前时间" + firstWeekTime+ "||" + monthCount + "月" + "第" + weekCount + "周");
            firstWeekTime = firstWeekTime.plusDays(7);
        }
        return resultList;
    }


    public static void main(String[] args) {


        getAllWeekByYear(2018);
        getAllWeekByYear(2019);
        getAllWeekByYear(2020);
//        LocalDateTime time = LocalDateTime.of(
//                2020, 1, 1, 0, 0, 0
//        );
//        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 7);
//        time = time.with(weekFields.dayOfWeek(), 1);
//        System.out.println(time);
    }

}
