package com.taiping.entity.problem;

import lombok.Data;

/**
 * 故障分类趋势实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-18
 */
@Data
public class TroubleTrend {
    /**
     * 主键
     */
    private String trendId;
    /**
     * 统计日期
     */
    private Long trendDate;
    /**
     * 故障一级分类
     */
    private String topType;
    /**
     * 极低次数
     */
    private Integer veryLowNumber;
    /**
     * 低次数
     */
    private Integer lowNumber;
    /**
     * 中次数
     */
    private Integer moderateNumber;
    /**
     * 高次数
     */
    private Integer highNumber;
    /**
     * 极高次数
     */
    private Integer veryHighNumber;
    /**
     * 分析月份
     */
    private Long reportDate;

    public TroubleTrend(){
        veryLowNumber = 0;
        lowNumber = 0;
        moderateNumber = 0;
        highNumber = 0;
        veryHighNumber = 0;
    }
}
