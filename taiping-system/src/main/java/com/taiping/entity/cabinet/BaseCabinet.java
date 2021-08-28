package com.taiping.entity.cabinet;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 基础机柜类
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:58
 */
@Data
public class BaseCabinet extends ExcelReadBean {

    /**
     * 机柜基础数据id
     */
    private String cabinetBaseId;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 机柜名称
     */
    private String cabinetName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 机柜唯一标识名
     */
    private String cabinetUniqueName;

    /**
     * pdu设计负荷
     */
    private double ratedPower;


    /**
     * 列头柜设计总负荷
     */
    private double arrayCabinetDesignLoad;

    /**
     * 列头柜名称
     */
    private String arrayCabinetName;

    /**
     * 楼层唯一标识名称
     */
    private String floorUniqueName;

    /**
     * 机柜列
     */
    private String cabinetColumn;

    /**
     * 设计u数
     */
    private Integer designSpaceCapacity;

    /**
     * 是否是配电保留
     */
    private String electricReserve;

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
