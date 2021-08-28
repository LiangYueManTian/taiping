package com.taiping.constant.operation;

/**
 * 问题分析服务返回信息
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-05
 */
public class OperationResultMsg {
    /**
     * 请求参数错误
     */
    public static final String PARAM_ERROR = "请求参数错误";
    /**
     * Excel文件sheet页数量错误
     */
    public static final String SHEET_ERROR = "Excel文件sheet页数量错误";
    /**
     * 行数据错误
     */
    public static final String DOUBLE_ERROR = "${name}第${row}行数据(${cell})不是数值类型";
    /**
     * 行数据错误
     */
    public static final String ROW_TYPE_ERROR = "第${row}行数据故障分类(${name})格式错误，系统无该分类";
    /**
     * 文件格式错误
     */
    public static final String FILE_TYPE_ERROR = "文件格式错误";
    public static final String POWER_DATA_EXIST = "配电系统本月数据已存在 不可重复导入!";
    /**
     * 文件无数据
     */
    public static final String FILE_EMPTY = "文件无可用数据";
    public static final String NOT_FIND_SHHET_NAME = "sheetName 不包含(变压器、高压柜、低压柜、柴发),无法解析数据!";
    /**
     * 行数据错误
     */
    public static final String ROW_CELL_DATE_ERROR = "第${row}行日期格式(${name})错误";
    /**
     * 未导入数据
     */
    public static final String PROBLEM_DATA_EMPTY = "未导入数据";
    /**
     * 该故障单已删除
     */
    public static final String TROUBLE_TICKET_DELETED = "该故障单已删除";
    /**
     * 导入的故障单不能被修改
     */
    public static final String TROUBLE_TICKET_IMPORT_UPDATE = "导入的故障单不能被修改";
    /**
     * 导入的故障单不能被删除
     */
    public static final String TROUBLE_TICKET_IMPORT_DELETE = "导入的故障单不能被删除";
    /**
     * 故障单号已存在
     */
    public static final String TICKET_CODE_HAVE = "故障单号已存在";
}
