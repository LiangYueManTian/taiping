package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/11/12
 * 维保计划表头枚举
 */
public enum  TableHeaderEnum {
    /**
     * 子系统
     */
    CHILDSYSTEM("childSystem","子系统"),
    /**
     * 设备
     */
    DEVICE("device","设备"),
    /**
     * 周期
     */
    PERIOD("period","周期"),
    /**
     * 部件
     */
    COMPONENT("component","部件"),
    /**
     * 维护保养项目
     */
    MAINTENANCEPLANNAME("maintenancePlanName","维护保养项目");
    /**
     * key
     */
    private String key;
    /**
     * title
     */
    private String title;

    TableHeaderEnum(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
