package com.taiping.entity.problem;

import lombok.Data;

import java.util.List;
import java.util.Map;
/**
 * 故障分类趋势曲线实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
@Data
public class TroubleTrendDto {
    /**
     * x轴数据
     */
    private List<String> trendDate;
    /**
     * y轴数据
     */
    private Map<String, Map<String, List<Integer>>> trendValue;
}
