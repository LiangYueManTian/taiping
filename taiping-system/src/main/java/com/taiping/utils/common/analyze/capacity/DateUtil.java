package com.taiping.utils.common.analyze.capacity;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;

import java.util.Calendar;

/**
 * 转换时间
 * @author hedongwei@wistronits.com
 * @date 2019/11/4 13:27
 */

public class DateUtil {

    /**
     * 获取统计的时间
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 16:59
     * @param month 月份
     * @param year 年份
     * @param collectionTime 统计时间
     * @return 统计的时间
     */
    public static Long generateCollectionTime(int month, int year, Long collectionTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        collectionTime = calendar.getTimeInMillis();
        return collectionTime;
    }

    /**
     * 获取时间的年份和月份
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 9:57
     * @param time 时间
     * @return 返回时间的年份和月份
     */
    public static CapacityAnalyzeTime getTimeYearAndMonth(long time) {
        CapacityAnalyzeTime analyzeTime = new CapacityAnalyzeTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        analyzeTime.setYear(year);
        analyzeTime.setMonth(month);
        System.out.println(year);
        System.out.println(month);
        return analyzeTime;
    }
}
