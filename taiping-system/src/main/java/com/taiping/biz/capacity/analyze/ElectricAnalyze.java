package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.CapacityBaseElectricStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityColumnStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityPduStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdModuleEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 电力容量分析类
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 9:23
 */
@Component
public class ElectricAnalyze {


    /**
     * 返回需要计算同比和环比的阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:40
     * @param capacityThresholdInfoList  容量阈值信息集合
     * @param dataStatisticsDtoList 查询容量数据集合
     * @param beforeDate 需要统计的时间那一年那一月
     * @param type 类型
     * @return 返回计算同比和环比的阈值信息
     */
    public <T extends CapacityBaseElectricStatisticsDto> List<CapacityThresholdInfo> castGroupInfo(List<CapacityThresholdInfo> capacityThresholdInfoList,
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
                for (T capacityElectricStatisticsDto : beforeList) {
                    String compareDataValue = getCompareValue(capacityElectricStatisticsDto, thresholdType);
                    if (dataValue.equals(compareDataValue)) {
                        //使用的电量数
                        double usedNumber =capacityElectricStatisticsDto.getUsedActualPower();
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
     * 生成查询一年的参数
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 11:39
     * @param analysisYear 年份
     * @param analysisMonth  月份
     * @return 返回查询的参数
     */
    public static CabinetInfoListParameter generateSearchParam(Integer analysisYear, Integer analysisMonth) {
        return SpaceAnalyze.generateSearchParam(analysisYear, analysisMonth);
    }


    /**
     * 根据类型获取比较的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:18
     * @param capacityElectricStatisticsDto 单个比较的对象信息
     * @param thresholdType 阈值的类型
     * @return 返回比较的值
     */
    public <T extends CapacityBaseElectricStatisticsDto> String getCompareValue(T capacityElectricStatisticsDto, ThresholdTypeEnum thresholdType) {
        String compareValue = "";
        if (ThresholdTypeEnum.CABINET_COLUMN.getType().equals(thresholdType.getType())) {
            CapacityColumnStatisticsDto dto = new CapacityColumnStatisticsDto();
            BeanUtils.copyProperties(capacityElectricStatisticsDto, dto);
            //调用列头柜的取比较值的方法
            compareValue = CabinetColumnAnalyze.getCompareValue(dto);
        } else if (ThresholdTypeEnum.PDU.getType().equals(thresholdType.getType())) {
            CapacityPduStatisticsDto dto = new CapacityPduStatisticsDto();
            BeanUtils.copyProperties(capacityElectricStatisticsDto, dto);
            //调用列头柜的取比较值的方法
            compareValue = PduAnalyze.getCompareValue(dto);
        }
        return compareValue;
    }

    /**
     * 根据类型获取总数的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:18
     * @param capacityElectricStatisticsDto 单个比较的对象信息
     * @param thresholdType 阈值的类型
     * @return 返回总数的值
     */
    public <T extends CapacityBaseElectricStatisticsDto> double getAllValue(T capacityElectricStatisticsDto, ThresholdTypeEnum thresholdType) {
        double allValue = 0;
        if (ThresholdTypeEnum.CABINET_COLUMN.getType().equals(thresholdType.getType())) {
            CapacityColumnStatisticsDto dto = new CapacityColumnStatisticsDto();
            BeanUtils.copyProperties(capacityElectricStatisticsDto, dto);
            //调用列头柜的取比较值的方法
            allValue = CabinetColumnAnalyze.getAllValue(dto);
        } else if (ThresholdTypeEnum.DEVICE_TYPE.getType().equals(thresholdType.getType())) {
            CapacityPduStatisticsDto dto = new CapacityPduStatisticsDto();
            BeanUtils.copyProperties(capacityElectricStatisticsDto, dto);
            //调用列头柜的取比较值的方法
            allValue = PduAnalyze.getAllValue(dto);
        }
        return allValue;
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
    public <T extends CapacityBaseElectricStatisticsDto> List<CapacityThresholdInfo> getThresholdList(List<T> nowMonthList, Integer analysisMonth, Integer analysisYear, List<SystemSetting> systemSettingList, ThresholdTypeEnum thresholdTypeEnum) {
        List<CapacityThresholdInfo> capacityThresholdInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (T capacityColumnStatisticsDto : nowMonthList) {
                //电力容量占比
                double usedPercent = capacityColumnStatisticsDto.getUsedActualPercent();
                AdviceBean adviceBean = CapacityAnalyze.getAdviceType(systemSettingList, usedPercent, thresholdTypeEnum);
                if (!ObjectUtils.isEmpty(adviceBean)) {
                    if (!ObjectUtils.isEmpty(adviceBean.getAdviceType())) {
                        CapacityThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, capacityColumnStatisticsDto, adviceBean, thresholdTypeEnum);
                        capacityThresholdInfoList.add(capacityThresholdInfo);
                    }
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
    public <T extends CapacityBaseElectricStatisticsDto> CapacityThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, T dataStatisticsDto, AdviceBean adviceBean, ThresholdTypeEnum thresholdType) {
        CapacityThresholdInfo capacityThresholdInfo = new CapacityThresholdInfo();
        capacityThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setMonth(analysisMonth);
        capacityThresholdInfo.setYear(analysisYear);
        capacityThresholdInfo.setType(thresholdType.getType());
        capacityThresholdInfo.setModule(ThresholdModuleEnum.ELECTRIC.getModule());
        capacityThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        String data = this.getCompareValue(dataStatisticsDto, thresholdType);
        capacityThresholdInfo.setThresholdData(data);
        capacityThresholdInfo.setThresholdName(data);
        capacityThresholdInfo.setThresholdValue(String.valueOf(dataStatisticsDto.getUsedActualPercent()));
        capacityThresholdInfo.setUsedNumber(dataStatisticsDto.getUsedActualPower());
        capacityThresholdInfo.setAllNumber(this.getAllValue(dataStatisticsDto, thresholdType));
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
     * 计算显示图像数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回显示图像数据
     */
    protected <T extends CapacityBaseElectricStatisticsDto> List<CapacityThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                                                                  List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                                                                  List<T> electricStatisticsDtoList, ThresholdTypeEnum thresholdTypeEnum) {

        //因为查询出的数据今年当月的数据到去年同期的数据，所以统计的去年同期的数据需要删除
        List<CapacityThresholdRelatedViewInfo> viewInfoList = CapacityAnalyze.getDefaultViewData(analysisYear, analysisMonth, capacityThresholdInfoList);
        //需要进行趋势预测的数据信息
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //需要往默认的数据中塞值
        for (T capacityStatisticsDto : electricStatisticsDtoList) {
            Integer dataYear = capacityStatisticsDto.getYear();
            Integer dataMonth = capacityStatisticsDto.getMonth();
            String value = this.getCompareValue(capacityStatisticsDto, thresholdTypeEnum);
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = CapacityAnalyze.getCompareViewValue(viewInfo);
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    if (ThresholdModuleEnum.ELECTRIC.getModule().equals(viewInfo.getModuleType())) {
                        //电力容量取剩余空间占比
                        double usedPercent = capacityStatisticsDto.getUsedActualPercent();
                        viewInfo.setValue(usedPercent);
                    }
                }
            }
        }

        dataViewMap = CapacityAnalyze.getDataViewMap(viewInfoList);
        //返回填充预测数据之后的值
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = CapacityAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }

}
