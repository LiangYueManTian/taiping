package com.taiping.enums.problem;


/**
 * 故障单一级分类供电配电分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum PowerDistTopEnum {

    /**
     * 液压检测
     */
    HYDRAULIC_TEST("液压检测","3001"),
    /**
     * 电能计量柜
     */
    ENERGY_METERING_CABINET("电能计量柜","3002"),
    /**
     * 低压进线总柜
     */
    LOW_MAIN_CABINET("低压进线总柜","3003"),
    /**
     * 列头柜
     */
    HEAD_CABINET("列头柜","3004"),
    /**
     * PDU
     */
    PDU("PDU","3005"),
    /**
     * EPS
     */
    EPS("EPS","3006"),
    /**
     * 发电机
     */
    GENERATOR("发电机","3007"),
    /**
     * 一般配电柜
     */
    GENERAL_DISTRIBUTION_CABINET("一般配电柜","3008"),
    /**
     * 电池
     */
    BATTERY("电池","3009"),
    /**
     * 高压开关柜
     */
    HIGH_VOLTAGE_SWITCHGEAR("高压开关柜","3010"),
    /**
     * 变压器
     */
    TRANSFORMER("变压器","3011"),
    /**
     * ATS
     */
    ATS("ATS","3012"),
    /**
     * STS
     */
    STS("STS","3013"),
    /**
     * 开关电源
     */
    SWITCH_POWER_SUPPLY("开关电源","3014"),
    /**
     * 电池检测
     */
    BATTERY_TEST("电池检测","3015"),
    /**
     * 母线
     */
    BUS_BAR("母线","3016"),
    /**
     * UPS
     */
    UPS("UPS","3017"),
    /**
     * 电量仪
     */
    ELECTRICITY_METER("电量仪","3018"),
    /**
     * 变压器温控器
     */
    TRANSFORMER_TEMPERATURE_CONTROLLER("变压器温控器","3019");


    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 分类编码
     */
    private String sortCode;


    PowerDistTopEnum(String sortName, String sortCode) {
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
