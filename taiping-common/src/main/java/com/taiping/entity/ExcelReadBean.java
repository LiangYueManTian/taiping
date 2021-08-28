package com.taiping.entity;

import lombok.Data;

/**
 * 导入Excel数据实体父类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
@Data
public class ExcelReadBean {

    /**
     * 是否跳出某个sheet页的循环
     */
    private Integer isReturn;
}
