package com.taiping.biz.capacity.service.analyze;

import com.taiping.bean.capacity.analyze.parameter.CapacityRelatedViewListParameter;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 容量阈值关联显示信息逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:43
 */
public interface CapacityThresholdRelatedViewInfoService {

    /**
     * 阈值关联显示信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:25
     * @param parameter 参数
     * @return 返回阈值关联显示信息集合
     */
    List<CapacityThresholdRelatedViewInfo> queryViewInfoList(CapacityRelatedViewListParameter parameter);

    /**
     * 查询最新的容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的容量阈值关联数据
     */
    CapacityThresholdRelatedViewInfo queryTopDataByTime();


    /**
     * 批量新增容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回容量关联显示信息
     */
    int insertThresholdRelatedViewInfoBatch(@Param("list") List<CapacityThresholdRelatedViewInfo> list);

    /**
     * 新增容量关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量关联显示信息
     */
    int insertThresholdRelatedViewInfo(CapacityThresholdRelatedViewInfo info);
}
