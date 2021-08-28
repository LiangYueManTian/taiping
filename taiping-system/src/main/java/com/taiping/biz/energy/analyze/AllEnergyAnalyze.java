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
 * 总能耗分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class AllEnergyAnalyze extends AbstractEnergyAnalyze {

    /**
     * 运维管理活动项逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 能耗信息逻辑层
     */
    @Autowired
    private EnergyThresholdInfoService thresholdInfoService;

    /**
     * 能耗关联显示信息逻辑层
     */
    @Autowired
    private EnergyThresholdRelatedViewInfoService viewInfoService;


    /**
     * 总能耗逻辑层
     */
    @Autowired
    private ElectricInstrumentService electricInstrumentService;

    /**
     * 自动注入能耗逻辑层
     */
    @Autowired
    private EnergyAnalyze energyAnalyze;


    /**
     * it能耗逻辑层
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * 动力能耗逻辑层
     */
    @Autowired
    private PowerEnergyItemService powerEnergyItemService;


    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param energyThresholdInfoList 统计信息集合
     * @param energyStatisticsDtoList 一年的数据
     * @return 返回同比
     */
    protected List<EnergyThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<EnergyThresholdInfo> energyThresholdInfoList,
                                                                List<EnergyAllStatisticsDto> energyStatisticsDtoList) {
        //计算同比
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            energyThresholdInfoList = EnergyAnalyze.castGroupInfo(energyThresholdInfoList, energyStatisticsDtoList, beforeDate, type);
        }
        return energyThresholdInfoList;
    }

    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param energyStatisticsDtoList 一年的数据
     * @param energyThresholdInfoList 统计信息集合
     * @return 返回环比
     */
    protected List<EnergyThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<EnergyThresholdInfo> energyThresholdInfoList,
                                                              List<EnergyAllStatisticsDto> energyStatisticsDtoList) {
        //计算环比
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            //上月同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            energyThresholdInfoList = EnergyAnalyze.castGroupInfo(energyThresholdInfoList, energyStatisticsDtoList, beforeDate, type);
        }
        return energyThresholdInfoList;
    }

    /**
     * 新增运维管理活动信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param manageActivityList 运维管理活动
     */
    @Override
    protected void insertManageActivityBatch(List<ManageActivity> manageActivityList) {
        manageActivityService.insertManageActivityNoEquals(ManageSourceEnum.ENERGY, manageActivityList);
    }

    /**
     * 新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdInfoList 能耗阈值
     */
    @Override
    protected void insertThresholdInfoBatch(List<EnergyThresholdInfo> energyThresholdInfoList) {
        thresholdInfoService.insertThresholdInfoBatch(energyThresholdInfoList);
    }

    /**
     * 新增能耗阈值关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdRelatedViewInfoList 能耗阈值关联显示信息集合
     */
    @Override
    protected void insertThresholdRelatedViewInfo(List<EnergyThresholdRelatedViewInfo> energyThresholdRelatedViewInfoList) {
        viewInfoService.insertThresholdRelatedViewInfoBatch(energyThresholdRelatedViewInfoList);
    }

    /**
     * 自定义实现分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param analysisYear 分析的年份
     * @param analysisMonth 分析的月份
     * @return 返回需要新增的分析数据信息
     */
    @Override
    protected EnergyAnalyzeData analyzeDataRealize(Integer analysisYear, Integer analysisMonth) {

        //查询一年每个it分项的数据
        List<ItEnergyBaseStatisticsDto> itemInfoType = this.getSearchItStatisticsDtoList(analysisYear, analysisMonth);

        //查询暖通动力能耗
        List<EnergyPowerItemStatisticsDto> heatInfoData = this.getSearchHeatStatisticsDtoList(analysisYear, analysisMonth);

        //查询动力总能耗数据
        List<EnergyPowerItemStatisticsDto> allData = this.getSearchAllPowerStatisticsDtoList(analysisYear, analysisMonth);

        //不是子项的集合
        List<EnergyAllStatisticsDto> notIsChildList = this.getNotIsChildList(itemInfoType, allData);

        //子项的集合
        List<EnergyAllStatisticsDto> allChildData = this.getAllChildEnergyList(heatInfoData, allData);


        List<EnergyAllStatisticsDto> itemInfoDataList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(notIsChildList)) {
            itemInfoDataList.addAll(notIsChildList);
        }

        if (!ObjectUtils.isEmpty(allChildData)) {
            itemInfoDataList.addAll(allChildData);
        }

        //当月数据
        List<EnergyAllStatisticsDto> nowMonthList = itemInfoDataList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //动力能耗分项统计信息
        List<EnergyThresholdInfo> energyThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear);

        //过滤掉不统计的数据
        energyThresholdInfoList = energyThresholdInfoList.stream().
                filter(c -> c.isStatistical()).collect(Collectors.toList());

        //计算环比
        energyThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, energyThresholdInfoList, itemInfoDataList);
        //计算同比
        energyThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, energyThresholdInfoList, itemInfoDataList);


        //只有主项会生成图数据
        List<EnergyThresholdInfo> viewInfo = energyThresholdInfoList.stream().
                filter(c -> ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(c.getIsChild())).collect(Collectors.toList());


        //设置父级code为属性
        energyThresholdInfoList = EnergyAnalyze.getAnalyzeInfo(viewInfo, energyThresholdInfoList);

        boolean isFilter = false;
        //计算图表显示信息
        List<EnergyThresholdRelatedViewInfo> viewInfoList = EnergyAnalyze.calculateViewInfo(analysisYear, analysisMonth, viewInfo, itemInfoDataList, isFilter);

        //形成建议信息
        energyThresholdInfoList = EnergyAnalyze.setAdviceToThreshold(analysisYear, analysisMonth, energyThresholdInfoList);

        //形成运维管理活动项
        List<ManageActivity> manageActivityList = energyAnalyze.generateManageActivity(viewInfo);
        EnergyAnalyzeData energyAnalyzeData = new EnergyAnalyzeData();
        energyAnalyzeData.setThresholdInfoList(energyThresholdInfoList);
        energyAnalyzeData.setRelatedViewInfoList(viewInfoList);
        energyAnalyzeData.setManageActivityList(manageActivityList);
        return energyAnalyzeData;
    }


    /**
     * 获取总能耗子项集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/22 9:42
     * @param heatInfoData 暖通分项数据
     * @param allData 总动力能耗数据
     * @return 返回总子项能耗的集合
     */
    public List<EnergyAllStatisticsDto> getAllChildEnergyList(List<EnergyPowerItemStatisticsDto> heatInfoData, List<EnergyPowerItemStatisticsDto> allData) {
        List<EnergyAllStatisticsDto> allChildData = new ArrayList<>();
        //其他能耗 = 总动力能耗 - it能耗
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
     * 获取不是子项的集合
     * @author hedongwei@wistronits.com
     * @date  2019/12/2 11:32
     * @param itemInfoType 子项类型
     * @param allData 全部数据
     * @return 返回不是子项的集合
     */
    public List<EnergyAllStatisticsDto> getNotIsChildList(List<ItEnergyBaseStatisticsDto> itemInfoType, List<EnergyPowerItemStatisticsDto> allData) {
        //it能耗 + 动力总能耗 = 产生运维管理活动的数据
        List<EnergyAllStatisticsDto> energyAllStatisticsDtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(itemInfoType)) {
            for (ItEnergyBaseStatisticsDto itOne : itemInfoType) {
                double allMeter = 0;
                //存储it能耗数据
                EnergyAllStatisticsDto energyItStatisticsDto = new EnergyAllStatisticsDto();
                BeanUtils.copyProperties(itOne, energyItStatisticsDto);
                double itMeterInfo = energyItStatisticsDto.getGrowthElectricMeter();
                energyItStatisticsDto = this.getNotIsChildParam(energyItStatisticsDto, EnergyStatisticalTypeEnum.IT_ENERGY, itMeterInfo);
                energyAllStatisticsDtoList.add(energyItStatisticsDto);
                if (!ObjectUtils.isEmpty(allData)) {
                    for (EnergyPowerItemStatisticsDto itemOne : allData) {
                        if (itOne.getYear().equals(itemOne.getYear()) && itOne.getMonth().equals(itemOne.getMonth())) {

                            //存储动力能耗数据
                            EnergyAllStatisticsDto energyPowerStatisticsDto = new EnergyAllStatisticsDto();
                            BeanUtils.copyProperties(itemOne, energyPowerStatisticsDto);
                            double powerMeter = energyPowerStatisticsDto.getGrowthElectricMeter();
                            energyPowerStatisticsDto = this.getNotIsChildParam(energyPowerStatisticsDto, EnergyStatisticalTypeEnum.POWER_ENERGY, powerMeter);
                            energyAllStatisticsDtoList.add(energyPowerStatisticsDto);

                            //计算总值
                            EnergyAllStatisticsDto energyAllStatisticsDto = new EnergyAllStatisticsDto();
                            allMeter = itMeterInfo + powerMeter;
                            BeanUtils.copyProperties(itemOne, energyAllStatisticsDto);
                            energyAllStatisticsDto = this.getNotIsChildParam(energyAllStatisticsDto, EnergyStatisticalTypeEnum.ALL_ENERGY, allMeter);
                            energyAllStatisticsDtoList.add(energyAllStatisticsDto);

                            //计算pue值
                            EnergyAllStatisticsDto pueStatisticsDto = new EnergyAllStatisticsDto();
                            BeanUtils.copyProperties(itemOne, pueStatisticsDto);
                            //pue值 = (it能耗 + 动力总能耗)/ it能耗
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
     * 获取不是子项的参数
     * @author hedongwei@wistronits.com
     * @date  2019/12/2 11:34
     * @param energyAllStatisticsDto  能耗统计dto
     * @param electricShowTypeEnum 统计查询枚举类
     * @param allMeter 全部电量
     * @return 获取不是子项的参数
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
     * 获取子集的菜单信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/22 9:30
     * @param dto 能耗统计dto
     * @param electricShowPowerTypeEnum 显示的类别
     * @param value 具体的值
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
     * 查询it能耗数据查询当前月份和去年同期月份的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<ItEnergyBaseStatisticsDto> getSearchItStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        ItEnergyListParameter itEnergyListParameter = new ItEnergyListParameter();
        BeanUtils.copyProperties(energyParameter, itEnergyListParameter);
        List<ItEnergyBaseStatisticsDto> itEnergyBaseDtoList = itEnergyCurrentService.queryItEnergyInfoGroupByDate(itEnergyListParameter);
        return itEnergyBaseDtoList;
    }

    /**
     * 能耗数据查询当前月份和去年同期月份的根据是暖通分项分组的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<EnergyPowerItemStatisticsDto> getSearchHeatStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryPowerItemMeterForHeat(energyParameter);
        return powerItemStatisticsDtoList;
    }

    /**
     * 获取查询所有动力能耗的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 17:05
     * @param analysisYear 年份
     * @param analysisMonth  月份
     * @return 查询所有动力能耗统计数据
     */
    public List<EnergyPowerItemStatisticsDto> getSearchAllPowerStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryPowerItemMeterForAllItem(energyParameter);
        return powerItemStatisticsDtoList;
    }


    /**
     * 获取查询的参数
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:35
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @return  返回查询的参数
     */
    public PowerEnergyListParameter getSearchParam(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = SpaceAnalyze.generateSearchParam(analysisYear, analysisMonth);
        PowerEnergyListParameter energyParameter = new PowerEnergyListParameter();
        BeanUtils.copyProperties(parameter, energyParameter);
        return energyParameter;
    }


    /**
     * 获取运维管理活动信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:04
     * @param nowMonthList 当前数据的时间
     * @param analysisMonth 月份
     * @param analysisYear 年份
     * @return 返回运维管理活动信息
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
     * 获取单个对象的信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 23:04
     * @param analysisMonth 统计月份
     * @param analysisYear  统计年份
     * @param dataStatisticsDto 数据
     * @return 返回单个对象的信息
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
        //原因
        String cause = "";
        cause += energyThresholdInfo.getThresholdName();
        cause += "运维管理活动";
        energyThresholdInfo.setCause(cause);
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, collectionTime);
        energyThresholdInfo.setDataCollectionTime(collectionTime);
        Long nowDate = System.currentTimeMillis();
        energyThresholdInfo.setCreateTime(nowDate);
        return energyThresholdInfo;
    }

}
