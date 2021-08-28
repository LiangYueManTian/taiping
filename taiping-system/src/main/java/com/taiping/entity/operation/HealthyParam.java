package com.taiping.entity.operation;


import lombok.Data;
/**
 * 健康卡参数实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class HealthyParam {
    /**
     * 参数ID
     */
    private String paramId;
    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 参数值
     */
    private String paramValue;
    /**
     * 参数衡量值
     */
    private String standardValue;
}
