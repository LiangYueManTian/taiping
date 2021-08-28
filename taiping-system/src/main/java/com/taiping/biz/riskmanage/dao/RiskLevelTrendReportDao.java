package com.taiping.biz.riskmanage.dao;

import com.taiping.entity.riskmanage.RiskLevelTrendReport;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/13
 * 风险等级趋势分析报告持久层接口
 */
public interface RiskLevelTrendReportDao {
    /**
     * 根据月份获取报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 报告数据
     */
    List<RiskLevelTrendReport> selectDataByMonth(Long monthTime);

    /**
     * 批量插入报告数据
     *
     * @param data 报告数据
     * @return 插入条数
     */
    int batchInsertData(List<RiskLevelTrendReport> data);

    /**
     * 批量删除报告数据
     *
     * @param ids 报告数据列表
     * @return 删除条数
     */
    int batchDeleteData(List<String> ids);
}
