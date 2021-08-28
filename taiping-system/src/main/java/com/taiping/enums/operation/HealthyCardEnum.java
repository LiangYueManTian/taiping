package com.taiping.enums.operation;
/**
 * 健康卡枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-27
 */
public enum HealthyCardEnum {
    /**
     * 水冷机组运行时长
     */
    WATER_COOLED_UNIT("水冷机组运行时长", "1"),
    /**
     * 消防蓄水池
     */
    FIRE_RESERVOIR("消防蓄水池", "2"),
    /**
     * 空间
     */
    CABINET_SPACE("空间", "3"),
    /**
     * 电力
     */
    CABINET_POWER("电力", "4"),
    /**
     * 能耗pue
     */
    ENERGY("能耗", "5"),
    /**
     * 预采购
     */
    BUDGET("预采购", "6"),
    /**
     * 服务可用性
     */
    SERVICE_AVAILABILITY("服务可用性", "7");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    HealthyCardEnum(String name, String type) {
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
