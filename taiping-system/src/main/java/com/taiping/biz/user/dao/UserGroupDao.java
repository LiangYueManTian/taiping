package com.taiping.biz.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.bean.user.UserGroupDto;
import com.taiping.entity.user.UserGroup;
import com.taiping.entity.user.UserGroupPermission;
import com.taiping.entity.user.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/10
 * 用户组管理持久层接口
 */
public interface UserGroupDao extends BaseMapper<UserGroup> {
    /**
     * 获取用户组列表
     *
     * @return 用户组列表
     */
    List<UserGroup> getUserGroupList();

    /**
     * 根据用户组id获取用户组
     *
     * @param userGroupId 用户组id
     * @return 用户组信息
     */
    UserGroup getUserGroupById(String userGroupId);

    /**
     * 根据用户组名获取用户组
     *
     * @param userGroupName 用户组名
     * @return 用户组信息
     */
    UserGroup getUserGroupByName(String userGroupName);

    /**
     * 根据用户组id获取用户组中用户列表
     *
     * @param userGroupId 用户组id
     * @return 用户列表
     */
    List<UserInfo> getUserListByUserGroupId(String userGroupId);

    /**
     * 获取默认用户组
     *
     * @return 默认用户组
     */
    UserGroup getDefaultUserGroup();

    /**
     * 添加用户组
     *
     * @param userGroup 用户组信息
     * @return 添加数据条数
     */
    int addUserGroup(UserGroup userGroup);

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 用户组id列表
     * @return 删除数据条数
     */
    int deleteUserGroups(List<String> userGroupIds);

    /**
     * 修改用户组
     *
     * @param userGroup 用户组信息
     * @return 更新数据条数
     */
    int updateUserGroup(UserGroup userGroup);

    /**
     * 批量删除用户组菜单权限
     *
     * @param userGroupIds 用户组id列表
     * @return 数据删除条数
     */
    int deleteUserGroupMenuPermission(List<String> userGroupIds);

    /**
     * 添加用户组菜单权限
     *
     * @param userGroupPermissions 修改的用户组菜单权限列表
     * @return 数据添加条数
     */
    int addUserGroupMenuPermission(List<UserGroupPermission> userGroupPermissions);
}
