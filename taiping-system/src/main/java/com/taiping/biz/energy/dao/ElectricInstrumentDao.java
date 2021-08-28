package com.taiping.biz.energy.dao;

import com.taiping.bean.energy.dto.BigScreenDto;
import com.taiping.bean.energy.dto.ElectricInstrumentDto;
import com.taiping.bean.energy.dto.ElectricInstrumentNotPageDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentInfoParameter;
import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.entity.energy.ElectricInstrument;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电量仪表持久层
 *
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:46
 */
public interface ElectricInstrumentDao {

    /**
     * 查询最新的电量总数据
     *
     * @return 返回最新的电量总数据
     * @author hedongwei@wistronits.com
     * @date 2019/11/14 14:39
     */
    ElectricInstrument queryTopDataByTime();

    /**
     * 新增电量仪表信息
     *
     * @param electricInstrument 电量仪表信息
     * @return 新增电量仪表结果
     * @author hedongwei@wistronits.com
     * @date 2019/10/11 9:04
     */
    int insertElectricInstrument(ElectricInstrument electricInstrument);

    /**
     * 查询总能耗集合信息
     *
     * @param parameter 参数
     * @return 总能耗集合信息
     * @author hedongwei@wistronits.com
     * @date 2019/10/29 21:47
     */
    List<ElectricInstrumentDto> queryAllEnergyInfoList(ElectricInstrumentInfoParameter parameter);


    /**
     * 查询总能耗数据列表不分页
     *
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     * @author hedongwei@wistronits.com
     * @date 2019/10/29 21:47
     */
    List<ElectricInstrumentNotPageDto> queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter);

    /**
     * 批量新增电量仪表数据表
     *
     * @param list 电量仪表表的集合
     * @return 返回批量新增电量仪表的结果
     * @author hedongwei@wistronits.com
     * @date 2019/10/11 9:07
     */
    int insertElectricInstrumentBatch(@Param("list") List<ElectricInstrument> list);

    /**
     * 删除全部电量仪表信息
     *
     * @return 返回删除电量仪表信息行数
     * @author hedongwei@wistronits.com
     * @date 2019/10/11 9:23
     */
    int deleteAllElectricInstrument();

    /**
     * 获取大屏数据 pue值
     *
     * @return
     */
    BigScreenDto getBidScreenByPueValue();

    /**
     * 获取大屏数据 容量空间
     *
     * @return
     */
    BigScreenDto getBidScreenByCabinet();

    /**
     * 获取大屏电力数据
     *
     * @return
     */
    BigScreenDto getBidScreenByUps();
}
