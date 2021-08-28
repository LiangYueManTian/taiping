package com.taiping.entity.productivity;

import lombok.Data;

import java.util.List;

/**
 * 分析报告实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-12-10
 */
@Data
public class ProReport {
    /**
     * 团队负荷
     */
    private List<MonthWorkload> workload;
    /**
     * 团队负荷曲线
     */
    private WorkloadTrend trend;
    /**
     * 日期
     */
    private Long reportDate;
}
