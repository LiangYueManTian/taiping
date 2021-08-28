package com.taiping.bean.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/25
 * 风险项复检前后端交互实体
 */
public class RiskItemRecheckVo {
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 复检人
     */
    private String checkUser;
    /**
     * 复检时间
     */
    private Long checkTime;
    /**
     * 复检结果
     */
    private Integer checkResult;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }
}
