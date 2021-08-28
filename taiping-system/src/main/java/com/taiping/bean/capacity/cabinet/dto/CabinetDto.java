package com.taiping.bean.capacity.cabinet.dto;

import com.taiping.entity.cabinet.Cabinet;
import lombok.Data;

/**
 * 机柜基础数据dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/24 14:03
 */
@Data
public class CabinetDto extends Cabinet {

    /**
     * 设备功能区
     */
    private String deviceType;
}
