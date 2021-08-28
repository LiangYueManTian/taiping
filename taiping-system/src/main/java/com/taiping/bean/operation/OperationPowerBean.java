package com.taiping.bean.operation;

import lombok.Data;

import java.util.List;

/**
 * 运行情况-物业沟通-返回
 *
 * @author: liyj
 * @date: 2019/12/12 18:11
 **/
@Data
public class OperationPowerBean {
    /**
     * 返回 返回对应的实体
     */
    private List data;
    /**
     * 运行情况详情数据
     */
    private List<String> detail;
    /**
     * 总览详情
     */
    private String title;
    /**
     * sheet 类型 0 高压柜 1 低压柜 2 柴发 3 变压器
     */
    private Integer sheetType;
    /**
     * 是否有异常详情 true 有 false  没有
     */
    private boolean exceptionStatus;

    /**
     * 查询月份
     */
    private Integer month;
    /**
     * 查询年份
     */
    private Integer year;
}
