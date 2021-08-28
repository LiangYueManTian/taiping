package com.taiping.biz.maintenanceplan.service;

import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.AssetBasicInfo;
import com.taiping.entity.maintenanceplan.MaintenancePlan;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产基本信息逻辑层接口
 */
public interface IAssetBasicInfoService {
    /**
     * 新增资产基本信息
     *
     * @param assetBasicInfo 需新增的资产基本信息
     * @return 新增结果
     */
    Result addAssetBasicInfo(AssetBasicInfo assetBasicInfo);

    /**
     * 修改资产基本信息
     *
     * @param assetBasicInfo 需修改的资产基本信息
     * @return 修改结果
     */
    Result modifyAssetBasicInfo(AssetBasicInfo assetBasicInfo);

    /**
     * 批量删除资产基本信息
     *
     * @param basicInfoIds 资产基本信息id列表
     * @return 删除结果
     */
    Result batchDeleteAssetBasicInfo(List<String> basicInfoIds);

    /**
     * 获取所有资产基本信息
     *
     * @return 资产基本信息列表
     */
    List<AssetBasicInfo> getAllAssetBasicInfo();

    /**
     * 分页查询资产基本信息列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean getAssetBasicInfoByCondition(QueryCondition<AssetBasicInfo> queryCondition);

    /**
     * 根据id查询资产基本信息
     *
     * @param basicInfoId 资产基本信息id
     * @return 资产基本信息
     */
    AssetBasicInfo getAssetBasicInfoById(String basicInfoId);

    /**
     * 根据资产编号查询资产基本信息
     *
     * @param assetNumber 资产基本信息id
     * @return 资产基本信息
     */
    AssetBasicInfo getAssetBasicInfoByAssetNumber(String assetNumber);

}
