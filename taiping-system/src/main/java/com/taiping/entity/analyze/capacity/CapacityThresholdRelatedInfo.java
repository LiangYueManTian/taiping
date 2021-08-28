package com.taiping.entity.analyze.capacity;

import lombok.Data;

/**
 * 容量阈值关联信息表
 * @author hedongwei@wistronits.com
 * @date 2019/11/1 9:03
 */
@Data
public class CapacityThresholdRelatedInfo {

    /**
     * 阈值关联id
     */
    private String thresholdRelatedId;

    /**
     * 阈值code
     */
    private String thresholdCode;

    /**
     * 类型  1 推荐数据  2 显示关联数据
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 值
     */
    private double value;

    /**
     * 描述
     */
    private String remark;

    /**
     * 0 未删除 1 已删除
     */
    private Integer isDeleted;
}
