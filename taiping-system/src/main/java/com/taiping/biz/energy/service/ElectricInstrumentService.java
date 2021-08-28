package com.taiping.biz.energy.service;

import com.taiping.bean.energy.dto.ElectricInstrumentDto;
import com.taiping.bean.energy.dto.ElectricInstrumentNotPageDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentInfoParameter;
import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.entity.energy.ElectricInstrument;

import java.util.List;

/**
 * 能耗逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 20:59
 */
public interface ElectricInstrumentService {

    /**
     * 返回最新的总能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 9:14
     * @return 返回最新的总能耗数据
     */
    ElectricInstrument queryTopDataByTime();


    /**
     * 查询总能耗集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗集合信息
     */
    List<ElectricInstrumentDto> queryAllEnergyInfoList(ElectricInstrumentInfoParameter parameter);


    /**
     * 查询总能耗数据列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     */
    List<ElectricInstrumentNotPageDto> queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter);


    /**
     * 批量新增电量仪表数据表
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 电量仪表表的集合
     * @return 返回批量新增电量仪表的结果
     */
    int insertElectricInstrumentBatch(List<ElectricInstrument> list);
}
