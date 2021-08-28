package com.taiping.biz.manage.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.biz.budget.dto.BudgetPurchaseDto;
import com.taiping.biz.budget.service.impl.BudgetPurchaseServiceImpl;
import com.taiping.biz.maintenanceplan.service.IMaintenancePlanService;
import com.taiping.biz.manage.dao.ManageActivityDao;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.riskmanage.service.IRiskManageService;
import com.taiping.biz.user.service.impl.UserManageServiceImpl;
import com.taiping.constant.AppConstant;
import com.taiping.constant.DateConstant;
import com.taiping.constant.manage.ManageActivityResultCode;
import com.taiping.constant.manage.ManageActivityResultMsg;
import com.taiping.constant.manage.ManageConstant;
import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.manage.ManageBean;
import com.taiping.entity.riskmanage.RiskItem;
import com.taiping.entity.user.UserInfo;
import com.taiping.enums.EmailTemplateEnum;
import com.taiping.enums.manage.*;
import com.taiping.enums.riskmanage.CreatingModeEnum;
import com.taiping.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 运维管理活动服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
@Slf4j
@Service
public class ManageActivityServiceImpl implements ManageActivityService {

    @Autowired
    private ManageActivityDao manageActivityDao;
    /**
     * 风控分析
     */
    @Autowired
    private IRiskManageService iRiskManageService;
    /**
     * 用户信息管理逻辑层服务
     */
    @Autowired
    private UserManageServiceImpl userManageService;
    /**
     * 邮件相关服务
     */
    @Autowired
    private EmailService emailService;
    /**
     * 维护保养计划
     */
    @Autowired
    private IMaintenancePlanService planService;
    /**
     * 预算与采购
     */
    @Autowired
    private BudgetPurchaseServiceImpl budgetPurchaseService;
    /**
     * 运维管理活动提醒
     */
    @Override
    public void reminderManageActivity() {
        long millis = System.currentTimeMillis();
        Long dayStartForTime = DateFormatUtils.getDayStartForTime(millis);
        Long dayEndForTime = DateFormatUtils.getDayEndForTime(millis);
        ManageActivity manageActivity = new ManageActivity();
        manageActivity.setCreateDate(dayStartForTime);
        manageActivity.setCreateTime(dayEndForTime);
        manageActivity.setIsRemind(ManageStatusEnum.REMIND.getType());
        List<ManageActivity> manageActivities = manageActivityDao.queryManageActivityForTime(manageActivity);
        log.info("运维管理活动提醒, 时间{}", DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_TWO, millis));
        for (ManageActivity activity : manageActivities) {
            String responsibleId = activity.getResponsibleId();
            if (responsibleId != null) {
                UserInfo user = userManageService.getUserById(responsibleId);
                if (user != null && user.getEmail() != null) {
                    log.info("运维管理活动提醒, 发送邮件给用户：{}", user.getUserName());
                    Context content = new Context();
                    String nameForCode = ManageSourceEnum.getNameForCode(activity.getSourceMode());
                    content.setVariable(ManageConstant.SOURCE_MODE, nameForCode);
                    content.setVariable(ManageConstant.CAUSE, activity.getCause());
                    content.setVariable(ManageConstant.SOURCE_NAME, activity.getSourceName());
                    String createDate = DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_FIVE,
                            activity.getCreateDate());
                    content.setVariable(ManageConstant.CREATE_DATE, createDate);
                    String completionTime = DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_SIX,
                            activity.getCompletionTime());
                    content.setVariable(ManageConstant.COMPLETION_TIME, completionTime);
                    emailService.sendTemplateMail(user.getEmail(), ManageConstant.EMAIL_TITLE,
                            content, EmailTemplateEnum.MANAGE_REMINDER);
                }
            }
        }
    }

    /**
     * 新增运维管理活动
     *
     * @param manageBean 运维管理活动
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addManageActivity(ManageBean manageBean) {
        //填充运维管理活动数据
        ManageActivity manageActivity = manageBean.getManage();
        manageActivity.setManageId(NineteenUUIDUtils.uuid());
        manageActivity.setManageType(ManageTypeEnum.USER_CREATE.getType());
        long currentTimeMillis = System.currentTimeMillis();
        manageActivity.setCreateTime(currentTimeMillis);
        manageActivity.setSourceMode(ManageSourceEnum.MANAGE.getCode());
        manageActivity.setSourceCode(ManageSourceEnum.USER_CREATE.getCode());
        manageActivity.setSourceName(ManageSourceEnum.USER_CREATE.getName());
        manageActivity.setIsReduce(ManageStatusEnum.UN_REDUCE.getType());
        //插入数据库
        try {
            manageActivityDao.addManageActivity(manageActivity);
        } catch (Exception e) {
            return ResultUtils.warn(ManageActivityResultCode.ADD_MANAGE_ACTIVITY_ERROR,
                    ManageActivityResultMsg.ADD_MANAGE_ACTIVITY_ERROR);
        }
        if (ManageActivityEnum.RISK_MANAGE.getCode().equals(manageActivity.getActivityType())) {
            //插入风险控制
            RiskItem risk = manageBean.getRisk();
            if (risk != null) {
                risk.setActivityId(manageActivity.getManageId());
                risk.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
                iRiskManageService.addRiskItemInside(risk);
            }
        } if (ManageActivityEnum.MAINTENANCE.getCode().equals(manageActivity.getActivityType())) {
            MaintenancePlan plan = manageBean.getPlan();
            if (plan != null) {
                plan.setActivityId(manageActivity.getManageId());
                plan.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
                planService.addMaintenancePlan(plan);
            }
        } else if (ManageActivityEnum.BUDGET_PURCHASE.getCode().equals(manageActivity.getActivityType())) {
            BudgetPurchaseDto budget = manageBean.getBudget();
            if (budget != null) {
                budget.setManageId(manageActivity.getManageId());
                budgetPurchaseService.add(budget);
            }
        }
        return ResultUtils.success();
    }

    /**
     * 查看运维管理活动详情
     *
     * @param manageId 运维管理活动ID
     * @return Result
     */
    @Override
    public Result queryManageActivityById(String manageId) {
        ManageActivity manageActivity = manageActivityDao.queryManageActivityById(manageId);
        //是否存在该运维管理活动
        if (manageActivity == null) {
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_IS_DELETED,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_IS_DELETED);
        }
        String changeReasonTxt = manageActivity.getChangeReasonTxt();
        if (StringUtils.isNotEmpty(changeReasonTxt)) {
            if (changeReasonTxt.indexOf(AppConstant.LINE_FEED) > 0) {
                changeReasonTxt = changeReasonTxt.replace(AppConstant.LINE_FEED, AppConstant.LINE_FEED_REPLACE);
                manageActivity.setChangeReasonTxt(changeReasonTxt);
            }
        }
        ManageBean manageBean = new ManageBean();
        manageBean.setManage(manageActivity);
        //获取关联名称
        setManageActivity(manageBean);
        return ResultUtils.success(manageBean);
    }


    /**
     * 查询关联名称
     *
     * @param manageActivity 运维管理活动
     * @return Result
     */
    @Override
    public Result queryManageRelation(ManageActivity manageActivity) {
        ManageBean manageBean = new ManageBean();
        manageBean.setManage(manageActivity);
        setManageActivity(manageBean);
        return ResultUtils.success(manageBean);
    }

    /**
     * 获取关联名称
     * @param manageBean 运维管理活动
     */
    private void setManageActivity(ManageBean manageBean) {
        ManageActivity manageActivity = manageBean.getManage();
        if (ManageActivityEnum.RISK_MANAGE.getCode().equals(manageActivity.getActivityType())) {
            //查询风险控制
            RiskItem riskItem = iRiskManageService.getRiskItemByActivityId(manageActivity.getManageId());
            if (riskItem != null) {
                manageBean.setRisk(riskItem);
            }
        } if (ManageActivityEnum.MAINTENANCE.getCode().equals(manageActivity.getActivityType())) {
            //查询维护保养计划
            MaintenancePlan plan = planService.getMaintenancePlanByActivityId(manageActivity.getManageId());
            if (plan != null) {
                manageBean.setPlan(plan);
            }
        } else if (ManageActivityEnum.BUDGET_PURCHASE.getCode().equals(manageActivity.getActivityType())) {
            //查询预算与采购
            BudgetPurchaseDto purchase = budgetPurchaseService.getPurchaseItemByActivityId(manageActivity.getManageId());
            if (purchase != null) {
                manageBean.setBudget(purchase);
            }
        }
    }



    /**
     * 管理运维管理活动
     *
     * @param manageBean 运维管理活动
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateManageActivity(ManageBean manageBean) {
        ManageActivity manageActivity = manageBean.getManage();
        ManageActivity manageActivityDb = manageActivityDao.queryManageActivityById(manageActivity.getManageId());
        //是否存在该运维管理活动
        if (manageActivityDb == null) {
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_IS_DELETED,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_IS_DELETED);
        }
        //是否已复核通过
        if (ManageStatusEnum.REVIEW_YES.getType().equals(manageActivityDb.getApprovalStatus())) {
            //复核通过不能被修改
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_APPROVAL,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_APPROVAL);
        } else {
            String chang = manageActivity.getChangeReason();
            String history = manageActivityDb.getChangeReasonTxt();
            long updateTime = System.currentTimeMillis();
            String changTime = DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_SEVEN, updateTime);
            chang = changTime + "  " + chang;
            if (StringUtils.isNotEmpty(history)) {
                if (history.length() + chang.length() > ManageConstant.TXT_LENGTH) {
                    int i = history.indexOf(AppConstant.LINE_FEED);
                    if (i > 0) {
                        history = history.substring(i + 6) + AppConstant.LINE_FEED;
                    } else {
                        history = "";
                    }
                } else {
                    history = history + AppConstant.LINE_FEED;
                }
                chang = history + chang;
            }
            manageActivity.setChangeReasonTxt(chang);
            manageActivity.setUpdateTime(updateTime);
            manageActivityDao.updateManageActivityById(manageActivity);
            if (ManageActivityEnum.RISK_MANAGE.getCode().equals(manageActivity.getActivityType())) {
                //插入风险控制
                RiskItem risk = manageBean.getRisk();
                if (risk != null) {
                    if (StringUtils.isEmpty(risk.getRiskItemId())) {
                        risk.setActivityId(manageActivity.getManageId());
                        risk.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
                        iRiskManageService.addRiskItemInside(risk);
                    } else {
                        iRiskManageService.modifyRiskItem(risk);
                    }
                }
            } if (ManageActivityEnum.MAINTENANCE.getCode().equals(manageActivity.getActivityType())) {
                //插入维护保养计划
                MaintenancePlan plan = manageBean.getPlan();
                if (plan != null) {
                    if (StringUtils.isEmpty(plan.getMaintenancePlanId())) {
                        plan.setActivityId(manageActivity.getManageId());
                        plan.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
                        planService.addMaintenancePlan(plan);
                    } else {
                        planService.modifyMaintenancePlan(plan);
                    }
                }
            } else if (ManageActivityEnum.BUDGET_PURCHASE.getCode().equals(manageActivity.getActivityType())) {
                //插入预算与采购
                BudgetPurchaseDto budget = manageBean.getBudget();
                if (budget != null) {
                    if (StringUtils.isEmpty(budget.getTid())) {
                        budget.setManageId(manageActivity.getManageId());
                        budgetPurchaseService.add(budget);
                    } else {
                        budgetPurchaseService.updatePurchase(budget);
                    }
                }
            }
        }
        return ResultUtils.success();
    }

    /**
     * 查询运维管理活动列表
     *
     * @param queryCondition 查询条件
     * @return 运维管理活动列表
     */
    @Override
    public Result selectManageActivityList(QueryCondition<ManageActivity> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "createDate", "desc");
        ManageActivity bizCondition = queryCondition.getBizCondition();
        List<FilterCondition> filterConditions = queryCondition.getFilterConditions();
        //添加月份筛选条件
        if (!(bizCondition == null || bizCondition.getCreateDate() == null)) {
            Long startTime = DateFormatUtils.getMonthStartForTime(bizCondition.getCreateDate());
            Long endTime = DateFormatUtils.getMonthEndForTime(bizCondition.getCreateDate());
            FilterCondition start = new FilterCondition();
            start.setFilterField("createDate");
            start.setFilterValue(startTime);
            start.setOperator("gte");
            FilterCondition end = new FilterCondition();
            end.setFilterField("createDate");
            end.setFilterValue(endTime);
            end.setOperator("lte");
            filterConditions.add(start);
            filterConditions.add(end);
        }
        //查询
        List<ManageActivity> manageActivityList = manageActivityDao.selectManageActivityList(
                queryCondition.getPageCondition(), filterConditions, queryCondition.getSortCondition());
        //查询总条数
        Integer count = manageActivityDao.selectManageActivityListCount(filterConditions);
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, manageActivityList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询运维管理活动列表数量
     *
     * @param queryCondition 查询条件
     * @return 运维管理活动列表
     */
    @Override
    public Integer selectManageActivityListCount(QueryCondition<ManageActivity> queryCondition) {
        //处理构建查询条件
        MpQueryHelper.structureQueryCondition(queryCondition, "createDate", "desc");
        //查询总条数
        return manageActivityDao.selectManageActivityListCount(queryCondition.getFilterConditions());
    }

    /**
     * 取消运维管理活动
     *
     * @param manageId 运维管理活动ID
     * @return Result
     */
    @Override
    public Result cancelManageActivity(String manageId) {
        ManageActivity manageActivity = manageActivityDao.queryManageActivityById(manageId);
        //是否存在该运维管理活动
        if (manageActivity == null) {
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_IS_DELETED,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_IS_DELETED);
        }
        //是否可以取消
        if (ManageTypeEnum.CAN_CANCEL.getType().equals(manageActivity.getManageType())) {
            ManageActivity manageActivityDb = new ManageActivity();
            manageActivityDb.setManageType(ManageTypeEnum.CANCELLED.getType());
            manageActivityDb.setManageId(manageId);
            manageActivityDao.cancelManageTypeById(manageActivityDb);
        } else if (ManageTypeEnum.CANCELLED.getType().equals(manageActivity.getManageType())) {
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_CANCEL,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_CANCEL);
        } else {
            return ResultUtils.warn(ManageActivityResultCode.MANAGE_ACTIVITY_NOT_CANCEL,
                    ManageActivityResultMsg.MANAGE_ACTIVITY_NOT_CANCEL);
        }
        return ResultUtils.success();
    }

    /**
     * 生成运维管理活动
     *
     * @param manageSourceEnum   来源模块
     * @param manageActivityList 运维管理活动List
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertManageActivity(ManageSourceEnum manageSourceEnum,
                                        List<ManageActivity> manageActivityList) {
        String mode = manageSourceEnum.getCode();
        //查询上月中对象本月再次产生
        List<ManageActivity> manageActivities = manageActivityDao.queryCurrentSameSource(mode,
                ManageTypeEnum.CURRENT.getType(), manageActivityList);
        List<String> typeList = new ArrayList<>();
        typeList.add(ManageTypeEnum.CURRENT.getType());
        typeList.add(ManageTypeEnum.CAN_CANCEL.getType());
        //修改上月和上月可取消的数据类型为历史（3）
        manageActivityDao.updateManageTypeForType(mode, ManageTypeEnum.HISTORY.getType(), typeList);
        if (CollectionUtils.isNotEmpty(manageActivities)) {
            //上月与本月对象相同的修改类型为可取消
            manageActivityDao.updateManageTypeForId(mode,
                    ManageTypeEnum.CAN_CANCEL.getType(), manageActivities);
        }
        //批量插入运维管理活动
        manageActivityDao.insertManageActivityBatch(manageActivityList);
    }

    /**
     * 生成运维管理活动，不查询相同对象
     *
     * @param manageSourceEnum   来源模块
     * @param manageActivityList 运维管理活动List
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertManageActivityNoEquals(ManageSourceEnum manageSourceEnum,
                                                List<ManageActivity> manageActivityList) {
        if (CollectionUtils.isEmpty(manageActivityList)) {
            return;
        }
        List<String> typeList = new ArrayList<>();
        typeList.add(ManageTypeEnum.CURRENT.getType());
        //修改上月的数据类型为历史（3）
        manageActivityDao.updateManageTypeForType(manageSourceEnum.getCode(),
                ManageTypeEnum.HISTORY.getType(), typeList);
        //批量插入运维管理活动
        manageActivityDao.insertManageActivityBatch(manageActivityList);
    }

    /**
     * 生成运维管理活动对象
     * @param manageId id
     * @param sourceName 来源对象名称
     * @param sourceCode  来源对象code
     * @param createTime 创建时间
     * @param advise 建议
     * @param manageSourceEnum 来源模块
     * @param manageStatusEnum 是否可以裁剪
     * @param cause 产生原因
     * @param createDate 产生日期
     * @return ManageActivity
     */
    @Override
    public ManageActivity createManageActivity(String manageId, String sourceName, String sourceCode, Long createTime,
                                               String advise, String cause, ManageStatusEnum manageStatusEnum,
                                               ManageSourceEnum manageSourceEnum, Long createDate) {
        ManageActivity manageActivity = new ManageActivity();
        manageActivity.setManageId(manageId);
        manageActivity.setSourceName(sourceName);
        manageActivity.setSourceCode(sourceCode);
        manageActivity.setSourceMode(manageSourceEnum.getCode());
        manageActivity.setIsReduce(manageStatusEnum.getType());
        manageActivity.setCause(cause);
        manageActivity.setAdvise(advise);
        manageActivity.setCreateDate(createDate);
        manageActivity.setCreateTime(createTime);
        manageActivity.setManageType(ManageTypeEnum.CURRENT.getType());
        manageActivity.setIsRemind(ManageStatusEnum.COMPLETED.getType());
        manageActivity.setCompletionStatus(ManageStatusEnum.INCOMPLETE.getType());
        manageActivity.setApprovalStatus(ManageStatusEnum.NO_REVIEW.getType());
        manageActivity.setActivityType(ManageActivityEnum.ATTENTION.getCode());
        return manageActivity;
    }

    /**
     * 生成运维管理活动对象
     *
     * @param manageId         id
     * @param sourceName       来源对象名称
     * @param sourceCode       来源对象code
     * @param createTime       创建时间
     * @param advise           建议
     * @param cause            产生原因
     * @param manageStatusEnum 是否可以裁剪
     * @param createDate       产生日期
     * @param manageSourceEnum 来源模块
     * @param sourceTypeEnum   来源分析类型
     * @return ManageActivity
     */
    @Override
    public ManageActivity createManageActivity(String manageId, String sourceName, String sourceCode, Long createTime,
                                               String advise, String cause, ManageStatusEnum manageStatusEnum,
                                               ManageSourceEnum manageSourceEnum,  Long createDate, ManageSourceTypeEnum sourceTypeEnum) {
        ManageActivity manageActivity = new ManageActivity();
        manageActivity.setManageId(manageId);
        manageActivity.setSourceName(sourceName);
        manageActivity.setSourceCode(sourceCode);
        manageActivity.setSourceMode(manageSourceEnum.getCode());
        manageActivity.setIsReduce(manageStatusEnum.getType());
        manageActivity.setCause(cause);
        manageActivity.setAdvise(advise);
        manageActivity.setCreateDate(createDate);
        manageActivity.setCreateTime(createTime);
        manageActivity.setManageType(ManageTypeEnum.CURRENT.getType());
        manageActivity.setIsRemind(ManageStatusEnum.COMPLETED.getType());
        manageActivity.setCompletionStatus(ManageStatusEnum.INCOMPLETE.getType());
        manageActivity.setApprovalStatus(ManageStatusEnum.NO_REVIEW.getType());
        manageActivity.setActivityType(ManageActivityEnum.ATTENTION.getCode());
        manageActivity.setSourceType(sourceTypeEnum.getCode());
        return manageActivity;
    }

    /**
     * 查询模块运维管理活动对象
     *
     * @param manageSourceEnum 来源模块
     * @return List<ManageActivity>
     */
    @Override
    public List<ManageActivity> queryManageActivityForMode(ManageSourceEnum manageSourceEnum) {
        String code = manageSourceEnum.getCode();
        return getManageActivities(code, null);
    }

    /**
     * 查询模块运维管理活动对象
     *
     * @param manageActivity 来源模块
     * @return Result
     */
    @Override
    public Result queryManageActivityForMode(ManageActivity manageActivity) {
        List<ManageActivity> manageActivityList = getManageActivities(manageActivity.getSourceMode(),
                manageActivity.getSourceType());
        manageActivityList.sort(Comparator.comparing(ManageActivity::getSourceCode));
        return ResultUtils.success(manageActivityList);
    }

    /**
     * 查询对应模块运维管理活动
     * @param code 模块
     * @return 运维管理活动
     */
    private List<ManageActivity> getManageActivities(String code, String sourceType) {
        List<ManageActivity> manageActivities;
        //问题分析
        String problemCode = ManageSourceEnum.PROBLEM.getCode();
        //生产力分析
        String productivityCode = ManageSourceEnum.PRODUCTIVITY.getCode();
        //生产力分析
        String energyCode = ManageSourceEnum.ENERGY.getCode();
        //当前和上月相同对象运维管理活动
        List<String> typeList = new ArrayList<>();
        typeList.add(ManageTypeEnum.CURRENT.getType());
        typeList.add(ManageTypeEnum.CAN_CANCEL.getType());
        //问题分析和生产力分析运维管理活动固定查询当月
        if (code.equals(problemCode) || code.equals(productivityCode)
                || code.equals(energyCode)) {
            manageActivities = manageActivityDao.queryManageForMode(code, typeList, sourceType,
                    null, null);
        } else {
            manageActivities = manageActivityDao.queryManageForMode(code, typeList, sourceType,
                    ManageTypeEnum.HISTORY.getType(), ManageActivityEnum.ATTENTION.getCode());
        }
        return manageActivities;
    }
}
