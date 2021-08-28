package com.taiping.constant.energy;

/**
 * 容量常量类
 * @author hedongwei@wistronits.com
 * @date 2019/10/29 17:25
 */

public class EnergyConstant {

    /**
     * 统计月份
     */
    public static final Integer STATISTICS_MONTH = 13;

    /**
     * 预测月份
     */
    public static final Integer FORECAST_MONTH = 0;


    /**
     * 动力能耗分项建议信息
     */
    public static final String ITEM_ADVICE_INFO = "${name}： ${month} 月份统计周期为：(${month}/1 - ${endMonth}/1 ), 环比 ${ringMonth}月份 ${ringValue}kwh (${ringPercent}%) ，同比去年${month}月份 ${yearOverYearValue}kwh (${yearOverYearPercent}%)";


    /**
     * 总能耗分项建议信息
     */
    public static final String ALL_ADVICE_INFO = "${name}： ${month} 月份统计电耗${value}kwh, 环比 ${ringMonth}月份 ${ringValue}kwh (${ringPercent}%) ，同比去年${month}月份 ${yearOverYearValue}kwh (${yearOverYearPercent}%)";

    /**
     * PUE建议信息
     */
    public static final String PUE_ADVICE_INFO = "统计周期(${month}/1 - ${endMonth}/1), 数据中心整体无异常耗电情况，当前pue值测算为 ${value} #### PUE值环比${ringMonth}月份 ${ringPercent}, 原因为： ${ringReason} , 所以PUE值环比${month} ${ringPercent} @@@@ PUE值同比${beforeYear}年${month}月份 ${yearOverYearValue},原因为${itReason}, ${powerReason} ";


    /**
     * 子项建议信息
     */
    public static final String CHILD_ADVICE_INFO = "${name}，${month} 月份电耗为: ${value} kwh, 环比 ${ringMonth}月份 ${ringValue}kwh (${ringPercent}%), 同比去年${month} 月份 ${yearOverYearValue}kwh (${yearOverYearPercent}%)";


    /**
     * 环比增长
     */
    public static final String RING_GROWTH = "统计周期内IT设备负载增长及动力负荷降低";

    /**
     * 环比下降
     */
    public static final String RING_DECLINE = "统计周期内IT设备负载下降及动力负荷增长" ;
}
