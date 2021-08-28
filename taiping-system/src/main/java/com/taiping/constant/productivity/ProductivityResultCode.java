package com.taiping.constant.productivity;
/**
 * 生产力分析服务返回码
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-1
 */
public class ProductivityResultCode {
    /**
     * 传入参数错误
     */
    public static final Integer PARAM_ERROR = 190001;
    /**
     * Excel文件sheet页数量错误
     */
    public static final Integer SHEET_ERROR = 190002;
    /**
     * 行数据错误
     */
    public static final Integer ROW_TYPE_ERROR = 190003;
    /**
     * 文件格式错误
     */
    public static final Integer FILE_TYPE_ERROR = 190004;
    /**
     * 文件无数据
     */
    public static final Integer FILE_EMPTY = 190005;
    /**
     * 行数据错误
     */
    public static final Integer ROW_DATE_ERROR = 190006;
    /**
     * 行数据错误
     */
    public static final Integer ROW_CELL_ERROR = 190007;
    /**
     * 未来变更计划已删除
     */
    public static final Integer CHANGE_PLAN_DELETED = 190101;
    /**
     * 不存在该未来变更计划
     */
    public static final Integer CHANGE_PLAN_EXIST = 190102;
    /**
     * 未来变更计划项目名称重复
     */
    public static final Integer CHANGE_PLAN_NAME = 190103;
    /**
     *班表数据重复重复
     */
    public static final Integer SCHEDULE_IMPORT = 190104;

}
