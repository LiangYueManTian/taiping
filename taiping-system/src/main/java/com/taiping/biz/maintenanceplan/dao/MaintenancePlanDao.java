package com.taiping.biz.maintenanceplan.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.maintenanceplan.PlanExecuteSituation;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维护保养计划持久层接口
 */
public interface MaintenancePlanDao extends BaseMapper<MaintenancePlan> {
    /**
     * 批量修改维保计划
     *
     * @param planList 需修改的维保计划列表
     * @return 更新条数
     */
    int batchUpdateData(List<MaintenancePlan> planList);
}
