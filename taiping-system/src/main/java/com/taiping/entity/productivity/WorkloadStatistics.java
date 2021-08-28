package com.taiping.entity.productivity;

/**
 * 团队负荷统计数据实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
public class WorkloadStatistics {
    /**
     * 主键id
     */
    private String workloadId;
    /**
     * 总上架U数
     */
    private Integer totalWorkload;
    /**
     * 日期
     */
    private Long workloadDate;
    /**
     * 空闲天数
     */
    private int freeDay;
    /**
     * 空闲负荷比
     */
    private Double freePercentage;
    /**
     * 正常天数
     */
    private int normalDay;
    /**
     * 正常负荷比
     */
    private Double normalPercentage;
    /**
     * 满负荷天数
     */
    private int fullDay;
    /**
     * 满负荷负荷比
     */
    private Double fullPercentage;
    /**
     * 计算百分比
     */
    public void calculate() {
        double day = freeDay + normalDay + fullDay;
        freePercentage = freeDay / day;
        normalPercentage = normalDay / day;
        fullPercentage = fullDay / day;
    }

    public String getWorkloadId() {
        return workloadId;
    }

    public void setWorkloadId(String workloadId) {
        this.workloadId = workloadId;
    }

    public Integer getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(Integer totalWorkload) {
        this.totalWorkload = totalWorkload;
    }

    public Long getWorkloadDate() {
        return workloadDate;
    }

    public void setWorkloadDate(Long workloadDate) {
        this.workloadDate = workloadDate;
    }

    public int getFreeDay() {
        return freeDay;
    }

    public void setFreeDay(int freeDay) {
        this.freeDay = freeDay;
    }

    public Double getFreePercentage() {
        return freePercentage;
    }

    public void setFreePercentage(Double freePercentage) {
        this.freePercentage = freePercentage;
    }

    public int getNormalDay() {
        return normalDay;
    }

    public void setNormalDay(int normalDay) {
        this.normalDay = normalDay;
    }

    public Double getNormalPercentage() {
        return normalPercentage;
    }

    public void setNormalPercentage(Double normalPercentage) {
        this.normalPercentage = normalPercentage;
    }

    public int getFullDay() {
        return fullDay;
    }

    public void setFullDay(int fullDay) {
        this.fullDay = fullDay;
    }

    public Double getFullPercentage() {
        return fullPercentage;
    }

    public void setFullPercentage(Double fullPercentage) {
        this.fullPercentage = fullPercentage;
    }
}
