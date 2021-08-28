package com.taiping.biz.system.dao;

import com.taiping.entity.system.BudgetTemp;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.system.SystemSetting;

import java.util.List;

/**
 * @author liyj
 * @date 2019/10/10
 */
public interface SystemDao {
    /**
     * 获取全部的系统编码
     *
     * @return
     */
    List<SystemSetting> queryAllSystemCode();

    /**
     * 修改系统默认配置阀值
     *
     * @param systemSetting
     */
    void updateSystemCodeValue(List<SystemSetting> systemSetting);

    /**
     * 获取子集最大的Code
     *
     * @param parentCode 父code
     * @return
     */
    Integer getChildMaxCodeByParentCode(String parentCode);

    /**
     * 删除InputCode 框
     *
     * @param code code码
     */
    void deleteInputCode(String code);

    /**
     * 查询所有的input 预设信息
     *
     * @return
     */
    List<SystemInputSetting> queryAllInputInfo();

    /**
     * 保存input 信息
     */
    void saveInputInfo(SystemInputSetting systemInputSetting);

    /**
     * 修改系统input 设定值
     *
     * @param systemInputSetting
     */
    void updateInputInfo(SystemInputSetting systemInputSetting);


    /**
     * 获取预算模板
     *
     * @return
     */
    List<BudgetTemp> queryBudgetTemp();

    /**
     * 修改预算模板
     *
     * @param budgetTemps
     */
    void updateBudgetTemp(List<BudgetTemp> budgetTemps);

    /**
     * 删除模板id
     *
     * @param budgetTempId
     */
    void deleteBudgetTemp(String budgetTempId);

    /**
     * 保存预算模板
     *
     * @param budgetTemp
     */
    void saveBudgetTemp(BudgetTemp budgetTemp);

    /**
     * 判断是否有下级
     * @param code
     * @return
     */
    Integer checkInputByCode(String code);
}



