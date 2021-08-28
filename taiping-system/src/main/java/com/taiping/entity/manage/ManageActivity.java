package com.taiping.entity.manage;

import com.taiping.utils.CheckInputString;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 运维管理活动实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
@Data
public class ManageActivity {
    /**
     * id
     */
    private String manageId;
    /**
     * 类型
     */
    private String manageType;
    /**
     * 来源对象名称
     */
    private String sourceName;
    /**
     * 来源对象code
     */
    private String sourceCode;
    /**
     *来源模块
     */
    private String sourceMode;
    /**
     *来源类型
     */
    private String sourceType;
    /**
     *产生原因
     */
    private String cause;
    /**
     *产生日期
     */
    private Long createDate;
    /**
     *创建时间
     */
    private Long createTime;
    /**
     *负责人ID
     */
    private String responsibleId;
    /**
     *负责人
     */
    private String responsiblePerson;
    /**
     *预计完成时间
     */
    private Long completionTime;
    /**
     * 更改时间原因
     */
    private String changeReason;
    /**
     * 更改原因记录
     */
    private String changeReasonTxt;
    /**
     *是否提醒
     */
    private String isRemind;
    /**
     *运维管理活动类型
     */
    private String activityType;
    /**
     * 运维管理活动名称
     */
    private String activityName;
    /**
     * 处理说明
     */
    private String solveInstruction;
    /**
     *建议
     */
    private String advise;
    /**
     *完成状态
     */
    private String completionStatus;
    /**
     *复核人id
     */
    private String reviewerId;
    /**
     *复核人
     */
    private String reviewer;
    /**
     *复核状态
     */
    private String approvalStatus;
    /**
     *复核说明
     */
    private String reviewInstruction;
    /**
     * 是否可以裁剪
     */
    private String isReduce;
    /**
     * 更新时间
     */
    private Long updateTime;

    public boolean checkInsert() {
        cause = CheckInputString.markCheck(cause);
        if (StringUtils.isEmpty(cause) || createDate == null) {
            return false;
        }
        activityName = CheckInputString.markCheck(activityName);
        if (StringUtils.isEmpty(activityName)) {
            return false;
        }
        return checkUpdate();
    }

    public boolean checkUpdate() {
        if (StringUtils.isNotEmpty(solveInstruction)) {
            solveInstruction = CheckInputString.markCheck(solveInstruction);
            if (StringUtils.isEmpty(solveInstruction)) {
                return false;
            }
        }
        if (StringUtils.isNotEmpty(reviewInstruction)) {
            reviewInstruction = CheckInputString.markCheck(reviewInstruction);
            if (StringUtils.isEmpty(reviewInstruction)) {
                return false;
            }
        }
        if (StringUtils.isNotEmpty(changeReason)) {
            changeReason = CheckInputString.markCheck(changeReason);
            if (StringUtils.isEmpty(changeReason)) {
                return false;
            }
        }
        return checkSelect();
    }

    private boolean checkSelect() {
        if (StringUtils.isEmpty(isRemind) || StringUtils.isEmpty(activityType)) {
            return false;
        }
        return !StringUtils.isEmpty(completionStatus) && !StringUtils.isEmpty(approvalStatus);
    }
}
