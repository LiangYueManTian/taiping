package com.taiping.entity.budget;


public class TBudgetPurchaseItem {

  private String tid;
  private String purchaseId;
  private String templateId;
  private java.sql.Date planStartDate;
  private java.sql.Date planEndDate;
  private java.sql.Date actStartDate;
  private java.sql.Date actEndDate;
  private String status;
  private String remark;
  private String createUser;
  private java.sql.Timestamp createDate;
  private String updateUser;
  private java.sql.Timestamp updateDate;


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


  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }


  public java.sql.Date getPlanStartDate() {
    return planStartDate;
  }

  public void setPlanStartDate(java.sql.Date planStartDate) {
    this.planStartDate = planStartDate;
  }


  public java.sql.Date getPlanEndDate() {
    return planEndDate;
  }

  public void setPlanEndDate(java.sql.Date planEndDate) {
    this.planEndDate = planEndDate;
  }


  public java.sql.Date getActStartDate() {
    return actStartDate;
  }

  public void setActStartDate(java.sql.Date actStartDate) {
    this.actStartDate = actStartDate;
  }


  public java.sql.Date getActEndDate() {
    return actEndDate;
  }

  public void setActEndDate(java.sql.Date actEndDate) {
    this.actEndDate = actEndDate;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public java.sql.Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }


  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public java.sql.Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(java.sql.Timestamp updateDate) {
    this.updateDate = updateDate;
  }

}
