package com.taiping.enums.operation;
/**
 * 运行情况分析参数Code枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-26
 */
public enum OperationParamEnum {
    /**
     * 运行情况分析
     */
    OPERATION("运行情况分析", "80", 0.0),
    /**
     * 健康卡
     */
    HEALTHY("健康卡衡量参数配置", "8010", 0.0),
    /**
     * 暖通系统水冷机组运行时长最大差值
     */
    WATER_COOLED_UNIT("暖通系统水冷机组运行时长正常最大差值", "801001", 300.0),
    /**
     * 消防蓄水池可供冷却系统补水量
     */
    FIRE_RESERVOIR("消防蓄水池可供冷却系统正常最小补水量", "801002", 100.0),
    /**
     * 空间最大使用率
     */
    CABINET_SPACE("空间容量正常最大使用率", "801003", 80.0),
    /**
     * 电力最大使用率
     */
    CABINET_POWER("电力容量正常最大使用率", "801004", 80.0),
    /**
     * 能耗pue
     */
    ENERGY("能耗PUE正常最大值", "801005", 1.9),
    /**
     * 预采购
     */
    BUDGET("预算与采购正常最小执行进度", "801006", 80.0);

    /**
     * 名称
     */
    private String name;
    /**
     * code
     */
    private String code;

    /**
     * 默认值
     */
    private Double value;

    /**
     *  @param name 名称
     * @param code code
     * @param value 默认值
     */
    OperationParamEnum(String name, String code, Double value) {
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Double getValue() {
        return value;
    }
}
