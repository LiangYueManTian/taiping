package com.taiping.biz.capacity.dao.cabling;

import com.taiping.bean.capacity.cabling.dto.CapacityCablingStatisticsDto;
import com.taiping.bean.capacity.cabling.dto.GenericCablingDto;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.cabling.GenericCabling;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 综合布线持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/11 21:28
 */
public interface GenericCablingDao {

    /**
     * 综合布线最新数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @return 综合布线最新数据
     */
    GenericCablingDto queryTopDataByTime();

    /**
     * 查询综合布线配线架统计数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @param parameter 参数
     * @return 综合布线最新数据
     */
    List<CapacityCablingStatisticsDto> queryCablingGroupByStatusAndRack(GenericCablingListParameter parameter);



    /**
     * 查询综合布线分析统计数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @param parameter 参数
     * @return 综合布线最新数据
     */
    List<CapacityCablingStatisticsDto> queryCablingGroupByStatusAndDate(GenericCablingListParameter parameter);


    /**
     * 综合布线列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 16:02
     * @param condition 条件
     * @return 返回综合布线列表的内容
     */
    List<GenericCablingDto> queryGenericCablingList(QueryCondition condition);


    /**
     * 综合布线列表数据数量
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 16:02
     * @param condition 条件
     * @return 返回综合布线列表数据数量
     */
    int queryGenericCablingCount(QueryCondition condition);

    /**
     * 新增综合布线信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:04
     * @param genericCabling 综合布线信息
     * @return 新增综合布线
     */
    int insertGenericCabling(GenericCabling genericCabling);

    /**
     * 批量新增综合布线信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 综合布线信息的集合
     * @return 返回批量新增综合布线的结果
     */
    int insertGenericCablingBatch(@Param("list") List<GenericCabling> list);

    /**
     * 根据条件删除综合布线信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:23
     * @param genericCabling 删除综合布线信息的参数
     * @return 返回删除综合布线信息行数
     */
    int deleteGenericCabling(GenericCabling genericCabling);
}
