package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运行情况-高压柜
 *
 * @author: liyj
 * @date: 2019/12/9 14:59
 **/
@Data
public class DistHighCabinet extends ExcelReadBean {
    /**
     * 主键id
     */
    private String tId;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 线路名称
     */
    private String lineName;
    /**
     * 配电柜编号
     */
    private String powerNum;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态
     */
    private Integer state;
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
     * 柜体是否有异响及异味
     */
    private Integer cabinetSmellStatus;
    /**
     * 柜面仪表指示灯状态
     */
    private Integer cabinetLampStatus;
    /**
     * A路直流屏电池电压
     */
    private Double dcPanelVoltageA;
    /**
     * A路直流屏电池电压序号
     */
    private String dcPanelVoltageAOrderNum;
    /**
     * B路直流屏电池电压
     */
    private Double dcPanelVoltageB;
    /**
     * A路直流屏电池电压序号
     */
    private String dcPanelVoltageBOrderNum;
    /**
     * A路直流屏电池内阻
     */
    private Double dcPanelInternalA;

    /**
     * B路直流屏电池内阻
     */
    private Double dcPanelInternalB;
    /**
     * 备注
     */
    private String remark;
    /**
     * 最小电流
     */
    private Double minElectric;
    /**
     * 最大电流
     */
    private Double maxElectric;

    /**
     * 最小电压
     */
    private Double minVoltage;
    /**
     * 最大电压
     */
    private Double maxVoltage;

    /**
     * 最低直流电压
     */
    private Double minDcVoltage;
    /**
     * 最高直流电压
     */
    private Double maxDcVoltage;
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
