package com.taiping.bean.energy.parameter.analyze;

import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import lombok.Data;

import java.util.List;

/**
 * 容量关联显示信息参数
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 20:21
 */
@Data
public class EnergyRelatedViewListParameter extends EnergyThresholdRelatedViewInfo {

    /**
     * 阈值code集合
     */
    private List<String> thresholdCodeList;
}
