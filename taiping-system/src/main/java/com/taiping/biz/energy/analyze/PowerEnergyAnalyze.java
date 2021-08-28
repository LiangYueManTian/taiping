package com.taiping.biz.energy.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.bean.energy.analyze.EnergyAnalyzeData;
import com.taiping.bean.energy.dto.analyze.EnergyPowerItemStatisticsDto;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.biz.capacity.analyze.SpaceAnalyze;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdRelatedViewInfoService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.enums.analyze.energy.EnergyStatisticalModelEnum;
import com.taiping.enums.analyze.energy.EnergyStatisticalTypeEnum;
import com.taiping.enums.energy.ElectricAirNameEnum;
import com.taiping.enums.energy.ElectricPowerIsChildEnum;
import com.taiping.enums.energy.ElectricPowerStatisticalEnum;
import com.taiping.enums.energy.ElectricPowerTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.NineteenUUIDUtils;
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
 * 动力能耗分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class PowerEnergyAnalyze extends AbstractEnergyAnalyze {

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
     * 动力能耗分项逻辑层
     */
    @Autowired
    private PowerEnergyItemService powerEnergyItemService;

    /**
     * 自动注入能耗逻辑层
     */
    @Autowired
    private EnergyAnalyze energyAnalyze;


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
                                                                List<EnergyPowerItemStatisticsDto> energyStatisticsDtoList) {
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
                                                              List<EnergyPowerItemStatisticsDto> energyStatisticsDtoList) {
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

        //查询一年每个暖通分项的数据
        List<EnergyPowerItemStatisticsDto> itemInfoType = this.getSearchTypeStatisticsDtoList(analysisYear, analysisMonth);

        //查询一年每个设备的数据
        List<EnergyPowerItemStatisticsDto> itemInfoData = this.getSearchDataStatisticsDtoList(analysisYear, analysisMonth);

        //将两个数据合并成一个类的数据
        List<EnergyPowerItemStatisticsDto> itemDataList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(itemInfoType)) {
            itemDataList.addAll(itemInfoType);
        }

        if (!ObjectUtils.isEmpty(itemInfoData)) {
            itemDataList.addAll(itemInfoData);
        }


        if (!ObjectUtils.isEmpty(itemDataList)) {
            for (EnergyPowerItemStatisticsDto itDataOne : itemDataList) {
                String type = this.setTypeToEnergy(itDataOne);
                itDataOne.setType(type);
            }
        }

        //当月数据
        List<EnergyPowerItemStatisticsDto> nowMonthList = itemDataList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //动力能耗分项统计信息
        List<EnergyThresholdInfo> energyThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear);

        //过滤掉不统计的数据
        energyThresholdInfoList = energyThresholdInfoList.stream().
                filter(c -> c.isStatistical()).collect(Collectors.toList());

        //计算环比
        energyThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, energyThresholdInfoList, itemDataList);
        //计算同比
        energyThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, energyThresholdInfoList, itemDataList);


        //只有主项会生成图数据
        List<EnergyThresholdInfo> viewInfo = energyThresholdInfoList.stream().
                filter(c -> ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(c.getIsChild())).collect(Collectors.toList());


        //设置父级code为属性
        energyThresholdInfoList = EnergyAnalyze.getAnalyzeInfo(viewInfo, energyThresholdInfoList);
        boolean isFilter = false;
        //计算图表显示信息
        List<EnergyThresholdRelatedViewInfo> viewInfoList = EnergyAnalyze.calculateViewInfo(analysisYear, analysisMonth, viewInfo, itemDataList, isFilter);

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
     * 能耗数据查询当前月份和去年同期月份的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<EnergyPowerItemStatisticsDto> getSearchDataStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryStatisticsGroupByData(energyParameter);
        return powerItemStatisticsDtoList;
    }

    /**
     * 能耗数据查询当前月份和去年同期月份的根据类型分组的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<EnergyPowerItemStatisticsDto> getSearchTypeStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        PowerEnergyListParameter energyParameter = this.getSearchParam(analysisYear, analysisMonth);
        List<EnergyPowerItemStatisticsDto> powerItemStatisticsDtoList = powerEnergyItemService.queryStatisticsGroupByType(energyParameter);
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
    public List<EnergyThresholdInfo> getThresholdList(List<EnergyPowerItemStatisticsDto> nowMonthList, Integer analysisMonth, Integer analysisYear) {
        List<EnergyThresholdInfo> energyInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (EnergyPowerItemStatisticsDto energyStatisticsDto : nowMonthList) {
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
    public EnergyThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, EnergyPowerItemStatisticsDto dataStatisticsDto) {
        EnergyThresholdInfo energyThresholdInfo = new EnergyThresholdInfo();
        energyThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        energyThresholdInfo.setMonth(analysisMonth);
        energyThresholdInfo.setYear(analysisYear);
        energyThresholdInfo.setModule(EnergyStatisticalModelEnum.POWER_METER.getModel());
        energyThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        String data = dataStatisticsDto.getDataCode();
        //数据名称
        String dataName = "";
        if (!ObjectUtils.isEmpty(dataStatisticsDto)) {
            //设置类型
            energyThresholdInfo.setType(dataStatisticsDto.getType());
            //设置名称和数据
            energyThresholdInfo = this.setNameAndData(data, dataName, energyThresholdInfo, dataStatisticsDto);
        }
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


    /**
     * 设置能耗分析的类型
     * @author hedongwei@wistronits.com
     * @date  2019/11/19 10:37
     * @param dataStatisticsDto 数据统计dto
     * @return 返回能耗分析的数据
     */
    public String setTypeToEnergy(EnergyPowerItemStatisticsDto dataStatisticsDto) {
        String returnInfo = "";
        if (ElectricPowerTypeEnum.REFRIGERATOR.getType().equals(dataStatisticsDto.getType())) {
            //冷机
            returnInfo = EnergyStatisticalTypeEnum.REFRIGERATOR_POWER.getType();
        } else if (ElectricPowerTypeEnum.WATER_PUMP.getType().equals(dataStatisticsDto.getType())) {
            //水泵
            returnInfo = EnergyStatisticalTypeEnum.WATER_PUMP_POWER.getType();
        } else if (ElectricPowerTypeEnum.PRECISION_AIR.getType().equals(dataStatisticsDto.getType())) {
            //精密空调
            returnInfo = EnergyStatisticalTypeEnum.PRECISION_AIR.getType();
        } else if (ElectricPowerTypeEnum.COOLING_TOWER.getType().equals(dataStatisticsDto.getType())) {
            //冷塔
            returnInfo = EnergyStatisticalTypeEnum.COOLING_TOWER.getType();
        }
        return returnInfo;
    }

    /**
     * 设置名称和数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/19 10:56
     * @param data 数据
     * @param dataName 数据名称
     * @param energyThresholdInfo 统计数据信息
     * @param dataStatisticsDto 查询到的能耗数据
     * @return 返回统计数据对象
     */
    public EnergyThresholdInfo setNameAndData(String data, String dataName, EnergyThresholdInfo energyThresholdInfo, EnergyPowerItemStatisticsDto dataStatisticsDto) {
        boolean isStatistical = true;
        //不是子集名称就是类型的信息
        if (ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(dataStatisticsDto.getIsChild())) {
            //数据就是类型
            data = energyThresholdInfo.getType();
            dataName = EnergyStatisticalTypeEnum.getTypeName(data);
        } else {
            if (EnergyStatisticalTypeEnum.PRECISION_AIR.getType().equals(dataStatisticsDto.getType())) {
                //精密空调
                if (!ElectricPowerStatisticalEnum.getResultByName(data)) {
                    isStatistical = false;
                } else {
                    dataName += ElectricAirNameEnum.getDataNameByCode(data);
                }
            } else {
                //除精密空调之外的数据
                String [] dataList = data.split("-");
                String dataInfo = "";
                Integer size = 3;
                if (dataList.length >= size) {
                    dataInfo = dataList[1] + dataList[2];
                }
                dataName += dataInfo;
                String name = dataStatisticsDto.getName();
                if (!ObjectUtils.isEmpty(name)) {
                    String empty = "";
                    dataName += name.replace(data, empty);
                }
            }
        }
        energyThresholdInfo.setThresholdData(data);
        energyThresholdInfo.setThresholdName(dataName);
        energyThresholdInfo.setStatistical(isStatistical);
        energyThresholdInfo.setIsChild(dataStatisticsDto.getIsChild());
        return energyThresholdInfo;
    }

}
