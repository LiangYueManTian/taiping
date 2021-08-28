package com.taiping.biz.problem.dao;

import com.taiping.entity.problem.TroubleTrend;

import java.util.List;

/**
 * 故障分类趋势持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
public interface TroubleTrendDao {
    /**
     * 批量插入
     * @param list 故障分类趋势
     * @return int
     */
    int insertTroubleTrendBatch(List<TroubleTrend> list);
    /**
     * 查询分析报告
     * @param date 时间
     * @return List<TroubleTrend>
     */
    List<TroubleTrend> queryReport(Long date);
}
