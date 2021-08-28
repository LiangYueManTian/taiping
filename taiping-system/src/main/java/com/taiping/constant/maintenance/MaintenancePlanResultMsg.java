package com.taiping.constant.maintenance;

/**
 * @author zhangliangyu
 * @since 2019/11/26
 * 维护保养计划模块返回Msg定义
 */
public class MaintenancePlanResultMsg {
    /**
     * 数据库操作失败
     */
    public static final String DATABASE_OPERATION_FAIL = "数据库操作失败";
    /**
     *维保计划不存在
     */
    public static final String MAINTENANCE_PLAN_NOT_EXISTED = "维保计划不存在";
    /**
     *资产基本信息不存在
     */
    public static final String ASSEST_BASIC_INFO_NOT_EXISTED = "资产基本信息不存在";
    /**
     *资产维保合同不存在
     */
    public static final String MAINTENANCE_CONTRACT_NOT_EXISTED = "资产维保合同不存在";
    /**
     *维保计划已存在，添加失败
     */
    public static final String MAINTENANCE_PLAN_HAVE_EXISTED = "维保计划已存在，添加失败";
    /**
     *资产基本信息已存在，添加失败
     */
    public static final String ASSEST_BASIC_INFO_HAVE_EXISTED = "资产基本信息已存在，添加失败";
    /**
     *资产维保合同已存在，添加失败
     */
    public static final String MAINTENANCE_CONTRACT_HAVE_EXISTED = "资产维保合同已存在，添加失败";
    /**
     * 维保计划执行情况不存在
     */
    public static final String EXECUTE_SITUATION_NOT_EXISTED = "维保计划执行情况不存在";
    /**
     * 计划执行时间不存在，请重新填写
     */
    public static final String PLAN_EXECUTE_TIME_NOT_EXISTED = "计划执行时间不存在，请重新填写";
    /**
     * 当前时间晚于该执行周期，无法修改
     */
    public static final String OUT_OF_EXECUTE_PERIOD = "当前时间晚于该执行周期，无法修改";
}
