package com.taiping.biz.capacity.service.analyze;


import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 容量阈值信息逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:43
 */
public interface CapacityThresholdInfoService {

    /**
     * 查询阈值信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param capacityThresholdInfo 参数信息
     * @return 返回阈值信息集合
     */
    List<CapacityThresholdInfo> queryThresholdInfoList(CapacityThresholdInfo capacityThresholdInfo);

    /**
     * 查询最新的容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的空间容量阈值数据
     */
    CapacityThresholdInfo queryTopDataByTime();


    /**
     * 批量新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    int insertThresholdInfoBatch(@Param("list") List<CapacityThresholdInfo> list);

    /**
     * 新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值信息
     */
    int insertThresholdInfo(CapacityThresholdInfo info);


    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param capacityThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    int updateAdviceInfoListForActivity(@Param("list") List<CapacityThresholdInfo> capacityThresholdInfoList);

}
