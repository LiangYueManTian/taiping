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
public class ItEnergyInfoParameter extends ItEnergyCurrent {

    /**
     * 数据名称集合
     */
    private List<String> dataNameList;
}
