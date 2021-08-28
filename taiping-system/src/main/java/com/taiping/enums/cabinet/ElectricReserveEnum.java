package com.taiping.enums.cabinet;

/**
 * 是否是配电保留
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricReserveEnum {

    /**
     * 不是配电因素保留
     */
    ELECTRIC_RESERVE_ONE("不保留", "0"),

    /**
     * 是配电因素保留
     */
    ELECTRIC_RESERVE_TWO("保留", "1");

    private String moduleName;

    private String module;

    ElectricReserveEnum(String moduleName, String module) {
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
        for (ElectricReserveEnum itModuleEnum : ElectricReserveEnum.values()) {
            if (itModuleEnum.getModuleName().equals(name)) {
                return itModuleEnum.getModule();
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
