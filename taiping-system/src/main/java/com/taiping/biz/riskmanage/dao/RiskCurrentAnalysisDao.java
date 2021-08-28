package com.taiping.biz.riskmanage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.riskmanage.RiskCurrentAnalysis;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * 风险现状分析持久层接口
 */
public interface RiskCurrentAnalysisDao extends BaseMapper<RiskCurrentAnalysis> {
    /**
     * 批量插入风险现状分析数据
     *
     * @param riskCurrentAnalysisData 风险现状分析数据
     * @return 插入条数
     */
    int batchInsertCurrentAnalysisData(List<RiskCurrentAnalysis> riskCurrentAnalysisData);

    /**
     * 批量删除现状分析数据
     *
     * @param ids 要删除的现状分析数据id列表
     * @return 删除条数
     */
    int batchDeleteCurrentAnalysisData(List<String> ids);

    /**
     * 批量更新风险现状分析数据
     *
     * @param riskCurrentAnalysisData 风险现状分析数据
     * @return 更新条数
     */
    int batchUpdateCurrentAnalysisData(List<RiskCurrentAnalysis> riskCurrentAnalysisData);

    /**
     * 获取风险现状分析数据
     *
     * @return 风险现状分析数据
     */
    List<RiskCurrentAnalysis> selectRiskCurrentAnalysisData();
}
