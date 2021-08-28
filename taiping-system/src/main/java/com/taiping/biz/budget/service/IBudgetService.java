package com.taiping.biz.budget.service;

import com.taiping.biz.budget.dto.*;
import com.taiping.entity.Result;
import com.taiping.entity.budget.BudgetAnalysisReport;
import com.taiping.entity.budget.TBudget;

import java.util.List;

public interface IBudgetService {
    /**
     * 新增预算
     *
     * @param dto
     * @return
     */
    Result add(BudgetDto dto);

    /**
     * 更新预算
     *
     * @param entity
     */
    Result updateBudget(TBudget entity);

    /**
     * 根据年份获取预算
     *
     * @return
     */
    Result<List<BudgetDto>> findByYear(String year);

    /**
     * 按年份获取预算方式表格数据
     *
     * @param dto
     * @return
     */
    Result<BudgetTableDto> getBudgetTable(BudgetFindDto dto);

    /**
     * 获取预算年份列表
     *
     * @return
     */
    Result<List<String>> getYearList();

    /**
     * 获取年度汇总
     *
     * @return
     */
    Result<BudgetTotalDto> getBudgetTotal();

    /**
     * 预算采购分析
     *
     * @return
     */
    Result budgetPurchaseAnalysis();

    /**
     * 保存预算采购分析数据
     *
     * @return 保存结果
     */
    Result saveBudgetPurchaseAnalysisReportData();

    /**
     * 根据月份获取预算采购分析报告数据
     *
     * @return 预算采购分析报告数据
     */
    BudgetPurchaseReportDto getBudgetPurchaseReportData(Long monthTime);
}
