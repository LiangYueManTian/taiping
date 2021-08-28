package com.taiping.biz.energy.service.impl;

import com.taiping.bean.energy.dto.PowerEnergyItemDto;
import com.taiping.bean.energy.dto.PowerEnergyNotPageDto;
import com.taiping.bean.energy.dto.analyze.EnergyPowerItemStatisticsDto;
import com.taiping.bean.energy.parameter.PowerEnergyInfoParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.biz.energy.dao.PowerEnergyItemDao;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.entity.energy.PowerEnergyItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动力能耗逻辑实现类
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 21:00
 */
@Service
@Slf4j
public class PowerEnergyItemServiceImpl implements PowerEnergyItemService {

    /**
     * 能耗分项持久层
     */
    @Autowired
    private PowerEnergyItemDao powerEnergyItemDao;

    /**
     * 返回最新的动力能耗分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 9:14
     * @return 返回最新的动力能耗分项
     */
    @Override
    public PowerEnergyItem queryTopDataByTime() {
        return powerEnergyItemDao.queryTopDataByTime();
    }


    /**
     * 查询动力能耗分项暖通分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 16:04
     * @param parameter 参数
     * @return 返回动力能耗分项暖通分项
     */
    @Override
    public List<EnergyPowerItemStatisticsDto> queryPowerItemMeterForHeat(PowerEnergyListParameter parameter) {
        return powerEnergyItemDao.queryPowerItemMeterForHeat(parameter);
    }

    /**
     * 查询动力能耗分项暖通分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 16:04
     * @param parameter 参数
     * @return 返回动力能耗分项暖通分项
     */
    @Override
    public List<EnergyPowerItemStatisticsDto> queryPowerItemMeterForAllItem(PowerEnergyListParameter parameter) {
        return powerEnergyItemDao.queryPowerItemMeterForAllItem(parameter);
    }


    /**
     * 查询根据数据分组的统计信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:18
     * @param powerEnergyListParameter 能耗集合参数
     * @return 查询数据分组的统计信息
     */
    @Override
    public List<EnergyPowerItemStatisticsDto> queryStatisticsGroupByData(PowerEnergyListParameter powerEnergyListParameter) {
        return powerEnergyItemDao.queryStatisticsGroupByData(powerEnergyListParameter);
    }

    /**
     * 查询根据类型分组的统计信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/18 17:18
     * @param powerEnergyListParameter 能耗集合参数
     * @return 查询类型分组的统计信息
     */
    @Override
    public List<EnergyPowerItemStatisticsDto> queryStatisticsGroupByType(PowerEnergyListParameter powerEnergyListParameter) {
        return powerEnergyItemDao.queryStatisticsGroupByType(powerEnergyListParameter);
    }


    /**
     * 查询动力能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/11/15 13:17
     * @param parameter 参数
     * @return 返回动力能耗列表数据
     */
    @Override
    public List<PowerEnergyNotPageDto> queryPowerEnergyListNotPage(PowerEnergyListParameter parameter) {
        return powerEnergyItemDao.queryPowerEnergyListNotPage(parameter);
    }


    /**
     * 查询动力能耗分项集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 动力能耗分项集合信息
     */
    @Override
    public List<PowerEnergyItemDto> queryPowerEnergyInfoList(PowerEnergyInfoParameter parameter) {
        return powerEnergyItemDao.queryPowerEnergyInfoList(parameter);
    }


    /**
     * 批量新增动力能耗分项数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 动力能耗分项数据的集合
     * @return 返回批量新增动力能耗分项的结果
     */
    @Override
    public int insertPowerEnergyItemBatch(List<PowerEnergyItem> list) {
        return powerEnergyItemDao.insertPowerEnergyItemBatch(list);
    }

}
