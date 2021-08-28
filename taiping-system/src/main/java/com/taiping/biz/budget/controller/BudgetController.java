package com.taiping.biz.budget.controller;

import com.taiping.biz.budget.dto.BudgetDto;
import com.taiping.biz.budget.dto.BudgetFindDto;
import com.taiping.biz.budget.dto.BudgetTableDto;
import com.taiping.biz.budget.dto.BudgetTotalDto;
import com.taiping.biz.budget.service.IBudgetService;
import com.taiping.entity.Result;
import com.taiping.entity.budget.TBudget;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预算计划
 */
@RestController
@RequestMapping("/taiping/budget")
public class BudgetController {
    @Autowired
    private IBudgetService budgetServiceImpl;

    /**
     * 新增预算
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody BudgetDto dto) {
        return budgetServiceImpl.add(dto);
    }

    /**
     * 更新预算
     *
     * @param entity
     * @return
     */
    @PostMapping("/updateBudget")
    public Result updateBudget(@RequestBody TBudget entity) {
        return budgetServiceImpl.updateBudget(entity);
    }

    /**
     * 根据年份获取预算
     *
     * @return
     */
    @GetMapping("/findByYear/{year}")
    public Result<List<BudgetDto>> findByYear(@PathVariable String year) {
        return budgetServiceImpl.findByYear(year);
    }

    /**
     * 根据年份获取预算方式表格
     *
     * @param dto
     * @return
     */
    @PostMapping("/getBudgetTable")
    public Result<BudgetTableDto> getBudgetTable(@RequestBody BudgetFindDto dto) {
        return budgetServiceImpl.getBudgetTable(dto);
    }

    /**
     * 获取预算年份列表
     *
     * @return
     */
    @GetMapping("/getYearList")
    public Result<List<String>> getYearList() {
        return budgetServiceImpl.getYearList();
    }

    /**
     * 获取汇总统计
     *
     * @return
     */
    @GetMapping("/getBudgetTotal")
    public Result<BudgetTotalDto> getBudgetTotal() {
        return budgetServiceImpl.getBudgetTotal();
    }

    /**
     * 预算采购分析
     *
     * @return
     */
    @GetMapping("/budgetAnalysis")
    public Result budgetAnalysis() {
        return budgetServiceImpl.budgetPurchaseAnalysis();
    }

    /**
     * 保存预算与采购分析数据
     *
     * @return 保存结果
     */
    @GetMapping("/saveBudgetPurchaseReportData")
    public Result saveBudgetPurchaseReportData() {
        budgetServiceImpl.saveBudgetPurchaseAnalysisReportData();
        return ResultUtils.success();
    }

    /**
     * 根据月份获取预算采购分析报告数据
     *
     * @param monthTime 指定月时间
     * @return 预算采购分析报告数据
     */
    @GetMapping("/queryBudgetPurchaseReportData/{monthTime}")
    public Result queryBudgetPurchaseReportData(@PathVariable Long monthTime) {
        return ResultUtils.success(budgetServiceImpl.getBudgetPurchaseReportData(monthTime));
    }
}
