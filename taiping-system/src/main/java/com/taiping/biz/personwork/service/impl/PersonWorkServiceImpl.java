package com.taiping.biz.personwork.service.impl;

import com.google.common.collect.Lists;
import com.taiping.biz.budget.dto.BudgetPurchaseDto;
import com.taiping.biz.budget.service.IBudgetPurchaseService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.personwork.service.PersonWorkService;
import com.taiping.biz.riskmanage.service.IRiskManageService;
import com.taiping.entity.FilterCondition;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.personwork.PersonWork;
import com.taiping.entity.personwork.PersonWorkCount;
import com.taiping.entity.riskmanage.RiskItem;
import com.taiping.exception.BizException;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liyj
 * @date 2019/11/11
 */
@Service
@Slf4j
public class PersonWorkServiceImpl implements PersonWorkService {

    /**
     * 运维管理活动
     */
    @Autowired
    private ManageActivityService manageActivityService;
    /**
     * 风险管理
     */
    @Autowired
    private IRiskManageService riskManageService;


    /**
     * 预算服务
     */
    @Autowired
    private IBudgetPurchaseService budgetPurchaseService;

    @Override
    public PersonWorkCount queryPersonWorkTotal(String userId) {
        PersonWorkCount count = new PersonWorkCount();
        //获取运维管理活动
        QueryCondition<ManageActivity> manCondition = new QueryCondition<>();
        List<FilterCondition> list = Lists.newArrayList();

        if (StringUtils.isNotEmpty(userId)) {
            FilterCondition condition = new FilterCondition();
            condition.setFilterField("responsibleId");
            condition.setFilterValue(userId);
            condition.setOperator("eq");
            list.add(condition);
        }
        manCondition.setFilterConditions(list);

        Integer manageCount = manageActivityService.selectManageActivityListCount(manCondition);
        count.setManageTotal(manageCount == null ? 0 : manageCount);

        // 风控管理
        Integer riskCount = riskManageService.queryCountByUserId(userId);
        count.setRiskTotal(riskCount == null ? 0 : riskCount);

        Integer bugCount = (Integer) budgetPurchaseService.getPurchaseCountByUser(userId).getData();
        count.setBudgetTotal(bugCount == null ? 0 : bugCount);
        return count;
    }

    /**
     * 获取 个人工作台信息
     *
     * @param queryCondition
     * @return
     */
    @Override
    public Result queryPersonWork(QueryCondition<PersonWork> queryCondition) {

        PersonWork work = queryCondition.getBizCondition();
        Integer type = work.getType();
        if (type == 1) {
            // 风控
            return riskHandler(queryCondition);
        } else if (type == 2) {
            // 预算
//            throw new BizException(160005, "预算模块暂时不开放,有问题请及时联系管理员!");
            return bugHandler(queryCondition);
        } else if (type == 3) {
            // 运维管理
            return manageHandler(queryCondition);
        } else {
            log.error("个人工作台类型错误" + type);
            throw new BizException(160004, "个人工作台类型错误");
        }
    }

    /**
     * 预算与采购处理
     *
     * @param queryCondition 查询条件
     * @return
     */
    private Result bugHandler(QueryCondition<PersonWork> queryCondition) {
        PersonWork work = queryCondition.getBizCondition();
        QueryCondition<BudgetPurchaseDto> budgetCondition = new QueryCondition<>();
        BeanUtils.copyProperties(queryCondition, budgetCondition);

        List<FilterCondition> list = Lists.newArrayList();

        if (StringUtils.isNotEmpty(work.getUserId())) {
            FilterCondition condition = new FilterCondition();
            condition.setFilterField("createUser");
            condition.setFilterValue(work.getUserId());
            condition.setOperator("eq");
            list.add(condition);
        }
        if (StringUtils.isNotEmpty(work.getName())) {
            // 模糊查询
            FilterCondition condition = new FilterCondition();
            condition.setFilterField("proName");
            condition.setFilterValue(work.getName());
            condition.setOperator("like");
            list.add(condition);
        }
        budgetCondition.setFilterConditions(list);
        budgetCondition.setBizCondition(null);


        return budgetPurchaseService.getPurchaseByPerson(budgetCondition);
    }

    /**
     * 风控管理
     *
     * @param queryCondition
     * @return
     */
    private Result riskHandler(QueryCondition<PersonWork> queryCondition) {
        //风控
        PersonWork work = queryCondition.getBizCondition();
        QueryCondition<RiskItem> riskCondition = new QueryCondition<>();
        BeanUtils.copyProperties(queryCondition, riskCondition);

        RiskItem riskItem = new RiskItem();
        riskItem.setCheckUser(work.getUserId());
        riskItem.setTrackUser(work.getUserId());

        if (StringUtils.isNotEmpty(work.getName())) {
            List<FilterCondition> filterConditions = Lists.newArrayList();
            FilterCondition condition = new FilterCondition();

            condition.setOperator("like");
            condition.setFilterField("riskItemName");
            condition.setFilterValue(work.getName());
            filterConditions.add(condition);
            riskCondition.setFilterConditions(filterConditions);
        }
        riskCondition.setBizCondition(riskItem);

        return ResultUtils.success(riskManageService.getUserRiskByCondition(riskCondition));

    }


    /**
     * 处理运维管理活动
     *
     * @param queryCondition
     * @return
     */
    private Result manageHandler(QueryCondition<PersonWork> queryCondition) {
        // 运维管理活动
        QueryCondition<ManageActivity> manCondition = new QueryCondition<>();
        BeanUtils.copyProperties(queryCondition, manCondition);

        List<FilterCondition> list = Lists.newArrayList();
        PersonWork work = queryCondition.getBizCondition();

        if (StringUtils.isNotEmpty(work.getUserId())) {
            FilterCondition condition = new FilterCondition();
            condition.setFilterField("responsibleId");
            condition.setFilterValue(work.getUserId());
            condition.setOperator("eq");
            list.add(condition);
        }
        if (StringUtils.isNotEmpty(work.getName())) {
            // 模糊查询
            FilterCondition condition = new FilterCondition();
            condition.setFilterField("sourceName");
            condition.setFilterValue(work.getName());
            condition.setOperator("like");
            list.add(condition);
        }
        manCondition.setFilterConditions(list);
        manCondition.setBizCondition(null);
        return manageActivityService.selectManageActivityList(manCondition);

    }


}
