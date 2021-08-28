package com.taiping.biz.capacity.service.cabinet;

import com.taiping.bean.capacity.cabinet.dto.BaseCabinetDto;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.BaseCabinet;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 机柜逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:43
 */
public interface BaseCabinetService {


    /**
     * 查询最新的机柜基础数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的机柜基础数据
     */
    BaseCabinetDto queryTopDataByTime();

    /**
     * 查询基础机柜信息列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息列表数据
     */
    List<BaseCabinetDto> queryBaseCabinetList(QueryCondition queryCondition);


    /**
     * 查询基础机柜信息个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息个数
     */
    int queryBaseCabinetCount(QueryCondition queryCondition);

    /**
     * 导入机柜基础数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:54
     * @param file 机柜基础数据文件
     * @return 返回机柜基础导入结果
     */
    Result importBaseCabinet(@RequestBody MultipartFile file);


    /**
     * 根据机柜唯一标识修改机柜基础信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/11 10:11
     * @param baseCabinet 机柜父级信息表
     * @return 返回机柜基础信息修改结果
     */
    int updateBaseCabinetByCabinetName(BaseCabinet baseCabinet);

    /**
     * 机柜基础数据列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    Result cabinetBaseList(@RequestBody QueryCondition condition);
}
