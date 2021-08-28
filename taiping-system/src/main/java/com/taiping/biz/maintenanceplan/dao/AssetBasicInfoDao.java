package com.taiping.biz.maintenanceplan.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.maintenanceplan.AssetBasicInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产基本信息持久层接口
 */
public interface AssetBasicInfoDao extends BaseMapper<AssetBasicInfo> {
    /**
     * 批量删除资产基本信息
     *
     * @param ids 资产合同信息id列表
     * @return 删除条数
     */
    int batchDeleteData(List<String> ids);
}
