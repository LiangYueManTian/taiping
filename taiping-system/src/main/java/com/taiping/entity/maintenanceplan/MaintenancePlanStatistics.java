package com.taiping.entity.maintenanceplan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/12/16
 * 维护保养计划统计实体类
 */
@Data
@TableName("t_maintenance_plan_statistics")
public class MaintenancePlanStatistics extends Model<MaintenancePlanStatistics> {
    /**
     * 主键id
     */
    @TableId
    private String id;
    /**
     *系统
     */
    private String system;
    /**
     *延期计划总数
     */
    private Integer delayTotalNum;
    /**
     *延期一到两倍周期数量
     */
    private Integer delayOneToTwoPeriodNum;
    /**
     *延期两倍周期以上数量
     */
    private Integer delayTwoPeriodUpperNum;
    /**
     *延期一到两个月数量
     */
    private Integer delayOneToTwoMonthNum;
    /**
     *延期两月以上数量
     */
    private Integer delayTwoMonthUpperNum;
    /**
     * 统计时间
     */
    private Long statisticsTime;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
