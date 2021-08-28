package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运行情况-物业沟通-变压器
 *
 * @author: liyj
 * @date: 2019/12/9 15:19
 **/
@Data
public class Transformer extends ExcelReadBean {
    /**
     * 主键id
     */
    private String tId;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 变压器名称
     */
    private String transformerName;
    /**
     * L1 电压
     */
    private Double voltageL1;
    /**
     * L2 电压
     */
    private Double voltageL2;
    /**
     * L3 电压
     */
    private Double voltageL3;
    /**
     * L1 电流
     */
    private Double electricL1;
    /**
     * L2 电流
     */
    private Double electricL2;
    /**
     * L3 电流
     */
    private Double electricL3;
    /**
     * 有功功率
     */
    private Double activePower;

    /**
     * 负荷率
     */
    private Double loadRate;


    /**
     * 铁芯温度A
     */
    private Double coreTemperatureA;
    /**
     * 风机状态
     */
    private Integer fanStatus;
    /**
     * 配电柜是否有异味
     */
    private Integer cabinetSmellStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 铁芯温度B
     */
    private Double coreTemperatureB;
    /**
     * 铁芯温度C
     */
    private Double coreTemperatureC;
    /**
     * 铁芯温度D
     */
    private Double coreTemperatureD;
    /**
     * 最低铁芯温度1
     */
    private Double minCoreTemperatureOne;
    /**
     * 最高铁芯温度1
     */
    private Double maxCoreTemperatureOne;
    /**
     * 最低铁芯温度2
     */
    private Double minCoreTemperatureTwo;
    /**
     * 最高铁芯温度2
     */
    private Double maxCoreTemperatureTwo;
    /**
     * 最低电压
     */
    private Double minVoltage;
    /**
     * 最高电压
     */
    private Double MaxVoltage;
    /**
     * 最低电流
     */
    private Double minElectric;
    /**
     * 最高电流
     */
    private Double maxElectric;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 月份
     */
    private Integer month;


}
