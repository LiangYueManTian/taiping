package com.taiping.constant.maintenance;

/**
 * @author zhangliangyu
 * @since 2019/11/27
 * 维护保养计划常量定义类
 */
public class MaintenancePlanConstant {
    /**
     * 维保预提醒邮件标题
     */
    public static final String EMAIL_TITLE = "维保预提醒";
    /**
     * 参数一
     */
    public static final String PARAM_ONE = "planName";
    /**
     * 参数二
     */
    public static final String PARAM_TWO = "preTime";
    /**
     * 参数三
     */
    public static final String PARAM_THREE = "time";
    /**
     * 表头宽度
     */
    public static final String HEADER_WIDTH = "40px";
    /**
     * 维保模块下拉选单一级code
     */
    public static final String MAINTENANCE_FIRST_CODE = "20";
    /**
     * 维保计划id数据库字段
     */
    public static final String MAINTENANCE_PLAN_ID_COLUMN = "maintenance_plan_id";
    /**
     * 维保合同id
     */
    public static final String CONTRACT_ID = "contractId";
    /**
     * 维保合同id数据库字段
     */
    public static final String CONTRACT_ID_COLUMN = "contract_id";
    /**
     * 资产基本信息id
     */
    public static final String ASSET_INFO_ID = "assetInfoId";
    /**
     * 资产基本信息id数据库字段
     */
    public static final String ASSET_INFO_ID_COLUMN = "asset_info_id";
    /**
     * 数据库操作in
     */
    public static final String OPERATOR_IN = "in";
    /**
     * 换行符
     */
    public static final String LINE_FEED_REPLACE = "\n";
    /**
     * 统计时间数据库字段
     */
    public static final String STATISTICS_TIME = "statistics_time";
    /**
     * 趋势预测平滑系数
     */
    public static final Double SMOOTHING_COEFFICIENT = 0.6;

}
