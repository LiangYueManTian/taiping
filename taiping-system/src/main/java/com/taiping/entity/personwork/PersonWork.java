package com.taiping.entity.personwork;

import lombok.Data;

/**
 * 个人工作台
 *
 * @author liyj
 * @date 2019/11/11
 */
@Data
public class PersonWork {
    /**
     * 主键id
     */
    private String id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 处理状态(保留)
     */
    private String handleStatus;
    /**
     * 备注
     */
    private String remark;
}
