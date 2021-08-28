package com.taiping.biz.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.taiping.bean.user.UserGroupDto;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.user.UserGroup;
import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户组管理逻辑层接口
 */
public interface IUserGroupManageService extends IService<UserGroup> {
    /**
     * 获取用户组列表
     *
     * @return 用户组列表
     */
    List<UserGroup> getUserGroupList();

    /**
     * 分页查询用户组列表
     *
     * @param queryCondition 查询条件
     * @return 分页结果
     */
    PageBean queryUserGroupByCondition(QueryCondition<UserGroup> queryCondition);

    /**
     * 根据用户id获取用户信息
     *
     * @param userGroupId 用户组id
     * @return 用户组信息
     */
    UserGroupDto getUserGroupById(String userGroupId);

    /**
     * 添加用户组
     *
     * @param userGroupDto 用户组信息
     * @return 添加结果
     */
    Result addUserGroup(UserGroupDto userGroupDto);

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 用户组id列表
     * @return 删除结果
     */
    Result deleteUserGroups(List<String> userGroupIds);

    /**
     * 更新用户组
     *
     * @param userGroupDto 用户信息组
     * @return 更新结果
     */
    Result updateUserGroup(UserGroupDto userGroupDto);

    /**
     * 验证用户组是否已存在
     *
     * @param userGroup 用户组信息
     * @return 验证结果
     */
    Result checkUserGroupExist(UserGroup userGroup);
}
