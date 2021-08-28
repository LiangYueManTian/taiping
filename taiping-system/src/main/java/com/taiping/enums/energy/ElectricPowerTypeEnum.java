package com.taiping.enums.energy;

/**
 * it类型数据枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerTypeEnum {

    /**
     *  变压器输出柜类型
     */
    OUTPUT_CABINET("变压器输出柜", "1"),


    /**
     *  进线
     */
    INCOMING_LINE("进线", "2"),

    /**
     *  冷机
     */
    REFRIGERATOR("冷机", "3"),

    /**
     *  水泵
     */
    WATER_PUMP("水泵", "4"),

    /**
     *  精密空调
     */
    PRECISION_AIR("精密空调", "5"),


    /**
     *  冷塔
     */
    COOLING_TOWER("冷塔", "6");


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
        for (ElectricPowerTypeEnum typeEnum : ElectricPowerTypeEnum.values()) {
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
        for (ElectricPowerTypeEnum typeEnum : ElectricPowerTypeEnum.values()) {
            if (typeEnum.getType().equals(code)) {
                return typeEnum.getTypeName();
            }
        }
        return "";
    }

    ElectricPowerTypeEnum(String typeName, String type) {
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
