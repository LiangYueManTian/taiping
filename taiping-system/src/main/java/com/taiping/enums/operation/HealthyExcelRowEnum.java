package com.taiping.enums.operation;
/**
 * 健康卡消防蓄水池和水冷机组运行时长Excel表行数据枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
public enum  HealthyExcelRowEnum {
    /**
     * 健康卡水冷机组运行时长
     */
    UNIT_NAME("unitName", 0),
    /**
     * 健康卡水冷机组运行时长
     */
    BEGIN_RUNTIME("beginRuntime", 1),
    /**
     * 健康卡水冷机组运行时长
     */
    CURRENT_RUNTIME("currentRuntime", 2),
    /**
     * 健康卡水冷机组运行时长
     */
    END_RUNTIME("endRuntime", 3),
    /**
     * 健康卡消防蓄水池
     */
    NAME("name", 0),
    /**
     * 健康卡消防蓄水池
     */
    WATER_CAPACITY("waterCapacity", 1),
    /**
     * 健康卡对应水位
     */
    WATER_LEVEL("waterLevel", 2),
    /**
     * 健康卡消防蓄水池
     */
    WATER_SUPPLEMENT("waterSupplement", 3),
    /**
     * 健康卡消防蓄水池
     */
    NATURAL_COOLING("naturalCooling", 4),
    /**
     * 健康卡消防蓄水池
     */
    ELECTRIC_REF_CURRENT("electricRefCurrent", 5),
    /**
     * 健康卡消防蓄水池
     */
    ELECTRIC_REF_FULL("electricRefFull", 6);

    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 单元格位置
     */
    private int cellNum;

    HealthyExcelRowEnum(String propertyName, int cellNum) {
        this.propertyName = propertyName;
        this.cellNum = cellNum;
    }


    public int getCellNum() {
        return cellNum;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
