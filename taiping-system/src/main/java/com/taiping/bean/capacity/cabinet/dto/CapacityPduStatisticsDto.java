package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * 容量pdu统计
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 16:20
 */
@Data
public class CapacityPduStatisticsDto extends CapacityBaseElectricStatisticsDto {

    /**
     * 机柜名称
     */
    private String cabinetUniqueName;

}
