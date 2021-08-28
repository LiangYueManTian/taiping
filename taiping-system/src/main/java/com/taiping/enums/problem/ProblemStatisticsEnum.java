package com.taiping.enums.problem;
/**
 * 问题分析分析报告枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum ProblemStatisticsEnum {
    /**
     * 故障总览
     */
    OVERVIEW("故障总览", "1"),
    /**
     * 断水断电
     */
    FLASH_OFF("断水断电", "2"),
    /**
     * 故障分类Top
     */
    TROUBLE_TYPE("故障分类Top", "3");
    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 类型编码
     */
    private String typeCode;

    ProblemStatisticsEnum(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }
}
