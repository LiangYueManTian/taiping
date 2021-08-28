package com.taiping.enums.productivity;
/**
 * 生产力分析Excel表行数据枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
public enum ProductivityExcelRowEnum {
    /**
     * 常白班排班和倒班排班Excel表
     *
     * 排班日期
     */
    DATE("scheduleDate", 0),
    /**
     * 班次名称
     */
    TYPE_NAME("scheduleName", 1),
    /**
     * 开始时间
     */
    START_TIME("startTime", 2),
    /**
     * 结束时间
     */
    END_TIME("endTime", 3),
    /**
     * 值班人员
     */
    DUTY_OFFICER("dutyOfficer", 4),
    /**
     * 特殊情况说明
     */
    SPECIAL_DESCRIPTION("specialDescription", 9),
    /**
     * 巡检工单数据Excel表
     *
     * 巡检工单号
     */
    INSPECTION_CODE("inspectionCode", 1),
    /**
     * 创建时间
     */
    CREATE_TIME("createTime", 2),
    /**
     * 状态
     */
    STATUS("status", 3),
    /**
     * 任务名称
     */
    TASK_NAME("taskName", 4),
    /**
     * 区域位置
     */
    LOCATION("location", 5),
    /**
     * 频率
     */
    FREQUENCY("frequency", 6),
    /**
     * 巡检组
     */
    INSPECTION_GROUP("inspectionGroup", 7),
    /**
     * 执行人
     */
    EXECUTOR("executor", 8),
    /**
     * 计划开始时间
     */
    PLAN_START_TIME("planStartTime", 9),
    /**
     * 计划结束时间
     */
    PLAN_END_TIME("planEndTime", 10),
    /**
     * 允许提前时间
     */
    ALLOW_LEAD_TIME("allowLeadTime", 11),
    /**
     * 允许延迟时间
     */
    ALLOW_DELAY_TIME("allowDelayTime", 12),
    /**
     * 计划用时
     */
    PLAN_USE_TIME("planUseTime", 13),
    /**
     * 实际用时
     */
    ACTUAL_USE_TIME("actualUseTime", 14),
    /**
     * 实际开始时间
     */
    ACTUAL_START_TIME("actualStartTime", 15),
    /**
     * 实际结束时间
     */
    ACTUAL_END_TIME("actualEndTime", 16),
    /**
     *变更单数据Excel表
     *
     * 变更单类型
     */
    CHANGE_TYPE("类型", 1),
    /**
     * 单日人数
     */
    PEOPLE("单日人数", 2),
    /**
     * 设备U数
     */
    WORKLOAD("设备U数", 3),
    /**
     * 开始日期
     */
    START_DATE("开始日期", 4),
    /**
     * 结束日期
     */
    END_DATE("结束日期", 5),
    /**
     * 历史交接人
     */
    HANDOVER_PEOPLE("历史交接人", 6),
    /**
     * 变更单号\项目名称
     */
    CHANGE_NAME_CODE("变更单", 7),
    /**
     * 项目状态
     */
    PROJECT_STATUS("项目状态", 8);

    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 单元格位置
     */
    private int cellNum;

    ProductivityExcelRowEnum(String propertyName, int cellNum) {
        this.propertyName = propertyName;
        this.cellNum = cellNum;
    }


    public int getCellNum() {
        return cellNum;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
