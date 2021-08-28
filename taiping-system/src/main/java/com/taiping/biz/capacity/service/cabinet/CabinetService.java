package com.taiping.biz.capacity.service.cabinet;

import com.taiping.bean.capacity.cabinet.dto.*;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 机柜逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:43
 */
public interface CabinetService {


    /**
     * 查询最新的机柜信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 11:00
     * @return 查询最新的机柜信息数据
     */
    CabinetDto queryTopDataByTime();


    /**
     * 查询容量根据pdu分组
     * @author hedongwei@wistronits.com
     * @date  2019/11/10 9:21
     * @param parameter 筛选条件
     * @return 返回pdu分组的数据
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
     * 查询根据机柜查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return 机柜容量信息
     */
    List<CapacityCabinetStatisticsDto> queryCapacityByCabinet(CabinetInfoListParameter parameter);


    /**
     * 查询根据pdu查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return pdu容量信息
     */
    List<CapacityPduStatisticsDto> queryCapacityByPdu(CabinetInfoListParameter parameter);

    /**
     * 查询机柜信息列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 机柜信息列表数据
     */
    List<CabinetDto> queryCabinetList(QueryCondition queryCondition);


    /**
     * 查询机柜信息个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 机柜信息个数
     */
    int queryCabinetCount(QueryCondition queryCondition);

    /**
     * 导入机柜数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:59
     * @param file 机柜数据文件
     * @return 返回机柜数据结果
     */
    Result importCabinet(MultipartFile file);


    /**
     * 机柜基础数据列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    Result cabinetList(@RequestBody QueryCondition condition);
}
