package com.taiping.enums.job;

/**
 * 任务触发器
 *
 * @author liyj
 * @date 2019/9/20
 */
public enum JobTriggerEnum {
    /**
     * 示例trigger
     */
    SIMPLE_TRIGGER("simple_trigger"),
    /**
     *更新风险超时时间
     */
    RISK_TIMEOUT_TIME("riskTimeoutTime"),
    /**
     * 维保预提醒
     */
    MAINTENANCE_REMINDER("maintenanceReminder"),
    /**
     * 运维管理活动提醒
     */
    MANAGE_REMINDER("manageReminder"),
    /**
     * 维保延期时间更新
     */
    MAINTENANCE_DELAY_TIME("maintenanceDelayTime");

    private String groupName;

    JobTriggerEnum(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
