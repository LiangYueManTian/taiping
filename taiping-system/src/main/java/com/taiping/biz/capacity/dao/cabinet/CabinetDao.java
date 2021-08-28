package com.taiping.biz.capacity.dao.cabinet;

import com.taiping.bean.capacity.cabinet.dto.*;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.cabinet.Cabinet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机柜持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface CabinetDao {

    /**
     * 查询最新的机柜信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 11:00
     * @return 查询最新的机柜信息数据
     */
    CabinetDto queryTopDataByTime();


    /**
     * 查询容量根据列头柜分组
     * @author hedongwei@wistronits.com
     * @date  2019/11/10 9:21
     * @param parameter 筛选条件
     * @return 返回列头柜分组的数据
     */
    List<CapacityColumnStatisticsDto> queryCapacityGroupByColumn(CabinetInfoListParameter parameter);

    /**
     * 根据楼层分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数为楼层
     * @return 返回楼层分组
     */
    List<CapacityFloorStatisticsDto> queryCapacityByGroupByFloor(CabinetInfoListParameter parameter);


    /**
     * 根据功能区分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数为功能区筛选条件
     * @return 返回功能区分组
     */
    List<CapacityDeviceTypeStatisticsDto> queryCapacityByGroupByDevice(CabinetInfoListParameter parameter);

    /**
     * 根据楼层和功能区分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数
     * @return 返回楼层和功能区分组
     */
    List<CapacityDeviceTypeStatisticsDto> queryCapacityByGroupByFloorAndDevice(CabinetInfoListParameter parameter);



    /**
     * 查询根据pdu查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return pdu容量信息
     */
    List<CapacityPduStatisticsDto> queryCapacityByPdu(CabinetInfoListParameter parameter);

    /**
     * 查询根据机柜查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return 机柜容量信息
     */
    List<CapacityCabinetStatisticsDto> queryCapacityByCabinet(CabinetInfoListParameter parameter);

    /**
     * 查询机柜信息列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息列表数据
     */
    List<CabinetDto> queryCabinetList(QueryCondition queryCondition);


    /**
     * 查询机柜信息个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息个数
     */
    int queryCabinetCount(QueryCondition queryCondition);


    /**
     * 新增机柜信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:04
     * @param cabinet 机柜信息
     * @return 新增机柜结果
     */
    int insertCabinet(Cabinet cabinet);

    /**
     * 批量新增数据表
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 基础数据表的集合
     * @return 返回批量新增表的结果
     */
    int insertCabinetBatch(@Param("list") List<Cabinet> list);

    /**
     * 根据条件删除机柜信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:23
     * @param cabinet 机柜信息
     * @return 返回删除机柜信息行数
     */
    int deleteCabinet(Cabinet cabinet);
}
