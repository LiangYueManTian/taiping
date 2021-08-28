package com.taiping.bean.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/25
 * 风险处理进度实体
 */
public class RiskProcessinProgressVo {
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 处理进度
     */
    private Integer processProgress;
    /**
     * 进度更新说明
     */
    private String progressUpdateDescription;
    /**
     * 解决状态
     */
    private Integer resolveStatus;
    /**
     * 实际解决时间(时间戳)
     */
    private Long actualResolutionTime;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public Integer getProcessProgress() {
        return processProgress;
    }

    public void setProcessProgress(Integer processProgress) {
        this.processProgress = processProgress;
    }

    public String getProgressUpdateDescription() {
        return progressUpdateDescription;
    }

    public void setProgressUpdateDescription(String progressUpdateDescription) {
        this.progressUpdateDescription = progressUpdateDescription;
    }

    public Integer getResolveStatus() {
        return resolveStatus;
    }

    public void setResolveStatus(Integer resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public Long getActualResolutionTime() {
        return actualResolutionTime;
    }

    public void setActualResolutionTime(Long actualResolutionTime) {
        this.actualResolutionTime = actualResolutionTime;
    }
}
