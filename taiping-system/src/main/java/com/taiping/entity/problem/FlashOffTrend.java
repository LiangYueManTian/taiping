package com.taiping.entity.problem;

import lombok.Data;

/**
 * 停水停电趋势实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-18
 */
@Data
public class FlashOffTrend {
    /**
     * 主键
     */
    private String trendId;
    /**
     * 统计日期
     */
    private Long trendDate;
    /**
     * 闪断类型
     */
    private String offType;
    /**
     *  闪断次数
     */
    private Integer offNumber;
    /**
     * 分析月份
     */
    private Long reportDate;


    public FlashOffTrend() {
        offNumber = 0;
    }
}
