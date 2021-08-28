package com.taiping.entity.budget;


public class TBudgetPurchasePlan {

  private String tid;
  private String purchaseId;
  private double paymentRatio;
  private double paymentAmount;
  private String paymentYear;


  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
  }


  public String getPurchaseId() {
    return purchaseId;
  }

  public void setPurchaseId(String purchaseId) {
    this.purchaseId = purchaseId;
  }


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

}
