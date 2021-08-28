package com.taiping.entity.problem;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;
/**
 * 故障单实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
@Data
public class TroubleTicket extends ExcelReadBean {
    /**
     * id
     */
    private String ticketId;
    /**
     * 故障单号
     */
    private String ticketCode;
    /**
     * 报障人
     */
    private String reportPerson;
    /**
     * 故障来源
     */
    private String troubleSource;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
     */
    private String createPerson;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 故障发生时间
     */
    private Long troubleTime;
    /**
     * 故障发生时间月份开始时间
     */
    private Long troubleTimeMonth;
    /**
     * 故障发生地点
     */
    private String troubleLocation;
    /**
     * 故障分类
     */
    private String troubleType;
    /**
     * 故障一级分类
     */
    private String topType;
    /**
     * 故障二级分类
     */
    private String secondaryType;
    /**
     * 故障名称
     */
    private String troubleName;
    /**
     * 描述
     */
    private String description;
    /**
     * 影响度
     */
    private String influence;
    /**
     * 紧急度
     */
    private String urgency;
    /**
     * 故障级别
     */
    private String troubleLevel;
    /**
     * 故障处理描述
     */
    private String handleDescription;
    /**
     * 故障解决描述
     */
    private String solveDescription;
    /**
     * 处理组
     */
    private String handleGroup;
    /**
     * 处理人
     */
    private String handlePerson;
    /**
     * 故障原因定位
     */
    private String troubleCause;
    /**
     * 解决人
     */
    private String solvePerson;
    /**
     * 解决时间
     */
    private String solveTime;
    /**
     * 关闭类型
     */
    private String closeType;
    /**
     * 关闭人
     */
    private String closePerson;
    /**
     * 关闭时间
     */
    private String closeTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 造成服务中断时长
     * interrupt_duration
     */
    private Double interruptDuration;
    /**
     * 类型(0导入1新增)
     */
    private String valueType;
    /**
     * 故障单所属（0物业1太平）
     */
    private String ticketType;
}
