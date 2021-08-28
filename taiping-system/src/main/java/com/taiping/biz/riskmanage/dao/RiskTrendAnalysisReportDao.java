package com.taiping.biz.riskmanage.dao;

import com.taiping.entity.riskmanage.RiskCurrentAnalysisReport;
import com.taiping.entity.riskmanage.RiskTrendAnalysisReport;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/13
 * 风险趋势分析持久层报告接口
 */
public interface RiskTrendAnalysisReportDao {
    /**
     * 根据月份获取报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 报告数据
     */
    List<RiskTrendAnalysisReport> selectDataByMonth(Long monthTime);

    /**
     * 批量插入报告数据
     *
     * @param data 报告数据
     * @return 插入条数
     */
    int batchInsertData(List<RiskTrendAnalysisReport> data);

    /**
     * 批量删除报告数据
     *
     * @param ids 报告数据列表
     * @return 删除条数
     */
    int batchDeleteData(List<String> ids);
}
