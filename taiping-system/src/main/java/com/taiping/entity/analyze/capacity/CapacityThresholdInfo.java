package com.taiping.entity.analyze.capacity;

import com.taiping.entity.analyze.AnalyzeThresholdBaseInfo;
import lombok.Data;

/**
 * 容量阈值信息表
 * @author hedongwei@wistronits.com
 * @date 2019/11/1 8:53
 */
@Data
public class CapacityThresholdInfo extends AnalyzeThresholdBaseInfo {

    /**
     * 同比比率
     */
    private double yearOverYearPercent;

    /**
     * 环比比率
     */
    private double ringGrowth;

    /**
     * 已用线缆数量  (综合布线特有字段)
     */
    private Integer usedCable;

    /**
     * 线缆总数量 (综合布线特有字段)
     */
    private Integer allCable;

    /**
     *  综合布线类型 (综合布线特有字段)
     */
    private String genericCablingType;

    /**
     * 建议
     */
    private String advice;

    /**
     * 运维管理活动类型
     */
    private String activityType;

    /**
     * 处理说明
     */
    private String solveInstruction;

    /**
     * 已用值
     */
    private double usedNumber;

    /**
     * 全部值
     */
    private double allNumber;

    /**
     * 建议类型  1 裁剪 2 采购
     */
    private String adviceType;

    /**
     * 建议信息
     */
    private String adviceInfo;

    /**
     * 运维管理活动原因
     */
    private String cause;

    /**
     * 数据采集时间
     */
    private Long dataCollectionTime;

    /**
     * 是否删除  0 未删除 1 删除
     */
    private Integer isDeleted;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Long updateTime;

}
