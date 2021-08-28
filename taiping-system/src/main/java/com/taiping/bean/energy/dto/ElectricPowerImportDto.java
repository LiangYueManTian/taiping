package com.taiping.bean.energy.dto;

import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.energy.PowerEnergyItem;
import lombok.Data;

import java.util.List;

/**
 * 总能耗导入数据dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:00
 */
@Data
public class ElectricPowerImportDto extends ExcelReadBean {

    /**
     * 电力集合
     */
    private List<PowerEnergyItem> powerEnergyItemList;

}
