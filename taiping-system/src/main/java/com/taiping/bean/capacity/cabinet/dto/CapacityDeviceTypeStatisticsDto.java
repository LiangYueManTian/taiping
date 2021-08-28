package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * 容量功能区统计dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 16:20
 */
@Data
public class CapacityDeviceTypeStatisticsDto extends CapacityBaseSpaceStatisticsDto {

    /**
     * 楼层唯一标识
     */
    private String floorUniqueName;

    /**
     * 功能区
     */
    private String deviceType;
}
