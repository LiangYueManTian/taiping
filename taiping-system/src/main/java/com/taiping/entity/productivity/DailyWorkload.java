package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 团队负荷实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
@Data
public class DailyWorkload {
    /**
     * ID
     */
    private String workloadId;
    /**
     * 日期
     */
    private Long changeDate;
    /**
     * DMC单号
     */
    private String projectCode;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目总工作量
     */
    private Double totalWorkload;
    /**
     * 当日完成工作量
     */
    private Double dailyWorkload;
    /**
     * 资源状态
     */
    private String workStatus;
    /**
     * 资源状态值
     */
    private Double workStatusValue;
    /**
     * 项目状态
     */
    private String projectStatus;
}
