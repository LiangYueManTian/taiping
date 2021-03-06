package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabling.dto.CapacityCablingStatisticsDto;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.biz.capacity.service.cabling.GenericCablingService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdCapacityEnum;
import com.taiping.enums.analyze.capacity.ThresholdModuleEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.analyze.capacity.ThresholdViewTypeEnum;
import com.taiping.enums.cabinet.CableTypeEnum;
import com.taiping.enums.cabling.GenericCablingStatusEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.AbstractCapacityAnalyze;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * ???????????????????????????
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class GenericCablingAnalyze extends AbstractCapacityAnalyze {

    /**
     * ??????????????????????????????
     */
    @Autowired
    private ManageActivityService manageActivityService;


    /**
     * ???????????????????????????
     */
    @Autowired
    private CapacityThresholdInfoService thresholdInfoService;

    /**
     * ?????????????????????????????????
     */
    @Autowired
    private CapacityThresholdRelatedInfoService relatedInfoService;


    /**
     * ???????????????????????????????????????
     */
    @Autowired
    private CapacityThresholdRelatedViewInfoService viewInfoService;


    /**
     * ?????????????????????
     */
    @Autowired
    private GenericCablingService genericCablingService;

    /**
     * ????????????
     */
    @Autowired
    private CapacityAnalyze capacityAnalyze;



    /**
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param capacityThresholdInfoList ??????????????????
     * @param rackStatisticsDtoList ????????????????????????????????????
     * @return ????????????????????????
     */
    protected List<CapacityThresholdRelatedInfo> calculateRecommendInfo(List<CapacityThresholdInfo> capacityThresholdInfoList, List<CapacityCablingStatisticsDto> rackStatisticsDtoList) {
        List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList = new ArrayList<>();
        //??????????????????
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                List<CapacityCablingStatisticsDto> recommendList = new ArrayList<>();
                String data  = capacityThresholdInfo.getThresholdData();
                for (CapacityCablingStatisticsDto rackOne : rackStatisticsDtoList) {
                    String compareData = this.getThresholdData(rackOne);
                    if (data.equals(compareData)) {
                        if (0 == rackOne.getPortPercent()) {
                            recommendList.add(rackOne);
                        }
                    }
                }

                if (CapacityConstant.ADVICE_TYPE_DIFF.equals(capacityThresholdInfo.getAdviceType()) &&
                        !ObjectUtils.isEmpty(recommendList)) {
                    //????????????
                    for (int i = 0; i < CapacityConstant.RECOMMEND_COUNT ; i++) {
                        if (i + 1 <= recommendList.size()) {
                            CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = this.getOneRelatedInfo(capacityThresholdInfo, recommendList.get(i));
                            capacityThresholdRelatedInfoList.add(capacityThresholdRelatedInfo);
                        }
                    }
                }
            }
        }
        //????????????
        return capacityThresholdRelatedInfoList;
    }



    /**
     * ????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:40
     * @param capacityThresholdInfo ?????????????????????
     * @param recommend ???????????????????????????
     * @return ????????????????????????
     */
    public CapacityThresholdRelatedInfo getOneRelatedInfo(CapacityThresholdInfo capacityThresholdInfo, CapacityCablingStatisticsDto recommend) {
        CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = new CapacityThresholdRelatedInfo();
        capacityThresholdRelatedInfo.setThresholdCode(capacityThresholdInfo.getThresholdCode());
        capacityThresholdRelatedInfo.setThresholdRelatedId(NineteenUUIDUtils.uuid());
        capacityThresholdRelatedInfo.setType(ThresholdViewTypeEnum.VIEW.getViewType());
        String name = recommend.getConnectRackCode();
        capacityThresholdRelatedInfo.setName(name);
        capacityThresholdRelatedInfo.setDataName(name);
        capacityThresholdRelatedInfo.setValue(0);
        return capacityThresholdRelatedInfo;
    }






    /**
     * ????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear ????????????
     * @param analysisMonth ????????????
     * @param capacityThresholdInfoList ??????????????????
     * @param cablingStatisticsDtoList ?????????????????????
     * @return ????????????????????????
     */
    protected List<CapacityThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                      List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                      List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {

        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<CapacityThresholdRelatedViewInfo> viewInfoList = CapacityAnalyze.getDefaultViewData(analysisYear, analysisMonth, capacityThresholdInfoList);
        //???????????????????????????????????????
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //?????????????????????????????????
        for (CapacityCablingStatisticsDto capacityStatisticsDto : cablingStatisticsDtoList) {
            Integer dataYear = capacityStatisticsDto.getYear();
            Integer dataMonth = capacityStatisticsDto.getMonth();
            String value = this.getCompareKey(capacityStatisticsDto);
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = CapacityAnalyze.getCompareViewValue(viewInfo);
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    if (ThresholdModuleEnum.GENERIC_CABLING.getModule().equals(viewInfo.getModuleType())) {
                        //??????????????????????????????
                        double portPercent = capacityStatisticsDto.getPortPercent();
                        viewInfo.setValue(portPercent);
                    }
                }
            }
        }

        dataViewMap = CapacityAnalyze.getDataViewMap(viewInfoList);
        //????????????????????????????????????
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = CapacityAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }



    /**
     * ????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear ??????
     * @param analysisMonth ??????
     * @param capacityThresholdInfoList ??????????????????
     * @param cablingStatisticsDtoList ???????????????
     * @return ????????????
     */
    protected List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {
        //????????????
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //??????????????????
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = this.castGroupInfo(capacityThresholdInfoList, cablingStatisticsDtoList, beforeYearDate, type);
        }
        return capacityThresholdInfoList;
    }


    /**
     * ????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear ??????
     * @param analysisMonth ??????
     * @param cablingStatisticsDtoList ???????????????
     * @param capacityThresholdInfoList ??????????????????
     * @return ????????????
     */
    protected List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {
        //????????????
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //??????????????????
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = this.castGroupInfo(capacityThresholdInfoList, cablingStatisticsDtoList, beforeDate, type);
        }
        return capacityThresholdInfoList;
    }


    /**
     * ????????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:40
     * @param capacityThresholdInfoList  ????????????????????????
     * @param dataStatisticsDtoList ????????????????????????
     * @param beforeDate ???????????????????????????????????????
     * @param type ??????
     * @return ??????????????????????????????????????????
     */
    public List<CapacityThresholdInfo> castGroupInfo(List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                    List<CapacityCablingStatisticsDto> dataStatisticsDtoList,
                                                    CapacityAnalyzeTime beforeDate, String type) {
        List<CapacityCablingStatisticsDto> beforeList = dataStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(beforeDate.getMonth()) && c.getYear().equals(beforeDate.getYear())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(beforeList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                double usedPort = capacityThresholdInfo.getUsedNumber();
                String dataValue = capacityThresholdInfo.getThresholdData();
                //?????????
                double growthPercent = 0;
                for (CapacityCablingStatisticsDto capacitySpaceStatisticsDto : beforeList) {
                    String compareDataValue = this.getThresholdData(capacitySpaceStatisticsDto);
                    if (dataValue.equals(compareDataValue)) {
                        double usedNumber = capacitySpaceStatisticsDto.getUsedPortNumber();
                        growthPercent = CapacityAnalyze.castPercentInfo(usedPort, usedNumber, usedNumber);
                        break;
                    }
                }
                if ("1".equals(type)) {
                    //??????
                    capacityThresholdInfo.setYearOverYearPercent(growthPercent);
                } else if ("2".equals(type)) {
                    //??????
                    capacityThresholdInfo.setRingGrowth(growthPercent);
                }
            }
        }
        return capacityThresholdInfoList;
    }

    /**
     * ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param manageActivityList ??????????????????
     */
    @Override
    protected void insertManageActivityBatch(List<ManageActivity> manageActivityList) {
        manageActivityService.insertManageActivity(ManageSourceEnum.CAPACITY, manageActivityList);
    }

    /**
     * ????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdInfoList ????????????
     */
    @Override
    protected void insertThresholdInfoBatch(List<CapacityThresholdInfo> capacityThresholdInfoList) {
        thresholdInfoService.insertThresholdInfoBatch(capacityThresholdInfoList);
    }

    /**
     * ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedInfoList ????????????????????????
     */
    @Override
    protected void insertThresholdRelatedInfo(List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList) {
        relatedInfoService.insertThresholdRelatedBatch(capacityThresholdRelatedInfoList);
    }

    /**
     * ????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedViewInfoList ????????????????????????????????????
     */
    @Override
    protected void insertThresholdRelatedViewInfo(List<CapacityThresholdRelatedViewInfo> capacityThresholdRelatedViewInfoList) {
        viewInfoService.insertThresholdRelatedViewInfoBatch(capacityThresholdRelatedViewInfoList);
    }

    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param searchCapacityThresholdList ????????????
     * @param analysisYear ???????????????
     * @param analysisMonth ???????????????
     * @return ???????????????????????????????????????
     */
    @Override
    protected CapacityAnalyzeData analyzeDataRealize(Map<String, List<SystemSetting>> searchCapacityThresholdList, Integer analysisYear, Integer analysisMonth) {
        //?????????????????????????????????
        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(ThresholdCapacityEnum.GENERIC_CABLING.getThresholdCode());

        //??????????????????????????????
        List<CapacityCablingStatisticsDto> rackStatisticsDtoList = this.getSearchMonthRackList(analysisYear, analysisMonth);
        String rackType = "2";
        //?????????????????????????????????????????????
        rackStatisticsDtoList = this.setPercentToData(rackStatisticsDtoList, rackType);

        //????????????????????????????????????????????????
        List<CapacityCablingStatisticsDto> cablingStatisticsDtoList = this.getSearchCablingStatisticsDtoList(analysisYear, analysisMonth);

        //?????????????????????????????????????????????????????????????????????
        String cablingType = "1";
        cablingStatisticsDtoList = this.setPercentToData(cablingStatisticsDtoList, cablingType);

        //????????????
        List<CapacityCablingStatisticsDto> nowMonthList = cablingStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //????????????????????????
        List<CapacityThresholdInfo> capacityThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, ThresholdTypeEnum.PORT_TYPE);
        //????????????
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //????????????
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //???????????????????????????
        List<CapacityThresholdRelatedInfo> relatedInfoList = calculateRecommendInfo(capacityThresholdInfoList, rackStatisticsDtoList);
        //????????????????????????
        List<CapacityThresholdRelatedViewInfo> viewInfoList = this.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //??????????????????
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, ThresholdTypeEnum.PORT_TYPE);
        //???????????????????????????
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, ThresholdTypeEnum.PORT_TYPE);
        CapacityAnalyzeData capacityAnalyzeData = new CapacityAnalyzeData();
        capacityAnalyzeData.setThresholdInfoList(capacityThresholdInfoList);
        capacityAnalyzeData.setRelatedInfoList(relatedInfoList);
        capacityAnalyzeData.setRelatedViewInfoList(viewInfoList);
        capacityAnalyzeData.setManageActivityList(manageActivityList);
        return capacityAnalyzeData;
    }


    /**
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:04
     * @param nowMonthList ?????????????????????
     * @param analysisMonth ??????
     * @param analysisYear ??????
     * @param systemSettingList ????????????
     * @param thresholdTypeEnum ????????????
     * @return ??????????????????
     */
    public List<CapacityThresholdInfo> getThresholdList(List<CapacityCablingStatisticsDto> nowMonthList, Integer analysisMonth, Integer analysisYear, List<SystemSetting> systemSettingList, ThresholdTypeEnum thresholdTypeEnum) {
        List<CapacityThresholdInfo> capacityThresholdInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (CapacityCablingStatisticsDto capacityCablingStatisticsDto : nowMonthList) {
                //???????????????
                double portPercent = capacityCablingStatisticsDto.getPortPercent();
                AdviceBean adviceBean = CapacityAnalyze.getAdviceType(systemSettingList, portPercent, thresholdTypeEnum);
                if (!ObjectUtils.isEmpty(adviceBean)) {
                    if (!ObjectUtils.isEmpty(adviceBean.getAdviceType())) {
                        CapacityThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, capacityCablingStatisticsDto, adviceBean, thresholdTypeEnum);
                        capacityThresholdInfoList.add(capacityThresholdInfo);
                    }
                }
            }
        }
        return capacityThresholdInfoList;
    }


    /**
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 23:04
     * @param analysisMonth ????????????
     * @param analysisYear  ????????????
     * @param dataStatisticsDto ??????
     * @param adviceBean ????????????
     * @return ????????????????????????
     */
    public CapacityThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, CapacityCablingStatisticsDto dataStatisticsDto, AdviceBean adviceBean, ThresholdTypeEnum thresholdType) {
        CapacityThresholdInfo capacityThresholdInfo = new CapacityThresholdInfo();
        capacityThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setMonth(analysisMonth);
        capacityThresholdInfo.setYear(analysisYear);
        capacityThresholdInfo.setType(thresholdType.getType());
        capacityThresholdInfo.setModule(ThresholdModuleEnum.GENERIC_CABLING.getModule());
        capacityThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        String data = this.getThresholdData(dataStatisticsDto);
        capacityThresholdInfo.setThresholdData(data);
        String name = dataStatisticsDto.getGenericCablingType();
        name += "-";
        name += CableTypeEnum.getCableTypeByCode(dataStatisticsDto.getCableType());
        capacityThresholdInfo.setThresholdName(name);
        capacityThresholdInfo.setThresholdValue(String.valueOf(dataStatisticsDto.getPortPercent()));
        capacityThresholdInfo.setUsedNumber(dataStatisticsDto.getUsedPortNumber().intValue());
        capacityThresholdInfo.setAllNumber(dataStatisticsDto.getAllPortNumber().intValue());
        capacityThresholdInfo.setUsedCable(dataStatisticsDto.getUsedPortNumber().intValue());
        capacityThresholdInfo.setAllCable(dataStatisticsDto.getAllPortNumber().intValue());
        capacityThresholdInfo.setAdviceType(adviceBean.getAdviceType());
        capacityThresholdInfo.setCause(adviceBean.getActivityCause());
        String adviceInfo = capacityThresholdInfo.getUsedCable() + "";
        adviceInfo += "/";
        adviceInfo += capacityThresholdInfo.getAllCable();
        capacityThresholdInfo.setAdviceInfo(adviceInfo);
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, collectionTime);
        capacityThresholdInfo.setDataCollectionTime(collectionTime);
        Long nowDate = System.currentTimeMillis();
        capacityThresholdInfo.setCreateTime(nowDate);
        return capacityThresholdInfo;
    }

    /**
     * ??????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 21:44
     * @param dataStatisticsDto ????????????
     * @return ????????????
     */
    public String getThresholdData(CapacityCablingStatisticsDto dataStatisticsDto) {
        String data = dataStatisticsDto.getGenericCablingType();
        data += "-";
        data += dataStatisticsDto.getCableType();
        return data;
    }

    /**
     * ?????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:16
     * @param cablingStatisticsDtoList ??????????????????
     * @param type ??????  1 ??????????????????????????? 2 ?????????
     * @return ??????????????????
     */
    public List<CapacityCablingStatisticsDto> setPercentToData(List<CapacityCablingStatisticsDto> cablingStatisticsDtoList, String type) {
        List<CapacityCablingStatisticsDto> dtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(cablingStatisticsDtoList)) {
            Map<String, List<CapacityCablingStatisticsDto>> dtoMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (CapacityCablingStatisticsDto capacityCablingStatisticsDto : cablingStatisticsDtoList) {
                String key = "";
                if ("1".equals(type)) {
                    //????????????????????????????????????
                    key = this.getKey(capacityCablingStatisticsDto);
                } else if ("2".equals(type)) {
                    //?????????
                    key = this.getRackKey(capacityCablingStatisticsDto);
                }
                List<CapacityCablingStatisticsDto> value = new ArrayList<>();
                if (!ObjectUtils.isEmpty(dtoMap)) {
                    if (dtoMap.containsKey(key)) {
                        value = dtoMap.get(key);
                    }
                }
                value.add(capacityCablingStatisticsDto);
                dtoMap.put(key, value);
            }

            if (!ObjectUtils.isEmpty(dtoMap)) {
                for (String key : dtoMap.keySet()) {
                    List<CapacityCablingStatisticsDto> value = dtoMap.get(key);
                    if (!ObjectUtils.isEmpty(value)) {
                        Integer usedPortNumber = 0;
                        Integer unusedPortNumber = 0;
                        for (CapacityCablingStatisticsDto dto : value) {
                            if (GenericCablingStatusEnum.STATUS_ONE.getStatus().equals(dto.getStatus())) {
                                //?????????
                                usedPortNumber += dto.getStatusNumber();
                            } else if (GenericCablingStatusEnum.STATUS_THREE.getStatus().equals(dto.getStatus())) {
                                //??????
                                unusedPortNumber += dto.getStatusNumber();
                            }
                        }
                        //???????????????
                        Integer allPortNumber = usedPortNumber + unusedPortNumber;
                        //???????????????
                        double percent = CalculateUtil.castPercent(usedPortNumber, allPortNumber);
                        CapacityCablingStatisticsDto info = value.get(0);
                        info.setUsedPortNumber(usedPortNumber);
                        info.setAllPortNumber(allPortNumber);
                        info.setPortPercent(percent);
                        dtoList.add(info);
                    }
                }
            }
        }
        return dtoList;
    }

    /**
     * ???????????????????????????key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto ????????????????????????
     * @return ???????????????????????????key
     */
    public String getCompareKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key  += capacityCablingStatisticsDto.getCableType();
        return key;
    }


    /**
     * ???????????????????????????key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto ????????????????????????
     * @return ???????????????????????????key
     */
    public String getKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key += capacityCablingStatisticsDto.getCableType();
        key += "-";
        key += capacityCablingStatisticsDto.getYear();
        key += "-";
        key += capacityCablingStatisticsDto.getMonth();
        return key;
    }

    /**
     * ???????????????????????????key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto ????????????????????????
     * @return ???????????????????????????key
     */
    public String getRackKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key += capacityCablingStatisticsDto.getCableType();
        key += "-";
        key += capacityCablingStatisticsDto.getConnectRackCode();
        return key;
    }



    /**
     * ????????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear ????????????
     * @param analysisMonth ????????????
     * @return  ??????????????????????????????
     */
    public List<CapacityCablingStatisticsDto> getSearchMonthRackList(Integer analysisYear, Integer analysisMonth) {
        GenericCablingListParameter parameter = new GenericCablingListParameter();
        parameter.setYear(analysisYear);
        parameter.setMonth(analysisMonth);
        List<CapacityCablingStatisticsDto> dtoList = genericCablingService.queryCablingGroupByStatusAndRack(parameter);
        return dtoList;
    }

    /**
     * ????????????????????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear ????????????
     * @param analysisMonth ????????????
     * @return  ??????????????????????????????????????????
     */
    public List<CapacityCablingStatisticsDto> getSearchCablingStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {

        CapacityAnalyzeTime capacityAnalyzeTime = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        Long nowDate = 0L;
        Long beforeYearDate = 0L;
        //???????????????????????????
        nowDate = DateUtil.generateCollectionTime(analysisMonth, analysisYear, nowDate);
        //???????????????????????????
        beforeYearDate = DateUtil.generateCollectionTime(capacityAnalyzeTime.getMonth(), capacityAnalyzeTime.getYear(), nowDate);
        GenericCablingListParameter parameter = new GenericCablingListParameter();
        parameter.setStartTime(beforeYearDate);
        parameter.setEndTime(nowDate);
        List<CapacityCablingStatisticsDto> cablingStatisticsDtoList = genericCablingService.queryCablingGroupByStatusAndDate(parameter);
        return cablingStatisticsDtoList;
    }


}
