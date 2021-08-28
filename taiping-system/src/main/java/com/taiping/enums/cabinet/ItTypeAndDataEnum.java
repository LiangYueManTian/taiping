package com.taiping.enums.cabinet;

import com.taiping.constant.capacity.CapacityConstant;

/**
 * it类型数据枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ItTypeAndDataEnum {

    /**
     *  第一个数据的模块
     */
    TYPE_AND_DATA_ONE(CapacityConstant.IT_DATA_NAME_ONE, ItTypeEnum.TYPE_ONE.getType()),


    /**
     *  第二个数据的模块
     */
    TYPE_AND_DATA_TWO(CapacityConstant.IT_DATA_NAME_TWO, ItTypeEnum.TYPE_ONE.getType()),

    /**
     *  第三个数据的模块
     */
    TYPE_AND_DATA_THREE(CapacityConstant.IT_DATA_NAME_THREE, ItTypeEnum.TYPE_ONE.getType()),

    /**
     *  第四个数据的模块
     */
    TYPE_AND_DATA_FOUR(CapacityConstant.IT_DATA_NAME_FOUR, ItTypeEnum.TYPE_ONE.getType()),

    /**
     *  第五个数据的模块
     */
    TYPE_AND_DATA_FIVE(CapacityConstant.IT_DATA_NAME_FIVE, ItTypeEnum.TYPE_TWO.getType()),


    /**
     *  第六个数据的模块
     */
    TYPE_AND_DATA_SIX(CapacityConstant.IT_DATA_NAME_SIX, ItTypeEnum.TYPE_TWO.getType()),

    /**
     *  第七个数据的模块
     */
    TYPE_AND_DATA_SEVEN(CapacityConstant.IT_DATA_NAME_SEVEN, ItTypeEnum.TYPE_TWO.getType()),

    /**
     *  第八个数据的模块
     */
    TYPE_AND_DATA_EIGHT(CapacityConstant.IT_DATA_NAME_EIGHT, ItTypeEnum.TYPE_TWO.getType());

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
        for (ItTypeAndDataEnum itTypeAndDataEnum : ItTypeAndDataEnum.values()) {
            if (itTypeAndDataEnum.getDataName().equals(name)) {
                return itTypeAndDataEnum.getTypeName();
            }
        }
        return "";
    }

    ItTypeAndDataEnum(String dataName, String typeName) {
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
