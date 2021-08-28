package com.taiping.enums.analyze.capacity;

/**
 * 阈值查询关联类型枚举值
 * @author hedongwei@wistronits.com
 * @date  2019/11/1 16:52
 */
public enum ThresholdRelatedTypeEnum {

    /**
     * 推荐
     */
    RECOMMEND("推荐", "1"),

    /**
     * 显示
     */
    VIEW("显示", "2");

    /**
     * 类型名称
     */
    private String relatedTypeName;
    /**
     * 类型
     */
    private String relatedType;

    ThresholdRelatedTypeEnum(String relatedTypeName, String relatedType) {
        this.relatedTypeName = relatedTypeName;
        this.relatedType = relatedType;
    }

    public String getRelatedTypeName() {
        return relatedTypeName;
    }

    public String getRelatedType() {
        return relatedType;
    }

    /**
     * 类型名称
     * @param relatedType 类型名称
     * @return 类型code
     */
    public static String getName(String relatedType) {
        for (ThresholdRelatedTypeEnum relatedTypeEnum : ThresholdRelatedTypeEnum.values()) {
            if (relatedTypeEnum.getRelatedTypeName().equals(relatedType)) {
                return relatedTypeEnum.getRelatedType();
            }
        }
        return ThresholdRelatedTypeEnum.VIEW.getRelatedType();
    }
}
