package com.taiping.enums.annex;
/**
 * 附件所属模块枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
public enum AnnexTypeEnum {
    /**
     * 风险控制
     */
    RISK_MANAGE("风险控制", "3001"),
    /**
     * 预算及采购
     */
    BUDGET_PURCHASE("预算及采购", "3002"),
    /**
     * 运维管理活动
     */
    MANAGE("运维管理活动", "3003"),
    /**
     * 维护保养活动
     */
    MAINTENANCE_PLAN("维护保养计划","3004"),
    /**
     * 资产基本信息
     */
    ASSET_INFO("资产基本信息","3005");

    /**
     * 名称
     */
    private String mode;
    /**
     * 类型
     */
    private String type;

    AnnexTypeEnum(String name, String type) {
        this.mode = name;
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据类型获取模块名称
     * @param type 模块类型
     * @return 模块名称
     */
    public static String getName(String type) {
        for (AnnexTypeEnum annexTypeEnum : AnnexTypeEnum.values()) {
            if (annexTypeEnum.getType().equals(type)) {
                return annexTypeEnum.getMode();
            }
        }
        return AnnexTypeEnum.BUDGET_PURCHASE.getMode();
    }
}
