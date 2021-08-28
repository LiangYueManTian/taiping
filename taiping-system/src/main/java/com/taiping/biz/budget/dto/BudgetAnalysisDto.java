package com.taiping.biz.budget.dto;

import com.taiping.entity.budget.TBudget;

public class BudgetAnalysisDto extends TBudget {
    private double execTotal;

    public double getExecTotal() {
        return execTotal;
    }

    public void setExecTotal(double execTotal) {
        this.execTotal = execTotal;
    }
}
