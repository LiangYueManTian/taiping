package com.taiping.biz.system.service.impl;

import com.google.common.collect.Lists;
import com.taiping.biz.system.dao.SystemDao;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.SystemResultCode;
import com.taiping.entity.system.BudgetTemp;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.system.SystemSetting;
import com.taiping.exception.BizException;
import com.taiping.utils.NineteenUUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyj
 * @date 2019/10/10
 */
@Service
public class SystemServiceImpl implements SystemService {

    /**
     * dao
     */
    @Autowired
    private SystemDao systemDao;

    /**
     * @param code 编码
     * @return
     */
    @Override
    public List<SystemSetting> querySystemValueByCode(String code) {
        //1 获取全部的阈值
        List<SystemSetting> allSystemCode = systemDao.queryAllSystemCode();
        // 2 根据 code 找到parent
        List<SystemSetting> resultData = Lists.newArrayList();

        allSystemCode.forEach(obj -> {
            if (code.equals(obj.getParentCode())) {
                //递归找到自己的子集
                resultData.add(getchild(obj, allSystemCode));
            }
        });
        return resultData;
    }

    /**
     * 修改系统默认配置阀值
     *
     * @param systemSetting 阈值
     */
    @Override
    public void updateSystemCodeValue(List<SystemSetting> systemSetting) {
        //todo: 这里的校验
        systemDao.updateSystemCodeValue(systemSetting);
    }

    /**
     * 查询input Value 值
     *
     * @param code code
     * @return Result
     */
    @Override
    public List<SystemInputSetting> queryInputValueByCode(String code) {
        //1 获取全部的阈值
        List<SystemInputSetting> allInputInfo = systemDao.queryAllInputInfo();
        // 2 根据 code 找到parent
        List<SystemInputSetting> resultData = Lists.newArrayList();

        allInputInfo.forEach(obj -> {
            if (code.equals(obj.getParentCode())) {
                //递归找到自己的子集
                resultData.add(getSystemInput(obj, allInputInfo));
            }
        });
        return resultData;

    }

    /**
     * 保存input 信息
     *
     * @param systemInputSetting
     */
    @Override
    public  SystemInputSetting saveInputInfo(SystemInputSetting systemInputSetting) {
        //1 保存这里的需要知道parentCode  通过这个获取最新的值 加10
        String parentCode = systemInputSetting.getParentCode();
        if (StringUtils.isEmpty(parentCode)) {
            parentCode = "root";
        }
        Integer maxCodeByParentCode = systemDao.getChildMaxCodeByParentCode(parentCode);
        if (maxCodeByParentCode != null) {
            maxCodeByParentCode = maxCodeByParentCode + 1;
        } else {
            //给一个初始值
            if ("root".equals(parentCode)) {
                maxCodeByParentCode = 10;
            } else {
                //父code 后面加 乘100 加10
                maxCodeByParentCode = Integer.parseInt(parentCode) * 100 + 10;
            }
        }

        systemInputSetting.setCode(maxCodeByParentCode.toString());
        systemInputSetting.setId(NineteenUUIDUtils.uuid());
        systemDao.saveInputInfo(systemInputSetting);
        //返回Data 中加 code 和parentCode
        return systemInputSetting;
    }

    /**
     * 修改系统input 设定值
     *
     * @param systemInputSetting
     */
    @Override
    public void updateInputInfo(SystemInputSetting systemInputSetting) {
        systemDao.updateInputInfo(systemInputSetting);
    }

    /**
     * 删除
     *
     * @param code
     */
    @Override
    public void deleteInputInfoByCode(String code) {
        // 这里增加规则 校验 是否有子集
        Integer codeStatus = systemDao.checkInputByCode(code);
        if (codeStatus == null || codeStatus == 0) {
            systemDao.deleteInputCode(code);
        } else {
            throw new BizException(SystemResultCode.SYSTEM_INPUT_PRENT_CODE_ERROR, "该下拉框有下级 请先删除下级菜单!");
        }
    }

    /**
     * 获取预算模板
     *
     * @return
     */
    @Override
    public List<BudgetTemp> queryBudgetTemp() {
        return systemDao.queryBudgetTemp();
    }

    /**
     * 修改预算模板
     *
     * @param budgetTemps
     */
    @Override
    public void updateBudgetTemp(List<BudgetTemp> budgetTemps) {
        systemDao.updateBudgetTemp(budgetTemps);
    }

    /**
     * 删除模板id
     *
     * @param budgetTempId
     */
    @Override
    public void deleteBudgetTemp(String budgetTempId) {
        systemDao.deleteBudgetTemp(budgetTempId);
    }

    /**
     * 保存预算模板信息
     *
     * @param budgetTemp
     */
    @Override
    public void saveBudgetTemp(BudgetTemp budgetTemp) {
        // TODO: 2019/11/8 后面看时候需要批量增加
        budgetTemp.setId(NineteenUUIDUtils.uuid());
        systemDao.saveBudgetTemp(budgetTemp);
    }


    /**
     * 获取系统input 预设值
     *
     * @param parentCode 父code
     * @param allData    所有数据
     * @return SystemInputSetting
     */
    private SystemInputSetting getSystemInput(SystemInputSetting parentCode, List<SystemInputSetting> allData) {
        allData.forEach(obj -> {
            if (parentCode.getCode().equals(obj.getParentCode())) {
                if (parentCode.getChild() == null) {
                    parentCode.setChild(new ArrayList<>());
                }
                parentCode.getChild().add(getSystemInput(obj, allData));
            }
        });
        return parentCode;
    }

    /**
     * 获取子集
     *
     * @param parentCode
     * @param allData
     * @return
     */
    private SystemSetting getchild(SystemSetting parentCode, List<SystemSetting> allData) {
        allData.forEach(obj -> {
            if (parentCode.getCode().equals(obj.getParentCode())) {
                if (parentCode.getChild() == null) {
                    parentCode.setChild(new ArrayList<>());
                }
                parentCode.getChild().add(getchild(obj, allData));
            }
        });
        return parentCode;
    }

}
