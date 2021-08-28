package com.taiping.entity.user;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since 2019/9/25
 * 用户组实体
 */
@Data
@TableName("t_user_group")
public class UserGroup extends Model<UserGroup> {
    /**
     * 用户组id
     */
    @TableId("user_group_id")
    private String userGroupId;
    /**
     * 用户组名
     */
    private String userGroupName;
    /**
     * 是否为默认用户组（0：否；1：是）
     */
    private Integer isDefaultGroup;
    /**
     * 描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否被删除
     */
    private Integer isDeleted;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新用户
     */
    private String updateUser;
    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.userGroupId;
    }
}
