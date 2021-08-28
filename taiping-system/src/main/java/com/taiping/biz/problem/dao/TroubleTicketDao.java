package com.taiping.biz.problem.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.problem.ProblemSelect;
import com.taiping.entity.problem.ProblemStatistics;
import com.taiping.entity.problem.TroubleTicket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 故障单持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
public interface TroubleTicketDao {
    /**
     * 新增故障单
     * @param troubleTicket 故障单
     * @return int
     */
    int insertTroubleTicket(TroubleTicket troubleTicket);

    /**
     * 根据故障单号查询故障单数量
     * @param troubleTicket 故障单号
     * @return Integer 故障单数量
     */
    Integer selectTroubleTicketByCode(TroubleTicket troubleTicket);

    /**
     * 根据ID查询故障单
     * @param ticketId 故障单 ID
     * @return TroubleTicket 故障单
     */
    TroubleTicket queryTroubleTicketById(String ticketId);

    /**
     * 根据ID删除故障单
     * @param ticketId 故障单 ID
     * @return int
     */
    int deleteTroubleTicket(String ticketId);

    /**
     * 查询最早故障时间和服务中断时长
     * @return TroubleTicket
     */
    TroubleTicket queryTroubleTicketInterrupt();

    /**
     * 根据ID修改故障单
     * @param troubleTicket 故障单
     * @return int
     */
    int updateTroubleTicketById(TroubleTicket troubleTicket);

    /**
     * 按级别查询故障单数量
     * @param problemSelect 查询条件
     * @return List<ProblemStatistics>
     */
    List<ProblemStatistics> selectTroubleTicketForLevel(ProblemSelect problemSelect);
    /**
     * 查询时间范围故障单
     * @param problemSelect 时间范围
     * @return List<TroubleTicket>
     */
    List<TroubleTicket> selectTroubleTicketForTime(ProblemSelect problemSelect);
    /**
     * 查询最新故障时间
     * @return  Long 故障时间
     */
    Long queryLatestTroubleTicketDate();
    /**
     * 批量导入故障单
     * @param troubleTicketList 故障单
     * @return int
     */
    int addTroubleTicketBatch(List<TroubleTicket> troubleTicketList);
    /**
     * 分页查询故障单
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<TroubleTicket>
     */
    List<TroubleTicket> selectTroubleTicketList(@Param("page") PageCondition pageCondition,
                                      @Param("filterList") List<FilterCondition> filterConditionList,
                                      @Param("sort") SortCondition sortCondition);

    /**
     * 查询故障单数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectTroubleTicketListCount(@Param("filterList") List<FilterCondition> filterConditionList);
}
