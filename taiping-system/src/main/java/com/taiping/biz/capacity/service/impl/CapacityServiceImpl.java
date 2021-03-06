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
 * ???????????????
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:03
 */
@Service
@Slf4j
public class CapacityServiceImpl implements CapacityService {

    /**
     * ???????????????????????????
     */
    @Autowired
    private BaseCabinetService baseCabinetService;

    /**
     * ???????????????????????????
     */
    @Autowired
    private GenericCablingService genericCablingService;

    /**
     * ?????????????????????
     */
    @Autowired
    private CabinetService cabinetService;

    /**
     * it???????????????
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * ???????????????
     */
    @Autowired
    private SystemService systemService;

    /**
     * ?????????????????????
     */
    @Autowired
    private FloorSpaceAnalyze floorSpaceAnalyze;

    /**
     * ??????????????????
     */
    @Autowired
    private FunctionTypeSpaceAnalyze functionTypeSpaceAnalyze;

    /**
     * ????????????????????????
     */
    @Autowired
    private CabinetSpaceAnalyze cabinetSpaceAnalyze;


    /**
     * ????????????????????????
     */
    @Autowired
    private ModuleElectricAnalyze moduleElectricAnalyze;

    /**
     * ups??????????????????
     */
    @Autowired
    private UpsElectricAnalyze upsElectricAnalyze;

    /**
     * ?????????????????????
     */
    @Autowired
    private CabinetColumnAnalyze cabinetColumnAnalyze;

    /**
     * pdu????????????
     */
    @Autowired
    private PduAnalyze pduAnalyze;

    /**
     * ????????????????????????
     */
    @Autowired
    private GenericCablingAnalyze genericCablingAnalyze;



    /**
     * ?????????????????????
     */
    @Autowired
    private CapacityThresholdInfoService thresholdInfoService;

    /**
     * ???????????????????????????????????????
     */
    @Autowired
    private CapacityThresholdRelatedViewInfoService viewInfoService;


    /**
     * ???????????????????????????
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * ???????????????????????????
     */
    @Autowired
    private ParamManageService paramManageService;


    /**
     * ????????????
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:49
     * @return ????????????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result capacityAnalysis() {

        //???????????????????????????
        CabinetDto cabinetDto = cabinetService.queryTopDataByTime();
        boolean isAbleAnalysis = this.checkIsAbleAnalysis(cabinetDto);
        //??????????????????
        if (!isAbleAnalysis) {
            return ResultUtils.warn(CapacityResultCode.CAPACITY_ANALYZE_ERROR, CapacityResultMsg.CAPACITY_ANALYZE_ERROR);
        }
        Integer analysisYear = cabinetDto.getYear();
        Integer analysisMonth = cabinetDto.getMonth();

        Long dateTime = 0L;
        dateTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, dateTime);
        //??????????????????
        paramManageService.checkAnalyze(ManageSourceEnum.CAPACITY, dateTime);

        //??????????????????
        Map<String, List<SystemSetting>> searchCapacityThresholdList = this.searchCapacityThresholdList();

        //??????????????????????????????
        floorSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //???????????????????????????
        functionTypeSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //????????????????????????
        cabinetSpaceAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //????????????
        moduleElectricAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //??????ups
        upsElectricAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //???????????????
        cabinetColumnAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //??????pdu
        pduAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //????????????????????????
        genericCablingAnalyze.analyzeData(searchCapacityThresholdList, analysisYear, analysisMonth);

        //????????????????????????????????????????????????
        paramManageService.updateParamManage(ManageSourceEnum.CAPACITY, ParamTypeEnum.ANALYZE, dateTime);
        return ResultUtils.success(ResultCode.SUCCESS, CapacityResultMsg.CAPACITY_ANALYZE_OK);
    }


    /**
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return ????????????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result savePreviewData() {
        //?????????????????????????????????
        CabinetDto cabinetDto = cabinetService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(cabinetDto)) {
            Integer year = cabinetDto.getYear();
            Integer month = cabinetDto.getMonth();

            //??????????????????????????????
            paramManageService.checkPreview(ManageSourceEnum.CAPACITY);

            //??????????????????
            CapacityThresholdInfo thresholdInfo = new CapacityThresholdInfo();
            thresholdInfo.setYear(year);
            thresholdInfo.setMonth(month);
            //?????????????????????????????????
            List<CapacityThresholdInfo> capacityThresholdInfoList = thresholdInfoService.queryThresholdInfoList(thresholdInfo);

            //?????????????????????????????????
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

                //??????????????????,??????????????????????????????????????????????????????????????????
                thresholdInfoService.updateAdviceInfoListForActivity(capacityThresholdInfoList);

                //???????????????????????????
                paramManageService.updateParamManage(ManageSourceEnum.CAPACITY, ParamTypeEnum.PREVIEW ,null);
            }
        }
        return ResultUtils.success(ResultCode.SUCCESS, CapacityResultMsg.SAVE_PREVIEW_DATA_OK);
    }


    /**
     * ????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:46
     * @param capacityThresholdInfo ????????????
     * @return ?????????????????????????????????
     */
    @Override
    public Result queryCapacityAnalyzeReport(CapacityThresholdInfo capacityThresholdInfo) {
        CapacityAnalyzeReportVo capacityAnalyzeReportVo = new CapacityAnalyzeReportVo();
        //?????????????????????????????????
        CapacityThresholdInfo thresholdInfo = thresholdInfoService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(thresholdInfo)) {
            Integer year = thresholdInfo.getYear();
            Integer month = thresholdInfo.getMonth();
            //??????????????????
            if (ObjectUtils.isEmpty(capacityThresholdInfo)) {
                capacityThresholdInfo = new CapacityThresholdInfo();
            }
            if (ObjectUtils.isEmpty(capacityThresholdInfo.getYear())) {
                capacityThresholdInfo.setYear(year);
            }
            if (ObjectUtils.isEmpty(capacityThresholdInfo.getMonth())) {
                capacityThresholdInfo.setMonth(month);
            }
            //?????????????????????????????????
            List<CapacityThresholdInfo> capacityThresholdInfoList = thresholdInfoService.queryThresholdInfoList(capacityThresholdInfo);
            List<String> thresholdCodeList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
                capacityAnalyzeReportVo.setDataList(capacityThresholdInfoList);
                for (CapacityThresholdInfo capacityThresholdOne : capacityThresholdInfoList) {
                    thresholdCodeList.add(capacityThresholdOne.getThresholdCode());
                }
            }
            //???????????????x???????????????
            List<String> xDataList = this.getDefaultDateList(year, month);
            if (!ObjectUtils.isEmpty(xDataList)) {
                capacityAnalyzeReportVo.setXDataList(xDataList);
            }

            //???????????????y???????????????
            List<List<CapacityViewData>> yDataList = new ArrayList<>();
            //????????????id??????????????????????????????
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
     * ?????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:45
     * @param year ??????
     * @param month ??????
     * @return ?????????????????????
     */
    public List<String> getDefaultDateList(Integer year, Integer month) {
        List<String> xDataList = new ArrayList<>();
        //???????????????????????????
        CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(year, month);
        int startYear = beforeYearDate.getYear();
        int startMonth = beforeYearDate.getMonth();
        //???????????????????????????????????????
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
     * ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 14:15
     * @param cabinetDto ?????????????????????
     * @return ??????????????????????????????
     */
    public boolean checkIsAbleAnalysis(CabinetDto cabinetDto) {
        //?????????????????????????????????
        BaseCabinetDto baseCabinetDto = baseCabinetService.queryTopDataByTime();
        //???????????????it????????????
        ItEnergyCurrent itEnergyCurrent = itEnergyCurrentService.queryTopDataByTime();
        //?????????????????????????????????
        GenericCablingDto genericCablingDto = genericCablingService.queryTopDataByTime();
        boolean isAbleAnalysis = false;
        //???????????????????????????????????????????????????
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
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 14:35
     */
    public Map<String, List<SystemSetting>> searchCapacityThresholdList() {
        //?????????????????????
        List<SystemSetting> settings = systemService.querySystemValueByCode("10");

        //?????????????????????map
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
