package com.taiping.enums.problem;
/**
 * 问题分析分析参数Code枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-21
 */
public enum ProblemParamEnum {
    /**
     * 问题分析
     */
    PROBLEM("问题分析", "20", 0),
    /**
     * 生产力分析
     */
    PROBLEM_PARAM("故障分类", "2010", 0),
    /**
     * 团队负荷资源状态上限
     */
    TOP("TOPN配置", "201001", 3);

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
    private Integer value;

    /**
     *  @param name 名称
     * @param code code
     * @param value 默认值
     */
    ProblemParamEnum(String name, String code, Integer value) {
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

    public Integer getValue() {
        return value;
    }
}
