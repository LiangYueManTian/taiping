package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.Change;
import com.taiping.entity.productivity.ChangeRead;
import com.taiping.entity.productivity.ProStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 变更单持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-07
 */
public interface ChangeDao {
    /**
     * 批量导入变更单
     * @param changeReadList 变更单
     * @return int
     */
    int insertChangeBatch(List<ChangeRead> changeReadList);

    /**
     * 分页查询变更单
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<Change>
     */
    List<Change> selectChangeList(@Param("page") PageCondition pageCondition,
                                  @Param("filterList") List<FilterCondition> filterConditionList,
                                  @Param("sort") SortCondition sortCondition);

    /**
     * 查询变更单数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectChangeListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询时间范围变更单
     * @param proStatistics 时间范围
     * @return  List<Change>
     */
    List<Change> selectChangeForTime(ProStatistics proStatistics);
}
