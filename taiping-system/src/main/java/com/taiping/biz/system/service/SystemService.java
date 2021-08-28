package com.taiping.biz.system.service;

import com.taiping.entity.system.BudgetTemp;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.system.SystemSetting;

import java.util.List;

/**
 * @author liyj
 * @date 2019/10/10
 */
public interface SystemService {
    /**
     * @param code 编码
     * @return
     */
    List<SystemSetting> querySystemValueByCode(String code);

    /**
     * 修改系统默认配置阀值
     *
     * @param systemSetting 阈值
     */
    void updateSystemCodeValue(List<SystemSetting> systemSetting);

    /**
     * 查询input Value 值
     *
     * @param code code
     * @return Result
     */
    List<SystemInputSetting> queryInputValueByCode(String code);

    /**
     * 保存input 信息
     */
    SystemInputSetting saveInputInfo(SystemInputSetting systemInputSetting);

    /**
     * 修改系统input 设定值
     *
     * @param systemInputSetting
     */
    void updateInputInfo(SystemInputSetting systemInputSetting);

    /**
     * 删除
     *
     * @param code
     */
    void deleteInputInfoByCode(String code);

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
     * 保存预算模板信息
     *
     * @param budgetTemp
     */
    void saveBudgetTemp(BudgetTemp budgetTemp);
}
