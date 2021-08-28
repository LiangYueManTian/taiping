package com.taiping.enums.manage;
/**
 * 运维管理活动来源枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public enum ManageSourceEnum {
    /**
     * 容量分析
     */
    CAPACITY("容量分析", "1001"),
    /**
     * 能耗分析
     */
    ENERGY("能耗分析", "1002"),
    /**
     * 问题分析
     */
    PROBLEM("问题分析", "1003"),
    /**
     * 生产力分析
     */
    PRODUCTIVITY("生产力分析", "1004"),
    /**
     * 运维管理活动
     */
    MANAGE("运维管理活动", "1005"),
    /**
     * 风控管理
     */
    RISK_MANAGE("风控管理","1006"),
    /**
     * 维护保养计划
     */
    MAINTENANCE_PLAN("维护保养计划","1007"),
    /**
     * 预算与采购
     */
    BUDGET("预算与采购","1008"),
    /**
     * 来源对象
     * 用户新增
     */
    USER_CREATE("用户新增", "100001");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String code;

    ManageSourceEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static String getNameForCode(String code) {
        for (ManageSourceEnum manageSourceEnum : ManageSourceEnum.values()) {
            if (manageSourceEnum.getCode().equals(code)) {
                return manageSourceEnum.getName();
            }
        }
        return "";
    }
}
