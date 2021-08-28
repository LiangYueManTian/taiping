package com.taiping.biz.riskmanage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.riskmanage.RiskCurrentAnalysis;
import com.taiping.entity.riskmanage.RiskTrendAnalysis;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * 风险趋势分析持久层接口
 */
public interface RiskTrendAnalysisDao extends BaseMapper<RiskTrendAnalysis> {
    /**
     * 批量插入风险趋势分析数据
     *
     * @param riskTrendAnalyseData 风险趋势分析数据
     * @return 插入条数
     */
    int batchInsertTrendAnalysisData(List<RiskTrendAnalysis> riskTrendAnalyseData);


    /**
     * 批量删除趋势分析数据
     *
     * @param ids 要删除的趋势分析数据id列表
     * @return 删除条数
     */
    int batchDeleteTrendAnalysisData(List<String> ids);

    /**
     * 批量更新风险趋势分析数据
     *
     * @param riskTrendAnalysisData 风险趋势分析数据
     * @return 更新条数
     */
    int batchUpdateTrendAnalysisData(List<RiskTrendAnalysis> riskTrendAnalysisData);

    /**
     * 获取风险趋势分析数据
     *
     * @return 风险趋势分析数据
     */
    List<RiskTrendAnalysis> selectRiskTrendAnalysisData();
}
