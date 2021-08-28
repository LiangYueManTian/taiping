package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 个人KPI实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-08
 */
@Data
public class Performance {
    /**
     * 主键id
     */
    private String performanceId;
    /**
     * 姓名
     */
    private String personName;
    /**
     * 月份
     */
    private Long workDate;
    /**
     * 上班天数（常白班）
     */
    private Integer dayNumber;
    /**
     * 上班天数（倒班）
     */
    private Integer dayNightNumber;
    /**
     * 巡检次数
     */
    private Integer inspectionNumber;
    /**
     * 上架工作量（U）
     */
    private Double workload;
    /**
     * 上架效率（U/小时）
     */
    private Double workEfficiency;


    public int getDayNightNumberInt() {
        if (dayNightNumber == null) {
            dayNightNumber = 0;
        }
        return dayNightNumber;
    }
    public int getDayNumberInt() {
        if (dayNumber == null) {
            dayNumber = 0;
        }
        return dayNumber;
    }
    public int getInspectionNumberInt() {
        if (inspectionNumber == null) {
            inspectionNumber = 0;
        }
        return inspectionNumber;
    }
    public double getWorkloadDouble() {
        if (workload == null) {
            workload = 0.0;
        }
        return workload;
    }
}
