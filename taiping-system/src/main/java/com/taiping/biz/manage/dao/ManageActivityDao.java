package com.taiping.biz.manage.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.manage.ManageActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运维管理活动持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
public interface ManageActivityDao {
    /**
     * 新增运维管理活动
     * @param manageActivity 运维管理活动
     * @return int
     */
    int addManageActivity(ManageActivity manageActivity);

    /**
     * 批量插入运维管理活动
     * @param list 运维管理活动
     * @return int
     */
    int insertManageActivityBatch(List<ManageActivity> list);

    /**
     * 取消运维管理活动
     * @param manageActivity 运维管理活动
     * @return int
     */
    int cancelManageTypeById(ManageActivity manageActivity);

    /**
     * 查询提醒运维管理活动
     * @param manageActivity 查询条件
     * @return List<ManageActivity>
     */
    List<ManageActivity> queryManageActivityForTime(ManageActivity manageActivity);

    /**
     * 根据ID查询运维管理活动
     * @param manageId ID
     * @return ManageActivity
     */
    ManageActivity queryManageActivityById(String manageId);

    /**
     * 修改运维管理活动
     * @param manageActivity 运维管理活动
     * @return int
     */
    int updateManageActivityById(ManageActivity manageActivity);

    /**
     * 分页查询运维管理活动
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<ManageActivity>
     */
    List<ManageActivity> selectManageActivityList(@Param("page") PageCondition pageCondition,
                                               @Param("filterList") List<FilterCondition> filterConditionList,
                                               @Param("sort") SortCondition sortCondition);
    /**
     * 查询运维管理活动数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectManageActivityListCount(@Param("filterList") List<FilterCondition> filterConditionList);

    /**
     * 查询模块下对应类型中相同来源对象数据
     * @param mode 模块
     * @param type 类型
     * @param list 运维管理活动
     * @return List<ManageActivity>
     */
    List<ManageActivity> queryCurrentSameSource(@Param("mode") String mode, @Param("type") String type,
                                                @Param("list") List<ManageActivity> list);

    /**
     * 修改模块下多个类型数据为一个类型数据
     * @param mode 模块
     * @param type 类型
     * @param list 类型集合
     * @return int
     */
    int updateManageTypeForType(@Param("mode") String mode, @Param("type") String type,
                                @Param("list") List<String> list);

    /**
     * 修改模块下运维管理活动类型
     * @param mode 模块
     * @param type 类型
     * @param list 运维管理活动
     * @return int
     */
    int updateManageTypeForId(@Param("mode") String mode, @Param("type") String type,
                              @Param("list") List<ManageActivity> list);

    /**
     * 查询模块运维管理活动
     * @param mode 模块
     * @param type 类型
     * @param list 类型
     * @param sourceType 类型
     * @param activityType 运维管理活动
     * @return List<ManageActivity>
     */
    List<ManageActivity> queryManageForMode(@Param("mode") String mode, @Param("list") List<String> list, @Param("sourceType") String sourceType,
                                            @Param("type") String type, @Param("activityType") String activityType);
}
