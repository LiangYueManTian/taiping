package com.taiping.biz.user.dao;

import com.taiping.entity.user.MenuInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/10
 * 菜单权限管理持久层接口
 */
public interface MenuInfoDao {
    /**
     * 获取菜单权限列表
     *
     * @return 菜单权限列表
     */
    List<MenuInfo> getMenuInfoList();

    /**
     * 根据用户组id获取用户组菜单权限
     *
     * @param userGroupId 用户组id
     * @return 用户组菜单权限信息
     */
    List<MenuInfo> getUserGroupMenuInfoByGrouopId(String userGroupId);
}
