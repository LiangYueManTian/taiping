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
 * 资产维保合同实体
 */
@Data
@TableName("t_asset_maintenance_contract")
public class MaintenanceContract extends Model<MaintenanceContract> {
    /**
     * 合同id
     */
    @TableId
    private String contractId;
    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 成本中心
     */
    private String costCenter;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 维保起始时间
     */
    private Long maintenanceStartTime;
    /**
     * 维保结束时间
     */
    private Long maintenanceEndTime;
    /**
     * 维保类型
     */
    private String maintenanceType;
    /**
     * 维保服务方式
     */
    private String maintenanceServiceMode;
    /**
     * 维保厂家
     */
    private String maintenanceFactory;
    /**
     * 承保类型
     */
    private String underwritingType;
    /**
     * 维保联系人
     */
    private String maintenanceContact;
    /**
     * 维保联系电话
     */
    private String maintenanceContactPhone;
    /**
     * 过保提醒
     */
    private String warrantyReminder;
    /**
     * 过保提醒人
     */
    private String warrantyReminderPerson;
    /**
     * 过保提醒人电话
     */
    private String warrantyReminderPhone;
    /**
     * 过保提醒人邮箱
     */
    private String warrantyReminderEmail;
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
     *包含资产信息列表
     */
    @TableField(exist = false)
    private List<AssetBasicInfo> assetBasicInfoList;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.contractId;
    }
}
