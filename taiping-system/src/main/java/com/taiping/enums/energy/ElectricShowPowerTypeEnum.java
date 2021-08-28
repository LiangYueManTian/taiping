package com.taiping.enums.energy;

/**
 * 显示能耗类型枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricShowPowerTypeEnum {

    /**
     *  暖通系统
     */
    HEAR_SYSTEM("暖通系统", "1"),


    /**
     *  其他动力
     */
    OTHER("其他动力", "2");


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
        for (ElectricShowPowerTypeEnum typeEnum : ElectricShowPowerTypeEnum.values()) {
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
        for (ElectricShowPowerTypeEnum typeEnum : ElectricShowPowerTypeEnum.values()) {
            if (typeEnum.getType().equals(code)) {
                return typeEnum.getTypeName();
            }
        }
        return "";
    }

    ElectricShowPowerTypeEnum(String typeName, String type) {
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
