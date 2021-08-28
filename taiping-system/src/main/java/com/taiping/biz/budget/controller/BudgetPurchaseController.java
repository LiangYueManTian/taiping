package com.taiping.biz.budget.controller;

import com.taiping.biz.budget.consts.BudgetConst;
import com.taiping.biz.budget.dto.*;
import com.taiping.biz.budget.service.IBudgetPurchaseService;
import com.taiping.entity.Result;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 预算采购
 */
@RestController
@RequestMapping("/taiping/budget/purchase")
public class BudgetPurchaseController {
    @Autowired
    private IBudgetPurchaseService budgetPurchaseServiceImpl;

    /**
     * 新增预算采购
     *
     * @param dto
     * @return
     */
    @PostMapping("add")
    public Result<?> add(@RequestBody BudgetPurchaseDto dto) {
        return this.budgetPurchaseServiceImpl.add(dto);
    }

    /**
     * 更新预算采购
     *
     * @param dto
     * @return
     */
    @PostMapping("updatePurchase")
    public Result<?> updatePurchase(@RequestBody BudgetPurchaseDto dto) {
        return this.budgetPurchaseServiceImpl.updatePurchase(dto);
    }

    /**
     * 更新采购审批项
     *
     * @param dto
     * @return
     */
    @PostMapping("updateItem")
    public Result<?> updateItem(@RequestBody TBudgetPurchaseItemDto dto) {
        return this.budgetPurchaseServiceImpl.updateItem(dto);
    }

    /**
     * 按年份获取采购方式表格数据
     *
     * @param year
     * @return
     */
    @GetMapping("getPurchaseTable/{year}")
    public Result<BudgetPurchaseTableDto> getPurchaseTable(@PathVariable String year) {
        return this.budgetPurchaseServiceImpl.getPurchaseTable(year);
    }

    /**
     * 根据用户ID获取用户预算总数
     *
     * @param userId
     * @return
     */
    @GetMapping("getPurchaseCountByUser/{userId}")
    public Result<Integer> getPurchaseCountByUser(@PathVariable String userId) {
        return budgetPurchaseServiceImpl.getPurchaseCountByUser(userId);
    }

    /**
     * 根据用户ID获取用户预算列表
     *
     * @param userId
     * @return
     */
    @GetMapping("getPurchaseListByUser/{userId}")
    public Result<List<BudgetPurchaseDto>> getPurchaseListByUser(@PathVariable String userId) {
        return budgetPurchaseServiceImpl.getPurchaseListByUser(userId);
    }

    /**
     * 获取采购年份列表
     *
     * @return
     */
    @GetMapping("getYearList")
    public Result<List<String>> getYearList() {
        return budgetPurchaseServiceImpl.getYearList();
    }

    /**
     * 获取审批项状态选项
     *
     * @return
     */
    @GetMapping("getPurchaseItemStatusList")
    public Result<List<PurchaseItemStatusDto>> getPurchaseItemStatusList() {
        List<PurchaseItemStatusDto> list = new ArrayList<>();
        list.add(PurchaseItemStatusDto.newInstance(BudgetConst.PURCHASE_ITEM_STATUS_KEY0, "未开始"));
        list.add(PurchaseItemStatusDto.newInstance(BudgetConst.PURCHASE_ITEM_STATUS_KEY5, "进行中"));
        list.add(PurchaseItemStatusDto.newInstance(BudgetConst.PURCHASE_ITEM_STATUS_KEY99, "已完成"));

        return ResultUtils.success(list);
    }

    /**
     * 获取审批模板
     */
    @GetMapping("/getTemplate")
    public Result getTemplate() {
        return ResultUtils.success(budgetPurchaseServiceImpl.getTemplate());
    }

    /**
     * 根基id获取审批模板
     *
     * @param templateId 审批模板id
     * @return 审批模板信息
     */
    @GetMapping("/getTemplateById/{templateId}")
    public Result getTemplateById(@PathVariable String templateId) {
        return ResultUtils.success(budgetPurchaseServiceImpl.getTemplateById(templateId));
    }

    /**
     * 修改审批模板
     *
     * @param template 审批模板
     * @return 修改结果
     */
    @PostMapping("/modifyTemplate")
    public Result modifyTemplate(@RequestBody TBudgetPurchaseTlDto template) {
       return budgetPurchaseServiceImpl.modifyTemplate(template);
    }

    /**
     * 删除审批模板
     */
    @PostMapping("/deleteTemplate")
    public Result deleteTemplate(@RequestBody List<String> templateIds) {
        return budgetPurchaseServiceImpl.deleteTemplate(templateIds);
    }
}
