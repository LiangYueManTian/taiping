package com.taiping.biz.operation.service;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.operation.CommunicateIssues;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 运行情况分析服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
public interface OperationAnalysisService {
    /**
     * 查询健康卡
     *
     * @return Result
     */
    Result queryHealthyParam();

    /**
     * 导入数据
     *
     * @param file
     * @return
     */
    Result importPowerData(MultipartFile file);

    /**
     * 查询运行情况-配电系统
     *
     * @param month 月份
     * @param year  年份
     * @return Result
     */
    Result queryOperationPower(Integer month, Integer year);

    /**
     * 查询事项沟通
     *
     * @param queryCondition
     * @return
     */
    Result queryCommunicateData(QueryCondition<CommunicateIssues> queryCondition);

    /**
     * 新增沟通事项
     *
     * @param communicateIssues
     * @return
     */
    Result saveCommunicateData(CommunicateIssues communicateIssues);

    /**
     * 删除沟通事项
     *
     * @param id
     * @return
     */
    Result deleteCommunicateById(List<String> id);

    /**
     * 修改沟通事项
     *
     * @param communicateIssues
     * @return
     */
    Result updateCommunicateIssues(CommunicateIssues communicateIssues);

    /**
     * 根据id 查询重要事项沟通
     * @param issuesId
     * @return
     */
    Result queryCommunicateIssuesById(String issuesId);
}
