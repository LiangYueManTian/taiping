package com.taiping.utils.common.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.AnalyzeBaseRelatedViewInfo;
import com.taiping.entity.analyze.AnalyzeThresholdBaseInfo;
import com.taiping.enums.analyze.capacity.ThresholdViewTypeEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 14:38
 */
@Component
public class AnalyzeInfo {

    /**
     * 计算显示图像数据默认值
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param analyzeThresholdInfoList 分析信息集合
     * @param allMonth 全部月份
     * @param forecastMonth  预测月份
     * @param isFilterBeforeYear 是否过滤去年同月的数据
     * @return 返回显示图像数据
     */
    public static <T extends AnalyzeThresholdBaseInfo> List<AnalyzeBaseRelatedViewInfo> getDefaultViewData(Integer analysisYear, Integer analysisMonth,
                                                                                    List<T> analyzeThresholdInfoList, Integer allMonth, Integer forecastMonth, boolean isFilterBeforeYear) {
        //找到去年同期的数据
        CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        List<AnalyzeBaseRelatedViewInfo> viewInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(analyzeThresholdInfoList)) {
            Map<String, AnalyzeThresholdBaseInfo> dataMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (T analyzeThresholdInfo : analyzeThresholdInfoList) {
                dataMap.put(analyzeThresholdInfo.getThresholdCode(), analyzeThresholdInfo);
            }

            if (!ObjectUtils.isEmpty(dataMap)) {
                //设置默认值
                for (String dataOne : dataMap.keySet()) {
                    int startYear = beforeYearDate.getYear();
                    int startMonth = beforeYearDate.getMonth();
                    if (isFilterBeforeYear) {
                        //过滤掉同比去年的年月的数据
                        startMonth ++;
                        if (startMonth > 12) {
                            startYear ++;
                            startMonth = 1;
                        }
                    }

                    for (int i = 1; i <= allMonth + forecastMonth; i ++) {
                        AnalyzeBaseRelatedViewInfo viewInfo = new AnalyzeBaseRelatedViewInfo();
                        viewInfo.setThresholdRelatedViewId(NineteenUUIDUtils.uuid());
                        viewInfo.setThresholdCode(dataOne);
                        viewInfo.setDataCode(dataMap.get(dataOne).getThresholdData());
                        viewInfo.setModuleType(dataMap.get(dataOne).getModule());
                        viewInfo.setDataModule(dataMap.get(dataOne).getType());
                        Long dateTime = 0L;
                        dateTime = DateUtil.generateCollectionTime(startMonth, startYear, dateTime);
                        viewInfo.setDataTime(dateTime);
                        viewInfo.setDataModule(dataMap.get(dataOne).getType());
                        viewInfo.setModuleType(dataMap.get(dataOne).getModule());
                        if (i > allMonth) {
                            viewInfo.setViewType(ThresholdViewTypeEnum.Trend.getViewType());
                        } else {
                            viewInfo.setViewType(ThresholdViewTypeEnum.VIEW.getViewType());
                        }
                        viewInfo.setValue(0);
                        viewInfo.setYear(startYear);
                        viewInfo.setMonth(startMonth);
                        startMonth ++;
                        if (startMonth > 12) {
                            startYear ++;
                            startMonth = 1;
                        }
                        viewInfoList.add(viewInfo);
                    }
                }
            }
        }
        return viewInfoList;
    }



    /**
     * 获取显示数据查询map
     * @author hedongwei@wistronits.com
     * @date  2019/11/8 15:29
     * @param viewInfoList 显示数据集合
     * @return 返回map
     */
    public static <T extends AnalyzeBaseRelatedViewInfo>  Map<String, List<T>> getDataViewMap(List<T> viewInfoList) {
        Map<String, List<T>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(viewInfoList)) {
            for (T viewInfo : viewInfoList) {
                String thresholdCode = viewInfo.getThresholdCode();
                List<T> relatedInfo = new ArrayList<>();
                if (!ObjectUtils.isEmpty(dataViewMap)) {
                    if (!ObjectUtils.isEmpty(dataViewMap.get(thresholdCode))) {
                        relatedInfo = dataViewMap.get(thresholdCode);
                    }
                }
                relatedInfo.add(viewInfo);
                dataViewMap.put(thresholdCode, relatedInfo);
            }
        }
        return dataViewMap;
    }
}
