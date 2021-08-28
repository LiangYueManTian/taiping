package com.taiping.enums.manage;

/**
 * 运维管理活动项目枚举类
 *
 * @author chaofang@wistronits.c
 * @since 2019-10-29
 */
public enum ManageActivityEnum {
    /**
     * 持续关注
     */
    ATTENTION("持续关注", "2001"),
    /**
     * 风险控制
     */
    RISK_MANAGE("风险控制", "2002"),
    /**
     * 维护保养计划
     */
    MAINTENANCE("维护保养计划", "2003"),
    /**
     * 预算及采购
     */
    BUDGET_PURCHASE("预算及采购", "2004"),
    /**
     * 沟通
     */
    COMMUNICATE("沟通", "2005"),
    /**
     * 裁减作业
     */
    REDUCE("裁减作业", "2006");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String code;

    ManageActivityEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    /**
     * 校验是否存在此项目code
     * @param code  项目类型
     * @return true 存在，false 不存在
     */
    public static boolean check(String code) {
        for (ManageActivityEnum manageActivityEnum : ManageActivityEnum.values()) {
            if (manageActivityEnum.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
