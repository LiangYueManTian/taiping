package com.taiping.biz.budget.dto;

public class BudgetTotalDto {
    private Double budgetTotal;
    private Double purchaseTotal;
    private Double ratio;

    public Double getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public Double getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(double purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
