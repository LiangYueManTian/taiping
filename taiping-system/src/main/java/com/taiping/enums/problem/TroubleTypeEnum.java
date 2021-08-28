package com.taiping.enums.problem;

/**
 * 故障单分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum TroubleTypeEnum {
    /**
     * 导入
     */
    IMPORT("导入","0"),
    /**
     * 新增
     */
    ADD("新增","1"),
    /**
     * 物业
     */
    PROPERTY("物业","0"),
    /**
     * 太平金科
     */
    TAI_PING("太平金科","1");
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型编码
     */
    private String type;


    TroubleTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getType() {
        return type;
    }
}
