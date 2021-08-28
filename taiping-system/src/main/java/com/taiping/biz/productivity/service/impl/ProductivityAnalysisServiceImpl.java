package com.taiping.biz.productivity.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.productivity.dao.*;
import com.taiping.biz.productivity.service.ProductivityAnalysisService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.DateConstant;
import com.taiping.constant.productivity.ProductivityConstant;
import com.taiping.constant.productivity.ProductivityResultCode;
import com.taiping.constant.productivity.ProductivityResultMsg;
import com.taiping.entity.*;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.productivity.*;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.enums.productivity.ManageTypeEnum;
import com.taiping.enums.productivity.ProductivityParamEnum;
import com.taiping.enums.productivity.ScheduleTypeEnum;
import com.taiping.enums.productivity.WorkloadTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.read.productivity.ChangeExcelImportRead;
import com.taiping.read.productivity.InspectionTaskExcelImportRead;
import com.taiping.read.productivity.ScheduleExcelImportRead;
import com.taiping.utils.*;
import com.taiping.utils.common.PercentageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 生产力分析服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Slf4j
@Service
public class ProductivityAnalysisServiceImpl implements ProductivityAnalysisService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private InspectionTaskDao inspectionTaskDao;

    @Autowired
    private ChangePlanDao changePlanDao;

    @Autowired
    private ChangeDao changeDao;

    @Autowired
    private ChangePeopleDao changePeopleDao;

    @Autowired
    private PerformanceDao performanceDao;

    @Autowired
    private DailyWorkloadDao dailyWorkloadDao;

    @Autowired
    private WorkloadStatisticsDao workloadStatisticsDao;

    @Autowired
    private MonthWorkloadDao monthWorkloadDao;

    @Autowired
    private ScheduleExcelImportRead scheduleExcelImportRead;

    @Autowired
    private InspectionTaskExcelImportRead inspectionTaskExcelImportRead;

    @Autowired
    private ChangeExcelImportRead changeExcelImportRead;

    @Autowired
    private ParamManageService paramManageService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ManageActivityService manageService;
    /**
     * 一天毫秒值
     */
    private static final int DAY_MILLISECOND = 86400000;
    /**
     * 标准工作量
     */
    private static final int STANDARD_WORKLOAD = 20;
    /**
     * 0.0
     */
    private static final double ZERO = 0.0;

    /**
     * 导入常白班排班和倒班排班Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    public Result importSchedule(MultipartFile file) {
        //解析Excel表格
        List<ScheduleDto> scheduleDtoList = (List)getExcelReadBeanForSame(file, scheduleExcelImportRead);
        String type;
        String name;
        if (ScheduleTypeEnum.ONE_DAY.getType().equals(scheduleDtoList.get(0).getScheduleType())) {
            type = ScheduleTypeEnum.ONE_DAY.getType();
            name = ScheduleTypeEnum.ONE_DAY.getScheduleName();
        } else {
            type = null;
            name = ScheduleTypeEnum.DAY.getScheduleName();
        }
        //查询日期
        List<Long> dateList = scheduleDao.queryScheduleForTime(scheduleDtoList, type);
        if (dateList.size() > 0) {
            String scheduleDate = DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_FIVE, dateList.get(0));
            String msg = ProductivityResultMsg.SCHEDULE_IMPORT.replace(ProductivityConstant.NAME_REPLACE, name);
            msg = msg.replace(ProductivityConstant.NUM_REPLACE, scheduleDate);
            return ResultUtils.warn(ProductivityResultCode.SCHEDULE_IMPORT, msg);
        }
        List<Schedule> scheduleList = new ArrayList<>();
        for (ScheduleDto scheduleDto : scheduleDtoList) {
            scheduleList.addAll(scheduleDto.getScheduleList());
        }
        scheduleDao.addScheduleBatch(scheduleList);
        return ResultUtils.success();
    }

    /**
     * 导入巡检工单数据Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    public Result importInspectionTask(MultipartFile file) {
        List<InspectionTask> inspectionTasks = (List)getExcelReadBeanForSame(file, inspectionTaskExcelImportRead);
        inspectionTaskDao.addInspectionTaskBatch(inspectionTasks);
        return ResultUtils.success();
    }

    /**
     * 导入变更单数据Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importChangeOrder(MultipartFile file) {
        List<ChangeRead> changeReadList = (List)getExcelReadBeanForSame(file, changeExcelImportRead);
        List<ChangePeople> changePeopleList = new ArrayList<>();
        for (ChangeRead changeRead : changeReadList) {
            changePeopleList.addAll(changeRead.getChangePeopleList());
        }
        changeDao.insertChangeBatch(changeReadList);
        changePeopleDao.insertChangePeopleBatch(changePeopleList);
        return ResultUtils.success();
    }

    /**
     * 查询列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectScheduleList(QueryCondition<Schedule> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "scheduleDate", "desc");
        //查询
        List<Schedule> scheduleList = scheduleDao.selectScheduleList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = scheduleDao.selectScheduleListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, scheduleList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectInspectionTaskList(QueryCondition<InspectionTask> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "createTime", "desc");
        //查询
        List<InspectionTask> inspectionTasks = inspectionTaskDao.selectInspectionTaskList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = inspectionTaskDao.selectInspectionTaskListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, inspectionTasks);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询变更单列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectChangeList(QueryCondition<Change> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "startDate", "desc");
        //查询
        List<Change> changeList = changeDao.selectChangeList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = changeDao.selectChangeListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, changeList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询执行人
     *
     * @return Result
     */
    @Override
    public Result selectExecutorList() {
        List<String> executorList = inspectionTaskDao.selectExecutorList();
        return ResultUtils.success(executorList);
    }

    /**
     * 新增未来变更计划
     *
     * @param changePlan 未来变更计划
     * @return Result
     */
    @Override
    public Result insertChangePlan(ChangePlan changePlan) {
        List<String> list = changePlanDao.selectChangePlanForName(changePlan);
        if (!CollectionUtils.isEmpty(list)) {
            //名称重复
            return ResultUtils.warn(ProductivityResultCode.CHANGE_PLAN_NAME,
                    ProductivityResultMsg.CHANGE_PLAN_NAME);
        }
        changePlan.setChangeId(NineteenUUIDUtils.uuid());
        changePlan.setCreateTime(System.currentTimeMillis());
        changePlanDao.insertChangePlan(changePlan);
        return ResultUtils.success();
    }

    /**
     * 修改未来变更计划
     *
     * @param changePlan 未来变更计划
     * @return Result
     */
    @Override
    public Result updateChangePlan(ChangePlan changePlan) {
        ChangePlan changePlanDb = changePlanDao.selectChangePlanById(changePlan.getChangeId());
        if (changePlanDb == null) {
            //不存在该未来变更计划
            return ResultUtils.warn(ProductivityResultCode.CHANGE_PLAN_DELETED,
                    ProductivityResultMsg.CHANGE_PLAN_DELETED);
        }
        List<String> list = changePlanDao.selectChangePlanForName(changePlan);
        if (!CollectionUtils.isEmpty(list)) {
            //名称重复
            return ResultUtils.warn(ProductivityResultCode.CHANGE_PLAN_NAME,
                    ProductivityResultMsg.CHANGE_PLAN_NAME);
        }
        changePlanDao.updateChangePlan(changePlan);
        return ResultUtils.success();
    }

    /**
     * 删除未来变更计划
     *
     * @param changeIdList 未来变更计划ID
     * @return Result
     */
    @Override
    public Result deleteChangePlan(List<String> changeIdList) {
        changePlanDao.deleteChangePlan(changeIdList);
        return ResultUtils.success();
    }

    /**
     * 根据名称查询未来变更计划
     *
     * @param changePlan 未来变更计划
     * @return List<String> 未来变更计划ID
     */
    @Override
    public Result selectChangePlanForName(ChangePlan changePlan) {
        List<String> list = changePlanDao.selectChangePlanForName(changePlan);
        boolean add = false;
        if (CollectionUtils.isEmpty(list)) {
            add = true;
        }
        return ResultUtils.success(add);
    }

    /**
     * 根据ID查询未来变更计划
     *
     * @param changeId ID
     * @return Result
     */
    @Override
    public Result selectChangePlanById(String changeId) {
        ChangePlan changePlan = changePlanDao.selectChangePlanById(changeId);
        if (changePlan == null) {
            //不存在该未来变更计划
            return ResultUtils.warn(ProductivityResultCode.CHANGE_PLAN_EXIST,
                    ProductivityResultMsg.CHANGE_PLAN_EXIST);
        }
        return ResultUtils.success(changePlan);
    }

    /**
     * 查询列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectChangePlanList(QueryCondition<ChangePlan> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "createTime", "desc");
        //查询
        List<ChangePlan> changePlans = changePlanDao.selectChangePlanList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = changePlanDao.selectChangePlanListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, changePlans);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询个人KPI列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectPerformanceList(QueryCondition<Performance> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "workDate", "desc");
        //添加月份筛选条件
        Performance bizCondition = queryCondition.getBizCondition();
        List<FilterCondition> filterConditions = queryCondition.getFilterConditions();
        if (!(bizCondition == null || bizCondition.getWorkDate() == null)) {
            Long startTime = DateFormatUtils.getMonthStartForTime(bizCondition.getWorkDate());
            Long endTime = DateFormatUtils.getMonthEndForTime(bizCondition.getWorkDate());
            FilterCondition start = new FilterCondition();
            start.setFilterField("workDate");
            start.setFilterValue(startTime);
            start.setOperator("gte");
            FilterCondition end = new FilterCondition();
            end.setFilterField("workDate");
            end.setFilterValue(endTime);
            end.setOperator("lte");
            filterConditions.add(start);
            filterConditions.add(end);
        }
        //查询
        List<Performance> performanceList = performanceDao.selectPerformanceList(queryCondition.getPageCondition(),
                filterConditions, queryCondition.getSortCondition());
        //查询总条数
        Integer count = performanceDao.selectPerformanceListCount(filterConditions);
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, performanceList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询人姓名
     *
     * @return Result
     */
    @Override
    public Result selectPersonList() {
        List<String> list = performanceDao.selectPersonList();
        return ResultUtils.success(list);
    }

    /**
     * 查询团队负荷列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectDailyWorkloadList(QueryCondition<DailyWorkload> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "changeDate", "desc");
        List<FilterCondition> filterConditions = queryCondition.getFilterConditions();
        //查询
        List<DailyWorkload> dailyWorkloadList = dailyWorkloadDao.selectDailyWorkloadList(
                queryCondition.getPageCondition(), filterConditions, queryCondition.getSortCondition());
        //查询总条数
        Integer count = dailyWorkloadDao.selectDailyWorkloadListCount(filterConditions);
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, dailyWorkloadList);
        return ResultUtils.pageSuccess(pageBean);
    }

    @Override
    public void test() {
        //获取系统参数设置
        List<SystemSetting> systemSettingList = systemService.querySystemValueByCode(
                ProductivityParamEnum.PRODUCTIVITY_PARAM.getCode());
        for (SystemSetting setting : systemSettingList) {
            System.out.println((double) setting.getValue() > 1);
        }
    }

    /**
     * 分析数据
     *
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result analysisData() {
        Long latestScheduleDate = scheduleDao.queryLatestScheduleDate();
        paramManageService.checkAnalyze(ManageSourceEnum.PRODUCTIVITY, latestScheduleDate);
        Long startTime = DateFormatUtils.getMonthStartForTime(latestScheduleDate);
        Long endTime = DateFormatUtils.getMonthEndForTime(latestScheduleDate);
        Map<String, Performance> performanceMap = new HashMap<>(64);
        //查询条件
        ProStatistics proStatistics = new ProStatistics();
        proStatistics.setStartTime(startTime);
        proStatistics.setEndTime(endTime);
        //统计排班表上班天数
        queryScheduleStatistics(proStatistics, performanceMap);
        //查询巡检次数 上架数量
        queryInspectionAndChangeStatistics(performanceMap, proStatistics);
        //计算KPI
        List<Performance> performanceList = new ArrayList<>(performanceMap.values());
        producePerformance(performanceList);
        //存储KPI列表
        performanceDao.insertPerformanceBatch(performanceList);
        //计算生成团队负荷
        List<DailyWorkload> dailyWorkloadList = new ArrayList<>();
        WorkloadParam workloadParam = produceDailyWorkload(proStatistics, dailyWorkloadList);
        //存储团队负荷
        dailyWorkloadDao.insertDailyWorkloadBatch(dailyWorkloadList);
        //查询当月每天团队负荷最大状态
        List<DailyWorkload> workloadList = dailyWorkloadDao.queryWorkloadForTime(proStatistics);
        //统计每种负荷状态天数，计算比率
        WorkloadStatistics statistics = getWorkloadStatistics(startTime, workloadList);
        statistics.setTotalWorkload(proStatistics.getTotalWorkload());
        //存储统计数据
        workloadStatisticsDao.insertWorkloadStatistics(statistics);
        //团队负荷月份统计预测数据
        List<MonthWorkload> monthWorkloadList = getMonthWorkloads(startTime, workloadParam, statistics);
        //存储团队负荷月份统计预测数据
        monthWorkloadDao.insertMonthWorkloadBatch(monthWorkloadList);
        List<ManageActivity> manageActivityList = createManageActivity(monthWorkloadList);
        //生成运维管理活动
        manageService.insertManageActivityNoEquals(ManageSourceEnum.PRODUCTIVITY, manageActivityList);
        //修改模块参数类型值已分析
        paramManageService.updateParamManage(ManageSourceEnum.PRODUCTIVITY, ParamTypeEnum.ANALYZE, startTime);
        return ResultUtils.success();
    }
    /**
     * 生成分析报告
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createReport() {
        //校验预览
        paramManageService.checkPreview(ManageSourceEnum.PRODUCTIVITY);
        //查询运维管理活动
        List<ManageActivity> manageActivityList =
                manageService.queryManageActivityForMode(ManageSourceEnum.PRODUCTIVITY);
        //更改当前月分析报告类型
        monthWorkloadDao.cancelReportCurrent();
        //生成分析报告
        monthWorkloadDao.updateBatchMonthWorkload(manageActivityList);
        //修改模块参数类型值已分析
        paramManageService.updateParamManage(ManageSourceEnum.PRODUCTIVITY, ParamTypeEnum.PREVIEW, null);
        return ResultUtils.success();
    }
    /**
     * 查询分析报告
     * @param monthWorkload 查询时间
     * @return Result
     */
    @Override
    public Result queryReport(MonthWorkload monthWorkload) {
        //分析报告
        ProReport proReport = new ProReport();
        //空闲预测
        WorkloadTrendValue freeTemp = new WorkloadTrendValue();
        //正常预测
        WorkloadTrendValue normalTemp = new WorkloadTrendValue();
        //满负荷预测
        WorkloadTrendValue fullTemp = new WorkloadTrendValue();
        //查询负荷分析报告
        boolean workload = findWorkload(monthWorkload, proReport, freeTemp, normalTemp, fullTemp);
        if (workload) {
            return ResultUtils.success(proReport);
        }
        //分析月份
        Long reportDate = proReport.getReportDate();
        //趋势曲线x轴数据
        List<String> xData = new ArrayList<>();
        //趋势曲线y轴数据
        List<List<WorkloadTrendValue>> yData = new ArrayList<>();
        //查询往年统计数据
        List<Long> monthOfYear = DateFormatUtils.getMonthOfYear(reportDate);
        Long nextTime = DateFormatUtils.getNextMonthTime(reportDate);
        Long startTime = DateFormatUtils.getTimeLastYear(nextTime);
        ProStatistics yearStatistics = new ProStatistics();
        yearStatistics.setStartTime(startTime);
        yearStatistics.setEndTime(reportDate);
        Map<Long, WorkloadStatistics> statisticsMap =
                workloadStatisticsDao.queryWorkloadStatisticsForTime(yearStatistics);
        List<WorkloadTrendValue> free = new ArrayList<>();
        List<WorkloadTrendValue> normal = new ArrayList<>();
        List<WorkloadTrendValue> full = new ArrayList<>();
        for (int i = 0; i < monthOfYear.size() - 1; i++) {
            Long month = monthOfYear.get(i);
            String monthStr = DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_FOUR, month);
            xData.add(monthStr);
            WorkloadTrendValue freeTrend = new WorkloadTrendValue();
            WorkloadTrendValue normalTrend = new WorkloadTrendValue();
            WorkloadTrendValue fullTrend = new WorkloadTrendValue();
            freeTrend.setName(WorkloadTypeEnum.FREE.getName());
            normalTrend.setName(WorkloadTypeEnum.NORMAL.getName());
            fullTrend.setName(WorkloadTypeEnum.FULL_LOAD.getName());
            if (statisticsMap.containsKey(month)) {
                WorkloadStatistics statistics = statisticsMap.get(month);
                String freeString = PercentageUtil.changeDoubleToString(statistics.getFreePercentage());
                freeTrend.setValue(percentageToDouble(freeString));
                String normalString = PercentageUtil.changeDoubleToString(statistics.getNormalPercentage());
                normalTrend.setValue(percentageToDouble(normalString));
                String fullString = PercentageUtil.changeDoubleToString(statistics.getFullPercentage());
                fullTrend.setValue(percentageToDouble(fullString));
            } else {
                double zeroPercent = percentageToDouble(PercentageUtil.ZERO_PERCENT);
                freeTrend.setValue(zeroPercent);
                normalTrend.setValue(zeroPercent);
                fullTrend.setValue(zeroPercent);
            }
            free.add(freeTrend);
            normal.add(normalTrend);
            full.add(fullTrend);
        }
        free.add(freeTemp);
        normal.add(normalTemp);
        full.add(fullTemp);
        yData.add(free);
        yData.add(normal);
        yData.add(full);
        xData.add(DateFormatUtils.dateLongToString(DateConstant.FORMAT_STRING_FOUR, nextTime));
        WorkloadTrend workloadTrend = new WorkloadTrend();
        workloadTrend.setXData(xData);
        workloadTrend.setYData(yData);
        proReport.setTrend(workloadTrend);
        return ResultUtils.success(proReport);
    }

    /**
     * 查询负荷分析报告
     * @param monthWorkload 查询条件
     * @param proReport 分析报告
     * @param freeTemp 空闲预测
     * @param normalTemp 正常预测
     * @param fullTemp 满负荷预测
     */
    private boolean findWorkload(MonthWorkload monthWorkload, ProReport proReport, WorkloadTrendValue freeTemp,
                              WorkloadTrendValue normalTemp, WorkloadTrendValue fullTemp) {
        Long date = System.currentTimeMillis();
        List<MonthWorkload> monthWorkloadList;
        if (monthWorkload.getWorkloadDate() == null) {
           monthWorkloadList = monthWorkloadDao.queryReportForCurrent();
        } else {
            Long startTime = DateFormatUtils.getMonthStartForTime(monthWorkload.getWorkloadDate());
            monthWorkloadList = monthWorkloadDao.queryReportForTime(startTime);
        }
        proReport.setWorkload(monthWorkloadList);
        if (CollectionUtils.isEmpty(monthWorkloadList)) {
            return true;
        }
        for (MonthWorkload workload : monthWorkloadList) {
            String valueType = workload.getValueType();
            String workloadType = workload.getWorkloadType();
            if (WorkloadTypeEnum.CURRENT.getType().equals(valueType)) {
                continue;
            }
            String sourceName = workload.getSourceName();
            double workloadPercentage = percentageToDouble(workload.getWorkloadPercentage());
            if (WorkloadTypeEnum.FREE.getType().equals(workloadType)) {
                date = workload.getWorkloadDate();
                freeTemp.setName(sourceName);
                freeTemp.setValue(workloadPercentage);
            } else if (WorkloadTypeEnum.NORMAL.getType().equals(workloadType)) {
                normalTemp.setName(sourceName);
                normalTemp.setValue(workloadPercentage);
            } else if (WorkloadTypeEnum.FULL_LOAD.getType().equals(workloadType)) {
                fullTemp.setName(sourceName);
                fullTemp.setValue(workloadPercentage);
            }
        }
        proReport.setReportDate(date);
        return false;
    }

    /**
     * 百分比转数值
     * @param percentage 百分比
     * @return 百分比数值
     */
    private double percentageToDouble(String percentage) {
        percentage = percentage.replace("%", "");
        return Double.parseDouble(percentage);
    }

    /**
     * 团队负荷月份统计预测数据
     * @param startTime Long
     * @param workloadParam WorkloadParam
     * @param statistics 团队负荷统计数据
     * @return List<MonthWorkload>
     */
    private List<MonthWorkload> getMonthWorkloads(Long startTime, WorkloadParam workloadParam,
                                                  WorkloadStatistics statistics) {
        //查询条件
        Long lastTime = DateFormatUtils.getTimeLastYear(startTime);
        Long lastMonthTime = DateFormatUtils.getTimeLastMonth(startTime);
        ProStatistics yearStatistics = new ProStatistics();
        yearStatistics.setStartTime(lastTime);
        yearStatistics.setEndTime(startTime);
        //查询往年统计数据
        Map<Long, WorkloadStatistics> statisticsMap =
                workloadStatisticsDao.queryWorkloadStatisticsForTime(yearStatistics);
        //团队负荷月份统计
        statisticsMap.put(statistics.getWorkloadDate(), statistics);
        MonthWorkloadDto monthDto = getMonthWorkloadDto(statisticsMap, lastMonthTime, statistics);
        MonthWorkloadDto yearDto = getMonthWorkloadDto(statisticsMap, lastTime, statistics);
        MonthWorkload freeWorkload = getMonthWorkload(startTime, ManageTypeEnum.FREE, WorkloadTypeEnum.FREE, WorkloadTypeEnum.CURRENT,
                statistics.getFreePercentage(), monthDto.getFreePercentage(), yearDto.getFreePercentage());
        MonthWorkload normalWorkload = getMonthWorkload(startTime, ManageTypeEnum.NORMAL, WorkloadTypeEnum.NORMAL, WorkloadTypeEnum.CURRENT,
                statistics.getNormalPercentage(), monthDto.getNormalPercentage(), yearDto.getNormalPercentage());
        MonthWorkload fullWorkload = getMonthWorkload(startTime, ManageTypeEnum.FULL_LOAD, WorkloadTypeEnum.FULL_LOAD, WorkloadTypeEnum.CURRENT,
                statistics.getFullPercentage(), monthDto.getFullPercentage(), yearDto.getFullPercentage());
        List<MonthWorkload> monthWorkloadList = new ArrayList<>();
        monthWorkloadList.add(freeWorkload);
        monthWorkloadList.add(normalWorkload);
        monthWorkloadList.add(fullWorkload);
        //查询条件
        Long nextStartTime = DateFormatUtils.getNextMonthStartForTime(startTime);
        Long nextEndTime = DateFormatUtils.getNextMonthEndForTime(startTime);
        ProStatistics nextStatistics = new ProStatistics();
        nextStatistics.setStartTime(nextStartTime);
        nextStatistics.setEndTime(nextEndTime);
        //计算预测数据
        WorkloadStatistics nextWorkloadStatistics = getWorkloadStatistics(statisticsMap, workloadParam, nextStatistics);
        //团队负荷月份预测统计
        Long nextLastTime = DateFormatUtils.getTimeLastYear(nextStartTime);
        MonthWorkloadDto nextMonthDto = getMonthWorkloadDto(statisticsMap, startTime, nextWorkloadStatistics);
        MonthWorkloadDto nextYearDto = getMonthWorkloadDto(statisticsMap, nextLastTime, nextWorkloadStatistics);
        MonthWorkload nextFreeWorkload = getMonthWorkload(startTime, ManageTypeEnum.FREE_NEXT, WorkloadTypeEnum.FREE, WorkloadTypeEnum.FORECAST,
                nextWorkloadStatistics.getFreePercentage(), nextMonthDto.getFreePercentage(), nextYearDto.getFreePercentage());
        MonthWorkload nextNormalWorkload = getMonthWorkload(startTime, ManageTypeEnum.NORMAL_NEXT, WorkloadTypeEnum.NORMAL, WorkloadTypeEnum.FORECAST,
                nextWorkloadStatistics.getNormalPercentage(), nextMonthDto.getNormalPercentage(), nextYearDto.getNormalPercentage());
        MonthWorkload nextFullWorkload = getMonthWorkload(startTime, ManageTypeEnum.FULL_LOAD_NEXT, WorkloadTypeEnum.FULL_LOAD, WorkloadTypeEnum.FORECAST,
                nextWorkloadStatistics.getFullPercentage(), nextMonthDto.getFullPercentage(), nextYearDto.getFullPercentage());
        monthWorkloadList.add(nextFreeWorkload);
        monthWorkloadList.add(nextNormalWorkload);
        monthWorkloadList.add(nextFullWorkload);
        return monthWorkloadList;
    }

    /**
     * 计算预测数据
     * @param statisticsMap 往年统计数据
     * @param workloadParam 衡量
     * @param nextStatistics 查询条件
     * @return 预测数据 WorkloadStatistics
     */
    private WorkloadStatistics getWorkloadStatistics(Map<Long, WorkloadStatistics> statisticsMap,
                                                     WorkloadParam workloadParam, ProStatistics nextStatistics) {
        Long nextStartTime = nextStatistics.getStartTime();
        Long nextEndTime = nextStatistics.getEndTime();
        List<ChangePlan> changePlans = changePlanDao.selectChangePlanForTime(nextStatistics);
        Map<Long, Double> dailyWorkloadMap = new HashMap<>(64);
        for (ChangePlan changePlan : changePlans) {
            //第一天
            Long startDate = DateFormatUtils.getDayStartForTime(changePlan.getExpectStartTime());
            //最后一天
            Long endDate = DateFormatUtils.getDayStartForTime(changePlan.getExpectEndTime());
            //相隔天数
            double dayNumTotal = (endDate - startDate) / DAY_MILLISECOND + 1;
            double dailyWorkload =  changePlan.getDeviceNumber().doubleValue() / dayNumTotal;
            if (nextStartTime >= startDate) {
                startDate = nextStartTime;
            }
            if (endDate >= nextEndTime) {
                startDate = nextEndTime;
            }
            //相隔天数
            double dayNum = (endDate - startDate) / DAY_MILLISECOND + 1;
            int totalWorkload = (int)Math.round(dailyWorkload * dayNum);
            nextStatistics.setTotalWorkload(nextStatistics.getTotalWorkload() + totalWorkload);
            while (startDate <= endDate) {
                if (dailyWorkloadMap.containsKey(startDate)) {
                    if (dailyWorkloadMap.get(startDate) < dailyWorkload) {
                        dailyWorkloadMap.put(startDate, dailyWorkload);
                    }
                } else {
                    dailyWorkloadMap.put(startDate, dailyWorkload);
                }
                startDate = DateFormatUtils.getTimeLongNextDay(startDate);
            }
        }
        List<Integer> monthWorkload = new ArrayList<>();
        List<WorkloadStatistics> workloadStatisticsList = new ArrayList<>(statisticsMap.values());
        workloadStatisticsList.sort(Comparator.comparing(WorkloadStatistics::getWorkloadDate));
        for (WorkloadStatistics workloadStatistics : workloadStatisticsList) {
            monthWorkload.add(workloadStatistics.getTotalWorkload());
        }
        Integer expectWorkload = getExpect(monthWorkload, 1, 0.5);
        int dayNumber = DateFormatUtils.getMonthDayNumber(nextStartTime);
        double dayWorkload = 0;
        if (expectWorkload.doubleValue() > nextStatistics.getTotalWorkload()) {
            dayWorkload = (expectWorkload.doubleValue() - nextStatistics.getTotalWorkload())/ dayNumber;
        }
        double value = dayWorkload / (workloadParam.getMaxPeople() * STANDARD_WORKLOAD);
        WorkloadStatistics nextWorkloadStatistics = new WorkloadStatistics();
        for (Double dailyWorkload : dailyWorkloadMap.values()) {
            dailyWorkload = (dailyWorkload + dayWorkload) / (workloadParam.getMaxPeople() * STANDARD_WORKLOAD);
            if (dailyWorkload > workloadParam.getSupperLimit()) {
                nextWorkloadStatistics.setFullDay(nextWorkloadStatistics.getFullDay() + 1);
            } else if (dailyWorkload > workloadParam.getLowerLimit()) {
                nextWorkloadStatistics.setNormalDay(nextWorkloadStatistics.getNormalDay() + 1);
            } else {
                nextWorkloadStatistics.setFreeDay(nextWorkloadStatistics.getFreeDay() + 1);
            }
        }
        if (dayWorkload > 0) {
            int count = dayNumber - dailyWorkloadMap.size();
            if (value > workloadParam.getSupperLimit()) {
                nextWorkloadStatistics.setFullDay(nextWorkloadStatistics.getFullDay() + count);
            } else if (value > workloadParam.getLowerLimit()) {
                nextWorkloadStatistics.setNormalDay(nextWorkloadStatistics.getNormalDay() + count);
            } else {
                nextWorkloadStatistics.setFreeDay(nextWorkloadStatistics.getFreeDay() + count);
            }
        }
        nextWorkloadStatistics.calculate();
        return nextWorkloadStatistics;
    }

    /**
     * 创建 运维管理活动
     * @param monthWorkloadList  List<MonthWorkload> monthWorkloadList
     * @return 运维管理活动
     */
    private List<ManageActivity> createManageActivity(List<MonthWorkload> monthWorkloadList) {
        Long time = System.currentTimeMillis();
        List<ManageActivity> manageActivityList = new ArrayList<>();
        for (MonthWorkload workload : monthWorkloadList) {
            ManageSourceTypeEnum manageSourceTypeEnum;
            if (WorkloadTypeEnum.CURRENT.getType().equals(workload.getValueType())) {
                manageSourceTypeEnum = ManageSourceTypeEnum.PRODUCTIVITY_CURRENT;
            } else {
                manageSourceTypeEnum = ManageSourceTypeEnum.PRODUCTIVITY_FORECAST;
            }
            ManageActivity manageActivity = manageService.createManageActivity(workload.getManageId(), workload.getSourceName(), workload.getSourceCode(),
                    time, null, workload.getCause(), ManageStatusEnum.UN_REDUCE, ManageSourceEnum.PRODUCTIVITY, workload.getWorkloadDate(), manageSourceTypeEnum);
            manageActivityList.add(manageActivity);
        }
        return manageActivityList;
    }

    /**
     * 二次指数平滑法求预测值
     * @param list 基础数据集合
     * @param month 月数
     * @param modulus 平滑系数
     * @return 预测值
     */
    private static Integer getExpect(List<Integer> list, int month, double modulus) {
        double modulusLeft = 1 - modulus;
        double lastIndex =  Double.valueOf(list.get(0));
        double lastSecIndex = Double.valueOf(list.get(0));
        for (Integer data : list) {
            lastIndex = modulus * data + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        double a = 2 * lastIndex - lastSecIndex;
        double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        return (int)Math.round(a + b * month);
    }

    /**
     * 组装团队负荷月份统计数据
     * @param date 日期
     * @param typeEnum 运维管理活动
     * @param workloadType 负荷类型
     * @param valueType 类型（预测或实际）
     * @param current 负荷比
     * @param month 环比
     * @param year 同比
     * @return MonthWorkload 团队负荷
     */
    private MonthWorkload getMonthWorkload(Long date, ManageTypeEnum typeEnum, WorkloadTypeEnum workloadType,
                                           WorkloadTypeEnum valueType, Double current, String month, String year) {
        MonthWorkload workload = new MonthWorkload();
        workload.setWorkloadId(NineteenUUIDUtils.uuid());
        workload.setValueType(valueType.getType());
        workload.setWorkloadDate(date);
        workload.setWorkloadType(workloadType.getType());
        String doubleToString = PercentageUtil.changeDoubleToString(current);
        workload.setWorkloadPercentage(doubleToString);
        workload.setMonthPercentage(month);
        workload.setYearPercentage(year);
        workload.setManageId(NineteenUUIDUtils.uuid());
        workload.setSourceName(typeEnum.getName());
        workload.setSourceCode(typeEnum.getCode());
        workload.setCause(typeEnum.getCause() + doubleToString);
        return workload;
    }

    /**
     * 计算同比、环比数据
     * @param statisticsMap 往期数据
     * @param time 时间
     * @param statistics 本期数据
     * @return  MonthWorkloadDto 同比、环比数据
     */
    private MonthWorkloadDto getMonthWorkloadDto(Map<Long, WorkloadStatistics> statisticsMap,
                                                 Long time, WorkloadStatistics statistics) {
        MonthWorkloadDto workloadDto = new MonthWorkloadDto();
        Double free = statistics.getFreePercentage();
        Double normal = statistics.getNormalPercentage();
        Double full = statistics.getFullPercentage();
        Double freePercentage;
        Double normalPercentage;
        Double fullPercentage;
        if (statisticsMap.containsKey(time)) {
            WorkloadStatistics workSta = statisticsMap.get(time);
            freePercentage = workSta.getFreePercentage();
            normalPercentage = workSta.getNormalPercentage();
            fullPercentage = workSta.getFullPercentage();
        } else {
            freePercentage = ZERO;
            normalPercentage = ZERO;
            fullPercentage = ZERO;
        }
        workloadDto.setFreePercentage(PercentageUtil.calculatePercentage(freePercentage, free));
        workloadDto.setNormalPercentage(PercentageUtil.calculatePercentage(normalPercentage, normal));
        workloadDto.setFullPercentage(PercentageUtil.calculatePercentage(fullPercentage, full));
        return workloadDto;
    }

    /**
     * 统计每种负荷状态天数，计算比率
     * @param startTime 月份
     * @param workloadList 负荷状态天数
     * @return 每种负荷状态比率
     */
    private WorkloadStatistics getWorkloadStatistics(Long startTime, List<DailyWorkload> workloadList) {
        WorkloadStatistics statistics = new WorkloadStatistics();
        statistics.setWorkloadId(NineteenUUIDUtils.uuid());
        statistics.setWorkloadDate(startTime);
        //统计每种负荷状态天数
        for (DailyWorkload dailyWorkload : workloadList) {
            String workStatus = dailyWorkload.getWorkStatus();
            if (WorkloadTypeEnum.FULL_LOAD.getType().equals(workStatus)) {
                statistics.setFullDay(statistics.getFullDay() + 1);
            }
            if (WorkloadTypeEnum.NORMAL.getType().equals(workStatus)) {
                statistics.setNormalDay(statistics.getNormalDay() + 1);
            }
            if (WorkloadTypeEnum.FREE.getType().equals(workStatus)) {
                statistics.setFreeDay(statistics.getFreeDay() + 1);
            }
        }
        statistics.calculate();
        return statistics;
    }

    /**
     * 计算KPI
     * @param performanceList KPI
     */
    private void producePerformance(List<Performance> performanceList) {
        for (Performance performance : performanceList) {
            int dayNightNumber = performance.getDayNightNumberInt();
            int dayNumber = performance.getDayNumberInt();
            int inspectionNumber = performance.getInspectionNumberInt();
            double workload = performance.getWorkloadDouble();
            performance.setWorkEfficiency(workload / (12 * dayNightNumber + 8 * dayNumber - 2 * inspectionNumber));
        }
    }

    /**
     * 计算生成团队负荷
     * @param proStatistics 时间范围
     * @param dailyWorkloadList 团队负荷
     * @return double 最多上架人数
     */
    private WorkloadParam produceDailyWorkload(ProStatistics proStatistics, List<DailyWorkload> dailyWorkloadList) {
        //获取系统参数设置
        List<SystemSetting> systemSettingList = systemService.querySystemValueByCode(
                ProductivityParamEnum.PRODUCTIVITY_PARAM.getCode());
        //团队负荷资源状态上限
        double supperLimit = ProductivityParamEnum.SUPPER_LIMIT.getValue();
        //团队负荷资源状态下限
        double lowerLimit = ProductivityParamEnum.LOWER_LIMIT.getValue();
        //最多上架人数
        double maxPeople = ProductivityParamEnum.MAX_PEOPLE.getValue();
        for (SystemSetting setting : systemSettingList) {
            String code = setting.getCode();
            double value = (double)setting.getValue();
            if (ProductivityParamEnum.SUPPER_LIMIT.getCode().equals(code)) {
                supperLimit = value;
            }
            if (ProductivityParamEnum.LOWER_LIMIT.getCode().equals(code)) {
                lowerLimit = value;
            }
            if (ProductivityParamEnum.MAX_PEOPLE.getCode().equals(code)) {
                maxPeople = value;
            }
        }
        //查询当月变更单
        List<Change> changeList = changeDao.selectChangeForTime(proStatistics);
        for (Change change : changeList) {
            proStatistics.setTotalWorkload(proStatistics.getTotalWorkload() + change.getWorkload());
            //第一天
            Long startDate = DateFormatUtils.getDayStartForTime(change.getStartDate());
            //最后一天
            Long endDate = DateFormatUtils.getDayStartForTime(change.getEndDate());
            //相隔天数
            double dayNum = (endDate - startDate) / DAY_MILLISECOND + 1;
            while (startDate <= endDate) {
                DailyWorkload  workload = new DailyWorkload();
                workload.setChangeDate(startDate);
                workload.setWorkloadId(NineteenUUIDUtils.uuid());
                workload.setProjectCode(change.getChangeCode());
                workload.setProjectName(change.getChangeName());
                workload.setProjectStatus(change.getProjectStatus());
                double totalWorkload = change.getWorkload().doubleValue() / STANDARD_WORKLOAD;
                double dailyWorkload = totalWorkload / dayNum;
                double value = dailyWorkload / change.getPeople();
                workload.setTotalWorkload(totalWorkload);
                workload.setDailyWorkload(dailyWorkload);
                workload.setWorkStatusValue(value);
                if (value > supperLimit) {
                    workload.setWorkStatus(WorkloadTypeEnum.FULL_LOAD.getType());
                } else if (value > lowerLimit) {
                    workload.setWorkStatus(WorkloadTypeEnum.NORMAL.getType());
                } else {
                    workload.setWorkStatus(WorkloadTypeEnum.FREE.getType());
                }
                startDate = DateFormatUtils.getTimeLongNextDay(startDate);
                dailyWorkloadList.add(workload);
            }
        }
        WorkloadParam workloadParam = new WorkloadParam();
        workloadParam.setSupperLimit(supperLimit);
        workloadParam.setLowerLimit(lowerLimit);
        workloadParam.setMaxPeople(maxPeople);
        return workloadParam;
    }

    /**
     * 统计巡检次数 上架数量
     * @param performanceMap 个人KPI集合
     * @param proStatistics 查询条件
     */
    private void queryInspectionAndChangeStatistics(Map<String, Performance> performanceMap,
                                                    ProStatistics proStatistics) {
        //查询巡检次数
        List<ProStatistics> statisticsList = inspectionTaskDao.selectCountOfTime(proStatistics);
        for (ProStatistics statistics : statisticsList) {
            String officer = statistics.getDutyOfficer();
            if (performanceMap.containsKey(officer)) {
                Performance performance = performanceMap.get(officer);
                performance.setInspectionNumber(statistics.getWorkDays());
            }
        }
        //查询巡检次数
        List<ProStatistics> proStatisticsList = changePeopleDao.querySumOfTime(proStatistics);
        for (ProStatistics statistics : proStatisticsList) {
            String officer = statistics.getDutyOfficer();
            if (performanceMap.containsKey(officer)) {
                Performance performance = performanceMap.get(officer);
                performance.setWorkload(statistics.getWorkload());
            }
        }
    }

    /**
     * 统计排班表上班天数
     * @param proStatistics 查询条件
     * @param performanceMap 个人KPI集合
     */
    private void queryScheduleStatistics(ProStatistics proStatistics, Map<String, Performance> performanceMap) {
        //查询排班统计数据
        List<ProStatistics> statisticsList = scheduleDao.selectCountOfMonth(proStatistics);
        for (ProStatistics statistics : statisticsList) {
            String officer = statistics.getDutyOfficer();
            String type = statistics.getScheduleType();
            Integer workDays = statistics.getWorkDays();
            //按人员统计长排班、倒班次数
            if (performanceMap.containsKey(officer)) {
                Performance performance = performanceMap.get(officer);
                if (ScheduleTypeEnum.ONE_DAY.getType().equals(type)) {
                    performance.setDayNumber(workDays);
                } else {
                    Integer dayNightNumber = performance.getDayNightNumber();
                    if (dayNightNumber == null) {
                        performance.setDayNightNumber(workDays);
                    } else {
                        performance.setDayNightNumber(dayNightNumber + workDays);
                    }
                }
            } else {
                Performance performance = new Performance();
                performance.setPerformanceId(NineteenUUIDUtils.uuid());
                performance.setPersonName(officer);
                performance.setWorkDate(proStatistics.getStartTime());
                if (ScheduleTypeEnum.ONE_DAY.getType().equals(type)) {
                    performance.setDayNumber(workDays);
                } else {
                    performance.setDayNightNumber(workDays);
                }
                performanceMap.put(officer, performance);
            }
        }
    }

    /**
     * sheet页表结构都是一样（一个实体时）取出所有数据
     * @param file 表格
     * @return 所有数据
     */
    private List<ExcelReadBean> getExcelReadBeanForSame(MultipartFile file, AbstractExcelImportRead importRead) {
        Map<String, List<ExcelReadBean>> map;
        try {
            map = importRead.readExcel(file);
        } catch (IOException | InvalidFormatException e) {
            //文件格式错误
            log.error("文件({})格式错误:{}", file.getOriginalFilename(), e);
            throw new BizException(ProductivityResultCode.FILE_TYPE_ERROR,
                    ProductivityResultMsg.FILE_TYPE_ERROR);
        }
        List<ExcelReadBean> excelReadBeanList = new ArrayList<>();
        for (List<ExcelReadBean> excelReadBeans : map.values()) {
            excelReadBeanList.addAll(excelReadBeans);
        }
        //是否有数据
        if (CollectionUtils.isEmpty(excelReadBeanList)) {
            throw new BizException(ProductivityResultCode.FILE_EMPTY,
                    ProductivityResultMsg.FILE_EMPTY);
        }
        return excelReadBeanList;
    }
}
