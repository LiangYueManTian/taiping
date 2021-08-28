package com.taiping.biz.operation.dao;

import com.taiping.entity.operation.HealthyParam;

import java.util.List;

/**
 * 健康卡持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface HealthyDao {
    /**
     * 批量导入健康卡消防水池、冷水机组
     * @param healthyParamList 健康卡消防水池、冷水机组
     * @return int
     */
    int addHealthyParamBatch(List<HealthyParam> healthyParamList);

    /**
     * 查询消防水池、冷水机组
     * @param paramType  消防水池、冷水机组
     * @return List<HealthyParam>
     */
    List<HealthyParam> queryHealthyParam(String paramType);
    /**
     * 查询消防水池、冷水机组
     * @param paramType  消防水池、冷水机组
     * @return List<HealthyParam>
     */
    List<HealthyParam> queryHealthyParamAll();
    /**
     * 删除消防水池、冷水机组
     * @return int
     */
    int deleteHealthyParam();
}
