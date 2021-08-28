package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 团队负荷衡量值
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
@Data
public class WorkloadParam {
    /**
     * 团队负荷资源状态上限
     */
    private Double supperLimit;
    /**
     * 团队负荷资源状态下限
     */
    private Double lowerLimit;
    /**
     * 最多上架人数
     */
    private Double maxPeople;
}
