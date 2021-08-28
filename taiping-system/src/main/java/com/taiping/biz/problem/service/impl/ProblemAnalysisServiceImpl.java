package com.taiping.biz.problem.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.problem.dao.*;
import com.taiping.biz.problem.service.ProblemAnalysisService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.DateConstant;
import com.taiping.constant.problem.ProblemConstant;
import com.taiping.constant.problem.ProblemResultCode;
import com.taiping.constant.problem.ProblemResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.problem.*;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.enums.problem.*;
import com.taiping.exception.BizException;
import com.taiping.read.problem.FlashOffExcelImportRead;
import com.taiping.read.problem.TroubleTicketExcelImportRead;
import com.taiping.utils.*;
import com.taiping.utils.problem.TroubleSortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.taiping.utils.DateFormatUtils.dateLongToString;
import static com.taiping.utils.common.PercentageUtil.calculatePercentage;

/**
 * 问题分析服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
@Slf4j
@Service
public class ProblemAnalysisServiceImpl implements ProblemAnalysisService {

    @Autowired
    private TroubleTicketDao troubleTicketDao;

    @Autowired
    private FlashOffDao flashOffDao;

    @Autowired
    private TroubleTypeStatisticsDao troubleTypeStatisticsDao;

    @Autowired
    private TroubleTrendDao troubleTrendDao;

    @Autowired
    private FlashOffTrendDao flashOffTrendDao;

    @Autowired
    private ProblemStatisticsDao problemStatisticsDao;

    @Autowired
    private TroubleTicketExcelImportRead troubleTicketExcelImportRead;

    @Autowired
    private FlashOffExcelImportRead flashOffExcelImportRead;

    @Autowired
    private ParamManageService paramManageService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ManageActivityService manageService;

    /**
     * 导入故障单Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    public Result importTroubleTicket(MultipartFile file) {
        List<TroubleTicket> troubleTicketList = (List) getExcelReadBeanForSame(file, troubleTicketExcelImportRead);
        troubleTicketDao.addTroubleTicketBatch(troubleTicketList);
        return ResultUtils.success();
    }

    /**
     * 导入停水停电记录Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    public Result importFlashOff(MultipartFile file) {
        List<FlashOff> flashOffList = (List) getExcelReadBeanForSame(file, flashOffExcelImportRead);
        flashOffDao.addFlashOffBatch(flashOffList);
        return ResultUtils.success();
    }

    /**
     * 查询故障单列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectTroubleTicketList(QueryCondition<TroubleTicket> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "troubleTime", "desc");
        //查询
        List<TroubleTicket> troubleTicketList = troubleTicketDao.selectTroubleTicketList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //替换换行
        for (TroubleTicket troubleTicket : troubleTicketList) {
            String handleDescription = troubleTicket.getHandleDescription();
            if (handleDescription != null) {
                handleDescription = handleDescription.replace(ProblemConstant.LINE_FEED_REPLACE,
                        ProblemConstant.BLANK_SPACE_REPLACE);
                handleDescription = handleDescription.replace(ProblemConstant.LINE_REPLACE,
                        ProblemConstant.BLANK_SPACE_REPLACE);
                troubleTicket.setHandleDescription(handleDescription);
            }
        }
        //查询总条数
        Integer count = troubleTicketDao.selectTroubleTicketListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, troubleTicketList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 查询停水停电记录列表
     *
     * @param queryCondition 查询条件
     * @return Result
     */
    @Override
    public Result selectFlashOffList(QueryCondition<FlashOff> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "offDate", "desc");
        //查询
        List<FlashOff> flashOffList = flashOffDao.selectFlashOffList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = flashOffDao.selectFlashOffListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, flashOffList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 新增故障单
     *
     * @param troubleTicket 故障单
     * @return Result
     */
    @Override
    public Result createTroubleTicket(TroubleTicket troubleTicket) {
        Integer integer = troubleTicketDao.selectTroubleTicketByCode(troubleTicket);
        //故障单号重复
        if ((integer != null) && (integer > 0)) {
            return ResultUtils.warn(ProblemResultCode.TICKET_CODE_HAVE,
                    ProblemResultMsg.TICKET_CODE_HAVE);
        }
        String troubleType = TroubleSortUtil.getNameForCode(
                troubleTicket.getTopType(), troubleTicket.getSecondaryType());
        troubleTicket.setTroubleType(troubleType);
        troubleTicket.setValueType(TroubleTypeEnum.ADD.getType());
        troubleTicket.setTicketId(NineteenUUIDUtils.uuid());
        troubleTicketDao.insertTroubleTicket(troubleTicket);
        return ResultUtils.success();
    }

    /**
     * 查询故障单号是否重复
     *
     * @param troubleTicket 故障单号
     * @return Result
     */
    @Override
    public Result queryTicketCode(TroubleTicket troubleTicket) {
        Integer integer = troubleTicketDao.selectTroubleTicketByCode(troubleTicket);
        boolean have = false;
        if ((integer != null) && (integer > 0)) {
            have = true;
        }
        return ResultUtils.success(have);
    }

    /**
     * 根据ID查询故障单详情
     *
     * @param ticketId 故障单ID
     * @return Result
     */
    @Override
    public Result queryTroubleTicket(String ticketId) {
        TroubleTicket troubleTicket = troubleTicketDao.queryTroubleTicketById(ticketId);
        //故障单已删除
        if (troubleTicket == null) {
            return ResultUtils.warn(ProblemResultCode.TROUBLE_TICKET_DELETED,
                    ProblemResultMsg.TROUBLE_TICKET_DELETED);
        }
        return ResultUtils.success(troubleTicket);
    }

    /**
     * 修改故障单
     *
     * @param troubleTicket 故障单
     * @return Result
     */
    @Override
    public Result updateTroubleTicket(TroubleTicket troubleTicket) {
        TroubleTicket troubleTicketDb = troubleTicketDao.queryTroubleTicketById(troubleTicket.getTicketId());
        //故障单已删除
        if (troubleTicketDb == null) {
            return ResultUtils.warn(ProblemResultCode.TROUBLE_TICKET_DELETED,
                    ProblemResultMsg.TROUBLE_TICKET_DELETED);
        }
        //故障单号重复
        Integer integer = troubleTicketDao.selectTroubleTicketByCode(troubleTicket);
        //故障单号重复
        if ((integer != null) && (integer > 0)) {
            return ResultUtils.warn(ProblemResultCode.TICKET_CODE_HAVE,
                    ProblemResultMsg.TICKET_CODE_HAVE);
        }
        //导入的故障单不能修改
        if (TroubleTypeEnum.IMPORT.getType().equals(troubleTicketDb.getValueType())) {
            return ResultUtils.warn(ProblemResultCode.TROUBLE_TICKET_IMPORT_UPDATE,
                    ProblemResultMsg.TROUBLE_TICKET_IMPORT_UPDATE);
        }
        //故障分类名称
        String troubleType = TroubleSortUtil.getNameForCode(
                troubleTicket.getTopType(), troubleTicket.getSecondaryType());
        troubleTicket.setTroubleType(troubleType);
        //更新
        troubleTicketDao.updateTroubleTicketById(troubleTicket);
        return ResultUtils.success();
    }


    /**
     * 删除故障单
     *
     * @param ticketId 故障单ID
     * @return Result
     */
    @Override
    public Result deleteTroubleTicket(String ticketId) {
        TroubleTicket troubleTicket = troubleTicketDao.queryTroubleTicketById(ticketId);
        //故障单已删除
        if (troubleTicket == null) {
            return ResultUtils.warn(ProblemResultCode.TROUBLE_TICKET_DELETED,
                    ProblemResultMsg.TROUBLE_TICKET_DELETED);
        }
        //导入的故障单不能删除
        if (TroubleTypeEnum.IMPORT.getType().equals(troubleTicket.getValueType())) {
            return ResultUtils.warn(ProblemResultCode.TROUBLE_TICKET_IMPORT_DELETE,
                    ProblemResultMsg.TROUBLE_TICKET_IMPORT_DELETE);
        }
        troubleTicketDao.deleteTroubleTicket(ticketId);
        return ResultUtils.success();
    }

    /**
     * 生成分析报告
     *
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createReport() {
        //校验预览
        paramManageService.checkPreview(ManageSourceEnum.PROBLEM);
        //查询运维管理活动
        List<ManageActivity> manageActivityList =
                manageService.queryManageActivityForMode(ManageSourceEnum.PROBLEM);
        //更改当前月分析报告类型
        troubleTypeStatisticsDao.cancelReportCurrent();
        problemStatisticsDao.cancelReportCurrent();
        //生成分析报告
        troubleTypeStatisticsDao.updateBatchTroubleTypeStatistics(manageActivityList);
        problemStatisticsDao.updateBatchProblemStatistics(manageActivityList);
        //修改模块参数类型值已分析
        paramManageService.updateParamManage(ManageSourceEnum.PROBLEM, ParamTypeEnum.PREVIEW, null);
        return ResultUtils.success();
    }

    /**
     * 查询分析报告
     *
     * @param report 分析报告
     * @return Result
     */
    @Override
    public Result requestReport(ProblemReport report) {
        //分析报告日期
        Long reportDate = report.getReportDate();
        //分析报告实体
        ProblemReport problemReport = new ProblemReport();
        //查询故障总览、断水断电、故障二级分类top数据
        List<ProblemStatistics> statisticsList;
        if (reportDate == null) {
            //查询最新分析报告数据
            statisticsList = problemStatisticsDao.queryReportForCurrent();
        } else {
            //查询指定月份分析报告数据
            reportDate = DateFormatUtils.getMonthStartForTime(reportDate);
            statisticsList = problemStatisticsDao.queryReportForTime(reportDate);
        }
        //无分析报告
        if (CollectionUtils.isEmpty(statisticsList)) {
            return ResultUtils.success(problemReport);
        }
        //当前分析报告日期
        reportDate = statisticsList.get(0).getValueDate();
        //前12个月和未来一个月日期集合
        List<Long> monthOfYear = DateFormatUtils.getMonthOfYear(reportDate);
        //转日期字符串
        List<String> time = new ArrayList<>();
        for (Long month : monthOfYear) {
            String monthStr = dateLongToString(DateConstant.FORMAT_STRING_FOUR, month);
            time.add(monthStr);
        }
        //拆分故障总览、断水断电、故障二级分类top数据
        splitProblemStatistics(reportDate, problemReport, statisticsList);
        //查询故障一级分类下故障二级分类top
        List<TroubleTypeStatistics> typeStatistics = troubleTypeStatisticsDao.queryReportForTime(reportDate);
        problemReport.setTypeStatistics(typeStatistics);
        //断水、断电趋势曲线
        ProblemTrend problemTrend = new ProblemTrend();
        problemTrend.setXData(time);
        //断水、断电趋势曲线曲线数据
        List<List<Trend>> listList = new ArrayList<>();
        //查询组装断水、断电趋势曲线曲线数据
        Map<String, List<Trend>> stringListMap = getFlashOffTrend(reportDate);
        //停水
        listList.add(stringListMap.get(FlashOffTypeEnum.WATER_CUT.getTypeCode()));
        //断电
        listList.add(stringListMap.get(FlashOffTypeEnum.POWER_CUT.getTypeCode()));
        //闪断
        listList.add(stringListMap.get(FlashOffTypeEnum.POWER_FLASH_OFF.getTypeCode()));
        problemTrend.setYData(listList);
        problemReport.setFlashOffTrendDto(problemTrend);
        //故障级别趋势曲线曲线数据
        Map<Long, TroubleTrend> trendMap = new HashMap<>(16);
        //故障分类趋势曲线
        TroubleTrendDto troubleTrendDto = new TroubleTrendDto();
        troubleTrendDto.setTrendDate(time);
        //查询组装故障分类趋势曲线曲线数据
        Map<String, Map<String, List<Integer>>> stringMapMap = getTroubleTrendMap(reportDate, trendMap);
        troubleTrendDto.setTrendValue(stringMapMap);
        problemReport.setTroubleTrendDto(troubleTrendDto);
        //组装故障总览曲线
        ProblemTrend trouble = getProblemTrend(time, trendMap);
        problemReport.setTroubleTrend(trouble);
        return ResultUtils.success(problemReport);
    }

    /**
     * 组装故障总览曲线
     * @param time 日期字符串
     * @param trendMap 故障总览曲线
     * @return 故障总览曲线
     */
    private ProblemTrend getProblemTrend(List<String> time, Map<Long, TroubleTrend> trendMap) {
        //故障总览曲线
        ProblemTrend trouble = new ProblemTrend();
        trouble.setXData(time);
        //故障总览曲线曲线数据
        List<List<Trend>> troubleList = new ArrayList<>();
        List<Trend> veryLow = new ArrayList<>();
        List<Trend> low = new ArrayList<>();
        List<Trend> moderate = new ArrayList<>();
        List<Trend> high = new ArrayList<>();
        List<Trend> veryHigh = new ArrayList<>();
        List<TroubleTrend> trendList = new ArrayList<>(trendMap.values());
        trendList.sort(Comparator.comparing(TroubleTrend::getTrendDate));
        for (TroubleTrend troubleTrend : trendList) {
            Trend veryLowTrend = new Trend();
            Trend lowTrend = new Trend();
            Trend moderateTrend = new Trend();
            Trend highTrend = new Trend();
            Trend veryHighTrend = new Trend();
            veryLowTrend.setName(TroubleLevelEnum.VERY_LOW.getLevelName());
            lowTrend.setName(TroubleLevelEnum.LOW.getLevelName());
            moderateTrend.setName(TroubleLevelEnum.MODERATE.getLevelName());
            highTrend.setName(TroubleLevelEnum.HIGH.getLevelName());
            veryHighTrend.setName(TroubleLevelEnum.VERY_HIGH.getLevelName());
            veryLowTrend.setValue(troubleTrend.getVeryLowNumber());
            lowTrend.setValue(troubleTrend.getLowNumber());
            moderateTrend.setValue(troubleTrend.getModerateNumber());
            highTrend.setValue(troubleTrend.getHighNumber());
            veryHighTrend.setValue(troubleTrend.getVeryHighNumber());
            veryLow.add(veryLowTrend);
            low.add(lowTrend);
            moderate.add(moderateTrend);
            high.add(highTrend);
            veryHigh.add(veryHighTrend);
        }
        troubleList.add(veryLow);
        troubleList.add(low);
        troubleList.add(moderate);
        troubleList.add(high);
        troubleList.add(veryHigh);
        trouble.setYData(troubleList);
        return trouble;
    }

    /**
     * 拆分故障总览、断水断电、故障二级分类top数据
     * @param reportDate 当前分析报告日期
     * @param problemReport 分析报告
     * @param statisticsList 故障总览、断水断电、故障二级分类top数据
     */
    private void splitProblemStatistics(Long reportDate, ProblemReport problemReport,
                                        List<ProblemStatistics> statisticsList) {
        //故障总览
        List<ProblemStatistics> troubleLevel = new ArrayList<>();
        //断水、断电
        List<ProblemStatistics> flashOff = new ArrayList<>();
        //故障二级分类Top
        List<ProblemStatistics> troubleType = new ArrayList<>();
        //拆分故障总览、断水断电、故障二级分类top数据
        for (ProblemStatistics problemStatistics : statisticsList) {
            String statisticsType = problemStatistics.getStatisticsType();
            if (ProblemStatisticsEnum.OVERVIEW.getTypeCode().equals(statisticsType)) {
                troubleLevel.add(problemStatistics);
            } else if (ProblemStatisticsEnum.FLASH_OFF.getTypeCode().equals(statisticsType)) {
                flashOff.add(problemStatistics);
            } else {
                troubleType.add(problemStatistics);
            }
        }
        //故障二级分类top数据按照数量排序
        troubleType.sort(Comparator.comparing(ProblemStatistics::getValueNumber).reversed());
        problemReport.setTroubleLevel(troubleLevel);
        problemReport.setFlashOff(flashOff);
        problemReport.setTroubleType(troubleType);
        problemReport.setReportDate(reportDate);
    }

    /**
     * 查询组装断水断电趋势曲线曲线数据
     * @param reportDate 当月时间
     * @return Map<String, List<Trend>>
     */
    private Map<String, List<Trend>> getFlashOffTrend(Long reportDate) {
        //查询断水断电趋势曲线曲线数据
        List<FlashOffTrend> flashOffTrendList = flashOffTrendDao.queryReport(reportDate);
        //断水断电趋势曲线曲线数据
        Map<String, List<Trend>> stringListMap = new HashMap<>(16);
        //无数据
        if (CollectionUtils.isEmpty(flashOffTrendList)) {
            return stringListMap;
        }
        //按照类型分类断水、断电组装曲线
        for (FlashOffTrend flashOffTrend : flashOffTrendList) {
            String offType = flashOffTrend.getOffType();
            Trend trend = new Trend();
            trend.setName(FlashOffTypeEnum.getNameForCode(flashOffTrend.getOffType()));
            trend.setValue(flashOffTrend.getOffNumber());
            if (stringListMap.containsKey(offType)) {
                List<Trend> trendList = stringListMap.get(offType);
                trendList.add(trend);
            } else {
                List<Trend> trendList = new ArrayList<>();
                trendList.add(trend);
                stringListMap.put(offType, trendList);
            }
        }
        return stringListMap;
    }

    /**
     * 查询组装故障分类趋势曲线曲线数据
     * @param reportDate 当月时间
     * @param trendMap 故障总览趋势曲线
     * @return Map<String, Map<String, List<Integer>>>
     */
    private Map<String, Map<String, List<Integer>>> getTroubleTrendMap(Long reportDate,
                                                                       Map<Long, TroubleTrend> trendMap) {
        //故障分类趋势曲线曲线数据
        Map<String, Map<String, List<Integer>>> stringMapMap = new LinkedHashMap<>();
        //查询故障分类趋势曲线曲线数据
        List<TroubleTrend> troubleTrendList = troubleTrendDao.queryReport(reportDate);
        //无数据
        if (CollectionUtils.isEmpty(troubleTrendList)) {
            return stringMapMap;
        }
        //极低级别
        String veryLowLevelCode = TroubleLevelEnum.VERY_LOW.getLevelCode();
        //低级别
        String lowLevelCode = TroubleLevelEnum.LOW.getLevelCode();
        //中级别
        String moderateLevelCode = TroubleLevelEnum.MODERATE.getLevelCode();
        //高级别
        String highLevelCode = TroubleLevelEnum.HIGH.getLevelCode();
        //极高级别
        String veryHighLevelCode = TroubleLevelEnum.VERY_HIGH.getLevelCode();
        //按照故障分类分类每个级别组装曲线
        for (TroubleTrend troubleTrend : troubleTrendList) {
            Long trendDate = troubleTrend.getTrendDate();
            //组装故障级别趋势曲线曲线数据
            if (trendMap.containsKey(trendDate)) {
                TroubleTrend trend = trendMap.get(trendDate);
                trend.setVeryLowNumber(trend.getVeryLowNumber() + troubleTrend.getVeryLowNumber());
                trend.setLowNumber(trend.getLowNumber() + troubleTrend.getLowNumber());
                trend.setModerateNumber(trend.getModerateNumber() + troubleTrend.getModerateNumber());
                trend.setHighNumber(trend.getHighNumber() + troubleTrend.getHighNumber());
                trend.setVeryHighNumber(trend.getVeryHighNumber() + troubleTrend.getVeryHighNumber());
            } else {
                trendMap.put(trendDate, troubleTrend);
            }
            //组装分类分类每个级别趋势曲线曲线数据
            Map<String, List<Integer>> listMap;
            String topType = troubleTrend.getTopType();
            if (stringMapMap.containsKey(topType)) {
                listMap = stringMapMap.get(topType);
                List<Integer> veryLowLevel = listMap.get(veryLowLevelCode);
                List<Integer> lowLevel = listMap.get(lowLevelCode);
                List<Integer> moderateLevel = listMap.get(moderateLevelCode);
                List<Integer> highLevel = listMap.get(highLevelCode);
                List<Integer> veryHighLevel = listMap.get(veryHighLevelCode);
                veryLowLevel.add(troubleTrend.getVeryLowNumber());
                lowLevel.add(troubleTrend.getLowNumber());
                moderateLevel.add(troubleTrend.getModerateNumber());
                highLevel.add(troubleTrend.getHighNumber());
                veryHighLevel.add(troubleTrend.getVeryHighNumber());
            } else {
                listMap = new LinkedHashMap<>();
                List<Integer> veryLowLevel = new ArrayList<>();
                List<Integer> lowLevel = new ArrayList<>();
                List<Integer> moderateLevel = new ArrayList<>();
                List<Integer> highLevel = new ArrayList<>();
                List<Integer> veryHighLevel = new ArrayList<>();
                veryLowLevel.add(troubleTrend.getVeryLowNumber());
                lowLevel.add(troubleTrend.getLowNumber());
                moderateLevel.add(troubleTrend.getModerateNumber());
                highLevel.add(troubleTrend.getHighNumber());
                veryHighLevel.add(troubleTrend.getVeryHighNumber());
                listMap.put(TroubleLevelEnum.VERY_LOW.getLevelCode(), veryLowLevel);
                listMap.put(TroubleLevelEnum.LOW.getLevelCode(), lowLevel);
                listMap.put(TroubleLevelEnum.MODERATE.getLevelCode(), moderateLevel);
                listMap.put(TroubleLevelEnum.HIGH.getLevelCode(), highLevel);
                listMap.put(TroubleLevelEnum.VERY_HIGH.getLevelCode(), veryHighLevel);
                stringMapMap.put(topType, listMap);
            }
        }
        return stringMapMap;
    }

    /**
     * 分析数据
     *                                                                          
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result analysisProblem() {
        //获取最新数据时间
        Long nearDate = getNearDate();
        //运维管理活动
        List<ManageActivity> manageActivityList = new ArrayList<>();
        //最新数据月份开始时间
        Long startDate = DateFormatUtils.getMonthStartForTime(nearDate);
        //最新数据月份结束时间
        Long endDate = DateFormatUtils.getMonthEndForTime(nearDate);
        //最新数据月份去年同月开始时间
        Long lastDate = DateFormatUtils.getTimeLastYear(nearDate);
        //最新数据月份前一个月开始时间
        Long monthDate = DateFormatUtils.getTimeLastMonth(startDate);
        //最新数据月份下个月开始时间
        Long nextDate = DateFormatUtils.getNextMonthStartForTime(startDate);
        //数据查询时间范围
        ProblemSelect problemSelect = new ProblemSelect();
        problemSelect.setStartTime(lastDate);
        problemSelect.setEndTime(endDate);
        //前12个月和未来一个月
        List<Long> monthOfYear = DateFormatUtils.getMonthOfYear(startDate);
        //故障分类趋势
        Map<Long, Map<String, TroubleTrend>> trendMap = new HashMap<>(64);
        //断水断电趋势
        Map<Long, Map<String, FlashOffTrend>> offTrendMap = new HashMap<>(64);
        //组装趋势分析曲线
        createTrend(startDate, monthOfYear, trendMap, offTrendMap);
        //统计分析故障单数据
        List<ProblemStatistics> statisticsList = produceTroubleStatistics(manageActivityList, startDate,
                lastDate, monthDate, problemSelect, trendMap);
        //故障单趋势分析
        produceTroubleTrend(startDate, nextDate, monthOfYear, trendMap);
        List<TroubleTrend> troubleTrendList = new ArrayList<>();
        for (Map<String, TroubleTrend> map : trendMap.values()) {
            troubleTrendList.addAll(map.values());
        }
        //存储故障分类趋势曲线
        troubleTrendDao.insertTroubleTrendBatch(troubleTrendList);
        //统计断水断电，范围去年同月统计数据
        Map<String, FlashOffTrend> lastTrendMap = createFlashOffTrendMap(startDate, lastDate, problemSelect, offTrendMap);
        //当前月
        Map<String, FlashOffTrend> currentTrendMap = offTrendMap.get(startDate);
        //前一个月
        Map<String, FlashOffTrend> monthTrendMap = offTrendMap.get(monthDate);
        //未来一个月
        Map<String, FlashOffTrend> nextOffTrendMap = offTrendMap.get(nextDate);
        //未来一个月去年同月
        Long nextYearDate = DateFormatUtils.getTimeLastYear(nextDate);
        //未来一个月去年同月
        Map<String, FlashOffTrend> nextYearTrendMap = offTrendMap.get(nextYearDate);
        //统计分析断水断电
        statisticsFlashOff(manageActivityList, startDate, statisticsList, lastTrendMap, currentTrendMap, monthTrendMap);
        //存储问题分析统计数据
        problemStatisticsDao.insertProblemStatisticsBatch(statisticsList);
        //存储运维管理活动
        manageService.insertManageActivityNoEquals(ManageSourceEnum.PROBLEM, manageActivityList);
        //趋势分析断水断电
        produceFlashOffTrend(lastTrendMap, currentTrendMap, nextOffTrendMap, nextYearTrendMap);
        List<FlashOffTrend> flashOffTrendList = new ArrayList<>();
        for (Map<String, FlashOffTrend> flashOffTrendMap : offTrendMap.values()) {
            flashOffTrendList.addAll(flashOffTrendMap.values());
        }
        //存储断水断电趋势曲线
        flashOffTrendDao.insertFlashOffTrendBatch(flashOffTrendList);
        //修改模块参数类型值已分析
        paramManageService.updateParamManage(ManageSourceEnum.PROBLEM, ParamTypeEnum.ANALYZE, startDate);
        return ResultUtils.success();
    }

    /**
     * 趋势分析断水断电
     * @param lastTrendMap 去年同月
     * @param currentTrendMap 当前月
     * @param nextOffTrendMap 未来月份
     * @param nextYearTrendMap 未来月份去年同月
     */
    private void produceFlashOffTrend(Map<String, FlashOffTrend> lastTrendMap, Map<String, FlashOffTrend> currentTrendMap,
                                      Map<String, FlashOffTrend> nextOffTrendMap, Map<String, FlashOffTrend> nextYearTrendMap) {
        for (FlashOffTrend flashOffTrend : currentTrendMap.values()) {
            Integer offNumber = flashOffTrend.getOffNumber();
            if (offNumber != 0) {
                String offType = flashOffTrend.getOffType();
                FlashOffTrend nextOffTrend = nextOffTrendMap.get(offType);
                FlashOffTrend nextYearOffTrend = nextYearTrendMap.get(offType);
                FlashOffTrend yearOffTrend = lastTrendMap.get(offType);
                Integer nextOff;
                Integer nextYearOff = nextYearOffTrend.getOffNumber();
                Integer yearOff = yearOffTrend.getOffNumber();
                if (nextYearOff == 0) {
                    nextOff = 0;
                } else if (yearOff == 0) {
                    nextOff = offNumber;
                } else {
                    nextOff = (int)Math.round(offNumber * nextYearOff.doubleValue() / yearOff);
                }
                nextOffTrend.setOffNumber(nextOff);
            }
        }
    }

    /**
     * 统计分析断水断电
     * @param manageActivityList 运维管理活动
     * @param startDate 当前月
     * @param statisticsList 问题分析统计数据
     * @param lastTrendMap 去年同月
     * @param currentTrendMap 当前月
     * @param monthTrendMap 前一个月
     */
    private void statisticsFlashOff(List<ManageActivity> manageActivityList, Long startDate,
                                    List<ProblemStatistics> statisticsList, Map<String, FlashOffTrend> lastTrendMap,
                                    Map<String, FlashOffTrend> currentTrendMap, Map<String, FlashOffTrend> monthTrendMap) {
        for (FlashOffTypeEnum flashOffTypeEnum : FlashOffTypeEnum.values()) {
            ProblemStatistics problemStatistics = new ProblemStatistics();
            problemStatistics.setStatisticsId(NineteenUUIDUtils.uuid());
            problemStatistics.setStatisticsType(ProblemStatisticsEnum.FLASH_OFF.getTypeCode());
            String typeCode = flashOffTypeEnum.getTypeCode();
            problemStatistics.setValueType(typeCode);
            FlashOffTrend currentTrend = currentTrendMap.get(typeCode);
            FlashOffTrend monthTrend = monthTrendMap.get(typeCode);
            FlashOffTrend yearTrend = lastTrendMap.get(typeCode);
            problemStatistics.setValueNumber(currentTrend.getOffNumber());
            String monthPercentage = calculatePercentage(monthTrend.getOffNumber(), currentTrend.getOffNumber());
            problemStatistics.setMonthPercentage(monthPercentage);
            String yearPercentage = calculatePercentage(yearTrend.getOffNumber(), currentTrend.getOffNumber());
            problemStatistics.setYearPercentage(yearPercentage);
            problemStatistics.setValueDate(startDate);
            problemStatistics.setManageId(NineteenUUIDUtils.uuid());
            String typeName = flashOffTypeEnum.getTypeName();
            problemStatistics.setSourceName(typeName);
            problemStatistics.setSourceCode(typeCode);
            String cause = ProblemConstant.OFF_CAUSE.replace(ProblemConstant.NAME_REPLACE, typeName);
            cause = cause.replace(ProblemConstant.NUM_REPLACE, String.valueOf(currentTrend.getOffNumber()));
            problemStatistics.setCause(cause);
            String yearStr = getYear(startDate);
            String monthStr = getMonth(startDate);
            String msg = getStringMsg(ProblemConstant.REPORT_MSG, yearStr, yearPercentage,
                    ProblemConstant.YEAR_NAME_REPLACE, ProblemConstant.YEAR_REPLACE);
            msg = getStringMsg(msg, monthStr, monthPercentage,
                    ProblemConstant.MONTH_NAME_REPLACE, ProblemConstant.MONTH_REPLACE);
            problemStatistics.setInstruction(msg);
            statisticsList.add(problemStatistics);
            Long time = System.currentTimeMillis();
            ManageActivity manageActivity = manageService.createManageActivity(problemStatistics.getManageId(), typeName,
                    typeCode, time, null, cause, ManageStatusEnum.UN_REDUCE, ManageSourceEnum.PROBLEM, startDate, ManageSourceTypeEnum.PROBLEM_FLASH_OFF);
            manageActivityList.add(manageActivity);
        }
    }

    /**
     * 统计断水断电，范围去年同月统计数据
     * @param startDate 当前月份
     * @param lastDate 去年同月
     * @param problemSelect 时间范围
     * @param offTrendMap  断水断电趋势
     * @return 去年同月统计数据
     */
    private Map<String, FlashOffTrend> createFlashOffTrendMap(Long startDate, Long lastDate, ProblemSelect problemSelect,
                                                              Map<Long, Map<String, FlashOffTrend>> offTrendMap) {
        Map<String, FlashOffTrend> lastTrendMap = new HashMap<>(16);
        getFlashOffTrend(startDate, lastDate, lastTrendMap, FlashOffTypeEnum.WATER_CUT.getTypeCode());
        getFlashOffTrend(startDate, lastDate, lastTrendMap, FlashOffTypeEnum.POWER_CUT.getTypeCode());
        getFlashOffTrend(startDate, lastDate, lastTrendMap, FlashOffTypeEnum.POWER_FLASH_OFF.getTypeCode());
        //查询断水断电
        List<FlashOff> flashOffList = flashOffDao.selectFlashOffForTime(problemSelect);
        for (FlashOff flashOff : flashOffList) {
            Long time = DateFormatUtils.getMonthStartForTime(flashOff.getOffDate());
            String offType = flashOff.getOffType();
            if (offTrendMap.containsKey(time)) {
                Map<String, FlashOffTrend> flashOffTrendMap = offTrendMap.get(time);
                FlashOffTrend flashOffTrend = flashOffTrendMap.get(offType);
                flashOffTrend.setOffNumber(flashOffTrend.getOffNumber() + 1);
            }
            if (time.equals(lastDate)) {
                if (lastTrendMap.containsKey(offType)) {
                    FlashOffTrend flashOffTrend = lastTrendMap.get(offType);
                    flashOffTrend.setOffNumber(flashOffTrend.getOffNumber() + 1);
                } else {
                    FlashOffTrend flashOffTrend = new FlashOffTrend();
                    flashOffTrend.setOffType(offType);
                    flashOffTrend.setOffNumber(flashOffTrend.getOffNumber() + 1);
                    lastTrendMap.put(offType, flashOffTrend);
                }
            }
        }
        return lastTrendMap;
    }

    /**
     * 故障单趋势分析
     * @param startDate 当前月份
     * @param nextDate 未来月份
     * @param monthOfYear 曲线时间
     * @param trendMap 趋势曲线
     */
    private void produceTroubleTrend(Long startDate, Long nextDate, List<Long> monthOfYear,
                                     Map<Long, Map<String, TroubleTrend>> trendMap) {
        Map<String, TroubleTrend> troubleTrendMap = trendMap.get(startDate);
        Map<String, TroubleTrend> nextTrendMap = trendMap.get(nextDate);
        for (TroubleTrend troubleTrend : troubleTrendMap.values()) {
            TroubleTrend nextTrend = nextTrendMap.get(troubleTrend.getTopType());
            if (troubleTrend.getVeryLowNumber() != 0) {
                List<Integer> num = new ArrayList<>();
                for (int i = 0; i < monthOfYear.size() - 1; i++) {
                    Long date = monthOfYear.get(i);
                    Map<String, TroubleTrend> map = trendMap.get(date);
                    TroubleTrend trend = map.get(troubleTrend.getTopType());
                    num.add(trend.getVeryLowNumber());
                }
                nextTrend.setVeryLowNumber(getExpect(num, 1, 0.5));
            }
            if (troubleTrend.getLowNumber() != 0) {
                List<Integer> lowNum = new ArrayList<>();
                for (int i = 0; i < monthOfYear.size() - 1; i++) {
                    Long date = monthOfYear.get(i);
                    Map<String, TroubleTrend> map = trendMap.get(date);
                    TroubleTrend trend = map.get(troubleTrend.getTopType());
                    lowNum.add(trend.getLowNumber());
                }
                nextTrend.setLowNumber(getExpect(lowNum, 1, 0.5));
            }
            if (troubleTrend.getModerateNumber() != 0) {
                List<Integer> moderateNum = new ArrayList<>();
                for (int i = 0; i < monthOfYear.size() - 1; i++) {
                    Long date = monthOfYear.get(i);
                    Map<String, TroubleTrend> map = trendMap.get(date);
                    TroubleTrend trend = map.get(troubleTrend.getTopType());
                    moderateNum.add(trend.getModerateNumber());
                }
                nextTrend.setModerateNumber(getExpect(moderateNum, 1, 0.5));
            }
            if (troubleTrend.getHighNumber() != 0) {
                List<Integer> highNum = new ArrayList<>();
                for (int i = 0; i < monthOfYear.size() - 1; i++) {
                    Long date = monthOfYear.get(i);
                    Map<String, TroubleTrend> map = trendMap.get(date);
                    TroubleTrend trend = map.get(troubleTrend.getTopType());
                    highNum.add(trend.getHighNumber());
                }
                nextTrend.setHighNumber(getExpect(highNum, 1, 0.5));
            }
            if (troubleTrend.getVeryHighNumber() != 0) {
                List<Integer> veryHighNum = new ArrayList<>();
                for (int i = 0; i < monthOfYear.size() - 1; i++) {
                    Long date = monthOfYear.get(i);
                    Map<String, TroubleTrend> map = trendMap.get(date);
                    TroubleTrend trend = map.get(troubleTrend.getTopType());
                    veryHighNum.add(trend.getVeryHighNumber());
                }
                nextTrend.setVeryHighNumber(getExpect(veryHighNum, 1, 0.5));
            }
        }
    }

    /**
     * 统计分析故障单数据
     * @param manageActivityList 运维管理活动
     * @param startDate 当前月份
     * @param lastDate 去年同月
     * @param monthDate 前一个月
     * @param problemSelect 事案件范围
     * @param trendMap 趋势曲线
     * @return List<ProblemStatistics> 问题分析统计数据
     */
    private List<ProblemStatistics> produceTroubleStatistics(List<ManageActivity> manageActivityList, Long startDate,
                                                             Long lastDate, Long monthDate, ProblemSelect problemSelect,
                                                             Map<Long, Map<String, TroubleTrend>> trendMap) {
        //故障单按年分组
        Map<Long, List<TroubleTicket>> ticketMap = new HashMap<>(64);
        //统计故障单
        statisticsTroubleTicket(problemSelect, trendMap, ticketMap);
        //问题分析统计数据
        List<ProblemStatistics> statisticsList = new ArrayList<>();
        //当前数据月份故障二级分类统计结果
        Map<String, Map<String, TroubleTypeStatistics>> statisticsMap = new HashMap<>(64);
        //当前数据月份前一个月故障二级分类统计结果
        Map<String, Map<String, TroubleTypeStatistics>> monthMap = new HashMap<>(64);
        //当前数据月份去年同月故障二级分类统计结果
        Map<String, Map<String, TroubleTypeStatistics>> yearMap = new HashMap<>(64);
        //统计故障级别数据
        boolean have = produceTroubleLevel(manageActivityList, startDate, lastDate, monthDate,
                ticketMap, statisticsList, statisticsMap, monthMap, yearMap);
        //获取系统参数设置
        List<SystemSetting> systemSettingList = systemService.querySystemValueByCode(
                ProblemParamEnum.PROBLEM_PARAM.getCode());
        //top
        int top = ProblemParamEnum.TOP.getValue();
        for (SystemSetting setting : systemSettingList) {
            String code = setting.getCode();
            Double aDouble = (Double) setting.getValue();
            int value = aDouble.intValue();
            if (ProblemParamEnum.TOP.getCode().equals(code)) {
                top = value;
            }
        }
        if (have) {
            List<TroubleTypeStatistics> typeStatistics = new ArrayList<>();
            for (Map<String, TroubleTypeStatistics> map : statisticsMap.values()) {
                List<TroubleTypeStatistics> statistics = new ArrayList<>(map.values());
                statistics.sort(Comparator.comparing(TroubleTypeStatistics::getTotalNumber).reversed());
                int length = Math.min(statistics.size(), top);
                for (int i = 0; i < length; i++) {
                    TroubleTypeStatistics troubleTypeStatistics = statistics.get(i);
                    troubleTypeStatistics.setCause(String.valueOf(i + 1));
                    typeStatistics.add(troubleTypeStatistics);
                }
            }
            troubleTypeTop(manageActivityList, startDate, monthMap, yearMap, typeStatistics);
            troubleTypeStatisticsDao.insertTroubleTypeStatisticsBatch(typeStatistics);
            typeStatistics.sort(Comparator.comparing(TroubleTypeStatistics::getTotalNumber).reversed());
            int length = Math.min(typeStatistics.size(), top);
            //所有分类top
            for (int i = 0; i < length; i++) {
                TroubleTypeStatistics troubleTypeStatistics = typeStatistics.get(i);
                ProblemStatistics problemStatistics = new ProblemStatistics();
                problemStatistics.setStatisticsId(NineteenUUIDUtils.uuid());
                problemStatistics.setValueDate(startDate);
                problemStatistics.setStatisticsType(ProblemStatisticsEnum.TROUBLE_TYPE.getTypeCode());
                String secondaryType = troubleTypeStatistics.getSecondaryType();
                problemStatistics.setValueType(secondaryType);
                Integer totalNumber = troubleTypeStatistics.getTotalNumber();
                problemStatistics.setValueNumber(totalNumber);
                problemStatistics.setManageId(NineteenUUIDUtils.uuid());
                String secondaryName = TroubleSortUtil.getSecondaryNameForCode(
                        troubleTypeStatistics.getTopType(), secondaryType);
                problemStatistics.setSourceName(secondaryName);
                problemStatistics.setSourceCode(secondaryType);
                String cause = ProblemConstant.TOP_CAUSE.replace(ProblemConstant.TOP_REPLACE, String.valueOf(i + 1));
                cause = cause.replace(ProblemConstant.NAME_REPLACE, secondaryName);
                cause = cause.replace(ProblemConstant.NUM_REPLACE, String.valueOf(totalNumber));
                problemStatistics.setCause(cause);
                statisticsList.add(problemStatistics);
                Long time = System.currentTimeMillis();
                ManageActivity manageActivity = manageService.createManageActivity(problemStatistics.getManageId(), secondaryName, secondaryType,
                        time, null, cause, ManageStatusEnum.UN_REDUCE, ManageSourceEnum.PROBLEM, startDate, ManageSourceTypeEnum.PROBLEM_TROUBLE_TYPE);
                manageActivityList.add(manageActivity);
            }
        }
        return statisticsList;
    }

    /**
     *  故障二级分类TOP
     * @param manageActivityList 运维管理活动
     * @param startDate 当前月份
     * @param monthMap 当前月份前一个月
     * @param yearMap 当前月份去年同月
     * @param typeStatistics 故障二级分类TOP
     */
    private void troubleTypeTop(List<ManageActivity> manageActivityList, Long startDate,
                                Map<String, Map<String, TroubleTypeStatistics>> monthMap,
                                Map<String, Map<String, TroubleTypeStatistics>> yearMap,
                                List<TroubleTypeStatistics> typeStatistics) {
        for (TroubleTypeStatistics typeStatistic : typeStatistics) {
            String topType = typeStatistic.getTopType();
            String secondaryType = typeStatistic.getSecondaryType();
            TroubleTypeStatistics monthStatistics = new TroubleTypeStatistics();
            if (monthMap.containsKey(topType)) {
                Map<String, TroubleTypeStatistics> typeStatisticsMap = monthMap.get(topType);
                if (typeStatisticsMap.containsKey(secondaryType)) {
                    monthStatistics = typeStatisticsMap.get(secondaryType);
                }
            }
            TroubleTypeStatistics yearStatistics = new TroubleTypeStatistics();
            if (yearMap.containsKey(topType)) {
                Map<String, TroubleTypeStatistics> typeStatisticsMap = yearMap.get(topType);
                if (typeStatisticsMap.containsKey(secondaryType)) {
                    yearStatistics = typeStatisticsMap.get(secondaryType);
                }
            }
            typeStatistic.setMonthPercentage(calculatePercentage(monthStatistics.getTotalNumber(),
                    typeStatistic.getTotalNumber()));
            typeStatistic.setYearPercentage(calculatePercentage(yearStatistics.getTotalNumber(),
                    typeStatistic.getTotalNumber()));
            typeStatistic.setVeryLowMonth(calculatePercentage(monthStatistics.getVeryLowNumber(),
                    typeStatistic.getVeryLowNumber()));
            typeStatistic.setVeryLowYear(calculatePercentage(yearStatistics.getVeryLowNumber(),
                    typeStatistic.getVeryLowNumber()));
            typeStatistic.setLowMonth(calculatePercentage(monthStatistics.getLowNumber(),
                    typeStatistic.getLowNumber()));
            typeStatistic.setLowYear(calculatePercentage(yearStatistics.getLowNumber(),
                    typeStatistic.getLowNumber()));
            typeStatistic.setModerateMonth(calculatePercentage(monthStatistics.getModerateNumber(),
                    typeStatistic.getModerateNumber()));
            typeStatistic.setModerateYear(calculatePercentage(yearStatistics.getModerateNumber(),
                    typeStatistic.getModerateNumber()));
            typeStatistic.setHighMonth(calculatePercentage(monthStatistics.getHighNumber(),
                    typeStatistic.getHighNumber()));
            typeStatistic.setHighYear(calculatePercentage(yearStatistics.getHighNumber(),
                    typeStatistic.getHighNumber()));
            typeStatistic.setVeryHighMonth(calculatePercentage(monthStatistics.getVeryHighNumber(),
                    typeStatistic.getVeryHighNumber()));
            typeStatistic.setVeryHighYear(calculatePercentage(yearStatistics.getVeryHighNumber(),
                    typeStatistic.getVeryHighNumber()));
            typeStatistic.setManageId(NineteenUUIDUtils.uuid());
            String secondaryName = TroubleSortUtil.getSecondaryNameForCode(topType, secondaryType);
            String topName = TroubleSortUtil.getNameForCode(topType);
            typeStatistic.setSourceName(secondaryName);
            typeStatistic.setSourceCode(secondaryType);
            String cause = ProblemConstant.TYPE_TOP_CAUSE.replace(
                    ProblemConstant.TOP_REPLACE, typeStatistic.getCause());
            cause = cause.replace(ProblemConstant.NAME_REPLACE, secondaryName);
            cause = cause.replace(ProblemConstant.YEAR_REPLACE, topName);
            cause = cause.replace(ProblemConstant.NUM_REPLACE, String.valueOf(typeStatistic.getTotalNumber()));
            typeStatistic.setCause(cause);
            typeStatistic.setValueYear(getYear(startDate));
            typeStatistic.setValueMonth(getMonth(startDate));
            Long time = System.currentTimeMillis();
            ManageActivity manageActivity = manageService.createManageActivity(typeStatistic.getManageId(), secondaryName, secondaryType,
                    time, null, cause, ManageStatusEnum.UN_REDUCE, ManageSourceEnum.PROBLEM, startDate, ManageSourceTypeEnum.PROBLEM_TROUBLE_TYPE_DETAIL);
            manageActivityList.add(manageActivity);
        }
    }

    /**
     * 统计故障级别数据
     * @param manageActivityList 运维管理活动
     * @param startDate 最新数据月份开始时间
     * @param lastDate 最新数据月份去年同月开始时间
     * @param monthDate 最新数据月份前一个月开始时间
     * @param ticketMap 故障单按年分组
     * @param statisticsList 问题分析统计数据
     * @param statisticsMap 故障二级分类统计结果
     * @param monthMap 故障二级分类统计结果 环比
     * @param yearMap 故障二级分类统计结果 同比
     * @return boolean
     */
    private boolean produceTroubleLevel(List<ManageActivity> manageActivityList, Long startDate, Long lastDate,
                                        Long monthDate, Map<Long, List<TroubleTicket>> ticketMap,
                                        List<ProblemStatistics> statisticsList,
                                        Map<String, Map<String, TroubleTypeStatistics>> statisticsMap,
                                        Map<String, Map<String, TroubleTypeStatistics>> monthMap,
                                        Map<String, Map<String, TroubleTypeStatistics>> yearMap) {
        //当前数据月份故障级别
        TroubleTrend current = new TroubleTrend();
        //当前数据前一个月月份故障级别
        TroubleTrend month = new TroubleTrend();
        //当前数据月份去年同月故障级别
        TroubleTrend year = new TroubleTrend();
        countTrouble(startDate, ticketMap, current, statisticsMap, true);
        boolean have = statisticsMap.size() > 0;
        countTrouble(monthDate, ticketMap, month, monthMap, have);
        countTrouble(lastDate, ticketMap, year, yearMap, have);
        for (TroubleLevelEnum troubleLevelEnum : TroubleLevelEnum.values()) {
            ProblemStatistics problemStatistics = new ProblemStatistics();
            problemStatistics.setStatisticsId(NineteenUUIDUtils.uuid());
            problemStatistics.setValueDate(startDate);
            problemStatistics.setStatisticsType(ProblemStatisticsEnum.OVERVIEW.getTypeCode());
            String levelCode = troubleLevelEnum.getLevelCode();
            problemStatistics.setValueType(levelCode);
            calculateProblemStatistics(problemStatistics, current, month, year);
            problemStatistics.setManageId(NineteenUUIDUtils.uuid());
            String manageName = troubleLevelEnum.getManageName();
            problemStatistics.setSourceName(manageName);
            problemStatistics.setSourceCode(levelCode);
            String causeStr = ProblemConstant.TROUBLE_LEVEL_MANAGE.replace(ProblemConstant.NAME_REPLACE,
                    troubleLevelEnum.getLevelName());
            causeStr = causeStr.replace(ProblemConstant.NUM_REPLACE, String.valueOf(problemStatistics.getValueNumber()));
            problemStatistics.setCause(causeStr);
            statisticsList.add(problemStatistics);
            Long time = System.currentTimeMillis();
            if (problemStatistics.getValueNumber() != 0) {
                ManageActivity manageActivity = manageService.createManageActivity(problemStatistics.getManageId(), manageName, levelCode,
                        time, null, causeStr, ManageStatusEnum.UN_REDUCE, ManageSourceEnum.PROBLEM, startDate, ManageSourceTypeEnum.PROBLEM_OVERVIEW);
                manageActivityList.add(manageActivity);
            }
        }
        return have;
    }

    /**
     * 统计故障单
     * @param problemSelect 时间范围查询条件
     * @param trendMap  故障分类趋势
     * @param ticketMap 故障单按年分组
     */
    private void statisticsTroubleTicket(ProblemSelect problemSelect, Map<Long, Map<String, TroubleTrend>> trendMap,
                                         Map<Long, List<TroubleTicket>> ticketMap) {
        //故障单数据
        List<TroubleTicket> troubleTicketList = troubleTicketDao.selectTroubleTicketForTime(problemSelect);
        for (TroubleTicket troubleTicket : troubleTicketList) {
            Long time = DateFormatUtils.getMonthStartForTime(troubleTicket.getTroubleTime());
            if (ticketMap.containsKey(time)) {
                List<TroubleTicket> ticketList = ticketMap.get(time);
                ticketList.add(troubleTicket);
            } else {
                List<TroubleTicket> ticketList = new ArrayList<>();
                ticketList.add(troubleTicket);
                ticketMap.put(time, ticketList);
            }
            if (trendMap.containsKey(time)) {
                Map<String, TroubleTrend> map = trendMap.get(time);
                String topType = troubleTicket.getTopType();
                String troubleLevel = troubleTicket.getTroubleLevel();
                if (map.containsKey(topType)) {
                    TroubleTrend troubleTrend = map.get(topType);
                    countTroubleLevel(troubleLevel, troubleTrend);
                }
            }
        }
    }

    /**
     * 组装趋势分析曲线
     * @param startDate 数据月份
     * @param monthOfYear 趋势时间范围
     * @param trendMap 故障分类趋势
     * @param offTrendMap 断水断电趋势
     */
    private void createTrend(Long startDate, List<Long> monthOfYear, Map<Long, Map<String, TroubleTrend>> trendMap,
                             Map<Long, Map<String, FlashOffTrend>> offTrendMap) {
        //组装趋势分析曲线
        for (Long month : monthOfYear) {
            Map<String, TroubleTrend> map = new HashMap<>(16);
            getTroubleTrend(startDate, month, map, TroubleSortEnum.CABINET.getSortCode());
            getTroubleTrend(startDate, month, map, TroubleSortEnum.MONITORING_COLLECTION.getSortCode());
            getTroubleTrend(startDate, month, map, TroubleSortEnum.POWER_DISTRIBUTION.getSortCode());
            getTroubleTrend(startDate, month, map, TroubleSortEnum.ENVIRONMENTAL_AIR_CONDITIONER.getSortCode());
            getTroubleTrend(startDate, month, map, TroubleSortEnum.SAFETY_PRECAUTION.getSortCode());
            trendMap.put(month, map);
            Map<String, FlashOffTrend> flashOffTrendMap = new HashMap<>(16);
            getFlashOffTrend(startDate, month, flashOffTrendMap, FlashOffTypeEnum.WATER_CUT.getTypeCode());
            getFlashOffTrend(startDate, month, flashOffTrendMap, FlashOffTypeEnum.POWER_CUT.getTypeCode());
            getFlashOffTrend(startDate, month, flashOffTrendMap, FlashOffTypeEnum.POWER_FLASH_OFF.getTypeCode());
            offTrendMap.put(month, flashOffTrendMap);
        }
    }

    /**
     * 获取最新数据时间
     * @return Long 最新数据时间
     */
    private Long getNearDate() {
        //故障最新时间
        Long ticketDate = troubleTicketDao.queryLatestTroubleTicketDate();
        //断水断电最新时间
        Long offDate = flashOffDao.queryLatestFlashOffDate();
        Long nearDate;
        boolean ticket = ticketDate == null;
        boolean off = offDate == null;
        if (ticket && off) {
            throw new BizException(ProblemResultCode.PROBLEM_DATA_EMPTY, ProblemResultMsg.PROBLEM_DATA_EMPTY);
        }
        if (ticket) {
            nearDate = offDate;
        } else if (off) {
            nearDate = ticketDate;
        } else {
            nearDate = Math.max(ticketDate, offDate);
        }
        //校验分析
        paramManageService.checkAnalyze(ManageSourceEnum.PROBLEM, nearDate);
        return nearDate;
    }

    /**
     * 创建闪断趋势实体
     * @param startDate 分析日期
     * @param month 数据日期
     * @param flashOffTrendMap  趋势
     * @param typeCode 类型
     */
    private void getFlashOffTrend(Long startDate, Long month,
                                  Map<String, FlashOffTrend> flashOffTrendMap, String typeCode) {
        FlashOffTrend flashOffTrend = new FlashOffTrend();
        flashOffTrend.setTrendId(NineteenUUIDUtils.uuid());
        flashOffTrend.setTrendDate(month);
        flashOffTrend.setReportDate(startDate);
        flashOffTrend.setOffType(typeCode);
        flashOffTrendMap.put(typeCode, flashOffTrend);
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
        int round = (int) Math.round(a + b * month);
        if (round < 0) {
            round = 0;
        }
        return round;
    }

    /**
     * 计算同比环比
     * @param problemStatistics 统计
     * @param current 本月
     * @param month 上月
     * @param year 去年同月
     */
    private void calculateProblemStatistics(ProblemStatistics problemStatistics, TroubleTrend current,
                                            TroubleTrend month, TroubleTrend year) {
        String troubleLevel = problemStatistics.getValueType();
        Integer currentNum;
        Integer monthNum;
        Integer yearNum;
        if (TroubleLevelEnum.VERY_LOW.getLevelCode().equals(troubleLevel)) {
            currentNum = current.getVeryLowNumber();
            monthNum = month.getVeryLowNumber();
            yearNum = year.getVeryLowNumber();
        } else if (TroubleLevelEnum.LOW.getLevelCode().equals(troubleLevel)) {
            currentNum = current.getLowNumber();
            monthNum = month.getLowNumber();
            yearNum = year.getLowNumber();
        } else if (TroubleLevelEnum.MODERATE.getLevelCode().equals(troubleLevel)) {
            currentNum = current.getModerateNumber();
            monthNum = month.getModerateNumber();
            yearNum = year.getModerateNumber();
        } else if (TroubleLevelEnum.HIGH.getLevelCode().equals(troubleLevel)) {
            currentNum = current.getHighNumber();
            monthNum = month.getHighNumber();
            yearNum = year.getHighNumber();
        } else {
            currentNum = current.getVeryHighNumber();
            monthNum = month.getVeryHighNumber();
            yearNum = year.getVeryHighNumber();
        }
        problemStatistics.setValueNumber(currentNum);
        String monthPercentage = calculatePercentage(monthNum, currentNum);
        problemStatistics.setMonthPercentage(monthPercentage);
        String yearPercentage = calculatePercentage(yearNum, currentNum);
        problemStatistics.setYearPercentage(yearPercentage);
        Long valueDate = problemStatistics.getValueDate();
        String yearStr = getYear(valueDate);
        String monthStr = getMonth(valueDate);
        String msg = getStringMsg(ProblemConstant.REPORT_MSG, yearStr, yearPercentage,
                ProblemConstant.YEAR_NAME_REPLACE, ProblemConstant.YEAR_REPLACE);
        msg = getStringMsg(msg, monthStr, monthPercentage,
                ProblemConstant.MONTH_NAME_REPLACE, ProblemConstant.MONTH_REPLACE);
        problemStatistics.setInstruction(msg);
    }

    /**
     * 去年
     * @param time 时间
     * @return String
     */
    private String getYear(Long time) {
        Long last = DateFormatUtils.getTimeLastYear(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(last);
    }
    /**
     * 上月
     * @param time 时间
     * @return String
     */
    private String getMonth(Long time) {
        Long last = DateFormatUtils.getTimeLastMonth(time);
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(last);
    }

    /**
     *
     * @param msg
     * @param time
     * @param percentage
     * @param name
     * @param value
     * @return
     */
    private String getStringMsg(String msg, String time, String percentage, String name, String value) {
        String str = msg.replace(name, time);
        if (percentage.contains(ProblemConstant.CONNECT_REPLACE)) {
            percentage = percentage.replace(ProblemConstant.CONNECT_REPLACE, "");
            str = str.replace(value, ProblemConstant.REDUCE_REPLACE + percentage);
        } else {
            str = str.replace(value, ProblemConstant.ADD_REPLACE + percentage);
        }
        return str;
    }

    /**
     * 计算故障单月份数据
     * @param startDate 分析月份
     * @param ticketMap 故障单
     * @param troubleTrend 故障总览
     * @param statisticsMap 二级分类
     * @param have 是否有当月数据
     */
    private void countTrouble(Long startDate, Map<Long, List<TroubleTicket>> ticketMap, TroubleTrend troubleTrend,
                              Map<String, Map<String, TroubleTypeStatistics>> statisticsMap, boolean have) {
        if (ticketMap.containsKey(startDate)) {
            List<TroubleTicket> ticketList = ticketMap.get(startDate);
            if (have) {
                countTroubleStatistics(startDate, troubleTrend, ticketList, statisticsMap);
            } else {
                countTroubleStatistics(troubleTrend, ticketList);
            }
        }
    }

    /**
     * 分析对应月份数据
     * @param troubleTrend 故障总览
     * @param ticketList 故障单
     */
    private void countTroubleStatistics(TroubleTrend troubleTrend, List<TroubleTicket> ticketList) {
        for (TroubleTicket troubleTicket : ticketList) {
            String level = troubleTicket.getTroubleLevel();
            countTroubleLevel(level, troubleTrend);
        }
    }

    /**
     * 分析对应月份数据
     * @param startDate 分析月份
     * @param troubleTrend 故障总览
     * @param ticketList 故障单
     * @param statisticsMap  二级分类
     */
    private void countTroubleStatistics(Long startDate, TroubleTrend troubleTrend, List<TroubleTicket> ticketList,
                                        Map<String, Map<String, TroubleTypeStatistics>> statisticsMap) {
        for (TroubleTicket troubleTicket : ticketList) {
            String topType = troubleTicket.getTopType();
            String secondaryType = troubleTicket.getSecondaryType();
            String level = troubleTicket.getTroubleLevel();
            if (statisticsMap.containsKey(topType)) {
                Map<String, TroubleTypeStatistics> typeStatisticsMap = statisticsMap.get(topType);
                if (typeStatisticsMap.containsKey(secondaryType)) {
                    TroubleTypeStatistics typeStatistics = typeStatisticsMap.get(secondaryType);
                    countTroubleType(troubleTrend, level, typeStatistics);
                } else {
                    createTypeStatistics(troubleTrend, startDate, topType,
                            secondaryType, level, typeStatisticsMap);
                }
            } else {
                Map<String, TroubleTypeStatistics> typeStatisticsMap = new HashMap<>(64);
                createTypeStatistics(troubleTrend, startDate, topType, secondaryType, level, typeStatisticsMap);
                statisticsMap.put(topType, typeStatisticsMap);
            }
        }
    }

    /**
     * 创建新二级分类统计实体
     * @param startDate 分析日期
     * @param topType 一级分类
     * @param secondaryType 二级分类
     * @param level 级别
     * @param typeStatisticsMap 二级分类
     */
    private void createTypeStatistics(TroubleTrend troubleTrend, Long startDate, String topType, String secondaryType,
                                      String level, Map<String, TroubleTypeStatistics> typeStatisticsMap) {
        TroubleTypeStatistics typeStatistics = new TroubleTypeStatistics();
        typeStatistics.setStatisticsId(NineteenUUIDUtils.uuid());
        typeStatistics.setTopType(topType);
        typeStatistics.setSecondaryType(secondaryType);
        typeStatistics.setStatisticsDate(startDate);
        countTroubleType(troubleTrend, level, typeStatistics);
        typeStatisticsMap.put(secondaryType, typeStatistics);
    }

    /**
     * 计算二级分类不同级别数量
     * @param level 级别
     * @param typeStatistics 二级分类统计
     */
    private void countTroubleType(TroubleTrend troubleTrend, String level, TroubleTypeStatistics typeStatistics) {
        typeStatistics.setTotalNumber(typeStatistics.getTotalNumber() + 1);
        if (TroubleLevelEnum.VERY_HIGH.getLevelCode().equals(level)) {
            typeStatistics.setVeryHighNumber(typeStatistics.getVeryHighNumber() + 1);
            troubleTrend.setVeryHighNumber(troubleTrend.getVeryHighNumber() + 1);
        } else if (TroubleLevelEnum.LOW.getLevelCode().equals(level)) {
            typeStatistics.setLowNumber(typeStatistics.getLowNumber() + 1);
            troubleTrend.setLowNumber(troubleTrend.getLowNumber() + 1);
        } else if (TroubleLevelEnum.MODERATE.getLevelCode().equals(level)) {
            typeStatistics.setModerateNumber(typeStatistics.getModerateNumber() + 1);
            troubleTrend.setModerateNumber(troubleTrend.getModerateNumber() + 1);
        } else if (TroubleLevelEnum.HIGH.getLevelCode().equals(level)) {
            typeStatistics.setHighNumber(typeStatistics.getHighNumber() + 1);
            troubleTrend.setHighNumber(troubleTrend.getHighNumber() + 1);
        } else {
            typeStatistics.setVeryLowNumber(typeStatistics.getVeryLowNumber() + 1);
            troubleTrend.setVeryLowNumber(troubleTrend.getVeryLowNumber() + 1);
        }
    }

    /**
     * 创建趋势曲线
     * @param startDate 分析月份
     * @param time 趋势月份
     * @param map  趋势曲线
     * @param topType 一级分类
     */
    private void getTroubleTrend(Long startDate, Long time, Map<String, TroubleTrend> map, String topType) {
        TroubleTrend troubleTrend = new TroubleTrend();
        troubleTrend.setTrendId(NineteenUUIDUtils.uuid());
        troubleTrend.setTrendDate(time);
        troubleTrend.setTopType(topType);
        troubleTrend.setReportDate(startDate);
        map.put(topType, troubleTrend);
    }

    /**
     * 计算故障级别数量
     * @param troubleLevel 故障级别
     * @param troubleTrend 故障分类
     */
    private void countTroubleLevel(String troubleLevel, TroubleTrend troubleTrend) {
        if (TroubleLevelEnum.VERY_LOW.getLevelCode().equals(troubleLevel)) {
            troubleTrend.setVeryLowNumber(troubleTrend.getVeryLowNumber() + 1);
        } else if (TroubleLevelEnum.LOW.getLevelCode().equals(troubleLevel)) {
            troubleTrend.setLowNumber(troubleTrend.getLowNumber() + 1);
        } else if (TroubleLevelEnum.MODERATE.getLevelCode().equals(troubleLevel)) {
            troubleTrend.setModerateNumber(troubleTrend.getModerateNumber() + 1);
        } else if (TroubleLevelEnum.HIGH.getLevelCode().equals(troubleLevel)) {
            troubleTrend.setHighNumber(troubleTrend.getHighNumber() + 1);
        } else {
            troubleTrend.setVeryHighNumber(troubleTrend.getVeryHighNumber() + 1);
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
            throw new BizException(ProblemResultCode.FILE_TYPE_ERROR,
                    ProblemResultMsg.FILE_TYPE_ERROR);
        }
        List<ExcelReadBean> excelReadBeanList = new ArrayList<>();
        for (List<ExcelReadBean> excelReadBeans : map.values()) {
            excelReadBeanList.addAll(excelReadBeans);
        }
        //是否有数据
        if (CollectionUtils.isEmpty(excelReadBeanList)) {
            throw new BizException(ProblemResultCode.FILE_EMPTY,
                    ProblemResultMsg.FILE_EMPTY);
        }
        return excelReadBeanList;
    }
}
