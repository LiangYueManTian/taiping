package com.taiping.biz.capacity.dao.analyze;

import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 容量阈值持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface CapacityThresholdRelatedInfoDao {


    /**
     * 查询最新的容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的容量阈值关联数据
     */
    CapacityThresholdRelatedInfo queryTopDataByTime();


    /**
     * 批量新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    int insertThresholdRelatedBatch(@Param("list") List<CapacityThresholdRelatedInfo> list);

    /**
     * 新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值关联信息
     */
    int insertThresholdRelated(CapacityThresholdRelatedInfo info);
}
