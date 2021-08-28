package com.taiping.biz.budget.dto;

import com.taiping.entity.budget.TBudgetPurchaseTl;

import java.util.List;

public class BudgetPurchaseTableDto {
    private List<TBudgetPurchaseTl> title;
    private List<BudgetPurchaseTableColDto> columns;

    public List<TBudgetPurchaseTl> getTitle() {
        return title;
    }

    public void setTitle(List<TBudgetPurchaseTl> title) {
        this.title = title;
    }

    public List<BudgetPurchaseTableColDto> getColumns() {
        return columns;
    }

    public void setColumns(List<BudgetPurchaseTableColDto> columns) {
        this.columns = columns;
    }
}
