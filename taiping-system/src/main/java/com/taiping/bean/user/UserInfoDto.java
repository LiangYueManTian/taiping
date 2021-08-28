package com.taiping.bean.user;

import com.taiping.entity.user.UserInfo;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户信息dto
 */
public class UserInfoDto extends UserInfo{
    /**
     * 用户组名称
     */
    private String userGroupName;

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}
