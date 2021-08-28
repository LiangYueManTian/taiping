package com.taiping.biz.problem.dao;

import com.taiping.entity.problem.FlashOffTrend;

import java.util.List;

/**
 * 停水停电趋势持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
public interface FlashOffTrendDao {
    /**
     * 批量插入
     * @param list 停水停电趋势
     * @return int
     */
    int insertFlashOffTrendBatch(List<FlashOffTrend> list);

    /**
     * 查询分析报告
     * @param date 时间
     * @return List<FlashOffTrend>
     */
    List<FlashOffTrend> queryReport(Long date);
}
