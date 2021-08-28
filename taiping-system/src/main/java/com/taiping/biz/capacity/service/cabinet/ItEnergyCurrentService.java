package com.taiping.biz.capacity.service.cabinet;

import com.taiping.bean.capacity.cabinet.dto.ItEnergyBaseStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyNotPageDto;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyInfoParameter;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * it能耗逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 16:53
 */
public interface ItEnergyCurrentService {


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
     * 导入it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:54
     * @param file 能耗数据文件
     * @return 返回能耗数据导入结果
     */
    Result importItEnergy(MultipartFile file);

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
     * 查询it能耗列表数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:54
     * @param condition 查询条件
     * @return it能耗列表
     */
    Result itEnergyList(QueryCondition condition);
}
