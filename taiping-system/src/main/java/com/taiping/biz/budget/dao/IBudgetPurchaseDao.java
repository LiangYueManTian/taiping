package com.taiping.biz.budget.dao;

import com.taiping.biz.budget.dto.BudgetPurchaseDto;
import com.taiping.biz.budget.dto.BudgetPurchaseTableColDto;
import com.taiping.biz.budget.dto.TBudgetPurchaseTlDto;
import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.Result;
import com.taiping.entity.SortCondition;
import com.taiping.entity.budget.*;
import com.taiping.entity.problem.TroubleTicket;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBudgetPurchaseDao {

    /**
     * 新增预算采购
     *
     * @param entity
     */
    void add(TBudgetPurchase entity);

    /**
     * 更新预算采购
     *
     * @param entity
     */
    void updatePurchase(TBudgetPurchase entity);

    /**
     * 新增采购付款计划
     *
     * @param entity
     */
    void addPurchasePlan(TBudgetPurchasePlan entity);

    /**
     * 新增采购审批项
     *
     * @param entity
     */
    void addPurchaseItem(TBudgetPurchaseItem entity);

    /**
     * 更新采购审批项
     *
     * @param entity
     */
    void updateItem(TBudgetPurchaseItem entity);

    /**
     * 获取审批模板
     */
    List<TBudgetPurchaseTlDto> getTemplate();

    /**
     * 根据id获取采购审批模板
     *
     * @param templateId 审批模板id
     * @return 审批模板信息
     */
    TBudgetPurchaseTlDto getTemplateById(String templateId);

    /**
     *更新审批模板
     *
     * @param template 审批模板
     * @return 更新条数
     */
    Integer updateTemplate(TBudgetPurchaseTlDto template);

    /**
     * 删除采购审批模板
     *
     * @param templateIds 采购审批模板id列表
     * @return 删除条数
     */
    Integer deleteTemplate(List<String> templateIds);

    /**
     * 根据年份获取采购
     */
    List<BudgetPurchaseDto> findPurchaseByYear(@Param("year") String year);

    /**
     * 获取年度采购所有审批模板项
     *
     * @param year
     * @return
     */
    List<TBudgetPurchaseTl> getPurchaseTableTemplate(@Param("year") String year);

    /**
     * 根据采购ID获取采购审批项
     *
     * @param purchaseId
     * @return
     */
    List<TBudgetPurchaseItem> getPurchaseTableItem(@Param("purchaseId") String purchaseId);

    /**
     * 获取采购方式表格
     *
     * @param year
     * @return
     */
    List<BudgetPurchaseTableColDto> getPurchaseTable(@Param("year") String year);

    /**
     * 根据用户ID获取用户预算总数
     *
     * @param userId
     * @return
     */
    Integer getPurchaseCountByUser(@Param("userId") String userId);

    /**
     * 根据用户ID获取用户预算列表
     *
     * @param userId
     * @return
     */
    List<BudgetPurchaseDto> getPurchaseListByUser(@Param("userId") String userId);

    /**
     * 更新采购审批状态
     *
     * @param purchaseId
     */
    void updatePurchaseStatus(@Param("purchaseId") String purchaseId);

    /**
     * 获取采购年份列表
     *
     * @return
     */
    List<String> getYearList();

    /**
     * 个人工作台获取预算信息
     *
     * @param pageCondition
     * @param filterConditionList
     * @param sortCondition
     * @return
     */
    List<BudgetPurchaseDto> getPurchaseByPerson(@Param("page") PageCondition pageCondition,
                                                @Param("filterList") List<FilterCondition> filterConditionList,
                                                @Param("sort") SortCondition sortCondition);

    /**
     * 查询故障单数量
     *
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer getPurchaseByPersonCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 保存采购分析报告数据
     *
     * @param data 采购分析报告数据
     * @return 添加条数
     */
    int savePurchaseAnalysisReportData(List<PurchaseAnalysisReport> data);

    /**
     * 根据月份获取采购分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 采购分析报告数据
     */
    List<PurchaseAnalysisReport> getPurchaseAnalysisDataByMonth(Long monthTime);

    /**
     * 批量删除采购分析报告数据
     *
     * @param oldReportData 采购分析报告数据
     * @return 删除条数
     */
    int batchDeletePurchaseAnalysisData(List<PurchaseAnalysisReport> oldReportData);

    /**
     * 根据相关运维活动id查询采购项目
     *
     * @param activityId 相关运维活动id
     * @return 维保计划信息
     */
    BudgetPurchaseDto getPurchaseItemByActivityId(String activityId);
}
