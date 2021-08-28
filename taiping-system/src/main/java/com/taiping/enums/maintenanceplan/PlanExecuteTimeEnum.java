package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/11/7
 * 计划执行时间枚举
 */
public enum PlanExecuteTimeEnum {
    /**
     * 每月第一周
     */
    FIRST_WEEK_OF_MONTH("每月第一周",1),
    /**
     *每月第二周
     */
    SECOND_WEEK_OF_MONTH("每月第二周",2),
    /**
     *每月第三周
     */
    THIRD_WEEK_OF_MONTH("每月第三周",3),
    /**
     *每月第四周
     */
    FOURTH_WEEK_OF_MONTH("每月第四周",4),
    /**
     * 每月最后一周
     */
    LATEST_WEEK_OF_MOMTH("每月最后一周",-1);
    /**
     * 执行时间
     */
    private String executeTime;
    /**
     * code
     */
    private Integer code;

    PlanExecuteTimeEnum(String executeTime, Integer code) {
        this.executeTime = executeTime;
        this.code = code;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public Integer getCode() {
        return code;
    }
}
