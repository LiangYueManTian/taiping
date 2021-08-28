package com.taiping.entity.user;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * @author zhangliangyu
 * @since  2019/9/25
 * 用户信息实体
 */
@Data
@TableName("t_user_info")
public class UserInfo extends Model<UserInfo> {
    /**
     * 用户id
     */
    @TableId("user_id")
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录账号
     */
    private String accountName;
    /**
     * 姓名
     */
    private String userRealName;
    /**
     * 员工编号
     */
    private String userNumber;
    /**
     * 密码
     */
    private String password;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 是否是超级用户（0：否；1：是）
     */
    private Integer isSuperUser;
    /**
     * 是否被删除
     */
    private Integer isDeleted;
    /**
     *创建用户
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
     * 用户组id
     */
    private String userGroupId;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.userId;
    }
}
