package com.taiping.biz.maintenanceplan.dao;

import com.taiping.entity.maintenanceplan.MaintenancePlanAnalysisReport;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/12/13
 * 维护保养计划分析报告持久层
 */
public interface MaintenancePlanAnalysisReportDao {
    /**
     * 批量插入维护保养计划分析报告数据
     *
     * @param reportData 需添加的维护保养计划分析报告数据
     * @return 添加条数
     */
    int batchInsertData(List<MaintenancePlanAnalysisReport> reportData);

    /**
     * 批量删除维护保养计划分析报告数据
     *
     * @param reportData 维护保养计划分析报告数据
     * @return 删除条数
     */
    int batchDeleteData(List<MaintenancePlanAnalysisReport> reportData);

    /**
     * 根据月份获取维护保养计划分析数据
     *
     * @param monthTime 指定月份开始时间
     * @return 维护保养计划分析数据
     */
    List<MaintenancePlanAnalysisReport> getMaintenancePlanAnalysisDataByMonth(Long monthTime);
}
