package com.taiping.utils.common;

import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.SortCondition;
import com.taiping.utils.ResultUtils;
import org.springframework.util.StringUtils;

/**
 * 检查条件工具类
 * @author hedongwei@wistronits.com
 * @date 2019/10/24 15:33
 */

public class CheckConditionUtil {

    /**
     * 校验pageCondition参数信息
     * @author hedongwei@wistronits.com
     * @date  2019/2/28 15:37
     * @param queryCondition 校验pageCondition参数信息
     */
    public static boolean checkPageCondition(QueryCondition queryCondition) {
        boolean checkResult = true;
        //分页条件不能为空
        if (null == queryCondition.getPageCondition()) {
            checkResult = false;
        } else {
            //pageNumber不能为空
            if (StringUtils.isEmpty(queryCondition.getPageCondition().getPageNum())) {
                checkResult = false;
            }
            //pageSize不能为空
            if (StringUtils.isEmpty(queryCondition.getPageCondition().getPageSize())) {
                checkResult = false;
            }
        }
        return checkResult;
    }


    /**
     * 查询条件参数
     * @author hedongwei@wistronits.com
     * @date  2019/3/5 17:00
     * @param queryCondition 查询条件信息
     */
    public static Result checkQueryConditionParam(QueryCondition queryCondition) {
        String msg = "请求参数不正确";
        //筛选对象不能为空
        if (null == queryCondition.getFilterConditions()) {
            return ResultUtils.warn(ResultCode.FAIL, msg);
        }

        //筛选对象不能为空
        if (null == queryCondition.getFilterConditions()) {
            return ResultUtils.warn(ResultCode.FAIL, msg);
        }

        //分页条件不能为空
        boolean resultPageCondition = CheckConditionUtil.checkPageCondition(queryCondition);
        if (!resultPageCondition) {
            return ResultUtils.warn(ResultCode.FAIL, msg);
        }
        return null;
    }


    /**
     * 查询条件过滤
     * @author hedongwei@wistronits.com
     * @date  2019/3/7 14:55
     * @param queryCondition 查询条件过滤
     * @return 返回查询条件过滤之后的查询条件
     */
    public static QueryCondition filterQueryCondition(QueryCondition queryCondition){
        String sortField = "createTime";
        String sortRule = "desc";
        queryCondition = CheckConditionUtil.getQueryCondition(queryCondition, sortField, sortRule);
        return queryCondition;
    }


    /**
     * 查询条件过滤
     * @author hedongwei@wistronits.com
     * @date  2019/3/7 14:55
     * @param queryCondition 查询条件过滤
     * @return 返回查询条件过滤之后的查询条件
     */
    public static QueryCondition filterQueryConditionByAsc(QueryCondition queryCondition){
        String sortField = "createTime";
        String sortRule = "asc";
        queryCondition = CheckConditionUtil.getQueryCondition(queryCondition, sortField, sortRule);
        return queryCondition;
    }

    /**
     * 获取查询条件
     * @author hedongwei@wistronits.com
     * @date  2019/4/8 15:30
     * @param queryCondition 查询条件
     * @param sortField 排序字段
     * @param sortRule 排序规则
     * @return 获取查询条件
     */
    public static QueryCondition getQueryCondition(QueryCondition queryCondition, String sortField, String sortRule) {
        // 无排序时的默认排序（当前按照创建时间降序）
        if (null == queryCondition.getSortCondition()){
            queryCondition = CheckConditionUtil.setDefaultCondition(queryCondition, sortField, sortRule);
        } else {
            if (StringUtils.isEmpty(queryCondition.getSortCondition().getSortRule())) {
                queryCondition = CheckConditionUtil.setDefaultCondition(queryCondition, sortField, sortRule);
            }
        }
        return queryCondition;
    }

    /**
     * 设置默认的排序条件
     * @author hedongwei@wistronits.com
     * @date  2019/4/8 15:30
     * @param queryCondition 查询条件
     * @param sortField 排序字段
     * @param sortRule 排序规则
     * @return 获取默认的排序条件
     */
    public static QueryCondition setDefaultCondition(QueryCondition queryCondition, String sortField, String sortRule) {
        SortCondition sortCondition = new SortCondition();
        sortCondition.setSortField(sortField);
        sortCondition.setSortRule(sortRule);
        queryCondition.setSortCondition(sortCondition);
        return queryCondition;
    }
}
