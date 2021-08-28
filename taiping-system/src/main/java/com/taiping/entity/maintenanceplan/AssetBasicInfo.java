package com.taiping.entity.maintenanceplan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产基本信息实体
 */
@Data
@TableName("t_asset_basic_info")
public class AssetBasicInfo extends Model<AssetBasicInfo> {
    /**
     * 主键id
     */
    @TableId
    private String assetInfoId;
    /**
     * 资产编号
     */
    private String assetNumber;
    /**
     * 资产名称
     */
    private String assetName;
    /**
     * 资产分类
     */
    private String assetType;
    /**
     * 资产状态
     */
    private String assetStatus;
    /**
     * 厂商分类
     */
    private String vendorType;
    /**
     * 序列号
     */
    private String serialNumber;
    /**
     * 资产描述
     */
    private String assetDescription;
    /**
     * 资产位置
     */
    private String assetLocation;

    /**
     * 所属项目
     */
    private String subordinateItem;
    /**
     * 额定功率
     */
    private Double ratedPower;
    /**
     * 所属部门
     */
    private String subordinateDept;
    /**
     * 所有者
     */
    private String owner;
    /**
     * 所有者电话
     */
    private String ownerPhone;
    /**
     * 所有者邮箱
     */
    private String ownerEmail;
    /**
     * 上架时间
     */
    private Long rackingTime;
    /**
     * 是否被删除
     */
    private Integer isDeleted;
    /**
     *创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新用户
     */
    private String updateUser;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     *关联维保合同列表
     */
    @TableField(exist = false)
    private List<MaintenanceContract> contracts;
    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.assetInfoId;
    }
}
