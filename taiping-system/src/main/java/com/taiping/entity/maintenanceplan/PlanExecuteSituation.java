package com.taiping.entity.maintenanceplan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/11/7
 * 维保计划执行情况实体
 */
@Data
@TableName("t_plan_execute_situation")
public class PlanExecuteSituation extends Model<PlanExecuteSituation> {
    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 维护保养计划id
     */
    private String maintenancePlanId;
    /**
     *计划执行时间(时间戳)
     */
    private Long planExecuteTime;
    /**
     * 执行状态(枚举)
     */
    private Integer executeStatus;
    /**
     * 实际执行时间(时间戳)
     */
    private Long actualExecuteTime;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
