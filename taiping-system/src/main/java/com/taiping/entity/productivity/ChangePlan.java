package com.taiping.entity.productivity;

import com.taiping.utils.CheckInputString;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 变更计划实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class ChangePlan{
    /**
     * 变更计划ID
     */
    private String changeId;
    /**
     * 项目提交人
     */
    private String reportPerson;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 设备型号（U）
     */
    private Integer deviceType;
    /**
     * 设备数量
     */
    private Integer deviceNumber;
    /**
     * 机房预计上架日期
     */
    private Long expectStartTime;
    /**
     * 机房预计完成日期
     */
    private Long expectEndTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Long createTime;


    public boolean check() {
        reportPerson = CheckInputString.nameCheck(reportPerson);
        projectName = CheckInputString.nameCheck(projectName);
        if (StringUtils.isEmpty(reportPerson) ||
                StringUtils.isEmpty(projectName)) {
            return false;
        }
        if (StringUtils.isNotEmpty(remark)) {
            remark = CheckInputString.markCheck(remark);
            if (StringUtils.isEmpty(remark)) {
                return false;
            }
        }
        if (expectStartTime == null ||
                expectEndTime == null) {
            return false;
        }
        return deviceNumber != null;
    }
}
