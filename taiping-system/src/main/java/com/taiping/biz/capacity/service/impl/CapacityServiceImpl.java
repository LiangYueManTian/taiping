package com.taiping.biz.capacity.service.impl;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.analyze.CapacityViewData;
import com.taiping.bean.capacity.analyze.parameter.CapacityRelatedViewListParameter;
import com.taiping.bean.capacity.analyze.vo.CapacityAnalyzeReportVo;
import com.taiping.bean.capacity.cabinet.dto.BaseCabinetDto;
import com.taiping.bean.capacity.cabinet.dto.CabinetDto;
import com.taiping.bean.capacity.cabling.dto.GenericCablingDto;
import com.taiping.biz.capacity.analyze.*;
import com.taiping.biz.capacity.service.CapacityService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.biz.capacity.service.cabinet.BaseCabinetService;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.biz.capacity.service.cabling.GenericCablingService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 容量逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:03
 */
@Service
@Slf4j
public class CapacityServiceImpl implements CapacityService {

    /**
     * 机柜基础信息逻辑层
     */
    @Autowired
    private BaseCabinetService baseCabinetService;

    /**
     * 综合布线信息逻辑层
     */
    @Autowired
    private GenericCablingService genericCablingService;

    /**
     * 机柜信息逻辑层
     */
    @Autowired
    private CabinetService cabinetService;

    /**
     * it能耗逻辑层
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * 系统逻辑层
     */
    @Autowired
    private SystemService systemService;

    /**
     * 楼层空间分析类
     */
    @Autowired
    private FloorSpaceAnalyze floorSpaceAnalyze;

    /**
     * 功能区分析类
     */
    @Autowired
    private FunctionTypeSpaceAnalyze functionTypeSpaceAnalyze;

    /**
     * 机柜空间容量分析
     */
    @Autowired
    private CabinetSpaceAnalyze cabinetSpaceAnalyze;


    /**
     * 模块电力容量分析
     */
    @Autowired
    private ModuleElectricAnalyze moduleElectricAnalyze;

    /**
     * ups电力容量分析
     */
    @Autowired
    private UpsElectricAnalyze upsElectricAnalyze;

    /**
     * 列头柜容量分析
     */
    @Autowired
    private CabinetColumnAnalyze cabinetColumnAnalyze;

    /**
     * pdu容量分析
     */
    @Autowired
    private PduAnalyze pduAnalyze;

    /**
     * 综合布线容量分析
     */
    @Autowired
    private GenericCablingAnalyze genericCablingAnalyze;



    /**
     * 容量阈值逻辑层
     */
    @Autowired
    private CapacityThresholdInfoService thresholdInfoService;

    /**
     * 容量阈值关联显示信息逻辑层
     */
    @Autowired
    private CapacityThresholdRelatedViewInfoService viewInfoService;


    /**
     * 运维管理活动逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 类型参数管理服务层
     */
    @Autowired
    private ParamManageService paramManageService;


    /**
     * 容量分析
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:49
     * @return 返回容量分析结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result capacityAnalysis() {

        //查询最新的机柜信息
        CabinetDto cabinetDto = cabinetService.queryTopDataByTime();
        boolean isAbleAnalysis = this.checkIsAbleAnalysis(cabinetDto);
        //显示提示信息
        if (!isAbleAnalysis) {
            return ResultUtils.warn(CapacityResultCode.CAPACITY_ANALYZE_ERROR, CapacityResultMsg.CAPACITY_ANALYZE_ERROR);
        }
        Integer analysisYear = cabinetDto.getYear();
        Integer analysisMonth = cabinetDto.getMonth();

        Long dateTime = 0L;
        dateTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, dateTime);
        //调用分析方法
        paramManageService.checkAnalyze(ManageSourceEnum.CAPACITY, dateTime);

        //查询阈值信息
        Map<String, List<SystemSetting>> searchCapacityThresholdList = this.searchCapacityThresholdList();

        //分析空间楼层容量数据
        floorSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析功能区容量数据
        functionTypeSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析机柜容量数据
        cabinetSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析模块
        moduleElectricAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析ups
        upsElectricAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析列头柜
        cabinetColumnAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析pdu
        pduAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析综合布线类型
        genericCablingAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //分析数据完成后调用修改状态的方法
        paramManageService.updateParamManage(ManageSourceEnum.CAPACITY, ParamTypeEnum.ANALYZE, dateTime);
        return ResultUtils.success(ResultCode.SUCCESS, CapacityResultMsg.CAPACITY_ANALYZE_OK);
    }


    /**
     * 保存预览数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return 返回保存预览结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result savePreviewData() {
        //查询最新的机柜信息数据
        CabinetDto cabinetDto = cabinetService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(cabinetDto)) {
            Integer year = cabinetDto.getYear();
            Integer month = cabinetDto.getMonth();

            //查询是否能够预览数据
            paramManageService.checkPreview(ManageSourceEnum.CAPACITY);

            //查询数据信息
            CapacityThresholdInfo thresholdInfo = new CapacityThresholdInfo();
            thresholdInfo.setYear(year);
            thresholdInfo.setMonth(month);
            //查询统计当月的阈值信息
            List<CapacityThresholdInfo> capacityThresholdInfoList = thresholdInfoService.queryThresholdInfoList(thresholdInfo);

            //查询运维管理活动的方法
            List<ManageActivity> manageActivityList = manageActivityService.queryManageActivityForMode(ManageSourceEnum.CAPACITY);
            if (!ObjectUtils.isEmpty(manageActivityList)) {
                Map<String, ManageActivity> manageActivityMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
                for (ManageActivity manageActivity : manageActivityList) {
                    manageActivityMap.put(manageActivity.getManageId(), manageActivity);
                }

                for (CapacityThresholdInfo thresholdInfoOne : capacityThresholdInfoList) {
                    if (!ObjectUtils.isEmpty(manageActivityMap)) {
                        if (manageActivityMap.containsKey(thresholdInfoOne.getThresholdCode())) {
                            ManageActivity manageActivityOne = manageActivityMap.get(thresholdInfoOne.getThresholdCode());
                            thresholdInfoOne.setActivityType(manageActivityOne.getActivityType());
                            thresholdInfoOne.setSolveInstruction(manageActivityOne.getSolveInstruction());
                        }
                    }
                }

                //修改数据信息,将运维管理活动类型和处理说明信息更新到数据中
                thresholdInfoService.updateAdviceInfoListForActivity(capacityThresholdInfoList);

                //将状态更改为已预览
                paramManageService.updateParamManage(ManageSourceEnum.CAPACITY, ParamTypeEnum.PREVIEW ,null);
            }
        }
        return ResultUtils.success(ResultCode.SUCCESS, CapacityResultMsg.SAVE_PREVIEW_DATA_OK);
    }


    /**
     * 查询容量分析报告
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:46
     * @param capacityThresholdInfo 阈值信息
     * @return 返回容量的分析报告数据
     */
    @Override
    public Result queryCapacityAnalyzeReport(CapacityThresholdInfo capacityThresholdInfo) {
        CapacityAnalyzeReportVo capacityAnalyzeReportVo = new CapacityAnalyzeReportVo();
        //查询最新的分析报告数据
        CapacityThresholdInfo thresholdInfo = thresholdInfoService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(thresholdInfo)) {
            Integer year = thresholdInfo.getYear();
            Integer month = thresholdInfo.getMonth();
            //查询数据信息
            if (ObjectUtils.isEmpty(capacityThresholdInfo)) {
                capacityThresholdInfo = new CapacityThresholdInfo();
            }
            if (ObjectUtils.isEmpty(capacityThresholdInfo.getYear())) {
                capacityThresholdInfo.setYear(year);
            }
            if (ObjectUtils.isEmpty(capacityThresholdInfo.getMonth())) {
                capacityThresholdInfo.setMonth(month);
            }
            //查询统计当月的阈值信息
            List<CapacityThresholdInfo> capacityThresholdInfoList = thresholdInfoService.queryThresholdInfoList(capacityThresholdInfo);
            List<String> thresholdCodeList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
                capacityAnalyzeReportVo.setDataList(capacityThresholdInfoList);
                for (CapacityThresholdInfo capacityThresholdOne : capacityThresholdInfoList) {
                    thresholdCodeList.add(capacityThresholdOne.getThresholdCode());
                }
            }
            //页面显示的x轴数据信息
            List<String> xDataList = this.getDefaultDateList(year, month);
            if (!ObjectUtils.isEmpty(xDataList)) {
                capacityAnalyzeReportVo.setXDataList(xDataList);
            }

            //页面显示的y轴数据信息
            List<List<CapacityViewData>> yDataList = new ArrayList<>();
            //根据阈值id集合查询显示的图信息
            if (!ObjectUtils.isEmpty(thresholdCodeList)) {
                CapacityRelatedViewListParameter parameter = new CapacityRelatedViewListParameter();
                parameter.setThresholdCodeList(thresholdCodeList);
                List<CapacityThresholdRelatedViewInfo> viewInfoList = viewInfoService.queryViewInfoList(parameter);
                if (!ObjectUtils.isEmpty(viewInfoList)) {
                    for (CapacityThresholdInfo capacityThresholdInfoOne : capacityThresholdInfoList) {
                        List<CapacityViewData> viewDataList = new ArrayList<>();
                        for (CapacityThresholdRelatedViewInfo viewInfoOne : viewInfoList) {
                            if (capacityThresholdInfoOne.getThresholdCode().equals(viewInfoOne.getThresholdCode())) {
                                CapacityViewData capacityViewData = new CapacityViewData();
                                capacityViewData.setName(capacityThresholdInfoOne.getThresholdName());
                                capacityViewData.setValue(viewInfoOne.getValue());
                                viewDataList.add(capacityViewData);
                            }
                        }
                        yDataList.add(viewDataList);
                    }
                }
            }
            if (!ObjectUtils.isEmpty(yDataList)) {
                capacityAnalyzeReportVo.setYDataList(yDataList);
            }
        }
        return ResultUtils.success(capacityAnalyzeReportVo);
    }


    /**
     * 显示默认的统计的时间段
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:45
     * @param year 年份
     * @param month 月份
     * @return 获取默认时间段
     */
    public List<String> getDefaultDateList(Integer year, Integer month) {
        List<String> xDataList = new ArrayList<>();
        //找到去年同期的数据
        CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(year, month);
        int startYear = beforeYearDate.getYear();
        int startMonth = beforeYearDate.getMonth();
        //过滤掉同比去年的年月的数据
        startMonth ++;
        if (startMonth > 12) {
            startYear ++;
            startMonth = 1;
        }
        int allMonth = CapacityConstant.STATISTICS_MONTH;
        int forecastMonth = CapacityConstant.FORECAST_MONTH;
        for (int i = 1; i <= allMonth + forecastMonth; i ++) {
            xDataList.add(startYear + "-" + startMonth);
            startMonth ++;
            if (startMonth > 12) {
                startYear ++;
                startMonth = 1;
            }
        }
        return xDataList;
    }


    /**
     * 判断是否能够分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 14:15
     * @param cabinetDto 最新的机柜信息
     * @return 返回是否能够分析数据
     */
    public boolean checkIsAbleAnalysis(CabinetDto cabinetDto) {
        //查询最新的机柜基础信息
        BaseCabinetDto baseCabinetDto = baseCabinetService.queryTopDataByTime();
        //查询最新的it能耗信息
        ItEnergyCurrent itEnergyCurrent = itEnergyCurrentService.queryTopDataByTime();
        //查询最新的综合布线信息
        GenericCablingDto genericCablingDto = genericCablingService.queryTopDataByTime();
        boolean isAbleAnalysis = false;
        //判断每组数据的时间是否都在一个月上
        if (!ObjectUtils.isEmpty(baseCabinetDto) && !ObjectUtils.isEmpty(cabinetDto) && !ObjectUtils.isEmpty(itEnergyCurrent) && !ObjectUtils.isEmpty(genericCablingDto)) {
            Integer cabinetYear = cabinetDto.getYear();
            Integer cabinetMonth = cabinetDto.getMonth();
            Integer itEnergyYear = itEnergyCurrent.getYear();
            Integer itEnergyMonth = itEnergyCurrent.getMonth();
            Integer genericCablingYear = genericCablingDto.getYear();
            Integer genericCablingMonth = genericCablingDto.getMonth();

            if (cabinetYear.equals(itEnergyYear) && cabinetMonth.equals(itEnergyMonth)) {
                if (cabinetYear.equals(genericCablingYear) && cabinetMonth.equals(genericCablingMonth)) {
                    isAbleAnalysis = true;
                }
            }
        }
        return isAbleAnalysis;
    }

    /**
     * 获取容量阈值
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 14:35
     */
    public Map<String, List<SystemSetting>> searchCapacityThresholdList() {
        //查询容量的阈值
        List<SystemSetting> settings = systemService.querySystemValueByCode("10");

        //查询系统设置的map
        Map<String, List<SystemSetting>> systemSettingMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(settings)) {
            for (SystemSetting systemSetting : settings) {
                if (!ObjectUtils.isEmpty(systemSetting.getChild())) {
                    List<SystemSetting> systemSettingList = new ArrayList<>();
                    for (SystemSetting systemSettingChildInfo : systemSetting.getChild()) {
                        if (systemSettingChildInfo.isCheckStatus() && !ObjectUtils.isEmpty(systemSettingChildInfo.getValue())) {
                            systemSettingList.add(systemSettingChildInfo);
                        }
                    }
                    if (!ObjectUtils.isEmpty(systemSettingList)) {
                        systemSettingMap.put(systemSetting.getCode(), systemSettingList);
                    }
                }
            }
        }
        return systemSettingMap;
    }


}
