package com.taiping.biz.energy.dao.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyThresholdInfoListParameter;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 能耗阈值持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface EnergyThresholdInfoDao {


    /**
     * 查询阈值信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param parameter 参数信息
     * @return 返回阈值信息集合
     */
    List<EnergyThresholdInfo> queryThresholdInfoList(EnergyThresholdInfoListParameter parameter);

    /**
     * 查询最新的能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的空间容量阈值数据
     */
    EnergyThresholdInfo queryTopDataByTime();


    /**
     * 批量新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    int insertThresholdInfoBatch(@Param("list") List<EnergyThresholdInfo> list);

    /**
     * 新增能耗阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回能耗阈值信息
     */
    int insertThresholdInfo(EnergyThresholdInfo info);

    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param energyThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    int updateAdviceInfoListForActivity(@Param("list") List<EnergyThresholdInfo> energyThresholdInfoList);
}
