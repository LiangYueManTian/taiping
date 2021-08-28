package com.taiping.entity.system;

import lombok.Data;

/**
 * 预算步骤模板
 *
 * @author liyj
 * @date 2019/11/8
 */
@Data
public class BudgetTemp {
    /**
     * 主键id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否删除
     */
    private boolean isDelete;
    /**
     * 顺序
     */
    private Integer isOrder;
    /**
     * 是否选中
     */
    private boolean checkFlag;

}
