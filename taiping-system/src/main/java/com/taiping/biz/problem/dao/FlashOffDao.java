package com.taiping.biz.problem.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.problem.FlashOff;
import com.taiping.entity.problem.ProblemSelect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 停水停电持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-14
 */
public interface FlashOffDao {
    /**
     * 查询最新停水停电记录时间
     * @return Long 最新停水停电记录时间
     */
    Long queryLatestFlashOffDate();

    /**
     * 查询时间范围停水停电记录
     * @param problemSelect 时间范围
     * @return  List<FlashOff>
     */
    List<FlashOff> selectFlashOffForTime(ProblemSelect problemSelect);
    /**
     * 批量导入停水停电记录
     * @param flashOffList 停水停电记录
     * @return int
     */
    int addFlashOffBatch(List<FlashOff> flashOffList);

    /**
     * 分页查询停水停电记录
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<FlashOff>
     */
    List<FlashOff> selectFlashOffList(@Param("page") PageCondition pageCondition,
                                                @Param("filterList") List<FilterCondition> filterConditionList,
                                                @Param("sort") SortCondition sortCondition);

    /**
     * 查询停水停电记录数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectFlashOffListCount(@Param("filterList") List<FilterCondition> filterConditionList);
}
