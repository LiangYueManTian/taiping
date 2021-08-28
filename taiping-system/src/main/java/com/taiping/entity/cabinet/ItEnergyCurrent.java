package com.taiping.entity.cabinet;

import com.taiping.bean.energy.EnergyBaseBean;
import lombok.Data;

/**
 * IT能耗电流表
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 15:18
 */
@Data
public class ItEnergyCurrent extends EnergyBaseBean {

    /**
     * 电量仪表id
     */
    private String itEnergyId;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 模块
     */
    private String module;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

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
