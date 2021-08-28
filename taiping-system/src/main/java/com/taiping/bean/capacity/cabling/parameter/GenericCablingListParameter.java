package com.taiping.bean.capacity.cabling.parameter;

import com.taiping.entity.cabling.GenericCabling;
import lombok.Data;

import java.util.List;

/**
 * 综合布线参数类
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 9:55
 */
@Data
public class GenericCablingListParameter extends GenericCabling {


    /**
     * 线缆类型集合
     */
    private List<String> cableTypeList;

    /**
     * 综合布线集合
     */
    private List<String> genericCablingTypeList;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

}
