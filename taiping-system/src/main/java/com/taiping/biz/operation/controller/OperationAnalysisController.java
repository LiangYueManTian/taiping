package com.taiping.biz.operation.controller;

import com.google.common.collect.Lists;
import com.taiping.bean.operation.OperationPowerBean;
import com.taiping.biz.operation.service.OperationAnalysisService;
import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.operation.CommunicateIssues;
import com.taiping.exception.BizException;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 运行情况分析控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
@RequestMapping("/taiping/operation")
@RestController
public class OperationAnalysisController {

    @Autowired
    private OperationAnalysisService operationAnalysisService;

    /**
     * 查询健康卡
     *
     * @return Result
     */
    @GetMapping("/queryHealthyParam")
    public Result queryHealthyParam() {
        return operationAnalysisService.queryHealthyParam();
    }


    /**
     * 导入健康卡
     *
     * @return
     */
    @PostMapping("/importPowerData")
    public Result importPowerData(@RequestBody MultipartFile file) {
        return operationAnalysisService.importPowerData(file);
    }

    /**
     * 查询配电系统 数据
     *
     * @param bean
     * @return
     */
    @PostMapping("/queryPowerData")
    public Result queryPowerData(@RequestBody OperationPowerBean bean) {
        if (bean == null || bean.getMonth() == null || bean.getYear() == null) {
            throw new BizException(OperationResultCode.PARAM_ERROR, OperationResultMsg.PARAM_ERROR);
        }
        return operationAnalysisService.queryOperationPower(bean.getMonth(), bean.getYear());
    }


    @PostMapping("/queryCommunicateData")
    public Result queryCommunicateData(@RequestBody QueryCondition<CommunicateIssues> queryCondition) {
        return operationAnalysisService.queryCommunicateData(queryCondition);
    }

    @PostMapping("/saveCommunicateData")
    public Result saveCommunicateData(@RequestBody CommunicateIssues communicateIssues) {
        return operationAnalysisService.saveCommunicateData(communicateIssues);
    }

    @GetMapping("/deleteCommunicateById/{id}")
    public Result deleteCommunicateById(@PathVariable("id") String tId) {
        if (tId == null) {
            return ResultUtils.warn(1000000, "沟通事项参数不能为空！");
        }
        List<String> list = Lists.newArrayList();
        list.add(tId);
        return operationAnalysisService.deleteCommunicateById(list);
    }

    @PostMapping("/updateCommunicateIssues")
    public Result updateCommunicateIssues(@RequestBody CommunicateIssues communicateIssues) {
        return operationAnalysisService.updateCommunicateIssues(communicateIssues);
    }

    @GetMapping("/queryCommunicateIssuesById/{id}")
    public Result queryCommunicateIssuesById(@PathVariable("id") String tId) {
        return operationAnalysisService.queryCommunicateIssuesById(tId);
    }

}
