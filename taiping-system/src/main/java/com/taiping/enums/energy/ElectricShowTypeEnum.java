package com.taiping.enums.energy;

/**
 * 电力类型枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricShowTypeEnum {

    /**
     *  IT负荷
     */
    IT_LOAD("IT能耗", "1"),


    /**
     *  动力负荷
     */
    POWER_LOAD("动力能耗", "2"),


    /**
     * 总能耗
     */
    All_LOAD("总能耗", "3"),

    /**
     * PUE值
     */
    PUE_INFO("PUE", "4"),

    /**
     *  冷机
     */
    REFRIGERATOR("冷机", "5"),

    /**
     *  水泵
     */
    WATER_PUMP("水泵", "6"),

    /**
     *  精密空调
     */
    PRECISION_AIR("精密空调", "7"),


    /**
     *  冷塔
     */
    COOLING_TOWER("冷塔", "8");


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
        for (ElectricShowTypeEnum typeEnum : ElectricShowTypeEnum.values()) {
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
        for (ElectricShowTypeEnum typeEnum : ElectricShowTypeEnum.values()) {
            if (typeEnum.getType().equals(code)) {
                return typeEnum.getTypeName();
            }
        }
        return "";
    }

    ElectricShowTypeEnum(String typeName, String type) {
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
