package com.taiping.biz.productivity.dao;

import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.productivity.MonthWorkload;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团队负荷月份统计数据持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-06
 */
public interface MonthWorkloadDao {
    /**
     * 批量插入团队负荷月份统计数据
     * @param monthWorkloadList 团队负荷月份统计数据
     * @return int
     */
    int insertMonthWorkloadBatch(List<MonthWorkload> monthWorkloadList);

    /**
     * 查询当月分析报告
     * @return List<MonthWorkload> 当月分析报告
     */
    List<MonthWorkload> queryReportForCurrent();

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
    int updateBatchMonthWorkload(@Param("list") List<ManageActivity> manageActivityList);

    /**
     * 查询分析报告
     * @param workloadDate 时间
     * @return List<MonthWorkload> 分析报告
     */
    List<MonthWorkload> queryReportForTime(Long workloadDate);

    /**
     * 查询团队负荷
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param valueType 类型
     * @return List<MonthWorkload>
     */
    List<MonthWorkload> queryMonthWorkloadForTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                                  @Param("valueType") String valueType);
}
