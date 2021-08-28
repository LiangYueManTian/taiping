package com.taiping.biz.maintenanceplan.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.maintenanceplan.MaintenancePlanStatistics;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/12/16
 * 维护保养计划统计持久层
 */
public interface MaintenancePlanStatisticsDao extends BaseMapper<MaintenancePlanStatistics> {
    /**
     * 批量插入维护保养计划统计数据
     *
     * @param statisticsData 需添加的维护保养计划分析报告数据
     * @return 添加条数
     */
    int batchInsertData(List<MaintenancePlanStatistics> statisticsData);

    /**
     * 批量删除维护保养计划统计数据
     *
     * @param statisticsData 维护保养计划统计数据
     * @return 删除条数
     */
    int batchDeleteData(List<MaintenancePlanStatistics> statisticsData);

    /**
     * 根据月份获取维护保养计划统计数据
     *
     * @param monthTime 指定月份开始时间
     * @return 维护保养计划统计数据
     */
    List<MaintenancePlanStatistics> getMaintenancePlanStatisticsDataByMonth(Long monthTime);
}
