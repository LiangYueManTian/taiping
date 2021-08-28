package com.taiping.biz.manage.service;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.manage.ManageBean;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageSourceTypeEnum;
import com.taiping.enums.manage.ManageStatusEnum;

import java.util.List;

/**
 * 运维管理活动服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
public interface ManageActivityService {

    /**
     * 运维管理活动提醒
     */
    void reminderManageActivity();

    /**
     * 新增运维管理活动
     * @param manageBean 运维管理活动
     * @return Result
     */
    Result addManageActivity(ManageBean manageBean);

    /**
     * 查看运维管理活动详情
     * @param manageId 运维管理活动ID
     * @return Result
     */
    Result queryManageActivityById(String manageId);

    /**
     * 查询关联名称
     * @param manageActivity 运维管理活动
     * @return Result
     */
    Result queryManageRelation(ManageActivity manageActivity);

    /**
     * 管理运维管理活动
     * @param manageBean 运维管理活动
     * @return Result
     */
    Result updateManageActivity(ManageBean manageBean);

    /**
     * 查询运维管理活动列表
     * @param queryCondition 查询条件
     * @return 运维管理活动列表
     */
    Result selectManageActivityList(QueryCondition<ManageActivity> queryCondition);
    /**
     * 查询运维管理活动列表数量
     * @param queryCondition 查询条件
     * @return 运维管理活动列表
     */
    Integer selectManageActivityListCount(QueryCondition<ManageActivity> queryCondition);

    /**
     * 取消运维管理活动
     * @param manageId 运维管理活动ID
     * @return Result
     */
    Result cancelManageActivity(String manageId);

    /**
     * 生成运维管理活动
     * @param manageActivityList 运维管理活动List
     * @param manageSourceEnum 来源模块
     */
    void insertManageActivity(ManageSourceEnum manageSourceEnum, List<ManageActivity> manageActivityList);

    /**
     * 生成运维管理活动，不查询相同对象
     * @param manageActivityList 运维管理活动List
     * @param manageSourceEnum 来源模块
     */
    void insertManageActivityNoEquals(ManageSourceEnum manageSourceEnum, List<ManageActivity> manageActivityList);

    /**
     * 生成运维管理活动对象
     * @param manageId id
     * @param sourceName 来源对象名称
     * @param sourceCode  来源对象code
     * @param createTime 创建时间
     * @param advise 建议
     * @param manageSourceEnum 来源模块
     * @param manageStatusEnum 是否可以裁剪
     * @param cause 产生原因
     * @param createDate 产生日期
     * @return ManageActivity
     */
    ManageActivity createManageActivity(String manageId, String sourceName, String sourceCode, Long createTime,
                                        String advise, String cause,  ManageStatusEnum manageStatusEnum,
                                        ManageSourceEnum manageSourceEnum, Long createDate);
    /**
     * 生成运维管理活动对象
     * @param manageId id
     * @param sourceName 来源对象名称
     * @param sourceCode  来源对象code
     * @param createTime 创建时间
     * @param advise 建议
     * @param manageSourceEnum 来源模块
     * @param manageStatusEnum 是否可以裁剪
     * @param cause 产生原因
     * @param createDate 产生日期
     * @param sourceTypeEnum 来源分析类型
     * @return ManageActivity
     */
    ManageActivity createManageActivity(String manageId, String sourceName, String sourceCode, Long createTime,
                                        String advise, String cause, ManageStatusEnum manageStatusEnum,
                                        ManageSourceEnum manageSourceEnum,  Long createDate, ManageSourceTypeEnum sourceTypeEnum);

    /**
     * 查询模块运维管理活动对象
     * @param manageSourceEnum 来源模块
     * @return  List<ManageActivity>
     */
    List<ManageActivity> queryManageActivityForMode(ManageSourceEnum manageSourceEnum);

    /**
     * 查询模块运维管理活动对象
     * @param manageActivity 来源模块
     * @return Result
     */
    Result queryManageActivityForMode(ManageActivity manageActivity);
}
