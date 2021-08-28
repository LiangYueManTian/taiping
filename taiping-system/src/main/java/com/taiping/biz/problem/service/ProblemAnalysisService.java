package com.taiping.biz.problem.service;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.problem.FlashOff;
import com.taiping.entity.problem.ProblemReport;
import com.taiping.entity.problem.TroubleTicket;
import org.springframework.web.multipart.MultipartFile;

/**
 * 问题分析服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
public interface ProblemAnalysisService {
    /**
     * 导入故障单Excel表
     * @param file Excel表
     * @return Result
     */
    Result importTroubleTicket(MultipartFile file);

    /**
     * 导入停水停电记录Excel表
     * @param file Excel表
     * @return Result
     */
    Result importFlashOff(MultipartFile file);

    /**
     * 查询故障单列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectTroubleTicketList(QueryCondition<TroubleTicket> queryCondition);

    /**
     * 查询停水停电记录列表
     * @param queryCondition 查询条件
     * @return Result
     */
    Result selectFlashOffList(QueryCondition<FlashOff> queryCondition);

    /**
     * 新增故障单
     * @param troubleTicket 故障单
     * @return Result
     */
    Result createTroubleTicket(TroubleTicket troubleTicket);

    /**
     * 查询故障单号是否重复
     * @param troubleTicket 故障单号
     * @return Result
     */
    Result queryTicketCode(TroubleTicket troubleTicket);

    /**
     * 根据ID查询故障单详情
     * @param ticketId 故障单ID
     * @return Result
     */
    Result queryTroubleTicket(String ticketId);
    /**
     * 修改故障单
     * @param troubleTicket 故障单
     * @return Result
     */
    Result updateTroubleTicket(TroubleTicket troubleTicket);

    /**
     * 删除故障单
     * @param ticketId 故障单ID
     * @return Result
     */
    Result deleteTroubleTicket(String ticketId);

    /**
     * 生成分析报告
     * @return Result
     */
    Result createReport();

    /**
     * 查询分析报告
     * @param report 分析报告
     * @return Result
     */
    Result requestReport(ProblemReport report);

    /**
     * 分析数据
     * @return Result
     */
    Result analysisProblem();
}
