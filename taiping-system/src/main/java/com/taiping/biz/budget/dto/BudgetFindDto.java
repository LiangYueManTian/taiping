package com.taiping.biz.budget.dto;

import org.springframework.web.bind.annotation.PathVariable;

public class BudgetFindDto {
    private String year;
    private String costType;
    private String costClass;
    private String costCenter;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getCostClass() {
        return costClass;
    }

    public void setCostClass(String costClass) {
        this.costClass = costClass;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
}
