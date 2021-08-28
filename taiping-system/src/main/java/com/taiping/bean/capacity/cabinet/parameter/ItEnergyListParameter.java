package com.taiping.bean.capacity.cabinet.parameter;

import com.taiping.entity.cabinet.ItEnergyCurrent;
import lombok.Data;

import java.util.List;

/**
 * it能耗参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:02
 */
@Data
public class ItEnergyListParameter extends ItEnergyCurrent {

    /**
     * 模块结合
     */
    private List<String> moduleList;

    /**
     * 设备类型集合
     */
    private List<String> typeList;

    /**
     * 年份集合
     */
    private List<Integer> yearList;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;
}
