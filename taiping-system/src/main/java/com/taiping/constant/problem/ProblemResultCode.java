package com.taiping.constant.problem;
/**
 * 问题分析服务返回码
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-05
 */
public class ProblemResultCode {
    /**
     * 传入参数错误
     */
    public static final Integer PARAM_ERROR = 190001;
    /**
     * Excel文件sheet页数量错误
     */
    public static final Integer SHEET_ERROR = 191001;
    /**
     * 行数据错误
     */
    public static final Integer ROW_LEVEL_ERROR = 191002;
    /**
     * 文件格式错误
     */
    public static final Integer FILE_TYPE_ERROR = 191003;
    /**
     * 文件无数据
     */
    public static final Integer FILE_EMPTY = 191004;
    /**
     * 行数据错误
     */
    public static final Integer ROW_TYPE_ERROR = 191005;
    /**
     * 行数据错误
     */
    public static final Integer ROW_CELL_DATE_ERROR = 191006;
    /**
     * 故障单所属错误
     */
    public static final Integer TICKET_TYPE_ERROR = 191007;
    /**
     * 行数据错误
     */
    public static final Integer ROW_DOUBLE_ERROR = 191007;
    /**
     * 未导入数据
     */
    public static final Integer PROBLEM_DATA_EMPTY = 190002;
    /**
     * 该故障单已删除
     */
    public static final Integer TROUBLE_TICKET_DELETED = 192001;
    /**
     * 导入的故障单不能被修改
     */
    public static final Integer TROUBLE_TICKET_IMPORT_UPDATE = 192002;
    /**
     * 导入的故障单不能被删除
     */
    public static final Integer TROUBLE_TICKET_IMPORT_DELETE = 192003;
    /**
     * 故障单号已存在
     */
    public static final Integer TICKET_CODE_HAVE = 192004;
}
