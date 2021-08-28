package com.taiping.biz.manage.service.impl;

import com.taiping.biz.manage.dao.ParamManageDao;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.constant.manage.ManageActivityResultCode;
import com.taiping.constant.manage.ManageActivityResultMsg;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ParamManage;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * 类型参数管理服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-31
 */
@Service
public class ParamManageServiceImpl implements ParamManageService {

    @Autowired
    private ParamManageDao paramManageDao;

    /**
     * 修改模块参数类型值
     *
     * @param manageSourceEnum 模块
     * @param paramTypeEnum    参数类型
     * @param nearTime         最经分析时间
     */
    @Override
    public void updateParamManage(ManageSourceEnum manageSourceEnum,
                                     ParamTypeEnum paramTypeEnum, Long nearTime) {
        ParamManage paramManage = new ParamManage();
        paramManage.setMode(manageSourceEnum.getCode());
        paramManage.setParamType(paramTypeEnum.getType());
        paramManage.setNearTime(nearTime);
        paramManageDao.updateParamManage(paramManage);
    }

    /**
     * 查询模块参数类型
     *
     * @param mode 模块
     * @return Result
     */
    @Override
    public Result queryParamManage(String mode) {
        ParamManage paramManage = paramManageDao.selectFormMode(mode);
        if (paramManage == null) {
            //配置参数丢失，请联系维护人员
            return ResultUtils.warn(ManageActivityResultCode.PARAM_LOSE,
                    ManageActivityResultMsg.PARAM_LOSE);
        }
        return ResultUtils.success(paramManage);
    }

    /**
     * 查询模块参数类型
     *
     * @param manageSourceEnum 模块
     * @return ParamManage
     */
    @Override
    public ParamManage queryParamManage(ManageSourceEnum manageSourceEnum) {
        return paramManageDao.selectFormMode(manageSourceEnum.getCode());
    }
    /**
     * 是否可以分析
     * @param manageSourceEnum 模块
     * @param nearTime 分析数据日期
     */
    @Override
    public void checkAnalyze(ManageSourceEnum manageSourceEnum, Long nearTime) {
        ParamManage paramManage = paramManageDao.selectFormMode(manageSourceEnum.getCode());
        if (paramManage == null) {
            //配置参数丢失，请联系维护人员
            throw new BizException(ManageActivityResultCode.PARAM_LOSE,
                    ManageActivityResultMsg.PARAM_LOSE);
        }
        String paramType = paramManage.getParamType();
        if (paramType.equals(ParamTypeEnum.APPROVAL_YES.getType())) {
            String[] checkArray = getCheckArray(nearTime);
            String[] checkArrayDb = getCheckArray(paramManage.getNearTime());
            boolean analyze = false;
            for (int i = 0; i < checkArray.length; i++) {
                if (!checkArray[i].equals(checkArrayDb[i])) {
                    analyze = true;
                }
            }
            if (!analyze) {
                // 请导入新的月份数据
                throw new BizException(ManageActivityResultCode.IMPORT_NEW_DATA,
                        ManageActivityResultMsg.IMPORT_NEW_DATA);
            }
            return;
        }
        if (!paramType.equals(ParamTypeEnum.INITIALIZE.getType())) {
            // 数据已分析
            throw new BizException(ManageActivityResultCode.DATA_ALREADY_ANALYZE,
                    ManageActivityResultMsg.DATA_ALREADY_ANALYZE);
        }
    }

    /**
     * 是否预览生成报告
     *
     * @param manageSourceEnum 模块
     */
    @Override
    public void checkPreview(ManageSourceEnum manageSourceEnum) {
        ParamManage paramManage = paramManageDao.selectFormMode(manageSourceEnum.getCode());
        if (paramManage == null) {
            //配置参数丢失，请联系维护人员
            throw new BizException(ManageActivityResultCode.PARAM_LOSE,
                    ManageActivityResultMsg.PARAM_LOSE);
        }
        String paramType = paramManage.getParamType();
        if (paramType.equals(ParamTypeEnum.SUBMIT.getType())) {
            // 已提交审批，无法更改分析报告
            throw new BizException(ManageActivityResultCode.ALREADY_SUBMIT_CHANGE,
                    ManageActivityResultMsg.ALREADY_SUBMIT_CHANGE);
        }
        if (paramType.equals(ParamTypeEnum.APPROVAL_YES.getType())) {
            // 已审批通过，无法更改分析报告
            throw new BizException(ManageActivityResultCode.ALREADY_APPROVAL_CHANGE,
                    ManageActivityResultMsg.ALREADY_APPROVAL_CHANGE);
        }
        if (paramType.equals(ParamTypeEnum.INITIALIZE.getType())) {
            //请导入数据并分析
            throw new BizException(ManageActivityResultCode.IMPORT_DATA_ANALYZE,
                    ManageActivityResultMsg.IMPORT_DATA_ANALYZE);
        }
    }

    /**
     * 提交审批
     *
     * @param mode 模块
     * @return Result
     */
    @Override
    public Result checkSubmit(String mode) {
        ParamManage paramManage = paramManageDao.selectFormMode(mode);
        if (paramManage == null) {
            //配置参数丢失，请联系维护人员
            return ResultUtils.warn(ManageActivityResultCode.PARAM_LOSE,
                    ManageActivityResultMsg.PARAM_LOSE);
        }
        String paramType = paramManage.getParamType();
        if (paramType.equals(ParamTypeEnum.SUBMIT.getType())) {
            // 已提交审批
            return ResultUtils.warn(ManageActivityResultCode.ALREADY_SUBMIT,
                    ManageActivityResultMsg.ALREADY_SUBMIT);
        }
        if (paramType.equals(ParamTypeEnum.APPROVAL_YES.getType()) ||
                paramType.equals(ParamTypeEnum.APPROVAL_NO.getType())) {
            // 已审批
            return ResultUtils.warn(ManageActivityResultCode.ALREADY_APPROVAL,
                    ManageActivityResultMsg.ALREADY_APPROVAL);
        }
        if (paramType.equals(ParamTypeEnum.INITIALIZE.getType())
                || paramType.equals(ParamTypeEnum.ANALYZE.getType())) {
            //请预览生成分析报告
            return ResultUtils.warn(ManageActivityResultCode.TO_PREVIEW,
                    ManageActivityResultMsg.TO_PREVIEW);
        }
        ParamManage paramManageDb = new ParamManage();
        paramManageDb.setMode(mode);
        paramManageDb.setParamType(ParamTypeEnum.SUBMIT.getType());
        paramManageDao.updateParamManage(paramManageDb);
        return ResultUtils.success();
    }

    /**
     * 审批
     *
     * @param paramManage 模块、是否通过
     * @return Result
     */
    @Override
    public Result checkApproval(ParamManage paramManage) {
        ParamManage paramManageDb = paramManageDao.selectFormMode(paramManage.getMode());
        if (paramManageDb == null) {
            //配置参数丢失，请联系维护人员
            return ResultUtils.warn(ManageActivityResultCode.PARAM_LOSE,
                    ManageActivityResultMsg.PARAM_LOSE);
        }
        String paramType = paramManageDb.getParamType();
        if (paramType.equals(ParamTypeEnum.APPROVAL_YES.getType())
                || paramType.equals(ParamTypeEnum.APPROVAL_NO.getType())) {
            // 报告已审批
            return ResultUtils.warn(ManageActivityResultCode.ALREADY_APPROVAL,
                    ManageActivityResultMsg.ALREADY_APPROVAL);
        }
        if (!paramType.equals(ParamTypeEnum.SUBMIT.getType())) {
            //请提交审批
            return ResultUtils.warn(ManageActivityResultCode.TO_SUBMIT,
                    ManageActivityResultMsg.TO_SUBMIT);
        }
        paramManageDao.updateParamManage(paramManage);
        return ResultUtils.success();
    }

    /**
     * 获取时间年份月份
     * @param nearTime 时间戳
     * @return String[]
     */
    private String[] getCheckArray(Long nearTime) {
        String date = new SimpleDateFormat("yyyy-MM").format(nearTime);
        return date.split("-");
    }
}
