package com.taiping.constant.manage;
/**
 * 运维管理活动服务返回码
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public class ManageActivityResultCode {
    /**
     * 传入参数错误
     */
    public static final Integer PARAM_ERROR = 160001;
    /**
     * 新增运维管理活动失败
     */
    public static final Integer ADD_MANAGE_ACTIVITY_ERROR = 160101;
    /**
     * 运维管理活动已取消
     */
    public static final Integer MANAGE_ACTIVITY_CANCEL = 160102;
    /**
     * 运维管理活动已复核通过
     */
    public static final Integer MANAGE_ACTIVITY_APPROVAL = 160103;
    /**
     * 不存在该运维管理活动
     */
    public static final Integer MANAGE_ACTIVITY_IS_DELETED = 160104;
    /**
     * 运维管理活动不能被取消
     */
    public static final Integer MANAGE_ACTIVITY_NOT_CANCEL = 160105;
    /**
     * 配置参数丢失，请联系维护人员
     */
    public static final Integer PARAM_LOSE = 160201;
    /**
     * 请导入新的月份数据
     */
    public static final Integer  IMPORT_NEW_DATA = 160202;
    /**
     * 数据已分析
     */
    public static final Integer  DATA_ALREADY_ANALYZE = 160203;
    /**
     * 已提交审批，无法更改分析报告
     */
    public static final Integer ALREADY_SUBMIT_CHANGE = 160204;
    /**
     * 已审批通过，无法更改分析报告
     */
    public static final Integer ALREADY_APPROVAL_CHANGE = 160205;
    /**
     * 请导入数据并分析
     */
    public static final Integer  IMPORT_DATA_ANALYZE = 160206;
    /**
     * 已提交审批
     */
    public static final Integer  ALREADY_SUBMIT = 160207;
    /**
     * 报告已审批
     */
    public static final Integer  ALREADY_APPROVAL = 160208;
    /**
     * 请先预览生成分析报告
     */
    public static final Integer  TO_PREVIEW = 160209;
    /**
     * 请先提交审批
     */
    public static final Integer  TO_SUBMIT = 160210;
}
