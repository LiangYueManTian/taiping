package com.taiping.enums.cabinet;

import com.taiping.constant.capacity.CapacityConstant;

/**
 * 机柜Excel表sheet标识枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ItModuleAndDataEnum {

    /**
     *  第一个数据的模块
     */
    MODULE_AND_DATA_ONE(CapacityConstant.IT_DATA_NAME_ONE, ItModuleEnum.MODULE_ONE.getModule()),


    /**
     *  第二个数据的模块
     */
    MODULE_AND_DATA_TWO(CapacityConstant.IT_DATA_NAME_TWO, ItModuleEnum.MODULE_ONE.getModule()),

    /**
     *  第三个数据的模块
     */
    MODULE_AND_DATA_THREE(CapacityConstant.IT_DATA_NAME_THREE, ItModuleEnum.MODULE_TWO.getModule()),

    /**
     *  第四个数据的模块
     */
    MODULE_AND_DATA_FOUR(CapacityConstant.IT_DATA_NAME_FOUR, ItModuleEnum.MODULE_TWO.getModule()),

    /**
     *  第五个数据的模块
     */
    MODULE_AND_DATA_FIVE(CapacityConstant.IT_DATA_NAME_FIVE, ItModuleEnum.MODULE_ONE.getModule()),


    /**
     *  第六个数据的模块
     */
    MODULE_AND_DATA_SIX(CapacityConstant.IT_DATA_NAME_SIX, ItModuleEnum.MODULE_ONE.getModule()),

    /**
     *  第七个数据的模块
     */
    MODULE_AND_DATA_SEVEN(CapacityConstant.IT_DATA_NAME_SEVEN, ItModuleEnum.MODULE_TWO.getModule()),

    /**
     *  第八个数据的模块
     */
    MODULE_AND_DATA_EIGHT(CapacityConstant.IT_DATA_NAME_EIGHT, ItModuleEnum.MODULE_TWO.getModule());

    private String dataName;

    private String moduleName;

    ItModuleAndDataEnum(String dataName, String moduleName) {
        this.dataName = dataName;
        this.moduleName = moduleName;
    }

    /**
     * 根据数据获取模块code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 模块code
     */
    public static String getModuleByData(String name) {
        for (ItModuleAndDataEnum itModuleAndDataEnum : ItModuleAndDataEnum.values()) {
            if (itModuleAndDataEnum.getDataName().equals(name)) {
                return itModuleAndDataEnum.getModelName();
            }
        }
        return "";
    }

    public String getDataName() {
        return dataName;
    }

    public String getModelName() {
        return moduleName;
    }
}
