package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * 容量列头柜统计
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 16:20
 */
@Data
public class CapacityColumnStatisticsDto extends CapacityBaseElectricStatisticsDto {

    /**
     * 列头柜设计负荷
     */
    private double arrayCabinetDesignLoad;

    /**
     * 列头柜名
     */
    private String arrayCabinetName;
}
