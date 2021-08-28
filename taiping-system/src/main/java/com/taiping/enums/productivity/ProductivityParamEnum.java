package com.taiping.enums.productivity;
/**
 * 生产力分析参数Code枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-12
 */
public enum ProductivityParamEnum {
    /**
     * 生产力分析
     */
    PRODUCTIVITY("生产力分析", "70", 0.0),
    /**
     * 生产力分析
     */
    PRODUCTIVITY_PARAM("生产力分析二级", "7010", 0.0),
    /**
     * 团队负荷资源状态上限
     */
    SUPPER_LIMIT("团队负荷资源状态上限", "701001", 0.7),
    /**
     * 团队负荷资源状态下限
     */
    LOWER_LIMIT("团队负荷资源状态下限", "701002", 1.0),
    /**
     * 最多上架人数
     */
    MAX_PEOPLE("最多上架人数", "701003", 2.0);

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
    ProductivityParamEnum(String name, String code, Double value) {
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
