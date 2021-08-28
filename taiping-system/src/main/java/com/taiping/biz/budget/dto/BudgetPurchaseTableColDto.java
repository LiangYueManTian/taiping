package com.taiping.biz.budget.dto;

import com.taiping.entity.budget.TBudgetPurchase;
import com.taiping.entity.budget.TBudgetPurchaseItem;

import java.util.List;

public class BudgetPurchaseTableColDto extends TBudgetPurchase {
    private double paymentRatio;
    private double paymentAmount;
    private String paymentYear;

    private String code;
    private String name;
    private String type;
    private String costCenter;
    private double amount;
    private String classify;
    private String kind;
    private String budgetYear;

    private List<TBudgetPurchaseItem> template;

    public double getPaymentRatio() {
        return paymentRatio;
    }

    public void setPaymentRatio(double paymentRatio) {
        this.paymentRatio = paymentRatio;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentYear() {
        return paymentYear;
    }

    public void setPaymentYear(String paymentYear) {
        this.paymentYear = paymentYear;
    }

    public List<TBudgetPurchaseItem> getTemplate() {
        return template;
    }

    public void setTemplate(List<TBudgetPurchaseItem> template) {
        this.template = template;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }
}
