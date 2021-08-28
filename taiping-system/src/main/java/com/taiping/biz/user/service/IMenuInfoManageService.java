package com.taiping.biz.user.service;

import com.taiping.entity.user.MenuInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 菜单管理逻辑层接口
 */
public interface IMenuInfoManageService {
    /**
     * 获取菜单权限列表
     *
     * @return 菜单列表
     */
    List<MenuInfo> getMenuInfoList();

    /**
     * 获取用户组菜单权限
     *
     * @param userGroupId 用户组id
     * @return 菜单列表
     */
    List<MenuInfo> getUserGroupPermissionByGroupId(String userGroupId);
}
