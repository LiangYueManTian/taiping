package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/10/23
 * 风险现状分析实体
 */
@Data
@TableName("t_risk_current_analysis")
public class RiskCurrentAnalysis extends Model<RiskCurrentAnalysis> {

    /**
     * 主键id
     */
    @TableId
    private String id;
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 风险项名称
     */
    private String riskItemName;
    /**
     * 超时时间（单位：月）
     */
    private Integer timeoutTime;
    /**
     * 风险等级
     */
    private Integer riskLevel;
    /**
     * 处理说明
     */
    private String processInstruction;
    /**
     * 运维管理活动类型
     */
    private Integer activityType;
    /**
     * 超过的阈值
     */
    private Double threshold;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
