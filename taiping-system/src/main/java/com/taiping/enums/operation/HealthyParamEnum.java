package com.taiping.enums.operation;
/**
 * 健康卡参数枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public enum HealthyParamEnum {
    /**
     * 水冷机组运行时长
     */
    WATER_COOLED_UNIT("水冷机组运行时长", "01"),
    /**
     * 消防蓄水池
     */
    FIRE_RESERVOIR("消防蓄水池", "02");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    HealthyParamEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
