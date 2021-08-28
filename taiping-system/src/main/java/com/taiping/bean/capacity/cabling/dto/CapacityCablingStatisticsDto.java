package com.taiping.bean.capacity.cabling.dto;

import lombok.Data;

/**
 * 容量综合布线统计类
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 9:17
 */
@Data
public class CapacityCablingStatisticsDto {

    /**
     * 总端口数
     */
    private Integer allPortNumber;

    /**
     * 已用端口数
     */
    private Integer usedPortNumber;

    /**
     * 端口占用率
     */
    private double portPercent;

    /**
     * 综合布线类型
     */
    private String genericCablingType;

    /**
     * 线缆类型
     */
    private String cableType;

    /**
     * 配线架code
     */
    private String connectRackCode;

    /**
     * 状态的数量
     */
    private Integer statusNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;
}
