package com.taiping.biz.maintenanceplan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taiping.bean.maintenance.MaintenancePlanReportDto;
import com.taiping.bean.maintenance.TableHeader;
import com.taiping.biz.maintenanceplan.dao.MaintenancePlanAnalysisReportDao;
import com.taiping.biz.maintenanceplan.dao.MaintenancePlanDao;
import com.taiping.biz.maintenanceplan.dao.MaintenancePlanStatisticsDao;
import com.taiping.biz.maintenanceplan.dao.PlanExecuteSituationDao;
import com.taiping.biz.maintenanceplan.service.IMaintenancePlanService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.biz.user.service.impl.UserManageServiceImpl;
import com.taiping.constant.maintenance.MaintenancePlanConstant;
import com.taiping.constant.maintenance.MaintenancePlanResultCode;
import com.taiping.constant.maintenance.MaintenancePlanResultMsg;
import com.taiping.constant.riskmanage.RiskManageConstant;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.entity.maintenanceplan.MaintenancePlanAnalysisReport;
import com.taiping.entity.maintenanceplan.MaintenancePlanStatistics;
import com.taiping.entity.maintenanceplan.PlanExecuteSituation;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.riskmanage.RiskTrendAnalysis;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.user.UserInfo;
import com.taiping.enums.EmailTemplateEnum;
import com.taiping.enums.maintenanceplan.*;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.enums.riskmanage.CreatingModeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * ???????????????????????????????????????
 */
@Service
@Slf4j
public class MaintenancePlanServiceImpl implements IMaintenancePlanService {
    /**
     * ?????????????????????????????????
     */
    @Autowired
    private MaintenancePlanDao maintenancePlanDao;
    /**
     * ???????????????????????????????????????
     */
    @Autowired
    private PlanExecuteSituationDao situationDao;
    /**
     * ??????????????????
     */
    @Autowired
    private EmailService emailService;
    /**
     * ?????????????????????????????????
     */
    @Autowired
    private UserManageServiceImpl userManageService;
    /**
     * ???????????????????????????
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * ??????????????????
     */
    @Autowired
    private SystemService systemService;
    /**
     * ???????????????????????????
     */
    @Autowired
    private ParamManageService paramManageService;

    /**
     * ???????????????????????????????????????
     */
    @Autowired
    private MaintenancePlanAnalysisReportDao analysisReportDao;
    /**
     * ?????????????????????????????????
     */
    @Autowired
    private MaintenancePlanStatisticsDao statisticsDao;

    /**
     * ????????????????????????
     *
     * @param maintenancePlan ??????????????????????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addMaintenancePlan(MaintenancePlan maintenancePlan) {
        //?????????????????????????????????
        if(!checkPlanExisted(maintenancePlan.getMaintenancePlanName())) {
            throw new BizException(MaintenancePlanResultCode.MAINTENANCE_PLAN_HAVE_EXISTED,
                    MaintenancePlanResultMsg.MAINTENANCE_PLAN_HAVE_EXISTED);
        }
        if(ObjectUtils.isEmpty(maintenancePlan.getMaintenanceStartTime())) {
            throw new BizException(MaintenancePlanResultCode.PLAN_EXECUTE_TIME_NOT_EXISTED,
                    MaintenancePlanResultMsg.PLAN_EXECUTE_TIME_NOT_EXISTED);
        }
        //????????????????????????
        maintenancePlan.setMaintenancePlanId(NineteenUUIDUtils.uuid());
        maintenancePlan.setStatus(PlanStatusEnum.NORMAL.getCode());
        maintenancePlan.setIsDeleted(0);
        maintenancePlan.setCreateTime(System.currentTimeMillis());
        //????????????????????????????????????
        List<PlanExecuteSituation> situations = getPlanExecuteSituationList(maintenancePlan, 0);
        try {
            maintenancePlanDao.insert(maintenancePlan);
            if(!ObjectUtils.isEmpty(situations)) {
                situationDao.batchInsertData(situations);
            }
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add maintenancePlan error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,
                    MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ????????????????????????
     *
     * @param maintenancePlan ??????????????????????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyMaintenancePlan(MaintenancePlan maintenancePlan) {
        //??????????????????????????????
        MaintenancePlan plan = maintenancePlanDao.selectById(maintenancePlan.getMaintenancePlanId());
        if (ObjectUtils.isEmpty(plan)) {
            throw new BizException(MaintenancePlanResultCode.MAINTENANCE_PLAN_NOT_EXISTED,
                    MaintenancePlanResultMsg.MAINTENANCE_PLAN_NOT_EXISTED);
        }
        List<PlanExecuteSituation> situations = getSituationsByPlanId(maintenancePlan.getMaintenancePlanId());
        List<String> deleteSituationIds = Lists.newArrayList();
        List<PlanExecuteSituation> insertSituations = Lists.newArrayList();
        //??????????????????????????????????????????????????????????????????
        if(!ObjectUtils.isEmpty(maintenancePlan.getPeriod()) && !maintenancePlan.getPeriod().equals(plan.getPeriod())) {
            //???????????????????????????????????????
            deleteSituationIds = situations.stream().filter(situation -> situation.getPlanExecuteTime() > System.currentTimeMillis())
                    .map(PlanExecuteSituation::getId).collect(Collectors.toList());
            if(ObjectUtils.isEmpty(maintenancePlan.getMaintenanceStartTime())) {
                throw new BizException(MaintenancePlanResultCode.PLAN_EXECUTE_TIME_NOT_EXISTED,
                        MaintenancePlanResultMsg.PLAN_EXECUTE_TIME_NOT_EXISTED);
            }
            //????????????????????????
            insertSituations = getPlanExecuteSituationList(maintenancePlan, 0);
        } else {
            //????????????????????????????????????????????????
            if(!ObjectUtils.isEmpty(maintenancePlan.getMaintenanceStartTime()) && !maintenancePlan.getMaintenanceStartTime().equals(plan.getMaintenanceStartTime())) {
                //???????????????????????????????????????
                deleteSituationIds = situations.stream().filter(situation -> situation.getPlanExecuteTime() > System.currentTimeMillis())
                        .map(PlanExecuteSituation::getId).collect(Collectors.toList());
                //????????????????????????
                insertSituations = getPlanExecuteSituationList(maintenancePlan, 0).stream()
                        .filter(situation -> situation.getPlanExecuteTime() > System.currentTimeMillis()).collect(Collectors.toList());
            }
        }
        maintenancePlan.setUpdateTime(System.currentTimeMillis());
        try {
            maintenancePlanDao.updateById(maintenancePlan);
            if(!ObjectUtils.isEmpty(deleteSituationIds)) {
                situationDao.batchDeleteData(deleteSituationIds);
            }
            if(!ObjectUtils.isEmpty(insertSituations)) {
                situationDao.batchInsertData(insertSituations);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify maintenancePlan error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,
                    MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param maintenancePlanIds ??????????????????id??????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDeleteMaintenancePlan(List<String> maintenancePlanIds) {
        //????????????????????????????????????????????????
        EntityWrapper<PlanExecuteSituation> wrapper = new EntityWrapper<>();
        wrapper.in(MaintenancePlanConstant.MAINTENANCE_PLAN_ID_COLUMN,maintenancePlanIds);
        List<PlanExecuteSituation> situations = situationDao.selectList(wrapper);
        List<String> situationIds = situations.stream().map(PlanExecuteSituation::getId).collect(Collectors.toList());
        try{
            if(!ObjectUtils.isEmpty(maintenancePlanIds)) {
                maintenancePlanDao.deleteBatchIds(maintenancePlanIds);
            }
            if(!ObjectUtils.isEmpty(situationIds)) {
                situationDao.batchDeleteData(situationIds);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("delete maintenancePlan error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return ????????????????????????
     */
    @Override
    public List<MaintenancePlan> getAllPlan() {
        EntityWrapper<MaintenancePlan> wrapper = new EntityWrapper<>();
        wrapper.orderBy("system").orderBy("child_system").orderBy("device").orderBy("period").orderBy("component").orderBy("maintenance_company");
        List<MaintenancePlan> planList = maintenancePlanDao.selectList(wrapper);
        List<PlanExecuteSituation> situationLists  = situationDao.selectList(new EntityWrapper<>());
        for (MaintenancePlan plan: planList) {
            List<PlanExecuteSituation> situations = situationLists.stream().filter(planExecuteSituation ->
                    plan.getMaintenancePlanId().equals(planExecuteSituation.getMaintenancePlanId())).collect(Collectors.toList());
            plan.setSituations(situations);
        }
        return planList;
    }

    /**
     * ??????????????????????????????
     *
     * @param queryCondition ????????????
     * @return ????????????
     */
    @Override
    public PageBean getMaintenancePlanByCondition(QueryCondition<MaintenancePlan> queryCondition) {
        //??????????????????
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        //??????????????????
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        entityWrapper.orderBy("system").orderBy("child_system").orderBy("device").orderBy("period").orderBy("component").orderBy("maintenance_company");
        //??????????????????
        int count = maintenancePlanDao.selectCount(entityWrapper);
        //????????????????????????
        List<MaintenancePlan> pageMaintenancePlan = maintenancePlanDao.selectPage(page, entityWrapper);
        List<PlanExecuteSituation> situationLists  = situationDao.selectList(new EntityWrapper<>());
        List<SystemInputSetting> settings = systemService.queryInputValueByCode(MaintenancePlanConstant.MAINTENANCE_FIRST_CODE);
        Map<String,String> settingMap = Maps.newHashMap();
        for (SystemInputSetting setting : settings) {
            List<SystemInputSetting> childSettings = setting.getChild();
            for (SystemInputSetting childSetting : childSettings) {
                settingMap.put(childSetting.getCode(),childSetting.getCodeName());
            }
        }
        for (MaintenancePlan plan: pageMaintenancePlan) {
            List<PlanExecuteSituation> situations = situationLists.stream().filter(planExecuteSituation ->
                    plan.getMaintenancePlanId().equals(planExecuteSituation.getMaintenancePlanId())).collect(Collectors.toList());
            plan.setSituations(situations);
            if (!ObjectUtils.isEmpty(settingMap)) {
                plan.setSystemName(settingMap.get(plan.getSystem()));
                plan.setChildSystemName(settingMap.get(plan.getChildSystem()));
                plan.setDeviceName(settingMap.get(plan.getDevice()));
                plan.setComponentName(settingMap.get(plan.getComponent()));
                plan.setMaintenanceCompanyName(settingMap.get(plan.getMaintenanceCompany()));
            }
        }
        return MpQueryHelper.myBatiesBuildPageBean(page,count,pageMaintenancePlan);
    }

    /**
     * ??????????????????????????????(???????????????)
     *
     * @param queryCondition ????????????
     * @return ????????????
     */
    @Override
    public List<MaintenancePlan> getMaintenancePlanTableData(QueryCondition<MaintenancePlan> queryCondition) {
        EntityWrapper wrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        wrapper.orderBy("system").orderBy("child_system").orderBy("device").orderBy("period").orderBy("component").orderBy("maintenance_company");
        List<MaintenancePlan> planList = maintenancePlanDao.selectList(wrapper);
        Map<String, List<MaintenancePlan>> systemMap = planList.stream().collect(Collectors.groupingBy(MaintenancePlan::getSystem));
        systemMap.forEach((system, systemList) -> {
            systemList.get(0).setSystemRowSpan(systemList.size());
            Map<String, List<MaintenancePlan>> childSystemMap = systemList.stream().collect(Collectors.groupingBy(MaintenancePlan::getChildSystem));
            childSystemMap.forEach((childSystem, childSystemList) -> {
                childSystemList.get(0).setChildSystemRowSpan(childSystemList.size());
                Map<String, List<MaintenancePlan>> deviceMap = childSystemList.stream().collect(Collectors.groupingBy(MaintenancePlan::getDevice));
                deviceMap.forEach((device, deviceList) -> {
                    deviceList.get(0).setDeviceRowSpan(deviceList.size());
                    Map<Integer, List<MaintenancePlan>> periodMap = deviceList.stream().collect(Collectors.groupingBy(MaintenancePlan::getPeriod));
                    periodMap.forEach((period, periodList) -> {
                        periodList.get(0).setPeriodRowSpan(periodList.size());
                        Map<String, List<MaintenancePlan>> componentMap = periodList.stream().collect(Collectors.groupingBy(MaintenancePlan::getComponent));
                        componentMap.forEach((component, componentList) -> {
                            componentList.get(0).setComponentRowSpan(componentList.size());
                            Map<String, List<MaintenancePlan>> maintenanceCompanyMap = componentList.stream().collect(Collectors.groupingBy(MaintenancePlan::getMaintenanceCompany));
                            maintenanceCompanyMap.forEach((maintenanceCompany, maintenanceCompanyList) -> {
                                maintenanceCompanyList.get(0).setMaintenanceCompanyRowSpan(maintenanceCompanyList.size());
                            });
                        });
                    });
                });
            });
        });
        List<PlanExecuteSituation> situationLists  = situationDao.selectList(new EntityWrapper<>());
        List<SystemInputSetting> settings = systemService.queryInputValueByCode(MaintenancePlanConstant.MAINTENANCE_FIRST_CODE);
        Map<String,String> settingMap = Maps.newHashMap();
        for (SystemInputSetting setting : settings) {
            List<SystemInputSetting> childSettings = setting.getChild();
            for (SystemInputSetting childSetting : childSettings) {
                settingMap.put(childSetting.getCode(),childSetting.getCodeName());
            }
        }
        for (MaintenancePlan plan: planList) {
            List<PlanExecuteSituation> situations = situationLists.stream().filter(planExecuteSituation ->
                    plan.getMaintenancePlanId().equals(planExecuteSituation.getMaintenancePlanId())).collect(Collectors.toList());
            plan.setSituations(situations);
            if (!ObjectUtils.isEmpty(settingMap)) {
                plan.setSystemName(settingMap.get(plan.getSystem()));
                plan.setChildSystemName(settingMap.get(plan.getChildSystem()));
                plan.setComponentName(settingMap.get(plan.getComponent()));
                plan.setDeviceName(settingMap.get(plan.getDevice()));
                plan.setMaintenanceCompanyName(settingMap.get(plan.getMaintenanceCompany()));
            }
        }
        return planList;
    }

    /**
     * ??????id??????????????????
     *
     * @param planId ????????????id
     * @return ??????????????????
     */
    @Override
    public MaintenancePlan getMaintenancePlanById(String planId) {
        MaintenancePlan plan = maintenancePlanDao.selectById(planId);
        plan.setSituations(getSituationsByPlanId(planId));
        return plan;
    }

    /**
     * ????????????????????????id??????????????????
     *
     * @param activityId ??????????????????id
     * @return ??????????????????
     */
    @Override
    public MaintenancePlan getMaintenancePlanByActivityId(String activityId) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.setActivityId(activityId);
        return maintenancePlanDao.selectOne(plan);
    }

    /**
     * ??????????????????????????????
     *
     * @param year ????????????
     * @return ????????????
     */
    @Override
    public List<TableHeader> getTableHeaderByYear(Integer year) {
        List<TableHeader> headerList = Lists.newArrayList();
        //????????????????????????
        Map<Long, String> timeMap = LocalDateUtil.getAllWeekByYear(year);
        Map<Long, String> result = Maps.newLinkedHashMap();
        timeMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        result.forEach((time, title) -> {
            String[] titleArrays = title.split(MaintenancePlanConstant.LINE_FEED_REPLACE);
            String monthTitle = titleArrays[0];
            String weekTitle = titleArrays[1];
            TableHeader header = createHeader(time,monthTitle,weekTitle,MaintenancePlanConstant.HEADER_WIDTH);
            headerList.add(header);
        });
        Map<String,List<TableHeader>> headerMap = headerList.stream().collect(Collectors.groupingBy(TableHeader::getMonthTitle));
        headerMap.forEach((monthTitle, list) -> list.get(0).setMonthColSpan(list.size())
        );
        return headerList;
    }

    /**
     * ??????/????????????????????????
     *
     * @param plan ?????????/???????????????????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result planPauseOrEnable(MaintenancePlan plan) {
        Long currentTime = System.currentTimeMillis();
        if (PlanStatusEnum.SUSPEND.getCode().equals(plan.getStatus())) {
            plan.setSuspendTime(currentTime);
        } else {
           plan.setSuspendTime(null);
        }
        List<PlanExecuteSituation> situations = getSituationsByPlanId(plan.getMaintenancePlanId());
        for (PlanExecuteSituation situation: situations) {
            if(currentTime < situation.getPlanExecuteTime()) {
                if (PlanStatusEnum.SUSPEND.getCode().equals(plan.getStatus())) {
                    situation.setExecuteStatus(ExecuteStatusEnum.PAUSED.getCode());
                } else {
                    situation.setExecuteStatus(ExecuteStatusEnum.UNENFORCED.getCode());
                }
            }
        }
        try {
            maintenancePlanDao.updateById(plan);
            if (!ObjectUtils.isEmpty(situations)) {
                situationDao.batchUpdateData(situations);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("pauseOrEnable maintenancePlan error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ????????????????????????
     *
     * @param situation ????????????????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyPlanExecuteSituation(PlanExecuteSituation situation) {
        PlanExecuteSituation executeSituation = situationDao.selectById(situation.getId());
        if(ObjectUtils.isEmpty(executeSituation)) {
            return ResultUtils.warn(MaintenancePlanResultCode.EXECUTE_SITUATION_NOT_EXISTED,MaintenancePlanResultMsg.EXECUTE_SITUATION_NOT_EXISTED);
        }
        List<PlanExecuteSituation> updateSituations = Lists.newArrayList();
        MaintenancePlan plan = maintenancePlanDao.selectById(executeSituation.getMaintenancePlanId());
        List<PlanExecuteSituation> situations = getSituationsByPlanId(executeSituation.getMaintenancePlanId());
        if (ExecuteStatusEnum.EXECUTED.getCode().equals(situation.getExecuteStatus())) {
            if (PlanStatusEnum.DELAY.getCode().equals(plan.getStatus())) {
                plan.setStatus(PlanStatusEnum.NORMAL.getCode());
            }
            for (PlanExecuteSituation planExecuteSituation: situations) {
                if(ExecuteStatusEnum.DELAY.getCode().equals(planExecuteSituation.getExecuteStatus())){
                    if (executeSituation.getPlanExecuteTime().equals(planExecuteSituation.getPlanExecuteTime())) {
                        planExecuteSituation.setExecuteStatus(ExecuteStatusEnum.DELAY_EXECUTED.getCode());
                        planExecuteSituation.setActualExecuteTime(System.currentTimeMillis());
                        updateSituations.add(planExecuteSituation);
                    } else if (executeSituation.getPlanExecuteTime() > planExecuteSituation.getPlanExecuteTime()){
                        planExecuteSituation.setExecuteStatus(ExecuteStatusEnum.UNENFORCED.getCode());
                        updateSituations.add(planExecuteSituation);
                    }
                }
            }
        }
        try {
            maintenancePlanDao.updateById(plan);
            if(!ObjectUtils.isEmpty(updateSituations)) {
                situationDao.batchUpdateData(updateSituations);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify planSituation error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDelaySituation() {
        List<MaintenancePlan> planList = getAllPlan();
        List<PlanExecuteSituation> updateSituations = Lists.newArrayList();
        for (MaintenancePlan plan : planList) {
            List<PlanExecuteSituation> situations = plan.getSituations();
            if (!PlanStatusEnum.SUSPEND.getCode().equals(plan.getStatus())) {
                for (PlanExecuteSituation situation : situations) {
                    if (DateFormatUtils.getNextWeekStartTime(situation.getPlanExecuteTime()) <= DateFormatUtils.getThisWeekStartTime(System.currentTimeMillis())
                            && ExecuteStatusEnum.UNENFORCED.getCode().equals(situation.getExecuteStatus())) {
                        situation.setExecuteStatus(ExecuteStatusEnum.DELAY.getCode());
                        updateSituations.add(situation);
                        if (PlanStatusEnum.NORMAL.getCode().equals(plan.getStatus())) {
                            plan.setStatus(PlanStatusEnum.DELAY.getCode());
                        }
                    }
                }
            }
            Integer delayType = calculationDelayTime(situations, plan);
            plan.setDelayType(delayType);
        }
        try {
            if (!ObjectUtils.isEmpty(planList)) {
                maintenancePlanDao.batchUpdateData(planList);
            }
            if(!ObjectUtils.isEmpty(updateSituations)) {
                situationDao.batchUpdateData(updateSituations);
            }
        }catch (Exception e) {
            log.error("update delaySituation error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ???????????????
     */
    @Override
    public void maintenancePreReminder() {
        List<MaintenancePlan> planList = getAllPlan();
        Long currentTime = System.currentTimeMillis();
        for (MaintenancePlan plan: planList) {
            if(!PlanStatusEnum.SUSPEND.getCode().equals(plan.getStatus())) {
                List<PlanExecuteSituation> situations = getSituationsByPlanId(plan.getMaintenancePlanId());
                situations = situations.stream().filter(situation -> situation.getPlanExecuteTime() > currentTime)
                        .sorted(Comparator.comparing(PlanExecuteSituation::getPlanExecuteTime)).collect(Collectors.toList());
                Long toNextExecuteTime = 0L;
                if(!ObjectUtils.isEmpty(situations)) {
                    toNextExecuteTime = situations.get(0).getPlanExecuteTime() - currentTime;
                }
                Integer toNextExecuteDays = (int)(toNextExecuteTime/(24 * 60 * 60 * 1000));
                if(toNextExecuteDays > plan.getPreReminderTime()) {
                    String maintenanceUserId = plan.getMaintenanceUser();
                    UserInfo maintenanceUser = userManageService.getUserById(maintenanceUserId);
                    if(!ObjectUtils.isEmpty(maintenanceUser)) {
                        Context content = new Context();
                        content.setVariable(MaintenancePlanConstant.PARAM_ONE, plan.getMaintenancePlanName());
                        content.setVariable(MaintenancePlanConstant.PARAM_TWO,toNextExecuteDays);
                        content.setVariable(MaintenancePlanConstant.PARAM_THREE, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")));
                        String sendTo = maintenanceUser.getEmail();
                        emailService.sendTemplateMail(sendTo,MaintenancePlanConstant.EMAIL_TITLE,content,EmailTemplateEnum.SAMPLE_THREE);
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param executeTime ??????????????????
     * @param period ??????????????????
     * @return ????????????
     */
    @Override
    public Result checkEnableModify(Long executeTime,Integer period) {
        Long currentTime = System.currentTimeMillis();
        Long selectPeriodEndTime;
        if (PeriodEnum.WEEKLY.getCode().equals(period)) {
            selectPeriodEndTime = DateFormatUtils.getNextWeekStartTime(executeTime);
        } else if (PeriodEnum.MONTHLY.getCode().equals(period)) {
            selectPeriodEndTime = DateFormatUtils.getNextMonthTime(executeTime);
        } else if (PeriodEnum.QUARTER.getCode().equals(period)) {
            selectPeriodEndTime = DateFormatUtils.getNextQuarterTime(executeTime);
        } else if (PeriodEnum.HALF_YEAR.getCode().equals(period)) {
            selectPeriodEndTime = DateFormatUtils.getNextHalfYearTime(executeTime);
        } else {
            return ResultUtils.success();
        }
        if(selectPeriodEndTime < currentTime) {
            return ResultUtils.warn(MaintenancePlanResultCode.OUT_OF_EXECUTE_PERIOD,MaintenancePlanResultMsg.OUT_OF_EXECUTE_PERIOD);
        }
        return ResultUtils.success();
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param activityId ??????????????????id
     * @param planName   ????????????????????????
     */
    @Override
    public void createMaintenancePlanByActivityId(String activityId, String planName) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.setActivityId(activityId);
        MaintenancePlan planExist = maintenancePlanDao.selectOne(plan);
        if (ObjectUtils.isEmpty(planExist)) {
            plan.setMaintenancePlanName(planName);
            plan.setPeriod(PeriodEnum.ONCE.getCode());
            plan.setMaintenanceStartTime(System.currentTimeMillis());
            plan.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
            addMaintenancePlan(plan);
        } else {
            planExist.setMaintenancePlanName(planName);
            modifyMaintenancePlan(planExist);
        }
    }

    /**
     * ????????????id?????????????????????????????????
     *
     * @param userId ??????id
     * @return Integer ???????????????????????????
     */
    @Override
    public Integer queryCountByUserId(String userId) {
        EntityWrapper<MaintenancePlan> wrapper = new EntityWrapper<>();
        MaintenancePlan plan = new MaintenancePlan();
        plan.setMaintenanceUser(userId);
        wrapper.setEntity(plan);
        return maintenancePlanDao.selectCount(wrapper);
    }

    /**
     * ????????????????????????
     *
     * @return ????????????
     */
    @Override
    public Result maintenancePlanAnalysis() {
        paramManageService.checkAnalyze(ManageSourceEnum.MAINTENANCE_PLAN,System.currentTimeMillis());
        //??????????????????????????????
        List<MaintenancePlan> planList = getAllPlan();
        List<ManageActivity> activityLists = Lists.newArrayList();
        for (MaintenancePlan plan: planList) {
            Integer delayType = plan.getDelayType();
            if(!ObjectUtils.isEmpty(delayType)) {
                if (DelayTypeEnum.DELAY_TWO_PERIOD_UPPER.getCode().equals(delayType)) {
                    ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),plan.getMaintenancePlanName(),
                            plan.getMaintenancePlanId(),System.currentTimeMillis(),null,"??????????????????????????????????????????",
                            ManageStatusEnum.UN_REDUCE,ManageSourceEnum.MAINTENANCE_PLAN,System.currentTimeMillis());
                    activity.setSourceType(ManageSourceTypeEnum.MAINTENANCE_PLAN_DELAY.getCode());
                    activityLists.add(activity);
                } else if(DelayTypeEnum.DELAY_ONE_PERIOD_TO_TWO_PERIOD.getCode().equals(delayType)) {
                    ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),plan.getMaintenancePlanName(),
                            plan.getMaintenancePlanId(),System.currentTimeMillis(),null,"??????????????????1-2?????????",
                            ManageStatusEnum.UN_REDUCE,ManageSourceEnum.MAINTENANCE_PLAN,System.currentTimeMillis());
                    activity.setSourceType(ManageSourceTypeEnum.MAINTENANCE_PLAN_DELAY.getCode());
                    activityLists.add(activity);
                } else if(DelayTypeEnum.DELAY_TWO_MONTH_UPPER.getCode().equals(delayType)) {
                    ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),plan.getMaintenancePlanName(),
                            plan.getMaintenancePlanId(),System.currentTimeMillis(),null,"?????????????????????????????????",
                            ManageStatusEnum.UN_REDUCE,ManageSourceEnum.MAINTENANCE_PLAN,System.currentTimeMillis());
                    activity.setSourceType(ManageSourceTypeEnum.MAINTENANCE_PLAN_DELAY.getCode());
                    activityLists.add(activity);
                } else if(DelayTypeEnum.DELAY_ONE_MONTH_TO_TWO_MONTH.getCode().equals(delayType)) {
                    ManageActivity activity = manageActivityService.createManageActivity(NineteenUUIDUtils.uuid(),plan.getMaintenancePlanName(),
                            plan.getMaintenancePlanId(),System.currentTimeMillis(),null,"??????????????????1-2??????",
                            ManageStatusEnum.UN_REDUCE,ManageSourceEnum.MAINTENANCE_PLAN,System.currentTimeMillis());
                    activity.setSourceType(ManageSourceTypeEnum.MAINTENANCE_PLAN_DELAY.getCode());
                    activityLists.add(activity);
                }
            }
        }
        List<MaintenancePlan> delayPlanList = planList.stream().filter(plan -> PlanStatusEnum.DELAY.getCode().equals(plan.getStatus())).collect(Collectors.toList());
        Map<String, List<MaintenancePlan>> systemMap = delayPlanList.stream().collect(Collectors.groupingBy(MaintenancePlan::getSystem));
        List<MaintenancePlanStatistics> statisticsList = Lists.newArrayList();
        systemMap.forEach((system, systemPlanList) -> {
            MaintenancePlanStatistics item = new MaintenancePlanStatistics();
            item.setId(NineteenUUIDUtils.uuid());
            item.setSystem(system);
            item.setDelayTotalNum(systemPlanList.size());
            Map<Integer, List<MaintenancePlan>> delayTypeMap = systemPlanList.stream().collect(Collectors.groupingBy(MaintenancePlan::getDelayType));
            List<MaintenancePlan> planList1 = delayTypeMap.get(DelayTypeEnum.DELAY_ONE_PERIOD_TO_TWO_PERIOD.getCode());
            item.setDelayOneToTwoPeriodNum(ObjectUtils.isEmpty(planList1) ? 0:planList1.size());
            List<MaintenancePlan> planList2 = delayTypeMap.get(DelayTypeEnum.DELAY_TWO_PERIOD_UPPER.getCode());
            item.setDelayTwoPeriodUpperNum(ObjectUtils.isEmpty(planList2) ? 0:planList2.size());
            List<MaintenancePlan> planList3 = delayTypeMap.get(DelayTypeEnum.DELAY_ONE_MONTH_TO_TWO_MONTH.getCode());
            item.setDelayOneToTwoMonthNum(ObjectUtils.isEmpty(planList3) ? 0:planList3.size());
            List<MaintenancePlan> planList4 = delayTypeMap.get(DelayTypeEnum.DELAY_TWO_MONTH_UPPER.getCode());
            item.setDelayTwoMonthUpperNum(ObjectUtils.isEmpty(planList4) ? 0:planList4.size());
            item.setStatisticsTime(DateFormatUtils.getMonthStartForTime(System.currentTimeMillis()));
            statisticsList.add(item);
        });
        List<MaintenancePlanStatistics> oldStatisticsList  = statisticsDao.getMaintenancePlanStatisticsDataByMonth(DateFormatUtils.getMonthStartForTime(System.currentTimeMillis()));
        paramManageService.updateParamManage(ManageSourceEnum.MAINTENANCE_PLAN,ParamTypeEnum.ANALYZE,System.currentTimeMillis());
        try {
            if(!ObjectUtils.isEmpty(oldStatisticsList)) {
                statisticsDao.batchDeleteData(oldStatisticsList);
            }
            if(!ObjectUtils.isEmpty(statisticsList)) {
                statisticsDao.batchInsertData(statisticsList);
            }
            if(!ObjectUtils.isEmpty(activityLists)) {
                manageActivityService.insertManageActivity(ManageSourceEnum.MAINTENANCE_PLAN,activityLists);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("maintenancePlan analysis error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @return ????????????
     */
    @Override
    public Result saveMaintenancePlanAnalysisReportData() {
        //????????????
        paramManageService.checkPreview(ManageSourceEnum.MAINTENANCE_PLAN);
        EntityWrapper<MaintenancePlanStatistics> wrapper = new EntityWrapper<>();
        Calendar calendar = Calendar.getInstance();
        Long currentMonthStartTime = DateFormatUtils.getThisMonthTime();
        calendar.setTimeInMillis(currentMonthStartTime);
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.MONTH, 1);
        wrapper.between(MaintenancePlanConstant.STATISTICS_TIME,calendar.getTimeInMillis(),currentMonthStartTime);
        List<MaintenancePlanStatistics> statisticsList = statisticsDao.selectList(wrapper);
        Map<String,List<MaintenancePlanStatistics>> systemMap = statisticsList.stream().collect(Collectors.groupingBy(MaintenancePlanStatistics::getSystem));
        List<MaintenancePlanAnalysisReport> reportList = Lists.newArrayList();
        systemMap.forEach((system, maintenancePlanStatistics) -> {
            Map<Long,MaintenancePlanStatistics> statisticsTimeMap = maintenancePlanStatistics.stream().collect(Collectors.toMap(MaintenancePlanStatistics::getStatisticsTime,m -> m));
            for (Long longTime = calendar.getTimeInMillis();longTime <= currentMonthStartTime;longTime = DateFormatUtils.getNextMonthTime(longTime)){
                MaintenancePlanAnalysisReport report = new MaintenancePlanAnalysisReport();
                report.setId(NineteenUUIDUtils.uuid());
                report.setSystem(system);
                report.setDelayTotalNum(0);
                report.setDelayOneToTwoPeriodNum(0);
                report.setDelayTwoPeriodUpperNum(0);
                report.setDelayOneToTwoMonthNum(0);
                report.setDelayTwoMonthUpperNum(0);
                report.setStatisticsTime(longTime);
                report.setReportTime(currentMonthStartTime);
                MaintenancePlanStatistics statistics = statisticsTimeMap.get(longTime);
                if(!ObjectUtils.isEmpty(statistics)) {
                    report.setDelayTotalNum(statistics.getDelayTotalNum());
                    report.setDelayOneToTwoPeriodNum(statistics.getDelayOneToTwoPeriodNum());
                    report.setDelayTwoPeriodUpperNum(statistics.getDelayTwoPeriodUpperNum());
                    report.setDelayOneToTwoMonthNum(statistics.getDelayOneToTwoMonthNum());
                    report.setDelayTwoMonthUpperNum(statistics.getDelayTwoMonthUpperNum());
                }
                reportList.add(report);
            }
        });
        reportList.addAll(getMaintenancePlanForecastData(reportList));
        List<MaintenancePlanAnalysisReport> oldReportList  = analysisReportDao.getMaintenancePlanAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(System.currentTimeMillis()));
        paramManageService.updateParamManage(ManageSourceEnum.MAINTENANCE_PLAN,ParamTypeEnum.PREVIEW,System.currentTimeMillis());
        try {
            if(!ObjectUtils.isEmpty(oldReportList)) {
                analysisReportDao.batchDeleteData(oldReportList);
            }
            if(!ObjectUtils.isEmpty(reportList)) {
                analysisReportDao.batchInsertData(reportList);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("save maintenancePlan analysis report data error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param monthTime
     * @return ????????????????????????????????????
     */
    @Override
    public MaintenancePlanReportDto geMaintenancePlanReportData(Long monthTime) {
        MaintenancePlanReportDto data = new MaintenancePlanReportDto();
        List<MaintenancePlanAnalysisReport> curveData = analysisReportDao.getMaintenancePlanAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        curveData = curveData.stream().sorted(Comparator.comparing(MaintenancePlanStatistics::getStatisticsTime)).collect(Collectors.toList());
        List<MaintenancePlanAnalysisReport> currentMonthData = curveData.stream().filter(report -> DateFormatUtils.getMonthStartForTime(monthTime).equals(report.getStatisticsTime())).collect(Collectors.toList());
        data.setCurrentMonthData(currentMonthData);
        data.setCurveData(curveData);
        return data;
    }

    /**
     * ??????????????????????????????
     */
    private List<MaintenancePlanAnalysisReport> getMaintenancePlanForecastData(List<MaintenancePlanAnalysisReport> reportList) {
        Map<String, List<MaintenancePlanAnalysisReport>> systemMap = reportList.stream().collect(Collectors.groupingBy(MaintenancePlanAnalysisReport::getSystem));
        List<MaintenancePlanAnalysisReport> forecastList = Lists.newArrayList();
        systemMap.forEach((system, reports) -> {
            reports = reports.stream().sorted(Comparator.comparing(MaintenancePlanAnalysisReport::getStatisticsTime)).collect(Collectors.toList());
            Long currentTime = System.currentTimeMillis();
            List<Integer> delayTotalCounts = reports.stream().map(MaintenancePlanAnalysisReport::getDelayTotalNum).collect(Collectors.toList());
            List<Integer> oneToTwoPeriodCounts = reports.stream().map(MaintenancePlanAnalysisReport::getDelayOneToTwoPeriodNum).collect(Collectors.toList());
            List<Integer> twoPeriodUpperCounts = reports.stream().map(MaintenancePlanAnalysisReport::getDelayTwoPeriodUpperNum).collect(Collectors.toList());
            List<Integer> oneToTwoMonthCounts = reports.stream().map(MaintenancePlanAnalysisReport::getDelayOneToTwoMonthNum).collect(Collectors.toList());
            List<Integer> twoMonthUpperCounts = reports.stream().map(MaintenancePlanAnalysisReport::getDelayTwoMonthUpperNum).collect(Collectors.toList());
            for (int month = 1; month < 4; month++) {
                Integer nextDelayTotalCount = getExpect(delayTotalCounts, month, MaintenancePlanConstant.SMOOTHING_COEFFICIENT);
                Integer nextOneToTwoPeriodCount = getExpect(oneToTwoPeriodCounts, month, MaintenancePlanConstant.SMOOTHING_COEFFICIENT);
                Integer nextTwoPeriodUpperCount = getExpect(twoPeriodUpperCounts, month, MaintenancePlanConstant.SMOOTHING_COEFFICIENT);
                Integer nextOneToTwoMonthCount = getExpect(oneToTwoMonthCounts, month, MaintenancePlanConstant.SMOOTHING_COEFFICIENT);
                Integer nextTwoMonthUpperCount = getExpect(twoMonthUpperCounts, month, MaintenancePlanConstant.SMOOTHING_COEFFICIENT);
                MaintenancePlanAnalysisReport report = new MaintenancePlanAnalysisReport();
                report.setId(NineteenUUIDUtils.uuid());
                report.setSystem(system);
                report.setReportTime(DateFormatUtils.getMonthStartForTime(System.currentTimeMillis()));
                currentTime = DateFormatUtils.getNextMonthTime(currentTime);
                report.setStatisticsTime(currentTime);
                report.setDelayTotalNum(nextDelayTotalCount);
                report.setDelayOneToTwoPeriodNum(nextOneToTwoPeriodCount);
                report.setDelayTwoPeriodUpperNum(nextTwoPeriodUpperCount);
                report.setDelayOneToTwoMonthNum(nextOneToTwoMonthCount);
                report.setDelayTwoMonthUpperNum(nextTwoMonthUpperCount);
                forecastList.add(report);
            }
        });
        return forecastList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param list    ??????????????????
     * @param month   ??????
     * @param modulus ????????????
     * @return ?????????
     */
    private static Integer getExpect(List<Integer> list, int month, Double modulus) {
        if (modulus >= 1 || modulus <= 0) {
            return null;
        }
        Double modulusLeft = 1 - modulus;
        Double lastIndex = Double.valueOf(list.get(0));
        Double lastSecIndex = Double.valueOf(list.get(0));
        for (Integer data : list) {
            lastIndex = modulus * data + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        Double a = 2 * lastIndex - lastSecIndex;
        Double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        Integer result = (int) Math.round(a + b * month);
        return result > 0 ? result : 0;
    }

    /**
     * ??????????????????
     *
     * @param situations ??????????????????????????????
     * @param plan
     * @return ????????????
     */
    private Integer calculationDelayTime(List<PlanExecuteSituation> situations, MaintenancePlan plan) {
        List<PlanExecuteSituation> delaySituations = situations.stream().sorted(Comparator.comparing(PlanExecuteSituation::getPlanExecuteTime))
                .filter(situation -> ExecuteStatusEnum.DELAY.getCode().equals(situation.getExecuteStatus())).collect(Collectors.toList());
        if(!ObjectUtils.isEmpty(delaySituations)) {
            Long earliestDelayTime = delaySituations.get(0).getPlanExecuteTime();
            Integer delayType = 0;
            if (!ObjectUtils.isEmpty(plan.getPeriod())) {
                Calendar calendar = Calendar.getInstance();
                Calendar current = Calendar.getInstance();
                calendar.setTimeInMillis(earliestDelayTime);
                int year = current.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
                int month;
                int currentDay = current.get(Calendar.DAY_OF_MONTH);
                int calDay = calendar.get(Calendar.DAY_OF_MONTH);
                if (currentDay < calDay) {
                    month = current.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) - 1;
                } else {
                    month = current.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
                }
                int delayMonth = year * 12 + month;
                if (PeriodEnum.WEEKLY.getCode().equals(plan.getPeriod())) {
                    int delayWeek = (int)Math.floorDiv(current.getTimeInMillis() - earliestDelayTime,7 * 24 * 60 * 60 * 1000L);
                    if (delayWeek > 2) {
                        delayType = DelayTypeEnum.DELAY_TWO_PERIOD_UPPER.getCode();
                    } else if (delayWeek > 0) {
                        delayType = DelayTypeEnum.DELAY_ONE_PERIOD_TO_TWO_PERIOD.getCode();
                    }
                } else if (PeriodEnum.MONTHLY.getCode().equals(plan.getPeriod())) {
                    if (delayMonth > 2) {
                        delayType = DelayTypeEnum.DELAY_TWO_MONTH_UPPER.getCode();
                    } else if (delayMonth > 0 ) {
                        delayType = DelayTypeEnum.DELAY_ONE_MONTH_TO_TWO_MONTH.getCode();
                    }
                }
            }
            return delayType;
        }
        return null;
    }

    /**
     * ????????????id??????????????????????????????
     *
     * @param planId ??????id
     * @return ????????????????????????
     */
    private List<PlanExecuteSituation> getSituationsByPlanId(String planId) {
        PlanExecuteSituation situation = new PlanExecuteSituation();
        situation.setMaintenancePlanId(planId);
        EntityWrapper<PlanExecuteSituation> wrapper = new EntityWrapper<>();
        wrapper.setEntity(situation);
        return situationDao.selectList(wrapper);
    }

    /**
     * ????????????
     *
     * @param key ??????key???
     * @param monthTitle ????????????title
     * @param weekTitle ????????????title
     * @param width ????????????
     * @return ????????????
     */
    private TableHeader createHeader(Object key,String monthTitle,String weekTitle,String width) {
        TableHeader header = new TableHeader();
        header.setKey(key);
        header.setMonthTitle(monthTitle);
        header.setWeekTitle(weekTitle);
        header.setWidth(width);
        return header;
    }

    /**
     * ??????????????????????????????
     * @param planName ??????????????????
     * @return ????????????
     */
    private boolean checkPlanExisted(String planName) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.setMaintenancePlanName(planName);
        EntityWrapper<MaintenancePlan> wrapper = new EntityWrapper<>();
        wrapper.setEntity(plan);
        //????????????????????????????????????
        List<MaintenancePlan> planExistList = maintenancePlanDao.selectList(wrapper);
        if(!ObjectUtils.isEmpty(planExistList)) {
            return false;
        }
        return true;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param plan ????????????????????????
     * @param year ??????
     * @return ??????????????????????????????
     */
    private List<PlanExecuteSituation> getPlanExecuteSituationList(MaintenancePlan plan, int year) {
        List<PlanExecuteSituation> planExecuteSituations = Lists.newArrayList();
        Long startTime = DateFormatUtils.getThisWeekStartTime(plan.getMaintenanceStartTime());
        plan.setMaintenanceStartTime(startTime);
        if(!ObjectUtils.isEmpty(plan.getPeriod())) {
            if(PeriodEnum.WEEKLY.getCode().equals(plan.getPeriod())) {
                if(year != 0) {
                    startTime = DateFormatUtils.getFirstWeekTimeOfYear(year);
                }
                for (Long time = startTime;time <= DateFormatUtils.getLastedWeekOfYear(startTime);time = DateFormatUtils.getNextWeekStartTime(time)) {
                    PlanExecuteSituation situation = createExecuteSituation(plan,time);
                    planExecuteSituations.add(situation);
                }
            } else if (PeriodEnum.MONTHLY.getCode().equals(plan.getPeriod())) {
                if(year != 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH,0);
                    startTime = calendar.getTimeInMillis();
                }
                if(!ObjectUtils.isEmpty(plan.getExecuteWeek())) {
                    if(PlanExecuteTimeEnum.LATEST_WEEK_OF_MOMTH.getCode().equals(plan.getExecuteWeek())) {
                        for (Long time = startTime;time <= DateFormatUtils.getLastedWeekOfYear(startTime);time = DateFormatUtils.getLastWeekOfNextMonthTime(time)) {
                            PlanExecuteSituation situation = createExecuteSituation(plan,time);
                            planExecuteSituations.add(situation);
                        }
                    } else {
                        for (Long time = startTime;time <= DateFormatUtils.getLastedWeekOfYear(startTime);time = DateFormatUtils.getSameWeekOfNextMonthTime(time)) {
                            PlanExecuteSituation situation = createExecuteSituation(plan,time);
                            planExecuteSituations.add(situation);
                        }
                    }
                }
            } else if (PeriodEnum.QUARTER.getCode().equals(plan.getPeriod())) {
                if(year != 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime);
                    calendar.set(Calendar.YEAR, year);
                    if(calendar.get(Calendar.MONTH) > 9) {
                        calendar.add(Calendar.MONTH,-9);
                    } else if(calendar.get(Calendar.MONTH) > 6){
                        calendar.add(Calendar.MONTH,-6);
                    } else if(calendar.get(Calendar.MONTH) > 3){
                        calendar.add(Calendar.MONTH,-3);
                    }
                    startTime = calendar.getTimeInMillis();
                }
                for (Long time = startTime;time <= DateFormatUtils.getLastedWeekOfYear(startTime);time = DateFormatUtils.getSameWeekOfNextQuarterTime(time)) {
                    PlanExecuteSituation situation = createExecuteSituation(plan,time);
                    planExecuteSituations.add(situation);
                }
            } else if (PeriodEnum.HALF_YEAR.getCode().equals(plan.getPeriod())) {
                if(year != 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime);
                    calendar.set(Calendar.YEAR, year);
                    if(calendar.get(Calendar.MONTH) > 6) {
                        calendar.add(Calendar.MONTH,6);
                    }
                    startTime = calendar.getTimeInMillis();
                }
                for (Long time = startTime;time <= DateFormatUtils.getLastedWeekOfYear(startTime);time = DateFormatUtils.getSameWeekOfNextHalfYearTime(time)) {
                    PlanExecuteSituation situation = createExecuteSituation(plan,time);
                    planExecuteSituations.add(situation);
                }
            } else if (PeriodEnum.YEAR.getCode().equals(plan.getPeriod())){
                if(year != 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime);
                    calendar.set(Calendar.YEAR, year);
                    startTime = calendar.getTimeInMillis();
                }
                PlanExecuteSituation situation = createExecuteSituation(plan,startTime);
                planExecuteSituations.add(situation);
            }else {
                if(year == 0) {
                    PlanExecuteSituation situation = createExecuteSituation(plan,startTime);
                    planExecuteSituations.add(situation);
                }
            }
        }
        return planExecuteSituations;
    }

    /**
     * ????????????????????????????????????
     * @param plan ????????????
     * @param executeTime ??????????????????
     * @return ??????????????????????????????
     */
    private PlanExecuteSituation createExecuteSituation(MaintenancePlan plan,Long executeTime){
        PlanExecuteSituation situation = new PlanExecuteSituation();
        situation.setId(NineteenUUIDUtils.uuid());
        situation.setMaintenancePlanId(plan.getMaintenancePlanId());
        if (PlanStatusEnum.SUSPEND.getCode().equals(plan.getStatus())) {
            situation.setExecuteStatus(ExecuteStatusEnum.PAUSED.getCode());
        } else {
            situation.setExecuteStatus(ExecuteStatusEnum.UNENFORCED.getCode());
        }
        if(DateFormatUtils.getFristWeekOfThisMonthTime(executeTime) >= DateFormatUtils.getMonthStartForTime(executeTime)
                && !PlanExecuteTimeEnum.LATEST_WEEK_OF_MOMTH.getCode().equals(plan.getExecuteWeek())
                && !PeriodEnum.WEEKLY.getCode().equals(plan.getPeriod())
                && !executeTime.equals(DateFormatUtils.getThisWeekStartTime(plan.getMaintenanceStartTime()))) {
            situation.setPlanExecuteTime(DateFormatUtils.getLastWeekStartTime(executeTime));
        } else {
            situation.setPlanExecuteTime(executeTime);
        }
        return situation;
    }

//    /**
//     *
//     * @param situation
//     * @return
//     */
//    private Long getNextExecuteTime(PlanExecuteSituation situation) {
//        MaintenancePlan plan = maintenancePlanDao.selectById(situation.getMaintenancePlanId());
//        Long nextExecuteTime = 0L;
//        if(PeriodEnum.WEEKLY.getCode().equals(plan.getPeriod())) {
//            nextExecuteTime = DateFormatUtils.getNextWeekStartTime(situation.getPlanExecuteTime());
//        } else if (PeriodEnum.MONTHLY.getCode().equals(plan.getPeriod())) {
//            if(PlanExecuteTimeEnum.LATEST_WEEK_OF_MOMTH.getCode().equals(plan.getExecuteWeek())) {
//                nextExecuteTime = DateFormatUtils.getLastWeekOfNextMonthTime(situation.getPlanExecuteTime());
//            } else {
//                nextExecuteTime = DateFormatUtils.getSameWeekOfNextMonthTime(situation.getPlanExecuteTime());
//            }
//        } else if (PeriodEnum.QUARTER.getCode().equals(plan.getPeriod())) {
//            nextExecuteTime = DateFormatUtils.getSameWeekOfNextQuarterTime(situation.getPlanExecuteTime());
//        } else if (PeriodEnum.HALF_YEAR.getCode().equals(plan.getPeriod())) {
//            nextExecuteTime = DateFormatUtils.getSameWeekOfNextHalfYearTime(situation.getPlanExecuteTime());
//        } else if (PeriodEnum.YEAR.getCode().equals(plan.getPeriod())) {
//            nextExecuteTime = DateFormatUtils.getSameWeekOfNextYearTime(situation.getPlanExecuteTime());
//        }
//        return nextExecuteTime;
//    }
}
