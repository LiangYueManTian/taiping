package com.taiping.biz.problem.controller;

import com.taiping.biz.problem.service.ProblemAnalysisService;
import com.taiping.constant.problem.ProblemResultCode;
import com.taiping.constant.problem.ProblemResultMsg;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.problem.FlashOff;
import com.taiping.entity.problem.ProblemReport;
import com.taiping.entity.problem.TroubleTicket;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 问题分析控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
@RestController
@RequestMapping("/taiping/problem")
public class ProblemAnalysisController {

    @Autowired
    private ProblemAnalysisService problemAnalysisService;

    /**
     * 导入故障单Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importTroubleTicket")
    public Result importTroubleTicket(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.importTroubleTicket(file);
    }

    /**
     * 导入供水供电Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importFlashOff")
    public Result importFlashOff(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.importFlashOff(file);
    }

    /**
     * 查询故障单列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectTroubleTicketList")
    public Result selectTroubleTicketList(@RequestBody QueryCondition<TroubleTicket> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.selectTroubleTicketList(queryCondition);
    }

    /**
     * 查询停水停电记录列表
     * @param queryCondition 查询条件
     * @return Result
     */
    @PostMapping("/selectFlashOffList")
    public Result selectFlashOffList(@RequestBody QueryCondition<FlashOff> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.selectFlashOffList(queryCondition);
    }

    /**
     * 新增故障单
     * @param troubleTicket 故障单
     * @return Result
     */
    @PostMapping("/createTroubleTicket")
    public Result createTroubleTicket(@RequestBody TroubleTicket troubleTicket) {

        return problemAnalysisService.createTroubleTicket(troubleTicket);
    }

    /**
     * 查询故障单号是否重复
     * @param troubleTicket 故障单号
     * @return Result
     */
    @GetMapping("/queryTicketCode")
    public Result queryTicketCode(@RequestBody TroubleTicket troubleTicket) {
        //校验参数
        if (troubleTicket == null || troubleTicket.getTicketCode() == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.queryTicketCode(troubleTicket);
    }

    /**
     * 根据ID查询故障单详情
     * @param ticketId 故障单ID
     * @return Result
     */
    @GetMapping("/queryTroubleTicket/{ticketId}")
    public Result queryTroubleTicket(@PathVariable String ticketId) {
        //校验参数
        if (ticketId == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.queryTroubleTicket(ticketId);
    }
    /**
     * 修改故障单
     * @param troubleTicket 故障单
     * @return Result
     */
    @PostMapping("/updateTroubleTicket")
    public Result updateTroubleTicket(@RequestBody TroubleTicket troubleTicket) {

        return problemAnalysisService.updateTroubleTicket(troubleTicket);
    }

    /**
     * 删除故障单
     * @param ticketId 故障单ID
     * @return Result
     */
    @GetMapping("/deleteTroubleTicket/{ticketId}")
    public Result deleteTroubleTicket(@PathVariable String ticketId) {
        //校验参数
        if (ticketId == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.deleteTroubleTicket(ticketId);
    }

    /**
     * 生成分析报告
     * @return Result
     */
    @GetMapping("/createReport")
    public Result createReport() {
        return problemAnalysisService.createReport();
    }

    /**
     * 查询分析报告
     * @param report 分析报告
     * @return Result
     */
    @PostMapping("/requestReport")
    public Result requestReport(@RequestBody ProblemReport report) {
        if (report == null) {
            return ResultUtils.warn(ProblemResultCode.PARAM_ERROR,
                    ProblemResultMsg.PARAM_ERROR);
        }
        return problemAnalysisService.requestReport(report);
    }

    /**
     * 分析数据
     * @return Result
     */
    @GetMapping("/analysisProblem")
    public Result analysisProblem() {
        return problemAnalysisService.analysisProblem();
    }
}
