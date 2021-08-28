package com.taiping.biz.energy.service.impl;

import com.taiping.bean.energy.dto.ElectricInstrumentDto;
import com.taiping.bean.energy.dto.ElectricInstrumentNotPageDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentInfoParameter;
import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.biz.energy.dao.ElectricInstrumentDao;
import com.taiping.biz.energy.service.ElectricInstrumentService;
import com.taiping.entity.energy.ElectricInstrument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 总能耗逻辑实现类
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 21:00
 */
@Service
@Slf4j
public class ElectricInstrumentServiceImpl implements ElectricInstrumentService {

    /**
     * 获取总能耗持久层
     */
    @Autowired
    private ElectricInstrumentDao electricInstrumentDao;

    /**
     * 返回最新的总能耗分项
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 9:14
     * @return 返回最新的总能耗分项
     */
    @Override
    public ElectricInstrument queryTopDataByTime() {
        return electricInstrumentDao.queryTopDataByTime();
    }


    /**
     * 查询总能耗集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗集合信息
     */
    @Override
    public List<ElectricInstrumentDto> queryAllEnergyInfoList(ElectricInstrumentInfoParameter parameter) {
        return electricInstrumentDao.queryAllEnergyInfoList(parameter);
    }



    /**
     * 查询总能耗数据列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     */
    @Override
    public List<ElectricInstrumentNotPageDto> queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter) {
        return electricInstrumentDao.queryAllEnergyListNotPage(parameter);
    }

    /**
     * 批量新增电量仪表数据表
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 9:07
     * @param list 电量仪表表的集合
     * @return 返回批量新增电量仪表的结果
     */
    @Override
    public int insertElectricInstrumentBatch(List<ElectricInstrument> list) {
        return electricInstrumentDao.insertElectricInstrumentBatch(list);
    }

}
