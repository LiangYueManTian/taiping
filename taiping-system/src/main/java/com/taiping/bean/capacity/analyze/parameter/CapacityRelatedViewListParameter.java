package com.taiping.bean.capacity.analyze.parameter;

import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import lombok.Data;

import java.util.List;

/**
 * 容量关联显示信息参数
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 20:21
 */
@Data
public class CapacityRelatedViewListParameter extends CapacityThresholdRelatedViewInfo {

    /**
     * 阈值code集合
     */
    private List<String> thresholdCodeList;
}
