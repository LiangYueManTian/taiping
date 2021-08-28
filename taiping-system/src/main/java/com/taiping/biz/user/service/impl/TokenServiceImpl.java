package com.taiping.biz.user.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.taiping.biz.user.service.ITokenService;
import com.taiping.constant.user.UserManageConstant;
import com.taiping.entity.user.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangliangyu
 * @since 2019/9/26
 * JWT 令牌相关服务接口实现
 */
@Service
public class TokenServiceImpl implements ITokenService {
    /**
     * 获取token信息
     *
     * @param user 登录用户信息
     * @return token
     */
    @Override
    public String getToken(UserInfo user) {
        //签发时间
        Date iatDate = new Date();
        //过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 24);
        Date expiresTime = nowTime.getTime();

        return JWT.create().withAudience(user.getUserId())
                .withClaim(UserManageConstant.USER_NAME,user.getUserName())
                .withExpiresAt(expiresTime)
                .withIssuedAt(iatDate)
                .sign(Algorithm.HMAC256(user.getPassword()));
    }
}
