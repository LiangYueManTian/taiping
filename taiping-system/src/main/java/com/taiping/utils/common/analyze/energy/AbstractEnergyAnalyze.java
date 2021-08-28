package com.taiping.utils.common.analyze.energy;

import com.taiping.bean.energy.analyze.EnergyAnalyzeData;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 抽象能耗分析类
 * @author hedongwei@wistronits.com
 * @date  2019/11/1 14:59
 */
public abstract class AbstractEnergyAnalyze {

    /**
     * 分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:03
     * @param analysisYear  能耗的年份
     * @param analysisMonth 能耗的月份
     */
    public void analyzeData(Integer analysisYear, Integer analysisMonth) {
        EnergyAnalyzeData energyAnalyzeData = analyzeDataRealize(analysisYear, analysisMonth);
        if (!ObjectUtils.isEmpty(energyAnalyzeData)) {
            //新增能耗运维管理活动项
            if (!ObjectUtils.isEmpty(energyAnalyzeData.getManageActivityList())) {
                insertManageActivityBatch(energyAnalyzeData.getManageActivityList());
            }


            //新增能耗阈值信息
            if (!ObjectUtils.isEmpty(energyAnalyzeData.getThresholdInfoList())) {
                insertThresholdInfoBatch(energyAnalyzeData.getThresholdInfoList());
            }

            //新增能耗关联显示信息
            if (!ObjectUtils.isEmpty(energyAnalyzeData.getRelatedViewInfoList())) {
                insertThresholdRelatedViewInfo(energyAnalyzeData.getRelatedViewInfoList());
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
     * 新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdInfoList 能耗阈值
     */
    protected abstract void insertThresholdInfoBatch(List<EnergyThresholdInfo> energyThresholdInfoList);

    /**
     * 新增能耗阈值关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param energyThresholdRelatedViewInfoList 能耗阈值关联显示信息集合
     */
    protected abstract void insertThresholdRelatedViewInfo(List<EnergyThresholdRelatedViewInfo> energyThresholdRelatedViewInfoList);

    /**
     * 自定义实现分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param analysisYear 分析的年份
     * @param analysisMonth 分析的月份
     * @return 返回需要新增的分析数据信息
     */
    protected abstract EnergyAnalyzeData analyzeDataRealize(Integer analysisYear, Integer analysisMonth);

}
