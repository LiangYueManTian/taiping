package com.taiping.biz.manage.dao;

import com.taiping.entity.manage.ParamManage;

/**
 * 类型参数管理持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-31
 */
public interface ParamManageDao {
    /**
     * 修改模块参数类型值
     * @param paramManage 参数类型
     * @return int
     */
    int updateParamManage(ParamManage paramManage);

    /**
     * 查询
     * @param mode 模块
     * @return ParamManage
     */
    ParamManage selectFormMode(String mode);

}
