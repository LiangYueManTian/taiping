package com.taiping.entity.system;

import lombok.Data;

import java.util.List;

/**
 * 系统设置
 *
 * @author liyj
 * @date 2019/10/10
 */
@Data
public class SystemSetting {
    /**
     * 主键id
     */
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 编码名称
     */
    private String codeName;

    /**
     * 父类Code
     */
    private String parentCode;
    /**
     * 阈值
     */
    private Object value;
    /**
     * checkStatus 是否选中  true选中/false 置空
     */
    private boolean checkStatus;
    /**
     * 子集
     */
    List<SystemSetting> child;
}
