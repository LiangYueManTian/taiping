package com.taiping.entity.problem;

import lombok.Data;

import java.util.List;
/**
 * 停水停电趋势曲线实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
@Data
public class ProblemTrend {
    /**
     * x轴数据
     */
    private List<String> xData;
    /**
     * y轴数据
     */
    private List<List<Trend>> yData;
}
