package com.taiping.bean.energy.analyze;

import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import lombok.Data;

import java.util.List;

/**
 * 能耗分析数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/1 15:05
 */
@Data
public class EnergyAnalyzeData {

    /**
     * 运维管理活动项集合
     */
    private List<ManageActivity> manageActivityList;

    /**
     * 阈值集合
     */
    private List<EnergyThresholdInfo> thresholdInfoList;

    /**
     * 阈值关联显示信息集合
     */
    private List<EnergyThresholdRelatedViewInfo> relatedViewInfoList ;
}
