package com.taiping.bean.riskmanage;


/**
 * @author zhangliangyu
 * @since 2019/10/25
 * 风险应对方案前后端交互实体
 */
public class RiskResponsePlanVo {
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 应对方案
     */
    private String responsePlan;

    /**
     * 参考附件名
     */
    private String referAnnexName;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public String getResponsePlan() {
        return responsePlan;
    }

    public void setResponsePlan(String responsePlan) {
        this.responsePlan = responsePlan;
    }

    public String getReferAnnexName() {
        return referAnnexName;
    }

    public void setReferAnnexName(String referAnnexName) {
        this.referAnnexName = referAnnexName;
    }
}
