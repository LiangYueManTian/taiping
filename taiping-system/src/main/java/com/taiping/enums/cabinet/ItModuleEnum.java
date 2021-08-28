package com.taiping.enums.cabinet;

/**
 * it能耗枚举值
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ItModuleEnum {

    /**
     * 模块一
     */
    MODULE_ONE("模块一", "1"),

    /**
     * 模块二
     */
    MODULE_TWO("模块二", "2");

    private String moduleName;

    private String module;

    ItModuleEnum(String moduleName, String module) {
        this.moduleName = moduleName;
        this.module = module;
    }

    /**
     * 根据模块获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 模块名称
     * @return 模块code
     */
    public static String getModuleByName(String name) {
        for (ItModuleEnum itModuleEnum : ItModuleEnum.values()) {
            if (itModuleEnum.getModuleName().equals(name)) {
                return itModuleEnum.getModule();
            }
        }
        return "";
    }

    /**
     * 根据code获取模块
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 模块code
     * @return 模块Name
     */
    public static String getModuleByCode(String code) {
        for (ItModuleEnum itModuleEnum : ItModuleEnum.values()) {
            if (itModuleEnum.getModule().equals(code)) {
                return itModuleEnum.getModuleName();
            }
        }
        return "";
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModule() {
        return module;
    }
}
