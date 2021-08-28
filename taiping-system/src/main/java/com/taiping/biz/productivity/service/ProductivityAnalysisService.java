package com.taiping.biz.productivity.service;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.productivity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 生产力分析服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface ProductivityAnalysisService {
    /**
     * 导入常白班排班和倒班排班Excel表
     * @param file Excel表
     * @return Result
     */
    Result importSchedule(MultipartFile file);
    /**
     * 导入巡检工单数据Excel表
     * @param file Excel表
     * @return Result
     */
    Result importInspectionTask(MultipartFile file);
    /**
     * 导入变更单数据Excel表
     * @param file Excel表
     * @return Result
     */
    Result importChangeOrder(MultipartFile file);

    /**
     * 查询班表列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectScheduleList(QueryCondition<Schedule> queryCondition);

    /**
     * 查询巡检列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectInspectionTaskList(QueryCondition<InspectionTask> queryCondition);
    /**
     * 查询变更单列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectChangeList(QueryCondition<Change> queryCondition);

    /**
     * 查询执行人
     * @return Result
     */
    Result selectExecutorList();
    /**
     * 新增未来变更计划
     * @param changePlan 未来变更计划
     * @return Result
     */
    Result insertChangePlan(ChangePlan changePlan);
    /**
     * 修改未来变更计划
     * @param changePlan 未来变更计划
     * @return Result
     */
    Result updateChangePlan(ChangePlan changePlan);

    /**
     * 删除未来变更计划
     * @param changeIdList 未来变更计划ID
     * @return Result
     */
    Result deleteChangePlan(List<String> changeIdList);

    /**
     * 根据名称查询未来变更计划
     * @param changePlan 未来变更计划
     * @return  List<String> 未来变更计划ID
     */
    Result selectChangePlanForName(ChangePlan changePlan);

    /**
     * 根据ID查询未来变更计划
     * @param changeId ID
     * @return Result
     */
    Result selectChangePlanById(String changeId);
    /**
     * 查询列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectChangePlanList(QueryCondition<ChangePlan> queryCondition);
    /**
     * 查询个人KPI列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectPerformanceList(QueryCondition<Performance> queryCondition);
    /**
     * 查询人姓名
     * @return Result
     */
    Result selectPersonList();
    /**
     * 查询团队负荷列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectDailyWorkloadList(QueryCondition<DailyWorkload> queryCondition);
    /**
     * 分析数据
     * @return Result
     */
    Result analysisData();
    /**
     * 生成分析报告
     * @return Result
     */
    Result createReport();
    /**
     * 查询分析报告
     * @param monthWorkload 查询时间
     * @return Result
     */
    Result queryReport(MonthWorkload monthWorkload);

    void test();

}
