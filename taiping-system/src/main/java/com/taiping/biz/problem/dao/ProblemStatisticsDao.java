package com.taiping.biz.problem.dao;

import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.problem.ProblemStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题分析次数统计持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
public interface ProblemStatisticsDao {

    /**
     * 批量插入
     * @param list 问题分析次数统计
     * @return int
     */
    int insertProblemStatisticsBatch(List<ProblemStatistics> list);
    /**
     * 取消当月分析报告
     * @return int
     */
    int cancelReportCurrent();
    /**
     * 生成分析报告
     * @param manageActivityList 运维管理活动
     * @return int
     */
    int updateBatchProblemStatistics(@Param("list") List<ManageActivity> manageActivityList);

    /**
     * 查询分析报告
     * @return List<ProblemStatistics>
     */
    List<ProblemStatistics> queryReportForCurrent();
    /**
     * 查询分析报告
     * @param date 月份
     * @return List<ProblemStatistics>
     */
    List<ProblemStatistics> queryReportForTime(Long date);
}
