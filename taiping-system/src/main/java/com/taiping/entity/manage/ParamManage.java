package com.taiping.entity.manage;

import lombok.Data;

/**
 * 类型参数管理实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-31
 */
@Data
public class ParamManage {
    /**
     * id
     */
    private String paramId;
    /**
     * 模块
     */
    private String mode;
    /**
     * 类型
     */
    private String paramType;
    /**
     * 最近一次时间（分析）
     */
    private Long nearTime;
}
