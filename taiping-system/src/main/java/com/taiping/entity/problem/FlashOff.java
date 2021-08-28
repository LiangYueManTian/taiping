package com.taiping.entity.problem;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 停水停电记录实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-14
 */
@Data
public class FlashOff extends ExcelReadBean {
    /**
     * id
     */
    private String id;
    /**
     * 闪断类型
     */
    private String offType;
    /**
     * 日期
     */
    private Long offDate;
    /**
     * 原因(停水原因/停电原因)
     */
    private String cause;
    /**
     * 造成影响
     */
    private String influence;
    /**
     * 停水时间/停电时间
     */
    private String startTime;
    /**
     * 来水时间/来电时间
     */
    private String endTime;
    /**
     * 备注
     */
    private String remark;
}
