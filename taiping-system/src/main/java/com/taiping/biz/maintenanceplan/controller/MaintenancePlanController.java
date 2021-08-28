package com.taiping.biz.maintenanceplan.controller;

import com.taiping.bean.maintenance.TableHeader;
import com.taiping.biz.maintenanceplan.service.IAssetBasicInfoService;
import com.taiping.biz.maintenanceplan.service.IMaintenanceContractService;
import com.taiping.biz.maintenanceplan.service.IMaintenancePlanService;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.AssetBasicInfo;
import com.taiping.entity.maintenanceplan.MaintenanceContract;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.maintenanceplan.PlanExecuteSituation;
import com.taiping.exception.handler.BizExceptionHandler;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维护保养计划控制层
 */
@RestController
@RequestMapping("/taiping/maintenance")
public class MaintenancePlanController extends BizExceptionHandler {
    /**
     * 维护保养计划逻辑层
     */
    @Autowired
    private IMaintenancePlanService maintenancePlanService;
    /**
     * 资产基本信息逻辑层
     */
    @Autowired
    private IAssetBasicInfoService assetBasicInfoService;
    /**
     * 资产维保合同逻辑层
     */
    @Autowired
    private IMaintenanceContractService contractService;

    /**
     * 获取指定年份表头列表
     *
     * @return 表头信息
     */
    //    @UserLoginToken
    @GetMapping("/getTableHeaderByYear/{year}")
    public Result getTableHeaderByYear(@PathVariable Integer year) {
        List<TableHeader> headerList = maintenancePlanService.getTableHeaderByYear(year);
        return ResultUtils.success(headerList);
    }

    /**
     * 新建维护保养计划
     *
     * @param plan 需添加的维护保养计划
     * @return 添加结果
     */
//    @UserLoginToken
    @PutMapping("/addMaintenancePlan")
    public Result addMaintenancePlan(@RequestBody MaintenancePlan plan) {
        return maintenancePlanService.addMaintenancePlan(plan);
    }

    /**
     * 修改维护保养计划
     *
     * @param plan 需修改的维护保养计划
     * @return 修改结果
     */
//    @UserLoginToken
    @PostMapping("/modifyMaintenancePlan")
    public Result modifyMaintenancePlan(@RequestBody MaintenancePlan plan) {
        return maintenancePlanService.modifyMaintenancePlan(plan);
    }

    /**
     * 修改维护保养计划
     *
     * @param plan 需修改的维护保养计划
     * @return 修改结果
     */
//    @UserLoginToken
    @PostMapping("/pauseOrEnablePlan")
    public Result pauseOrEnablePlan(@RequestBody MaintenancePlan plan) {
        return maintenancePlanService.planPauseOrEnable(plan);
    }

    /**
     * 修改维护保养计划执行情况
     *
     * @param situation 需修改的维护保养计划执行情况
     * @return 修改结果
     */
//    @UserLoginToken
    @PostMapping("/modifyPlanSituation")
    public Result modifyPlanSituation(@RequestBody PlanExecuteSituation situation) {
        return maintenancePlanService.modifyPlanExecuteSituation(situation);
    }

    /**
     * 批量删除维护保养计划
     *
     * @param planIds 需修改的维护保养计划id列表
     * @return 删除结果
     */
    //    @UserLoginToken
    @PostMapping("/deleteMaintenancePlan")
    public Result deleteMaintenancePlan(@RequestBody List<String> planIds) {
        return maintenancePlanService.batchDeleteMaintenancePlan(planIds);
    }

    /**
     * 获取所有维护保养计划
     *
     * @return Result<List<MaintenancePlan>> 维护保养计划列表
     */
//    @UserLoginToken
    @GetMapping("/queryMaintenancePlanList")
    public Result queryMaintenancePlanList() {
        List<MaintenancePlan> planList = maintenancePlanService.getAllPlan();
        return ResultUtils.success(planList);
    }

    /**
     * 条件查询维护保养计划
     *
     * @param queryCondition 查询条件
     * @return 维护保养计划列表
     */
//    @UserLoginToken
    @PostMapping("/queryMaintenancePlanByCondition")
    private Result queryMaintenancePlanByCondition(@RequestBody QueryCondition<MaintenancePlan> queryCondition) {
        PageBean pageBean = maintenancePlanService.getMaintenancePlanByCondition(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 查询维保计划表格数据(无分页条件)
     *
     * @param queryCondition 查询条件
     * @return 维护保养计划列表
     */
//    @UserLoginToken
    @PostMapping("/queryMaintenancePlanTableData")
    private Result queryMaintenancePlanTableData(@RequestBody QueryCondition<MaintenancePlan> queryCondition) {
        List<MaintenancePlan> planList = maintenancePlanService.getMaintenancePlanTableData(queryCondition);
        return ResultUtils.success(planList);
    }

    /**
     * 根据id查询维护保养计划
     *
     * @param planId 维护保养计划id
     * @return Result<MaintenancePlan> 维护保养计划信息
     */
//    @UserLoginToken
    @GetMapping("/queryMaintenancePlanById/{planId}")
    public Result queryMaintenancePlanById(@PathVariable String planId) {
        MaintenancePlan plan = maintenancePlanService.getMaintenancePlanById(planId);
        return ResultUtils.success(plan);
    }

    /**
     * 根据相关运维活动id查询维保计划
     *
     * @param activityId 相关运维活动id
     * @return Result<MaintenancePlan> 维护保养计划信息
     */
//    @UserLoginToken
    @GetMapping("/queryMaintenancePlanByActivityId/{activityId}")
    public Result queryMaintenancePlanByActivityId(@PathVariable String activityId) {
        MaintenancePlan plan = maintenancePlanService.getMaintenancePlanByActivityId(activityId);
        return ResultUtils.success(plan);
    }


    /**
     * 添加资产基本信息
     *
     * @param basicInfo 需添加的资产基本信息
     * @return 添加结果
     */
//    @UserLoginToken
    @PutMapping("/addAssetBasicInfo")
    public Result addAssetBasicInfo(@RequestBody AssetBasicInfo basicInfo) {
        return assetBasicInfoService.addAssetBasicInfo(basicInfo);
    }

    /**
     * 修改资产基本信息
     *
     * @param basicInfo 需修改的资产基本信息
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyAssetBasicInfo")
    public Result modifyAssetBasicInfo(@RequestBody AssetBasicInfo basicInfo) {
        return assetBasicInfoService.modifyAssetBasicInfo(basicInfo);
    }

    /**
     * 批量删除资产基本信息
     *
     * @param basicInfoIds 需修改的资产基本信息id列表
     * @return 删除结果
     */
    //    @UserLoginToken
    @PostMapping("/deleteAssetBasicInfo")
    public Result deleteAssetBasicInfo(@RequestBody List<String> basicInfoIds) {
        return assetBasicInfoService.batchDeleteAssetBasicInfo(basicInfoIds);
    }

    /**
     * 获取所有资产基本信息
     *
     * @return Result<List<AssetBasicInfo>> 资产基本信息列表
     */
//    @UserLoginToken
    @GetMapping("/queryAssetBasicInfoList")
    public Result queryAssetBasicInfoList() {
        List<AssetBasicInfo> assetBasicInfoList = assetBasicInfoService.getAllAssetBasicInfo();
        return ResultUtils.success(assetBasicInfoList);
    }

    /**
     * 条件查询资产基本信息
     *
     * @param queryCondition 查询条件
     * @return 资产基本信息列表
     */
//    @UserLoginToken
    @PostMapping("/queryAssetBasicInfoByCondition")
    private Result queryAssetBasicInfoByCondition(@RequestBody QueryCondition<AssetBasicInfo> queryCondition) {
        PageBean pageBean = assetBasicInfoService.getAssetBasicInfoByCondition(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 根据id查询资产基本信息
     *
     * @param basicInfoId 资产基本信息id
     * @return Result<AssetBasicInfo> 资产基本信息
     */
//    @UserLoginToken
    @GetMapping("/queryAssetBasicInfoById/{basicInfoId}")
    public Result queryAssetBasicInfoById(@PathVariable String basicInfoId) {
        AssetBasicInfo basicInfo = assetBasicInfoService.getAssetBasicInfoById(basicInfoId);
        return ResultUtils.success(basicInfo);
    }


    /**
     * 添加资产维保合同
     *
     * @param contract 需添加的资产维保合同
     * @return 添加结果
     */
//    @UserLoginToken
    @PutMapping("/addMaintenanceContract")
    public Result addMaintenanceContract(@RequestBody MaintenanceContract contract) {
        return contractService.addMaintenanceContract(contract);
    }

    /**
     * 修改资产维保合同
     *
     * @param contract 需修改的资产维保合同
     * @return 修改结果
     */
    //    @UserLoginToken
    @PostMapping("/modifyMaintenanceContract")
    public Result modifyMaintenanceContract(@RequestBody MaintenanceContract contract) {
        return contractService.modifyMaintenanceContract(contract);
    }

    /**
     * 批量删除资产维保合同
     *
     * @param contractIds 需修改的资产维保合同id列表
     * @return 删除结果
     */
    //    @UserLoginToken
    @PostMapping("/deleteMaintenanceContract")
    public Result deleteMaintenanceContract(@RequestBody List<String> contractIds) {
        return contractService.batchDeleteMaintenanceContract(contractIds);
    }

    /**
     * 获取所有资产维保合同
     *
     * @return Result<List<MaintenanceContract>> 资产维保合同列表
     */
//    @UserLoginToken
    @GetMapping("/queryMaintenanceContractList")
    public Result queryMaintenanceContractList() {
        List<MaintenanceContract> contracts = contractService.getAllMaintenanceContract();
        return ResultUtils.success(contracts);
    }

    /**
     * 条件查询资产维保合同
     *
     * @param queryCondition 查询条件
     * @return 资产维保合同列表
     */
//    @UserLoginToken
    @PostMapping("/queryMaintenanceContractByCondition")
    private Result queryMaintenanceContractByCondition(@RequestBody QueryCondition<MaintenanceContract> queryCondition) {
        PageBean pageBean = contractService.getMaintenanceContractByCondition(queryCondition);
        return ResultUtils.success(pageBean);
    }

    /**
     * 根据id查询资产维保合同
     *
     * @param contractId 资产维保合同id
     * @return Result<MaintenanceContract> 资产维保合同信息
     */
//    @UserLoginToken
    @GetMapping("/queryMaintenanceContractById/{contractId}")
    public Result queryMaintenanceContractById(@PathVariable String contractId) {
        MaintenanceContract contract = contractService.getMaintenanceContractInfoById(contractId);
        return ResultUtils.success(contract);
    }

    /**
     * 获取所有维护保养计划
     *
     * @return Result<List<MaintenancePlan>> 维护保养计划列表
     */
//    @UserLoginToken
    @GetMapping("/updateDelaySituation")
    public Result updateDelaySituation() {
        maintenancePlanService.updateDelaySituation();
        return ResultUtils.success();
    }

    /**
     * 验证维保计划执行情况是否可修改
     *
     * @param executeTime 计划执行时间
     * @param period 计划执行周期
     * @return 验证结果
     */
//    @UserLoginToken
    @GetMapping("/checkEnableModify/{executeTime}/{period}")
    public Result checkEnableModify(@PathVariable Long executeTime,@PathVariable Integer period) {
        return maintenancePlanService.checkEnableModify(executeTime,period);
    }

    /**
     * 维护保养计划分析
     *
     * @return 分析结果
     */
    @GetMapping("/maintenancePlanAnalysis")
    public Result maintenancePlanAnalysis() {
        return maintenancePlanService.maintenancePlanAnalysis();
    }


    /**
     * 保存维护保养计划分析数据
     *
     * @return 保存结果
     */
    @GetMapping("/saveMaintenancePlanReportData")
    public Result saveMaintenancePlanReportData() {
        maintenancePlanService.saveMaintenancePlanAnalysisReportData();
        return ResultUtils.success();
    }

    /**
     * 根据月份获取维护保养计划分析报告数据
     *
     * @param monthTime 指定月时间
     * @return 维护保养计划分析报告数据
     */
    @GetMapping("/queryMaintenancePlanReportData/{monthTime}")
    public Result queryMaintenancePlanReportData(@PathVariable Long monthTime) {
        return ResultUtils.success(maintenancePlanService.geMaintenancePlanReportData(monthTime));
    }
}
