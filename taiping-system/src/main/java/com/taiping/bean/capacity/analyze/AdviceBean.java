package com.taiping.bean.capacity.analyze;

import lombok.Data;

/**
 * 建议类
 * @author hedongwei@wistronits.com
 * @date 2019/11/11 15:32
 */
@Data
public class AdviceBean {

    /**
     * 建议类型
     */
    private String adviceType;

    /**
     * 运维管理活动
     */
    private String activityCause;
}
