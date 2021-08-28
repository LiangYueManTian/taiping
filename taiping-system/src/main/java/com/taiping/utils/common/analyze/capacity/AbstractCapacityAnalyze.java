package com.taiping.utils.common.analyze.capacity;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 抽象容量分析类
 * @author hedongwei@wistronits.com
 * @date  2019/11/1 14:59
 */
public abstract class AbstractCapacityAnalyze {


    /**
     * 分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:03
     * @param searchCapacityThresholdList 容量阈值信息
     * @param analysisYear  容量的年份
     * @param analysisMonth 容量的月份
     */
    public void analyzeData(Map<String, List<SystemSetting>> searchCapacityThresholdList, Integer analysisYear, Integer analysisMonth) {
        CapacityAnalyzeData capacityAnalyzeDataList = analyzeDataRealize(searchCapacityThresholdList, analysisYear, analysisMonth);
        if (!ObjectUtils.isEmpty(capacityAnalyzeDataList)) {
            //新增运维管理活动项
            if (!ObjectUtils.isEmpty(capacityAnalyzeDataList.getManageActivityList())) {
                insertManageActivityBatch(capacityAnalyzeDataList.getManageActivityList());
            }

            //新增容量阈值信息
            if (!ObjectUtils.isEmpty(capacityAnalyzeDataList.getThresholdInfoList())) {
                insertThresholdInfoBatch(capacityAnalyzeDataList.getThresholdInfoList());
            }

            //新增容量关联信息
            if (!ObjectUtils.isEmpty(capacityAnalyzeDataList.getRelatedInfoList())) {
                insertThresholdRelatedInfo(capacityAnalyzeDataList.getRelatedInfoList());
            }

            //新增容量关联显示信息
            if (!ObjectUtils.isEmpty(capacityAnalyzeDataList.getRelatedViewInfoList())) {
                insertThresholdRelatedViewInfo(capacityAnalyzeDataList.getRelatedViewInfoList());
            }
        }
    }



    /**
     * 新增运维管理活动信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param manageActivityList 运维管理活动
     */
    protected abstract void insertManageActivityBatch(List<ManageActivity> manageActivityList);

    /**
     * 新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdInfoList 容量阈值
     */
    protected abstract void insertThresholdInfoBatch(List<CapacityThresholdInfo> capacityThresholdInfoList);

    /**
     * 新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedInfoList 容量阈值关联集合
     */
    protected abstract void insertThresholdRelatedInfo(List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList);

    /**
     * 新增容量阈值关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedViewInfoList 容量阈值关联显示信息集合
     */
    protected abstract void insertThresholdRelatedViewInfo(List<CapacityThresholdRelatedViewInfo> capacityThresholdRelatedViewInfoList);

    /**
     * 自定义实现分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param searchCapacityThresholdList 阈值信息
     * @param analysisYear 分析的年份
     * @param analysisMonth 分析的月份
     * @return 返回需要新增的分析数据信息
     */
    protected abstract CapacityAnalyzeData analyzeDataRealize(Map<String ,List<SystemSetting>> searchCapacityThresholdList, Integer analysisYear, Integer analysisMonth);

}
