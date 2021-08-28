package com.taiping.biz.budget.dto;


import com.taiping.entity.budget.TBudgetPurchase;

import java.sql.Date;


public class BudgetPurchaseDto extends TBudgetPurchase {
    private Date planStartDate;

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }
}
