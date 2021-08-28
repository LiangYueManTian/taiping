package com.taiping.entity.operation;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 健康卡消防蓄水池实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Data
public class FireReservoir extends ExcelReadBean {
    /**
     * 名称
     */
    private String name;
    /**
     * 行位置
     */
    private Integer rowNumber;
    /**
     * 消防蓄水池可储水量(合计)
     */
    private Double waterCapacity;
    /**
     * 水位
     */
    private String waterLevel;
    /**
     * 消防主备蓄水池可供水冷机组冷却系统补水量
     */
    private Double waterSupplement;
    /**
     * 自然冷却模式（板换运行）可供冷却系统补水量持续使用时长（h）
     */
    private Double naturalCooling;
    /**
     * 电制冷模式（冷机运行）当前负载 可供冷却系统补水量持续使用时长（h）
     */
    private Double electricRefCurrent;
    /**
     * 电制冷模式（冷机运行）满载 可供冷却系统补水量持续使用时长（h）
     */
    private Double electricRefFull;
}