package com.taiping.biz.energy.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.energy.dto.analyze.EnergyStatisticsBaseDto;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.constant.energy.EnergyConstant;
import com.taiping.entity.analyze.AnalyzeBaseRelatedViewInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.enums.analyze.energy.EnergyStatisticalModelEnum;
import com.taiping.enums.analyze.energy.EnergyStatisticalTypeEnum;
import com.taiping.enums.energy.ElectricPowerIsChildEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.utils.common.analyze.AnalyzeInfo;
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
 * 能耗统计信息
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 11:23
 */
@Component
public class EnergyAnalyze {

    /**
     * 自动注入运维管理逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 返回需要计算同比和环比的阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:40
     * @param energyThresholdInfoList  能耗统计信息集合
     * @param dataStatisticsDtoList 查询能耗数据集合
     * @param beforeDate 需要统计的时间那一年那一月
     * @param type 类型
     * @return 返回计算同比和环比的统计信息
     */
    public static <T extends EnergyStatisticsBaseDto> List<EnergyThresholdInfo> castGroupInfo(List<EnergyThresholdInfo> energyThresholdInfoList,
                                                                                       List<T> dataStatisticsDtoList,
                                                                                       CapacityAnalyzeTime beforeDate, String type) {
        List<T> beforeList = dataStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(beforeDate.getMonth()) && c.getYear().equals(beforeDate.getYear())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(beforeList)) {
            for (EnergyThresholdInfo energyThresholdInfo : energyThresholdInfoList) {
                String dataValue = energyThresholdInfo.getThresholdData();
                double dataInfo = Double.valueOf(energyThresholdInfo.getThresholdValue());
                //增长率
                double growthPercent = 0;
                //增长值
                double growthNumber = 0;
                for (T energyStatisticsDto : beforeList) {
                    String compareDataValue = energyStatisticsDto.getDataCode();
                    if (ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(energyThresholdInfo.getIsChild())) {
                        compareDataValue = energyStatisticsDto.getType();
                    }
                    if (dataValue.equals(compareDataValue)) {
                        double growthMeter = energyStatisticsDto.getGrowthElectricMeter();
                        growthNumber = dataInfo - growthMeter;
                        growthPercent = CapacityAnalyze.castPercentInfo(dataInfo, growthMeter, growthMeter);
                        break;
                    }
                }
                if ("1".equals(type)) {
                    //同比
                    energyThresholdInfo.setYearOverYearPercent(growthPercent);
                    energyThresholdInfo.setYearOverYearNumber(growthNumber);
                } else if ("2".equals(type)) {
                    //环比
                    energyThresholdInfo.setRingGrowth(growthPercent);
                    energyThresholdInfo.setRingGrowthNumber(growthNumber);
                }
            }
        }
        return energyThresholdInfoList;
    }


    /**
     * 计算显示图像数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param energyThresholdInfoList 能耗信息集合
     * @param isFilterBeforeYear 是否筛选掉去年同月的数据
     * @return 返回显示图像数据
     */
    public static <T extends EnergyStatisticsBaseDto> List<EnergyThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                                                         List<EnergyThresholdInfo> energyThresholdInfoList,
                                                                                                         List<T> energyStatisticsDtoList, boolean isFilterBeforeYear) {

        //因为查询出的数据今年当月的数据到去年同期的数据，所以统计的去年同期的数据需要删除
        List<AnalyzeBaseRelatedViewInfo> viewBaseInfoList = AnalyzeInfo.getDefaultViewData(analysisYear, analysisMonth, energyThresholdInfoList, EnergyConstant.STATISTICS_MONTH, EnergyConstant.FORECAST_MONTH, isFilterBeforeYear);
        List<EnergyThresholdRelatedViewInfo> viewInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(viewBaseInfoList)) {
            for (AnalyzeBaseRelatedViewInfo viewInfoOne : viewBaseInfoList) {
                EnergyThresholdRelatedViewInfo energyOne = new EnergyThresholdRelatedViewInfo();
                BeanUtils.copyProperties(viewInfoOne, energyOne);
                viewInfoList.add(energyOne);
            }
        }
        //需要进行趋势预测的数据信息
        Map<String, List<EnergyThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //需要往默认的数据中塞值
        for (T energyStatisticsDto : energyStatisticsDtoList) {
            Integer dataYear = energyStatisticsDto.getYear();
            Integer dataMonth = energyStatisticsDto.getMonth();
            String value = energyStatisticsDto.getType();
            for (EnergyThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = viewInfo.getDataCode();
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    double electricMeter = energyStatisticsDto.getGrowthElectricMeter();
                    viewInfo.setValue(electricMeter);
                }
            }
        }

        dataViewMap = AnalyzeInfo.getDataViewMap(viewInfoList);
        //返回填充预测数据之后的值
        List<EnergyThresholdRelatedViewInfo> returnViewInfoList = EnergyAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }



    /**
     * 计算预测数据信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:46
     * @param viewMap 关联信息数据
     * @return 预测数据信息
     */
    public static List<EnergyThresholdRelatedViewInfo> calculateExpectInfo(Map<String, List<EnergyThresholdRelatedViewInfo>> viewMap) {
        //返回填充趋势后的数据
        List<EnergyThresholdRelatedViewInfo> returnViewInfoList = new ArrayList<>();
        //计算趋势信息
        if (!ObjectUtils.isEmpty(viewMap)) {
            for (String thresholdCode : viewMap.keySet()) {
                List<String> doubleList = new ArrayList<>();
                for (int i = 0; i < CapacityConstant.STATISTICS_MONTH ; i++) {
                    EnergyThresholdRelatedViewInfo viewInfo = viewMap.get(thresholdCode).get(i);
                    doubleList.add(viewInfo.getValue() + "");
                }
                //预测数据
                double modulus = 0.6;
                for (int i = 0; i < CapacityConstant.FORECAST_MONTH ; i++) {
                    boolean isPercent = false;
                    doubleList.add(CapacityAnalyze.getExpect(doubleList, i + 1 ,modulus , isPercent) + "");
                }

                //楼层map
                for (int i = 0 ; i < viewMap.get(thresholdCode).size() ; i++) {
                    //预测数据开始下标减一
                    int startIndex = CapacityConstant.STATISTICS_MONTH - 1;
                    for (int j = startIndex ; j < viewMap.get(thresholdCode).size(); j++) {
                        EnergyThresholdRelatedViewInfo viewInfoOne = viewMap.get(thresholdCode).get(j);
                        viewInfoOne.setValue(Double.valueOf(doubleList.get(j)));
                    }
                }
            }
        }

        //将预测的值生成数据信息
        for (String thresholdOne  : viewMap.keySet()) {
            returnViewInfoList.addAll(viewMap.get(thresholdOne));
        }
        return returnViewInfoList;
    }

    /**
     * 获取查询信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/22 11:44
     * @param viewInfo 查询信息
     * @param energyThresholdInfoList 能耗分析信息集合
     * @return 返回查询信息
     */
    public static List<EnergyThresholdInfo> getAnalyzeInfo(List<EnergyThresholdInfo> viewInfo, List<EnergyThresholdInfo> energyThresholdInfoList) {
        //设置父级code为属性
        if (!ObjectUtils.isEmpty(viewInfo) && !ObjectUtils.isEmpty(energyThresholdInfoList)) {
            for (EnergyThresholdInfo energyThresholdInfo : viewInfo) {
                String code = energyThresholdInfo.getThresholdCode();
                String type = energyThresholdInfo.getType();
                for (EnergyThresholdInfo energyInfo : energyThresholdInfoList) {
                    if (ElectricPowerIsChildEnum.IS_CHILD.getCode().equals(energyInfo.getIsChild())) {
                        String compareType = energyInfo.getType();
                        if (type.equals(compareType)) {
                            energyInfo.setParentCode(code);
                        }
                    }
                }
            }
        }
        return energyThresholdInfoList;
    }


    /**
     * 设置提示的消息信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 16:19
     * @param energyThresholdInfoList 能耗信息表
     * @return 返回提示的消息信息
     */
    public static List<EnergyThresholdInfo> setAdviceToThreshold(Integer analysisYear, Integer analysisMonth , List<EnergyThresholdInfo> energyThresholdInfoList) {
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            for (EnergyThresholdInfo energyThresholdInfo : energyThresholdInfoList) {
                //去年的时间
                CapacityAnalyzeTime beforeDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
                //上个月的时间
                CapacityAnalyzeTime beforeMonthDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
                String name = energyThresholdInfo.getThresholdName();
                String value = energyThresholdInfo.getThresholdValue();
                double ringPercent = energyThresholdInfo.getRingGrowth();
                double ringValue = energyThresholdInfo.getRingGrowthNumber();
                Integer ringMonth = beforeMonthDate.getMonth();
                Integer beforeYear = beforeDate.getYear();
                double yearOverYearPercent = energyThresholdInfo.getYearOverYearPercent();
                double yearOverYearValue = energyThresholdInfo.getYearOverYearNumber();
                Integer month = analysisMonth;
                Integer endMonth = 0;
                String ringReason = "";
                if (12 == month) {
                    endMonth = 1;
                } else {
                    endMonth = month + 1;
                }
                String adviceName = "";
                List<String> templateList = EnergyAnalyze.getDefaultList();
                //模板map
                if (EnergyStatisticalTypeEnum.PUE.getType().equals(energyThresholdInfo.getType())) {
                    double ringNumber = energyThresholdInfo.getRingGrowthNumber();
                    if (ringNumber >= 0) {
                        ringReason = EnergyConstant.RING_GROWTH;
                    } else {
                        ringReason = EnergyConstant.RING_DECLINE;
                    }
                }
                Map<String, String> templateMap = EnergyAnalyze.getTemplateMap(name, value, month, endMonth,
                        ringMonth, ringValue, ringPercent,
                        yearOverYearValue, yearOverYearPercent, beforeYear, ringReason);
                String template = "";
                if (ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(energyThresholdInfo.getIsChild())) {
                    template = EnergyAnalyze.getTemplate(energyThresholdInfo.getModule());
                } else {
                    template = EnergyConstant.CHILD_ADVICE_INFO;
                }
                adviceName = TemplateUtil.parseTemplate(template, templateList, templateMap);
                energyThresholdInfo.setAdvice(adviceName);
            }
        }
        return energyThresholdInfoList;
    }

    /**
     * 获取默认的模板集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/19 17:24
     * @return 返回默认的模板集合信息
     */
    public static List<String> getDefaultList() {
        List<String> templateList = new ArrayList<>();
        templateList.add("name");
        templateList.add("value");
        templateList.add("month");
        templateList.add("endMonth");
        templateList.add("ringMonth");
        templateList.add("ringValue");
        templateList.add("ringPercent");
        templateList.add("yearOverYearValue");
        templateList.add("yearOverYearPercent");
        templateList.add("beforeYear");
        templateList.add("ringReason");
        return templateList;
    }


    /**
     * 获取的模板数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/19 17:24
     * @return 返回默认的模板数据信息
     */
    public static Map<String, String> getTemplateMap(String name, String value, Integer month, Integer endMonth, Integer ringMonth, double ringValue, double ringPercent,
                                                     double yearOverYearValue, double yearOverYearPercent, Integer beforeYear, String ringReason) {
        Map<String, String> templateMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        templateMap.put("name", name);
        templateMap.put("value", value);
        templateMap.put("month", month.toString());
        templateMap.put("endMonth", endMonth.toString());
        templateMap.put("ringMonth", ringMonth.toString());
        templateMap.put("ringValue", EnergyAnalyze.getNumberInfo(Double.valueOf(ringValue)));
        templateMap.put("ringPercent", EnergyAnalyze.getNumberInfo(Double.valueOf(ringPercent)));
        templateMap.put("yearOverYearValue",EnergyAnalyze.getNumberInfo(Double.valueOf(yearOverYearValue)));
        templateMap.put("yearOverYearPercent", EnergyAnalyze.getNumberInfo(Double.valueOf(yearOverYearPercent)));
        templateMap.put("ringReason", ringReason);
        templateMap.put("beforeYear", beforeYear.toString());
        return templateMap;
    }

    /**
     * 获取值的中文显示字段
     * @author hedongwei@wistronits.com
     * @date  2019/11/20 16:57
     * @param data 数据
     * @return 返回数据
     */
    public static String getNumberInfo(double data) {
        String dataInfo = "";
        if (data >= 0) {
            dataInfo = "增加" + data;
        } else {
            dataInfo = "减少" + data;
            String diffInfo = "-";
            String emptyInfo = "";
            dataInfo = dataInfo.replace(diffInfo, emptyInfo);
        }
        return dataInfo;
    }


    public static String getTemplate(String model) {
        String template = "";
        if (EnergyStatisticalModelEnum.POWER_METER.getModel().equals(model)) {
            //动力分项能耗
            template = EnergyConstant.ITEM_ADVICE_INFO;
        } else if (EnergyStatisticalModelEnum.ALL_METER.getModel().equals(model)) {
            //总能耗
            template = EnergyConstant.ALL_ADVICE_INFO;
        } else if (EnergyStatisticalModelEnum.PUE.getModel().equals(model)) {
            //pue值
            template = EnergyConstant.PUE_ADVICE_INFO;
        }
        return template;
    }



    /**
     * 生成运维管理活动项
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 17:50
     * @param energyThresholdInfoList 能耗关联信息集合
     * @return 运维管理活动项集合
     */
    public  List<ManageActivity> generateManageActivity(List<EnergyThresholdInfo> energyThresholdInfoList) {
        List<ManageActivity> manageActivityList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(energyThresholdInfoList)) {
            for (EnergyThresholdInfo energyThresholdInfo : energyThresholdInfoList) {
                Long createTime = energyThresholdInfo.getDataCollectionTime();
                String name = energyThresholdInfo.getThresholdName();
                String code = energyThresholdInfo.getThresholdData();
                ManageStatusEnum manageStatusEnum = ManageStatusEnum.UN_REDUCE;
                ManageActivity manageOne = manageActivityService.createManageActivity(energyThresholdInfo.getThresholdCode(), name, code, System.currentTimeMillis(),
                        energyThresholdInfo.getAdvice(), energyThresholdInfo.getCause(), manageStatusEnum, ManageSourceEnum.ENERGY, createTime);
                manageActivityList.add(manageOne);
            }
        }
        return manageActivityList;
    }
}
