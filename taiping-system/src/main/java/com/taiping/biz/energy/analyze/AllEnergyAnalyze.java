package com.taiping.biz.energy.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyBaseStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.bean.energy.analyze.EnergyAnalyzeData;
import com.taiping.bean.energy.dto.analyze.EnergyAllStatisticsDto;
import com.taiping.bean.energy.dto.analyze.EnergyPowerItemStatisticsDto;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.biz.capacity.analyze.SpaceAnalyze;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.biz.energy.service.ElectricInstrumentService;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdRelatedViewInfoService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.enums.analyze.energy.EnergyStatisticalModelEnum;
import com.taiping.enums.analyze.energy.EnergyStatisticalTypeEnum;
import com.taiping.enums.energy.ElectricPowerIsChildEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import com.taiping.utils.common.analyze.energy.AbstractEnergyAnalyze;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * ??????????????????
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class AllEnergyAnalyze extends AbstractEnergyAnalyze {

    /**
     * ??????????????????????????????
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * ?????????????????????
     */
    @Autowired
    private EnergyThresholdInfoService thresholdInfoService;

    /**
     * ?????????????????????????????????
     */
    @Autowired
    private EnergyThresholdRelatedViewInfoService viewInfoService;


    /**
     * ??????????????????
     */
    @Autowired
    private ElectricInstrumentService electricInstrumentService;

    /**
     * ???????????????????????????
     */
    @Autowired
    private EnergyAnalyze energyAnalyze;


    /**
     * it???????????????
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * ?????????????????????
     */
    @Autowired
    private PowerEnergyItemService powerEnergyItemService;


    /**
     * ????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear ??????
     * @param analysisMonth ??????
     * @param energyThresholdInfoList ??????????????????
     * @param energyStatisticsDtoList ???????????????
     * @return ????????????
     */
    protected List<EnergyThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<EnergyThresholdInfo> energyThresholdInfoList,
                                                                List<EnergyAllStatisticsDto> energyStatisticsDtoList) {
        //????????????
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            //??????????????????
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            energyThresholdInfoList = EnergyAnalyze.castGroupInfo(energyThresholdInfoList, energyStatisticsDtoList, beforeDate, type);
        }
        return energyThresholdInfoList;
    }

    /**
     * ????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear ??????
     * @param analysisMonth ??????
     * @param energyStatisticsDtoList ???????????????
     * @param energyThresholdInfoList ??????????????????
     * @return ????????????
     */
    protected List<EnergyThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<EnergyThresholdInfo> energyThresholdInfoList,
                                                              List<EnergyAllStatisticsDto> energyStatisticsDtoList) {
        //????????????
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            //??????????????????
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            energyThresholdInfoList = EnergyAnalyze.castGroupInfo(energyThresholdInfoList, energyStatisticsDtoList, beforeDate, type);
        }
        return energyThresholdInfoList;
    }

    /**
     * ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param manageActivityList ??????????????????
     */
    @Override
    protected void insertManageActivityBatch(List<ManageActivity> manageActivityList) {
        manageActivityService.insertManageActivityNoEquals(ManageSourceEnum.ENERGY, manageActivityList);
    }

    /**
     * ????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdInfoList ????????????
     */
    @Override
    protected void insertThresholdInfoBatch(List<EnergyThresholdInfo> energyThresholdInfoList) {
        thresholdInfoService.insertThresholdInfoBatch(energyThresholdInfoList);
    }

    /**
     * ????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdRelatedViewInfoList ????????????????????????????????????
     */
    @Override
    protected void insertThresholdRelatedViewInfo(List<EnergyThresholdRelatedViewInfo> energyThresholdRelatedViewInfoList) {
        viewInfoService.insertThresholdRelatedViewInfoBatch(energyThresholdRelatedViewInfoList);
    }

    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param analysisYear ???????????????
     * @param analysisMonth ???????????????
     * @return ???????????????????????????????????????
     */
    @Override
    protected EnergyAnalyzeData analyzeDataRealize(Integer analysisYear, Integer analysisMonth) {

        //??????????????????it???????????????
        List<ItEnergyBaseStatisticsDto> itemInfoType = this.getSearchItStatisticsDtoList(analysisYear, analysisMonth);

        //????????????????????????
        List<EnergyPowerItemStatisticsDto> heatInfoData = this.getSearchHeatStatisticsDtoList(analysisYear, analysisMonth);

        //???????????????????????????
        List<EnergyPowerItemStatisticsDto> allData = this.getSearchAllPowerStatisticsDtoList(analysisYear, analysisMonth);

        //?????????????????????
        List<EnergyAllStatisticsDto> notIsChildList = this.getNotIsChildList(itemInfoType, allData);

        //???????????????
        List<EnergyAllStatisticsDto> allChildData = this.getAllChildEnergyList(heatInfoData, allData);


        List<EnergyAllStatisticsDto> itemInfoDataList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(notIsChildList)) {
            itemInfoDataList.addAll(notIsChildList);
        }

        if (!ObjectUtils.isEmpty(allChildData)) {
            itemInfoDataList.addAll(allChildData);
        }

        //????????????
        List<EnergyAllStatisticsDto> nowMonthList = itemInfoDataList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //??????????????????????????????
        List<EnergyThresholdInfo> energyThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear);

        //???????????????????????????
        energyThresholdInfoList = energyThresholdInfoList.stream().
                filter(c -> c.isStatistical()).collect(Collectors.toList());

        //????????????
        energyThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, energyThresholdInfoList, itemInfoDataList);
        //????????????
        energyThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, energyThresholdInfoList, itemInfoDataList);


        //??????????????????????????????
        List<EnergyThresholdInfo> viewInfo = energyThresholdInfoList.stream().
                filter(c -> ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(c.getIsChild())).collect(Collectors.toList());


        //????????????code?????????
        energyThresholdInfoList = EnergyAnalyze.getAnalyzeInfo(viewInfo, energyThresholdInfoList);

        boolean isFilter = false;
        //????????????????????????
        List<EnergyThresholdRelatedViewInfo> viewInfoList = EnergyAnalyze.calculateViewInfo(analysisYear, analysisMonth, viewInfo, itemInfoDataList, isFilter);

        //??????????????????
        energyThresholdInfoList = EnergyAnalyze.setAdviceToThreshold(analysisYear, analysisMonth, energyThresholdInfoList);

        //???????????????????????????
        List<ManageActivity> manageActivityList = energyAnalyze.generateManageActivity(viewInfo);
        EnergyAnalyzeData energyAnalyzeData = new EnergyAnalyzeData();
        energyAnalyzeData.setThresholdInfoList(energyThresholdInfoList);
        energyAnalyzeData.setRelatedViewInfoList(viewInfoList);
        energyAnalyzeData.setManageActivityList(manageActivityList);
        return energyAnalyzeData;
    }


    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/22 9:42
     * @param heatInfoData ??????????????????
     * @param allData ?????????????????????
     * @return ??????????????????????????????
     */
    public List<EnergyAllStatisticsDto> getAllChildEnergyList(List<EnergyPowerItemStatisticsDto> heatInfoData, List<EnergyPowerItemStatisticsDto> allData) {
        List<EnergyAllStatisticsDto> allChildData = new ArrayList<>();
        //???????????? = ??????????????? - it??????
        if (!ObjectUtils.isEmpty(heatInfoData) && !ObjectUtils.isEmpty(allData)) {
            for (EnergyPowerItemStatisticsDto heatOne : heatInfoData) {
                Integer compareYear = heatOne.getYear();
                Integer compareMonth = heatOne.getMonth();
                double diffMeter = heatOne.getGrowthElectricMeter();
                EnergyAllStatisticsDto heatDto = new EnergyAllStatisticsDto();
                BeanUtils.copyProperties(heatOne, heatDto);
                heatDto = getAllStatisticsChildParam(heatDto, EnergyStatisticalTypeEnum.HEAT_ITEM, EnergyStatisticalTypeEnum.POWER_ENERGY, diffMeter);
                allChildData.add(heatDto);
                for (EnergyPowerItemStatisticsDto energyPowerItemStatisticsDto : allData) {
                    double allMeter = energyPowerItemStatisticsDto.getGrowthElectricMeter();
                    Integer year = energyPowerItemStatisticsDto.getYear();
                    Integer month = energyPowerItemStatisticsDto.getMonth();
                    if (year.equals(compareYear) && month.equals(compareMonth)) {
                        double value = allMeter - diffMeter;
                        EnergyAllStatisticsDto allDto = new EnergyAllStatisticsDto();
                        BeanUtils.copyProperties(energyPowerItemStatisticsDto, allDto);
                        allDto = getAllStatisticsChildParam(allDto, EnergyStatisticalTypeEnum.OTHER, EnergyStatisticalTypeEnum.POWER_ENERGY, value);
                        allChildData.add(allDto);
                        break;
                    }
                }
            }
        }
        return allChildData;
    }

    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/12/2 11:32
     * @param itemInfoType ????????????
     * @param allData ????????????
     * @return ???????????????????????????
     */
    public List<EnergyAllStatisticsDto> getNotIsChildList(List<ItEnergyBaseStatisticsDto> itemInfoType, List<EnergyPowerItemStatisticsDto> allData) {
        //it?????? + ??????????????? = ?????????????????????????????????
        List<EnergyAllStatisticsDto> energyAllStatisticsDtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(itemInfoType)) {
            for (ItEnergyBaseStatisticsDto itOne : itemInfoType) {
                double allMeter = 0;
                //??????it????????????
                EnergyAllStatisticsDto energyItStatisticsDto = new EnergyAllStatisticsDto();
                BeanUtils.copyProperties(itOne, energyItStatisticsDto);
                double itMeterInfo = energyItStatisticsDto.getGrowthElectricMeter();
                energyItStatisticsDto = this.getNotIsChildParam(energyItStatisticsDto, EnergyStatisticalTypeEnum.IT_ENERGY, itMeterInfo);
                energyAllStatisticsDtoList.add(energyItStatisticsDto);
                if (!ObjectUtils.isEmpty(allData)) {
                    for (EnergyPowerItemStatisticsDto itemOne : allData) {
                        if (itOne.getYear().equals(itemOne.getYear()) && itOne.getMonth().equals(itemOne.getMonth())) {

                            //????????????????????????
                            EnergyAllStatisticsDto energyPowerStatisticsDto = new EnergyAllStatisticsDto();
                            BeanUtils.copyProperties(itemOne, energyPowerStatisticsDto);
                            double powerMeter = energyPowerStatisticsDto.getGrowthElectricMeter();
                            energyPowerStatisticsDto = this.getNotIsChildParam(energyPowerStatisticsDto, EnergyStatisticalTypeEnum.POWER_ENERGY, powerMeter);
                            energyAllStatisticsDtoList.add(energyPowerStatisticsDto);

                            //????????????
                            EnergyAllStatisticsDto energyAllStatisticsDto = new EnergyAllStatisticsDto();
                            allMeter = itMeterInfo + powerMeter;
                            BeanUtils.copyProperties(itemOne, energyAllStatisticsDto);
                            energyAllStatisticsDto = this.getNotIsChildParam(energyAllStatisticsDto, EnergyStatisticalTypeEnum.ALL_ENERGY, allMeter);
                            energyAllStatisticsDtoList.add(energyAllStatisticsDto);

                            //??????pue???
                            EnergyAllStatisticsDto pueStatisticsDto = new EnergyAllStatisticsDto();
                            BeanUtils.copyProperties(itemOne, pueStatisticsDto);
                            //pue??? = (it?????? + ???????????????)/ it??????
                            double pue = CalculateUtil.castPercent(allMeter, itMeterInfo);
                            pueStatisticsDto = this.getNotIsChildParam(pueStatisticsDto, EnergyStatisticalTypeEnum.PUE, pue);
                            energyAllStatisticsDtoList.add(pueStatisticsDto);
                        }
                    }
                }
            }
        }
        return energyAllStatisticsDtoList;
    }

    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/12/2 11:34
     * @param energyAllStatisticsDto  ????????????dto
     * @param electricShowTypeEnum ?????????????????????
     * @param allMeter ????????????
     * @return ???????????????????????????
     */
    public EnergyAllStatisticsDto getNotIsChildParam(EnergyAllStatisticsDto energyAllStatisticsDto , EnergyStatisticalTypeEnum electricShowTypeEnum ,double allMeter) {
        energyAllStatisticsDto.setDataCode(electricShowTypeEnum.getType());
        energyAllStatisticsDto.setIsChild(ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode());
        energyAllStatisticsDto.setType(electricShowTypeEnum.getType());
        energyAllStatisticsDto.setDataName(electricShowTypeEnum.getTypeName());
        energyAllStatisticsDto.setName(electricShowTypeEnum.getTypeName());
        energyAllStatisticsDto.setGrowthElectricMeter(allMeter);
        return energyAllStatisticsDto;
    }

    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/22 9:30
     * @param dto ????????????dto
     * @param electricShowPowerTypeEnum ???????????????
     * @param value ????????????
     * @return
     */
    public EnergyAllStatisticsDto getAllStatisticsChildParam(EnergyAllStatisticsDto dto, EnergyStatisticalTypeEnum electricShowPowerTypeEnum, EnergyStatisticalTypeEnum energyStatisticalTypeEnum, double value) {
        dto.setIsChild(ElectricPowerIsChildEnum.IS_CHILD.getCode());
        dto.setType(energyStatisticalTypeEnum.getType());
        dto.setDataCode(electricShowPowerTypeEnum.getType());
        dto.setDataName(electricShowPowerTypeEnum.getTypeName());
        dto.setName(electricShowPowerTypeEnum.getTypeName());
        dto.setGrowthElectricMeter(value);
        return dto;
    }


    /**
     * ??????it????????????????????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear ????????????
     * @param analysisMonth ????????????
     * @return  ??????????????????????????????????????????
     */
    public List<ItEnergyBaseStatisticsDto> getSearchItStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        ItEnergyListParameter itEnergyListParameter = new ItEnergyListParameter();
        BeanUtils.copyProperties(energyParameter, itEnergyListParameter);
        List<ItEnergyBaseStatisticsDto> itEnergyBaseDtoList = itEnergyCurrentService.queryItEnergyInfoGroupByDate(itEnergyListParameter);
        return itEnergyBaseDtoList;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear ????????????
     * @param analysisMonth ????????????
     * @return  ??????????????????????????????????????????
     */
    public List<EnergyPowerItemStatisticsDto> getSearchHeatStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryPowerItemMeterForHeat(energyParameter);
        return powerItemStatisticsDtoList;
    }

    /**
     * ???????????????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 17:05
     * @param analysisYear ??????
     * @param analysisMonth  ??????
     * @return ????????????????????????????????????
     */
    public List<EnergyPowerItemStatisticsDto> getSearchAllPowerStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryPowerItemMeterForAllItem(energyParameter);
        return powerItemStatisticsDtoList;
    }


    /**
     * ?????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:35
     * @param analysisYear ??????
     * @param analysisMonth ??????
     * @return  ?????????????????????
     */
    public PowerEnergyListParameter getSearchParam(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = SpaceAnalyze.generateSearchParam(analysisYear, analysisMonth);
        PowerEnergyListParameter energyParameter = new PowerEnergyListParameter();
        BeanUtils.copyProperties(parameter, energyParameter);
        return energyParameter;
    }


    /**
     * ??????????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:04
     * @param nowMonthList ?????????????????????
     * @param analysisMonth ??????
     * @param analysisYear ??????
     * @return ??????????????????????????????
     */
    public List<EnergyThresholdInfo> getThresholdList(List<EnergyAllStatisticsDto> nowMonthList, Integer analysisMonth, Integer analysisYear) {
        List<EnergyThresholdInfo> energyInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (EnergyAllStatisticsDto energyStatisticsDto : nowMonthList) {
                EnergyThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, energyStatisticsDto);
                energyInfoList.add(capacityThresholdInfo);
            }
        }
        return energyInfoList;
    }



    /**
     * ???????????????????????????
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 23:04
     * @param analysisMonth ????????????
     * @param analysisYear  ????????????
     * @param dataStatisticsDto ??????
     * @return ???????????????????????????
     */
    public EnergyThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, EnergyAllStatisticsDto dataStatisticsDto) {
        EnergyThresholdInfo energyThresholdInfo = new EnergyThresholdInfo();
        energyThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        energyThresholdInfo.setMonth(analysisMonth);
        energyThresholdInfo.setYear(analysisYear);
        if (EnergyStatisticalTypeEnum.PUE.getType().equals(dataStatisticsDto.getDataCode())) {
            energyThresholdInfo.setModule(EnergyStatisticalModelEnum.PUE.getModel());
        } else {
            energyThresholdInfo.setModule(EnergyStatisticalModelEnum.ALL_METER.getModel());
        }
        energyThresholdInfo.setStatistical(true);
        energyThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        energyThresholdInfo.setIsChild(dataStatisticsDto.getIsChild());
        String data = dataStatisticsDto.getDataCode();
        if (!ObjectUtils.isEmpty(dataStatisticsDto)) {
            energyThresholdInfo.setType(dataStatisticsDto.getType());
            energyThresholdInfo.setThresholdName(dataStatisticsDto.getName());
        }
        energyThresholdInfo.setThresholdData(data);
        energyThresholdInfo.setThresholdValue(String.valueOf(dataStatisticsDto.getGrowthElectricMeter()));
        //??????
        String cause = "";
        cause += energyThresholdInfo.getThresholdName();
        cause += "??????????????????";
        energyThresholdInfo.setCause(cause);
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, collectionTime);
        energyThresholdInfo.setDataCollectionTime(collectionTime);
        Long nowDate = System.currentTimeMillis();
        energyThresholdInfo.setCreateTime(nowDate);
        return energyThresholdInfo;
    }

}
