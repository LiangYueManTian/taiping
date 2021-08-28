package com.taiping.biz.productivity.controller;

import com.taiping.biz.productivity.service.ProductivityAnalysisService;
import com.taiping.constant.productivity.ProductivityResultCode;
import com.taiping.constant.productivity.ProductivityResultMsg;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.productivity.*;
import com.taiping.utils.ResultUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 生产力分析控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@RestController
@RequestMapping("/taiping/productivity")
public class ProductivityAnalysisController {

    @Autowired
    private ProductivityAnalysisService productivityAnalysisService;

    /**
     * 导入常白班排班和倒班排班Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importSchedule")
    public Result importSchedule(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.importSchedule(file);
    }

    /**
     * 导入巡检工单数据Excel表
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importInspectionTask")
    public Result importInspectionTask(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.importInspectionTask(file);
    }

    /**
     * 导入变更单数据Excel表
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importChangeOrder")
    public Result importChangeOrder(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.importChangeOrder(file);
    }

    /**
     * 查询列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectScheduleList")
    public Result selectScheduleList(@RequestBody QueryCondition<Schedule> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectScheduleList(queryCondition);
    }

    /**
     * 查询列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectInspectionTaskList")
    public Result selectInspectionTaskList(@RequestBody QueryCondition<InspectionTask> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectInspectionTaskList(queryCondition);
    }

    /**
     * 查询变更单列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectChangeList")
    public Result selectChangeList(@RequestBody QueryCondition<Change> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectChangeList(queryCondition);
    }

    /**
     * 查询执行人
     * @return Result
     */
    @GetMapping("/selectExecutorList")
    public Result selectExecutorList() {
        return productivityAnalysisService.selectExecutorList();
    }

    /**
     * 新增未来变更计划
     * @param changePlan 未来变更计划
     * @return Result
     */
    @PostMapping("/insertChangePlan")
    public Result insertChangePlan(@RequestBody ChangePlan changePlan) {
        if (changePlan == null || !changePlan.check()) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.insertChangePlan(changePlan);
    }
    /**
     * 修改未来变更计划
     * @param changePlan 未来变更计划
     * @return Result
     */
    @PostMapping("/updateChangePlan")
    public Result updateChangePlan(@RequestBody ChangePlan changePlan) {
        if (changePlan == null || !changePlan.check()) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.updateChangePlan(changePlan);
    }

    /**
     * 删除未来变更计划
     * @param changeIdList 未来变更计划ID
     * @return Result
     */
    @PostMapping("/deleteChangePlan")
    public Result deleteChangePlan(@RequestBody List<String> changeIdList){
        if (CollectionUtils.isEmpty(changeIdList)) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.deleteChangePlan(changeIdList);
    }

    /**
     * 根据名称查询未来变更计划
     * @param changePlan 未来变更计划
     * @return  List<String> 未来变更计划ID
     */
    @PostMapping("/selectChangePlanForName")
    public Result selectChangePlanForName(@RequestBody ChangePlan changePlan){
        if (changePlan == null || StringUtils.isEmpty(changePlan.getProjectName())) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectChangePlanForName(changePlan);
    }

    /**
     * 根据ID查询未来变更计划
     * @param changeId ID
     * @return Result
     */
    @GetMapping("/selectChangePlanById/{changeId}")
    public Result selectChangePlanById(@PathVariable String changeId){
        if (StringUtils.isEmpty(changeId)) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectChangePlanById(changeId);
    }
    /**
     * 查询列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectChangePlanList")
    public Result selectChangePlanList(@RequestBody QueryCondition<ChangePlan> queryCondition){
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectChangePlanList(queryCondition);
    }

    /**
     * 查询个人KPI列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectPerformanceList")
    public Result selectPerformanceList(@RequestBody QueryCondition<Performance> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectPerformanceList(queryCondition);
    }

    /**
     * 查询人姓名
     * @return Result
     */
    @GetMapping("/selectPersonList")
    public Result selectPersonList() {
        return productivityAnalysisService.selectPersonList();
    }

    /**
     * 查询团队负荷列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectDailyWorkloadList")
    public Result selectDailyWorkloadList(@RequestBody QueryCondition<DailyWorkload> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.selectDailyWorkloadList(queryCondition);
    }

    /**
     * 分析数据
     * @return Result
     */
    @GetMapping("/analysisData")
    public Result analysisData() {
        return productivityAnalysisService.analysisData();
    }
    /**
     * 生成分析报告
     * @return Result
     */
    @GetMapping("/createReport")
    public Result createReport() {
        return productivityAnalysisService.createReport();
    }
    /**
     * 查询分析报告
     * @param monthWorkload 查询时间
     * @return Result
     */
    @PostMapping("/queryReport")
    public Result queryReport(@RequestBody MonthWorkload monthWorkload) {
        if (monthWorkload == null) {
            return ResultUtils.warn(ProductivityResultCode.PARAM_ERROR,
                    ProductivityResultMsg.PARAM_ERROR);
        }
        return productivityAnalysisService.queryReport(monthWorkload);
    }


    @GetMapping("/test")
    public void test() {
        productivityAnalysisService.test();
    }
}
