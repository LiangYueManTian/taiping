package com.taiping.biz.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taiping.entity.user.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/9/25
 * 用户信息持久层接口
 */
public interface UserInfoDao extends BaseMapper<UserInfo> {

    /**
     * 获取用户信息列表
     *
     * @return 用户信息列表
     */
    List<UserInfo> getUserInfoList();

    /**
     * 根据id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfo getUserInfoById(String userId);

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return 用户信息
     */
    UserInfo getUserInfoByUserName(String userName);

    /**
     * 根据姓名获取用户信息
     *
     * @param userRealName 姓名
     * @return 用户信息
     */
    UserInfo getUserInfoByRealName(String userRealName);

    /**
     * 根据登录账号获取用户信息
     *
     * @param accountName 登录账号
     * @return 用户信息
     */
    UserInfo getUserInfoByAccountName(String accountName);

    /**
     * 根据员工编号获取用户信息
     *
     * @param userNumber 登录账号
     * @return 用户信息
     */
    UserInfo getUserInfoByUserNumber(String userNumber);

    /**
     * 添加用户
     *
     * @param userInfo 新用户信息
     * @return 添加数据条数
     */
    int addUser(UserInfo userInfo);

    /**
     * 批量删除用户
     *
     * @param userIds 用户id列表
     * @return 删除数据条数
     */
    int deleteUser(List<String> userIds);

    /**
     * 更新用户信息
     *
     * @param userInfo 修改后的用户信息
     * @return 更新数据条数
     */
    int updateUser(UserInfo userInfo);

    /**
     * 获取超级用户
     *
     * @return 超级用户信息
     */
    UserInfo getSuperUser();

    /**
     * 用户登录
     *
     * @param loginUser 登录信息
     * @return 用户信息
     */
    UserInfo userLogin(UserInfo loginUser);

    /**
     * 修改密码
     *
     * @param userId 用户id
     * @param password 新密码
     */
    int modifyPassword(@Param("userId") String userId,@Param("password") String password);
}
