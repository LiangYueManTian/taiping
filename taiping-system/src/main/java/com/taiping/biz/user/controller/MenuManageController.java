package com.taiping.biz.user.controller;

import com.taiping.biz.user.annotation.UserLoginToken;
import com.taiping.biz.user.service.IMenuInfoManageService;
import com.taiping.entity.Result;
import com.taiping.entity.user.MenuInfo;
import com.taiping.exception.handler.BizExceptionHandler;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/taiping/menu")
@Slf4j
public class MenuManageController extends BizExceptionHandler {

    /**
     * 菜单管理逻辑层服务
     */
    @Autowired
    private IMenuInfoManageService menuInfoManageService;

    /**
     * 获取所有菜单列表
     *
     * @return Result<List<MenuInfo>> 用户组列表信息
     */
    @UserLoginToken
    @GetMapping("/queryMenuList")
    public Result queryMenuList() {
        List<MenuInfo> menuInfos = menuInfoManageService.getMenuInfoList();
        return ResultUtils.success(menuInfos);
    }
}
