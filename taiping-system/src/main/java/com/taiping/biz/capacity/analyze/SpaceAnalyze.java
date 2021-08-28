package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.CapacityBaseSpaceStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityCabinetStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityDeviceTypeStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityFloorStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdModuleEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.analyze.capacity.ThresholdViewTypeEnum;
import com.taiping.enums.cabinet.DeviceTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import com.taiping.utils.common.analyze.capacity.TemplateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 空间容量分析类
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 9:23
 */
@Component
public class SpaceAnalyze {

    /**
     * 运维管理活动项逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 机柜逻辑层
     */
    @Autowired
    private CabinetService cabinetService;

    /**
     * 算出空间容量和电力容量的占比
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:33
     * @param spaceDtoList 楼层集合
     * @return 返回统计数据
     */
    public static <T extends CapacityBaseSpaceStatisticsDto> List<T> setPercentToData(List<T> spaceDtoList) {
        if (!ObjectUtils.isEmpty(spaceDtoList)) {
            for (T capacitySpaceStatisticsDto : spaceDtoList) {
                double usedSpace = capacitySpaceStatisticsDto.getUsedSpaceCapacity();
                double allSpace = capacitySpaceStatisticsDto.getDesignSpaceCapacity();
                //计算空间容量占比
                double spacePercent = CalculateUtil.castPercent(usedSpace, allSpace);
                capacitySpaceStatisticsDto.setUsedSpacePercent(spacePercent);

                double usedPower = capacitySpaceStatisticsDto.getUsedActualPower();
                double allRatedPower = capacitySpaceStatisticsDto.getRatedPower();
                //计算电力容量占比
                double powerPercent = CalculateUtil.castPercent(usedPower, allRatedPower);
                capacitySpaceStatisticsDto.setUsedActualPercent(powerPercent);
            }
        }
        return spaceDtoList;
    }


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
    public <T extends CapacityBaseSpaceStatisticsDto> List<CapacityThresholdInfo> castGroupInfo(Integer analysisYear, Integer analysisMonth,
                                                       List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                       List<T> dataStatisticsDtoList,
                                                       CapacityAnalyzeTime beforeDate, String type, ThresholdTypeEnum thresholdType) {
        List<T> beforeList = dataStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(beforeDate.getMonth()) && c.getYear().equals(beforeDate.getYear())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(beforeList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                double usedNumberInfo = capacityThresholdInfo.getUsedNumber();
                String dataValue = capacityThresholdInfo.getThresholdData();
                //增长率
                double growthPercent = 0;
                for (T capacitySpaceStatisticsDto : beforeList) {
                    String compareDataValue = getCompareValue(capacitySpaceStatisticsDto, thresholdType);
                    if (dataValue.equals(compareDataValue)) {
                        double usedNumber = capacitySpaceStatisticsDto.getUsedSpaceCapacity();
                        growthPercent = CapacityAnalyze.castPercentInfo(usedNumberInfo, usedNumber, usedNumber);
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
     * 获取单个阈值
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 23:04
     * @param analysisMonth 统计月份
     * @param analysisYear  统计年份
     * @param dataStatisticsDto 数据
     * @param adviceBean 建议对象
     * @return 返回单个阈值信息
     */
    public <T extends CapacityBaseSpaceStatisticsDto> CapacityThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, T dataStatisticsDto, AdviceBean adviceBean, ThresholdTypeEnum thresholdType) {
        CapacityThresholdInfo capacityThresholdInfo = new CapacityThresholdInfo();
        capacityThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setMonth(analysisMonth);
        capacityThresholdInfo.setYear(analysisYear);
        capacityThresholdInfo.setType(thresholdType.getType());
        capacityThresholdInfo.setModule(ThresholdModuleEnum.SPACE.getModule());
        capacityThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        String data = this.getCompareValue(dataStatisticsDto, thresholdType);
        capacityThresholdInfo.setThresholdData(data);
        if (ThresholdTypeEnum.DEVICE_TYPE.getType().equals(thresholdType.getType())) {
            //功能区的名称需要显示名称
            capacityThresholdInfo.setThresholdName(DeviceTypeEnum.getDeviceTypeNameByCode(data));
        } else {
            //除功能区之外的名称不用转含义
            capacityThresholdInfo.setThresholdName(data);
        }
        capacityThresholdInfo.setThresholdValue(String.valueOf(dataStatisticsDto.getUsedSpacePercent()));
        capacityThresholdInfo.setUsedNumber(dataStatisticsDto.getUsedSpaceCapacity());
        capacityThresholdInfo.setAllNumber(dataStatisticsDto.getDesignSpaceCapacity());
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
     * 根据类型获取比较的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:18
     * @param capacitySpaceStatisticsDto 单个比较的对象信息
     * @param thresholdType 阈值的类型
     * @return 返回比较的值
     */
    public <T extends CapacityBaseSpaceStatisticsDto> String getCompareValue(T capacitySpaceStatisticsDto, ThresholdTypeEnum thresholdType) {
        String compareValue = "";
        if (ThresholdTypeEnum.FLOOR.getType().equals(thresholdType.getType())) {
            CapacityFloorStatisticsDto dto = new CapacityFloorStatisticsDto();
            BeanUtils.copyProperties(capacitySpaceStatisticsDto, dto);
            //调用楼层的取比较值的方法
            compareValue = FloorSpaceAnalyze.getCompareValue(dto);
        } else if (ThresholdTypeEnum.DEVICE_TYPE.getType().equals(thresholdType.getType())) {
            CapacityDeviceTypeStatisticsDto dto = new CapacityDeviceTypeStatisticsDto();
            BeanUtils.copyProperties(capacitySpaceStatisticsDto, dto);
            //调用功能区的取比较值的方法
            compareValue = FunctionTypeSpaceAnalyze.getCompareValue(dto);
        } else if (ThresholdTypeEnum.CABINET.getType().equals(thresholdType.getType())) {
            //调用机柜的取比较值的方法
            CapacityCabinetStatisticsDto dto = new CapacityCabinetStatisticsDto();
            BeanUtils.copyProperties(capacitySpaceStatisticsDto, dto);
            //调用机柜的取比较值的方法
            compareValue = CabinetSpaceAnalyze.getCompareValue(dto);
        }
        return compareValue;
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
    protected <T extends CapacityBaseSpaceStatisticsDto> List<CapacityThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                       List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                       List<T> spaceStatisticsDtoList, ThresholdTypeEnum thresholdTypeEnum) {

        //因为查询出的数据今年当月的数据到去年同期的数据，所以统计的去年同期的数据需要删除
        List<CapacityThresholdRelatedViewInfo> viewInfoList = CapacityAnalyze.getDefaultViewData(analysisYear, analysisMonth, capacityThresholdInfoList);
        //需要进行趋势预测的数据信息
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //需要往默认的数据中塞值
        for (T capacityStatisticsDto : spaceStatisticsDtoList) {
            Integer dataYear = capacityStatisticsDto.getYear();
            Integer dataMonth = capacityStatisticsDto.getMonth();
            String value = this.getCompareValue(capacityStatisticsDto, thresholdTypeEnum);
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = CapacityAnalyze.getCompareViewValue(viewInfo);
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    if (ThresholdModuleEnum.SPACE.getModule().equals(viewInfo.getModuleType())) {
                        //空间容量取剩余空间占比
                        double spacePercent = capacityStatisticsDto.getUsedSpacePercent();
                        viewInfo.setValue(spacePercent);
                    }
                }
            }
        }

        dataViewMap = CapacityAnalyze.getDataViewMap(viewInfoList);
        //返回填充预测数据之后的值
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = CapacityAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }

    /**
     * 生成查询一年的参数
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 11:39
     * @param analysisYear 年份
     * @param analysisMonth  月份
     * @return 返回查询的参数
     */
    public static CabinetInfoListParameter generateSearchParam(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = new CabinetInfoListParameter();
        CapacityAnalyzeTime capacityAnalyzeTime = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        Long nowDate = 0L;
        Long beforeYearDate = 0L;
        //当前年份月份的时间
        nowDate = DateUtil.generateCollectionTime(analysisMonth, analysisYear, nowDate);
        //去年年份月份的时间
        beforeYearDate = DateUtil.generateCollectionTime(capacityAnalyzeTime.getMonth(), capacityAnalyzeTime.getYear(), nowDate);
        parameter.setStartTime(beforeYearDate);
        parameter.setEndTime(nowDate);
        return parameter;
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
    public <T extends CapacityBaseSpaceStatisticsDto> List<CapacityThresholdInfo> getThresholdList(List<T> nowMonthList, Integer analysisMonth, Integer analysisYear, List<SystemSetting> systemSettingList, ThresholdTypeEnum thresholdTypeEnum) {
        List<CapacityThresholdInfo> capacityThresholdInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (T capacityFloorStatisticsDto : nowMonthList) {
                //空间容量占比
                double spacePercent = capacityFloorStatisticsDto.getUsedSpacePercent();
                AdviceBean adviceBean = CapacityAnalyze.getAdviceType(systemSettingList, spacePercent, thresholdTypeEnum);
                if (!ObjectUtils.isEmpty(adviceBean)) {
                    if (!ObjectUtils.isEmpty(adviceBean.getAdviceType())) {
                        CapacityThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, capacityFloorStatisticsDto, adviceBean, thresholdTypeEnum);
                        capacityThresholdInfoList.add(capacityThresholdInfo);
                    }
                }
            }
        }
        return capacityThresholdInfoList;
    }



    /**
     * 比较的结果
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:38
     * @param s1 比较的数
     * @param s2 被比较的数
     * @return 返回比较的结果
     */
    public <T extends CapacityBaseSpaceStatisticsDto> int compareInfo(T s1, T s2) {
        double sumInfo = s1.getDesignSpaceCapacity() - s1.getUsedSpaceCapacity();
        double sumInfoTwo = s2.getDesignSpaceCapacity() - s2.getUsedSpaceCapacity();
        return compareValue(sumInfo, sumInfoTwo);
    }


    /**
     * 比较的结果
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:38
     * @param s1 比较的数
     * @param s2 被比较的数
     * @return 返回比较的结果
     */
    public <T extends CapacityBaseSpaceStatisticsDto> int compareInfoItem(T s1, T s2) {
        double sumInfo = s1.getUsedSpacePercent();
        double sumInfoTwo = s2.getUsedSpacePercent();
        return compareValue(sumInfo, sumInfoTwo);
    }


    /**
     * 比较值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 17:22
     * @param sumInfo 统计信息一
     * @param sumInfoTwo 统计信息二
     * @return 比较结果
     */
    public Integer compareValue(double sumInfo, double sumInfoTwo) {
        if(sumInfo < sumInfoTwo){
            return -1;
        }else if(sumInfo == sumInfoTwo){
            return 0;
        }else{
            return 1;
        }
    }


    /**
     * 获取单个推荐信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:40
     * @param capacityThresholdInfo 容量阈值信息表
     * @param recommend 容量功能区统计信息
     * @param thresholdTypeEnum 推荐数据的类型
     * @return 获取单个推荐信息
     */
    public <T extends CapacityBaseSpaceStatisticsDto> CapacityThresholdRelatedInfo getOneRelatedInfo(CapacityThresholdInfo capacityThresholdInfo, T recommend, ThresholdTypeEnum thresholdTypeEnum) {
        CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = new CapacityThresholdRelatedInfo();
        capacityThresholdRelatedInfo.setThresholdCode(capacityThresholdInfo.getThresholdCode());
        capacityThresholdRelatedInfo.setThresholdRelatedId(NineteenUUIDUtils.uuid());
        capacityThresholdRelatedInfo.setType(ThresholdViewTypeEnum.VIEW.getViewType());
        String name = this.getCompareValue(recommend, thresholdTypeEnum);
        capacityThresholdRelatedInfo.setName(name);
        if (ThresholdTypeEnum.DEVICE_TYPE.getType().equals(thresholdTypeEnum.getType())) {
            capacityThresholdRelatedInfo.setDataName(DeviceTypeEnum.getDeviceTypeNameByCode(name));
        } else {
            capacityThresholdRelatedInfo.setDataName(name);
        }
        double usedSpace = recommend.getUsedSpaceCapacity();
        double allSpace = recommend.getDesignSpaceCapacity();
        //计算空间容量占比
        double spacePercent = CalculateUtil.castPercent(usedSpace, allSpace);
        recommend.setUsedSpacePercent(spacePercent);

        double usedPower = recommend.getUsedActualPower();
        double allRatedPower = recommend.getRatedPower();
        //计算电力容量占比
        double powerPercent = CalculateUtil.castPercent(usedPower, allRatedPower);
        recommend.setUsedActualPercent(powerPercent);
        if (ThresholdModuleEnum.SPACE.getModule().equals(capacityThresholdInfo.getModule())) {
            capacityThresholdRelatedInfo.setValue(recommend.getUsedSpacePercent());
        } else if (ThresholdModuleEnum.ELECTRIC.getModule().equals(capacityThresholdInfo.getModule())) {
            capacityThresholdRelatedInfo.setValue(recommend.getUsedActualPercent());
        }
        return capacityThresholdRelatedInfo;
    }





    /**
     * 容量楼层数据查询当前月份的楼层功能区数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份的楼层功能区数据
     */
    public List<CapacityDeviceTypeStatisticsDto> getSearchFloorAndDeviceList(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = new CabinetInfoListParameter();
        parameter.setYear(analysisYear);
        parameter.setMonth(analysisMonth);
        List<CapacityDeviceTypeStatisticsDto> deviceStatisticsDtoList = cabinetService.queryCapacityByGroupByFloorAndDevice(parameter);
        return deviceStatisticsDtoList;
    }

}
