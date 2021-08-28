package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/10/23
 * 风险趋势分析实体
 */
@Data
@TableName("t_risk_trend_analysis")
public class RiskTrendAnalysis extends Model<RiskTrendAnalysis> {

    /**
     * 主键id
     */
    @TableId
    private String id;
    /**
     * 风险类型
     */
    private String riskType;
    /**
     * 高等级风险个数
     */
    private Integer advancedRiskNumber;
    /**
     * 高等级风险同比
     */
    private Double advancedRiskYearOverYear;
    /**
     * 高等级风险环比
     */
    private Double advancedRiskRingGrowth;
    /**
     * 中等级风险个数
     */
    private Integer intermediateRiskNumber;
    /**
     * 中等级风险同比
     */
    private Double intermediateRiskYearOverYear;
    /**
     * 中等级风险环比
     */
    private Double intermediateRiskRingGrowth;
    /**
     * 低等级风险个数
     */
    private Integer lowRiskNumber;
    /**
     * 低等级风险同比
     */
    private Double lowRiskYearOverYear;
    /**
     * 低等级风险环比
     */
    private Double lowRiskRingGrowth;
    /**
     * 发生时间
     */
    private Long riskFoundTime;
    /**
     * 处理说明
     */
    private String processInstruction;
    /**
     * 运维管理活动类型
     */
    private Integer activityType;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
