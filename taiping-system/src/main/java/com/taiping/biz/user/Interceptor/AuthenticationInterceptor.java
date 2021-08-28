package com.taiping.biz.user.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.taiping.bean.user.UserInfoDto;
import com.taiping.biz.user.annotation.PassLoginToken;
import com.taiping.biz.user.annotation.UserLoginToken;
import com.taiping.biz.user.service.IUserInfoManageService;
import com.taiping.constant.user.UserManageConstant;
import com.taiping.constant.user.UserManageResultCode;
import com.taiping.constant.user.UserManageResultMsg;
import com.taiping.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhangliangyu
 * @since 2019/9/26
 * 用户权限拦截器
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    /**
     *用户信息管理逻辑层服务
     */
    @Autowired
    private IUserInfoManageService userInfoManageService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader(UserManageConstant.TOKEN);
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)object;
        Method method = handlerMethod.getMethod();

        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassLoginToken.class)) {
            PassLoginToken passLoginToken = method.getAnnotation(PassLoginToken.class);
            if (passLoginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (ObjectUtils.isEmpty(token)) {
                    throw new BizException(UserManageResultCode.TOKEN_NOT_EXISTED,UserManageResultMsg.TOKEN_NOT_EXISTED);
                }

                // 获取 token 中的 userId
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new BizException(UserManageResultCode.TOKEN_ANALYSIS_ERROR,UserManageResultMsg.TOKEN_ANALYSIS_ERROR);
                }
                UserInfoDto userInfoDto = userInfoManageService.getUserById(userId);
                if (ObjectUtils.isEmpty(userInfoDto)) {
                    throw new BizException(UserManageResultCode.USER_NOT_EXISTED,UserManageResultMsg.USER_NOT_EXISTED);
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userInfoDto.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new BizException(UserManageResultCode.TOKEN_ERROR,UserManageResultMsg.TOKEN_ERROR);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
    }
}
