package com.taiping.biz.user.service;

import com.taiping.entity.user.UserInfo;

/**
 * @author zhangliangyu
 * @since 2019/9/26
 * JWT 令牌相关服务接口
 */
public interface ITokenService {
    /**
     * 获取token信息
     * @param user 登录用户信息
     * @return token
     */
    String getToken(UserInfo user);
}
