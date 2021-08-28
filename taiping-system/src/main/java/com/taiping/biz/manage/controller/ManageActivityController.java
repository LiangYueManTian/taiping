package com.taiping.biz.manage.controller;

import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.manage.ManageActivityResultCode;
import com.taiping.constant.manage.ManageActivityResultMsg;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.manage.ManageBean;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运维管理活动控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
@RestController
@RequestMapping("/taiping/manageActivity")
public class ManageActivityController {

    @Autowired
    private ManageActivityService activityService;

    /**
     * 新增运维管理活动
     * @param manageBean 运维管理活动
     * @return Result
     */
    @PostMapping("/addManageActivity")
    public Result addManageActivity(@RequestBody ManageBean manageBean) {
        //校验参数
        if (manageBean == null || manageBean.getManage() == null
                || !manageBean.getManage().checkInsert()) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.addManageActivity(manageBean);
    }

    /**
     * 查看运维管理活动详情
     * @param manageId 运维管理活动ID
     * @return Result
     */
    @GetMapping("/queryManageActivityById/{manageId}")
    public Result queryManageActivityById(@PathVariable String manageId) {
        //校验参数
        if (StringUtils.isEmpty(manageId)) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.queryManageActivityById(manageId);
    }

    /**
     * 查询关联名称
     * @param manageActivity 运维管理活动
     * @return Result
     */
    @PostMapping("/queryManageRelation")
    public Result queryManageRelation(@RequestBody ManageActivity manageActivity) {
        //校验参数
        if (manageActivity == null || StringUtils.isEmpty(manageActivity.getManageId())
                || StringUtils.isEmpty(manageActivity.getActivityType())) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.queryManageRelation(manageActivity);
    }

    /**
     * 管理运维管理活动
     * @param manageBean 运维管理活动
     * @return Result
     */
    @PostMapping("/updateManageActivity")
    public Result updateManageActivity(@RequestBody ManageBean manageBean) {
        //校验参数
        if (manageBean == null || manageBean.getManage() == null
                || !manageBean.getManage().checkInsert()) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.updateManageActivity(manageBean);
    }

    /**
     * 查询运维管理活动列表
     * @param queryCondition 查询条件
     * @return 运维管理活动列表
     */
    @PostMapping("/selectManageActivityList")
    public Result selectManageActivityList(@RequestBody QueryCondition<ManageActivity> queryCondition) {
        //校验参数
        if (queryCondition == null || queryCondition.getPageCondition() == null
                || queryCondition.getFilterConditions() == null) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.selectManageActivityList(queryCondition);
    }

    /**
     * 取消运维管理活动
     * @param manageId 运维管理活动ID
     * @return Result
     */
    @GetMapping("/cancelManageActivity/{manageId}")
    public Result cancelManageActivity(@PathVariable String manageId) {
        //校验参数
        if (StringUtils.isEmpty(manageId)) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.cancelManageActivity(manageId);
    }

    @GetMapping("/test")
    public List<ManageActivity> test() {
        return activityService.queryManageActivityForMode(ManageSourceEnum.CAPACITY);
    }

    /**
     * 查询模块运维管理活动对象
     * @param manageActivity 来源模块
     * @return Result
     */
    @PostMapping("/queryManageActivityForMode")
    public Result queryManageActivityForMode(@RequestBody ManageActivity manageActivity) {
        if (manageActivity == null || StringUtils.isEmpty(manageActivity.getSourceMode())
                || StringUtils.isEmpty(manageActivity.getSourceType())) {
            return ResultUtils.warn(ManageActivityResultCode.PARAM_ERROR,
                    ManageActivityResultMsg.PARAM_ERROR);
        }
        return activityService.queryManageActivityForMode(manageActivity);
    }

}
