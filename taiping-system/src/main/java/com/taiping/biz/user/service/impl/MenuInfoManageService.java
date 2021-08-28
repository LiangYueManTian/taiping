package com.taiping.biz.user.service.impl;

import com.taiping.biz.user.dao.MenuInfoDao;
import com.taiping.biz.user.service.IMenuInfoManageService;
import com.taiping.entity.user.MenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 菜单管理逻辑层实现
 */
@Service
public class MenuInfoManageService implements IMenuInfoManageService {

    /**
     * 菜单权限管理持久层
     */
    @Autowired
    private MenuInfoDao menuInfoDao;

    /**
     * 获取菜单权限列表
     *
     * @return 菜单列表
     */
    @Override
    public List<MenuInfo> getMenuInfoList() {
        return menuInfoDao.getMenuInfoList();
    }

    /**
     * 获取用户组菜单权限
     *
     * @param userGroupId 用户组id
     * @return 菜单列表
     */
    @Override
    public List<MenuInfo> getUserGroupPermissionByGroupId(String userGroupId) {
        return menuInfoDao.getUserGroupMenuInfoByGrouopId(userGroupId);
    }
}
