package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 常白班排班和倒班排班实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class Schedule {
    /**
     * id
     */
    private String scheduleId;
    /**
     * 排班日期
     */
    private Long scheduleDate;
    /**
     * 班次类别
     */
    private String scheduleType;
    /**
     * 班次名称
     */
    private String scheduleName;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 值班人员
     */
    private String dutyOfficer;
    /**
     * 特殊情况说明
     */
    private String specialDescription;
}
