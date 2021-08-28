package com.taiping.biz.user.controller;

import com.taiping.bean.user.UserGroupDto;
import com.taiping.biz.user.annotation.UserLoginToken;
import com.taiping.biz.user.service.IUserGroupManageService;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.UserGroup;
import com.taiping.constant.user.UserManageResultCode;
import com.taiping.exception.handler.BizExceptionHandler;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户组管理控制器
 */
@RestController
@RequestMapping("/taiping/userGroup")
@Slf4j
public class UserGroupManageController extends BizExceptionHandler {
    /**
     * 用户组管理逻辑层服务
     */
    @Autowired
    private IUserGroupManageService userGroupManageService;

    /**
     * 获取所有用户组列表
     *
     * @return Result<List<UserGroup>> 用户组列表信息
     */
    @UserLoginToken
    @GetMapping("/queryUserGroupList")
    public Result queryUserGroupList() {
        List<UserGroup> userGroups = userGroupManageService.getUserGroupList();
        return ResultUtils.success(userGroups);
    }

    /**
     * 条件查询用户组信息
     *
     * @param queryCondition 查询条件
     * @return 用户组信息列表
     */
    @UserLoginToken
    @PostMapping("/queryUserGroupByCondition")
    public Result queryUserGroupByCondition(@RequestBody QueryCondition<UserGroup> queryCondition) {
        PageBean pageBean = userGroupManageService.queryUserGroupByCondition(queryCondition);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 根据id获取用户组信息
     *
     * @param userGroupId 用户组id
     * @return 用户组信息
     */
    @UserLoginToken
    @GetMapping("/queryUserGroupById/{userGroupId}")
    public Result queryUserGroupById(@PathVariable String userGroupId) {
        UserGroupDto user = userGroupManageService.getUserGroupById(userGroupId);
        return ResultUtils.success(user);
    }

    /**
     * 新建用户组
     *
     * @param userGroup 用户组信息
     * @return 新建结果
     */
    @UserLoginToken
    @PutMapping("/addUserGroup")
    public Result addUserGroup(@RequestBody UserGroupDto userGroup) {
        return userGroupManageService.addUserGroup(userGroup);
    }

    /**
     * 删除用户组
     *
     * @param userGroupIds 用户组id列表
     * @return 删除结果
     */
    @UserLoginToken
    @PostMapping("/deleteUserGroup")
    public Result deleteUserGroup(@RequestBody List<String> userGroupIds) {
        return userGroupManageService.deleteUserGroups(userGroupIds);
    }

    /**
     * 修改用户组
     *
     * @param userGroup 用户组信息
     * @return 修改结果
     */
    @UserLoginToken
    @PostMapping("/updateUserGroup")
    public Result updateUserGroup(@RequestBody UserGroupDto userGroup) {
        return userGroupManageService.updateUserGroup(userGroup);
    }

    /**
     * 验证用户组是否存在
     *
     * @param userGroup 用户组信息
     * @return 验证结果
     */
    @UserLoginToken
    @PostMapping("/checkUserGroupExist")
    public Result checkUserGroupExist(@RequestBody UserGroup userGroup) {
        return userGroupManageService.checkUserGroupExist(userGroup);
    }
}
