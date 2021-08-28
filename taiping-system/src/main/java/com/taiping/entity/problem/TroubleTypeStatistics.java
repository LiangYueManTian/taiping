package com.taiping.entity.problem;

import lombok.Data;

/**
 * 故障分类统计实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-18
 */
@Data
public class TroubleTypeStatistics {
    /**
     * 主键
     */
    private String statisticsId;
    /**
     * 统计日期
     */
    private Long statisticsDate;
    /**
     * 故障一级分类
     */
    private String topType;
    /**
     * 故障二级分类
     */
    private String secondaryType;
    /**
     * 次数
     */
    private Integer totalNumber;
    /**
     * 环比
     */
    private String monthPercentage;
    /**
     * 同比
     */
    private String yearPercentage;
    /**
     * 极低次数
     */
    private Integer veryLowNumber;
    /**
     * 极低环比
     */
    private String veryLowMonth;
    /**
     * 极低同比
     */
    private String veryLowYear;
    /**
     * 低次数
     */
    private Integer lowNumber;
    /**
     * 低环比
     */
    private String lowMonth;
    /**
     * 低同比
     */
    private String lowYear;
    /**
     * 中次数
     */
    private Integer moderateNumber;
    /**
     * 中环比
     */
    private String moderateMonth;
    /**
     * 中同比
     */
    private String moderateYear;
    /**
     * 高次数
     */
    private Integer highNumber;
    /**
     * 高环比
     */
    private String highMonth;
    /**
     * 高同比
     */
    private String highYear;
    /**
     * 极高次数
     */
    private Integer veryHighNumber;
    /**
     * 极高环比
     */
    private String veryHighMonth;
    /**
     * 极高同比
     */
    private String veryHighYear;
    /**
     * 年
     */
    private String valueYear;
    /**
     * 月
     */
    private String valueMonth;
    /**
     * 关联运维管理活动ID
     */
    private String manageId;
    /**
     * 产生对象
     */
    private String sourceName;
    /**
     * 产生对象Code
     */
    private String sourceCode;
    /**
     * 产生原因
     * */
    private String cause;
    /**
     * 处理说明
     */
    private String solveInstruction;
    /**
     *运维管理活动类型
     */
    private String activityType;
    /**
     * 报告类型
     */
    private String reportType;

    public TroubleTypeStatistics(){
        totalNumber = 0;
        veryLowNumber = 0;
        lowNumber = 0;
        moderateNumber = 0;
        highNumber = 0;
        veryHighNumber = 0;
    }
}
