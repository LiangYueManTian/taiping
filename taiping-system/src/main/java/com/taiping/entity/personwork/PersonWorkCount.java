package com.taiping.entity.personwork;

import lombok.Data;

/**
 * 个人工作台统计总数
 *
 * @author liyj
 * @date 2019/11/11
 */
@Data
public class PersonWorkCount {
    /**
     * 风险总数
     */
    private int riskTotal;
    /**
     * 预算总数
     */
    private int budgetTotal;
    /**
     * 运维管理活动总数
     */
    private int manageTotal;

}
