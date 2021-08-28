package com.taiping.bean.energy.parameter.analyze;

import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import lombok.Data;

import java.util.List;

/**
 * 能耗分析信息集合参数
 * @author hedongwei@wistronits.com
 * @date 2019/11/20 16:08
 */
@Data
public class EnergyThresholdInfoListParameter extends EnergyThresholdInfo {

    /**
     * 分析code集合
     */
    private List<String> thresholdCodeList;

    /**
     * 父级code集合
     */
    private List<String> parentCodeList;
}
