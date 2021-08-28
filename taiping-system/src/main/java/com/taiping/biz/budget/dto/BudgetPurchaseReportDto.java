package com.taiping.biz.budget.dto;

import com.taiping.entity.budget.BudgetAnalysisCurve;
import com.taiping.entity.budget.BudgetAnalysisReport;
import com.taiping.entity.budget.PurchaseAnalysisReport;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/12/12
 * 预算与采购分析报告dto
 */
public class BudgetPurchaseReportDto {
    /**
     * 预算分析数据
     */
    private List<BudgetAnalysisReport> budgetAnalysisData;
    /**
     * 预算分析曲线数据
     */
    private List<BudgetAnalysisCurve> curveData;
    /**
     * 采购分析数据
     */
    private List<PurchaseAnalysisReport> purchaseAnalysisData;

    public List<BudgetAnalysisReport> getBudgetAnalysisData() {
        return budgetAnalysisData;
    }

    public void setBudgetAnalysisData(List<BudgetAnalysisReport> budgetAnalysisData) {
        this.budgetAnalysisData = budgetAnalysisData;
    }

    public List<BudgetAnalysisCurve> getCurveData() {
        return curveData;
    }

    public void setCurveData(List<BudgetAnalysisCurve> curveData) {
        this.curveData = curveData;
    }

    public List<PurchaseAnalysisReport> getPurchaseAnalysisData() {
        return purchaseAnalysisData;
    }

    public void setPurchaseAnalysisData(List<PurchaseAnalysisReport> purchaseAnalysisData) {
        this.purchaseAnalysisData = purchaseAnalysisData;
    }
}
