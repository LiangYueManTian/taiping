package com.taiping.biz.productivity.dao;

import com.taiping.entity.productivity.ChangePeople;
import com.taiping.entity.productivity.ProStatistics;

import java.util.List;

/**
 * 变更单人员持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-07
 */
public interface ChangePeopleDao {
    /**
     * 批量导入变更单人员
     * @param changePeopleList 变更单
     * @return int
     */
    int insertChangePeopleBatch(List<ChangePeople> changePeopleList);
    /**
     * 统计时间范围每个人上架数量
     * @param proStatistics 时间范围
     * @return List<ProStatistics>
     */
    List<ProStatistics> querySumOfTime(ProStatistics proStatistics);
}
