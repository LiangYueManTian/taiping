package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运行情况-物业沟通-低电压
 *
 * @author: liyj
 * @date: 2019/12/9 15:12
 **/
@Data
public class DistLowCabinet extends ExcelReadBean {
    /**
     * 主键
     */
    private String tId;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 位置
     */
    private String position;
    /**
     * 配电柜名称
     */
    private String powerName;
    /**
     * 环境温度
     */
    private Double envTemperature;
    /**
     * 柜体是否有异响及异味
     */
    private Integer cabinetSmellStatus;
    /**
     * 开关状态是否正常
     */
    private Integer switchingState;
    /**
     * 柜面仪表指示灯是否正常
     */
    private Integer cabinetLampStatus;
    /**
     * 接线端子温度是否正常
     */
    private Integer lineTemperatureStatus;
    /**
     * 接地是否正常
     */
    private Integer earthingStatus;
    /**
     * 配电柜门是否关闭
     */
    private Integer cabinetDoorStatus;
    /**
     * 环境是否整洁
     */
    private Integer envClearStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 最低环境温度
     */
    private Double minEnvTemperature;
    /**
     * 最大环境温度
     */
    private Double maxEnvTemperature;
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
