package com.taiping.biz.energy.dao.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeDeleteParameter;
import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeInfoListParameter;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 能耗分析阈值持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface EnergyAnalyzeInfoDao {


    /**
     * 查询能耗分析阈值信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:16
     * @param parameter 参数
     * @return 返回阈值信息集合
     */
    List<EnergyAnalyzeInfo> queryThresholdInfoList(EnergyAnalyzeInfoListParameter parameter);

    /**
     * 查询最新的能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的能耗分析阈值数据
     */
    EnergyAnalyzeInfo queryTopDataByTime();


    /**
     * 批量新增能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回阈值信息
     */
    int insertThresholdInfoBatch(@Param("list") List<EnergyAnalyzeInfo> list);

    /**
     * 新增能耗分析阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回容量阈值信息
     */
    int insertThresholdInfo(EnergyAnalyzeInfo info);

    /**
     * 批量修改运维管理活动类型和处理说明
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:39
     * @param energyThresholdInfoList 修改的对象
     * @return 批量修改运维管理活动类型和处理说明
     */
    int updateAdviceInfoListForActivity(@Param("list") List<EnergyAnalyzeInfo> energyThresholdInfoList);


    /**
     * 批量删除分析信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 14:19
     * @param parameter 删除分析的条件
     * @return 返回修改的行数
     */
    int deleteAnalyzeBatch(EnergyAnalyzeDeleteParameter parameter);
}
