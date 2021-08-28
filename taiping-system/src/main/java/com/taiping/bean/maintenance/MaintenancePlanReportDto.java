package com.taiping.bean.maintenance;

import com.taiping.entity.maintenanceplan.MaintenancePlanAnalysisReport;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/12/16
 * 维护保养计划分析报告dto
 */
public class MaintenancePlanReportDto {
    /**
     * 本月分析数据
     */
    private List<MaintenancePlanAnalysisReport> currentMonthData;
    /**
     *曲线数据
     */
    private List<MaintenancePlanAnalysisReport> curveData;

    public List<MaintenancePlanAnalysisReport> getCurrentMonthData() {
        return currentMonthData;
    }

    public void setCurrentMonthData(List<MaintenancePlanAnalysisReport> currentMonthData) {
        this.currentMonthData = currentMonthData;
    }

    public List<MaintenancePlanAnalysisReport> getCurveData() {
        return curveData;
    }

    public void setCurveData(List<MaintenancePlanAnalysisReport> curveData) {
        this.curveData = curveData;
    }
}
