package com.taiping.biz.capacity.dao.cabinet;

import com.taiping.bean.capacity.cabinet.dto.BaseCabinetDto;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.cabinet.BaseCabinet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基础机柜持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface BaseCabinetDao {


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
     * 新增基础机柜信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:04
     * @param baseCabinet 基础机柜信息
     * @return 新增基础机柜结果
     */
    int insertBaseCabinet(BaseCabinet baseCabinet);

    /**
     * 批量新增基础数据表
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 基础数据表的集合
     * @return 返回批量新增基础表的结果
     */
    int insertBaseCabinetBatch(@Param("list") List<BaseCabinet> list);


    /**
     * 根据机柜唯一标识修改机柜基础信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/11 10:11
     * @param baseCabinet 机柜父级信息表
     * @return 返回机柜基础信息修改结果
     */
    int updateBaseCabinetByCabinetName(BaseCabinet baseCabinet);

    /**
     * 删除全部机柜基础信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:23
     * @return 返回删除机柜基础信息行数
     */
    int deleteAllBaseCabinet();
}
