package com.taiping.biz.productivity.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.productivity.ProStatistics;
import com.taiping.entity.productivity.Schedule;
import com.taiping.entity.productivity.ScheduleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 常白班排班和倒班排班持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface ScheduleDao {
    /**
     * 批量导入常白班排班和倒班排班参数
     * @param scheduleList 健康卡参数
     * @return int
     */
    int addScheduleBatch(List<Schedule> scheduleList);

    /**
     * 查询重复日期
     * @param list 日期
     * @param type 类型
     * @return 日期 List<Long>
     */
    List<Long> queryScheduleForTime(@Param("list") List<ScheduleDto> list, @Param("type") String type);

    /**
     * 分页查询常白班排班和倒班排班
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<Schedule>
     */
    List<Schedule> selectScheduleList(@Param("page") PageCondition pageCondition,
                                      @Param("filterList") List<FilterCondition> filterConditionList,
                                      @Param("sort") SortCondition sortCondition);

    /**
     * 查询查询常白班排班和倒班排班数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectScheduleListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询最新数据时间
     * @return Long
     */
    Long queryLatestScheduleDate();

    /**
     * 统计时间范围每个人值班次数
     * @param proStatistics 时间范围
     * @return List<ProStatistics>
     */
    List<ProStatistics> selectCountOfMonth(ProStatistics proStatistics);
}
