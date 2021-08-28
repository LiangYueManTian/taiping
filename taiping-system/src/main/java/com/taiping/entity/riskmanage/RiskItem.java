package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/10/23
 * 风险项实体
 */
@Data
@TableName("t_risk_item")
public class RiskItem extends Model<RiskItem> {
    /**
     * 风险项id
     */
    @TableId
    private String riskItemId;
    /**
     * 风险项名称
     */
    private String riskItemName;
    /**
     * 发现时间（时间戳）
     */
    private Long foundDate;
    /**
     * 位置/区域
     */
    private String location;
    /**
     * 涉及系统
     */
    private Integer referSystem;
    /**
     * 子系统
     */
    private String childSystem;
    /**
     * 涉及页面
     */
    private Integer referView;
    /**
     * 问题来源
     */
    private Integer problemSource;
    /**
     * 问题描述
     */
    private String problemDescribe;
    /**
     * 风险等级
     */
    private Integer riskLevel;
    /**
     * 风险类型
     */
    private Integer riskType;
    /**
     * 风险描述
     */
    private String riskDescribe;
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
    /**
     * 应对方案
     */
    private String responsePlan;
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
     * 计划解决时间(时间戳)
     */
    private Long planResolutionTime;
    /**
     * 实际解决时间(时间戳)
     */
    private Long actualResolutionTime;
    /**
     * 超时时间(单位:天)
     */
    private Integer timeoutTime;
    /**
     * 参考附件名
     */
    private String referAnnexName;
    /**
     * 风险追踪负责人
     */
    private String trackUser;
    /**
     * 复检人
     */
    private String checkUser;
    /**
     * 复检时间
     */
    private Long checkTime;
    /**
     * 复检结果
     */
    private Integer checkResult;
    /**
     * 是否为手动新增(0:是；1:否)
     */
    private Integer isManuallyAdded;
    /**
     * 相关运维管理活动id
     */
    private String activityId;
    /**
     * 发生年份
     */
    private Integer riskFoundYear;
    /**
     * 发生月份
     */
    private Integer riskFoundMonth;
    /**
     * 修改原因
     */
    private String updateReason;
    /**
     * 是否被删除
     */
    private Integer isDeleted;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新用户
     */
    private String updateUser;
    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.riskItemId;
    }
}
