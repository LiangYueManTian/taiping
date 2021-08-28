package com.taiping.biz.capacity.service.cabling;

import com.taiping.bean.capacity.cabling.dto.CapacityCablingStatisticsDto;
import com.taiping.bean.capacity.cabling.dto.GenericCablingDto;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 综合布线逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/12 9:23
 */
public interface GenericCablingService {


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
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    Result genericCablingList(QueryCondition condition);

    /**
     * 导入综合布线数据
     *
     * @param file 导入的文件
     * @return 返回导入的结果
     * @author hedongwei@wistronits.com
     * @date 2019/10/12 9:17
     */
    Result importGenericCabling(MultipartFile file);

}
