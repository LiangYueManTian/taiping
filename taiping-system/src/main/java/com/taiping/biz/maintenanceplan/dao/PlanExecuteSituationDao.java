package com.taiping.biz.maintenanceplan.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.maintenanceplan.PlanExecuteSituation;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/12
 * 维保计划执行情况持久层接口
 */
public interface PlanExecuteSituationDao extends BaseMapper<PlanExecuteSituation> {
    /**
     * 批量插入维保计划执行情况
     *
     * @param situationList 需添加的维保计划执行情况列表
     * @return 添加条数
     */
    int batchInsertData(List<PlanExecuteSituation> situationList);

    /**
     * 批量修改维保计划执行情况
     *
     * @param situationList 需修改的维保计划执行情况列表
     * @return 更新条数
     */
    int batchUpdateData(List<PlanExecuteSituation> situationList);

    /**
     * 批量删除维保计划执行情况
     *
     * @param ids 维保计划执行情况id列表
     * @return 删除条数
     */
    int batchDeleteData(List<String> ids);

}
