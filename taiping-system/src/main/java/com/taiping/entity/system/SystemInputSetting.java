package com.taiping.entity.system;

import lombok.Data;

import java.util.List;

/**
 * 系统下拉框设定
 *
 * @author liyj
 * @date 2019/10/17
 */
@Data
public class SystemInputSetting {
    /**
     * 主键id
     */
    private String id;
    /**
     * code 码
     */
    private String code;
    /**
     * codeName
     */
    private String codeName;
    /**
     * 父code
     */
    private String parentCode;
    /**
     * child
     */
    private List<SystemInputSetting> child;
}
