package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 趋势曲线实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
@Data
public class WorkloadTrendValue {
    /**
     * y轴数据名称
     */
    private String name;
    /**
     * y轴数据值
     */
    private Double value;
}
