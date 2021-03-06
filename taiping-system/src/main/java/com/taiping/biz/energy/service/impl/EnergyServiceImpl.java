package com.taiping.biz.energy.service.impl;

import com.google.common.collect.Lists;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.energy.analyze.EnergyViewData;
import com.taiping.bean.energy.analyze.vo.EnergyAnalyzeReportVo;
import com.taiping.bean.energy.dto.ElectricInstrumentImportDto;
import com.taiping.bean.energy.dto.ElectricInstrumentNotPageDto;
import com.taiping.bean.energy.dto.ElectricPowerImportDto;
import com.taiping.bean.energy.dto.PowerEnergyNotPageDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.bean.energy.parameter.analyze.*;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.biz.capacity.service.impl.CapacityServiceImpl;
import com.taiping.biz.energy.analyze.AllEnergyAnalyze;
import com.taiping.biz.energy.analyze.PowerEnergyAnalyze;
import com.taiping.biz.energy.component.EnergyComponent;
import com.taiping.biz.energy.service.ElectricInstrumentService;
import com.taiping.biz.energy.service.EnergyService;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeInfoService;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeRelatedViewInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdRelatedViewInfoService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.constant.energy.EnergyConstant;
import com.taiping.constant.energy.EnergyResultCode;
import com.taiping.constant.energy.EnergyResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import com.taiping.entity.analyze.energy.EnergyAnalyzeRelatedViewInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.energy.ElectricInstrument;
import com.taiping.entity.energy.PowerEnergyItem;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.manage.ParamManage;
import com.taiping.enums.analyze.energy.EnergyStatisticalTypeEnum;
import com.taiping.enums.cabinet.ItEnergySheetEnum;
import com.taiping.enums.energy.ElectricPowerIsChildEnum;
import com.taiping.enums.energy.EnergyTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.read.energy.ElectricExcelImportRead;
import com.taiping.read.energy.ElectricPowerExcelImportRead;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import com.taiping.utils.common.analyze.capacity.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ?????????????????????
 *
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 21:00
 */
@Service
@Slf4j
public class EnergyServiceImpl implements EnergyService {

    /**
     * ?????????????????????????????????
     */
    @Autowired
    private ElectricInstrumentService electricInstrumentService;

    /**
     * ???????????????????????????????????????
     */
    @Autowired
    private PowerEnergyItemService powerEnergyItemService;

    /**
     * ?????????????????????
     */
    @Autowired
    private ElectricExcelImportRead electricExcelImportRead;

    /**
     * ?????????????????????
     */
    @Autowired
    private ElectricPowerExcelImportRead electricPowerExcelImportRead;

    /**
     * ????????????
     */
    @Autowired
    private EnergyComponent energyComponent;

    /**
     * ??????????????????
     */
    @Autowired
    private PowerEnergyAnalyze powerEnergyAnalyze;


    /**
     * ???????????????????????????
     */
    @Autowired
    private ParamManageService paramManageService;


    /**
     * ???????????????????????????
     */
    @Autowired
    private ManageActivityService manageActivityService;


    /**
     * ?????????????????????
     */
    @Autowired
    private EnergyThresholdInfoService energyThresholdInfoService;

    /**
     * ????????????????????????
     */
    @Autowired
    private EnergyThresholdRelatedViewInfoService relatedViewInfoService;


    /**
     * ????????????????????????????????????
     */
    @Autowired
    private EnergyAnalyzeInfoService energyAnalyzeInfoService;

    /**
     * ????????????????????????????????????
     */
    @Autowired
    private EnergyAnalyzeRelatedViewInfoService analyzeViewInfoService;

    /**
     * ???????????????
     */
    @Autowired
    private CapacityServiceImpl capacityService;

    /**
     * ???????????????
     */
    @Autowired
    private AllEnergyAnalyze allEnergyAnalyze;

    /**
     * ????????????????????????
     *
     * @param file ??????????????????
     * @return ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/10/10 16:54
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importPowerEnergyInfo(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<PowerEnergyItem> powerEnergyList = new ArrayList<>();
        List<ElectricPowerImportDto> importEnergyList = new ArrayList<>();
        Map<String, List<PowerEnergyItem>> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        try {
            stringListMap = electricPowerExcelImportRead.readExcel(file);
            //??????it??????????????????
            importEnergyList = (List) stringListMap.get(ItEnergySheetEnum.SHEET_ONE.getSheetName());

            //?????????????????????????????????????????????
            PowerEnergyItem powerEnergyItem = powerEnergyItemService.queryTopDataByTime();
            int beforeYear = 0;
            int beforeMonth = 0;
            if (!ObjectUtils.isEmpty(powerEnergyItem)) {
                beforeYear = powerEnergyItem.getYear();
                beforeMonth = powerEnergyItem.getMonth();
            }

            //?????????????????????????????????????????????
            if (!ObjectUtils.isEmpty(importEnergyList)) {
                for (ElectricPowerImportDto electricPowerImportDto : importEnergyList) {
                    map = energyComponent.setMonthData(map, electricPowerImportDto.getPowerEnergyItemList());
                }
            }

            String flag = "0";
            int dataYear = 0;
            energyComponent.getYearAndFlag(beforeYear, flag, dataYear, map);
            //??????????????????????????????
            powerEnergyList = energyComponent.getIsInsertEnergyList(flag, map, dataYear, beforeMonth, EnergyTypeEnum.POWER_ENERGY);
            //??????????????????????????????
            if (!ObjectUtils.isEmpty(powerEnergyList)) {
                powerEnergyItemService.insertPowerEnergyItemBatch(powerEnergyList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(EnergyResultCode.IMPORT_POWER_ITEM_ERROR, EnergyResultMsg.IMPORT_POWER_ITEM_ERROR);
        }
        return ResultUtils.success();
    }


    /**
     * ???????????????????????????
     *
     * @param file ??????
     * @return ??????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/12 17:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importAllEnergy(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<ElectricInstrument> allEnergyList = new ArrayList<>();
        List<ElectricInstrumentImportDto> importEnergyList = new ArrayList<>();
        Map<String, List<ElectricInstrument>> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        try {
            stringListMap = electricExcelImportRead.readExcel(file);
            //?????????????????????
            importEnergyList = (List) stringListMap.get(ItEnergySheetEnum.SHEET_ONE.getSheetName());

            //?????????????????????????????????????????????
            ElectricInstrument electricInstrument = electricInstrumentService.queryTopDataByTime();
            int beforeYear = 0;
            int beforeMonth = 0;
            if (!ObjectUtils.isEmpty(electricInstrument)) {
                beforeYear = electricInstrument.getYear();
                beforeMonth = electricInstrument.getMonth();
            }
            //?????????????????????????????????????????????
            if (!ObjectUtils.isEmpty(importEnergyList)) {
                for (ElectricInstrumentImportDto electricInstrumentImportDto : importEnergyList) {
                    map = energyComponent.setMonthData(map, electricInstrumentImportDto.getElectricInstrumentList());
                }
            }

            String flag = "0";
            int dataYear = 0;
            energyComponent.getYearAndFlag(beforeYear, flag, dataYear, map);
            //???????????????????????????
            allEnergyList = energyComponent.getIsInsertEnergyList(flag, map, dataYear, beforeMonth, EnergyTypeEnum.ALL_ENERGY);
            //???????????????????????????
            if (!ObjectUtils.isEmpty(allEnergyList)) {
                electricInstrumentService.insertElectricInstrumentBatch(allEnergyList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(EnergyResultCode.IMPORT_ALL_ENERGY_ERROR, EnergyResultMsg.IMPORT_ALL_ENERGY_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * ????????????????????????????????????
     *
     * @param parameter ??????
     * @return ????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/10/29 21:47
     */
    @Override
    public Result queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter) {
        List<ElectricInstrumentNotPageDto> data = electricInstrumentService.queryAllEnergyListNotPage(parameter);
        return ResultUtils.success(data);
    }

    /**
     * ?????????????????????????????????
     *
     * @param parameter ??????
     * @return ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/15 13:17
     */
    @Override
    public Result queryPowerEnergyListNotPage(PowerEnergyListParameter parameter) {
        List<PowerEnergyNotPageDto> data = powerEnergyItemService.queryPowerEnergyListNotPage(parameter);
        return ResultUtils.success(data);
    }


    /**
     * ????????????
     *
     * @return ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 9:42
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result energyAnalysis() {
        //?????????????????????????????????????????????
        PowerEnergyItem powerEnergyItem = powerEnergyItemService.queryTopDataByTime();
        boolean isAbleAnalysis = this.checkIsAbleAnalysis(powerEnergyItem);
        //??????????????????
        if (!isAbleAnalysis) {
            return ResultUtils.warn(EnergyResultCode.ENERGY_ANALYZE_ERROR, EnergyResultMsg.ENERGY_ANALYZE_ERROR);
        }

        Integer analysisYear = powerEnergyItem.getYear();
        Integer analysisMonth = powerEnergyItem.getMonth();

        Long dateTime = 0L;
        dateTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, dateTime);

        //??????????????????
        paramManageService.checkAnalyze(ManageSourceEnum.ENERGY, dateTime);

        //????????????????????????
        powerEnergyAnalyze.analyzeData(analysisYear, analysisMonth);

        //???????????????
        allEnergyAnalyze.analyzeData(analysisYear, analysisMonth);

        //????????????????????????????????????????????????
        paramManageService.updateParamManage(ManageSourceEnum.ENERGY, ParamTypeEnum.ANALYZE, dateTime);

        return ResultUtils.success(ResultCode.SUCCESS, EnergyResultMsg.ENERGY_ANALYZE_OK);
    }


    /**
     * ??????????????????
     *
     * @return ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 9:42
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result savePreviewData() {
        //??????????????????????????????
        paramManageService.checkPreview(ManageSourceEnum.ENERGY);
        //?????????????????????????????????
        ParamManage paramManage = paramManageService.queryParamManage(ManageSourceEnum.ENERGY);
        Long analyzeTime = paramManage.getNearTime();
        CapacityAnalyzeTime capacityAnalyzeTime = DateUtil.getTimeYearAndMonth(analyzeTime);
        //????????????????????????????????????????????????
        this.deleteBatch(capacityAnalyzeTime);

        //?????????????????????????????????
        List<ManageActivity> manageActivityList = manageActivityService.queryManageActivityForMode(ManageSourceEnum.ENERGY);

        if (!ObjectUtils.isEmpty(manageActivityList)) {
            Map<String, ManageActivity> manageActivityMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            List<String> manageIdList = new ArrayList<>();
            for (ManageActivity manageActivityOne : manageActivityList) {
                manageActivityMap.put(manageActivityOne.getManageId(), manageActivityOne);
                manageIdList.add(manageActivityOne.getManageId());
            }

            //???????????????????????????id????????????????????????
            List<EnergyThresholdRelatedViewInfo> viewInfoList = this.getViewList(manageIdList);

            //???????????????????????????id?????????????????????
            EnergyThresholdInfoListParameter parameter = new EnergyThresholdInfoListParameter();
            parameter.setThresholdCodeList(manageIdList);
            List<EnergyThresholdInfo> thresholdList = energyThresholdInfoService.queryThresholdInfoList(parameter);

            //????????????code????????????????????????id?????????
            parameter = new EnergyThresholdInfoListParameter();
            parameter.setParentCodeList(manageIdList);
            List<EnergyThresholdInfo> childThresholdList = energyThresholdInfoService.queryThresholdInfoList(parameter);
            Map<String, List<EnergyThresholdInfo>> childMap = this.getChildMap(childThresholdList);

            //?????????????????????
            List<EnergyAnalyzeInfo> energyAnalyzeList = this.getAddEnergyAnalyzeList(capacityAnalyzeTime, thresholdList, childMap, manageActivityMap, analyzeTime);

            //???????????????????????????
            List<EnergyAnalyzeRelatedViewInfo> energyViewList = this.getAddEnergyViewList(capacityAnalyzeTime, analyzeTime, viewInfoList);

            //??????????????????????????????
            this.insertAnalyzeInfoBatch(energyAnalyzeList);

            //???????????????????????????????????????
            this.insertAnalyzeViewInfoBatch(energyViewList);

            //???????????????????????????
            paramManageService.updateParamManage(ManageSourceEnum.ENERGY, ParamTypeEnum.PREVIEW, null);
        }

        return ResultUtils.success(ResultCode.SUCCESS, EnergyResultMsg.SAVE_PREVIEW_DATA_OK);
    }


    /**
     * ????????????????????????
     *
     * @param energyAnalyzeInfo ??????????????????
     * @return ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 11:05
     */
    @Override
    public Result queryEnergyAnalyzeReport(EnergyAnalyzeInfo energyAnalyzeInfo) {
        EnergyAnalyzeReportVo reportVo = new EnergyAnalyzeReportVo();
        //?????????????????????????????????
        EnergyAnalyzeInfo energyAnalyze = energyAnalyzeInfoService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(energyAnalyze)) {
            Integer year = energyAnalyze.getYear();
            Integer month = energyAnalyze.getMonth();
            //??????????????????
            if (ObjectUtils.isEmpty(energyAnalyzeInfo)) {
                energyAnalyzeInfo = new EnergyAnalyzeInfo();
            }
            if (ObjectUtils.isEmpty(energyAnalyzeInfo.getYear())) {
                energyAnalyzeInfo.setYear(year);
            }
            if (ObjectUtils.isEmpty(energyAnalyzeInfo.getMonth())) {
                energyAnalyzeInfo.setMonth(month);
            }
            //?????????????????????????????????
            EnergyAnalyzeInfoListParameter parameter = new EnergyAnalyzeInfoListParameter();
            BeanUtils.copyProperties(energyAnalyzeInfo, parameter);
            List<EnergyAnalyzeInfo> energyAnalyzeInfoList = energyAnalyzeInfoService.queryThresholdInfoList(parameter);
            if (!ObjectUtils.isEmpty(energyAnalyzeInfoList)) {
                List<EnergyAnalyzeInfo> dataList = Lists.newArrayList();
                //=============================================================
                //todo: ???????????? ?????????????????????
                energyAnalyzeInfoList.forEach(obj -> {
                    if (StringUtils.isEmpty(obj.getParentCode())) {
                        //???????????????????????? ??? '####' ??????
                        String advice = obj.getAdvice();
                        if (advice.contains("####")) {
                            advice = advice.substring(0, advice.indexOf("####"));
                            obj.setAdvice(advice);
                        }
                        dataList.add(getChild(obj, energyAnalyzeInfoList));
                    }
                });
                reportVo.setDataList(dataList);
            }

            //???????????????x???????????????
            List<String> xDataList = this.getDefaultDateList(year, month);
            if (!ObjectUtils.isEmpty(xDataList)) {
                reportVo.setXDataList(xDataList);
            }

            //???????????????y???????????????
            List<List<EnergyViewData>> yDataList = new ArrayList<>();
            //????????????????????????????????????
            if (!ObjectUtils.isEmpty(reportVo.getDataList())) {
                EnergyAnalyzeRelatedViewListParameter parameterInfo = new EnergyAnalyzeRelatedViewListParameter();
                parameterInfo.setAnalyzeYear(year);
                parameterInfo.setAnalyzeMonth(month);
                List<EnergyAnalyzeRelatedViewInfo> viewInfoList = analyzeViewInfoService.queryViewInfoList(parameterInfo);
                if (!ObjectUtils.isEmpty(viewInfoList)) {
                    for (EnergyAnalyzeInfo energyAnalyzeInfoOne : reportVo.getDataList()) {
                        List<EnergyViewData> viewDataList = new ArrayList<>();
                        for (EnergyAnalyzeRelatedViewInfo viewInfoOne : viewInfoList) {
                            if (energyAnalyzeInfoOne.getThresholdCode().equals(viewInfoOne.getThresholdCode())) {
                                EnergyViewData capacityViewData = new EnergyViewData();
                                capacityViewData.setName(energyAnalyzeInfoOne.getThresholdName());
                                capacityViewData.setValue(viewInfoOne.getValue());
                                viewDataList.add(capacityViewData);
                            }
                        }
                        yDataList.add(viewDataList);
                    }
                }
            }
            if (!ObjectUtils.isEmpty(yDataList)) {
                reportVo.setYDataList(yDataList);
            }
        }
        return ResultUtils.success(reportVo);
    }


    /**
     * ????????????
     *
     * @param parentCode
     * @param allData
     * @return
     */
    private EnergyAnalyzeInfo getChild(EnergyAnalyzeInfo parentCode, List<EnergyAnalyzeInfo> allData) {
        allData.forEach(obj -> {
            if (parentCode.getThresholdCode().equals(obj.getParentCode())) {
                if (parentCode.getChildList() == null) {
                    parentCode.setChildList(new ArrayList<>());
                }
                parentCode.getChildList().add(getChild(obj, allData));
            }
        });
        return parentCode;
    }


    /**
     * ??????????????????????????????
     *
     * @param energyAnalyzeList ??????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:53
     */
    public void insertAnalyzeInfoBatch(List<EnergyAnalyzeInfo> energyAnalyzeList) {
        if (!ObjectUtils.isEmpty(energyAnalyzeList)) {
            energyAnalyzeInfoService.insertThresholdInfoBatch(energyAnalyzeList);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param energyViewList ?????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:53
     */
    public void insertAnalyzeViewInfoBatch(List<EnergyAnalyzeRelatedViewInfo> energyViewList) {
        if (!ObjectUtils.isEmpty(energyViewList)) {
            analyzeViewInfoService.insertThresholdRelatedViewInfoBatch(energyViewList);
        }
    }


    /**
     * ????????????????????????
     *
     * @param manageIdList ?????????????????????id??????
     * @return ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:38
     */
    public List<EnergyThresholdRelatedViewInfo> getViewList(List<String> manageIdList) {
        EnergyRelatedViewListParameter parameter = new EnergyRelatedViewListParameter();
        List<EnergyThresholdRelatedViewInfo> viewInfoList = relatedViewInfoService.queryViewInfoList(parameter);
        return viewInfoList;
    }

    /**
     * ??????????????????????????????
     *
     * @param capacityAnalyzeTime ???????????????
     * @param viewInfoList        ????????????
     * @param analyzeTime         ????????????
     * @return ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:46
     */
    public List<EnergyAnalyzeRelatedViewInfo> getAddEnergyViewList(CapacityAnalyzeTime capacityAnalyzeTime, Long analyzeTime, List<EnergyThresholdRelatedViewInfo> viewInfoList) {
        List<EnergyAnalyzeRelatedViewInfo> energyAnalyzeList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(viewInfoList)) {
            for (EnergyThresholdRelatedViewInfo viewInfoOne : viewInfoList) {
                EnergyAnalyzeRelatedViewInfo analyzeRelatedViewInfo = new EnergyAnalyzeRelatedViewInfo();
                BeanUtils.copyProperties(viewInfoOne, analyzeRelatedViewInfo);
                analyzeRelatedViewInfo.setAnalyzeTime(analyzeTime);
                analyzeRelatedViewInfo.setAnalyzeMonth(capacityAnalyzeTime.getMonth());
                analyzeRelatedViewInfo.setAnalyzeYear(capacityAnalyzeTime.getYear());
                energyAnalyzeList.add(analyzeRelatedViewInfo);
            }
        }
        return energyAnalyzeList;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param capacityAnalyzeTime ??????????????????
     * @param thresholdList       ???????????????
     * @param childMap            ?????????????????????
     * @param manageActivityMap   ?????????????????????map
     * @param analyzeTime         ????????????
     * @return ?????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:21
     */
    public List<EnergyAnalyzeInfo> getAddEnergyAnalyzeList(CapacityAnalyzeTime capacityAnalyzeTime, List<EnergyThresholdInfo> thresholdList,
                                                           Map<String, List<EnergyThresholdInfo>> childMap, Map<String, ManageActivity> manageActivityMap,
                                                           Long analyzeTime) {
        Integer year = capacityAnalyzeTime.getYear();
        Integer month = capacityAnalyzeTime.getMonth();
        List<EnergyAnalyzeInfo> energyAnalyzeList = new ArrayList<>();
        //???????????????????????????
        Map<String, EnergyAnalyzeInfo> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(thresholdList)) {
            //??????????????????
            String powerReason = "";
            //it????????????
            String itReason = "";
            //???????????????????????????????????????????????????????????????????????????
            for (EnergyThresholdInfo thresholdInfo : thresholdList) {
                EnergyAnalyzeInfo analyze = new EnergyAnalyzeInfo();
                String thresholdCode = thresholdInfo.getThresholdCode();
                String isChild = thresholdInfo.getIsChild();
                String type = thresholdInfo.getType();
                String adviceName = thresholdInfo.getAdvice();
                ManageActivity manageActivityOne = manageActivityMap.get(thresholdCode);
                if (ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(isChild)) {
                    if (EnergyStatisticalTypeEnum.POWER_ENERGY.getType().equals(type)) {
                        //????????????
                        powerReason = manageActivityOne.getSolveInstruction();
                    } else if (EnergyStatisticalTypeEnum.IT_ENERGY.getType().equals(type)) {
                        //IT??????
                        itReason = manageActivityOne.getSolveInstruction();
                    }
                }
                BeanUtils.copyProperties(thresholdInfo, analyze);
                analyze.setThresholdInfoId(NineteenUUIDUtils.uuid());
                analyze.setManageType(manageActivityOne.getManageType());
                analyze.setSolveInstruction(manageActivityOne.getSolveInstruction());
                analyze.setActivityType(manageActivityOne.getActivityType());
                if (childMap.containsKey(thresholdCode)) {
                    adviceName += "####";
                    String itemInfo = "";
                    for (EnergyThresholdInfo energyThresholdInfo : childMap.get(thresholdCode)) {
                        if (!ObjectUtils.isEmpty(itemInfo)) {
                            itemInfo += "@@@@";
                        }
                        itemInfo += energyThresholdInfo.getAdvice();
                        //??????????????????
                        EnergyAnalyzeInfo analyzeChildInfo = new EnergyAnalyzeInfo();
                        BeanUtils.copyProperties(energyThresholdInfo, analyzeChildInfo);
                        analyzeChildInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
                        analyzeChildInfo.setAnalyzeMonth(month);
                        analyzeChildInfo.setAnalyzeYear(year);
                        map.put(analyzeChildInfo.getThresholdCode(), analyzeChildInfo);
                    }
                    adviceName += itemInfo;
                }
                analyze.setIsChild(ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode());
                analyze.setAdvice(adviceName);
                analyze.setAnalyzeMonth(month);
                analyze.setAnalyzeYear(year);
                analyze.setAnalyzeTime(analyzeTime);
                energyAnalyzeList.add(analyze);
            }
            energyAnalyzeList = this.setAdviceToPueInfo(energyAnalyzeList, itReason, powerReason);
        }

        if (!ObjectUtils.isEmpty(map)) {
            for (String thresholdCode : map.keySet()) {
                energyAnalyzeList.add(map.get(thresholdCode));
            }
        }
        return energyAnalyzeList;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param capacityAnalyzeTime ????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:08
     */
    public void deleteBatch(CapacityAnalyzeTime capacityAnalyzeTime) {
        Integer year = capacityAnalyzeTime.getYear();
        Integer month = capacityAnalyzeTime.getMonth();
        EnergyAnalyzeDeleteParameter parameter = new EnergyAnalyzeDeleteParameter();
        parameter.setAnalyzeMonth(month);
        parameter.setAnalyzeYear(year);
        //?????????????????????????????????
        energyAnalyzeInfoService.deleteAnalyzeBatch(parameter);

        EnergyAnalyzeDeleteViewParameter viewParameter = new EnergyAnalyzeDeleteViewParameter();
        BeanUtils.copyProperties(parameter, viewParameter);
        //?????????????????????????????????
        analyzeViewInfoService.deleteAnalyzeRelatedInfoBatch(viewParameter);
    }

    /**
     * ?????????????????????pue??????
     *
     * @param energyAnalyzeList ??????????????????
     * @param itReason          it??????
     * @param powerReason       ??????????????????
     * @return ?????????????????????pue??????
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:49
     */
    public List<EnergyAnalyzeInfo> setAdviceToPueInfo(List<EnergyAnalyzeInfo> energyAnalyzeList, String itReason, String powerReason) {
        for (EnergyAnalyzeInfo thresholdInfo : energyAnalyzeList) {
            String type = thresholdInfo.getType();
            if (EnergyStatisticalTypeEnum.PUE.getType().equals(type)) {
                List<String> templateList = new ArrayList<>();
                templateList.add("itReason");
                templateList.add("powerReason");
                Map<String, String> templateMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
                templateMap.put("itReason", itReason);
                templateMap.put("powerReason", powerReason);
                String adviceName = thresholdInfo.getAdvice();
                //??????pue????????????
                adviceName = TemplateUtil.parseTemplate(adviceName, templateList, templateMap);
                thresholdInfo.setAdvice(adviceName);
            }
        }
        return energyAnalyzeList;
    }

    /**
     * ??????????????????map
     *
     * @param childThresholdList ??????????????????
     * @return ????????????map
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 17:51
     */
    public Map<String, List<EnergyThresholdInfo>> getChildMap(List<EnergyThresholdInfo> childThresholdList) {
        Map<String, List<EnergyThresholdInfo>> childMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(childThresholdList)) {
            for (EnergyThresholdInfo energyThresholdInfo : childThresholdList) {
                List<EnergyThresholdInfo> thresholdInfoMapOne = new ArrayList<>();
                String code = energyThresholdInfo.getParentCode();
                if (!ObjectUtils.isEmpty(childMap)) {
                    if (childMap.containsKey(code)) {
                        thresholdInfoMapOne = childMap.get(code);
                    }
                }
                thresholdInfoMapOne.add(energyThresholdInfo);
                childMap.put(code, thresholdInfoMapOne);
            }
        }
        return childMap;
    }


    /**
     * ??????????????????????????????
     *
     * @param powerEnergyItem ?????????????????????????????????
     * @return ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date 2019/11/1 14:15
     */
    public boolean checkIsAbleAnalysis(PowerEnergyItem powerEnergyItem) {
        //??????????????????????????????
        ElectricInstrument electricInstrument = electricInstrumentService.queryTopDataByTime();
        boolean isAbleAnalysis = false;
        //???????????????????????????????????????????????????
        if (!ObjectUtils.isEmpty(powerEnergyItem) && !ObjectUtils.isEmpty(electricInstrument)) {
            Integer powerYear = powerEnergyItem.getYear();
            Integer powerMonth = powerEnergyItem.getMonth();
            Integer allYear = electricInstrument.getYear();
            Integer allMonth = electricInstrument.getMonth();

            if (powerYear.equals(allYear) && powerMonth.equals(allMonth)) {
                isAbleAnalysis = true;
            }
        }
        return isAbleAnalysis;
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

        int allEnergyMonth = EnergyConstant.STATISTICS_MONTH;
        int forecastEnergyMonth = EnergyConstant.FORECAST_MONTH;
        for (int i = 1; i <= allEnergyMonth + forecastEnergyMonth; i ++) {
            xDataList.add(startYear + "-" + startMonth);
            startMonth ++;
            if (startMonth > 12) {
                startYear ++;
                startMonth = 1;
            }
        }
        return xDataList;
    }
}
