package com.taiping.bean.energy.parameter;

import com.taiping.entity.energy.ElectricInstrument;
import lombok.Data;

import java.util.List;

/**
 * 总能耗参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:02
 */
@Data
public class ElectricInstrumentInfoParameter extends ElectricInstrument {

    /**
     * 数据名称集合
     */
    private List<String> dataCodeList;
}
