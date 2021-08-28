package com.taiping.entity.operation;

import lombok.Data;

/**
 * 重要沟通事项跟踪实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-12-05
 */
@Data
public class CommunicateIssues {
    /**
     * 事项主键ID
     */
    private String issuesId;
    /**
     * 事项描述
     */
    private String issuesDescribe;
    /**
     * 所在位置
     */
    private String location;
    /**
     * 物业方跟踪人
     */
    private String propertyPeople;
    /**
     * 计划完成时间
     */
    private Long planTime;
    /**
     * 实际完成时间
     */
    private Long actualTime;
    /**
     * 目前的进展完成情况
     */
    private String completionStatus;
    /**
     * 沟通内容
     */
    private String communicateContent;
}
