package com.taiping.bean.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/25
 * 风险追踪负责人指派前后端交互实体
 */
public class RiskTrackUserAssignVo {
    /**
     * 风险项id
     */
    private String riskItemId;

    /**
     * 风险追踪负责人
     */
    private String trackUser;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public String getTrackUser() {
        return trackUser;
    }

    public void setTrackUser(String trackUser) {
        this.trackUser = trackUser;
    }
}
