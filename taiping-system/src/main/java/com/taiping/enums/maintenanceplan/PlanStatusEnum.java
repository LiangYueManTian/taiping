package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/11/7
 * 计划状态枚举
 */
public enum PlanStatusEnum {
    /**
     * 正常
     */
    NORMAL("正常",1),
    /**
     * 暂停
     */
    SUSPEND("暂停",2),
    /**
     * 延期
     */
    DELAY("延期",3);
    /**
     * 状态
     */
    private String status;
    /**
     * code
     */
    private Integer code;

    PlanStatusEnum(String status, Integer code) {
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
