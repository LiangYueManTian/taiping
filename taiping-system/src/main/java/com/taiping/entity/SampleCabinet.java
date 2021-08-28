package com.taiping.entity;

import lombok.Data;

/**
 * 机柜容量实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
@Data
public class SampleCabinet extends ExcelReadBean {

    private String cabinetCode;

    private String floor;

    private String cabinetRow;

    private String cabinetName;

    private String type;

    private String practicalCapacity;

    private String occupiedCapacity;

    private String spareCapacity;

    private String occupiedCapacityPercentage;

    private String practicalElectricPower;

    private String occupiedElectricPower;

    private String spareElectricPower;

    private String occupiedElectricPowerPercentage;

}
