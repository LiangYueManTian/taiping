package com.taiping.biz.energy.service;

import com.taiping.bean.energy.dto.PowerEnergyItemDto;
import com.taiping.bean.energy.dto.PowerEnergyNotPageDto;
import com.taiping.bean.energy.dto.analyze.EnergyPowerItemStatisticsDto;
import com.taiping.bean.energy.parameter.PowerEnergyInfoParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.entity.energy.PowerEnergyItem;

import java.util.List;

/**
 * 能耗逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 20:59
 */
public interface PowerEnergyItemService {

    /**
     * 返回最新的动力能耗分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 9:14
     * @return 返回最新的动力能耗分项
     */
    PowerEnergyItem queryTopDataByTime();



    /**
     * 查询动力能耗分项暖通分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 16:04
     * @param parameter 参数
     * @return 返回动力能耗分项暖通分项
     */
    List<EnergyPowerItemStatisticsDto> queryPowerItemMeterForHeat(PowerEnergyListParameter parameter);


    /**
     * 查询动力能耗分项暖通分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 16:04
     * @param parameter 参数
     * @return 返回动力能耗分项暖通分项
     */
    List<EnergyPowerItemStatisticsDto> queryPowerItemMeterForAllItem(PowerEnergyListParameter parameter);


    /**
     * 查询根据数据分组的统计信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:18
     * @param powerEnergyListParameter 能耗集合参数
     * @return 查询数据分组的统计信息
     */
    List<EnergyPowerItemStatisticsDto> queryStatisticsGroupByData(PowerEnergyListParameter powerEnergyListParameter);

    /**
     * 查询根据类型分组的统计信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:18
     * @param powerEnergyListParameter 能耗集合参数
     * @return 查询类型分组的统计信息
     */
    List<EnergyPowerItemStatisticsDto> queryStatisticsGroupByType(PowerEnergyListParameter powerEnergyListParameter);


    /**
     * 查询动力能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/11/15 13:17
     * @param parameter 参数
     * @return 返回动力能耗列表数据
     */
    List<PowerEnergyNotPageDto>  queryPowerEnergyListNotPage(PowerEnergyListParameter parameter);


    /**
     * 查询动力能耗分项集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 动力能耗分项集合信息
     */
    List<PowerEnergyItemDto> queryPowerEnergyInfoList(PowerEnergyInfoParameter parameter);


    /**
     * 批量新增动力能耗分项数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 动力能耗分项数据的集合
     * @return 返回批量新增动力能耗分项的结果
     */
    int insertPowerEnergyItemBatch(List<PowerEnergyItem> list);
}
