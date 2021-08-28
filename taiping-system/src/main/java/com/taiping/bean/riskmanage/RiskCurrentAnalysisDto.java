package com.taiping.bean.riskmanage;

import com.taiping.entity.riskmanage.RiskCurrentAnalysis;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/30
 * 风险现状分析dto
 */
public class RiskCurrentAnalysisDto {
    /**
     * 风险等级
     */
    private String riskLevel;
    /**
     * 现状分析数据列表
     */
    private List<RiskCurrentAnalysis> riskCurrentAnalysisList;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<RiskCurrentAnalysis> getRiskCurrentAnalysisList() {
        return riskCurrentAnalysisList;
    }

    public void setRiskCurrentAnalysisList(List<RiskCurrentAnalysis> riskCurrentAnalysisList) {
        this.riskCurrentAnalysisList = riskCurrentAnalysisList;
    }
}
