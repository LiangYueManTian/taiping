package com.taiping.enums.energy;

/**
 * 电力暖通分析枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerHeatEnum {

    /**
     * 不是动力能耗暖通
     */
    NOT_IS_HEAT_ITEM("不是动力能耗暖通", "0"),


    /**
     *  是动力能耗暖通
     */
    IS_HEAT_ITEM("是动力能耗暖通", "1");


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
        for (ElectricPowerHeatEnum typeEnum : ElectricPowerHeatEnum.values()) {
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
        for (ElectricPowerHeatEnum heatEnum : ElectricPowerHeatEnum.values()) {
            if (heatEnum.getCode().equals(code)) {
                return heatEnum.getName();
            }
        }
        return "";
    }

    ElectricPowerHeatEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
