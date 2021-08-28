package com.taiping.constant.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/11/28
 * 风控管理常量定义类
 */
public class RiskManageConstant {
    /**
     * 发现日期
     */
    public static final String FOUND_DATE = "foundDate";
    /**
     * 风险发现时间
     */
    public static final String RISK_FOUND_TIME = "riskFoundTime";
    /**
     * 风险发现时间数据库字段
     */
    public static final String RISK_FOUND_TIME_COLUMN = "risk_found_time";
    /**
     * 风险类型数据库字段
     */
    public static final String RISK_TYPE_COLUMN = "risk_type";
    /**
     * 风险追踪负责人数据库字段
     */
    public static final String TRACK_USER_COLUMN = "track_user";
    /**
     * 风险复检人数据库字段
     */
    public static final String CHECK_USER_COLUMN = "check_user";
    /**
     * 大于等于
     */
    public static final String GREATER_THAN_OR_EQUAL = "gte";
    /**
     * 小于等于
     */
    public static final String LESS_THAN_OR_EQUAL = "lte";
    /**
     * 降序
     */
    public static final String DESCENDING_ORDER = "desc";
    /**
     * 分号
     */
    public static final String SEMICOLON = ";";
    /**
     * 风险超时时间阈值二级code
     */
    public static final String RISK_TIMEOUT_TIME_SECOND_CODE = "5010";
    /**
     * 风险等级阈值二级code
     */
    public static final String RISK_LEVEL_SECOND_CODE = "5020";
    /**
     * 低等级风险code
     */
    public static final String LOW_LEVEL_RISK_CODE = "502010";
    /**
     * 高等级风险code
     */
    public static final String HIGH_LEVEL_RISK_CODE = "502020";
    /**
     * 连接符
     */
    public static final String LINK_SYMBOL = "-";
    /**
     * 趋势预测平滑系数
     */
    public static final Double SMOOTHING_COEFFICIENT = 0.6;
}
