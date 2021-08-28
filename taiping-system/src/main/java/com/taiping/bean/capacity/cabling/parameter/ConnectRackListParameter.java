package com.taiping.bean.capacity.cabling.parameter;

import com.taiping.entity.cabling.ConnectRack;
import lombok.Data;

import java.util.List;

/**
 * 配线架参数类
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 9:55
 */
@Data
public class ConnectRackListParameter extends ConnectRack {

    /**
     * 配线架集合
     */
    private List<String> connectRackList;

    /**
     * 序列号集合
     */
    private List<String> serialNumberList;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

}
