package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.ChangePlan;
import com.taiping.entity.productivity.ProStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 未来变更计划持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-06
 */
public interface ChangePlanDao {

    /**
     * 新增未来变更计划
     * @param changePlan 未来变更计划
     * @return int
     */
    int insertChangePlan(ChangePlan changePlan);

    /**
     * 修改未来变更计划
     * @param changePlan 未来变更计划
     * @return int
     */
    int updateChangePlan(ChangePlan changePlan);

    /**
     * 删除未来变更计划
     * @param list 未来变更计划ID
     * @return int
     */
    int deleteChangePlan(List<String> list);

    /**
     * 根据名称查询未来变更计划
     * @param changePlan 未来变更计划
     * @return  List<String> 未来变更计划ID
     */
    List<String> selectChangePlanForName(ChangePlan changePlan);

    /**
     * 根据ID查询未来变更计划
     * @param changeId ID
     * @return ChangePlan
     */
    ChangePlan selectChangePlanById(String changeId);
    /**
     * 分页查询未来变更计划
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<ManageActivity>
     */
    List<ChangePlan> selectChangePlanList(@Param("page") PageCondition pageCondition,
                                          @Param("filterList") List<FilterCondition> filterConditionList,
                                          @Param("sort") SortCondition sortCondition);
    /**
     * 查询未来变更计划
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectChangePlanListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询时间范围未来变更计划
     * @param proStatistics 时间范围
     * @return List<ChangePlan>
     */
    List<ChangePlan> selectChangePlanForTime(ProStatistics proStatistics);
}
