package com.taiping.biz.operation.dao;

import com.taiping.entity.FilterCondition;
import com.taiping.entity.PageCondition;
import com.taiping.entity.SortCondition;
import com.taiping.entity.operation.CommunicateIssues;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 重要沟通事项跟踪持久层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface CommunicateIssuesDao {
    /**
     * 插入 重要沟通事项跟踪
     * @param communicateIssues  重要沟通事项跟踪
     * @return int
     */
    int insertCommunicateIssues(CommunicateIssues communicateIssues);

    /**
     * 根据ID查询重要沟通事项跟踪
     * @param issuesId ID
     * @return CommunicateIssues  重要沟通事项跟踪
     */
    CommunicateIssues selectCommunicateIssuesById(String issuesId);

    /**
     * 修改 重要沟通事项跟踪
     * @param communicateIssues 重要沟通事项跟踪
     * @return int
     */
    int updateCommunicateIssues(CommunicateIssues communicateIssues);

    /**
     * 删除 重要沟通事项跟踪
     * @param list 事项主键ID集合
     * @return int
     */
    int deleteBatchCommunicateIssues(List<String> list);
    /**
     * 分页查询重要沟通事项跟踪
     * @param pageCondition 分页条件
     * @param filterConditionList 查询条件
     * @param sortCondition 排序条件
     * @return List<CommunicateIssues>
     */
    List<CommunicateIssues> selectCommunicateIssuesList(@Param("page") PageCondition pageCondition,
                                                        @Param("filterList") List<FilterCondition> filterConditionList,
                                                        @Param("sort") SortCondition sortCondition);

    /**
     * 查询重要沟通事项跟踪数量
     * @param filterConditionList 查询条件
     * @return Integer
     */
    Integer selectCommunicateIssuesListCount(@Param("filterList") List<FilterCondition> filterConditionList);
}
