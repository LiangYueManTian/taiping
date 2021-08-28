package com.taiping.biz.energy.dao.analyze;

import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeDeleteViewParameter;
import com.taiping.bean.energy.parameter.analyze.EnergyAnalyzeRelatedViewListParameter;
import com.taiping.entity.analyze.energy.EnergyAnalyzeRelatedViewInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 能耗阈值关联显示持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface EnergyAnalyzeRelatedViewInfoDao {


    /**
     * 阈值关联显示信息集合
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 20:25
     * @param parameter 参数
     * @return 返回阈值关联显示信息集合
     */
    List<EnergyAnalyzeRelatedViewInfo> queryViewInfoList(EnergyAnalyzeRelatedViewListParameter parameter);

    /**
     * 查询最新的能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的能耗阈值关联数据
     */
    EnergyAnalyzeRelatedViewInfo queryTopDataByTime();


    /**
     * 批量新增能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:43
     * @param list 阈值信息集合
     * @return 返回能耗关联显示信息
     */
    int insertThresholdRelatedViewInfoBatch(@Param("list") List<EnergyAnalyzeRelatedViewInfo> list);

    /**
     * 新增能耗关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 10:47
     * @param info 阈值信息
     * @return 返回能耗关联显示信息
     */
    int insertThresholdRelatedViewInfo(EnergyAnalyzeRelatedViewInfo info);


    /**
     * 批量删除分析关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 14:11
     * @param parameter 删除参数
     * @return 返回值
     */
    int deleteAnalyzeRelatedInfoBatch(EnergyAnalyzeDeleteViewParameter parameter);
}
