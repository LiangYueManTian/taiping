package com.taiping.bean.energy.analyze;

import com.taiping.bean.energy.dto.analyze.EnergyAllStatisticsDto;
import lombok.Data;

import java.util.List;

/**
 * 总能耗数据分析
 * @author hedongwei@wistronits.com
 * @date 2019/11/22 9:54
 */
@Data
public class AllEnergyDataAnalyze {

    /**
     * 子项dto
     */
    private List<EnergyAllStatisticsDto> allEnergyData;

    /**
     * PUE dto
     */
    private List<EnergyAllStatisticsDto> pueData;
}
