package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 团队负荷月份统计数据同比环比实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
@Data
public class MonthWorkloadDto {

    /**
     * 空闲
     */
    private String freePercentage;
    /**
     * 正常
     */
    private String normalPercentage;
    /**
     * 满负荷
     */
    private String fullPercentage;
}
