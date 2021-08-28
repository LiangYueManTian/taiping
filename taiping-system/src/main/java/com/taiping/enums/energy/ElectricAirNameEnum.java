package com.taiping.enums.energy;

/**
 * 能耗机密空调枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricAirNameEnum {

    /**
     *  第一个数据的模块
     */
    DATA_ONE(ElectricPowerItemEnum.DATA_TEN.getDataName(), "A201精密空调间A路"),


    /**
     *  第二个数据的模块
     */
    DATA_TWO(ElectricPowerItemEnum.DATA_ELEVEN.getDataName(), "A202精密空调间A路"),


    /**
     *  第三个数据的模块
     */
    DATA_THREE(ElectricPowerItemEnum.DATA_TWELVE.getDataName(), "A301精密空调间A路"),

    /**
     *  第四个数据的模块
     */
    DATA_FOUR(ElectricPowerItemEnum.DATA_THIRTEEN.getDataName(), "A302精密空调间A路"),

    /**
     *  第五个数据的模块
     */
    DATA_NINE(ElectricPowerItemEnum.DATA_TWENTY_NINE.getDataName(), "A201精密空调间B路"),

    /**
     *  第六个数据的模块
     */
    DATA_TEN(ElectricPowerItemEnum.DATA_THIRTY.getDataName(), "A202精密空调间B路"),


    /**
     *  第七个数据的模块
     */
    DATA_ELEVEN(ElectricPowerItemEnum.DATA_THIRTY_ONE.getDataName(), "A301精密空调间B路"),


    /**
     *  第八个数据的模块
     */
    DATA_TWELVE(ElectricPowerItemEnum.DATA_THIRTY_TWO.getDataName(), "A302精密空调间B路");


    private String dataCode;

    private String dataName;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getCodeByName(String name) {
        for (ElectricAirNameEnum dataEnum : ElectricAirNameEnum.values()) {
            if (dataEnum.getDataName().equals(name)) {
                return dataEnum.getDataCode();
            }
        }
        return "";
    }


    /**
     * 根据数据code获取数据名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param dataCode 数据code
     * @return 数据名称
     */
    public static String getDataNameByCode(String dataCode) {
        for (ElectricAirNameEnum resultEnum : ElectricAirNameEnum.values()) {
            if (resultEnum.getDataCode().equals(dataCode)) {
                return resultEnum.getDataName();
            }
        }
        return "";
    }

    ElectricAirNameEnum(String dataCode, String dataName) {
        this.dataCode = dataCode;
        this.dataName = dataName;
    }

    public String getDataCode() {
        return dataCode;
    }

    public String getDataName() {
        return dataName;
    }
}
