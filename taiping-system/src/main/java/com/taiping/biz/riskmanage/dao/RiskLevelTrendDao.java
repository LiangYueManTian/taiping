package com.taiping.biz.riskmanage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.riskmanage.RiskLevelTrend;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/4
 * 风险等级趋势分析持久层接口
 */
public interface RiskLevelTrendDao extends BaseMapper<RiskLevelTrend> {
    /**
     * 批量插入风险等级趋势分析数据
     *
     * @param riskLevelTrendData 风险等级趋势分析数据
     * @return 插入条数
     */
    int batchInsertLevelTrendData(List<RiskLevelTrend> riskLevelTrendData);


    /**
     * 批量删除风险等级趋势分析数据
     *
     * @param ids 要删除的风险等级趋势分析数据id列表
     * @return 删除条数
     */
    int batchDeleteLevelTrendData(List<String> ids);

    /**
     * 批量更新风险等级趋势分析数据
     *
     * @param riskLevelTrendData 风险等级趋势分析数据
     * @return 更新条数
     */
    int batchUpdateLevelTrendData(List<RiskLevelTrend> riskLevelTrendData);

    /**
     * 获取风险等级趋势分析数据
     *
     * @return 风险等级趋势分析数据
     */
    List<RiskLevelTrend> selectLevelTrendData();
}
