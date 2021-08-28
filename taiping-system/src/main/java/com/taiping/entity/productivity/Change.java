package com.taiping.entity.productivity;

import lombok.Data;


/**
 * 变更单实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class Change {
    /**
     * ID
     */
    private String changeId;
    /**
     * 变更单号
     */
    private String changeCode;
    /**
     * 项目名称
     */
    private String changeName;
    /**
     * 变更单类型
     */
    private String changeType;
    /**
     * 人数
     */
    private Integer people;
    /**
     * 设备U数
     */
    private Integer workload;
    /**
     * 开始日期
     */
    private Long startDate;
    /**
     * 结束日期
     */
    private Long endDate;
    /**
     * 历史交接人
     */
    private String handoverPeople;
    /**
     * 项目状态
     */
    private String projectStatus;
}
