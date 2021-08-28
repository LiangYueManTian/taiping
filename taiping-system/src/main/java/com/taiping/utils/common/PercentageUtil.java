package com.taiping.utils.common;

import java.text.NumberFormat;
/**
 * 计算同比、环比
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-21
 */
public class PercentageUtil {
    /**
     * 100%
     */
    public static final String HUNDRED_PERCENT = "100%";
    /**
     * 0%
     */
    public static final String ZERO_PERCENT = "0%";
    /**
     * 计算同比、环比
     * @param last 上期，同期
     * @param current 本期
     * @return 同比、环比
     */
    public static String calculatePercentage(Double last, Double current) {
        if (last == null || last == 0) {
            if (current == null || current == 0) {
                return ZERO_PERCENT;
            }
            return HUNDRED_PERCENT;
        }
        Double percentage = (current - last) /  last;
        return changeDoubleToString(percentage);
    }

    /**
     * 计算同比、环比
     * @param last 上期，同期
     * @param current 本期
     * @return 同比、环比
     */
    public static String calculatePercentage(Integer last, Integer current) {
        if (last == null || last == 0) {
            if (current == null || current == 0) {
                return ZERO_PERCENT;
            }
            return HUNDRED_PERCENT;
        }
        Double percentage = (double)(current - last) /  last;
        return changeDoubleToString(percentage);
    }

    /**
     * 小数转百分数字符串
     * @param percentage 小数
     * @return 百分数 String
     */
    public static String changeDoubleToString(Double percentage) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(2);
        return percentInstance.format(percentage);
    }
}
