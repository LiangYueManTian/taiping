package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.InspectionTask;
import com.taiping.entity.productivity.ProStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-15
 */
public interface InspectionTaskDao {

    /**
     * 批量导入巡检工单数据
     * @param inspectionTaskList 巡检工单数据
     * @return int
     */
    int addInspectionTaskBatch(List<InspectionTask> inspectionTaskList);

    /**
     * 分页查询常白班排班和倒班排班
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<InspectionTask>
     */
    List<InspectionTask> selectInspectionTaskList(@Param("page") PageCondition pageCondition,
                                      @Param("filterList") List<FilterCondition> filterConditionList,
                                      @Param("sort") SortCondition sortCondition);

    /**
     * 查询查询常白班排班和倒班排班数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectInspectionTaskListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询执行人
     * @return 执行人
     */
    List<String> selectExecutorList();
    /**
     * 统计时间范围每个人巡检次数
     * @param proStatistics 时间范围
     * @return List<ProStatistics>
     */
    List<ProStatistics> selectCountOfTime(ProStatistics proStatistics);
}
