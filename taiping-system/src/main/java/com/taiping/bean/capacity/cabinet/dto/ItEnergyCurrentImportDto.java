package com.taiping.bean.capacity.cabinet.dto;

import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import lombok.Data;

import java.util.List;

/**
 * it能耗dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:00
 */
@Data
public class ItEnergyCurrentImportDto extends ExcelReadBean {

    /**
     * it能耗集合
     */
    private List<ItEnergyCurrent> itEnergyCurrentList;

}
