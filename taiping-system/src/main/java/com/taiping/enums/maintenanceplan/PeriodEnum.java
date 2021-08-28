package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/11/7
 * 维保计划周期枚举
 */
public enum PeriodEnum {
    /**
     * 周度
     */
    WEEKLY("周度",1),
    /**
     * 月度
     */
    MONTHLY("月度",2),
    /**
     * 季度
     */
    QUARTER("季度",3),
    /**
     * 半年度
     */
    HALF_YEAR("半年度",4),
    /**
     * 年度
     */
    YEAR("年度",5),
    /**
     * 单次
     */
    ONCE("单次",6);

    /**
     * 周期
     */
    private String period;
    /**
     * code
     */
    private Integer code;

    PeriodEnum(String period, Integer code) {
        this.period = period;
        this.code = code;
    }

    public String getPeriod() {
        return period;
    }

    public Integer getCode() {
        return code;
    }
}
