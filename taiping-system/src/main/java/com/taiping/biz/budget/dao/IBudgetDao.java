package com.taiping.biz.budget.dao;

import com.taiping.biz.budget.dto.*;
import com.taiping.entity.budget.BudgetAnalysisCurve;
import com.taiping.entity.budget.BudgetAnalysisReport;
import com.taiping.entity.budget.BudgetStatistics;
import com.taiping.entity.budget.TBudget;
import com.taiping.entity.maintenanceplan.AssetContractInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBudgetDao {
    /**
     * 新增预算
     *
     * @param entity
     * @return
     */
    void add(TBudget entity);

    /**
     * 更新预算
     *
     * @param entity
     */
    void updateBudget(TBudget entity);

    /**
     * 根据ID获取对象数据
     *
     * @param code
     */
    BudgetDto selectByCode(@Param("code") String code);

    /**
     * 根据年份获取预算
     *
     * @return
     */
    List<BudgetDto> findByYear(@Param("year") String year);

    /**
     * 根据年份获取预算
     *
     * @return
     */
    List<BudgetTableRowDto> findByYearTable(BudgetFindDto dto);

    /**
     * 获取预算年份列表
     *
     * @return
     */
    List<String> getYearList();

    /**
     * 获取年度汇总
     *
     * @param year
     * @return
     */
    BudgetTotalDto getBudgetTotal(@Param("year") String year);

    /**
     * 获取单项分析
     *
     * @param year
     * @return
     */
    List<BudgetAnalysisDto> getBudgetAnalysis(@Param("year") String year);

    /**
     * 保存预算分析报告数据
     *
     * @param data 预算分析报告数据
     * @return 添加条数
     */
    int saveBudgetAnalysisReportData(List<BudgetAnalysisReport> data);

    /**
     * 根据月份获取预算分析数据
     *
     * @param monthTime 指定月份开始时间
     * @return 预算分析数据
     */
    List<BudgetAnalysisReport> getBudgetAnalysisDataByMonth(Long monthTime);

    /**
     * 批量删除预算分析数据
     *
     * @param oldReportData 预算分析数据
     * @return 删除条数
     */
    int batchDeleteAnalysisData(List<BudgetAnalysisReport> oldReportData);

    /**
     * 保存预算统计数据
     *
     * @param data 预算统计数据
     * @return 添加条数
     */
    int batchInsertBudgetStatisticsData(List<BudgetStatistics> data);

    /**
     * 批量删除预算统计数据
     *
     * @param oldStatisticsData 预算统计数据
     * @return 删除条数
     */
    int batchDeleteBudgetStatisticsData(List<BudgetStatistics> oldStatisticsData);

    /**
     * 根据月份获取预算统计数据
     *
     * @param monthTime 指定月份开始时间
     * @return 预算统计数据
     */
    List<BudgetStatistics> getBudgetStatisticsDataByMonth(Long monthTime);

    /**
     * 获取所有预算统计数据
     *
     * @return 预算统计数据
     */
    List<BudgetStatistics> getAllStatisticsData();
    /**
     * 保存预算分析曲线数据
     *
     * @param data 预算分析曲线数据
     * @return 添加条数
     */
    int batchInsertBudgetCurveData(List<BudgetAnalysisCurve> data);

    /**
     * 批量删除预算分析曲线数据
     *
     * @param oldReportData 预算分析曲线数据
     * @return 删除条数
     */
    int batchDeleteBudgetCurveData(List<BudgetAnalysisCurve> oldReportData);

    /**
     * 根据月份获取预算分析曲线数据
     *
     * @param monthTime 指定月份开始时间
     * @return 预算分析曲线数据
     */
    List<BudgetAnalysisCurve> getBudgetCurveDataByMonth(Long monthTime);


}
