package com.taiping.entity.cabling;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 综合布线信息表
 * @author hedongwei@wistronits.com
 * @date 2019/10/11 21:08
 */
@Data
public class GenericCabling extends ExcelReadBean {

    /**
     * 综合布线编号
     */
    private String genericCablingId;

    /**
     * 线缆类型
     */
    private String cableType;

    /**
     * 综合布线类型
     */
    private String genericCablingType;

    /**
     * 机柜唯一标识名
     */
    private String cabinetUniqueName;

    /**
     * 本端配线架编号
     */
    private String connectRackCode;

    /**
     * 本端u数
     */
    private Integer usedWeight;

    /**
     * 配线架位置
     */
    private String location;

    /**
     * 本端端口
     */
    private String port;

    /**
     * 对端机柜唯一标识
     */
    private String oppositeEndCabinetName;

    /**
     * 对端配线架编号
     */
    private String oppositeEndRackCode;

    /**
     * 对端u数
     */
    private Integer oppositeUsedWeight;

    /**
     * 配线架位置
     */
    private String oppositeLocation;

    /**
     * 对端端口
     */
    private String oppositeEndPort;

    /**
     * 长度
     */
    private Integer extent;

    /**
     * 状态
     */
    private String status;

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
