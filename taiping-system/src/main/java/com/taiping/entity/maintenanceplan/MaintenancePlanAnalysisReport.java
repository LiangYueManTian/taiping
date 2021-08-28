package com.taiping.entity.maintenanceplan;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/12/13
 * 维护保养计划分析报告实体
 */
@Data
public class MaintenancePlanAnalysisReport extends MaintenancePlanStatistics{
    /**
     *报告生成时间(精确到月)
     */
    private Long reportTime;
}
