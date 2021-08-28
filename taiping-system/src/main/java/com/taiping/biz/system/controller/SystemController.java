package com.taiping.biz.system.controller;

import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.SystemResultCode;
import com.taiping.entity.Result;
import com.taiping.entity.system.BudgetTemp;
import com.taiping.entity.system.SystemInputSetting;
import com.taiping.entity.system.SystemSetting;
import com.taiping.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统Controller
 *
 * @author liyj
 * @date 2019/10/10
 */
@RestController
@RequestMapping("/taiping/system")
public class SystemController {

    /**
     * service
     */
    @Autowired
    private SystemService service;

    /**
     * 获取系统阈值 根据code
     *
     * @param code code 编码
     * @return Result
     */
    @GetMapping("/querySystemValueByCode/{code}")
    public Result querySystemValueByCode(@PathVariable("code") String code) {
        if (StringUtils.isEmpty(code)) {
            return ResultUtils.warn(SystemResultCode.SYSTEM_CODE_IS_NULL, "编码不能为空!");
        }
        return ResultUtils.success(service.querySystemValueByCode(code));
    }

    /**
     * 保存选择的阈值
     *
     * @return
     */
    @PostMapping("/saveSystemInfo")
    public Result updateSystemCodeValue(@RequestBody List<SystemSetting> systemSetting) {
        // TODO: 2019/10/10 参数校验 后续添加
        service.updateSystemCodeValue(systemSetting);
        return ResultUtils.success();
    }

    /**
     * 获取系统阈值 根据code
     *
     * @param code code 编码
     * @return Result
     */
    @GetMapping("/queryInputValueByCode/{code}")
    public Result queryInputValueByCode(@PathVariable("code") String code) {
        return ResultUtils.success(service.queryInputValueByCode(code));
    }

    /**
     * 保存下拉框值
     *
     * @param systemInputSetting systemInputSetting
     * @return
     */
    @PostMapping("/saveInputInfo")
    public Result saveInputInfo(@RequestBody SystemInputSetting systemInputSetting) {
        return ResultUtils.success( service.saveInputInfo(systemInputSetting));
    }

    /**
     * 修改下拉框信息
     *
     * @param systemInputSetting
     * @return
     */
    @PostMapping("/updateInputInfo")
    public Result updateInputInfo(@RequestBody SystemInputSetting systemInputSetting) {
        service.updateInputInfo(systemInputSetting);
        return ResultUtils.success();
    }

    /**
     * 删除deleteInputInfo
     *
     * @return
     */
    @GetMapping("/deleteInputInfo/{code}")
    public Result deleteInputInfoByCode(@PathVariable("code") String code) {
        service.deleteInputInfoByCode(code);
        return ResultUtils.success();
    }


    /**
     * 获取所有的预算模板
     *
     * @return
     */
    @GetMapping("/queryBudgetTemp")
    public Result queryBudgetTemp() {
        return ResultUtils.success(service.queryBudgetTemp());
    }

    /**
     * 修改预算模板
     *
     * @param budgetTemps
     */
    @PostMapping("/updateBudgetTemp")
    public Result updateBudgetTemp(@RequestBody List<BudgetTemp> budgetTemps) {
        service.updateBudgetTemp(budgetTemps);
        return ResultUtils.success();
    }


    /**
     * 删除模板id
     *
     * @param budgetTempId
     */
    @GetMapping("/deleteBudgetTemp/{budgetTempId}")
    public Result deleteBudgetTemp(@PathVariable("budgetTempId") String budgetTempId) {
        service.deleteBudgetTemp(budgetTempId);
        return ResultUtils.success();
    }


    /**
     * 保存预算模板信息
     *
     * @param budgetTemp
     */
    @PostMapping("/saveBudgetTemp")
    public Result saveBudgetTemp(@RequestBody BudgetTemp budgetTemp) {
        service.saveBudgetTemp(budgetTemp);
        return ResultUtils.success();
    }




}
