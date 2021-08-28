package com.taiping.enums.job;


import org.apache.commons.lang.StringUtils;

/**
 * 任务组枚举
 */
public enum JobGroupEnum {
    /**
     *
     */
    SIMPLE("simpleGroup"),
    /**
     * 更新风险超时时间
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

    JobGroupEnum(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 根据参数获取当前枚举
     *
     * @param group
     * @return
     */
    public static JobGroupEnum getJobGroupEnumByStr(String group) {
        for (JobGroupEnum value : JobGroupEnum.values()) {
            if (StringUtils.equalsIgnoreCase(value.groupName, group)) {
                return value;
            }
        }
        return null;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
