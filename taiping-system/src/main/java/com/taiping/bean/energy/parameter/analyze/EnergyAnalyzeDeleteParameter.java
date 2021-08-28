package com.taiping.bean.energy.parameter.analyze;

import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import lombok.Data;

import java.util.List;

/**
 * 能耗删除显示关联信息类
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 20:21
 */
@Data
public class EnergyAnalyzeDeleteParameter extends EnergyAnalyzeInfo {

    /**
     * 阈值code集合
     */
    private List<String> thresholdCodeList;
}
