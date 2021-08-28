package com.taiping.biz.budget.service.impl;

import com.google.common.collect.Lists;
import com.taiping.biz.budget.dao.IBudgetDao;
import com.taiping.biz.budget.dao.IBudgetPurchaseDao;
import com.taiping.biz.budget.dto.*;
import com.taiping.biz.budget.service.IBudgetPurchaseService;
import com.taiping.biz.budget.service.IBudgetService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.maintenance.MaintenancePlanConstant;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.budget.*;
import com.taiping.entity.maintenanceplan.MaintenancePlanAnalysisReport;
import com.taiping.entity.maintenanceplan.MaintenancePlanStatistics;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.LocalDateUtil;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements IBudgetService {
    @Autowired
    private IBudgetDao iBudgetDao;
    @Autowired
    private IBudgetPurchaseDao iBudgetPurchaseDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ManageActivityService manageActivityService;
    @Autowired
    private ParamManageService paramManageService;
    @Autowired
    private IBudgetPurchaseService purchaseService;

    /**
     * 新增预算
     *
     * @param dto
     * @return
     */
    @Override
    public Result add(BudgetDto dto) {
        BudgetDto bean = iBudgetDao.selectByCode(dto.getCode());

        if (bean != null) {
            return ResultUtils.warn(ResultCode.FAIL, "预算代码[" + dto.getCode() + "]已存在，请重新输入预算代码");
        }

        if (StringUtils.isBlank(dto.getBudgetYear())) {
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            dto.setBudgetYear(year);
        }

        dto.setTid(NineteenUUIDUtils.uuid());
        iBudgetDao.add(dto);

        return ResultUtils.success();
    }

    /**
     * 更新预算
     *
     * @param entity
     */
    @Override
    public Result updateBudget(TBudget entity) {
        iBudgetDao.updateBudget(entity);
        return ResultUtils.success();
    }

    /**
     * 根据年份获取预算
     *
     * @param year
     * @return
     */
    @Override
    public Result<List<BudgetDto>> findByYear(String year) {
        return ResultUtils.success(iBudgetDao.findByYear(year));
    }

    /**
     * 生成预算表格
     *
     * @param vo
     * @return
     */
    @Override
    public Result<BudgetTableDto> getBudgetTable(BudgetFindDto vo) {

        BudgetTableDto dto = new BudgetTableDto();
        List<BudgetTableRowDto> budgetList = iBudgetDao.findByYearTable(vo);
        List<BudgetPurchaseDto> purchaseList = iBudgetPurchaseDao.findPurchaseByYear(vo.getYear());

        dto.setTitles(purchaseList);
        dto.setRows(budgetList);

        //生成表格
        for (BudgetTableRowDto row : budgetList) {

            List<BudgetTableRowItemDto> cols = new ArrayList<>();

            for (BudgetPurchaseDto col : purchaseList) {
                if (col.getBudgetId().equals(row.getTid())) {
                    BudgetTableRowItemDto item = new BudgetTableRowItemDto();
                    item.setBudgetAmount(col.getBudgetAmount());
                    item.setDealAmount(col.getDealAmount());
                    item.setProName(col.getProName());
                    cols.add(item);
                } else {
                    cols.add(null);
                }

            }

            row.setValues(cols);
        }

        return ResultUtils.success(dto);
    }

    /**
     * 获取预算年份列表
     *
     * @return
     */
    @Override
    public Result<List<String>> getYearList() {
        List<String> list = iBudgetDao.getYearList();
        return ResultUtils.success(list);
    }

    /**
     * 获取年度汇总
     *
     * @return
     */
    @Override
    public Result<BudgetTotalDto> getBudgetTotal() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);

        return ResultUtils.success(iBudgetDao.getBudgetTotal(String.valueOf(year)));
    }

    /**
     * 预算采购分析
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result budgetPurchaseAnalysis() {
        paramManageService.checkAnalyze(ManageSourceEnum.BUDGET,System.currentTimeMillis());
        List<ManageActivity> activityList =  budgetAnalysis();
        activityList.addAll(purchaseService.purchaseAnalysis());
        paramManageService.updateParamManage(ManageSourceEnum.BUDGET,ParamTypeEnum.ANALYZE,System.currentTimeMillis());
        if (!ObjectUtils.isEmpty(activityList)) {
            manageActivityService.insertManageActivityNoEquals(ManageSourceEnum.BUDGET, activityList);
            return ResultUtils.success(1, "有产生运维管理活动，跳转到运维管理活动功能");
        }
        return ResultUtils.success(ResultCode.SUCCESS, "未产生运维管理活动，保持当前页面");
    }

    /**
     * 保存预算采购分析数据
     *
     * @return 保存结果
     */
    @Override
    public Result saveBudgetPurchaseAnalysisReportData() {
        //校验预览
        paramManageService.checkPreview(ManageSourceEnum.BUDGET);
        saveBudgetPurchaseAnalysisData();
        paramManageService.updateParamManage(ManageSourceEnum.BUDGET,ParamTypeEnum.PREVIEW,System.currentTimeMillis());
        return ResultUtils.success();
    }

    /**
     * 根据月份获取预算采购分析报告数据
     *
     * @param monthTime
     * @return 预算采购分析报告数据
     */
    @Override
    public BudgetPurchaseReportDto getBudgetPurchaseReportData(Long monthTime) {
        BudgetPurchaseReportDto data = new BudgetPurchaseReportDto();
        List<BudgetAnalysisReport> budgetAnalysisData = iBudgetDao.getBudgetAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        List<BudgetAnalysisCurve> curveData = iBudgetDao.getBudgetCurveDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        List<PurchaseAnalysisReport> purchaseAnalysisData = iBudgetPurchaseDao.getPurchaseAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(monthTime));
        data.setBudgetAnalysisData(budgetAnalysisData);
        data.setCurveData(curveData);
        data.setPurchaseAnalysisData(purchaseAnalysisData);
        return data;
    }

    /**
     * 预算分析
     *
     * @return 运维管理活动列表
     */
    private List<ManageActivity> budgetAnalysis() {
        List<SystemSetting> list = systemService.querySystemValueByCode("6010");
        Double s10 = null;
        Double s20 = null;
        Double m30 = null;
        Double m40 = null;

        for (SystemSetting item : list) {
            if (item.getCode().equals("601010")) {
                s10 = (Double) item.getValue();
            } else if (item.getCode().equals("601020")) {
                s20 = (Double) item.getValue();
            } else if (item.getCode().equals("601030")) {
                m30 = (Double) item.getValue();
            } else if (item.getCode().equals("601040")) {
                m40 = (Double) item.getValue();
            }
        }

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;

        List<ManageActivity> activityList = new ArrayList<>();
        List<BudgetStatistics> statisticsList = Lists.newArrayList();
         //汇总预算
        if (m40 != null && month < m40.intValue()) {
            Result<BudgetTotalDto> rs = getBudgetTotal();
            BudgetTotalDto total = (BudgetTotalDto) rs.getData();

            double ratio = (total.getRatio() * 100);
            BudgetStatistics statistics = new BudgetStatistics();
            statistics.setId(NineteenUUIDUtils.uuid());
            statistics.setStatisticsObject(year + "");
            statistics.setIsYearBudget(0);
            statistics.setRatio(ObjectUtils.isEmpty(ratio) ? 0:ratio);
            statistics.setStatisticsTime(DateFormatUtils.getThisMonthTime());
            statisticsList.add(statistics);
            if (ratio < m30.doubleValue()) {
                ManageActivity bean = manageActivityService.createManageActivity(
                        NineteenUUIDUtils.uuid(),
                        (year + "年度预算采购分析"),
                        (year + ""),
                        System.currentTimeMillis(),
                        (year + "年度采购不达标"),
                        (year + "年度, 总预算：" + total.getBudgetTotal() + ", 总采购量：" + total.getPurchaseTotal() + ", 执行比例：" + ratio + "%，不达标！"),
                        ManageStatusEnum.UN_REDUCE,
                        ManageSourceEnum.BUDGET,
                        System.currentTimeMillis());
                bean.setSourceType(ManageSourceTypeEnum.YEAR_BUDGET.getCode());
                activityList.add(bean);
            }
        }

        //单项预算
        if (s20 != null && month < s20.intValue()) {
            List<BudgetAnalysisDto> list2 = iBudgetDao.getBudgetAnalysis(String.valueOf(year));

            for (BudgetAnalysisDto item : list2) {

                double ratio = (item.getExecTotal() / item.getAmount()) * 100;
                BudgetStatistics statistics = new BudgetStatistics();
                statistics.setId(NineteenUUIDUtils.uuid());
                statistics.setStatisticsObject(item.getCode());
                statistics.setIsYearBudget(1);
                statistics.setRatio(ObjectUtils.isEmpty(ratio) ? 0:ratio);
                statistics.setStatisticsTime(DateFormatUtils.getThisMonthTime());
                statisticsList.add(statistics);
                if (ratio < s10.doubleValue()) {
                    ManageActivity bean = manageActivityService.createManageActivity(
                            NineteenUUIDUtils.uuid(),
                            (year + "[" + item.getName() + "]预算采购分析"),
                            item.getCode(),
                            System.currentTimeMillis(),
                            (year + "[" + item.getName() + "]采购不达标"),
                            (year + "[" + item.getName() + "]，预算：" + item.getAmount() + ", 采购量：" + item.getExecTotal() + ", 执行比例：" + ratio + "%，不达标！"),
                            ManageStatusEnum.UN_REDUCE,
                            ManageSourceEnum.BUDGET,
                            System.currentTimeMillis());
                    bean.setSourceType(ManageSourceTypeEnum.INDIVIDUAL_BUDGET.getCode());
                    activityList.add(bean);
                }
            }
        }
        List<BudgetStatistics> oldStatisticsList  = iBudgetDao.getBudgetStatisticsDataByMonth(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
        if(!ObjectUtils.isEmpty(oldStatisticsList)) {
            iBudgetDao.batchDeleteBudgetStatisticsData(oldStatisticsList);
        }
        if(!ObjectUtils.isEmpty(statisticsList)) {
            iBudgetDao.batchInsertBudgetStatisticsData(statisticsList);
        }
        return activityList;
    }

    /**
     * 保存预算分析数据
     */
    private void saveBudgetPurchaseAnalysisData() {
        //查询运维管理活动
        List<ManageActivity> manageActivityList =
                manageActivityService.queryManageActivityForMode(ManageSourceEnum.BUDGET);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        //查询本年度所有预算项
        List<BudgetAnalysisDto> list2 = iBudgetDao.getBudgetAnalysis(String.valueOf(year));
        List<BudgetPurchaseTableColDto> cols = iBudgetPurchaseDao.getPurchaseTable(String.valueOf(year));
        List<BudgetAnalysisReport> budgetAnalysisReports = Lists.newArrayList();
        List<PurchaseAnalysisReport> purchaseAnalysisReports = Lists.newArrayList();
        //生成年度总预算分析报告数据
        BudgetAnalysisReport  yearBudgetReport = new BudgetAnalysisReport();
        yearBudgetReport.setTid(NineteenUUIDUtils.uuid());
        yearBudgetReport.setReportTime(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
        yearBudgetReport.setAnalysisObject(String.valueOf(year));
        Result<BudgetTotalDto> rs = getBudgetTotal();
        BudgetTotalDto total = (BudgetTotalDto) rs.getData();
        yearBudgetReport.setBudgetAmount(total.getBudgetTotal());
        yearBudgetReport.setDealAmount(total.getPurchaseTotal());
        yearBudgetReport.setRatio(total.getRatio());
        yearBudgetReport.setSurplusAmount(total.getBudgetTotal() - total.getPurchaseTotal());
        yearBudgetReport.setIsYearBudget(0);
        for (ManageActivity activity: manageActivityList) {
            if (activity.getCreateTime() > DateFormatUtils.getMonthStartForTime(date.getTimeInMillis())
                    && activity.getCreateTime() < DateFormatUtils.getMonthEndForTime(date.getTimeInMillis())) {
                if (ManageSourceTypeEnum.YEAR_BUDGET.getCode().equals(activity.getSourceType())) {
                    yearBudgetReport.setManageId(activity.getManageId());
                    yearBudgetReport.setActivityType(activity.getActivityType());
                    yearBudgetReport.setSolveInstruction(activity.getSolveInstruction());
                }else if (ManageSourceTypeEnum.INDIVIDUAL_BUDGET.getCode().equals(activity.getSourceType())){
                    //生成分项预算分析报告数据
                    for (BudgetAnalysisDto budgetAnalysisDto: list2) {
                        if (activity.getSourceCode().equals(budgetAnalysisDto.getCode())) {
                            BudgetAnalysisReport report = new BudgetAnalysisReport();
                            report.setManageId(activity.getManageId());
                            report.setActivityType(activity.getActivityType());
                            report.setSolveInstruction(activity.getSolveInstruction());
                            report.setTid(NineteenUUIDUtils.uuid());
                            report.setReportTime(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
                            report.setAnalysisObject(budgetAnalysisDto.getName());
                            report.setBudgetAmount(budgetAnalysisDto.getAmount());
                            report.setDealAmount(budgetAnalysisDto.getExecTotal());
                            double ratio = (budgetAnalysisDto.getExecTotal() / budgetAnalysisDto.getAmount()) * 100;
                            report.setRatio(ratio);
                            report.setSurplusAmount(budgetAnalysisDto.getAmount() - budgetAnalysisDto.getExecTotal());
                            report.setIsYearBudget(1);
                            budgetAnalysisReports.add(report);
                        }
                    }
                } else {
                    for (BudgetPurchaseTableColDto dto: cols) {
                        if (activity.getSourceCode().equals(dto.getTid())) {
                            //生成采购项目分析报告数据
                            PurchaseAnalysisReport report = new PurchaseAnalysisReport();
                            report.setTid(NineteenUUIDUtils.uuid());
                            report.setPurchaseId(dto.getTid());
                            report.setPurchaseProName(dto.getProName());
                            report.setBudgetAmount(dto.getBudgetAmount());
                            report.setDealAmount(dto.getDealAmount());
                            report.setManageCause(activity.getCause());
                            report.setManageId(activity.getManageId());
                            report.setActivityType(activity.getActivityType());
                            report.setSolveInstruction(activity.getSolveInstruction());
                            report.setReportTime(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
                            purchaseAnalysisReports.add(report);
                        }
                    }
                }
            }
        }
        budgetAnalysisReports.add(yearBudgetReport);
        List<BudgetAnalysisCurve> analysisCurveList = createBudgetAnalysisCurveData();
        List<BudgetAnalysisReport> oldReportData = iBudgetDao.getBudgetAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
        if (!ObjectUtils.isEmpty(oldReportData)) {
            iBudgetDao.batchDeleteAnalysisData(oldReportData);
        }
        if (!ObjectUtils.isEmpty(budgetAnalysisReports)) {
            iBudgetDao.saveBudgetAnalysisReportData(budgetAnalysisReports);
        }
        List<BudgetAnalysisCurve> oldCurveData = iBudgetDao.getBudgetCurveDataByMonth(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
        if (!ObjectUtils.isEmpty(oldCurveData)) {
            iBudgetDao.batchDeleteBudgetCurveData(oldCurveData);
        }
        if (!ObjectUtils.isEmpty(analysisCurveList)) {
            iBudgetDao.batchInsertBudgetCurveData(analysisCurveList);
        }
        List<PurchaseAnalysisReport> oldPurchaseReportData = iBudgetPurchaseDao.getPurchaseAnalysisDataByMonth(DateFormatUtils.getMonthStartForTime(date.getTimeInMillis()));
        if (!ObjectUtils.isEmpty(oldPurchaseReportData)) {
            iBudgetPurchaseDao.batchDeletePurchaseAnalysisData(oldPurchaseReportData);
        }
        if (!ObjectUtils.isEmpty(purchaseAnalysisReports)) {
            iBudgetPurchaseDao.savePurchaseAnalysisReportData(purchaseAnalysisReports);
        }
    }

    /**
     * 生成预算分析曲线数据
     *
     * @return 预算分析曲线数据
     */
    private List<BudgetAnalysisCurve> createBudgetAnalysisCurveData() {
        List<BudgetAnalysisCurve> analysisCurveData = Lists.newArrayList();
        List<BudgetStatistics> statisticsList = iBudgetDao.getAllStatisticsData();
        Map<String,List<BudgetStatistics>> statisticsMap = statisticsList.stream().collect(Collectors.groupingBy(BudgetStatistics::getStatisticsObject));
        Calendar calendar = Calendar.getInstance();
        Long currentMonthStartTime = DateFormatUtils.getThisMonthTime();
        calendar.setTimeInMillis(currentMonthStartTime);
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.MONTH, 1);
        statisticsMap.forEach((object, budgetStatistics) -> {
            Map<Long,BudgetStatistics> statisticsTimeMap = budgetStatistics.stream().collect(Collectors.toMap(BudgetStatistics::getStatisticsTime, m -> m));
            for (Long longTime = calendar.getTimeInMillis();longTime <= currentMonthStartTime;longTime = DateFormatUtils.getNextMonthTime(longTime)){
                BudgetAnalysisCurve curve = new BudgetAnalysisCurve();
                curve.setId(NineteenUUIDUtils.uuid());
                curve.setStatisticsObject(object);
                curve.setStatisticsTime(longTime);
                curve.setReportTime(currentMonthStartTime);
                curve.setRatio(0.0);
                curve.setIsYearBudget(1);
                if(budgetStatistics.get(0).getIsYearBudget() == 0) {
                    curve.setIsYearBudget(0);
                }
                if(!ObjectUtils.isEmpty(statisticsTimeMap.get(longTime))) {
                    curve.setRatio(statisticsTimeMap.get(longTime).getRatio());
                }
                analysisCurveData.add(curve);
            }
        });
        analysisCurveData.addAll(getBudgetCurveForecastData(analysisCurveData));
        return analysisCurveData;
    }

    /**
     *预算分析趋势预测
     */
    private List<BudgetAnalysisCurve> getBudgetCurveForecastData(List<BudgetAnalysisCurve> analysisCurveData) {
        Map<String, List<BudgetAnalysisCurve>> objectMap = analysisCurveData.stream().collect(Collectors.groupingBy(BudgetAnalysisCurve::getStatisticsObject));
        List<BudgetAnalysisCurve> forecastList = Lists.newArrayList();
        objectMap.forEach((object, analysisCurves) -> {
            analysisCurves = analysisCurves.stream().sorted(Comparator.comparing(BudgetAnalysisCurve::getStatisticsTime)).collect(Collectors.toList());
            Long currentTime = System.currentTimeMillis();
            List<Double> ratios = analysisCurves.stream().map(BudgetAnalysisCurve::getRatio).collect(Collectors.toList());
            for (int month = 1; month < 4; month++) {
                Double nextRatio = getExpect(ratios, month, 0.6);
                BudgetAnalysisCurve curve = new BudgetAnalysisCurve();
                curve.setId(NineteenUUIDUtils.uuid());
                curve.setStatisticsObject(object);
                currentTime = DateFormatUtils.getNextMonthTime(currentTime);
                curve.setStatisticsTime(currentTime);
                curve.setReportTime(DateFormatUtils.getMonthStartForTime(System.currentTimeMillis()));
                curve.setRatio(nextRatio);
                curve.setIsYearBudget(1);
                if(analysisCurves.get(0).getIsYearBudget() == 0) {
                    curve.setIsYearBudget(0);
                }
                forecastList.add(curve);
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
    private static Double getExpect(List<Double> list, int month, Double modulus) {
        if (modulus >= 1 || modulus <= 0) {
            return null;
        }
        Double modulusLeft = 1 - modulus;
        Double lastIndex = list.get(0);
        Double lastSecIndex = list.get(0);
        for (Double data : list) {
            lastIndex = modulus * data + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        Double a = 2 * lastIndex - lastSecIndex;
        Double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        return a + b * month;
    }

}
