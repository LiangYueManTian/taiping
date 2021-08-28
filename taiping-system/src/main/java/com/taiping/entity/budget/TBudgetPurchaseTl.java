package com.taiping.entity.budget;


public class TBudgetPurchaseTl {

  private String tid;
  private String name;
  private int planNeedTime;
  private long isDelete;
  private long isOrder;
  private long checkFlag;


  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public long getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(long isDelete) {
    this.isDelete = isDelete;
  }


  public long getIsOrder() {
    return isOrder;
  }

  public void setIsOrder(long isOrder) {
    this.isOrder = isOrder;
  }


  public long getCheckFlag() {
    return checkFlag;
  }

  public void setCheckFlag(long checkFlag) {
    this.checkFlag = checkFlag;
  }

  public int getPlanNeedTime() {
    return planNeedTime;
  }

  public void setPlanNeedTime(int planNeedTime) {
    this.planNeedTime = planNeedTime;
  }
}
