package com.taiping.entity.problem;

import lombok.Data;

/**
 * 问题分析查询统计实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-18
 */
@Data
public class ProblemSelect {
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;
}
