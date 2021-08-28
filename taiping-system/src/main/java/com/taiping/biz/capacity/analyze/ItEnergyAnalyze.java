package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyBaseStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdCapacityEnum;
import com.taiping.enums.analyze.capacity.ThresholdModuleEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.cabinet.ItModuleEnum;
import com.taiping.enums.cabinet.ItTypeEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
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
 * it 能耗分析
 * @author hedongwei@wistronits.com
 * @date 2019/11/8 16:17
 */
@Component
public class ItEnergyAnalyze {

    /**
     * it能耗逻辑层
     */
    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;

    /**
     * 容量分析
     */
    @Autowired
    private CapacityAnalyze capacityAnalyze;

    /**
     * 返回需要计算同比和环比的阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:40
     * @param analysisYear 统计的年份
     * @param analysisMonth 统计的月份
     * @param capacityThresholdInfoList  容量阈值信息集合
     * @param dataStatisticsDtoList 查询容量数据集合
     * @param beforeDate 需要统计的时间那一年那一月
     * @param type 类型
     * @return 返回计算同比和环比的阈值信息
     */
    public static List<CapacityThresholdInfo> castGroupInfo(Integer analysisYear, Integer analysisMonth,
                                                     List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                     List<ItEnergyBaseStatisticsDto> dataStatisticsDtoList,
                                                     CapacityAnalyzeTime beforeDate, String type, ThresholdTypeEnum thresholdType) {
        List<ItEnergyBaseStatisticsDto> beforeList = dataStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(beforeDate.getMonth()) && c.getYear().equals(beforeDate.getYear())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(beforeList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                double usedNumber = capacityThresholdInfo.getUsedNumber();
                String dataValue = capacityThresholdInfo.getThresholdData();
                //增长率
                double growthPercent = 0;
                for (ItEnergyBaseStatisticsDto itStatisticsDto : beforeList) {
                    String module = itStatisticsDto.getModule();
                    String deviceType = itStatisticsDto.getType();
                    String compareDataValue = module + "-" + deviceType;
                    if (dataValue.equals(compareDataValue)) {
                        //同比环比
                        double compareUsedNumber = itStatisticsDto.getGrowthElectricMeter();
                        growthPercent = CapacityAnalyze.castPercentInfo(usedNumber, compareUsedNumber, compareUsedNumber);
                        break;
                    }
                }
                if ("1".equals(type)) {
                    //同比
                    capacityThresholdInfo.setYearOverYearPercent(growthPercent);
                } else if ("2".equals(type)) {
                    //环比
                    capacityThresholdInfo.setRingGrowth(growthPercent);
                }
            }
        }
        return capacityThresholdInfoList;
    }



    /**
     * 计算显示图像数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回显示图像数据
     */
    protected  List<CapacityThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                        List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                        List<ItEnergyBaseStatisticsDto> itStatisticsDtoList, ThresholdTypeEnum thresholdTypeEnum) {

        //因为查询出的数据今年当月的数据到去年同期的数据，所以统计的去年同期的数据需要删除
        List<CapacityThresholdRelatedViewInfo> viewInfoList = CapacityAnalyze.getDefaultViewData(analysisYear, analysisMonth, capacityThresholdInfoList);
        //需要进行趋势预测的数据信息
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //需要往默认的数据中塞值
        for (ItEnergyBaseStatisticsDto capacityStatisticsDto : itStatisticsDtoList) {
            Integer dataYear = capacityStatisticsDto.getYear();
            Integer dataMonth = capacityStatisticsDto.getMonth();
            String module = capacityStatisticsDto.getModule();
            String deviceType = capacityStatisticsDto.getType();
            String compareDataValue = module + "-" + deviceType;
            String value = compareDataValue;
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = viewInfo.getDataCode();
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    //it能耗电力容量百分比
                    double spacePercent = capacityStatisticsDto.getDataPercent();
                    viewInfo.setValue(spacePercent);
                }
            }
        }

        dataViewMap = CapacityAnalyze.getDataViewMap(viewInfoList);

        //返回填充预测数据之后的值
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = CapacityAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }



    /**
     * 获取阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:04
     * @param nowMonthList 当前数据的时间
     * @param analysisMonth 月份
     * @param analysisYear 年份
     * @param systemSettingList 系统设置
     * @param thresholdTypeEnum 阈值类型
     * @return 返回阈值信息
     */
    public List<CapacityThresholdInfo> getThresholdList(List<ItEnergyBaseStatisticsDto> nowMonthList, Integer analysisMonth, Integer analysisYear, List<SystemSetting> systemSettingList, ThresholdTypeEnum thresholdTypeEnum) {
        List<CapacityThresholdInfo> capacityThresholdInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (ItEnergyBaseStatisticsDto capacityItStatisticsDto : nowMonthList) {
                //模块占比
                double percent = capacityItStatisticsDto.getDataPercent();
                AdviceBean adviceBean = CapacityAnalyze.getAdviceType(systemSettingList, percent, thresholdTypeEnum);
                if (!ObjectUtils.isEmpty(adviceBean)) {
                    if (!ObjectUtils.isEmpty(adviceBean.getAdviceType())) {
                        CapacityThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, capacityItStatisticsDto, adviceBean, thresholdTypeEnum);
                        capacityThresholdInfoList.add(capacityThresholdInfo);
                    }
                }
            }
        }
        return capacityThresholdInfoList;
    }

    /**
     * 获得一个阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/8 10:22
     * @param analysisMonth 月份
     * @param analysisYear 年份
     * @param capacityItStatisticsDto it能耗阈值信息
     * @param adviceBean 建议对象
     * @param thresholdType  阈值类型
     */
    public CapacityThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, ItEnergyBaseStatisticsDto capacityItStatisticsDto, AdviceBean adviceBean, ThresholdTypeEnum thresholdType) {
        CapacityThresholdInfo capacityThresholdInfo = new CapacityThresholdInfo();
        capacityThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setMonth(analysisMonth);
        capacityThresholdInfo.setYear(analysisYear);
        capacityThresholdInfo.setType(thresholdType.getType());
        capacityThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setModule(ThresholdModuleEnum.ELECTRIC.getModule());
        String data = capacityItStatisticsDto.getModule() + "-" + capacityItStatisticsDto.getType();
        capacityThresholdInfo.setThresholdData(data);
        capacityThresholdInfo.setThresholdName(ItModuleEnum.getModuleByCode(capacityItStatisticsDto.getModule()) + "-" + ItTypeEnum.getTypeByCode(capacityItStatisticsDto.getType()));
        capacityThresholdInfo.setUsedNumber(capacityItStatisticsDto.getGrowthElectricMeter());
        capacityThresholdInfo.setThresholdValue(String.valueOf(capacityItStatisticsDto.getDataPercent()));
        capacityThresholdInfo.setAdviceType(adviceBean.getAdviceType());
        capacityThresholdInfo.setCause(adviceBean.getActivityCause());
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, collectionTime);
        capacityThresholdInfo.setDataCollectionTime(collectionTime);
        Long nowDate = System.currentTimeMillis();
        capacityThresholdInfo.setCreateTime(nowDate);
        return capacityThresholdInfo;
    }


    /**
     * 转换系统设置为map
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 18:07
     * @param systemSettingList 系统设置集合
     * @return 返回系统设置的map
     */
    public static Map<String, SystemSetting> castSystemListToMap(List<SystemSetting> systemSettingList) {
        Map<String, SystemSetting> systemSettingMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(systemSettingList)) {
            for (SystemSetting systemSetting : systemSettingList) {
                systemSettingMap.put(systemSetting.getCode(), systemSetting);
            }
        }
        return systemSettingMap;
    }


    /**
     * 在it能耗数据中设置比例信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 17:57
     * @param dtoList
     * @param systemSettingList
     */
    public static List<ItEnergyBaseStatisticsDto> setPercentToData(List<ItEnergyBaseStatisticsDto> dtoList, List<SystemSetting> systemSettingList) {
        Map<String, SystemSetting> systemSettingMap = ItEnergyAnalyze.castSystemListToMap(systemSettingList);
        if (!ObjectUtils.isEmpty(dtoList)) {
            for (ItEnergyBaseStatisticsDto capacitySpaceStatisticsDto : dtoList) {
                double percent = 0;
                if (ItModuleEnum.MODULE_ONE.getModule().equals(capacitySpaceStatisticsDto.getModule())) {
                    percent = ItEnergyAnalyze.calculatePercent(capacitySpaceStatisticsDto, ItModuleEnum.MODULE_ONE, systemSettingMap);
                } else if (ItModuleEnum.MODULE_TWO.getModule().equals(capacitySpaceStatisticsDto.getModule())) {
                    percent = ItEnergyAnalyze.calculatePercent(capacitySpaceStatisticsDto, ItModuleEnum.MODULE_TWO, systemSettingMap);
                }
                capacitySpaceStatisticsDto.setDataPercent(percent);
            }
        }
        return dtoList;
    }


    /**
     * 计算比例
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 17:58
     * @param capacityStatisticsDto it能耗dto
     * @param moduleEnum 模块枚举
     * @param systemSettingMap 系统设置Map
     * @return 返回比例信息
     */
    public static double calculatePercent(ItEnergyBaseStatisticsDto capacityStatisticsDto, ItModuleEnum moduleEnum, Map<String, SystemSetting> systemSettingMap) {
        double allNumber = 0;
        String code = "";
        if (ItTypeEnum.TYPE_ONE.getType().equals(capacityStatisticsDto.getType())) {
            if (ItModuleEnum.MODULE_ONE.getModule().equals(moduleEnum.getModule())) {
                //模块一变压器的总数
                code = "109010";
            } else if (ItModuleEnum.MODULE_TWO.getModule().equals(moduleEnum.getModule())) {
                //模块二变压器的总数
                code = "109030";
            }
        } else if (ItTypeEnum.TYPE_TWO.getType().equals(capacityStatisticsDto.getType())) {
            if (ItModuleEnum.MODULE_ONE.getModule().equals(moduleEnum.getModule())) {
                //模块一ups的总数
                code = "109020";
            } else if (ItModuleEnum.MODULE_TWO.getModule().equals(moduleEnum.getModule())) {
                //模块二ups的总数
                code = "109040";
            }
        }
        allNumber = Double.valueOf(systemSettingMap.get(code).getValue().toString());
        double dataValue = capacityStatisticsDto.getGrowthElectricMeter();
        return CalculateUtil.castPercent(dataValue, allNumber);
    }

    /**
     * 容量模块数据查询当前月份和去年同期月份之间的时间段的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<ItEnergyBaseStatisticsDto> getSearchModelList(Integer analysisYear, Integer analysisMonth, ThresholdTypeEnum thresholdTypeEnum) {
        CapacityAnalyzeTime capacityAnalyzeTime = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        Long nowDate = 0L;
        Long beforeYearDate = 0L;
        //当前年份月份的时间
        nowDate = DateUtil.generateCollectionTime(analysisMonth, analysisYear, nowDate);
        //去年年份月份的时间
        beforeYearDate = DateUtil.generateCollectionTime(capacityAnalyzeTime.getMonth(), capacityAnalyzeTime.getYear(), nowDate);
        ItEnergyListParameter parameter = new ItEnergyListParameter();
        parameter.setStartTime(beforeYearDate);
        parameter.setEndTime(nowDate);
        String type = "";
        if (ThresholdTypeEnum.MODULE.getType().equals(thresholdTypeEnum.getType())) {
            type = ItTypeEnum.TYPE_ONE.getType();
        } else if (ThresholdTypeEnum.UPS.getType().equals(thresholdTypeEnum.getType())) {
            type = ItTypeEnum.TYPE_TWO.getType();
        }
        parameter.setType(type);
        List<ItEnergyBaseStatisticsDto> itEnergyBaseStatisticsDtoList = itEnergyCurrentService.queryItEnergyGroupByModuleAndDate(parameter);
        return itEnergyBaseStatisticsDtoList;
    }


    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param itStatisticsDtoList it能耗集合
     * @param typeEnum 类型枚举
     * @return 返回同比
     */
    public List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<ItEnergyBaseStatisticsDto> itStatisticsDtoList,
                                                                ThresholdTypeEnum typeEnum) {
        //计算同比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = ItEnergyAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, itStatisticsDtoList, beforeYearDate, type, typeEnum);
        }
        return capacityThresholdInfoList;
    }





    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param itEnergyStatisticsDtoList 一年的数据
     * @param capacityThresholdInfoList 阈值信息集合
     * @param typeEnum 类型枚举
     * @return 返回环比
     */
    public List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<ItEnergyBaseStatisticsDto> itEnergyStatisticsDtoList,
                                                              ThresholdTypeEnum typeEnum) {
        //计算环比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = ItEnergyAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, itEnergyStatisticsDtoList, beforeDate, type, typeEnum);
        }
        return capacityThresholdInfoList;
    }

    /**
     * 返回需要分析的数据信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param searchCapacityThresholdList 阈值信息
     * @param analysisYear 分析的年份
     * @param analysisMonth 分析的月份
     * @param thresholdTypeEnum 阈值类型
     * @return 返回需要新增的分析数据信息
     */
    public CapacityAnalyzeData getAnalyzeData(Map<String, List<SystemSetting>> searchCapacityThresholdList, Integer analysisYear, Integer analysisMonth, ThresholdTypeEnum thresholdTypeEnum) {
        //获取模块阈值的信息
        String systemCode = "";
        if (ThresholdTypeEnum.MODULE.getType().equals(thresholdTypeEnum.getType())) {
            systemCode = ThresholdCapacityEnum.MODULE_ELECTRIC_PERCENT.getThresholdCode();
        } else if (ThresholdTypeEnum.UPS.getType().equals(thresholdTypeEnum.getType())) {
            systemCode = ThresholdCapacityEnum.UPS_ELECTRIC_PERCENT.getThresholdCode();
        }

        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(systemCode);

        //查询距离当前时间一年的模块变压器数据
        List<ItEnergyBaseStatisticsDto> dtoList = this.getSearchModelList(analysisYear, analysisMonth, thresholdTypeEnum);

        //获取总数的阈值信息
        List<SystemSetting> allNumberSystem = searchCapacityThresholdList.get(ThresholdCapacityEnum.ALL_NUMBER_SETTING.getThresholdCode());

        //设置比例信息
        dtoList = ItEnergyAnalyze.setPercentToData(dtoList, allNumberSystem);

        //当月数据
        List<ItEnergyBaseStatisticsDto> nowMonthList = dtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());


        //容量楼层阈值信息
        List<CapacityThresholdInfo> capacityThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, thresholdTypeEnum);

        //计算推荐或显示数据
        List<CapacityThresholdRelatedInfo> relatedInfoList = new ArrayList<>();

        //计算环比
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, dtoList, thresholdTypeEnum);
        //计算同比
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, dtoList, thresholdTypeEnum);

        //计算图表显示信息
        List<CapacityThresholdRelatedViewInfo> viewInfoList = this.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, dtoList, thresholdTypeEnum);

        //形成建议信息
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, thresholdTypeEnum);

        //形成运维管理活动项
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, thresholdTypeEnum);
        CapacityAnalyzeData capacityAnalyzeData = new CapacityAnalyzeData();
        capacityAnalyzeData.setThresholdInfoList(capacityThresholdInfoList);
        capacityAnalyzeData.setRelatedInfoList(new ArrayList<>());
        capacityAnalyzeData.setRelatedViewInfoList(viewInfoList);
        capacityAnalyzeData.setManageActivityList(manageActivityList);
        return capacityAnalyzeData;
    }

}
