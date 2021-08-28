package com.taiping.enums.manage;
/**
 * 运维管理活动来源类型枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public enum ManageSourceTypeEnum {
    /**
     * 问题分析故障单总览
     */
    PROBLEM_OVERVIEW("问题分析故障单总览", "100301"),
    /**
     * 问题分析供水、供电系统故障
     */
    PROBLEM_FLASH_OFF("问题分析供水、供电系统故障", "100302"),
    /**
     * 问题分析故障频发设备TopN
     */
    PROBLEM_TROUBLE_TYPE("问题分析故障频发设备TopN", "100303"),
    /**
     * 问题分析按故障涉及系统分类分析
     */
    PROBLEM_TROUBLE_TYPE_DETAIL("问题分析按故障涉及系统分类分析", "100304"),


    /**
     * 生产力分析现状
     */
    PRODUCTIVITY_CURRENT("生产力分析现状", "100401"),
    /**
     * 生产力分析未来趋势
     */
    PRODUCTIVITY_FORECAST("生产力分析未来趋势", "100402"),


    /**
     * 容量分析 空间容量-楼层-现状
     */
    FLOOR_ACTUALITY("空间容量-楼层-现状", "100101"),
    /**
     * 容量分析 空间容量-机柜-现状
     */
    CABINET_ACTUALITY("空间容量-机柜-现状", "100102"),
    /**
     * 容量分析 空间容量-功能区-现状
     */
    DEVICE_TYPE_ACTUALITY("空间容量-功能区-现状", "100103"),
    /**
     * 容量分析 电力容量-模块-现状
     */
    MODULE_ACTUALITY("电力容量-模块-现状", "100104"),
    /**
     * 容量分析 电力容量-UPS-现状
     */
    UPS_ACTUALITY("电力容量-UPS-现状", "100105"),
    /**
     * 容量分析 电力容量-列头柜-现状
     */
    CABINET_COLUMN_ACTUALITY("电力容量-列头柜-现状", "100106"),
    /**
     * 容量分析 电力容量-PDU-现状
     */
    PDU_ACTUALITY("电力容量-PDU-现状", "100107"),
    /**
     * 容量分析 配线架-路由类型-现状
     */
    PORT_TYPE_ACTUALITY("配线架-路由类型-现状", "100108"),
    /**
     * 容量分析 空间容量-楼层-趋势
     */
    FLOOR_TREND("空间容量-楼层-趋势", "100109"),
    /**
     * 容量分析 空间容量-机柜-趋势
     */
    CABINET_TREND("空间容量-机柜-趋势", "100110"),
    /**
     * 容量分析 空间容量-功能区-趋势
     */
    DEVICE_TYPE_TREND("空间容量-功能区-趋势", "100111"),
    /**
     * 容量分析 电力容量-模块-趋势
     */
    MODULE_TREND("电力容量-模块-趋势", "100112"),
    /**
     * 容量分析 电力容量-UPS-趋势
     */
    UPS_TREND("电力容量-UPS-趋势", "100113"),
    /**
     * 容量分析 电力容量-列头柜-趋势
     */
    CABINET_COLUMN_TREND("电力容量-列头柜-趋势", "100114"),
    /**
     * 容量分析 电力容量-PDU-趋势
     */
    PDU_TREND("电力容量-PDU-趋势", "100115"),
    /**
     * 容量分析 配线架-路由类型-趋势
     */
    PORT_TYPE_TREND("配线架-路由类型-趋势", "100116"),

    /**
     *  能耗分析 暖通分项
     */
    HEAT_ITEM("能耗分析-暖通分项", "100201"),
    /**
     *  能耗分析 数据机房
     */
    DATA_ROOM("能耗分析-数据机房", "100202"),
    /**
     * 能耗分析 PUE值
     */
    PUE_INFO("数据中心能耗信息-PUE", "100203"),
//    /**
//     *  能耗分析IT负荷
//     */
//    IT_LOAD("数据中心能耗分析-IT能耗", "100201"),
//    /**
//     *  能耗分析动力负荷
//     */
//    POWER_LOAD("数据中心能耗分析-动力能耗", "100202"),
//    /**
//     * 能耗分析总能耗
//     */
//    All_LOAD("数据中心能耗分析-总能耗", "100203"),
//    /**
//     * 能耗分析PUE值
//     */
//    PUE_INFO("数据中心能耗信息-PUE", "100204"),
//    /**
//     *  能耗分析冷机
//     */
//    REFRIGERATOR("暖通分项能耗分析-冷机", "100205"),
//    /**
//     *  能耗分析水泵
//     */
//    WATER_PUMP("暖通分项能耗分析-水泵", "100206"),
//    /**
//     *  能耗分析精密空调
//     */
//    PRECISION_AIR("暖通分项能耗分析-精密空调", "100207"),
//    /**
//     *  能耗分析冷塔
//     */
//    COOLING_TOWER("暖通分项能耗分析-冷塔", "100208");
    /**
     * 维保计划延期
     */
    MAINTENANCE_PLAN_DELAY("维保计划延期","100701"),
    /**
     * 年度总预算
     */
    YEAR_BUDGET("年度总预算", "100801"),
    /**
     *单项预算
     */
    INDIVIDUAL_BUDGET("单项预算","100802"),
    /**
     *采购项目分析
     */
    PURCHASE_ANALYSIS("采购项目分析","100803");
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String code;

    ManageSourceTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
