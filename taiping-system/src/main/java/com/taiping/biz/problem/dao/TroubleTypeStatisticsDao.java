package com.taiping.biz.problem.dao;

import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.problem.TroubleTypeStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 故障分类统计持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
public interface TroubleTypeStatisticsDao {
    /**
     * 批量插入
     * @param list 故障分类统计
     * @return int
     */
    int insertTroubleTypeStatisticsBatch(List<TroubleTypeStatistics> list);
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
    int updateBatchTroubleTypeStatistics(@Param("list") List<ManageActivity> manageActivityList);
    /**
     * 查询分析报告
     * @return List<TroubleTypeStatistics>
     */
    List<TroubleTypeStatistics> queryReportForCurrent();
    /**
     * 查询分析报告
     * @param date 月份
     * @return List<TroubleTypeStatistics>
     */
    List<TroubleTypeStatistics> queryReportForTime(Long date);
}
