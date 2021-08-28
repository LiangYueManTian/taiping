package com.taiping.enums.energy;

import com.taiping.constant.capacity.CapacityConstant;

/**
 * 电力类型数据枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricTypeAndDataEnum {

    /**
     *  第一个数据的模块
     */
    TYPE_AND_DATA_ONE(CapacityConstant.ELECTRIC_DATA_NAME_ONE, ElectricTypeEnum.IT_LOAD.getType()),


    /**
     *  第二个数据的模块
     */
    TYPE_AND_DATA_TWO(CapacityConstant.ELECTRIC_DATA_NAME_TWO, ElectricTypeEnum.IT_LOAD.getType()),

    /**
     *  第三个数据的模块
     */
    TYPE_AND_DATA_THREE(CapacityConstant.ELECTRIC_DATA_NAME_THREE, ElectricTypeEnum.IT_LOAD.getType()),

    /**
     *  第四个数据的模块
     */
    TYPE_AND_DATA_FOUR(CapacityConstant.ELECTRIC_DATA_NAME_FOUR, ElectricTypeEnum.IT_LOAD.getType()),

    /**
     *  第五个数据的模块
     */
    TYPE_AND_DATA_FIVE(CapacityConstant.ELECTRIC_DATA_NAME_FIVE, ElectricTypeEnum.POWER_LOAD.getType()),


    /**
     *  第六个数据的模块
     */
    TYPE_AND_DATA_SIX(CapacityConstant.ELECTRIC_DATA_NAME_SIX, ElectricTypeEnum.POWER_LOAD.getType()),

    /**
     *  第七个数据的模块
     */
    TYPE_AND_DATA_SEVEN(CapacityConstant.ELECTRIC_DATA_NAME_SEVEN, ElectricTypeEnum.POWER_LOAD.getType()),

    /**
     *  第八个数据的模块
     */
    TYPE_AND_DATA_EIGHT(CapacityConstant.ELECTRIC_DATA_NAME_EIGHT, ElectricTypeEnum.POWER_LOAD.getType()),


    /**
     *  第九个数据的模块
     */
    TYPE_AND_DATA_NINE(CapacityConstant.ELECTRIC_DATA_NAME_NINE, ElectricTypeEnum.BASEMENT.getType());

    private String dataName;

    private String typeName;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getTypeByData(String name) {
        for (ElectricTypeAndDataEnum itTypeAndDataEnum : ElectricTypeAndDataEnum.values()) {
            if (itTypeAndDataEnum.getDataName().equals(name)) {
                return itTypeAndDataEnum.getTypeName();
            }
        }
        return "";
    }

    ElectricTypeAndDataEnum(String dataName, String typeName) {
        this.dataName = dataName;
        this.typeName = typeName;
    }

    public String getDataName() {
        return dataName;
    }

    public String getTypeName() {
        return typeName;
    }
}
