package com.taiping.enums.energy;

/**
 * 电量能耗类型是否是暖通
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerTypeHeatEnum {

    /**
     *  变压器输出柜类型
     */
    OUTPUT_CABINET(ElectricPowerTypeEnum.OUTPUT_CABINET.getType(), ElectricPowerHeatEnum.NOT_IS_HEAT_ITEM.getCode()),


    /**
     *  进线
     */
    INCOMING_LINE(ElectricPowerTypeEnum.INCOMING_LINE.getType(), ElectricPowerHeatEnum.NOT_IS_HEAT_ITEM.getCode()),

    /**
     *  冷机
     */
    REFRIGERATOR(ElectricPowerTypeEnum.REFRIGERATOR.getType(), ElectricPowerHeatEnum.IS_HEAT_ITEM.getCode()),

    /**
     *  水泵
     */
    WATER_PUMP(ElectricPowerTypeEnum.WATER_PUMP.getType(), ElectricPowerHeatEnum.IS_HEAT_ITEM.getCode()),

    /**
     *  精密空调
     */
    PRECISION_AIR(ElectricPowerTypeEnum.PRECISION_AIR.getType(), ElectricPowerHeatEnum.IS_HEAT_ITEM.getCode()),


    /**
     *  冷塔
     */
    COOLING_TOWER(ElectricPowerTypeEnum.COOLING_TOWER.getType(), ElectricPowerHeatEnum.IS_HEAT_ITEM.getCode());


    private String typeName;

    private String type;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getTypeByName(String name) {
        for (ElectricPowerTypeHeatEnum typeEnum : ElectricPowerTypeHeatEnum.values()) {
            if (typeEnum.getTypeName().equals(name)) {
                return typeEnum.getType();
            }
        }
        return "";
    }


    /**
     * 根据数据类型获取名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 数据code
     * @return 类型名称
     */
    public static String getTypeNameByCode(String code) {
        for (ElectricPowerTypeHeatEnum typeEnum : ElectricPowerTypeHeatEnum.values()) {
            if (typeEnum.getType().equals(code)) {
                return typeEnum.getTypeName();
            }
        }
        return "";
    }

    ElectricPowerTypeHeatEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
