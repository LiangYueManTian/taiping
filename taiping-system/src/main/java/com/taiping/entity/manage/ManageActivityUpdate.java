package com.taiping.entity.manage;

import lombok.Data;

/**
 * 运维管理活动修改实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-06
 */
@Data
public class ManageActivityUpdate extends ManageActivity {
    /**
     * 关联ID
     */
    private String relationId;
    /**
     * 关联名称
     */
    private String relationName;
}
