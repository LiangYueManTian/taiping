package com.taiping.enums.analyze.energy;

/**
 * 能耗分析类型枚举类
 * @author hedongwei@wistronits.com
 * @date 2019/11/5 15:32
 */
public enum EnergyStatisticalTypeEnum {

    /**
     * 冷机电耗
     */
    REFRIGERATOR_POWER("冷机电耗", "1"),

    /**
     * 水泵电耗
     */
    WATER_PUMP_POWER("水泵电耗", "2"),

    /**
     * 冷塔电耗
     */
    COOLING_TOWER("冷塔电耗", "3"),

    /**
     * 精密空调
     */
    PRECISION_AIR("精密空调电耗", "4"),

    /**
     * 动力能耗
     */
    POWER_ENERGY("动力能耗", "5"),

    /**
     * IT能耗
     */
    IT_ENERGY("IT能耗", "6"),


    /**
     * 总能耗
     */
    ALL_ENERGY("总能耗", "7"),

    /**
     * PUE
     */
    PUE("PUE", "8"),


    /**
     * 暖通分项
     */
    HEAT_ITEM("暖通分项", "9"),

    /**
     * 其他
     */
    OTHER("其他动力", "10");



    /**
     * 阈值名称
     */
    private String typeName;

    /**
     * 阈值code
     */
    private String type;

    EnergyStatisticalTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据获取类型
     * @param typeName 类型Name
     * @return 类型
     */
    public static String getType(String typeName) {
        for (EnergyStatisticalTypeEnum typeEnum : EnergyStatisticalTypeEnum.values()) {
            if (typeEnum.getTypeName().equals(typeName)) {
                return typeEnum.getType();
            }
        }
        return EnergyStatisticalTypeEnum.REFRIGERATOR_POWER.getType();
    }


    /**
     * 根据获取类型名称
     * @param type 类型
     * @return 类型名称
     */
    public static String getTypeName(String type) {
        for (EnergyStatisticalTypeEnum typeEnum : EnergyStatisticalTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getTypeName();
            }
        }
        return EnergyStatisticalTypeEnum.REFRIGERATOR_POWER.getTypeName();
    }
}
