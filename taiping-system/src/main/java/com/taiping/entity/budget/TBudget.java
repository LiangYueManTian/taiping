package com.taiping.entity.budget;


public class TBudget {

  private String tid;
  private String code;
  private String name;
  private String type;
  private String costCenter;
  private double amount;
  private String classify;
  private String kind;
  private String budgetYear;
  private String costType;
  private String costClass;
  private String createUser;
  private String updateUser;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;


  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
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

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }

}
