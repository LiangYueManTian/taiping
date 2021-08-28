package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/11/7
 * 执行状态枚举
 */
public enum ExecuteStatusEnum {
    /**
     * 未执行
     */
    UNENFORCED("未执行",1),
    /**
     * 已执行
     */
    EXECUTED("已执行",2),
    /**
     * 已延期
     */
    DELAY("已延期",3),
    /**
     * 已延期执行
     */
    DELAY_EXECUTED("已延期执行",4),
    /**
     * 计划已暂停
     */
    PAUSED("计划已暂停",5);
    /**
     *执行状态
     */
    private String status;
    /**
     * code
     */
    private Integer code;

    ExecuteStatusEnum(String status, Integer code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }
}
