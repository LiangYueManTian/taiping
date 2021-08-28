package com.taiping.bean.energy.parameter;

import com.taiping.entity.energy.ElectricInstrument;
import lombok.Data;

import java.util.List;

/**
 * 总能耗能耗参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:02
 */
@Data
public class ElectricInstrumentListParameter extends ElectricInstrument {

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
