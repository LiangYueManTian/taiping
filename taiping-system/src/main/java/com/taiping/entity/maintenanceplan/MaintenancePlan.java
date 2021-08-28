package com.taiping.entity.maintenanceplan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维护保养计划实体
 */
@Data
@TableName("t_maintenance_plan")
public class MaintenancePlan extends Model<MaintenancePlan> {
    /**
     * 维护保养计划id
     */
    @TableId
    private String maintenancePlanId;
    /**
     * 系统
     */
    private String system;
    /**
     * 子系统
     */
    private String childSystem;
    /**
     * 设备
     */
    private String device;
    /**
     * 周期(枚举)
     */
    private Integer period;
    /**
     * 部件
     */
    private String component;
    /**
     * 维保单位
     */
    private String maintenanceCompany;
    /**
     * 维护保养项目名称
     */
    private String maintenancePlanName;
    /**
     * 维保项目开始执行时间
     */
    private Long maintenanceStartTime;
    /**
     * 执行月数(每季度/半年度/年度第xx月)
     */
    private Integer executeMonth;
    /**
     * 执行周数(每月第xx周)
     */
    private Integer executeWeek;
    /**
     * 状态(正常、暂停、延期)
     */
    private Integer status;
    /**
     *暂停时间(时间戳)
     */
    private Long suspendTime;
    /**
     * 延期类型（延期xx倍周期或延期几个月）
     */
    private Integer delayType;
    /**
     * 是否为手动新增(0:是；1:否)
     */
    private Integer isManuallyAdded;
    /**
     * 相关运维管理活动id
     */
    private String activityId;
    /**
     * 维保醒目负责人
     */
    private String maintenanceUser;
    /**
     * 预提醒时间(单位:天)
     */
    private Integer preReminderTime;
    /**
     * 是否被删除
     */
    private Integer isDeleted;
    /**
     *创建用户
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
     *系统名
     */
    @TableField(exist = false)
    private String systemName;
    /**
     *子系统名
     */
    @TableField(exist = false)
    private String childSystemName;
    /**
     *设备名
     */
    @TableField(exist = false)
    private String deviceName;
    /**
     *部件名
     */
    @TableField(exist = false)
    private String componentName;
    /**
     * 维保单位
     */
    @TableField(exist = false)
    private String maintenanceCompanyName;
    /**
     *执行情况列表
     */
    @TableField(exist = false)
    private List<PlanExecuteSituation> situations;

    /**
     *根据系统合并行数
     */
    @TableField(exist = false)
    private Integer systemRowSpan;
    /**
     *根据子系统合并行数
     */
    @TableField(exist = false)
    private Integer childSystemRowSpan;
    /**
     *根据设备合并行数
     */
    @TableField(exist = false)
    private Integer deviceRowSpan;
    /**
     *根据周期合并行数
     */
    @TableField(exist = false)
    private Integer periodRowSpan;
    /**
     *根据部件合并行数
     */
    @TableField(exist = false)
    private Integer componentRowSpan;
    /**
     *根据维保单位合并行数
     */
    @TableField(exist = false)
    private Integer maintenanceCompanyRowSpan;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.maintenancePlanId;
    }
}
