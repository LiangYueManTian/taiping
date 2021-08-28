package com.taiping.bean.capacity.cabinet.parameter;

import com.taiping.entity.cabinet.BaseCabinet;
import lombok.Data;

import java.util.List;

/**
 * 机柜信息列表参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/24 13:48
 */
@Data
public class BaseCabinetListParameter extends BaseCabinet {

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
     * 楼层唯一标识集合
     */
    private List<String> floorUniqueNameList;

}
