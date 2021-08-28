package com.taiping.enums.problem;


/**
 * 故障单一级分类监控采集分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum MonConTopEnum {

    /**
     * 采集模块
     */
    ACQUISITION_MODULE("采集模块","2001"),
    /**
     * U位资产检测
     */
    ASSET_TEST("U位资产检测","2002"),
    /**
     * 采集主机
     */
    ACQUISITION_HOST("采集主机","2003"),
    /**
     * 液压检测
     */
    HYDRAULIC_TEST("液压检测","2004"),
    /**
     * 电池巡检
     */
    BATTERY_INSPECTION("电池巡检","2005");

    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 分类编码
     */
    private String sortCode;


    MonConTopEnum(String sortName, String sortCode) {
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
