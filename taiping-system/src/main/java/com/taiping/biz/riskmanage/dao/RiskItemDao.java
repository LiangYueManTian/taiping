package com.taiping.biz.riskmanage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.riskmanage.RiskItem;
import com.taiping.entity.riskmanage.RiskTrendStatistics;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * 风险项管理持久层接口
 */
public interface RiskItemDao extends BaseMapper<RiskItem> {
    /**
     * 查询所有风险项
     *
     * @return List<RiskItem> 风险项列表
     */
    List<RiskItem> selectAllRiskItem();

    /**
     * 根据风险追踪负责人查询风险项
     *
     * @param trackUser 风险追踪负责人
     * @return 风险项列表
     */
    List<RiskItem> selectRiskItemsByTrackUser(String trackUser);

    /**
     * 根据复检人查询风险项
     *
     * @param checkUser 复检人
     * @return 风险项列表
     */
    List<RiskItem> selectRiskItemByCheckUser(String checkUser);

    /**
     * 根据用户id获取待处理风险项总数
     *
     * @param userId 用户id
     * @return Integer 待处理风险项总数
     */
    Integer selectCountByUserId(String userId);
    /**
     * 根据相关运维活动id查询风险项
     *
     * @param activityId 相关运维活动id
     * @return 风险项信息
     */
    RiskItem selectRiskItemByActivityId(String activityId);

    /**
     * 批量更新风险项超时时间
     *
     * @param riskItems 风险项列表
     * @return 更新条数
     */
    void batchUpdateTimeoutTime(List<RiskItem> riskItems);

    /**
     * 获取风险趋势分析统计数据
     *
     * @return 统计数据
     */
    List<RiskTrendStatistics> selectRiskTrendStatisticsData();
}
