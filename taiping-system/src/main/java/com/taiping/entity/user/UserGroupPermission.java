package com.taiping.entity.user;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/10/10
 * 用户组菜单权限实体
 */
@Data
@TableName("t_user_group_permission")
public class UserGroupPermission {

    /**
     * 权限id
     */
    private String permissionId;
    /**
     * 用户组id
     */
    private String userGroupId;
    /**
     * 菜单id
     */
    private String menuId;
}
