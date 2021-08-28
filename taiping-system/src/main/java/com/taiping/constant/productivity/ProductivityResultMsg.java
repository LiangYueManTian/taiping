package com.taiping.constant.productivity;
/**
 * 生产力分析服务返回信息
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-1
 */
public class ProductivityResultMsg {
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
    public static final String ROW_TYPE_ERROR = "第${row}行数据班次名称(${name})错误";
    /**
     * 行数据错误
     */
    public static final String ROW_CELL_ERROR = "第${row}行数据${cell}(${name})格式错误";
    /**
     * 文件格式错误
     */
    public static final String FILE_TYPE_ERROR = "文件格式错误";
    /**
     * 文件无数据
     */
    public static final String FILE_EMPTY = "文件无可用数据";
    /**
     * 行数据错误
     */
    public static final String ROW_DATE_ERROR = "第${row}行数据排班日期(${name})格式错误";
    /**
     * 不存在该未来变更计划
     */
    public static final String CHANGE_PLAN_DELETED = "未来变更计划已删除";
    /**
     * 不存在该未来变更计划
     */
    public static final String CHANGE_PLAN_EXIST = "不存在该未来变更计划";
    /**
     * 未来变更计划项目名称重复
     */
    public static final String CHANGE_PLAN_NAME = "项目名称已存在";
    /**
     * 班表数据重复重复
     */
    public static final String SCHEDULE_IMPORT = "${num}${name}表数据已导入";
}
