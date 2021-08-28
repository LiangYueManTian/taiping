package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * 机柜统计dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 16:20
 */
@Data
public class CapacityCabinetStatisticsDto extends CapacityBaseSpaceStatisticsDto {

    /**
     * 机柜唯一标识名
     */
    private String cabinetUniqueName;
}
