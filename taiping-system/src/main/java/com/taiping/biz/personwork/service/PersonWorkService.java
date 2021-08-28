package com.taiping.biz.personwork.service;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.personwork.PersonWork;
import com.taiping.entity.personwork.PersonWorkCount;

/**
 * 个人工作台 接口
 *
 * @author liyj
 * @date 2019/11/11
 */
public interface PersonWorkService {

    /**
     * 通过用户id  获取个人工作台总数
     *
     * @param userId 用户id
     * @return
     */
    PersonWorkCount queryPersonWorkTotal(String userId);

    /**
     * 获取 个人工作台信息
     *
     * @param queryCondition
     * @return
     */
    Result queryPersonWork(QueryCondition<PersonWork> queryCondition);
}
