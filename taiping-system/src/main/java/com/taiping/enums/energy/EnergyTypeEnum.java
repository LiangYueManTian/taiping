package com.taiping.enums.energy;

/**
 * 能耗类型枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum EnergyTypeEnum {

    /**
     * 总能耗
     */
    ALL_ENERGY("总能耗", "1"),

    /**
     * IT能耗
     */
    IT_ENERGY("IT能耗", "2"),


    /**
     *  动力能耗
     */
    POWER_ENERGY("动力能耗", "3");


    private String name;

    private String code;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getCodeByName(String name) {
        for (EnergyTypeEnum typeEnum : EnergyTypeEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum.getCode();
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
    public static String getNameByCode(String code) {
        for (EnergyTypeEnum heatEnum : EnergyTypeEnum.values()) {
            if (heatEnum.getCode().equals(code)) {
                return heatEnum.getName();
            }
        }
        return "";
    }

    EnergyTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
