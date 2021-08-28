package com.taiping.entity;

import lombok.Data;

/**
 * Excel数据开始于结束行数
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
@Data
public class ExcelImportReadBean {
    /**
     * 开始行位置
     */
    private Integer begin;
    /**
     * 结束行位置
     */
    private Integer end;
}
