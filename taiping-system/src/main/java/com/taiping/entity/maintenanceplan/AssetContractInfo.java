package com.taiping.entity.maintenanceplan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/12/3
 * 资产合同信息实体
 */
@Data
@TableName("t_asset_related_contract")
public class AssetContractInfo extends Model<AssetContractInfo> {
    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 资产基本信息id
     */
    private String assetInfoId;
    /**
     *维保合同id
     */
    private String contractId;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
