package com.taiping.biz.capacity.dao.cabling;

import com.taiping.bean.capacity.cabling.dto.ConnectRackDto;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.cabling.ConnectRack;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配线架持久层
 * @author hedongwei@wistronits.com
 * @date 2019/10/11 21:28
 */
public interface ConnectRackDao {


    /**
     * 查询配线架列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 查询配线架列表数据
     */
    List<ConnectRackDto> queryConnectRackList(QueryCondition queryCondition);


    /**
     * 查询配线架列表个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 配线架列表个数
     */
    int queryConnectRackCount(QueryCondition queryCondition);

    /**
     * 新增配线架信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:04
     * @param connectRack 配线架信息
     * @return 新增配线架
     */
    int insertConnectRack(ConnectRack connectRack);

    /**
     * 批量新增配线架信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 配线架信息的集合
     * @return 返回批量新增配线架的结果
     */
    int insertConnectRackBatch(@Param("list") List<ConnectRack> list);

    /**
     * 删除全部配线架信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:23
     * @return 返回删除配线架信息行数
     */
    int deleteAllConnectRack();
}
