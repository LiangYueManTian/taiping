package com.taiping.entity.manage;

import com.taiping.entity.annex.Annex;
import lombok.Data;

import java.util.List;

/**
 * 运维管理活动列表展示
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-25
 */
@Data
public class ManageActivityDto extends ManageActivity {
    /**
     * 附件
     */
    private List<Annex> annexList;
}
