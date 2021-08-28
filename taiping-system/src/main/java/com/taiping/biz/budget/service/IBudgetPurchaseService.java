package com.taiping.biz.budget.service;

import com.taiping.biz.budget.dto.BudgetPurchaseDto;
import com.taiping.biz.budget.dto.BudgetPurchaseTableDto;
import com.taiping.biz.budget.dto.TBudgetPurchaseItemDto;
import com.taiping.biz.budget.dto.TBudgetPurchaseTlDto;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.budget.TBudget;
import com.taiping.entity.budget.TBudgetPurchase;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.manage.ManageActivity;

import java.util.List;

public interface IBudgetPurchaseService {

    /**
     * 新增预算采购
     *
     * @param dto
     * @return
     */
    Result add(BudgetPurchaseDto dto);

    /**
     * 更新预算采购
     *
     * @param entity
     */
    Result updatePurchase(TBudgetPurchase entity);

    /**
     * 更新采购审批项
     *
     * @param dto
     * @return
     */
    Result<?> updateItem(TBudgetPurchaseItemDto dto);

    /**
     * 按年份获取采购方式表格数据
     *
     * @param year
     * @return
     */
    Result<BudgetPurchaseTableDto> getPurchaseTable(String year);

    /**
     * 根据用户ID获取用户预算总数
     *
     * @param userId
     * @return
     */
    Result<Integer> getPurchaseCountByUser(String userId);

    /**
     * 根据用户ID获取用户预算列表
     *
     * @param userId
     * @return
     */
    Result<List<BudgetPurchaseDto>> getPurchaseListByUser(String userId);

    /**
     * 获取采购年份列表
     *
     * @return
     */
    Result<List<String>> getYearList();

    /**
     * 个人工作台获取预算列表
     *
     * @param queryCondition
     * @return
     */
    Result<PageBean> getPurchaseByPerson(QueryCondition<BudgetPurchaseDto> queryCondition);

    /**
     * 获取审批模板
     */
    List<TBudgetPurchaseTlDto> getTemplate();

    /**
     * 根据id获取审批模板
     *
     * @param templateId 审批模板id
     * @return 审批模板信息
     */
    TBudgetPurchaseTlDto getTemplateById(String templateId);

    /**
     * 修改审批模板
     *
     * @param template 审批模板
     * @return 修改结果
     */
    Result modifyTemplate(TBudgetPurchaseTlDto template);

    /**
     * 批量删除审批模板
     *
     * @param templateIds 审批模板id列表
     * @return 删除结果
     */
    Result deleteTemplate(List<String> templateIds);

    /**
     * 采购分析
     *
     * @return 运维管理活动列表
     */
    List<ManageActivity> purchaseAnalysis();

    /**
     * 根据相关运维活动id查询采购项目
     *
     * @param activityId 相关运维活动id
     * @return 维保计划信息
     */
    BudgetPurchaseDto getPurchaseItemByActivityId(String activityId);
}
