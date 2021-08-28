package com.taiping.biz.maintenanceplan.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.maintenanceplan.AssetContractInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/12/4
 * 资产合同信息持久层接口
 */
public interface AssetContractInfoDao extends BaseMapper<AssetContractInfo> {
    /**
     * 批量插入资产合同信息
     *
     * @param assetContractInfoList 需添加的资产合同信息列表
     * @return 添加条数
     */
    int batchInsertData(List<AssetContractInfo> assetContractInfoList);

    /**
     * 批量删除资产合同信息
     *
     * @param ids 资产合同信息id列表
     * @return 删除条数
     */
    int batchDeleteData(List<String> ids);
}
