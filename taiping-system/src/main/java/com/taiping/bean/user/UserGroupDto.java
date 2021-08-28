package com.taiping.bean.user;

import com.taiping.entity.user.MenuInfo;
import com.taiping.entity.user.UserGroup;
import com.taiping.entity.user.UserInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户组dto
 */
public class UserGroupDto {
    /**
     * 用户组信息
     */
    private UserGroup userGroup;

    /**
     * 用户组菜单权限
     */
    private List<String> menuPermissionCodes;

    /**
     * 组中用户列表
     */
    private List<UserInfo> userInfoList;

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public List<String> getMenuPermissionCodes() {
        return menuPermissionCodes;
    }

    public void setMenuPermissionCodes(List<String> menuPermissionCodes) {
        this.menuPermissionCodes = menuPermissionCodes;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
