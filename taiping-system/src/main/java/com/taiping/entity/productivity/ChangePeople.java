package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 变更单单人实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class ChangePeople {
    /**
     * id
     */
    private String id;
    /**
     * id
     */
    private String changeId;
    /**
     * 单人U数
     */
    private Double workload;
    /**
     * 交接人
     */
    private String people;
    /**
     * 开始日期
     */
    private Long startDate;
    /**
     * 结束日期
     */
    private Long endDate;
}
