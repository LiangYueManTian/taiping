package com.taiping.biz.maintenanceplan.service;

import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.maintenanceplan.MaintenanceContract;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 资产维保合同逻辑层接口
 */
public interface IMaintenanceContractService {
    /**
     * 新增资产维保合同
     *
     * @param contract 需新增的资产维保合同
     * @return 新增结果
     */
    Result addMaintenanceContract(MaintenanceContract contract);

    /**
     * 修改资产维保合同
     *
     * @param contract 需修改的资产维保合同
     * @return 修改结果
     */
    Result modifyMaintenanceContract(MaintenanceContract contract);

    /**
     * 批量资产维保合同
     *
     * @param contractIds 资产维保合同id列表
     * @return 删除结果
     */
    Result batchDeleteMaintenanceContract(List<String> contractIds);

    /**
     * 获取所有资产维保合同
     *
     * @return 资产维保合同列表
     */
    List<MaintenanceContract> getAllMaintenanceContract();

    /**
     * 分页查询资产维保合同列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean getMaintenanceContractByCondition(QueryCondition<MaintenanceContract> queryCondition);

    /**
     * 根据id查询资产维保合同
     *
     * @param contractId 资产维保合同id
     * @return 资产维保合同信息
     */
    MaintenanceContract getMaintenanceContractInfoById(String contractId);

    /**
     * 根据合同编号查询资产维保合同
     *
     * @param contractNumber 资产维保合同编号
     * @return 资产维保合同信息
     */
    MaintenanceContract getMaintenanceContractInfoByContractNumber(String contractNumber);

}
