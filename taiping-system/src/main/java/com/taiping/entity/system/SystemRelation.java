package com.taiping.entity.system;

import lombok.Data;

/**
 * 系统关系表
 *
 * @author liyj
 * @date 2019/10/10
 */
@Data
@Deprecated
public class SystemRelation {
    /**
     * 主键id
     */
    private String id;
    /**
     * code 唯一值
     */
    private String code;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 处理类型 枚举 HandleEnum(0 无 1 运维活动 2 告警) 多个选择已 ， 分割开
     */
    private String handleType;
    /**
     * 阈值
     */
    private Object value;
}
