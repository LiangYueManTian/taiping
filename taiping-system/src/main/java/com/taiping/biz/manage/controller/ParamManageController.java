package com.taiping.biz.manage.controller;

import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.constant.manage.ManageActivityResultCode;
import com.taiping.constant.manage.ManageActivityResultMsg;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ParamManage;
import com.taiping.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类型参数管理控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-31
 */
@RestController
@RequestMapping("/taiping/paramManage")
public class ParamManageController {

    @Autowired
    private ParamManageService paramManageService;


    /**
     * 查询模块参数类型
     * @param mode 模块
     * @return Result
     */
    @GetMapping("/queryParamManage/{mode}")
    public Result queryParamManage(@PathVariable String mode) {
        if (StringUtils.isEmpty(mode)) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return paramManageService.queryParamManage(mode);
    }

    /**
     * 提交审批
     * @param mode 模块
     * @return Result
     */
    @GetMapping("/checkSubmit/{mode}")
    public Result checkSubmit(@PathVariable String mode) {
        if (StringUtils.isEmpty(mode)) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return paramManageService.checkSubmit(mode);
    }

    /**
     * 审批
     * @param paramManage 模块、是否通过
     * @return Result
     */
    @PostMapping("/checkApproval")
    public Result checkApproval(@RequestBody ParamManage paramManage) {
        if (paramManage == null || StringUtils.isEmpty(paramManage.getMode())
                || StringUtils.isEmpty(paramManage.getParamType())) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return paramManageService.checkApproval(paramManage);
    }
}
