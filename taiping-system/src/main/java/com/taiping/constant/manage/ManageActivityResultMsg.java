package com.taiping.constant.manage;
/**
 * 运维管理活动服务返回信息
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public class ManageActivityResultMsg {
    /**
     * 请求参数错误
     */
    public static final String PARAM_ERROR = "请求参数错误";
    /**
     * 新增运维管理活动失败
     */
    public static final String ADD_MANAGE_ACTIVITY_ERROR = "新增运维管理活动失败";
    /**
     * 运维管理活动已取消
     */
    public static final String MANAGE_ACTIVITY_CANCEL = "运维管理活动已经被取消";
    /**
     * 运维管理活动已复核通过
     */
    public static final String MANAGE_ACTIVITY_APPROVAL = "已复核通过，不能被修改";
    /**
     * 不存在该运维管理活动
     */
    public static final String MANAGE_ACTIVITY_IS_DELETED = "不存在该运维管理活动";
    /**
     * 运维管理活动不能被取消
     */
    public static final String MANAGE_ACTIVITY_NOT_CANCEL = "该运维管理活动不能被取消";
    /**
     * 配置参数丢失，请联系维护人员
     */
    public static final String PARAM_LOSE = "配置参数丢失，请联系维护人员";
    /**
     * 请导入新的月份数据
     */
    public static final String  IMPORT_NEW_DATA = "暂无可分析数据";
    /**
     * 数据已分析
     */
    public static final String  DATA_ALREADY_ANALYZE = "数据已分析";
    /**
     * 已提交审批，无法更改分析报告
     */
    public static final String ALREADY_SUBMIT_CHANGE = "已提交审批，无法更改分析报告";
    /**
     * 已审批通过，无法更改分析报告
     */
    public static final String ALREADY_APPROVAL_CHANGE = "已审批通过，无法更改分析报告";
    /**
     * 请导入数据并分析
     */
    public static final String  IMPORT_DATA_ANALYZE = "请导入数据并分析";
    /**
     * 已提交审批
     */
    public static final String  ALREADY_SUBMIT = "已提交审批";
    /**
     * 报告已审批
     */
    public static final String  ALREADY_APPROVAL = "当前报告已审批";
    /**
     * 请先预览生成分析报告
     */
    public static final String  TO_PREVIEW = "请先预览生成分析报告";
    /**
     * 请先提交审批
     */
    public static final String  TO_SUBMIT = "请先提交审批";
}
