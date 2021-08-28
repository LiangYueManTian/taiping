package com.taiping.entity;

/**
 * <p>
 * 系统共用返回码定义
 * 子服务返回码定义规范如下：
 * <p>
 * 公共         10****
 * 用户模块：   12****
 * 容量模块     13****
 * 能耗模块     14****
 * 风险管理     15****
 * 系统设置     16****
 * 运维管理     17****
 * 变更管理     18****
 * 生产力分析   19****
 * 问题管理     20****
 * 维护保养计划 21****
 * 预算预采购   22****
 * <p>
 * 例如用户不存在可以定义为： 120000
 * 后续增加服务编号递增
 * <p>
 * </p>
 *
 * @author yuanyao@wistronits.com
 * create on 2019/1/7 14:10
 */
public class ResultCode {

    /**
     * 请求成功
     */
    public static final Integer SUCCESS = 0;

    public static final Integer FAIL = -1;

    /**
     * 未登录
     */
    public static final Integer NOT_LOGIN = -2;

}
