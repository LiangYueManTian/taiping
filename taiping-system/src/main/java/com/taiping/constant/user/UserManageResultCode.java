package com.taiping.constant.user;

/**
 * @author zhangliangyu
 * @since 2019/10/11
 * 用户管理模块返回码定义
 */
public class UserManageResultCode {

    /**
     * 登录失败，用户名或密码错误
     */
    public static final Integer LOGIN_FAIL = 120000;

    /**
     * 数据库操作失败
     */
    public static final Integer DATABASE_OPERATION_FAIL = 110001;

    /**
     * 添加用户失败
     */
    public static final Integer ADD_USER_FAIL = 120002;

    /**
     * 删除用户失败
     */
    public static final Integer DELETE_USER_FAIL = 120003;

    /**
     * 修改用户失败
     */
    public static final Integer UPDATE_USER_FAIL = 120004;
    /**
     * 用户已存在
     */
    public static final Integer USER_EXISTED = 120005;

    /**
     * 原密码输入错误
     */
    public static final Integer OLD_PASSWORD_ERROR = 120006;

    /**
     * 无token
     */
    public static final Integer TOKEN_NOT_EXISTED = 120007;

    /**
     * token 解析失败
     */
    public static final Integer TOKEN_ANALYSIS_ERROR = 120008;

    /**
     * 用户不存在
     */
    public static final Integer USER_NOT_EXISTED = 120009;


    /**
     * 用户组已存在
     */
    public static final Integer USER_GROUP_EXISTED = 120010;

    /**
     * 添加用户组失败
     */
    public static final Integer ADD_USER_GROUP_FAIL = 120011;

    /**
     * 删除用户组失败
     */
    public static final Integer DELETE_USER_GROUP_FAIL = 120012;


    /**
     * token错误或token已过期
     */
    public static final Integer TOKEN_ERROR = 120013;
    /**
     * 用户名或登录账号已存在
     */
    public static final Integer USERNAME_OR_ACCOUNTNAME_HAVE_EXISTED = 120014;
}
