package com.taiping.bean.user;

import com.taiping.entity.user.MenuInfo;
import com.taiping.entity.user.UserInfo;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/9
 * 用户登录返回结果
 */
public class UserLoginVo {
    /**
     * 用户基础信息
     */
    private UserInfo userInfoBase;

    /**
     * Token
     */
    private String token;

    /**
     * 菜单权限
     */
    private List<MenuInfo> menuPermissions;

    public UserInfo getUserInfoBase() {
        return userInfoBase;
    }

    public void setUserInfoBase(UserInfo userInfoBase) {
        this.userInfoBase = userInfoBase;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MenuInfo> getMenuPermissions() {
        return menuPermissions;
    }

    public void setMenuPermissions(List<MenuInfo> menuPermissions) {
        this.menuPermissions = menuPermissions;
    }
}
