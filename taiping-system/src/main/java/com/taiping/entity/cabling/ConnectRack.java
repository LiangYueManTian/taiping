package com.taiping.entity.cabling;

import com.taiping.entity.ExcelReadBean;
import lombok.Data;

/**
 * 配线架信息表
 * @author hedongwei@wistronits.com
 * @date 2019/10/11 20:57
 */
@Data
public class ConnectRack extends ExcelReadBean {

    /**
     * 配线架id
     */
    private String connectRackId;

    /**
     * 配线架code
     */
    private String connectRackCode;

    /**
     * eic编号
     */
    private String eicNumber;

    /**
     * 序列号
     */
    private String serialNumber;


    /**
     * 配线架名称
     */
    private String connectRackName;

    /**
     * 配线架描述
     */
    private String description;

    /**
     * 厂商
     */
    private String manufacture;

    /**
     * 品牌
     */
    private String logo;

    /**
     * 系列名称
     */
    private String serialInfo;

    /**
     * 型号
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 位置
     */
    private String location;

    /**
     * 所属项目
     */
    private String project;

    /**
     * 所属部门
     */
    private String department;

    /**
     * 所属者
     */
    private String owner;

    /**
     * 重量
     */
    private double weight;

    /**
     * u位高度
     */
    private Integer usedWeight;


    /**
     * 资产编号
     */
    private String assetCode;

    /**
     * 上架时间
     */
    private Long rackingTime;


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
