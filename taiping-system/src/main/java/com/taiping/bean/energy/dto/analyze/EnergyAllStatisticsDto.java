package com.taiping.bean.energy.dto.analyze;

import lombok.Data;

/**
 * 总能耗统计dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/31 16:20
 */
@Data
public class EnergyAllStatisticsDto extends EnergyStatisticsBaseDto {

    /**
     * 是否是子项 0 否 1 是
     */
    private String isChild;

}
