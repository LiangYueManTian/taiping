package com.taiping.constant.operation;

/**
 * 问题分析常量定义类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-05
 */
public class OperationConstant {
    /**
     * 故障分类层数
     */
    public static final String TROUBLE_TYPE_SPLIT = ">";
    /**
     * 故障分类拼接
     */
    public static final String TROUBLE_TYPE_CONNECT = " > ";
    /**
     * 故障分类开头
     */
    public static final String TROUBLE_TYPE_START = "基础设施 > ";
    /**
     * 故障分类层数
     */
    public static final int TROUBLE_TYPE_LENGTH = 3;
    /**
     * 故障分类层数
     */
    public static final int TROUBLE_TYPE_TOP = 1;
    /**
     * 故障分类层数
     */
    public static final int TROUBLE_TYPE_SECONDARY = 2;
    /**
     * name替换
     */
    public static final String NAME_REPLACE = "${name}";
    /**
     * num替换
     */
    public static final String NUM_REPLACE = "${num}";
    /**
     * TOP替换
     */
    public static final String TOP_REPLACE = "${TOP}";
    /**
     * year替换
     */
    public static final String YEAR_REPLACE = "${year}";
    /**
     * month替换
     */
    public static final String MONTH_REPLACE = "${month}";
    /**
     * yearName替换
     */
    public static final String YEAR_NAME_REPLACE = "${yearName}";
    /**
     * monthName替换
     */
    public static final String MONTH_NAME_REPLACE = "${monthName}";
    /**
     * 增长了
     */
    public static final String ADD_REPLACE = "增长了";
    /**
     * 减少了
     */
    public static final String REDUCE_REPLACE = "减少了";
    /**
     * 故障级别运维管理活动原因
     */
    public static final String TROUBLE_LEVEL_MANAGE = "故障级别为${name}的故障单有${num}个";
    /**
     * 替换
     */
    public static final String CONNECT_REPLACE = "-";
    /**
     * 分析报告同比环比
     */
    public static final String REPORT_MSG = "同比${yearName}年${year},环比${monthName}月${month}";
    /**
     * 故障分类TOP运维管理活动原因
     */
    public static final String TOP_CAUSE = "故障分类TOP${TOP}:${name}，故障单数：${num}";
    /**
     * 运维管理活动原因
     */
    public static final String OFF_CAUSE = "${name}发生了${num}次";
    /**
     * 故障分类TOP运维管理活动原因
     */
    public static final String TYPE_TOP_CAUSE = "故障一级分类${year}TOP${TOP}:${name}，故障单数：${num}";
    /**
     * 换行
     */
    public static final String LINE_FEED_REPLACE = "\n";
    /**
     * 换行
     */
    public static final String LINE_REPLACE = "\r";
    /**
     * 空格
     */
    public static final String BLANK_SPACE_REPLACE = "  ";


    public static final String OPER_SHEET_NAME_CHAI_FA = "柴发";
    public static final String OPER_SHEET_NAME_TRANS = "变压器";
    public static final String OPER_SHEET_NAME_DIST_LOW = "低压柜";
    public static final String OPER_SHEET_NAME_DIST_HIGH = "高压柜";
    public static final String OPER_EXCEPTION_LINE = "###";
    public static final String OPER_EXCEPTION_NAME = "@@@";


}
