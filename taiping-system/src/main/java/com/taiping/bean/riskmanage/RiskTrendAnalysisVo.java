package com.taiping.bean.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/31
 * 风险趋势分析查询条件
 */
public class RiskTrendAnalysisVo {
    /**
     * 风险类型
     */
    private Integer riskType;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
