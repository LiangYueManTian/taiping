package com.taiping.constant.maintenance;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维护保养计划模块返回码定义
 */
public class MaintenancePlanResultCode {
    /**
     * 数据库操作失败
     */
    public static final Integer DATABASE_OPERATION_FAIL = 210000;
    /**
     *维保计划不存在
     */
    public static final Integer MAINTENANCE_PLAN_NOT_EXISTED = 210001;
    /**
     *资产基本信息不存在
     */
    public static final Integer ASSEST_BASIC_INFO_NOT_EXISTED = 210002;
    /**
     *资产维保合同不存在
     */
    public static final Integer MAINTENANCE_CONTRACT_NOT_EXISTED = 210003;
    /**
     *维保计划已存在，添加失败
     */
    public static final Integer MAINTENANCE_PLAN_HAVE_EXISTED = 210004;
    /**
     *资产基本信息已存在，添加失败
     */
    public static final Integer ASSEST_BASIC_INFO_HAVE_EXISTED = 210005;
    /**
     *资产维保合同已存在，添加失败
     */
    public static final Integer MAINTENANCE_CONTRACT_HAVE_EXISTED = 210006;
    /**
     * 维保计划执行情况不存在
     */
    public static final Integer EXECUTE_SITUATION_NOT_EXISTED = 210007;
    /**
     * 计划执行时间不存在，请重新填写
     */
    public static final Integer PLAN_EXECUTE_TIME_NOT_EXISTED = 210008;
    /**
     * 当前时间晚于该执行周期，无法修改
     */
    public static final Integer OUT_OF_EXECUTE_PERIOD = 210008;
}
