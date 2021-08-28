package com.taiping.biz.manage.service;

import com.taiping.entity.Result;
import com.taiping.entity.manage.ParamManage;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;

/**
 * 类型参数管理服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-31
 */
public interface ParamManageService {
    /**
     * 修改模块参数类型值
     * @param manageSourceEnum 模块
     * @param paramTypeEnum 参数类型
     * @param nearTime 最经分析时间
     */
    void updateParamManage(ManageSourceEnum manageSourceEnum, ParamTypeEnum paramTypeEnum,
                              Long nearTime);

    /**
     * 查询模块参数类型
     * @param mode 模块
     * @return Result
     */
    Result queryParamManage(String mode);

    /**
     * 查询模块参数类型
     * @param manageSourceEnum 模块
     * @return ParamManage
     */
    ParamManage queryParamManage(ManageSourceEnum manageSourceEnum);

    /**
     * 是否可以分析
     * @param manageSourceEnum 模块
     * @param nearTime 分析数据日期
     */
    void checkAnalyze(ManageSourceEnum manageSourceEnum, Long nearTime);

    /**
     * 是否预览生成报告
     * @param manageSourceEnum 模块
     */
    void checkPreview(ManageSourceEnum manageSourceEnum);

    /**
     * 提交审批
     * @param mode 模块
     * @return Result
     */
    Result checkSubmit(String mode);

    /**
     * 审批
     * @param paramManage 模块、是否通过
     * @return Result
     */
    Result checkApproval(ParamManage paramManage);
}
