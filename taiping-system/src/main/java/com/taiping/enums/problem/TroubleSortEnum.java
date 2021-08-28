package com.taiping.enums.problem;

/**
 * 故障单一级分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum TroubleSortEnum {
    /**
     * 机柜
     */
    CABINET("机柜","10"),
    /**
     * 监控采集
     */
    MONITORING_COLLECTION("监控采集","20"),
    /**
     * 供电配电
     */
    POWER_DISTRIBUTION("供电配电","30"),
    /**
     * 环境空调
     */
    ENVIRONMENTAL_AIR_CONDITIONER("环境空调","40"),
    /**
     * 安全防范
     */
    SAFETY_PRECAUTION("安全防范","50");

    /**
     * 类型名称
     */
    private String sortName;
    /**
     * 类型编码
     */
    private String sortCode;


    TroubleSortEnum(String sortName, String sortCode) {
        this.sortName = sortName;
        this.sortCode = sortCode;
    }

    public String getSortName() {
        return sortName;
    }

    public String getSortCode() {
        return sortCode;
    }
}
