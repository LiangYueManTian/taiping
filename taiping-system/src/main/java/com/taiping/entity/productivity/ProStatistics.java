package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 统计实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-08
 */
@Data
public class ProStatistics {
    /**
     * 次数
     */
    private Integer workDays;
    /**
     * 上架U数
     */
    private Double workload;
    /**
     * 每月上架U数
     */
    private int totalWorkload;
    /**
     * 类型
     */
    private String scheduleType;
    /**
     * 值班人
     */
    private String dutyOfficer;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;
}
