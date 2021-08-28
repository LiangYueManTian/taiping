package com.taiping.bean.capacity.analyze;

import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import lombok.Data;

import java.util.List;

/**
 * 容量分析数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/1 15:05
 */
@Data
public class CapacityAnalyzeData {

    /**
     * 运维管理活动项集合
     */
    private List<ManageActivity> manageActivityList;

    /**
     * 阈值集合
     */
    private List<CapacityThresholdInfo> thresholdInfoList;

    /**
     * 阈值关联信息集合
     */
    private List<CapacityThresholdRelatedInfo> relatedInfoList ;

    /**
     * 阈值关联显示信息集合
     */
    private List<CapacityThresholdRelatedViewInfo> relatedViewInfoList ;
}
