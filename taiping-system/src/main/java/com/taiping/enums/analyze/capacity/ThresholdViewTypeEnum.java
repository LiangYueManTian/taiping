package com.taiping.enums.analyze.capacity;

/**
 * 阈值查询类型枚举值
 * @author hedongwei@wistronits.com
 * @date  2019/11/1 16:52
 */
public enum ThresholdViewTypeEnum {

    /**
     * 显示
     */
    VIEW("显示", "1"),

    /**
     * 趋势
     */
    Trend("趋势", "2");

    /**
     * 类型名称
     */
    private String viewTypeName;
    /**
     * 类型
     */
    private String viewType;

    ThresholdViewTypeEnum(String viewTypeName, String viewType) {
        this.viewTypeName = viewTypeName;
        this.viewType = viewType;
    }

    public String getViewTypeName() {
        return viewTypeName;
    }

    public String getViewType() {
        return viewType;
    }

    /**
     * 根据类型名称
     * @param viewType 模块类型
     * @return 类型名称
     */
    public static String getName(String viewType) {
        for (ThresholdViewTypeEnum viewTypeEnum : ThresholdViewTypeEnum.values()) {
            if (viewTypeEnum.getViewTypeName().equals(viewType)) {
                return viewTypeEnum.getViewType();
            }
        }
        return ThresholdViewTypeEnum.VIEW.getViewType();
    }
}
