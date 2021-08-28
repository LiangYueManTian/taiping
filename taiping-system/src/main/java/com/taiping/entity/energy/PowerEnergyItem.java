package com.taiping.entity.energy;

import com.taiping.bean.energy.EnergyBaseBean;
import lombok.Data;

/**
 * 动力能耗功能项数据表
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 15:18
 */
@Data
public class PowerEnergyItem extends EnergyBaseBean {

    /**
     * 动力能耗项编号
     */
    private String powerEnergyId;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 电量仪（度）
     */
    private String electricMeter;

    /**
     * 名称
     */
    private String name;


    /**
     * 类型
     */
    private String type;

    /**
     * 路由
     */
    private String route;

    /**
     * 是不是暖通分项
     */
    private String isHeatItem;

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
