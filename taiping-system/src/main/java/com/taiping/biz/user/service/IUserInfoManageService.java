package com.taiping.biz.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.taiping.bean.user.ModifyPasswordVo;
import com.taiping.bean.user.UserInfoDto;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.UserInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/9/25
 * 用户信息管理逻辑层接口
 */
public interface IUserInfoManageService extends IService<UserInfo> {

    /**
     * 用户登录
     *
     * @param user 登录用户
     * @return 登录结果
     */
    Result userLogin(UserInfo user);
    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    List<UserInfo> getUserInfoList();

    /**
     * 分页查询用户列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean queryUserInfoByCondition(QueryCondition<UserInfo> queryCondition);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfoDto getUserById(String userId);

    /**
     * 添加用户
     *
     * @param userInfo 用户信息
     * @return 添加结果
     */
    Result addUser(UserInfo userInfo);

    /**
     * 批量删除用户
     *
     * @param userIds 用户id列表
     * @return 删除结果
     */
    Result deleteUsers(List<String> userIds);

    /**
     * 更新用户
     *
     * @param userInfo 用户信息
     * @return 更新结果
     */
    Result updateUser(UserInfo userInfo);

    /**
     * 修改密码
     *
     * @param modifyPasswordVo 修改密码对象
     * @return 修改结果
     */
    Result modifyPassword(ModifyPasswordVo modifyPasswordVo);

    /**
     * 验证用户是否存在
     *
     * @param user 待验证用户
     * @return 验证结果
     */
    Result checkUserExist(UserInfo user);

}
