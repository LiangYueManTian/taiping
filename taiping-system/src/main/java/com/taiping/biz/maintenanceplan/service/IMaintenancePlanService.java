package com.taiping.biz.maintenanceplan.service;

import com.taiping.bean.maintenance.MaintenancePlanReportDto;
import com.taiping.bean.maintenance.TableHeader;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.maintenanceplan.MaintenancePlanAnalysisReport;
import com.taiping.entity.maintenanceplan.PlanExecuteSituation;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维护保养计划逻辑层接口
 */
public interface IMaintenancePlanService {
    /**
     * 新增维护保养计划
     *
     * @param maintenancePlan 需新增的维护保养计划
     * @return 新增结果
     */
    Result addMaintenancePlan(MaintenancePlan maintenancePlan);

    /**
     * 修改维护保养计划
     *
     * @param maintenancePlan 需修改的维护保养计划
     * @return 修改结果
     */
    Result modifyMaintenancePlan(MaintenancePlan maintenancePlan);

    /**
     * 批量删除维护保养计划
     *
     * @param maintenancePlanIds 维护保养计划id列表
     * @return 删除结果
     */
    Result batchDeleteMaintenancePlan(List<String> maintenancePlanIds);

    /**
     * 获取所有维护保养计划
     *
     * @return 维护保养计划列表
     */
    List<MaintenancePlan> getAllPlan();

    /**
     * 分页查询维保计划列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean getMaintenancePlanByCondition(QueryCondition<MaintenancePlan> queryCondition);

    /**
     * 查询维保计划表格数据(无分页条件)
     *
     * @param queryCondition 查询条件
     * @return 查询结果
     */
    List<MaintenancePlan> getMaintenancePlanTableData(QueryCondition<MaintenancePlan> queryCondition);

    /**
     * 根据id查询维保计划
     *
     * @param planId 维保计划id
     * @return 维保计划信息
     */
    MaintenancePlan getMaintenancePlanById(String planId);

    /**
     * 根据相关运维活动id查询维保计划
     *
     * @param activityId 相关运维活动id
     * @return 维保计划信息
     */
    MaintenancePlan getMaintenancePlanByActivityId(String activityId);

    /**
     *获取指定年份表头列表
     *
     * @param year 指定年份
     * @return 表头信息
     */
    List<TableHeader> getTableHeaderByYear(Integer year);

    /**
     * 暂停/启用维护保养计划
     *
     * @param plan 需暂停/启用的维护保养计划
     */
    Result planPauseOrEnable(MaintenancePlan plan);

    /**
     * 修改计划执行情况
     *
     * @param situation 需修改的执行情况
     * @return 修改结果
     */
    Result modifyPlanExecuteSituation(PlanExecuteSituation situation);

    /**
     * 更新延期情况
     */
    void updateDelaySituation();

    /**
     * 维保预提醒
     */
    void maintenancePreReminder();

    /**
     * 验证维保计划执行情况是否可修改
     *
     * @param executeTime 计划执行时间
     * @param period 计划执行周期
     * @return 验证结果
     */
    Result checkEnableModify(Long executeTime,Integer period);

    /**
     * 根据运维管理活动生成维护保养计划
     *
     * @param activityId 运维管理活动id
     * @param planName 维护保养计划名称
     */
    void createMaintenancePlanByActivityId(String activityId,String planName);

    /**
     * 根据用户id获取待处理维保计划总数
     *
     * @param userId 用户id
     * @return Integer 待处理维保计划总数
     */
    Integer queryCountByUserId(String userId);

    /**
     * 维护保养计划分析
     *
     * @return 分析结果
     */
    Result maintenancePlanAnalysis();

    /**
     * 保存维护保养计划分析数据
     *
     * @return 保存结果
     */
    Result saveMaintenancePlanAnalysisReportData();

    /**
     * 根据月份获取维护保养计划分析报告数据
     *
     * @return 维护保养计划分析报告数据
     */
    MaintenancePlanReportDto geMaintenancePlanReportData(Long monthTime);
}
