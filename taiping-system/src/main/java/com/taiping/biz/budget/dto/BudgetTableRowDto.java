package com.taiping.biz.budget.dto;

import com.taiping.entity.budget.TBudget;

import java.util.List;

public class BudgetTableRowDto extends TBudget {
    private List<BudgetTableRowItemDto> values;

    public List<BudgetTableRowItemDto> getValues() {
        return values;
    }

    public void setValues(List<BudgetTableRowItemDto> values) {
        this.values = values;
    }
}
