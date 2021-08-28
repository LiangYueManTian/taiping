package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.DailyWorkload;
import com.taiping.entity.productivity.ProStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 团队负荷持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-06
 */
public interface DailyWorkloadDao {
    /**
     * 批量插入团队负荷表
     * @param dailyWorkloadList  团队负荷
     * @return int
     */
    int insertDailyWorkloadBatch(List<DailyWorkload> dailyWorkloadList);

    /**
     * 分页查询团队负荷
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<DailyWorkload>
     */
    List<DailyWorkload> selectDailyWorkloadList(@Param("page") PageCondition pageCondition,
                                            @Param("filterList") List<FilterCondition> filterConditionList,
                                            @Param("sort") SortCondition sortCondition);

    /**
     * 查询团队负荷数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectDailyWorkloadListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询时间范围团队负荷
     * @param proStatistics  时间范围
     * @return List<DailyWorkload>
     */
    List<DailyWorkload> queryWorkloadForTime(ProStatistics proStatistics);
}
