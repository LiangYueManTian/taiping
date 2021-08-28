package com.taiping.enums.maintenanceplan;

/**
 * @author zhangliangyu
 * @since 2019/12/13
 * 延期类型枚举
 */
public enum  DelayTypeEnum{
    /**
     * 延期小于一个周期且小于一个月
     */
    DELAY("延期小于一个周期且小于一个月",0),
    /**
     *延期一到两个周期
     */
    DELAY_ONE_PERIOD_TO_TWO_PERIOD("延期一到两个周期",1),
    /**
     *延期两个周期以上
     */
    DELAY_TWO_PERIOD_UPPER("延期两个周期以上",2),
    /**
     *延期一到两个月
     */
    DELAY_ONE_MONTH_TO_TWO_MONTH("延期一到两个月",3),
    /**
     *延期两月以上
     */
    DELAY_TWO_MONTH_UPPER("延期两月以上",4);
    /**
     * 类型
     */
    private String type;
    /**
     * code
     */
    private Integer code;

    DelayTypeEnum(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public Integer getCode() {
        return code;
    }
}
