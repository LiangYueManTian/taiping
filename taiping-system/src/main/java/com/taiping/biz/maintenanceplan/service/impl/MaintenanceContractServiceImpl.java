package com.taiping.biz.maintenanceplan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.taiping.biz.maintenanceplan.dao.AssetBasicInfoDao;
import com.taiping.biz.maintenanceplan.dao.AssetContractInfoDao;
import com.taiping.biz.maintenanceplan.dao.MaintenanceContractDao;
import com.taiping.biz.maintenanceplan.service.IMaintenanceContractService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产维保合同逻辑层接口实现
 */
@Service
@Slf4j
public class MaintenanceContractServiceImpl implements IMaintenanceContractService {
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
     * 资产基本信息持久层接口
     */
    @Autowired
    private AssetBasicInfoDao assetBasicInfoDao;
    /**
     * 新增资产维保合同
     *
     * @param contract 需新增的资产维保合同
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addMaintenanceContract(MaintenanceContract contract) {
        MaintenanceContract maintenanceContract = getMaintenanceContractInfoByContractNumber(contract.getContractNumber());
        if(!ObjectUtils.isEmpty(maintenanceContract) && maintenanceContract.getIsDeleted() == 0) {
            return ResultUtils.warn(MaintenancePlanResultCode.MAINTENANCE_CONTRACT_HAVE_EXISTED,MaintenancePlanResultMsg.MAINTENANCE_CONTRACT_HAVE_EXISTED);
        }
        List<AssetBasicInfo> assetBasicInfoList = contract.getAssetBasicInfoList();
        List<AssetContractInfo> contractInfoList = Lists.newArrayList();
        contract.setContractId(NineteenUUIDUtils.uuid());
        if(!ObjectUtils.isEmpty(assetBasicInfoList)) {
            assetBasicInfoList.forEach(assetBasicInfo -> {
                AssetContractInfo contractInfo = new AssetContractInfo();
                contractInfo.setId(NineteenUUIDUtils.uuid());
                contractInfo.setAssetInfoId(assetBasicInfo.getAssetInfoId());
                contractInfo.setContractId(contract.getContractId());
                contractInfoList.add(contractInfo);
            });
        }
        contract.setIsDeleted(0);
        contract.setCreateTime(System.currentTimeMillis());
        try {
            if (!ObjectUtils.isEmpty(contractInfoList)) {
                assetContractInfoDao.batchInsertData(contractInfoList);
            }
            contractDao.insert(contract);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("add maintenanceContract error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 修改资产维保合同
     *
     * @param contract 需修改的资产维保合同
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyMaintenanceContract(MaintenanceContract contract) {
        //验证维保计划是否存在
        MaintenanceContract maintenanceContract = contractDao.selectById(contract.getContractId());
        if (ObjectUtils.isEmpty(maintenanceContract) || maintenanceContract.getIsDeleted() == 1) {
            return ResultUtils.warn(MaintenancePlanResultCode.MAINTENANCE_CONTRACT_NOT_EXISTED,MaintenancePlanResultMsg.MAINTENANCE_CONTRACT_NOT_EXISTED);
        }
        EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
        AssetContractInfo contractInfoCond = new AssetContractInfo();
        contractInfoCond.setContractId(contract.getContractId());
        wrapper.setEntity(contractInfoCond);
        List<AssetContractInfo> deleteContractInfoList = assetContractInfoDao.selectList(wrapper);
        List<String> deleteIds = deleteContractInfoList.stream().map(AssetContractInfo::getId).collect(Collectors.toList());
        List<AssetContractInfo> insertContractInfoList = Lists.newArrayList();
        List<AssetBasicInfo> assetBasicInfoList = contract.getAssetBasicInfoList();
        if (!ObjectUtils.isEmpty(assetBasicInfoList)) {
            assetBasicInfoList.forEach(basicInfo -> {
                AssetContractInfo contractInfo = new AssetContractInfo();
                contractInfo.setContractId(contract.getContractId());
                contractInfo.setId(NineteenUUIDUtils.uuid());
                contractInfo.setAssetInfoId(basicInfo.getAssetInfoId());
                insertContractInfoList.add(contractInfo);
            });
        }
        contract.setUpdateTime(System.currentTimeMillis());
        try {
            if (!ObjectUtils.isEmpty(deleteContractInfoList)) {
                assetContractInfoDao.batchDeleteData(deleteIds);
            }
            if (!ObjectUtils.isEmpty(insertContractInfoList)) {
                assetContractInfoDao.batchInsertData(insertContractInfoList);
            }
            contractDao.updateById(contract);
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("modify maintenanceContract error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 批量资产维保合同
     *
     * @param contractIds 资产维保合同id列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDeleteMaintenanceContract(List<String> contractIds) {
        try{
            if (!ObjectUtils.isEmpty(contractIds)) {
                contractDao.deleteBatchIds(contractIds);
            }
            return ResultUtils.success();
        }catch (Exception e) {
            log.error("delete maintenanceContract error", e);
            throw new BizException(MaintenancePlanResultCode.DATABASE_OPERATION_FAIL,MaintenancePlanResultMsg.DATABASE_OPERATION_FAIL);
        }
    }

    /**
     * 获取所有资产维保合同
     *
     * @return 资产维保合同列表
     */
    @Override
    public List<MaintenanceContract> getAllMaintenanceContract() {
        EntityWrapper<MaintenanceContract> wrapper = new EntityWrapper<>();
        MaintenanceContract contract = new MaintenanceContract();
        contract.setIsDeleted(0);
        wrapper.setEntity(contract);
        return contractDao.selectList(wrapper);
    }

    /**
     * 分页查询资产维保合同列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    @Override
    public PageBean getMaintenanceContractByCondition(QueryCondition<MaintenanceContract> queryCondition) {
        List<FilterCondition> filterConditionList = queryCondition.getFilterConditions();
        List<String> contractIds = Lists.newArrayList();
        for (FilterCondition condition : filterConditionList) {
            if (MaintenancePlanConstant.CONTRACT_ID.equals(condition.getFilterField())) {
                contractIds = (List<String>) condition.getFilterValue();
            }
            if (MaintenancePlanConstant.ASSET_INFO_ID.equals(condition.getFilterField())) {
                EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
                wrapper.andNew().in(MaintenancePlanConstant.ASSET_INFO_ID_COLUMN, (Collection<?>) condition.getFilterValue());
                List<AssetContractInfo> assetContractInfoList = assetContractInfoDao.selectList(wrapper);
                List<String> contractList = assetContractInfoList.stream().distinct().map(AssetContractInfo::getContractId).collect(Collectors.toList());
                if (ObjectUtils.isEmpty(contractList)) {
                    contractList.add("");
                }
                contractIds.addAll(contractList);
            }

        }
        FilterCondition filterCondition = new FilterCondition();
        filterCondition.setFilterField(MaintenancePlanConstant.CONTRACT_ID);
        filterCondition.setOperator(MaintenancePlanConstant.OPERATOR_IN);
        filterCondition.setFilterValue(contractIds);
        filterConditionList.removeIf(cond -> MaintenancePlanConstant.ASSET_INFO_ID.equals(cond.getFilterField()) || MaintenancePlanConstant.CONTRACT_ID.equals(cond.getFilterField()));
        filterConditionList.add(filterCondition);
        MaintenanceContract contract = queryCondition.getBizCondition();
        if(ObjectUtils.isEmpty(contract)) {
            contract = new MaintenanceContract();
        }
        contract.setIsDeleted(0);
        //构建分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(queryCondition);
        //构建查询条件
        EntityWrapper entityWrapper = MpQueryHelper.myBatiesBuildQuery(queryCondition);
        //获取查询总数
        int count = contractDao.selectCount(entityWrapper);
        //获取分页查询数据
        List<MaintenanceContract> pageMaintenanceContract = contractDao.selectPage(page, entityWrapper);
        return MpQueryHelper.myBatiesBuildPageBean(page,count,pageMaintenanceContract);
    }

    /**
     * 根据id查询资产维保合同
     *
     * @param contractId 资产维保合同id
     * @return 资产维保合同信息
     */
    @Override
    public MaintenanceContract getMaintenanceContractInfoById(String contractId) {
        MaintenanceContract contract = contractDao.selectById(contractId);
        EntityWrapper<AssetContractInfo> wrapper = new EntityWrapper<>();
        AssetContractInfo contractInfo = new AssetContractInfo();
        contractInfo.setContractId(contractId);
        wrapper.setEntity(contractInfo);
        List<AssetContractInfo> assetContractInfoList = assetContractInfoDao.selectList(wrapper);
        List<String> assetInfoIds = assetContractInfoList.stream().map(AssetContractInfo::getAssetInfoId).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(assetInfoIds)) {
            List<AssetBasicInfo> assetBasicInfoList = assetBasicInfoDao.selectBatchIds(assetInfoIds);
            contract.setAssetBasicInfoList(assetBasicInfoList);
        }
        return contract;
    }

    /**
     * 根据id查询资产维保合同
     *
     * @param contractNumber 资产维保合同编号
     * @return 资产维保合同信息
     */
    @Override
    public MaintenanceContract getMaintenanceContractInfoByContractNumber(String contractNumber) {
        MaintenanceContract contract = new MaintenanceContract();
        contract.setContractNumber(contractNumber);
        return contractDao.selectOne(contract);
    }

}
