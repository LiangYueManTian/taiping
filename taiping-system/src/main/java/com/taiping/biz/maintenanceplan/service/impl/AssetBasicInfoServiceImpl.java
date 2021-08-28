package com.taiping.biz.maintenanceplan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.taiping.biz.maintenanceplan.dao.AssetBasicInfoDao;
import com.taiping.biz.maintenanceplan.dao.AssetContractInfoDao;
import com.taiping.biz.maintenanceplan.dao.MaintenanceContractDao;
import com.taiping.biz.maintenanceplan.service.IAssetBasicInfoService;
import com.taiping.constant.maintenance.MaintenancePlanConstant;
import com.taiping.constant.maintenance.MaintenancePlanResultCode;
import com.taiping.constant.maintenance.MaintenancePlanResultMsg;
import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.AssetBasicInfo;
import com.taiping.entity.maintenanceplan.AssetContractInfo;
import com.taiping.entity.maintenanceplan.MaintenanceContract;
import com.taiping.entity.maintenanceplan.MaintenancePlan;
import com.taiping.exception.BizException;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产基本信息逻辑层接口实现
 */
@Service
@Slf4j
public class AssetBasicInfoServiceImpl implements IAssetBasicInfoService {
    /**
     * 资产基本信息持久层接口
     */
    @Autowired
    private AssetBasicInfoDao assetBasicInfoDao;
    /**
     * 资产维保合同持久层接口
     */
    @Autowired
    private MaintenanceContractDao contractDao;
    /**
     * 资产维保合同持久层接口
     */
    @Autowired
    private AssetContractInfoDao assetContractInfoDao;
    /**
     * 新增资产基本信息
     *
     * @param assetBasicInfo 需新增的资产基本信息
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAssetBasicInfo(AssetBasicInfo assetBasicInfo) {
        AssetBasicInfo basicInfo = getAssetBasicInfoByAssetNumber(assetBasicInfo.getAssetNumber());
        if(!ObjectUtils.isEmpty(basicInfo) && basicInfo.getIsDeleted() == 0) {
            return ResultUtils.warn(MaintenancePlanResultCode.ASSEST_BASIC_INFO_HAVE_EXISTED,MaintenancePlanResultMsg.ASSEST_BASIC_INFO_HAVE_EXISTED);
        }
        List<MaintenanceContract> maintenanceContracts = assetBasicInfo.getContracts();
        List<AssetContractInfo> assetContractInfoList = Lists.newArrayList();
        assetBasicInfo.setAssetInfoId(NineteenUUIDUtils.uuid());
        if(!ObjectUtils.isEmpty(maintenanceContracts)) {
            maintenanceContracts.forEach(contract -> {
                AssetContractInfo contractInfo = new AssetContractInfo();
                contractInfo.setAssetInfoId(assetBasicInfo.getAssetInfoId());
                contractInfo.setContractId(contract.getContractId());
                contractInfo.setId(NineteenUUIDUtils.uuid());
                assetContractInfoList.add(contractInfo);
            });
        }
        assetBasicInfo.setIsDeleted(0);
        assetBasicInfo.setCreateTime(System.currentTimeMillis());
        try {
            if (!ObjectUtils.isEmpty(assetContractInfoList)) {
                assetContractInfoDao.batchInsertData(assetContractInfoList);
            }
            assetBasicInfoDao.insert(assetBasicInfo);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add assetBasicInfo error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 修改资产基本信息
     *
     * @param assetBasicInfo 需修改的资产基本信息
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyAssetBasicInfo(AssetBasicInfo assetBasicInfo) {
        //验证维保计划是否存在
        AssetBasicInfo basicInfo = assetBasicInfoDao.selectById(assetBasicInfo.getAssetInfoId());
        if (ObjectUtils.isEmpty(basicInfo) || basicInfo.getIsDeleted() == 1) {
            return ResultUtils.warn(MaintenancePlanResultCode.ASSEST_BASIC_INFO_NOT_EXISTED,MaintenancePlanResultMsg.ASSEST_BASIC_INFO_NOT_EXISTED);
        }
        EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
        AssetContractInfo contractInfoCond = new AssetContractInfo();
        contractInfoCond.setAssetInfoId(assetBasicInfo.getAssetInfoId());
        wrapper.setEntity(contractInfoCond);
        List<AssetContractInfo> deleteContractInfoList = assetContractInfoDao.selectList(wrapper);
        List<String> deleteIds = deleteContractInfoList.stream().map(AssetContractInfo::getId).collect(Collectors.toList());
        List<AssetContractInfo> insertContractInfoList = Lists.newArrayList();
        List<MaintenanceContract> contractList = assetBasicInfo.getContracts();
        if (!ObjectUtils.isEmpty(contractList)) {
            contractList.forEach(contract -> {
                AssetContractInfo contractInfo = new AssetContractInfo();
                contractInfo.setId(NineteenUUIDUtils.uuid());
                contractInfo.setContractId(contract.getContractId());
                contractInfo.setAssetInfoId(assetBasicInfo.getAssetInfoId());
                insertContractInfoList.add(contractInfo);
            });
        }
        assetBasicInfo.setUpdateTime(System.currentTimeMillis());
        try {
            if (!ObjectUtils.isEmpty(deleteContractInfoList)) {
                assetContractInfoDao.batchDeleteData(deleteIds);
            }
            if (!ObjectUtils.isEmpty(insertContractInfoList)) {
                assetContractInfoDao.batchInsertData(insertContractInfoList);
            }
            assetBasicInfoDao.updateById(assetBasicInfo);
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify assetBasicInfo error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 批量资产基本信息
     *
     * @param basicInfoIds 资产基本信息id列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDeleteAssetBasicInfo(List<String> basicInfoIds) {
        try{
            //删除资产基本信息
            if (!ObjectUtils.isEmpty(basicInfoIds)) {
                assetBasicInfoDao.batchDeleteData(basicInfoIds);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("delete assetBasicInfo error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 获取所有资产基本信息
     *
     * @return 资产基本信息列表
     */
    @Override
    public List<AssetBasicInfo> getAllAssetBasicInfo() {
        EntityWrapper<AssetBasicInfo> wrapper = new EntityWrapper<>();
        AssetBasicInfo basicInfo = new AssetBasicInfo();
        basicInfo.setIsDeleted(0);
        wrapper.setEntity(basicInfo);
        return assetBasicInfoDao.selectList(wrapper);
    }

    /**
     * 分页查询资产基本信息列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean getAssetBasicInfoByCondition(QueryCondition<AssetBasicInfo> queryCondition) {
        List<FilterCondition> filterConditionList = queryCondition.getFilterConditions();
        List<String> assetInfoIds = Lists.newArrayList();
        for (FilterCondition condition : filterConditionList) {
            if (MaintenancePlanConstant.ASSET_INFO_ID.equals(condition.getFilterField())) {
                assetInfoIds = (List<String>) condition.getFilterValue();
            }
            if (MaintenancePlanConstant.CONTRACT_ID.equals(condition.getFilterField())) {
                EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
                wrapper.andNew().in(MaintenancePlanConstant.CONTRACT_ID_COLUMN, (Collection<?>) condition.getFilterValue());
                List<AssetContractInfo> assetContractInfoList = assetContractInfoDao.selectList(wrapper);
                List<String>  assetInfoIdList = assetContractInfoList.stream().distinct().map(AssetContractInfo::getAssetInfoId).collect(Collectors.toList());
                if (ObjectUtils.isEmpty(assetInfoIdList)) {
                    assetInfoIdList.add("");
                }
                assetInfoIds.addAll(assetInfoIdList);
            }

        }
        FilterCondition filterCondition = new FilterCondition();
        filterCondition.setFilterField(MaintenancePlanConstant.ASSET_INFO_ID);
        filterCondition.setOperator(MaintenancePlanConstant.OPERATOR_IN);
        filterCondition.setFilterValue(assetInfoIds);
        filterConditionList.removeIf(cond -> MaintenancePlanConstant.ASSET_INFO_ID.equals(cond.getFilterField()) || MaintenancePlanConstant.CONTRACT_ID.equals(cond.getFilterField()));
        filterConditionList.add(filterCondition);
        AssetBasicInfo basicInfo = queryCondition.getBizCondition();
        if(ObjectUtils.isEmpty(basicInfo)) {
           basicInfo = new AssetBasicInfo();
        }
        basicInfo.setIsDeleted(0);
        //构建分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        //构建查询条件
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        //获取查询总数
        int count = assetBasicInfoDao.selectCount(entityWrapper);
        //获取分页查询数据
        List<AssetBasicInfo> pageAssetBasicInfo = assetBasicInfoDao.selectPage(page, entityWrapper);
        return MpQueryHelper.myBatiesBuildPageBean(page,count,pageAssetBasicInfo);
    }

    /**
     * 根据id查询资产基本信息
     *
     * @param basicInfoId 资产基本信息id
     * @return 资产基本信息
     */
    @Override
    public AssetBasicInfo getAssetBasicInfoById(String basicInfoId) {
        AssetBasicInfo assetBasicInfo = assetBasicInfoDao.selectById(basicInfoId);
        EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
        AssetContractInfo contractInfo = new AssetContractInfo();
        contractInfo.setAssetInfoId(basicInfoId);
        wrapper.setEntity(contractInfo);
        List<AssetContractInfo> assetContractInfoList = assetContractInfoDao.selectList(wrapper);
        List<String> contractIds = assetContractInfoList.stream().map(AssetContractInfo::getContractId).collect(Collectors.toList());
        if(!ObjectUtils.isEmpty(contractIds)) {
            List<MaintenanceContract> contractList = contractDao.selectBatchIds(contractIds);
            assetBasicInfo.setContracts(contractList);
        }
        return assetBasicInfo;
    }

    /**
     * 根据资产编号查询资产基本信息
     *
     * @param assetNumber 资产基本信息id
     * @return 资产基本信息
     */
    @Override
    public AssetBasicInfo getAssetBasicInfoByAssetNumber(String assetNumber) {
        AssetBasicInfo basicInfo = new AssetBasicInfo();
        basicInfo.setAssetNumber(assetNumber);
        return assetBasicInfoDao.selectOne(basicInfo);
    }

}
