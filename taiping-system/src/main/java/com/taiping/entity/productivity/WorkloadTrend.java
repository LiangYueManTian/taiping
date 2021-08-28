package com.taiping.entity.productivity;

import lombok.Data;

import java.util.List;

/**
 * 趋势曲线实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-12-10
 */
@Data
public class WorkloadTrend {
    /**
     * x轴数据
     */
    private List<String> xData;
    /**
     * y轴数据
     */
    private List<List<WorkloadTrendValue>> yData;
}
