package com.taiping.biz.user.controller;

import com.taiping.bean.user.ModifyPasswordVo;
import com.taiping.bean.user.UserInfoDto;
import com.taiping.bean.user.UserLoginVo;
import com.taiping.biz.user.annotation.UserLoginToken;
import com.taiping.biz.user.service.ITokenService;
import com.taiping.biz.user.service.IUserInfoManageService;
import com.taiping.biz.user.service.impl.MenuInfoManageService;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.MenuInfo;
import com.taiping.entity.user.UserInfo;
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
 * @since 2019/9/25
 * 用户管理控制器
 */
@RestController
@RequestMapping("/taiping/user")
@Slf4j
public class UserManageController extends BizExceptionHandler {

    /**
     * 用户信息管理逻辑层服务
     */
    @Autowired
    private IUserInfoManageService userInfoManageService;


    /**
     * 获取所有用户列表
     *
     * @return Result<List<UserInfo>> 用户列表信息
     */
    @UserLoginToken
    @GetMapping("/queryUserInfoList")
    public Result queryUserInfoList() {
        List<UserInfo> userInfoList = userInfoManageService.getUserInfoList();
        return ResultUtils.success(userInfoList);
    }

    /**
     * 条件查询用户信息
     *
     * @param queryCondition 查询条件
     * @return 用户信息列表
     */
    @UserLoginToken
    @PostMapping("/queryUserInfoByCondition")
    public Result queryUserInfoByCondition(@RequestBody QueryCondition<UserInfo> queryCondition) {
        PageBean pageBean = userInfoManageService.queryUserInfoByCondition(queryCondition);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 根据id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @UserLoginToken
    @GetMapping("/queryUserById/{userId}")
    public Result queryUserById(@PathVariable String userId) {
        UserInfoDto user = userInfoManageService.getUserById(userId);
        return ResultUtils.success(user);
    }

    /**
     * 用户登录
     *
     * @param userInfo 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserInfo userInfo) {
        return userInfoManageService.userLogin(userInfo);
    }

    /**
     * 新建用户
     *
     * @param userInfo 用户信息
     * @return 新建结果
     */
    @UserLoginToken
    @PutMapping("/addUser")
    public Result addUser(@RequestBody UserInfo userInfo) {
        return userInfoManageService.addUser(userInfo);
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @return 删除结果
     */
    @UserLoginToken
    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestBody List<String> userIds) {
        return userInfoManageService.deleteUsers(userIds);
    }

    /**
     * 修改用户
     *
     * @param userInfo 用户信息
     * @return 修改结果
     */
    @UserLoginToken
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UserInfo userInfo) {
        return userInfoManageService.updateUser(userInfo);
    }

    /**
     * 修改密码
     *
     * @param modifyPasswordVo 修改密码对象
     * @return 修改结果
     */
    @UserLoginToken
    @PostMapping("/modifyPassword")
    public Result modifyPassword(@RequestBody ModifyPasswordVo modifyPasswordVo) {
        return userInfoManageService.modifyPassword(modifyPasswordVo);
    }

    /**
     * 验证用户是否存在
     *
     * @param user 待验证用户
     * @return 验证结果
     */
    @UserLoginToken
    @PostMapping("/checkUserExist")
    public Result checkUserExist(@RequestBody UserInfo user) {
        return userInfoManageService.checkUserExist(user);
    }

//    /**
//     * 用户登出
//     *
//     * @return
//     */
//    @UserLoginToken
//    @GetMapping("/logout")
//    public Result logOut(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        System.out.println(token);
//        if (token != null) {
//            request.getHeader("token").replace(token,null);
//        }
//        return ResultUtils.success();
//    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
