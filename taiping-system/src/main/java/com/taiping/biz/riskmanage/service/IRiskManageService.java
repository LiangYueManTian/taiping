package com.taiping.biz.riskmanage.service;

import com.baomidou.mybatisplus.service.IService;
import com.taiping.bean.riskmanage.RiskCurrentAnalysisDto;
import com.taiping.bean.riskmanage.RiskItemDto;
import com.taiping.bean.riskmanage.RiskTrendAnalysisVo;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.riskmanage.*;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * 风控管理逻辑层接口
 */
public interface IRiskManageService extends IService<RiskItem> {
    /**
     * 获取所有风险项
     *
     * @return List<RiskItem> 风险项列表
     */
    List<RiskItem> getAllRiskItem();

    /**
     * 分页查询风险项列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean queryRiskItemByCondition(QueryCondition<RiskItem> queryCondition);

    /**
     * 根据id查询风险项
     *
     * @param riskItemId 风险项id
     * @return 风险项信息
     */
    RiskItemDto getRiskItemById(String riskItemId);

    /**
     * 根据风险追踪负责人查询风险项
     *
     * @param trackUser 风险追踪负责人
     * @return 风险项列表
     */
    List<RiskItem> getRiskItemsByTrackUser(String trackUser);

    /**
     * 根据复检人查询风险项
     *
     * @param checkUser 复检人
     * @return 风险项列表
     */
    List<RiskItem> getRiskItemsByCheckUser(String checkUser);

    /**
     * 根据相关运维活动id查询风险项
     *
     * @param activityId 相关运维活动id
     * @return 风险项信息
     */
    RiskItem getRiskItemByActivityId(String activityId);

    /**
     * 添加风险项
     *
     * @param riskItem 需添加的风险项
     * @return 添加结果
     */
    Result addRiskItem(RiskItem riskItem);

    /**
     * 添加风险项
     *
     * @param riskItem 需添加的风险项
     */
    void addRiskItemInside(RiskItem riskItem);

    /**
     * 修改风险项
     *
     * @param riskItem 需修改的风险项
     * @return 修改结果
     */
    Result modifyRiskItem(RiskItem riskItem);

    /**
     * 删除风险项
     *
     * @param riskItemIds 需删除的风险项id列表
     * @return 删除结果
     */
    Result deleteRiskItem(List<String> riskItemIds);

//    /**
//     * 风险分值登记
//     *
//     * @param riskScoreVo 风险分值实体
//     * @return 登记结果
//     */
//    Result riskScoreRegister(RiskScoreVo riskScoreVo);
//
//    /**
//     * 风险追踪负责人指派
//     *
//     * @param riskTrackUserAssignVo 风险追踪负责人指派信息
//     * @return 指派结果
//     */
//    Result riskTrackUserAssign(RiskTrackUserAssignVo riskTrackUserAssignVo);
//
//    /**
//     * 风险应对方案提交
//     *
//     * @param riskResponsePlanVo 风险应对方案信息
//     * @return 提交结果
//     */
//    Result riskResponsePlanCommit(RiskResponsePlanVo riskResponsePlanVo);
//
//    /**
//     * 风险处理进度更新
//     *
//     * @param riskProcessinProgressVo 风险处理进度信息
//     * @return 更新结果
//     */
//    Result riskProcessinProgressUpdate(RiskProcessinProgressVo riskProcessinProgressVo);
//
//    /**
//     * 更新风险项复检信息
//     *
//     * @param riskItemRecheckVo 风险项复检信息
//     * @return 更新结果
//     */
//    Result riskItemRecheck(RiskItemRecheckVo riskItemRecheckVo);

    /**
     * 更新分析数据
     */
    Result updateAnalysisData();

    /**
     * 获取风险现状分析数据
     *
     * @return 险现状分析数据列表
     */
    List<RiskCurrentAnalysisDto> getRiskCurrentAnalysisData();

    /**
     * 分页查询风险趋势分析数据
     *
     * @param queryCondition 查询条件
     * @return 险现状分析数据列表
     */
    PageBean getRiskTrendAnalysisData(QueryCondition<RiskTrendAnalysisVo> queryCondition);

    /**
     * 查询风险趋势分析图表数据
     *
     * @param trendAnalysisVo 查询条件
     * @return List<RiskCurrentAnalysisDto> 风险现状分析数据列表
     */

    List<RiskTrendAnalysis> getRiskTrendChartData(RiskTrendAnalysisVo trendAnalysisVo);

    /**
     * 查询最近一年风险趋势分析图表数据(包含预测数据)
     *
     * @return List<RiskCurrentAnalysisDto> 风险现状分析数据列表
     */
    List<RiskTrendAnalysis> getYearTrendChartData();

    /**
     * 查询本月风险趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险趋势分析数据列表
     */
    List<RiskTrendAnalysis> getRiskTrendMonthData();

    /**
     * 查询下月风险趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险趋势分析数据列表
     */
    List<RiskTrendAnalysis> getRiskTrendNextMonthData();

    /**
     * 查询本月风险等级趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险等级趋势分析数据列表
     */
    List<RiskLevelTrend> getRiskLevelTrendMonthData();

    /**
     * 查询下月风险等级趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险等级趋势分析数据列表
     */
    List<RiskLevelTrend> getRiskLevelTrendNextMonthData();

    /**
     * 更新风险超时时间
     */
    void updateRiskTimeoutTime();

    /**
     * 更新风险现状分析数据
     *
     * @param currentAnalysis 需修改的数据
     * @return 修改结果
     */
    Result modifyRiskCurrentData(RiskCurrentAnalysis currentAnalysis);

    /**
     *更新风险趋势分析数据
     *
     * @param trendAnalysis 需修改的数据
     * @return 修改结果
     */
    Result modifyRiskTrendData(RiskTrendAnalysis trendAnalysis);

    /**
     *
     * 更新风险等级趋势分析数据
     * @param levelTrend 需修改的数据
     * @return 修改结果
     */
    Result modifyRiskLevelTrendData(RiskLevelTrend levelTrend);

    /**
     * 根据id获取现状分析数据
     *
     * @param dataId 数据id
     * @return 现状分析数据项
     */
    RiskCurrentAnalysis getCurrentDataById(String dataId);

    /**
     * 根据id获取趋势分析数据
     *
     * @param dataId 数据id
     * @return 趋势分析数据项
     */
    RiskTrendAnalysis getTrendDataById(String dataId);

    /**
     * 根据id获取风险等级趋势分析数据
     *
     * @param dataId 数据id
     * @return 风险等级趋势分析数据项
     */
    RiskLevelTrend getLevelTrendDataById(String dataId);

    /**
     * 根据运维管理活动生成风险项
     *
     * @param activityId 运维管理活动id
     * @param riskItemName 风险项名称
     */
    void createRiskItemByActivityId(String activityId,String riskItemName);

    /**
     * 根据用户id获取待处理风险项总数
     *
     * @param userId 用户id
     * @return Integer 待处理风险项总数
     */
    Integer queryCountByUserId(String userId);

    /**
     * 条件查询用户待处理的风险项列表
     *
     * @param queryCondition 查询条件
     * @return 风险项列表
     */
    PageBean getUserRiskByCondition(QueryCondition<RiskItem> queryCondition);

    /**
     * 保存风险分析报告数据
     *
     * @return 保存结果
     */
    Result saveAnalysisReportData();

    /**
     * 根据月份获取现状分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 现状分析报告数据
     */
    List<RiskCurrentAnalysisDto> getCurrentAnalysisReportData(Long monthTime);

    /**
     * 根据月份获取趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    List<RiskTrendAnalysisReport> getTrendAnalysisReportData(Long monthTime);

    /**
     * 根据月份获取当月现状趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    List<RiskTrendAnalysisReport> getMonthTrendAnalysisReportData(Long monthTime);

    /**
     * 根据月份获取下月趋势预测分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    List<RiskTrendAnalysisReport> getNextMonthTrendAnalysisReportData(Long monthTime);

    /**
     * 根据月份获取等级趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 级趋势分析报告数据
     */
    List<RiskLevelTrendReport> getLevelTrendAnalysisReportData(Long monthTime);

    /**
     * 根据月份获取下月等级趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 级趋势分析报告数据
     */
    List<RiskLevelTrendReport> getNextMonthLevelTrendReportData(Long monthTime);
}
