package com.taiping.biz.capacity.dao.cabinet;

import com.taiping.bean.capacity.cabinet.dto.ItEnergyBaseStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyNotPageDto;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyInfoParameter;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IT能耗电流持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface ItEnergyCurrentDao {


    /**
     * 查询it能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 21:18
     * @param parameter 参数
     * @return it能耗列表信息
     */
    List<ItEnergyNotPageDto> queryItEnergyListNotPage(ItEnergyListParameter parameter);

    /**
     * 查询it能耗集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return it能耗集合信息
     */
    List<ItEnergyCurrentDto> queryItEnergyInfoList(ItEnergyInfoParameter parameter);

    /**
     * 查询最新的it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 11:00
     * @return 查询最新的it能耗数据
     */
    ItEnergyCurrent queryTopDataByTime();

    /**
     * 查询it能耗集合
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:45
     * @param condition 查询it能耗条件
     * @return it能耗数据集合
     */
    List<ItEnergyCurrentDto> queryItEnergyList(QueryCondition condition);


    /**
     * 查询it能耗信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 16:29
     * @param itEnergyListParameter it能耗集合参数
     * @return 查询it能耗信息数据
     */
    List<ItEnergyBaseStatisticsDto> queryItEnergyGroupByModuleAndDate(ItEnergyListParameter itEnergyListParameter);

    /**
     * 查询it能耗总信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 16:29
     * @param itEnergyListParameter it能耗集合参数
     * @return 查询it能耗信息数据
     */
    List<ItEnergyBaseStatisticsDto> queryItEnergyInfoGroupByDate(ItEnergyListParameter itEnergyListParameter);

    /**
     * 查询it能耗数据数量
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:45
     * @param condition 查询it能耗条件
     * @return it能耗数据数量
     */
    int queryItEnergyCount(QueryCondition condition);

    /**
     * 新增IT能耗电流信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:04
     * @param itEnergyCurrent IT能耗电流数据
     * @return 新增IT能耗电流数据结果
     */
    int insertItEnergyCurrent(ItEnergyCurrent itEnergyCurrent);

    /**
     * 批量新增IT能耗电流数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list IT能耗电流的集合
     * @return 返回批量新增IT能耗电流的结果
     */
    int insertItEnergyCurrentBatch(@Param("list") List<ItEnergyCurrent> list);

    /**
     * 删除全部IT能耗电流信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:23
     * @return 返回删除IT能耗电流信息行数
     */
    int deleteAllItEnergyCurrent();
}
