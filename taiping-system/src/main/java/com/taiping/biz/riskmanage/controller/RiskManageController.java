package com.taiping.biz.riskmanage.controller;

import com.taiping.bean.riskmanage.*;
import com.taiping.biz.riskmanage.service.IRiskManageService;
import com.taiping.constant.riskmanage.RiskManageResultCode;
import com.taiping.constant.user.UserManageResultCode;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.riskmanage.*;
import com.taiping.exception.handler.BizExceptionHandler;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * description
 */
@RestController
@RequestMapping("/taiping/riskManage")
@Slf4j
public class RiskManageController extends BizExceptionHandler {

    /**
     *风险项管理持久层接口
     */
    @Autowired
    private IRiskManageService riskManageService;

    /**
     * 获取所有风险项
     *
     * @return Result<List<RiskItem>> 风险项列表
     */
//    @UserLoginToken
    @GetMapping("/queryRiskItemList")
    public Result queryRiskItemList(){
        List<RiskItem> riskItemList = riskManageService.getAllRiskItem();
        return ResultUtils.success(riskItemList);
    }

    /**
     * 条件查询风险项
     *
     * @param queryCondition 查询条件
     * @return 风险项列表
     */
//    @UserLoginToken
    @PostMapping("/queryRiskItemByCondition")
    private Result queryRiskItemByCondition(@RequestBody QueryCondition<RiskItem> queryCondition) {
        PageBean pageBean = riskManageService.queryRiskItemByCondition(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 条件查询用户待处理的风险项列表
     *
     * @param queryCondition 查询条件
     * @return 风险项列表
     */
//    @UserLoginToken
    @PostMapping("/queryUserRiskByCondition")
    private Result queryUserRiskByCondition(@RequestBody QueryCondition<RiskItem> queryCondition) {
        PageBean pageBean = riskManageService.getUserRiskByCondition(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 根据id查询风险项
     *
     * @param riskItemId 风险项id
     * @return Result<RiskItem> 风险项信息
     */
//    @UserLoginToken
    @GetMapping("/queryRiskItemById/{riskItemId}")
    public Result queryRiskItemById(@PathVariable String riskItemId){
        RiskItemDto riskItem = riskManageService.getRiskItemById(riskItemId);
        return ResultUtils.success(riskItem);
    }

    /**
     * 根据风险追踪负责人查询风险项
     *
     * @param trackUser 风险追踪负责人id
     * @return Result<List<RiskItem>> 风险项列表
     */
//    @UserLoginToken
    @GetMapping("/queryRiskItemsByTrackUser/{trackUser}")
    public Result queryRiskItemsByTrackUser(@PathVariable String trackUser){
        List<RiskItem> riskItemList = riskManageService.getRiskItemsByTrackUser(trackUser);
        return ResultUtils.success(riskItemList);
    }

    /**
     * 根据用户id获取待处理风险项总数
     *
     * @param userId 用户id
     * @return Result<Integer> 待处理风险项总数
     */
//    @UserLoginToken
    @GetMapping("/queryCountByUserId/{userId}")
    public Result queryCountByUserId(@PathVariable String userId) {
        return ResultUtils.success(riskManageService.queryCountByUserId(userId));
    }

    /**
     * 根据复检人查询风险项
     *
     * @param checkUser 复检人id
     * @return Result<List<RiskItem>> 风险项列表
     */
//    @UserLoginToken
    @GetMapping("/queryRiskItemsByCheckUser/{checkUser}")
    public Result queryRiskItemsByCheckUser(@PathVariable String checkUser){
        List<RiskItem> riskItemList = riskManageService.getRiskItemsByCheckUser(checkUser);
        return ResultUtils.success(riskItemList);
    }

    /**
     * 根据相关运维活动id查询风险项
     *
     * @param activityId 相关运维活动id
     * @return Result<List<RiskItem>> 风险项信息
     */
//    @UserLoginToken
    @GetMapping("/queryRiskItemsByActivityId/{activityId}")
    public Result queryRiskItemsByActivityId(@PathVariable String activityId){
        RiskItem riskItem = riskManageService.getRiskItemByActivityId(activityId);
        return ResultUtils.success(riskItem);
    }

    /**
     * 手动添加风险项
     *
     * @param riskItem 需添加的风险项
     * @return 添加结果
     */
//    @UserLoginToken
    @PutMapping("/addRiskItem")
    public Result addRiskItem(@RequestBody RiskItem riskItem) {
        return riskManageService.addRiskItem(riskItem);
    }

    /**
     * 修改风险项
     *
     * @param riskItem 需修改的风险项
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyRiskItem")
    public Result modifyRiskItem(@RequestBody RiskItem riskItem) {
        return riskManageService.modifyRiskItem(riskItem);
    }

    /**
     * 批量删除风险项
     *
     * @param riskItems 需修改的风险项id列表
     * @return 删除结果
     */
    //    @UserLoginToken
    @PostMapping("/deleteRiskItems")
    public Result deleteRiskItems(@RequestBody List<String> riskItems) {
        return riskManageService.deleteRiskItem(riskItems);
    }

//    /**
//     * 风险分值登记
//     *
//     * @param riskScoreVo 风险分值实体
//     * @return 登记结果
//     */
//    //    @UserLoginToken
//    @PostMapping("/riskScoreRegister")
//    public Result riskScoreRegister(@RequestBody RiskScoreVo riskScoreVo) {
//        Result result = riskManageService.riskScoreRegister(riskScoreVo);
//        if (ObjectUtils.isEmpty(result)) {
//            result = ResultUtils.warn(UserManageResultCode.DATABASE_OPERATION_FAIL,"数据库操作失败");
//        }
//        return result;
//    }
//
//    /**
//     * 风险追踪负责人指派
//     *
//     * @param riskTrackUserAssignVo 风险追踪负责人指派信息
//     * @return 指派结果
//     */
//    //    @UserLoginToken
//    @PostMapping("/riskTrackUserAssign")
//    public Result riskTrackUserAssign(@RequestBody RiskTrackUserAssignVo riskTrackUserAssignVo) {
//        Result result = riskManageService.riskTrackUserAssign(riskTrackUserAssignVo);
//        if (ObjectUtils.isEmpty(result)) {
//            result = ResultUtils.warn(UserManageResultCode.DATABASE_OPERATION_FAIL,"数据库操作失败");
//        }
//        return result;
//    }
//
//    /**
//     * 风险应对方案提交
//     *
//     * @param riskResponsePlanVo 风险应对方案信息
//     * @return 提交结果
//     */
//    //    @UserLoginToken
//    @PostMapping("/riskResponsePlanCommit")
//    public Result riskResponsePlanCommit(@RequestBody RiskResponsePlanVo riskResponsePlanVo) {
//        Result result = riskManageService.riskResponsePlanCommit(riskResponsePlanVo);
//        if (ObjectUtils.isEmpty(result)) {
//            result = ResultUtils.warn(UserManageResultCode.DATABASE_OPERATION_FAIL,"数据库操作失败");
//        }
//        return result;
//    }
//
//    /**
//     * 风险处理进度更新
//     *
//     * @param riskProcessinProgressVo 风险处理进度信息
//     * @return 更新结果
//     */
//    //    @UserLoginToken
//    @PostMapping("/riskProcessinProgressUpdate")
//    public Result riskProcessinProgressUpdate(@RequestBody RiskProcessinProgressVo riskProcessinProgressVo) {
//        Result result = riskManageService.riskProcessinProgressUpdate(riskProcessinProgressVo);
//        if (ObjectUtils.isEmpty(result)) {
//            result = ResultUtils.warn(UserManageResultCode.DATABASE_OPERATION_FAIL,"数据库操作失败");
//        }
//        return result;
//    }
//
//    /**
//     * 风险项复检
//     *
//     * @param riskItemRecheckVo 风险项复检信息
//     * @return 更新结果
//     */
//    //    @UserLoginToken
//    @PostMapping("/riskItemRecheck")
//    public Result riskItemRecheck(@RequestBody RiskItemRecheckVo riskItemRecheckVo) {
//        Result result = riskManageService.riskItemRecheck(riskItemRecheckVo);
//        if (ObjectUtils.isEmpty(result)) {
//            result = ResultUtils.warn(UserManageResultCode.DATABASE_OPERATION_FAIL,"数据库操作失败");
//        }
//        return result;
//    }

    /**
     * 更新风险现状、趋势分析数据
     *
     * @return 更新结果
     */
    //    @UserLoginToken
    @GetMapping("/updateRiskAnalysisData")
    public Result updateRiskAnalysisData() {
        return riskManageService.updateAnalysisData();
    }

    /**
     * 保存风险分析报告数据
     *
     * @return 保存结果
     */
    //    @UserLoginToken
    @GetMapping("/saveRiskAnalysisData")
    public Result saveRiskAnalysisData() {
        return riskManageService.saveAnalysisReportData();
    }

    /**
     * 获取风险现状分析数据
     *
     * @return Result<List<RiskCurrentAnalysisDto>> 风险现状分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryRiskCurrentData")
    public Result queryRiskCurrentData() {
        List<RiskCurrentAnalysisDto> riskCurrentAnalysisDtoList = riskManageService.getRiskCurrentAnalysisData();
        return ResultUtils.success(riskCurrentAnalysisDtoList);
    }

    /**
     * 分页查询风险趋势分析数据
     *
     * @param queryCondition 查询条件
     * @return 风险趋势分析数据分页列表
     */
    //    @UserLoginToken
    @PostMapping("/pageQueryRiskTrendData")
    public Result pageQueryRiskTrendData(@RequestBody QueryCondition<RiskTrendAnalysisVo> queryCondition) {
        PageBean pageBean = riskManageService.getRiskTrendAnalysisData(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 查询风险趋势分析图表数据
     *
     * @param trendAnalysisVo 查询条件
     * @return Result<List<RiskTrendAnalysis>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @PostMapping("/queryRiskTrendChartData")
    public Result queryRiskTrendChartData(@RequestBody RiskTrendAnalysisVo trendAnalysisVo) {
        List<RiskTrendAnalysis> riskTrendAnalyses = riskManageService.getRiskTrendChartData(trendAnalysisVo);
        return ResultUtils.success(riskTrendAnalyses);
    }

    /**
     * 查询本月风险趋势分析数据
     *
     * @return Result<List<RiskTrendAnalysis>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryRiskTrendMonthData")
    public Result queryRiskTrendMonthData() {
        List<RiskTrendAnalysis> riskTrendAnalyses = riskManageService.getRiskTrendMonthData();
        return ResultUtils.success(riskTrendAnalyses);
    }

    /**
     * 查询下月风险趋势分析数据
     *
     * @return Result<List<RiskTrendAnalysis>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryRiskTrendNextMonthData")
    public Result queryRiskTrendNextMonthData() {
        List<RiskTrendAnalysis> riskTrendAnalyses = riskManageService.getRiskTrendNextMonthData();
        return ResultUtils.success(riskTrendAnalyses);
    }

    /**
     * 查询最近一年风险趋势分析数据（包含预测数据）
     *
     * @return Result<List<RiskTrendAnalysis>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryRiskTrendYearData")
    public Result queryRiskTrendYearData() {
        List<RiskTrendAnalysis> riskTrendAnalyses = riskManageService.getYearTrendChartData();
        return ResultUtils.success(riskTrendAnalyses);
    }

    /**
     * 查询本月风险等级趋势分析数据
     *
     * @return Result<List<RiskLevelTrend>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryLevelTrendMonthData")
    public Result queryLevelTrendMonthData() {
        List<RiskLevelTrend> riskLevelTrends = riskManageService.getRiskLevelTrendMonthData();
        return ResultUtils.success(riskLevelTrends);
    }

    /**
     * 查询下月风险等级趋势分析数据
     *
     * @return Result<List<RiskLevelTrend>> 风险趋势分析数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryLevelTrendNextMonthData")
    public Result queryLevelTrendNextMonthData() {
        List<RiskLevelTrend> riskLevelTrends = riskManageService.getRiskLevelTrendNextMonthData();
        return ResultUtils.success(riskLevelTrends);
    }

    /**
     * 更新风险现状分析数据
     *
     * @param currentAnalysis 需修改的数据
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyRiskCurrentData")
    public Result modifyRiskCurrentData(@RequestBody RiskCurrentAnalysis currentAnalysis) {
        return riskManageService.modifyRiskCurrentData(currentAnalysis);
    }

    /**
     * 更新风险趋势报告数据
     *
     * @param trendAnalysis 需修改的数据
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyRiskTrendData")
    public Result modifyRiskTrendData(@RequestBody RiskTrendAnalysis trendAnalysis) {
        return riskManageService.modifyRiskTrendData(trendAnalysis);
    }

    /**
     * 更新风险等级趋势分析数据
     *
     * @param levelTrendData 需修改的数据
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyRiskLevelTrendData")
    public Result modifyRiskLevelTrendData(@RequestBody RiskLevelTrend levelTrendData) {
        return riskManageService.modifyRiskLevelTrendData(levelTrendData);
    }

    /**
     * 根据id获取现状分析数据
     *
     * @param dataId 数据id
     * @return Result<RiskCurrentAnalysis> 现状分析数据项
     */
//    @UserLoginToken
    @GetMapping("/queryCurrentDataById/{dataId}")
    public Result queryCurrentDataById(@PathVariable String dataId){
        RiskCurrentAnalysis currentAnalysis = riskManageService.getCurrentDataById(dataId);
        return ResultUtils.success(currentAnalysis);
    }

    /**
     * 根据id获取趋势分析数据
     *
     * @param dataId 数据id
     * @return Result<RiskTrendAnalysis> 趋势分析数据项
     */
//    @UserLoginToken
    @GetMapping("/queryTrendDataById/{dataId}")
    public Result queryTrendDataById(@PathVariable String dataId){
        RiskTrendAnalysis trendAnalysis = riskManageService.getTrendDataById(dataId);
        return ResultUtils.success(trendAnalysis);
    }

    /**
     * 根据id获取风险等级趋势分析数据
     *
     * @param dataId 数据id
     * @return Result<RiskCurrentAnalysis> 风险等级趋势分析数据项
     */
//    @UserLoginToken
    @GetMapping("/queryLevelTrendDataById/{dataId}")
    public Result queryLevelTrendDataById(@PathVariable String dataId){
        RiskLevelTrend levelTrendData = riskManageService.getLevelTrendDataById(dataId);
        return ResultUtils.success(levelTrendData);
    }

    @GetMapping("/updateRiskTimeoutTime")
    public Result updateRiskTimeoutTime() {
        riskManageService.updateRiskTimeoutTime();
        return ResultUtils.success();
    }

    /**
     * 根据月份获取风险现状分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 风险现状分析报告数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryCurrentReportData/{monthTime}")
    public Result queryCurrentReportData(@PathVariable Long monthTime) {
        List<RiskCurrentAnalysisDto> riskCurrentAnalysisDtoList = riskManageService.getCurrentAnalysisReportData(monthTime);
        return ResultUtils.success(riskCurrentAnalysisDtoList);
    }

    /**
     * 根据月份获取风险趋势分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 风险趋势分析报告数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryTrendReportData/{monthTime}")
    public Result queryTrendReportData(@PathVariable Long monthTime) {
        List<RiskTrendAnalysisReport> trendAnalysisReports = riskManageService.getTrendAnalysisReportData(monthTime);
        return ResultUtils.success(trendAnalysisReports);
    }

    /**
     * 根据月份获取当月现状趋势分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 当月现状趋势分析报告数据
     */
    //    @UserLoginToken
    @GetMapping("/queryMonthTrendReportData/{monthTime}")
    public Result queryMonthTrendReportData(@PathVariable Long monthTime) {
        List<RiskTrendAnalysisReport> trendAnalysisReports = riskManageService.getMonthTrendAnalysisReportData(monthTime);
        return ResultUtils.success(trendAnalysisReports);
    }

    /**
     * 根据月份获取下月趋势预测分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 下月趋势预测分析报告数据
     */
    //    @UserLoginToken
    @GetMapping("/queryNextMonthTrendReportData/{monthTime}")
    public Result queryNextMonthTrendReportData(@PathVariable Long monthTime) {
        List<RiskTrendAnalysisReport> trendAnalysisReports = riskManageService.getNextMonthTrendAnalysisReportData(monthTime);
        return ResultUtils.success(trendAnalysisReports);
    }

    /**
     * 根据月份获取风险等级趋势分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 风险趋势分析报告数据列表
     */
    //    @UserLoginToken
    @GetMapping("/queryLevelTrendReportData/{monthTime}")
    public Result queryLevelTrendReportData(@PathVariable Long monthTime) {
        List<RiskLevelTrendReport> levelTrendReports = riskManageService.getLevelTrendAnalysisReportData(monthTime);
        return ResultUtils.success(levelTrendReports);
    }

    /**
     * 根据月份获取下月风险等级趋势分析报告数据
     *
     * @param monthTime 选择的月份时间
     * @return Result<List<RiskCurrentAnalysisDto>> 下月风险等级趋势分析报告数据
     */
    //    @UserLoginToken
    @GetMapping("/queryNextMonthLevelTrendReportData/{monthTime}")
    public Result queryNextMonthLevelTrendReportData(@PathVariable Long monthTime) {
        List<RiskLevelTrendReport> levelTrendReports = riskManageService.getNextMonthLevelTrendReportData(monthTime);
        return ResultUtils.success(levelTrendReports);
    }
}
