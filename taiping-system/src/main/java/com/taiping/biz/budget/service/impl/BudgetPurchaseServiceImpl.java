package com.taiping.biz.budget.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taiping.biz.budget.dao.IBudgetPurchaseDao;
import com.taiping.biz.budget.dto.*;
import com.taiping.biz.budget.service.IBudgetPurchaseService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.budget.BudgetPurchaseResultCode;
import com.taiping.constant.budget.BudgetPurchaseResultMsg;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.budget.TBudgetPurchase;
import com.taiping.entity.budget.TBudgetPurchaseItem;
import com.taiping.entity.budget.TBudgetPurchaseTl;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Date;
import java.util.*;

@Service
@Slf4j
public class BudgetPurchaseServiceImpl implements IBudgetPurchaseService {

    @Autowired
    private IBudgetPurchaseDao iBudgetPurchaseDao;
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 新增预算采购
     *
     * @param dto
     * @return
     */
    public Result add(BudgetPurchaseDto dto) {
        if (StringUtils.isBlank(dto.getPaymentPlan())) {
            throw new BizException(ResultCode.FAIL, "付款计划不能为空");
        }

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);

        // 新增采购
        String purchaseId = NineteenUUIDUtils.uuid();
        dto.setTid(purchaseId);
        dto.setPurchaseYear(String.valueOf(year));
        this.iBudgetPurchaseDao.add(dto);

        // 新增付款计划
        String[] plans = dto.getPaymentPlan().split(",");

        for (String plan : plans) {
            plan = plan.replaceAll("%", "");
            double ratio = Double.valueOf(plan) / 100;

            BudgetPurchasePlanDto planVo = new BudgetPurchasePlanDto();
            planVo.setTid(NineteenUUIDUtils.uuid());
            planVo.setPurchaseId(purchaseId);
            planVo.setPaymentAmount(dto.getDealAmount() * ratio);
            planVo.setPaymentYear(String.valueOf(year++));

            this.iBudgetPurchaseDao.addPurchasePlan(planVo);
        }

        Date authStartDate = dto.getPlanStartDate();

        // 新增采购审批项
        List<TBudgetPurchaseTlDto> tls = iBudgetPurchaseDao.getTemplate();
        for (TBudgetPurchaseTlDto item : tls) {
            TBudgetPurchaseItem vo = new TBudgetPurchaseItem();
            vo.setPlanStartDate(authStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(authStartDate);
            calendar.add(Calendar.DATE, item.getPlanNeedTime());
            authStartDate = new Date(calendar.getTime().getTime());
            vo.setPlanEndDate(authStartDate);
            vo.setTid(NineteenUUIDUtils.uuid());
            vo.setTemplateId(item.getTid());
            vo.setPurchaseId(purchaseId);
            vo.setStatus("0");
            this.iBudgetPurchaseDao.addPurchaseItem(vo);

        }

        return ResultUtils.success();
    }

    /**
     * 更新预算采购
     *
     * @param entity
     */
    @Override
    public Result updatePurchase(TBudgetPurchase entity) {
        iBudgetPurchaseDao.updatePurchase(entity);
        return ResultUtils.success();
    }

    /**
     * 更新采购审批项
     *
     * @param dto
     * @return
     */
    @Override
    public Result<?> updateItem(TBudgetPurchaseItemDto dto) {
        List<TBudgetPurchaseItem> items = iBudgetPurchaseDao.getPurchaseTableItem(dto.getPurchaseId());
        List<TBudgetPurchaseTlDto> templates = iBudgetPurchaseDao.getTemplate();
        Map<Long,TBudgetPurchaseItem> itemMap = Maps.newHashMap();
        for (TBudgetPurchaseItem item: items) {
            for (TBudgetPurchaseTlDto template: templates) {
                if (item.getTemplateId().equals(template.getTid())) {
                    itemMap.put(template.getIsOrder(),item);
                }
            }
        }
        TBudgetPurchaseTlDto currentTemplate = iBudgetPurchaseDao.getTemplateById(dto.getTemplateId());
        if (!ObjectUtils.isEmpty(dto.getActEndDate())) {
            Date authStartDate = dto.getActEndDate();
            for (TBudgetPurchaseTlDto temp: templates) {
                if (temp.getIsOrder() > currentTemplate.getIsOrder()) {
                    TBudgetPurchaseItem purchaseItem = itemMap.get(temp.getIsOrder());
                    purchaseItem.setPlanStartDate(authStartDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(authStartDate);
                    calendar.add(Calendar.DATE, temp.getPlanNeedTime());
                    authStartDate = new Date(calendar.getTime().getTime());
                    purchaseItem.setPlanEndDate(authStartDate);
                    iBudgetPurchaseDao.updateItem(purchaseItem);
                }
             }
        }
        iBudgetPurchaseDao.updateItem(dto);
        iBudgetPurchaseDao.updatePurchaseStatus(dto.getPurchaseId());
        return ResultUtils.success();
    }

    /**
     * 按年份获取采购方式表格数据
     *
     * @param year
     * @return
     */
    @Override
    public Result<BudgetPurchaseTableDto> getPurchaseTable(String year) {
        BudgetPurchaseTableDto dto = new BudgetPurchaseTableDto();
        List<TBudgetPurchaseTl> titles = iBudgetPurchaseDao.getPurchaseTableTemplate(year);
        List<BudgetPurchaseTableColDto> cols = iBudgetPurchaseDao.getPurchaseTable(year);

        for (BudgetPurchaseTableColDto col : cols) {
            List<TBudgetPurchaseItem> items = iBudgetPurchaseDao.getPurchaseTableItem(col.getTid());
            col.setTemplate(getTemplate(items, titles));
        }

        dto.setTitle(titles);
        dto.setColumns(cols);

        return ResultUtils.success(dto);
    }

    private List<TBudgetPurchaseItem> getTemplate(List<TBudgetPurchaseItem> items, List<TBudgetPurchaseTl> titles) {
        List<TBudgetPurchaseItem> template = new ArrayList<>();

        for (TBudgetPurchaseTl title : titles) {

            boolean isAdd = true;

            for (TBudgetPurchaseItem tl : items) {
                if (title.getTid().equals(tl.getTemplateId())) {
                    template.add(tl);
                    items.remove(tl);
                    isAdd = false;
                    break;
                }
            }

            if (isAdd) {
                template.add(null);
            }
        }

        return template;
    }

    /**
     * 根据用户ID获取用户预算总数
     *
     * @param userId
     * @return
     */
    @Override
    public Result<Integer> getPurchaseCountByUser(String userId) {
        Integer count = iBudgetPurchaseDao.getPurchaseCountByUser(userId);
        return ResultUtils.success(count);
    }

    /**
     * 根据用户ID获取用户预算列表
     *
     * @param userId
     * @return
     */
    @Override
    public Result<List<BudgetPurchaseDto>> getPurchaseListByUser(String userId) {
        List<BudgetPurchaseDto> list = iBudgetPurchaseDao.getPurchaseListByUser(userId);
        return ResultUtils.success(list);
    }

    /**
     * 个人工作台 分页处理
     *
     * @param queryCondition
     * @return
     */
    @Override
    public Result<PageBean> getPurchaseByPerson(QueryCondition<BudgetPurchaseDto> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "updateDate", "desc");
        //查询
        List<BudgetPurchaseDto> troubleTicketList = iBudgetPurchaseDao.getPurchaseByPerson(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = iBudgetPurchaseDao.getPurchaseByPersonCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, troubleTicketList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 获取审批模板
     */
    @Override
    public List<TBudgetPurchaseTlDto> getTemplate() {
        return iBudgetPurchaseDao.getTemplate();
    }

    /**
     * 根据id获取审批模板
     *
     * @param templateId 审批模板id
     * @return 审批模板信息
     */
    @Override
    public TBudgetPurchaseTlDto getTemplateById(String templateId) {
        return iBudgetPurchaseDao.getTemplateById(templateId);
    }

    /**
     * 修改审批模板
     *
     * @param template 审批模板
     */
    @Override
    public Result modifyTemplate(TBudgetPurchaseTlDto template) {
        try {
            iBudgetPurchaseDao.updateTemplate(template);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("modify template error", e);
            throw new BizException(BudgetPurchaseResultCode.DATABASE_OPERATION_FAIL,BudgetPurchaseResultMsg.DATABASE_OPERATION_FAIL);
        }

    }

    /**
     * 批量删除审批模板
     *
     * @param templateIds 审批模板id列表
     * @return 删除结果
     */
    @Override
    public Result deleteTemplate(List<String> templateIds) {
        try {
            iBudgetPurchaseDao.deleteTemplate(templateIds);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("delete template error", e);
            throw new BizException(BudgetPurchaseResultCode.DATABASE_OPERATION_FAIL,BudgetPurchaseResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 采购分析
     *
     * @return 运维管理活动列表
     */
    @Override
    public List<ManageActivity> purchaseAnalysis() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        List<BudgetPurchaseTableColDto> cols = iBudgetPurchaseDao.getPurchaseTable(year);
        List<ManageActivity> activities = Lists.newArrayList();
        for (BudgetPurchaseTableColDto dto : cols) {
            List<TBudgetPurchaseItem> items = iBudgetPurchaseDao.getPurchaseTableItem(dto.getTid());
            items.sort(Comparator.comparing(TBudgetPurchaseItem::getPlanStartDate));
            for (TBudgetPurchaseItem item : items) {
                if (!ObjectUtils.isEmpty(item.getActEndDate())) {
                    if (item.getActEndDate().after(item.getPlanEndDate())) {
                        ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),
                                dto.getProName(),
                                dto.getTid(),
                                System.currentTimeMillis(),
                                "采购任务未按时完成",
                                getTemplateById(item.getTemplateId()).getName() + "步骤未按时完成",
                                ManageStatusEnum.UN_REDUCE,
                                ManageSourceEnum.BUDGET,
                                System.currentTimeMillis());
                        activity.setSourceType(ManageSourceTypeEnum.PURCHASE_ANALYSIS.getCode());
                        activities.add(activity);
                        break;
                    }
                } else {
                    if (new Date(System.currentTimeMillis()).after(item.getPlanEndDate())) {
                        ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),
                                dto.getProName(),
                                dto.getTid(),
                                System.currentTimeMillis(),
                                "采购任务未按时完成",
                                getTemplateById(item.getTemplateId()).getName() + "步骤未按时完成",
                                ManageStatusEnum.UN_REDUCE,
                                ManageSourceEnum.BUDGET,
                                System.currentTimeMillis());
                        activity.setSourceType(ManageSourceTypeEnum.PURCHASE_ANALYSIS.getCode());
                        activities.add(activity);
                        break;
                    }
                }
            }
        }
        return activities;
    }

    /**
     * 根据相关运维活动id查询采购项目
     *
     * @param activityId 相关运维活动id
     * @return 维保计划信息
     */
    @Override
    public BudgetPurchaseDto getPurchaseItemByActivityId(String activityId) {
        return iBudgetPurchaseDao.getPurchaseItemByActivityId(activityId);
    }


    /**
     * 获取采购年份列表
     *
     * @return
     */
    @Override
    public Result<List<String>> getYearList() {
        List<String> list = iBudgetPurchaseDao.getYearList();
        return ResultUtils.success(list);
    }
}
