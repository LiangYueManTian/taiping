package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运行情况--柴发
 *
 * @author: liyj
 * @date: 2019/12/9 14:41
 **/
@Data
public class ChaiFa extends ExcelReadBean {

    /**
     * 主键id
     */
    private String tId;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 柴发名称
     */
    private String chaiFaName;
    /**
     * 冷却液水位状态
     */
    private Integer coolantLevelStatus;
    /**
     * 水温
     */
    private Double waterTemperature;
    /**
     * 机油油位状态
     */
    private Integer oilLevelStatus;
    /**
     * 供油管路及机组是否渗漏
     */
    private Integer oilSupplyLineStatus;
    /**
     * A1蓄电池电压是否正常
     */
    private Double batteryVoltageA1;
    /**
     * A2蓄电池电压是否正常
     */
    private Double batteryVoltageA2;
    /**
     * B1蓄电池电压是否正常
     */
    private Double batteryVoltageB1;
    /**
     * B2蓄电池电压是否正常
     */
    private Double batteryVoltageB2;
    /**
     * A1 电池电阻
     */
    private Double resistanceA1;
    /**
     * A2 电池电阻
     */
    private Double resistanceA2;
    /**
     * B1 电池电阻
     */
    private Double resistanceB1;
    /**
     * b2 电池电阻
     */
    private Double resistanceB2;
    /**
     * 日用油箱油位
     */
    private Double oilLevelDaily;
    /**
     * 室外1油罐油位
     */
    private Double oilLevelOutdoorOne;
    /**
     * 室外2油罐油位
     */
    private Double oilLevelOutdoorTwo;
    /**
     * 当前负载剩余柴油可用时长
     */
    private Double currLoadResidueOilTime;
    /**
     * 剩余柴油满载可用时长
     */
    private Double residueFullLoadOilTime;

    /**
     * 柴发控制柜状态
     */
    private Integer chaiFaControllerStatus;
    /**
     * 柴发测试时电流状态
     */
    private Integer chaiFaTestEleStatus;
    /**
     * 柴发测试时电压状态
     */
    private Integer chaiFaTestVolStatus;
    /**
     * 供油油泵控制柜状态
     */
    private Integer fuelFeediControllerStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最低水温
     */
    private Double minWaterTemperature;

    /**
     * 最高水温
     */
    private Double maxWaterTemperature;
    /**
     * 最小电压
     */
    private Double minBatteryVoltage;
    /**
     * 最大电压
     */
    private Double maxBatteryVoltage;
    /**
     * 最低电阻
     */
    private Double minResistance;
    /**
     * 最大电阻
     */
    private Double maxResistance;
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

    /**
     * 最高日用油箱油位
     */
    private Double maxOilLevelDaily;
    /**
     * 最低日用油箱油位
     */
    private Double minOilLevelDaily;
    /**
     * 最低室外油罐
     */
    private Double minOilLevelOutdoor;
    /**
     * 最高室外油罐
     */
    private Double maxOilLevelOutdoor;
    /**
     * 冷却液范围
     */
    private String coolantRange;
    /**
     * 柴发测试时电流范围
     */
    private String testEleRange;
    /**
     * 柴发测试时电压范围
     */
    private String testVolRange;

    /**
     * 当前负载剩余柴油可用时长（h）
     */
    private Double minLoadResidueTime;
    /**
     * 当前负载剩余柴油可用时长
     */
    private Double maxLoadResidueTime;




}
