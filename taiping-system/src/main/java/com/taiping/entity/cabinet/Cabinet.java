package com.taiping.entity.cabinet;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 机柜信息类
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:58
 */
@Data
public class Cabinet extends ExcelReadBean {

    /**
     * 机柜id
     */
    private String cabinetId;

    /**
     * 机柜唯一标识名
     */
    private String cabinetUniqueName;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 机柜列
     */
    private String cabinetColumn;

    /**
     * 柜名
     */
    private String cabinetName;

    /**
     * 机柜位置
     */
    private String cabinetLocation;

    /**
     * 设计设备空间容量
     */
    private int designSpaceCapacity;

    /**
     * 使用的空间容量
     */
    private int usedSpaceCapacity;

    /**
     * 剩余空间容量
     */
    private int unusedSpaceCapacity;

    /**
     * 已使用的空间容量占比
     */
    private double usedSpaceCapacityPercent;

    /**
     * 额定功率
     */
    private double ratedPower;

    /**
     * 已用额定功率
     */
    private double usedRatedPower;

    /**
     * 额定已用功率百分比
     */
    private double usedRatedPercent;

    /**
     * 已用实际功率
     */
    private double usedActualPower;

    /**
     * 实际已用百分比
     */
    private double usedActualPercent;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 数据采集时间
     */
    private Long dataCollectionTime;

    /**
     * 是否删除  0 未删除 1 删除
     */
    private Integer isDeleted;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Long updateTime;

}
