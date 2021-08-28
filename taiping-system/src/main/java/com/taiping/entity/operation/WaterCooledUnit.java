package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 健康卡水冷机组运行时长实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class WaterCooledUnit extends ExcelReadBean {
    /**
     * 水冷机组名称
     */
    private String unitName;
    /**
     * 运行时长（月末累计）
     */
    private Double beginRuntime;
    /**
     * 运行时长（当月实际）
     */
    private Double currentRuntime;
    /**
     * 运行时长（月末累计）
     */
    private Double endRuntime;
}