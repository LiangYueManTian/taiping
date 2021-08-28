package com.taiping.bean.energy.dto;


import lombok.Data;

/**
 * 大屏实体
 *
 * @author liyj
 * @date 2019/11/26
 */
@Data
public class BigScreenDto {

    /**
     * 年份
     */
    private Integer year;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 已使用空间容量
     */
    private Double useSpace;
    /**
     * 未使用空间容量
     */
    private Double unUseSpace;
    /**
     * pue 使用值
     */
    private Double pueValue;
    /**
     * 机柜使用电力容量
     */
    private Double usePower;
    /**
     * 机柜设计电力容量
     */
    private Double allPower;
}
