package com.taiping.constant.user;

/**
 * @author zhangliangyu
 * @since 2019/11/26
 * 用户管理模块返回Msg定义
 */
public class UserManageResultMsg {
    /**
     * 登录失败，用户名或密码错误
     */
    public static final String LOGIN_FAIL = "用户名或密码错误";

    /**
     * 数据库操作失败
     */
    public static final String DATABASE_OPERATION_FAIL = "数据库操作失败";

    /**
     * 添加用户失败
     */
    public static final String ADD_USER_FAIL = "添加用户失败";

    /**
     * 存在超级用户，用户删除失败
     */
    public static final String EXISTED_SUPER_USER = "存在超级用户，用户删除失败";

    /**
     * 用户已存在
     */
    public static final String USER_EXISTED = "用户已存在";

    /**
     * 原密码输入错误
     */
    public static final String OLD_PASSWORD_ERROR = "原密码输入错误";

    /**
     * 无token
     */
    public static final String TOKEN_NOT_EXISTED = "无token";

    /**
     * token 解析失败
     */
    public static final String TOKEN_ANALYSIS_ERROR = "token解析失败";

    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXISTED = "用户不存在";


    /**
     * 用户组已存在
     */
    public static final String USER_GROUP_EXISTED = "用户组已存在";

    /**
     * 添加用户组失败
     */
    public static final String ADD_USER_GROUP_FAIL = "添加用户组失败";

    /**
     * 存在默认用户组，用户组删除失败
     */
    public static final String EXISTED_DEFAULT_USERGROUP = "存在默认用户组，用户组删除失败";
    /**
     * 用户组中存在用户，用户组删除失败
     */
    public static final String USERGROUP_HAVING_USER = "用户组中存在用户，用户组删除失败";

    /**
     * token错误或token已过期
     */
    public static final String TOKEN_ERROR = "token错误或token已过期";
    /**
     * 用户名或登录账号已存在
     */
    public static final String USERNAME_OR_ACCOUNTNAME_HAVE_EXISTED = "用户名或登录账号已存在";
}
