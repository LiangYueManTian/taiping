package com.taiping.bean.capacity.cabinet.parameter;

import com.taiping.entity.cabinet.Cabinet;
import lombok.Data;

import java.util.List;

/**
 * 机柜信息列表参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/24 13:48
 */
@Data
public class CabinetInfoListParameter  extends Cabinet {

    /**
     * 楼层集合
     */
    private List<String> floorList;


    /**
     * 功能区集合
     */
    private List<String> deviceTypeList;


    /**
     * 机柜列集合
     */
    private List<String> cabinetColumnList ;

    /**
     * 机柜唯一标识集合
     */
    private List<String> cabinetUniqueNameList;

    /**
     * 机柜类型
     */
    private String deviceType;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

}
