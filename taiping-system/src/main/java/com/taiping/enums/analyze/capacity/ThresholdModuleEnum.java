package com.taiping.enums.analyze.capacity;

/**
 * 阈值模块枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
public enum ThresholdModuleEnum {
    /**
     * 空间容量
     */
    SPACE("空间容量", "1"),
    /**
     * 电力容量
     */
    ELECTRIC("电力容量", "2"),

    /**
     * 综合布线
     */
    GENERIC_CABLING("综合布线", "3");

    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 模块
     */
    private String module;

    ThresholdModuleEnum(String moduleName, String module) {
        this.moduleName = moduleName;
        this.module = module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModule() {
        return module;
    }

    /**
     * 根据获取模块名称
     * @param type 模块类型
     * @return 模块名称
     */
    public static String getName(String type) {
        for (ThresholdModuleEnum thresholdModuleEnum : ThresholdModuleEnum.values()) {
            if (thresholdModuleEnum.getModuleName().equals(type)) {
                return thresholdModuleEnum.getModule();
            }
        }
        return ThresholdModuleEnum.SPACE.getModule();
    }
}
