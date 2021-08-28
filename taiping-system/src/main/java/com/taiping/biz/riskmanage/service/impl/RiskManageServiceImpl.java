package com.taiping.biz.riskmanage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taiping.bean.riskmanage.RiskCurrentAnalysisDto;
import com.taiping.bean.riskmanage.RiskItemDto;
import com.taiping.bean.riskmanage.RiskTrendAnalysisVo;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.riskmanage.dao.*;
import com.taiping.biz.riskmanage.service.IRiskManageService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.biz.user.dao.UserInfoDao;
import com.taiping.constant.riskmanage.RiskManageConstant;
import com.taiping.constant.riskmanage.RiskManageResultCode;
import com.taiping.constant.riskmanage.RiskManageResultMsg;
import com.taiping.entity.*;
import com.taiping.entity.riskmanage.*;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.system.SystemSetting;
import com.taiping.entity.user.UserInfo;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.enums.riskmanage.CreatingModeEnum;
import com.taiping.enums.riskmanage.RecheckResultEnum;
import com.taiping.enums.riskmanage.RiskLevelEnum;
import com.taiping.enums.riskmanage.RiskTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangliangyu
 * @since 2019/10/24
 * 风控管理逻辑层接口实现
 */
@Service
@Slf4j
public class RiskManageServiceImpl extends ServiceImpl<RiskItemDao, RiskItem> implements IRiskManageService {

    /**
     * 风险项管理持久层接口
     */
    @Autowired
    private RiskItemDao riskItemDao;
    /**
     * 风险现状分析持久层接口
     */
    @Autowired
    private RiskCurrentAnalysisDao riskCurrentAnalysisDao;
    /**
     * 风险现状分析报告持久层接口
     */
    @Autowired
    private RiskCurrentAnalysisReportDao currentAnalysisReportDao;
    /**
     * 风险趋势分析持久层接口
     */
    @Autowired
    private RiskTrendAnalysisDao riskTrendAnalysisDao;
    /**
     * 风险趋势分析报告持久层接口
     */
    @Autowired
    private RiskTrendAnalysisReportDao trendAnalysisReportDao;
    /**
     * 风险等级趋势分析持久层接口
     */
    @Autowired
    private RiskLevelTrendDao riskLevelTrendDao;
    /**
     * 风险等级趋势分析报告持久层接口
     */
    @Autowired
    private RiskLevelTrendReportDao levelTrendReportDao;
    /**
     * 类型参数管理服务实现层
     */
    @Autowired
    private ParamManageService paramManageService;
    /**
     * 用户管理持久层接口
     */
    @Autowired
    private UserInfoDao userInfoDao;

    /**
     * 系统配置服务
     */
    @Autowired
    private SystemService systemService;


    /**
     * 获取所有风险项
     *
     * @return List<RiskItem> 风险项列表
     */
    @Override
    public List<RiskItem> getAllRiskItem() {
        return riskItemDao.selectAllRiskItem();
    }

    /**
     * 分页查询风险项列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean queryRiskItemByCondition(QueryCondition<RiskItem> queryCondition) {
        //构建分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        List<FilterCondition> filterConditions = queryCondition.getFilterConditions();
        for (FilterCondition condition : filterConditions) {
            if (RiskManageConstant.FOUND_DATE.equals(condition.getFilterField())) {
                if (RiskManageConstant.GREATER_THAN_OR_EQUAL.equals(condition.getOperator())) {
                    condition.setFilterValue(DateFormatUtils.getDayStartForTime((Long) condition.getFilterValue()));
                } else {
                    condition.setFilterValue(DateFormatUtils.getTimeLongNextDay((Long) condition.getFilterValue()));
                }
            }
        }
        EntityWrapper<RiskItem> wrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        return getRiskItemDataByCondition(page, wrapper);
    }

    /**
     * 根据id查询风险项
     *
     * @param riskItemId 风险项id
     * @return 风险项信息
     */
    @Override
    public RiskItemDto getRiskItemById(String riskItemId) {
        RiskItemDto riskItemDto = new RiskItemDto();
        RiskItem riskItem = riskItemDao.selectById(riskItemId);
        BeanUtils.copyProperties(riskItem, riskItemDto);
        if (!ObjectUtils.isEmpty(riskItem.getTrackUser())) {
            UserInfo trackUser = userInfoDao.getUserInfoById(riskItem.getTrackUser());
            if(!ObjectUtils.isEmpty(trackUser)) {
                riskItemDto.setTrackUserName(trackUser.getUserRealName());
            }
        }
        if (!ObjectUtils.isEmpty(riskItem.getCheckUser())) {
            UserInfo checkUser = userInfoDao.getUserInfoById(riskItem.getCheckUser());
            if(!ObjectUtils.isEmpty(checkUser)) {
                riskItemDto.setCheckUserName(checkUser.getUserRealName());
            }
        }
        return riskItemDto;
    }

    /**
     * 根据风险追踪负责人查询风险项
     *
     * @param trackUser 风险追踪负责人
     * @return 风险项列表
     */
    @Override
    public List<RiskItem> getRiskItemsByTrackUser(String trackUser) {
        return riskItemDao.selectRiskItemsByTrackUser(trackUser);
    }

    /**
     * 根据复检人查询风险项
     *
     * @param checkUser 复检人
     * @return 风险项列表
     */
    @Override
    public List<RiskItem> getRiskItemsByCheckUser(String checkUser) {
        return riskItemDao.selectRiskItemByCheckUser(checkUser);
    }

    /**
     * 根据相关运维活动id查询风险项
     *
     * @param activityId 相关运维活动id
     * @return 风险项列表
     */
    @Override
    public RiskItem getRiskItemByActivityId(String activityId) {
        return riskItemDao.selectRiskItemByActivityId(activityId);
    }

    /**
     * 添加风险项
     *
     * @param riskItem 需添加的风险项
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addRiskItem(RiskItem riskItem) {
        try {
            addRiskItemInside(riskItem);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add riskItem error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 添加风险项
     *
     * @param riskItem 需添加的风险项
     */
    @Override
    public void addRiskItemInside(RiskItem riskItem) {
        riskItem.setRiskItemId(NineteenUUIDUtils.uuid());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(riskItem.getFoundDate());
        riskItem.setRiskFoundYear(calendar.get(Calendar.YEAR));
        riskItem.setRiskFoundMonth(calendar.get(Calendar.MONTH) + 1);
        riskItem.setIsDeleted(0);
        riskItem.setCreateTime(System.currentTimeMillis());
        riskItemDao.insert(riskItem);
    }

    /**
     * 修改风险项
     *
     * @param riskItem 需修改的风险项
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyRiskItem(RiskItem riskItem) {
        //验证风险项是否存在
        RiskItem riskItemExist = riskItemDao.selectById(riskItem.getRiskItemId());
        if (ObjectUtils.isEmpty(riskItemExist)) {
            throw new BizException(RiskManageResultCode.RISK_ITEM_NOT_EXISTED,RiskManageResultMsg.RISK_ITEM_NOT_EXISTED);
        }
        //设置风险级别
        setRiskLevel(riskItem);
        //设置修改计划完成时间语音
        setUpdateReason(riskItem,riskItemExist);
        String referAnnexName = riskItemExist.getReferAnnexName();
        if (ObjectUtils.isEmpty(referAnnexName)) {
            referAnnexName = riskItem.getReferAnnexName();
        } else if (!ObjectUtils.isEmpty(riskItem.getReferAnnexName())){
            referAnnexName = referAnnexName + RiskManageConstant.SEMICOLON + riskItem.getReferAnnexName();
        }
        riskItem.setReferAnnexName(referAnnexName);
        if (!ObjectUtils.isEmpty(riskItem.getFoundDate())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(riskItem.getFoundDate());
            riskItem.setRiskFoundYear(calendar.get(Calendar.YEAR));
            riskItem.setRiskFoundMonth(calendar.get(Calendar.MONTH) + 1);
        }
        //设置超时时间
        Long actualResolutionTime = riskItem.getActualResolutionTime();
        riskItem.setActualResolutionTime(actualResolutionTime);
        Long planResolutionTime = riskItem.getPlanResolutionTime();
        if (!ObjectUtils.isEmpty(actualResolutionTime) && actualResolutionTime > planResolutionTime) {
            Long timeInterval = actualResolutionTime - planResolutionTime;
            riskItem.setTimeoutTime((int) (timeInterval / (60 * 60 * 24 * 1000)));
        }
        riskItem.setUpdateTime(System.currentTimeMillis());
        try {
            riskItemDao.updateById(riskItem);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("modify riskItem error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }



    /**
     * 删除风险项
     *
     * @param riskItemIds 需删除的风险项id列表
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteRiskItem(List<String> riskItemIds) {
        try {
            riskItemDao.deleteBatchIds(riskItemIds);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("delete riskItem error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

//    /**
//     * 风险分值登记
//     *
//     * @param riskScoreVo 风险分值实体
//     * @return 登记结果
//     */
//    @Transactional
//    @Override
//    public Result riskScoreRegister(RiskScoreVo riskScoreVo) {
//        //验证风险项是否存在
//        RiskItem riskItem = riskItemDao.selectById(riskScoreVo.getRiskItemId());
//        if (ObjectUtils.isEmpty(riskItem)) {
//            return ResultUtils.warn(RiskManageResultCode.RISK_ITEM_NOT_EXISTED, "风险项不存在");
//        }
//        riskItem.setSerialEffectScore(riskScoreVo.getSerialEffectScore());
//        riskItem.setSystemLevelScore(riskScoreVo.getSystemLevelScore());
//        riskItem.setHighUseEffectScore(riskScoreVo.getHighUseEffectScore());
//        riskItem.setRiskHappenProbScore(riskScoreVo.getRiskHappenProbScore());
//        riskItem.setRiskScore(riskScoreVo.getRiskScore());
//        riskItem.setUpdateTime(System.currentTimeMillis());
//        List<SystemSetting> settings = systemService.querySystemValueByCode("5020");
//        double lowRiskThreshold = 0;
//        double highRiskThreshold = 0;
//        for (SystemSetting setting : settings) {
//            if (setting.getCode().equals("502010")) {
//                lowRiskThreshold = (double) setting.getValue();
//            }
//            if (setting.getCode().equals("502020")) {
//                highRiskThreshold = (double) setting.getValue();
//            }
//        }
//        if (riskScoreVo.getRiskScore() < lowRiskThreshold) {
//            riskItem.setRiskLevel(RiskLevelEnum.LOW.getLevelCode());
//        } else if (riskScoreVo.getRiskScore() > highRiskThreshold) {
//            riskItem.setRiskLevel(RiskLevelEnum.HIGH.getLevelCode());
//        } else {
//            riskItem.setRiskLevel(RiskLevelEnum.MIDDLE.getLevelCode());
//        }
//        try {
//            riskItemDao.updateById(riskItem);
//            return ResultUtils.success();
//        } catch (Exception e) {
//            throw new RuntimeException("数据库操作失败" + e);
//        }
//    }
//
//    /**
//     * 风险追踪负责人指派
//     *
//     * @param riskTrackUserAssignVo 风险追踪负责人指派信息
//     * @return 指派结果
//     */
//    @Transactional
//    @Override
//    public Result riskTrackUserAssign(RiskTrackUserAssignVo riskTrackUserAssignVo) {
//        //验证风险项是否存在
//        RiskItem riskItem = riskItemDao.selectById(riskTrackUserAssignVo.getRiskItemId());
//        if (ObjectUtils.isEmpty(riskItem)) {
//            return ResultUtils.warn(RiskManageResultCode.RISK_ITEM_NOT_EXISTED, "风险项不存在");
//        }
//        riskItem.setTrackUser(riskTrackUserAssignVo.getTrackUser());
//        riskItem.setUpdateTime(System.currentTimeMillis());
//        try {
//            riskItemDao.updateById(riskItem);
//            return ResultUtils.success();
//        } catch (Exception e) {
//            throw new RuntimeException("数据库操作失败" + e);
//        }
//    }
//
//    /**
//     * 风险应对方案提交
//     *
//     * @param riskResponsePlanVo 风险应对方案信息
//     * @return 提交结果
//     */
//    @Transactional
//    @Override
//    public Result riskResponsePlanCommit(RiskResponsePlanVo riskResponsePlanVo) {
//        //验证风险项是否存在
//        RiskItem riskItem = riskItemDao.selectById(riskResponsePlanVo.getRiskItemId());
//        if (ObjectUtils.isEmpty(riskItem)) {
//            return ResultUtils.warn(RiskManageResultCode.RISK_ITEM_NOT_EXISTED, "风险项不存在");
//        }
//        riskItem.setResponsePlan(riskResponsePlanVo.getResponsePlan());
//        String referAnnexName = riskItem.getReferAnnexName();
//        if (!ObjectUtils.isEmpty(referAnnexName)) {
//            referAnnexName = riskResponsePlanVo.getReferAnnexName();
//        } else {
//            referAnnexName = referAnnexName + ";" + riskResponsePlanVo.getReferAnnexName();
//        }
//        riskItem.setReferAnnexName(referAnnexName);
//        riskItem.setUpdateTime(System.currentTimeMillis());
//        try {
//            riskItemDao.updateById(riskItem);
//            return ResultUtils.success();
//        } catch (Exception e) {
//            throw new RuntimeException("数据库操作失败" + e);
//        }
//    }
//
//    /**
//     * 风险处理进度更新
//     *
//     * @param riskProcessinProgressVo 风险处理进度信息
//     * @return 更新结果
//     */
//    @Transactional
//    @Override
//    public Result riskProcessinProgressUpdate(RiskProcessinProgressVo riskProcessinProgressVo) {
//        //验证风险项是否存在
//        RiskItem riskItem = riskItemDao.selectById(riskProcessinProgressVo.getRiskItemId());
//        if (ObjectUtils.isEmpty(riskItem)) {
//            return ResultUtils.warn(RiskManageResultCode.RISK_ITEM_NOT_EXISTED, "风险项不存在");
//        }
//        riskItem.setProcessProgress(riskProcessinProgressVo.getProcessProgress());
//        riskItem.setProgressUpdateDescription(riskProcessinProgressVo.getProgressUpdateDescription());
//        riskItem.setResolveStatus(riskProcessinProgressVo.getResolveStatus());
//        Long actualResolutionTime = riskProcessinProgressVo.getActualResolutionTime();
//        riskItem.setActualResolutionTime(actualResolutionTime);
//        Long planResolutionTime = riskItem.getPlanResolutionTime();
//        if (!ObjectUtils.isEmpty(actualResolutionTime) && actualResolutionTime > planResolutionTime) {
//            Long timeInterval = actualResolutionTime - planResolutionTime;
//            riskItem.setTimeoutTime((int) (timeInterval / (60 * 60 * 24 * 1000)));
//        }
//        riskItem.setUpdateTime(System.currentTimeMillis());
//        try {
//            riskItemDao.updateById(riskItem);
//            return ResultUtils.success();
//        } catch (Exception e) {
//            throw new RuntimeException("数据库操作失败" + e);
//        }
//    }
//
//    /**
//     * 更新风险项复检信息
//     *
//     * @param riskItemRecheckVo 风险项复检信息
//     * @return 更新结果
//     */
//    @Transactional
//    @Override
//    public Result riskItemRecheck(RiskItemRecheckVo riskItemRecheckVo) {
//        //验证风险项是否存在
//        RiskItem riskItem = riskItemDao.selectById(riskItemRecheckVo.getRiskItemId());
//        if (ObjectUtils.isEmpty(riskItem)) {
//            return ResultUtils.warn(RiskManageResultCode.RISK_ITEM_NOT_EXISTED, "风险项不存在");
//        }
//        riskItem.setCheckUser(riskItemRecheckVo.getCheckUser());
//        riskItem.setCheckTime(riskItemRecheckVo.getCheckTime());
//        riskItem.setCheckResult(riskItemRecheckVo.getCheckResult());
//        riskItem.setUpdateTime(System.currentTimeMillis());
//        try {
//            riskItemDao.updateById(riskItem);
//            return ResultUtils.success();
//        } catch (Exception e) {
//            throw new RuntimeException("数据库操作失败" + e);
//        }
//    }

    /**
     * 更新分析数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateAnalysisData() {
        updateCurrentAnalysisData();
        updateTrendAnalysisData();
        updateRiskLevelTrendData();
        paramManageService.updateParamManage(ManageSourceEnum.RISK_MANAGE, ParamTypeEnum.ANALYZE, System.currentTimeMillis());
        return ResultUtils.success();
    }

    /**
     * 设置风险级别
     *
     * @param riskItem 风险项信息
     */
    private void setRiskLevel(RiskItem riskItem) {
        List<SystemSetting> settings = systemService.querySystemValueByCode(RiskManageConstant.RISK_LEVEL_SECOND_CODE);
        double lowRiskThreshold = 0;
        double highRiskThreshold = 0;
        for (SystemSetting setting : settings) {
            if (setting.getCode().equals(RiskManageConstant.LOW_LEVEL_RISK_CODE)) {
                lowRiskThreshold = (double) setting.getValue();
            }
            if (setting.getCode().equals(RiskManageConstant.HIGH_LEVEL_RISK_CODE)) {
                highRiskThreshold = (double) setting.getValue();
            }
        }
        if (!ObjectUtils.isEmpty(riskItem.getRiskScore())) {
            if (riskItem.getRiskScore() < lowRiskThreshold) {
                riskItem.setRiskLevel(RiskLevelEnum.LOW.getLevelCode());
            } else if (riskItem.getRiskScore() > highRiskThreshold) {
                riskItem.setRiskLevel(RiskLevelEnum.HIGH.getLevelCode());
            } else {
                riskItem.setRiskLevel(RiskLevelEnum.MIDDLE.getLevelCode());
            }
        }
    }

    /**
     * 设置修改计划完成时间语音
     *
     * @param riskItem 修改后的风险项信息
     * @param riskItemExist 修改前的风险项信息
     */
    private void setUpdateReason(RiskItem riskItem, RiskItem riskItemExist) {
//        if(riskItem)
    }

    /**
     * 更新风险现状分析数据
     */
    private void updateCurrentAnalysisData() {
        //获取所有风险项
        List<RiskItem> riskItemList = riskItemDao.selectAllRiskItem();
        //筛选未关闭的风险项
        List<RiskItem> riskItems = riskItemList.stream().filter(riskItem -> (!RecheckResultEnum.RESOLVED.getCode().equals(riskItem.getCheckResult()))).collect(Collectors.toList());
        //组装风险现状分析基础数据
        List<RiskCurrentAnalysis> riskCurrentAnalysisList = Lists.newArrayList();
        //获取风险超时时间阈值
        List<SystemSetting> settings = systemService.querySystemValueByCode(RiskManageConstant.RISK_TIMEOUT_TIME_SECOND_CODE);
        List<Double> doubleSettings = Lists.newArrayList();
        for (SystemSetting setting : settings) {
            if (setting.isCheckStatus() && !ObjectUtils.isEmpty(setting.getValue())) {
                doubleSettings.add((double) setting.getValue());
            }
        }
        riskItems.forEach(riskItem -> {
            if (!ObjectUtils.isEmpty(riskItem.getTimeoutTime())) {
                RiskCurrentAnalysis riskCurrentAnalysis = new RiskCurrentAnalysis();
                riskCurrentAnalysis.setRiskItemId(riskItem.getRiskItemId());
                riskCurrentAnalysis.setRiskItemName(riskItem.getRiskItemName());
                riskCurrentAnalysis.setRiskLevel(riskItem.getRiskLevel());
                riskCurrentAnalysis.setTimeoutTime(riskItem.getTimeoutTime() / 30);
                for (Double doubleSetting : doubleSettings) {
                    if (riskCurrentAnalysis.getTimeoutTime() >= doubleSetting) {
                        riskCurrentAnalysis.setThreshold(doubleSetting);
                    }
                }
                riskCurrentAnalysisList.add(riskCurrentAnalysis);
            }
        });
        //保存风险现状分析基础数据
        saveRiskCurrentData(riskCurrentAnalysisList);
    }

    /**
     * 保存风险现状分析基础数据
     *
     * @param baseData 风险现状分析基础数据
     */
    private void saveRiskCurrentData(List<RiskCurrentAnalysis> baseData) {
        if (!ObjectUtils.isEmpty(baseData.toArray())) {
            List<RiskCurrentAnalysis> oldCurrentAnalyses = riskCurrentAnalysisDao.selectRiskCurrentAnalysisData();
            List<RiskCurrentAnalysis> addList = Lists.newArrayList();
            List<RiskCurrentAnalysis> updateList = Lists.newArrayList();
            List<String> deleteIdList = Lists.newArrayList();
            List<String> oldRiskItemIds = oldCurrentAnalyses.stream().map(RiskCurrentAnalysis::getRiskItemId).collect(Collectors.toList());
            List<String> newRiskItemIds = baseData.stream().map(RiskCurrentAnalysis::getRiskItemId).collect(Collectors.toList());
            for (RiskCurrentAnalysis currentAnalysis : baseData) {
                if (oldRiskItemIds.contains(currentAnalysis.getRiskItemId())) {
                    updateList.add(currentAnalysis);
                } else {
                    currentAnalysis.setId(NineteenUUIDUtils.uuid());
                    addList.add(currentAnalysis);
                }
            }
            for (RiskCurrentAnalysis riskCurrentAnalysis : oldCurrentAnalyses) {
                if (!newRiskItemIds.contains(riskCurrentAnalysis.getRiskItemId())) {
                    deleteIdList.add(riskCurrentAnalysis.getId());
                }
            }
            try {
                if (!ObjectUtils.isEmpty(addList)) {
                    riskCurrentAnalysisDao.batchInsertCurrentAnalysisData(addList);
                }
                if (!ObjectUtils.isEmpty(updateList)) {
                    riskCurrentAnalysisDao.batchUpdateCurrentAnalysisData(updateList);
                }
                if (!ObjectUtils.isEmpty(deleteIdList)) {
                    riskCurrentAnalysisDao.batchDeleteCurrentAnalysisData(deleteIdList);
                }
            } catch (Exception e) {
                log.error("save riskCurrentData error", e);
                throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
            }
        }
    }

    /**
     * 获取风险现状分析数据
     *
     * @return 险现状分析数据列表
     */
    @Override
    public List<RiskCurrentAnalysisDto> getRiskCurrentAnalysisData() {
        //获取风险现状分析基础数据
        List<RiskCurrentAnalysis> riskCurrentAnalysisList = riskCurrentAnalysisDao.selectRiskCurrentAnalysisData();
        return processCurrentData(riskCurrentAnalysisList);
    }


    /**
     * 更新风险趋势分析数据
     */
    private void updateTrendAnalysisData() {
        //获取风险趋势筛选统计结果
        List<RiskTrendStatistics> riskTrendStatisticsData = riskItemDao.selectRiskTrendStatisticsData();
        riskTrendStatisticsData = riskTrendStatisticsData.stream().filter(data -> !ObjectUtils.isEmpty(data.getRiskType())).collect(Collectors.toList());
        List<RiskTrendAnalysis> riskTrendAnalysisList = Lists.newArrayList();
        Map<String, List<RiskTrendStatistics>> statisticsDataMap = riskTrendStatisticsData.stream().collect(Collectors.groupingBy(o -> o.getRiskType().toString() + o.getRiskFoundYear() + o.getRiskFoundMonth()));
        List<SystemInputSetting> systemInputSettings = systemService.queryInputValueByCode("1060");
        for (List<RiskTrendStatistics> trendStatistics : statisticsDataMap.values()) {
            RiskTrendAnalysis riskTrendAnalysis = new RiskTrendAnalysis();
            riskTrendAnalysis.setId(NineteenUUIDUtils.uuid());
            RiskTrendStatistics trendStatistic = trendStatistics.get(0);
            Integer advancedRiskNumber = 0;
            Integer intermediateRiskNumber = 0;
            Integer lowRiskNumber = 0;
            Double advancedRiskRingGrowth = null;
            Double intermediateRiskRingGrowth = null;
            Double lowRiskRingGrowth = null;
            Double advancedRiskYearOverYear = null;
            Double intermediateRiskYearOverYear = null;
            Double lowRiskYearOverYear = null;
            for (SystemInputSetting setting : systemInputSettings) {
                if (setting.getCode().equals(trendStatistic.getRiskType().toString())) {
                    riskTrendAnalysis.setRiskType(setting.getCodeName());
                    Integer foundYear = trendStatistic.getRiskFoundYear();
                    Integer foundMonth = trendStatistic.getRiskFoundMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, foundYear);
                    calendar.set(Calendar.MONTH, foundMonth - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    riskTrendAnalysis.setRiskFoundTime(calendar.getTimeInMillis());
                }
            }
            for (RiskTrendStatistics data : trendStatistics) {
                //设置高等级风险项数量
                if (RiskLevelEnum.HIGH.getLevelCode().equals(data.getRiskLevel())) {
                    advancedRiskNumber = data.getCount();
                }
                //设置中等级风险项数量
                if (RiskLevelEnum.MIDDLE.getLevelCode().equals(data.getRiskLevel())) {
                    intermediateRiskNumber = data.getCount();
                }
                //设置低等级风险项数量
                if (RiskLevelEnum.LOW.getLevelCode().equals(data.getRiskLevel())) {
                    lowRiskNumber = data.getCount();
                }
            }
            String lastMonthKey;
            if (trendStatistic.getRiskFoundMonth() == 1) {
                lastMonthKey = trendStatistic.getRiskType().toString() + (trendStatistic.getRiskFoundYear() - 1) + 12;
            } else {
                lastMonthKey = trendStatistic.getRiskType().toString() + trendStatistic.getRiskFoundYear() + (trendStatistic.getRiskFoundMonth() - 1);
            }
            List<RiskTrendStatistics> lastMonthData = statisticsDataMap.get(lastMonthKey);
            if (!ObjectUtils.isEmpty(lastMonthData)) {
                for (RiskTrendStatistics riskTrendStatistics : lastMonthData) {
                    //设置高等级风险项环比
                    if (RiskLevelEnum.HIGH.getLevelCode().equals(riskTrendStatistics.getRiskLevel())) {
                        advancedRiskRingGrowth = (advancedRiskNumber - riskTrendStatistics.getCount()) / ((riskTrendStatistics.getCount() * 1.0));
                    }
                    //设置中等级风险项环比
                    if (RiskLevelEnum.MIDDLE.getLevelCode().equals(riskTrendStatistics.getRiskLevel())) {
                        intermediateRiskRingGrowth = (intermediateRiskNumber - riskTrendStatistics.getCount()) / ((riskTrendStatistics.getCount() * 1.0));
                    }
                    //设置低等级风险项环比
                    if (RiskLevelEnum.LOW.getLevelCode().equals(riskTrendStatistics.getRiskLevel())) {
                        lowRiskRingGrowth = (lowRiskNumber - riskTrendStatistics.getCount()) / ((riskTrendStatistics.getCount() * 1.0));
                    }
                }
            }
            String lastYearKey = trendStatistic.getRiskType().toString() + (trendStatistic.getRiskFoundYear() - 1) + (trendStatistic.getRiskFoundMonth());
            List<RiskTrendStatistics> lastYearData = statisticsDataMap.get(lastYearKey);
            if (!ObjectUtils.isEmpty(lastYearData)) {
                for (RiskTrendStatistics trendStatisticsData : lastYearData) {
                    //设置低等级风险项同比
                    if (RiskLevelEnum.LOW.getLevelCode().equals(trendStatisticsData.getRiskLevel())) {
                        lowRiskYearOverYear = (lowRiskNumber - trendStatisticsData.getCount()) / ((trendStatisticsData.getCount() * 1.0));
                    }
                    //设置中等级风险项同比
                    if (RiskLevelEnum.MIDDLE.getLevelCode().equals(trendStatisticsData.getRiskLevel())) {
                        intermediateRiskYearOverYear = (intermediateRiskNumber - trendStatisticsData.getCount()) / ((trendStatisticsData.getCount() * 1.0));
                    }
                    //设置高等级风险项同比
                    if (RiskLevelEnum.HIGH.getLevelCode().equals(trendStatisticsData.getRiskLevel())) {
                        advancedRiskYearOverYear = (advancedRiskNumber - trendStatisticsData.getCount()) / ((trendStatisticsData.getCount() * 1.0));
                    }
                }
            }
            riskTrendAnalysis.setAdvancedRiskNumber(advancedRiskNumber);
            riskTrendAnalysis.setIntermediateRiskNumber(intermediateRiskNumber);
            riskTrendAnalysis.setLowRiskNumber(lowRiskNumber);
            riskTrendAnalysis.setAdvancedRiskRingGrowth(advancedRiskRingGrowth);
            riskTrendAnalysis.setIntermediateRiskRingGrowth(intermediateRiskRingGrowth);
            riskTrendAnalysis.setLowRiskRingGrowth(lowRiskRingGrowth);
            riskTrendAnalysis.setAdvancedRiskYearOverYear(advancedRiskYearOverYear);
            riskTrendAnalysis.setIntermediateRiskYearOverYear(intermediateRiskYearOverYear);
            riskTrendAnalysis.setLowRiskYearOverYear(lowRiskYearOverYear);
            riskTrendAnalysisList.add(riskTrendAnalysis);
        }

        riskTrendAnalysisList.addAll(getRiskTrendForecastData(riskTrendAnalysisList));
        riskTrendAnalysisList = riskTrendAnalysisList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                // 根据风险项类型和风险发现时间去重
                () -> new TreeSet<>(Comparator.comparing(o -> o.getRiskType() + o.getRiskFoundTime()))), ArrayList::new));
        //保存风险趋势分析基础数据
        saveRiskTrendData(riskTrendAnalysisList);
    }

    /**
     * 保存风险趋势分析基本数据
     *
     * @param baseData 风险趋势分析基本数据
     */
    private void saveRiskTrendData(List<RiskTrendAnalysis> baseData) {
        if (!ObjectUtils.isEmpty(baseData.toArray())) {
            List<RiskTrendAnalysis> oldTrendAnalyses = riskTrendAnalysisDao.selectRiskTrendAnalysisData();
            List<RiskTrendAnalysis> addList = Lists.newArrayList();
            List<RiskTrendAnalysis> updateList = Lists.newArrayList();
            List<String> deleteIdList = Lists.newArrayList();
            Map<String, RiskTrendAnalysis> oldMap = Maps.newHashMap();
            Map<String, RiskTrendAnalysis> newMap = Maps.newHashMap();
            for (RiskTrendAnalysis riskTrendAnalysis : oldTrendAnalyses) {
                String key = riskTrendAnalysis.getRiskType() + RiskManageConstant.LINK_SYMBOL + riskTrendAnalysis.getRiskFoundTime();
                oldMap.put(key, riskTrendAnalysis);
            }
            for (RiskTrendAnalysis trendAnalysis : baseData) {
                String key = trendAnalysis.getRiskType() + RiskManageConstant.LINK_SYMBOL + trendAnalysis.getRiskFoundTime();
                newMap.put(key, trendAnalysis);
            }
            newMap.forEach((s, riskTrend) -> {
                if (oldMap.keySet().contains(s)) {
                    riskTrend.setId(oldMap.get(s).getId());
                    updateList.add(riskTrend);
                } else {
                    addList.add(riskTrend);
                }
            });
            oldMap.forEach((k, riskAnalysis) -> {
                if (!newMap.keySet().contains(k)) {
                    deleteIdList.add(riskAnalysis.getId());
                }
            });
            try {
                if (!ObjectUtils.isEmpty(addList)) {
                    riskTrendAnalysisDao.batchInsertTrendAnalysisData(addList);
                }
                if (!ObjectUtils.isEmpty(updateList)) {
                    riskTrendAnalysisDao.batchUpdateTrendAnalysisData(updateList);
                }
                if (!ObjectUtils.isEmpty(deleteIdList)) {
                    riskTrendAnalysisDao.batchDeleteTrendAnalysisData(deleteIdList);
                }
            } catch (Exception e) {
                log.error("save riskTrendData error", e);
                throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
            }
        }
    }

    /**
     * 风险趋势预测
     */
    private List<RiskTrendAnalysis> getRiskTrendForecastData(List<RiskTrendAnalysis> riskTrendAnalyses) {
        Optional<RiskTrendAnalysis> optionalAnalysis = riskTrendAnalyses.stream().min(Comparator.comparingLong(RiskTrendAnalysis::getRiskFoundTime));
        Long earliestTime = optionalAnalysis.get().getRiskFoundTime();
        List<RiskTrendAnalysis> baseData = nullProcesser(riskTrendAnalyses, earliestTime, DateFormatUtils.getThisMonthTime());
        Map<String, List<RiskTrendAnalysis>> riskTrendMap = baseData.stream().collect(Collectors.groupingBy(RiskTrendAnalysis::getRiskType));
        List<RiskTrendAnalysis> forecastList = Lists.newArrayList();
        riskTrendMap.forEach((riskTye, trendAnalyses) -> {
            trendAnalyses = trendAnalyses.stream().sorted(Comparator.comparing(RiskTrendAnalysis::getRiskFoundTime)).collect(Collectors.toList());
            Long currentTime = System.currentTimeMillis();
            List<Integer> highCounts = trendAnalyses.stream().map(RiskTrendAnalysis::getAdvancedRiskNumber).collect(Collectors.toList());
            List<Integer> middleCounts = trendAnalyses.stream().map(RiskTrendAnalysis::getIntermediateRiskNumber).collect(Collectors.toList());
            List<Integer> lowCounts = trendAnalyses.stream().map(RiskTrendAnalysis::getLowRiskNumber).collect(Collectors.toList());
            for (int month = 1; month < 4; month++) {
                Integer nextHighCount = getExpect(highCounts, month, RiskManageConstant.SMOOTHING_COEFFICIENT);
                Integer nextMiddleCount = getExpect(middleCounts, month, RiskManageConstant.SMOOTHING_COEFFICIENT);
                Integer nextLowCount = getExpect(lowCounts, month, RiskManageConstant.SMOOTHING_COEFFICIENT);
                RiskTrendAnalysis riskTrendAnalysis = new RiskTrendAnalysis();
                riskTrendAnalysis.setId(NineteenUUIDUtils.uuid());
                riskTrendAnalysis.setRiskType(riskTye);
                currentTime = DateFormatUtils.getNextMonthTime(currentTime);
                riskTrendAnalysis.setRiskFoundTime(currentTime);
                riskTrendAnalysis.setAdvancedRiskNumber(nextHighCount);
                riskTrendAnalysis.setIntermediateRiskNumber(nextMiddleCount);
                riskTrendAnalysis.setLowRiskNumber(nextLowCount);
                forecastList.add(riskTrendAnalysis);
            }
        });
        return forecastList;
    }

    /**
     * 二次指数平滑法求预测值
     *
     * @param list    基础数据集合
     * @param month   月数
     * @param modulus 平滑系数
     * @return 预测值
     */
    private static Integer getExpect(List<Integer> list, int month, Double modulus) {
        if (modulus <= 0 || modulus >= 1) {
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
     * 分页查询风险趋势分析数据
     *
     * @param queryCondition 查询条件
     * @return 险现状分析数据列表
     */
    @Override
    public PageBean getRiskTrendAnalysisData(QueryCondition<RiskTrendAnalysisVo> queryCondition) {
        //构建查询条件
        QueryCondition<RiskTrendAnalysis> condition = new QueryCondition<>();
        RiskTrendAnalysis riskTrendAnalysis = new RiskTrendAnalysis();
        RiskTrendAnalysisVo trendAnalysisVo = queryCondition.getBizCondition();
        //过滤条件
        List<FilterCondition> filterConditions = queryCondition.getFilterConditions();
        if (!ObjectUtils.isEmpty(trendAnalysisVo)) {
            //业务条件
            if (!ObjectUtils.isEmpty(trendAnalysisVo.getRiskType())) {
                riskTrendAnalysis.setRiskType(RiskTypeEnum.getNameByCode(trendAnalysisVo.getRiskType()));
                condition.setBizCondition(riskTrendAnalysis);
            }
            if (!ObjectUtils.isEmpty(trendAnalysisVo.getStartTime())) {
                FilterCondition filterCondition = new FilterCondition();
                filterCondition.setFilterField(RiskManageConstant.RISK_FOUND_TIME);
                filterCondition.setOperator(RiskManageConstant.GREATER_THAN_OR_EQUAL);
                filterCondition.setFilterValue(trendAnalysisVo.getStartTime());
                filterConditions.add(filterCondition);
            }
            if (!ObjectUtils.isEmpty(trendAnalysisVo.getEndTime())) {
                FilterCondition filterCond = new FilterCondition();
                filterCond.setFilterField(RiskManageConstant.RISK_FOUND_TIME);
                filterCond.setOperator(RiskManageConstant.LESS_THAN_OR_EQUAL);
                filterCond.setFilterValue(trendAnalysisVo.getEndTime());
                filterConditions.add(filterCond);
            }
        }
        condition.setFilterConditions(filterConditions);
        //排序条件
        if (ObjectUtils.isEmpty(queryCondition.getSortCondition())) {
            SortCondition defaultSort = new SortCondition();
            defaultSort.setSortField(RiskManageConstant.RISK_FOUND_TIME);
            defaultSort.setSortRule(RiskManageConstant.DESCENDING_ORDER);
            queryCondition.setSortCondition(defaultSort);
        }
        condition.setSortCondition(queryCondition.getSortCondition());
        //构建分页条件
        condition.setPageCondition(queryCondition.getPageCondition());
        Page page = MpQueryHelper.myBatiesBuildPage(condition);
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(condition);
        //获取查询总数
        int count = riskTrendAnalysisDao.selectCount(entityWrapper);
        //获取分页查询数据
        List<RiskTrendAnalysis> pageRiskItem = riskTrendAnalysisDao.selectPage(page, entityWrapper);
        return MpQueryHelper.myBatiesBuildPageBean(page, count, pageRiskItem);
    }

    /**
     * 查询风险趋势分析图表数据
     *
     * @param trendAnalysisVo 查询条件
     * @return Result<List < RiskCurrentAnalysisDto>> 风险现状分析数据列表
     */
    @Override
    public List<RiskTrendAnalysis> getRiskTrendChartData(RiskTrendAnalysisVo trendAnalysisVo) {
        EntityWrapper<RiskTrendAnalysis> wrapper = new EntityWrapper<>();
        RiskTrendAnalysis trendAnalysis = new RiskTrendAnalysis();
        if (!ObjectUtils.isEmpty(trendAnalysisVo.getRiskType())) {
            trendAnalysis.setRiskType(RiskTypeEnum.getNameByCode(trendAnalysisVo.getRiskType()));
            wrapper.setEntity(trendAnalysis);
        }
        if (!ObjectUtils.isEmpty(trendAnalysisVo.getStartTime())) {
            wrapper.ge(RiskManageConstant.RISK_FOUND_TIME_COLUMN, trendAnalysisVo.getStartTime());
        }
        if (!ObjectUtils.isEmpty(trendAnalysisVo.getEndTime())) {
            wrapper.le(RiskManageConstant.RISK_FOUND_TIME_COLUMN, DateFormatUtils.getNextMonthTime(trendAnalysisVo.getEndTime()));
        }
        List<RiskTrendAnalysis> riskTrendAnalyses = riskTrendAnalysisDao.selectList(wrapper);
        if (!ObjectUtils.isEmpty(trendAnalysisVo.getStartTime()) && !ObjectUtils.isEmpty(trendAnalysisVo.getEndTime())) {
            riskTrendAnalyses = nullProcesser(riskTrendAnalyses, trendAnalysisVo.getStartTime(), trendAnalysisVo.getEndTime());
        }
        riskTrendAnalyses = riskTrendAnalyses.stream().sorted(Comparator.comparing(RiskTrendAnalysis::getRiskFoundTime)).collect(Collectors.toList());
        return riskTrendAnalyses;
    }

    /**
     * 查询最近一年风险趋势分析图表数据(包含预测数据)
     *
     * @return List<RiskCurrentAnalysisDto> 风险现状分析数据列表
     */
    @Override
    public List<RiskTrendAnalysis> getYearTrendChartData() {
        RiskTrendAnalysisVo riskTrendAnalysisVo = new RiskTrendAnalysisVo();
        Calendar calendar = Calendar.getInstance();
        Long currentMonthStartTime = DateFormatUtils.getThisMonthTime();
        calendar.setTimeInMillis(currentMonthStartTime);
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        riskTrendAnalysisVo.setStartTime(calendar.getTimeInMillis());
        riskTrendAnalysisVo.setEndTime(DateFormatUtils.getNextQuarterTime(currentMonthStartTime));
        return getRiskTrendChartData(riskTrendAnalysisVo);
    }

    /**
     * 将不存在统计数据的月份统计数据处理为0；
     *
     * @param riskTrendAnalyses 基础数据列表
     * @param startTime         开始处理时间
     * @param endTime           处理结束时间
     * @return 处理后的数据列表
     */
    private List<RiskTrendAnalysis> nullProcesser(List<RiskTrendAnalysis> riskTrendAnalyses, Long startTime, Long endTime) {
        Map<String, List<RiskTrendAnalysis>> riskMap = riskTrendAnalyses.stream().collect(Collectors.groupingBy(RiskTrendAnalysis::getRiskType));
        //将不存在统计数据的月份统计数据处理为0；
        List<RiskTrendAnalysis> trendAnalyses = Lists.newArrayList();
        riskMap.forEach((riskType, riskTrendAnalysisList) -> {
            List<Long> foundTimeList = riskTrendAnalysisList.stream().map(RiskTrendAnalysis::getRiskFoundTime).collect(Collectors.toList());
            trendAnalyses.addAll(riskTrendAnalysisList);
            for (Long monthTime = startTime; monthTime <= endTime; monthTime = DateFormatUtils.getNextMonthTime(monthTime)) {
                if (!foundTimeList.contains(monthTime)) {
                    RiskTrendAnalysis riskTrendAnalysis = new RiskTrendAnalysis();
                    riskTrendAnalysis.setId(NineteenUUIDUtils.uuid());
                    riskTrendAnalysis.setRiskType(riskType);
                    riskTrendAnalysis.setRiskFoundTime(monthTime);
                    riskTrendAnalysis.setAdvancedRiskNumber(0);
                    riskTrendAnalysis.setIntermediateRiskNumber(0);
                    riskTrendAnalysis.setLowRiskNumber(0);
                    trendAnalyses.add(riskTrendAnalysis);
                }
            }
        });
        return trendAnalyses;
    }

    /**
     * 查询本月风险趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险趋势分析数据列表
     */
    @Override
    public List<RiskTrendAnalysis> getRiskTrendMonthData() {
        EntityWrapper<RiskTrendAnalysis> wrapper = new EntityWrapper<>();
        RiskTrendAnalysis trendAnalysis = new RiskTrendAnalysis();
        trendAnalysis.setRiskFoundTime(DateFormatUtils.getThisMonthTime());
        wrapper.setEntity(trendAnalysis);
        wrapper.orderAsc(Arrays.asList(RiskManageConstant.RISK_TYPE_COLUMN));
        return riskTrendAnalysisDao.selectList(wrapper);
    }

    /**
     * 查询下月风险趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险趋势分析数据列表
     */
    @Override
    public List<RiskTrendAnalysis> getRiskTrendNextMonthData() {
        EntityWrapper<RiskTrendAnalysis> wrapper = new EntityWrapper<>();
        RiskTrendAnalysis trendAnalysis = new RiskTrendAnalysis();
        trendAnalysis.setRiskFoundTime(DateFormatUtils.getNextMonthTime(System.currentTimeMillis()));
        wrapper.setEntity(trendAnalysis);
        wrapper.orderAsc(Arrays.asList(RiskManageConstant.RISK_TYPE_COLUMN));
        return riskTrendAnalysisDao.selectList(wrapper);
    }

    /**
     * 查询本月风险等级趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险等级趋势分析数据列表
     */
    @Override
    public List<RiskLevelTrend> getRiskLevelTrendMonthData() {
        EntityWrapper<RiskLevelTrend> wrapper = new EntityWrapper<>();
        RiskLevelTrend trendAnalysis = new RiskLevelTrend();
        trendAnalysis.setRiskFoundTime(DateFormatUtils.getThisMonthTime());
        wrapper.setEntity(trendAnalysis);
        return riskLevelTrendDao.selectList(wrapper);
    }

    /**
     * 查询下月风险等级趋势分析数据
     *
     * @return List<RiskTrendAnalysis> 风险等级趋势分析数据列表
     */
    @Override
    public List<RiskLevelTrend> getRiskLevelTrendNextMonthData() {
        EntityWrapper<RiskLevelTrend> wrapper = new EntityWrapper<>();
        RiskLevelTrend trendAnalysis = new RiskLevelTrend();
        trendAnalysis.setRiskFoundTime(DateFormatUtils.getNextMonthTime(System.currentTimeMillis()));
        wrapper.setEntity(trendAnalysis);
        return riskLevelTrendDao.selectList(wrapper);
    }

    /**
     * 更新风险超时时间
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRiskTimeoutTime() {
        //获取所有风险项
        List<RiskItem> riskItemList = riskItemDao.selectAllRiskItem();
        for (RiskItem riskItem : riskItemList) {
            Long planResolutionTime = riskItem.getPlanResolutionTime();
            Long actualResolutionTime = riskItem.getActualResolutionTime();
            Long currentTime = System.currentTimeMillis();
            Long timeInterval = 0L;
            if (!ObjectUtils.isEmpty(planResolutionTime)) {
                if (ObjectUtils.isEmpty(actualResolutionTime) && currentTime > planResolutionTime) {
                    timeInterval = currentTime - planResolutionTime;
                } else if (!ObjectUtils.isEmpty(actualResolutionTime) && actualResolutionTime > planResolutionTime) {
                    timeInterval = actualResolutionTime - planResolutionTime;
                }
            }
            if (timeInterval != 0L) {
                riskItem.setTimeoutTime((int) (timeInterval / (60 * 60 * 24 * 1000)));
            } else {
                riskItem.setTimeoutTime(null);
            }
        }
        riskItemDao.batchUpdateTimeoutTime(riskItemList);
    }

    /**
     * 更新风险现状分析数据
     *
     * @param currentAnalysis 需修改的数据
     * @return 修改结果
     */
    @Override
    public Result modifyRiskCurrentData(RiskCurrentAnalysis currentAnalysis) {
        try {
            riskCurrentAnalysisDao.updateById(currentAnalysis);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("modify riskCurrentData error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 更新风险趋势分析数据
     *
     * @param trendAnalysis 需修改的数据
     * @return 修改结果
     */
    @Override
    public Result modifyRiskTrendData(RiskTrendAnalysis trendAnalysis) {
        try {
            riskTrendAnalysisDao.updateById(trendAnalysis);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("modify riskTrendData error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 更新风险等级趋势分析数据
     *
     * @param levelTrend 需修改的数据
     * @return 修改结果
     */
    @Override
    public Result modifyRiskLevelTrendData(RiskLevelTrend levelTrend) {
        try {
            riskLevelTrendDao.updateById(levelTrend);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("modify riskLevelTrendData error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 根据id获取现状分析数据
     *
     * @param dataId 数据id
     * @return 现状分析数据项
     */
    @Override
    public RiskCurrentAnalysis getCurrentDataById(String dataId) {
        return riskCurrentAnalysisDao.selectById(dataId);
    }

    /**
     * 根据id获取趋势分析数据
     *
     * @param dataId 数据id
     * @return 趋势分析数据项
     */
    @Override
    public RiskTrendAnalysis getTrendDataById(String dataId) {
        return riskTrendAnalysisDao.selectById(dataId);
    }

    /**
     * 根据id获取风险等级趋势分析数据
     *
     * @param dataId 数据id
     * @return 风险等级趋势分析数据项
     */
    @Override
    public RiskLevelTrend getLevelTrendDataById(String dataId) {
        return riskLevelTrendDao.selectById(dataId);
    }

    /**
     * 根据运维管理活动生成风险项
     *
     * @param activityId   运维管理活动id
     * @param riskItemName 风险项名称
     */
    @Override
    public void createRiskItemByActivityId(String activityId, String riskItemName) {
        RiskItem item = riskItemDao.selectRiskItemByActivityId(activityId);
        if (ObjectUtils.isEmpty(item)) {
            RiskItem riskItem = new RiskItem();
            riskItem.setActivityId(activityId);
            riskItem.setRiskItemName(riskItemName);
            riskItem.setIsManuallyAdded(CreatingModeEnum.MANAGE_CREATE.getCode());
            riskItem.setFoundDate(System.currentTimeMillis());
            addRiskItem(riskItem);
        } else {
            item.setRiskItemName(riskItemName);
            modifyRiskItem(item);
        }
    }

    /**
     * 根据用户id获取待处理风险项总数
     *
     * @param userId 用户id
     * @return Integer 待处理风险项总数
     */
    @Override
    public Integer queryCountByUserId(String userId) {
        return riskItemDao.selectCountByUserId(userId);
    }

    /**
     * 条件查询用户待处理的风险项列表
     *
     * @param queryCondition 查询条件
     * @return 风险项列表
     */
    @Override
    public PageBean getUserRiskByCondition(QueryCondition<RiskItem> queryCondition) {
        //构建分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        RiskItem item = queryCondition.getBizCondition();
        EntityWrapper<RiskItem> wrapper = new EntityWrapper<>();
        if (!ObjectUtils.isEmpty(item)) {
            if (!ObjectUtils.isEmpty(item.getTrackUser()) && !ObjectUtils.isEmpty(item.getCheckUser())) {
                String trackUser = item.getTrackUser();
                String checkUser = item.getCheckUser();
                item.setTrackUser(null);
                item.setCheckUser(null);
                wrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
                //  and 和 or  处理
                wrapper.andNew().eq(RiskManageConstant.TRACK_USER_COLUMN, trackUser).or().eq(RiskManageConstant.CHECK_USER_COLUMN, checkUser);
            } else {
                wrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
            }
        }
        return getRiskItemDataByCondition(page, wrapper);
    }


    /**
     * 保存风险分析报告数据
     *
     * @return 保存结果
     */
    @Override
    public Result saveAnalysisReportData() {
        Long thisMonthTime = DateFormatUtils.getThisMonthTime();
        //获取风险现状分析基础数据
        List<RiskCurrentAnalysis> riskCurrentAnalysisList = riskCurrentAnalysisDao.selectRiskCurrentAnalysisData();
        //构建风险现状分析报告数据
        List<RiskCurrentAnalysisReport> reportDataList = Lists.newArrayList();
        for (RiskCurrentAnalysis currentAnalysis : riskCurrentAnalysisList) {
            RiskCurrentAnalysisReport reportData = new RiskCurrentAnalysisReport();
            BeanUtils.copyProperties(currentAnalysis, reportData);
            reportData.setId(NineteenUUIDUtils.uuid());
            reportData.setGenerationTime(thisMonthTime);
            reportDataList.add(reportData);
        }
        //获取风险趋势分析年数据
        List<RiskTrendAnalysis> riskTrendAnalysisList = getYearTrendChartData();
        //构建风险趋势分析报告数据
        List<RiskTrendAnalysisReport> trendAnalysisReportList = Lists.newArrayList();
        for (RiskTrendAnalysis trendAnalysis : riskTrendAnalysisList) {
            RiskTrendAnalysisReport trendAnalysisReport = new RiskTrendAnalysisReport();
            BeanUtils.copyProperties(trendAnalysis, trendAnalysisReport);
            trendAnalysisReport.setId(NineteenUUIDUtils.uuid());
            trendAnalysisReport.setGenerationTime(thisMonthTime);
            trendAnalysisReportList.add(trendAnalysisReport);
        }
        //获取风险等级趋势分析数据
        List<RiskLevelTrend> levelTrendList = getRiskLevelTrendMonthData();
        levelTrendList.addAll(getRiskLevelTrendNextMonthData());
        //构建风险等级趋势分析报告数据
        List<RiskLevelTrendReport> levelTrendReportList = Lists.newArrayList();
        for (RiskLevelTrend levelTrend : levelTrendList) {
            RiskLevelTrendReport levelTrendReport = new RiskLevelTrendReport();
            BeanUtils.copyProperties(levelTrend, levelTrendReport);
            levelTrendReport.setId(NineteenUUIDUtils.uuid());
            levelTrendReport.setGenerationTime(thisMonthTime);
            levelTrendReportList.add(levelTrendReport);
        }
        //保存风险分析报告数据
        List<RiskCurrentAnalysisReport> oldCurrentData = currentAnalysisReportDao.selectDataByMonth(thisMonthTime);
        List<String> oldCurrentDataIds = oldCurrentData.stream().map(RiskCurrentAnalysisReport::getId).collect(Collectors.toList());
        List<RiskTrendAnalysisReport> oldTrendData = trendAnalysisReportDao.selectDataByMonth(thisMonthTime);
        List<String> oldTrendDataIds = oldTrendData.stream().map(RiskTrendAnalysisReport::getId).collect(Collectors.toList());
        List<RiskLevelTrendReport> oldLevelTrendData = levelTrendReportDao.selectDataByMonth(thisMonthTime);
        List<String> oldLevelTrendDataIds = oldLevelTrendData.stream().map(RiskLevelTrendReport::getId).collect(Collectors.toList());
        try {
            if (!ObjectUtils.isEmpty(oldCurrentDataIds)) {
                currentAnalysisReportDao.batchDeleteData(oldCurrentDataIds);
            }
            if (!ObjectUtils.isEmpty(reportDataList)) {
                currentAnalysisReportDao.batchInsertData(reportDataList);
            }
            if (!ObjectUtils.isEmpty(oldTrendDataIds)) {
                trendAnalysisReportDao.batchDeleteData(oldTrendDataIds);
            }
            if (!ObjectUtils.isEmpty(trendAnalysisReportList)) {
                trendAnalysisReportDao.batchInsertData(trendAnalysisReportList);
            }
            if (!ObjectUtils.isEmpty(oldLevelTrendDataIds)) {
                levelTrendReportDao.batchDeleteData(oldLevelTrendDataIds);
            }
            if (!ObjectUtils.isEmpty(levelTrendReportList)) {
                levelTrendReportDao.batchInsertData(levelTrendReportList);
            }
        } catch (Exception e) {
            log.error("save analysisReportData error", e);
            throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
        }
        paramManageService.checkPreview(ManageSourceEnum.RISK_MANAGE);
        paramManageService.updateParamManage(ManageSourceEnum.RISK_MANAGE, ParamTypeEnum.PREVIEW, System.currentTimeMillis());
        return ResultUtils.success();
    }

    /**
     * 根据月份获取现状分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 现状分析报告数据
     */
    @Override
    public List<RiskCurrentAnalysisDto> getCurrentAnalysisReportData(Long monthTime) {
        List<RiskCurrentAnalysisReport> reportData = currentAnalysisReportDao.selectDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        List<RiskCurrentAnalysis> riskCurrentAnalysisList = Lists.newArrayList();
        riskCurrentAnalysisList.addAll(reportData);
        return processCurrentData(riskCurrentAnalysisList);
    }

    /**
     * 根据月份获取趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    @Override
    public List<RiskTrendAnalysisReport> getTrendAnalysisReportData(Long monthTime) {
        List<RiskTrendAnalysisReport> riskTrendAnalyses = trendAnalysisReportDao.selectDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        riskTrendAnalyses = riskTrendAnalyses.stream().sorted(Comparator.comparing(o -> o.getRiskFoundTime() + o.getRiskType())).collect(Collectors.toList());
        return riskTrendAnalyses;
    }

    /**
     * 根据月份获取当月现状趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    @Override
    public List<RiskTrendAnalysisReport> getMonthTrendAnalysisReportData(Long monthTime) {
        Long monthStartTime = DateFormatUtils.getMonthStartForTime(monthTime);
        List<RiskTrendAnalysisReport> reportList = trendAnalysisReportDao.selectDataByMonth(monthStartTime);
        reportList = reportList.stream().sorted(Comparator.comparing(RiskTrendAnalysis::getRiskType)).
                filter(riskTrendAnalysisReport -> monthStartTime.equals(riskTrendAnalysisReport.getRiskFoundTime())).collect(Collectors.toList());
        return reportList;
    }

    /**
     * 根据月份获取下月趋势预测分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 趋势分析报告数据
     */
    @Override
    public List<RiskTrendAnalysisReport> getNextMonthTrendAnalysisReportData(Long monthTime) {
        Long monthStartTime = DateFormatUtils.getMonthStartForTime(monthTime);
        List<RiskTrendAnalysisReport> reportList = trendAnalysisReportDao.selectDataByMonth(monthStartTime);
        reportList = reportList.stream().sorted(Comparator.comparing(RiskTrendAnalysis::getRiskType)).
                filter(riskTrendAnalysisReport -> DateFormatUtils.getNextMonthTime(monthStartTime).equals(riskTrendAnalysisReport.getRiskFoundTime())).collect(Collectors.toList());
        return reportList;
    }

    /**
     * 根据月份获取等级趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 级趋势分析报告数据
     */
    @Override
    public List<RiskLevelTrendReport> getLevelTrendAnalysisReportData(Long monthTime) {
        Long monthStartTime = DateFormatUtils.getMonthStartForTime(monthTime);
        List<RiskLevelTrendReport> reportList = levelTrendReportDao.selectDataByMonth(monthStartTime);
        reportList = reportList.stream().filter(riskLevelTrendReport -> monthStartTime.equals(riskLevelTrendReport.getRiskFoundTime())).collect(Collectors.toList());
        return reportList;
    }

    /**
     * 根据月份获取下月等级趋势分析报告数据
     *
     * @param monthTime 指定月份开始时间
     * @return 级趋势分析报告数据
     */
    @Override
    public List<RiskLevelTrendReport> getNextMonthLevelTrendReportData(Long monthTime) {
        Long monthStartTime = DateFormatUtils.getMonthStartForTime(monthTime);
        List<RiskLevelTrendReport> reportList = levelTrendReportDao.selectDataByMonth(monthStartTime);
        reportList = reportList.stream().filter(riskLevelTrendReport -> DateFormatUtils.getNextMonthTime(monthStartTime).equals(riskLevelTrendReport.getRiskFoundTime())).collect(Collectors.toList());
        return reportList;
    }

    /**
     * 获取分页条件查询数据
     *
     * @param page    分页条件
     * @param wrapper 查询条件
     * @return 分页数据
     */
    private PageBean getRiskItemDataByCondition(Page page, EntityWrapper<RiskItem> wrapper) {
        //获取查询总数
        int count = riskItemDao.selectCount(wrapper);
        //获取分页查询数据
        List<RiskItem> pageRiskItem = riskItemDao.selectPage(page, wrapper);
        List<UserInfo> allUser = userInfoDao.getUserInfoList();
        List<RiskItemDto> riskItemDtos = Lists.newArrayList();
        pageRiskItem.forEach(riskItem -> {
            RiskItemDto riskItemDto = new RiskItemDto();
            allUser.forEach(userInfo -> {
                BeanUtils.copyProperties(riskItem, riskItemDto);
                if (!ObjectUtils.isEmpty(riskItem.getTrackUser()) && riskItem.getTrackUser().equals(userInfo.getUserId())) {
                    riskItemDto.setTrackUserName(userInfo.getUserRealName());
                }
                if (!ObjectUtils.isEmpty(riskItem.getCheckUser()) && riskItem.getCheckUser().equals(userInfo.getUserId())) {
                    riskItemDto.setCheckUserName(userInfo.getUserRealName());
                }
            });
            riskItemDtos.add(riskItemDto);
        });
        return MpQueryHelper.myBatiesBuildPageBean(page, count, riskItemDtos);
    }

    /**
     * 更新风险等级趋势分析数据
     */
    private void updateRiskLevelTrendData() {
        //获取风险趋势筛选统计结果
        List<RiskTrendAnalysis> riskTrendAnalyses = riskTrendAnalysisDao.selectRiskTrendAnalysisData();
        Map<Long, List<RiskTrendAnalysis>> baseMap = riskTrendAnalyses.stream().collect(Collectors.groupingBy(RiskTrendAnalysis::getRiskFoundTime));
        List<RiskLevelTrend> riskLevelTrends = Lists.newArrayList();
        baseMap.forEach((k, riskTrendAnalysisList) -> {
            Integer highCount = riskTrendAnalysisList.stream().mapToInt(RiskTrendAnalysis::getAdvancedRiskNumber).sum();
            Integer middleCount = riskTrendAnalysisList.stream().mapToInt(RiskTrendAnalysis::getIntermediateRiskNumber).sum();
            Integer lowCount = riskTrendAnalysisList.stream().mapToInt(RiskTrendAnalysis::getLowRiskNumber).sum();
            if (ObjectUtils.isEmpty(highCount)) {
                highCount = 0;
            }
            if (ObjectUtils.isEmpty(middleCount)) {
                middleCount = 0;
            }
            if (ObjectUtils.isEmpty(lowCount)) {
                lowCount = 0;
            }
            RiskLevelTrend highLevelTrend = new RiskLevelTrend();
            highLevelTrend.setId(NineteenUUIDUtils.uuid());
            highLevelTrend.setRiskFoundTime(k);
            highLevelTrend.setRiskCount(highCount);
            highLevelTrend.setRiskLevel(RiskLevelEnum.HIGH.getLevelName());
            RiskLevelTrend middleLevelTrend = new RiskLevelTrend();
            middleLevelTrend.setId(NineteenUUIDUtils.uuid());
            middleLevelTrend.setRiskFoundTime(k);
            middleLevelTrend.setRiskCount(middleCount);
            middleLevelTrend.setRiskLevel(RiskLevelEnum.MIDDLE.getLevelName());
            RiskLevelTrend lowLevelTrend = new RiskLevelTrend();
            lowLevelTrend.setId(NineteenUUIDUtils.uuid());
            lowLevelTrend.setRiskFoundTime(k);
            lowLevelTrend.setRiskCount(lowCount);
            lowLevelTrend.setRiskLevel(RiskLevelEnum.LOW.getLevelName());
            riskLevelTrends.add(highLevelTrend);
            riskLevelTrends.add(middleLevelTrend);
            riskLevelTrends.add(lowLevelTrend);
        });
        for (RiskLevelTrend riskLevelTrend : riskLevelTrends) {
            for (RiskLevelTrend levelTrend : riskLevelTrends) {
                if (levelTrend.getRiskLevel().equals(riskLevelTrend.getRiskLevel())) {
                    if (riskLevelTrend.getRiskFoundTime().equals(DateFormatUtils.getNextMonthTime(levelTrend.getRiskFoundTime()))) {
                        Integer addNumber = riskLevelTrend.getRiskCount() - levelTrend.getRiskCount();
                        riskLevelTrend.setAddNumber(addNumber);
                        if (levelTrend.getRiskCount() != 0) {
                            riskLevelTrend.setRingGrowth(addNumber / ((levelTrend.getRiskCount() * 1.0)));
                        }
                    }
                }
            }
        }
        saveRiskLevelTrendData(riskLevelTrends);
    }

    /**
     * 保存风险等级趋势分析基本数据
     *
     * @param baseData 风险等级趋势分析基本数据
     */
    private void saveRiskLevelTrendData(List<RiskLevelTrend> baseData) {
        if (!ObjectUtils.isEmpty(baseData.toArray())) {
            List<RiskLevelTrend> oldLevelTrendList = riskLevelTrendDao.selectLevelTrendData();
            List<RiskLevelTrend> addList = Lists.newArrayList();
            List<RiskLevelTrend> updateList = Lists.newArrayList();
            List<String> deleteIdList = Lists.newArrayList();
            Map<String, RiskLevelTrend> oldLevelMap = Maps.newHashMap();
            Map<String, RiskLevelTrend> newLevelMap = Maps.newHashMap();
            for (RiskLevelTrend levelTrend : oldLevelTrendList) {
                String key = levelTrend.getRiskLevel() + RiskManageConstant.LINK_SYMBOL + levelTrend.getRiskFoundTime();
                oldLevelMap.put(key, levelTrend);
            }
            for (RiskLevelTrend newLevelTrend : baseData) {
                String key = newLevelTrend.getRiskLevel() + RiskManageConstant.LINK_SYMBOL + newLevelTrend.getRiskFoundTime();
                newLevelMap.put(key, newLevelTrend);
            }
            newLevelMap.forEach((s, newRiskLevelTrend) -> {
                if (!oldLevelMap.keySet().contains(s)) {
                    addList.add(newRiskLevelTrend);
                } else {
                    newRiskLevelTrend.setId(newLevelMap.get(s).getId());
                    updateList.add(newRiskLevelTrend);
                }
            });
            oldLevelMap.forEach((k, oldRiskLevelTrend) -> {
                if (!newLevelMap.keySet().contains(k)) {
                    deleteIdList.add(oldRiskLevelTrend.getId());
                }
            });
            try {
                if (!ObjectUtils.isEmpty(addList)) {
                    riskLevelTrendDao.batchInsertLevelTrendData(addList);
                }
                if (!ObjectUtils.isEmpty(updateList)) {
                    riskLevelTrendDao.batchUpdateLevelTrendData(updateList);
                }
                if (!ObjectUtils.isEmpty(deleteIdList)) {
                    riskLevelTrendDao.batchDeleteLevelTrendData(deleteIdList);
                }
            } catch (Exception e) {
                log.error("save riskLevelTrendData error", e);
                throw new BizException(RiskManageResultCode.DATABASE_OPERATION_FAIL,RiskManageResultMsg.DATABASE_OPERATION_FAIL);
            }
        }
    }

    /**
     * 处理风险现状分析数据
     *
     * @param currentAnalysisList 风险现状分析数据列表
     * @return 处理后的数据列表
     */
    private List<RiskCurrentAnalysisDto> processCurrentData(List<RiskCurrentAnalysis> currentAnalysisList) {
        //获取风险等级分类
        currentAnalysisList = currentAnalysisList.stream().filter(currentAnalysis -> !ObjectUtils.isEmpty(currentAnalysis.getRiskLevel())).collect(Collectors.toList());
        List<Integer> riskLevelList = currentAnalysisList.stream().map(RiskCurrentAnalysis::getRiskLevel).sorted().collect(Collectors.toList());
        riskLevelList = riskLevelList.stream().distinct().collect(Collectors.toList());
        //构建风险现状分析结果
        List<RiskCurrentAnalysisDto> riskCurrentAnalysisDtoList = Lists.newArrayList();
        for (Integer riskLevel : riskLevelList) {
            RiskCurrentAnalysisDto riskCurrentAnalysisDto = new RiskCurrentAnalysisDto();
            //设置风险等级
            riskCurrentAnalysisDto.setRiskLevel(RiskLevelEnum.getNameByCode(riskLevel));
            //筛选各风险等级现状分析数据
            List<RiskCurrentAnalysis> riskCurrentAnalyses = currentAnalysisList.stream()
                    .filter(riskCurrentAnalysis -> riskCurrentAnalysis.getRiskLevel().equals(riskLevel))
                    .collect(Collectors.toList());
            riskCurrentAnalyses = riskCurrentAnalyses.stream().sorted(Comparator.comparing(RiskCurrentAnalysis::getTimeoutTime).reversed()).collect(Collectors.toList());
            riskCurrentAnalysisDto.setRiskCurrentAnalysisList(riskCurrentAnalyses);
            riskCurrentAnalysisDtoList.add(riskCurrentAnalysisDto);
        }
        return riskCurrentAnalysisDtoList;
    }

}
