package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/11/4
 * 风险等级趋势分析实体
 */
@Data
@TableName("t_risk_level_trend")
public class RiskLevelTrend extends Model<RiskLevelTrend> {
    /**
     * 主键id
     */
    @TableId
    private String id;
    /**
     * 风险等级
     */
    private String riskLevel;
    /**
     * 风险发生时间（时间戳，精确到月）
     */
    private Long riskFoundTime;
    /**
     * 数量
     */
    private Integer riskCount;
    /**
     * 增加数量
     */
    private Integer addNumber;
    /**
     * 环比值
     */
    private Double ringGrowth;
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
