package com.taiping.utils.common;

import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * 计算工具类
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 17:49
 */
public class CalculateUtil {

    /**
     * 返回百分比
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:54
     * @param dividend 除数
     * @param divisor 被除数
     * @return 返回百分比
     */
    public static double castPercent(Integer dividend, Integer divisor) {
        double percentDouble = 0;
        double percentFloat =  (float)dividend / (float)divisor * 100;
        //小数点后两位
        Integer scale = 2;
        if (!ObjectUtils.isEmpty(dividend) && !ObjectUtils.isEmpty(divisor)) {
            if (0 == divisor.intValue()) {
                percentDouble = 100;
                return percentDouble;
            }
            //精确百分比到小数点后两位
            percentDouble = new BigDecimal(percentFloat).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return percentDouble;
    }


    /**
     * 返回百分比
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:54
     * @param dividend 除数
     * @param divisor 被除数
     * @return 返回百分比
     */
    public static double castPercent(double dividend, double divisor) {
        double percentDouble = 0;
        double percentFloat =  dividend / divisor * 100;
        //小数点后两位
        Integer scale = 2;
        if (!ObjectUtils.isEmpty(dividend) && !ObjectUtils.isEmpty(divisor)) {
            if (0 == divisor) {
                percentDouble = 100;
                return percentDouble;
            }
            //精确百分比到小数点后两位
            percentDouble = new BigDecimal(percentFloat).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return percentDouble;
    }
}
