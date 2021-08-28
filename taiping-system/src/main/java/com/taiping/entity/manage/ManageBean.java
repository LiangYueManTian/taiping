package com.taiping.entity.manage;

import com.taiping.biz.budget.dto.BudgetPurchaseDto;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.riskmanage.RiskItem;
import lombok.Data;

/**
 * 运维管理活动实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-12-06
 */
@Data
public class ManageBean {
    /**
     * 运维管理活动
     */
    private ManageActivity manage;
    /**
     * 风险
     */
    private RiskItem risk;
    /**
     * 预算与采购
     */
    private BudgetPurchaseDto budget;
    /**
     * 维护保养计划
     */
    private MaintenancePlan plan;
}
