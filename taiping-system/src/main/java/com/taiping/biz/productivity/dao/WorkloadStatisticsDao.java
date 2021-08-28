package com.taiping.biz.productivity.dao;

import com.taiping.entity.productivity.ProStatistics;
import com.taiping.entity.productivity.WorkloadStatistics;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * 团队负荷统计数据持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-17
 */
public interface WorkloadStatisticsDao {

    /**
     * 插入 团队负荷统计数据
     * @param workloadStatistics 团队负荷统计数据
     * @return int
     */
    int insertWorkloadStatistics(WorkloadStatistics workloadStatistics);

    /**
     * 时间范围团队负荷统计数据
     * @param proStatistics 时间范围
     * @return Map<Long, WorkloadStatistics>
     */
    @MapKey("workloadDate")
    Map<Long, WorkloadStatistics> queryWorkloadStatisticsForTime(ProStatistics proStatistics);
}
