package com.taiping.biz.budget.dto;

import java.util.List;

public class BudgetTableDto {
    private List<BudgetPurchaseDto> titles;
    private List<BudgetTableRowDto> rows;

    public List<BudgetPurchaseDto> getTitles() {
        return titles;
    }

    public void setTitles(List<BudgetPurchaseDto> titles) {
        this.titles = titles;
    }

    public List<BudgetTableRowDto> getRows() {
        return rows;
    }

    public void setRows(List<BudgetTableRowDto> rows) {
        this.rows = rows;
    }
}
