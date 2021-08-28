package com.taiping.entity.productivity;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 巡检实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-14
 */
@Data
public class InspectionTask extends ExcelReadBean {
    /**
     * id
     */
    private String inspectionId;
    /**
     * 巡检工单号
     */
    private String inspectionCode;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 区域位置
     */
    private String location;
    /**
     * 频率
     */
    private String frequency;
    /**
     * 巡检组
     */
    private String inspectionGroup;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 计划开始时间
     */
    private String planStartTime;
    /**
     * 计划结束时间
     */
    private String planEndTime;
    /**
     * 允许提前时间
     */
    private String allowLeadTime;
    /**
     * 允许延迟时间
     */
    private String allowDelayTime;
    /**
     * 计划用时
     */
    private String planUseTime;
    /**
     * 实际用时
     */
    private String actualUseTime;
    /**
     * 实际开始时间
     */
    private String actualStartTime;
    /**
     * 实际结束时间
     */
    private Long actualEndTime;
}
