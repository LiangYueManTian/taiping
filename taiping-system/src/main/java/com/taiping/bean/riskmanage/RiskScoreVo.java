package com.taiping.bean.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/25
 * 风险分值登记前后端交互实体
 */
public class RiskScoreVo {
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 连续性影响分值
     */
    private Double serialEffectScore;
    /**
     * 高可用影响分值
     */
    private Double highUseEffectScore;
    /**
     * 系统级别分值
     */
    private Double systemLevelScore;
    /**
     * 风险发生概率分值
     */
    private Double riskHappenProbScore;
    /**
     * 风险分值
     */
    private Double riskScore;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public Double getSerialEffectScore() {
        return serialEffectScore;
    }

    public void setSerialEffectScore(Double serialEffectScore) {
        this.serialEffectScore = serialEffectScore;
    }

    public Double getHighUseEffectScore() {
        return highUseEffectScore;
    }

    public void setHighUseEffectScore(Double highUseEffectScore) {
        this.highUseEffectScore = highUseEffectScore;
    }

    public Double getSystemLevelScore() {
        return systemLevelScore;
    }

    public void setSystemLevelScore(Double systemLevelScore) {
        this.systemLevelScore = systemLevelScore;
    }

    public Double getRiskHappenProbScore() {
        return riskHappenProbScore;
    }

    public void setRiskHappenProbScore(Double riskHappenProbScore) {
        this.riskHappenProbScore = riskHappenProbScore;
    }

    public Double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }
}
