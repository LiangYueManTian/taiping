package com.taiping.enums.problem;


/**
 * 故障单一级分类环境空调分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum EnvAirConTopEnum {

    /**
     * 精密空调
     */
    PRECISION_AIR_CONDITIONER("精密空调","4001"),
    /**
     * 冷却塔
     */
    COOLING_TOWER("冷却塔","4002"),
    /**
     * 除湿器
     */
    DEHUMIDIFIER("除湿器","4003"),
    /**
     * 新风机
     */
    FRESH_AIR_FAN("新风机","4004"),
    /**
     * 冷水机组
     */
    WATER_CHILLER("冷水机组","4005"),
    /**
     * 冷冻水泵
     */
    CHILLED_WATER_PUMP("冷冻水泵","4006"),
    /**
     * 蓄冷罐
     */
    COLD_STORAGE_TANK("蓄冷罐","4007"),
    /**
     * 加湿器
     */
    HUMIDIFIER("加湿器","4008"),
    /**
     * 温度巡检仪
     */
    TEMPERATURE_INSPECTION_INSTRUMENT("温度巡检仪","4009"),
    /**
     * 排风机
     */
    BLOWER("排风机","4010"),
    /**
     * 冷却水泵
     */
    COOLING_WATER_PUMP("冷却水泵","4011"),
    /**
     * 普通空调
     */
    COMMON_AIR_CONDITIONER("普通空调","4012"),
    /**
     * 漏水检测
     */
    LEAKAGE_DETECTION("漏水检测","4013"),
    /**
     * 温湿度检测
     */
    TEMPERATURE_HUMIDITY_DETECTION("温湿度检测","4014"),
    /**
     * 空调优化器
     */
    AIR_CONDITIONER_OPTIMIZER("空调优化器","4015"),
    /**
     * 阀门
     */
    VALVE("阀门","4016"),
    /**
     * 加湿除湿一体机
     */
    HUMIDIFICATION_DE_MACHINE("加湿除湿一体机","4017"),
    /**
     * 空调遥控器
     */
    AIR_CONDITIONER_REMOTE("空调遥控器","4018"),
    /**
     * 变频空调
     */
    INVERTER_AIR_CONDITIONER("变频空调","4019");


    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 分类编码
     */
    private String sortCode;


    EnvAirConTopEnum(String sortName, String sortCode) {
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
