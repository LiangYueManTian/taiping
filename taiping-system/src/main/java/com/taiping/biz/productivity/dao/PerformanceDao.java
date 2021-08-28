package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.Performance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人KPI持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
public interface PerformanceDao {
    /**
     * 批量新增个人KPI
     * @param performanceList 个人KPI
     * @return int
     */
    int insertPerformanceBatch(List<Performance> performanceList);


    /**
     * 分页查询个人KPI
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<Performance>
     */
    List<Performance> selectPerformanceList(@Param("page") PageCondition pageCondition,
                                      @Param("filterList") List<FilterCondition> filterConditionList,
                                      @Param("sort") SortCondition sortCondition);

    /**
     * 查询个人KPI数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectPerformanceListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询人
     * @return  List<String> 人
     */
    List<String> selectPersonList();
}
