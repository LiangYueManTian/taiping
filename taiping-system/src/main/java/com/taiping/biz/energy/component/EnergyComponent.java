package com.taiping.biz.energy.component;

import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentDto;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyInfoParameter;
import com.taiping.bean.energy.EnergyBaseBean;
import com.taiping.bean.energy.dto.ElectricInstrumentDto;
import com.taiping.bean.energy.dto.PowerEnergyItemDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentInfoParameter;
import com.taiping.bean.energy.parameter.PowerEnergyInfoParameter;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.biz.energy.service.ElectricInstrumentService;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import com.taiping.entity.energy.ElectricInstrument;
import com.taiping.entity.energy.PowerEnergyItem;
import com.taiping.enums.energy.EnergyTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 能耗组件
 * @author hedongwei@wistronits.com
 * @date 2019/11/14 16:30
 */
@Component
public class EnergyComponent {


    @Autowired
    private ItEnergyCurrentService itEnergyCurrentService;


    @Autowired
    private PowerEnergyItemService powerEnergyItemService;

    @Autowired
    private ElectricInstrumentService electricInstrumentService;


    /**
     * 设置月份数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 16:51
     * @param map 数据map
     * @param dataList 数据集合
     * @return 设置月份数据
     */
    public <T extends EnergyBaseBean> Map<String, List<T>> setMonthData(Map<String, List<T>> map, List<T> dataList) {
        for (T energyOne : dataList) {
            List<T> electricInstrumentList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(map)) {
                if (map.containsKey(energyOne.getMonth() + "")) {
                    electricInstrumentList = map.get(energyOne.getMonth() + "");
                    electricInstrumentList.add(energyOne);
                } else {
                    electricInstrumentList.add(energyOne);
                }
            } else {
                electricInstrumentList.add(energyOne);
            }
            map.put(energyOne.getMonth().toString(), electricInstrumentList);
        }
        return map;
    }

    /**
     * 获取新增能耗数据集合
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 15:53
     * @param flag 标识位
     * @param map  根据月份为键的能耗数据
     * @param dataYear 导入数据的年份
     * @param beforeMonth 数据库最新数据的月份
     * @param type 能耗类型
     * @return 能耗数据集合
     */
    public <T extends EnergyBaseBean> List<T> getIsInsertEnergyList(String flag, Map<String, List<T>> map, Integer dataYear, Integer beforeMonth, EnergyTypeEnum type) {
        //获取数据最新的月份信息
        Integer month = this.getNewDataMonth(map);
        Integer startMonth = 0;
        Integer endMonth = 0;
        if (!ObjectUtils.isEmpty(month)) {
            //标志位2
            String flagInfoTwo = "2";
            //标志位3
            String flagInfoThree = "3";
            //标志位4
            String flagInfoFour = "4";
            if (flagInfoTwo.equals(flag)) {
                //相同年的数据的月份大于已经存在的数据的月份时
                if (month > beforeMonth) {
                    startMonth = beforeMonth + 1;
                    endMonth = month;
                } else {
                    return new ArrayList<>();
                }
            } else if (flagInfoThree.equals(flag)) {
                startMonth = 1;
                endMonth = month;
            } else if (flagInfoFour.equals(flag)) {
                startMonth = 1;
                endMonth = month;
            } else {
                startMonth = 1;
                endMonth = month;
            }
            return this.getSubMonth(startMonth, endMonth, map, dataYear, type);
        }
        return new ArrayList<>();
    }

    /**
     * 获取截取的月份 （并且计算出与上一个月的差值）
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 14:21
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @param map map对象
     * @param dataYear 数据年份
     * @return 返回截取的月份
     */
    public <T extends EnergyBaseBean> List<T> getSubMonth(int startMonth, int endMonth, Map<String, List<T>> map, int dataYear, EnergyTypeEnum type) {
        List<T> subEnergyList = new ArrayList<>();
        //筛选出满足条件月份的值的数据
        for (int i = startMonth ; i <= endMonth ; i ++) {
            List<T> itEnergyCurrentList = map.get(i + "");
            subEnergyList.addAll(itEnergyCurrentList);
        }

        //将满足条件的map定义成以数据名称为键，数据为集合
        Map<String, List<T>> energyMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        Set<String> energySet = new HashSet<>();
        for (T energyCurrent : subEnergyList) {
            List<T> dataList = new ArrayList<>();
            if (energyMap.containsKey(energyCurrent.getDataCode())) {
                dataList = energyMap.get(energyCurrent.getDataCode());
            }
            dataList.add(energyCurrent);
            energyMap.put(energyCurrent.getDataCode(), dataList);
            energySet.add(energyCurrent.getDataCode());
        }

        int searchYear = 0;
        int month = 0;
        List<String> dataCodeList = new ArrayList<>();
        //查询一月之前的去年的数据信息
        if (startMonth == 1) {
            searchYear = dataYear - 1;
            month = 12;
        } else {
            searchYear = dataYear;
            month = startMonth - 1;
        }
        if (!ObjectUtils.isEmpty(energySet)) {
            dataCodeList.addAll(energySet);
            Map<String, String> beforeMonthMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            if (EnergyTypeEnum.IT_ENERGY.getCode().equals(type.getCode())) {
                List<ItEnergyCurrent> dtoInfoList = this.searchItDataInfo(dataCodeList, month, searchYear);
                //将上月数据转换成map
                beforeMonthMap = this.getMeterMap(dtoInfoList);
            } else if (EnergyTypeEnum.POWER_ENERGY.getCode().equals(type.getCode())) {
                //动力能耗
                List<PowerEnergyItem> dtoInfoList = this.searchPowerDataInfo(dataCodeList, month, searchYear);
                //将上月数据转换成map
                beforeMonthMap = this.getMeterMap(dtoInfoList);
            } else if (EnergyTypeEnum.ALL_ENERGY.getCode().equals(type.getCode())) {
                //总能耗
                List<ElectricInstrument> dtoInfoList = this.searchAllDataInfo(dataCodeList, month, searchYear);
                //将上月数据转换成map
                beforeMonthMap = this.getMeterMap(dtoInfoList);
            }
            subEnergyList = this.compareSubEnergyList(energyMap, beforeMonthMap, startMonth, endMonth);
        }
        return subEnergyList;
    }

    /**
     * 获取it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 17:32
     * @param dataCodeList 数据code集合
     * @param month 月份
     * @param searchYear 年份
     * @return 返回it能耗数据
     */
    public List<ItEnergyCurrent> searchItDataInfo(List<String> dataCodeList, int month, int searchYear) {
        List<ItEnergyCurrent> dtoEntityList = new ArrayList<>();
        //it能耗
        ItEnergyInfoParameter itEnergyInfoParameter = new ItEnergyInfoParameter();
        itEnergyInfoParameter.setDataNameList(dataCodeList);
        itEnergyInfoParameter.setMonth(month);
        itEnergyInfoParameter.setYear(searchYear);
        //查询信息
        if (!ObjectUtils.isEmpty(itEnergyInfoParameter)) {
            List<ItEnergyCurrentDto> dtoList = itEnergyCurrentService.queryItEnergyInfoList(itEnergyInfoParameter);
            if (!ObjectUtils.isEmpty(dtoList)) {
                for (ItEnergyCurrentDto itEnergyCurrentDto : dtoList) {
                    ItEnergyCurrent itEnergyCurrentInfo = new ItEnergyCurrent();
                    BeanUtils.copyProperties(itEnergyCurrentDto, itEnergyCurrentInfo);
                    dtoEntityList.add(itEnergyCurrentInfo);
                }
                return dtoEntityList;
            }
        }
        return dtoEntityList;
    }


    /**
     * 获取动力能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 17:32
     * @param dataCodeList 数据code集合
     * @param month 月份
     * @param searchYear 年份
     * @return 返回动力能耗数据
     */
    public List<PowerEnergyItem> searchPowerDataInfo(List<String> dataCodeList, int month, int searchYear) {
        List<PowerEnergyItem> dtoEntityList = new ArrayList<>();
        //it能耗
        PowerEnergyInfoParameter powerEnergyInfoParameter = new PowerEnergyInfoParameter();
        powerEnergyInfoParameter.setDataCodeList(dataCodeList);
        powerEnergyInfoParameter.setMonth(month);
        powerEnergyInfoParameter.setYear(searchYear);
        //查询信息
        if (!ObjectUtils.isEmpty(powerEnergyInfoParameter)) {
            List<PowerEnergyItemDto> dtoList = powerEnergyItemService.queryPowerEnergyInfoList(powerEnergyInfoParameter);
            if (!ObjectUtils.isEmpty(dtoList)) {
                for (PowerEnergyItemDto powerEnergyItemDto : dtoList) {
                    PowerEnergyItem powerEnergyItem = new PowerEnergyItem();
                    BeanUtils.copyProperties(powerEnergyItemDto, powerEnergyItem);
                    dtoEntityList.add(powerEnergyItem);
                }
                return dtoEntityList;
            }
        }
        return dtoEntityList;
    }


    /**
     * 获取总能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 17:32
     * @param dataCodeList 数据code集合
     * @param month 月份
     * @param searchYear 年份
     * @return 返回总能耗数据
     */
    public List<ElectricInstrument> searchAllDataInfo(List<String> dataCodeList, int month, int searchYear) {
        List<ElectricInstrument> dtoEntityList = new ArrayList<>();
        //it能耗
        ElectricInstrumentInfoParameter electricParameter = new ElectricInstrumentInfoParameter();
        electricParameter.setDataCodeList(dataCodeList);
        electricParameter.setMonth(month);
        electricParameter.setYear(searchYear);
        //查询信息
        if (!ObjectUtils.isEmpty(electricParameter)) {
            List<ElectricInstrumentDto> dtoList = electricInstrumentService.queryAllEnergyInfoList(electricParameter);
            if (!ObjectUtils.isEmpty(dtoList)) {
                for (ElectricInstrumentDto electricInstrumentDto : dtoList) {
                    ElectricInstrument electricInstrument = new ElectricInstrument();
                    BeanUtils.copyProperties(electricInstrumentDto, electricInstrument);
                    dtoEntityList.add(electricInstrument);
                }
                return dtoEntityList;
            }
        }
        return dtoEntityList;
    }

    /**
     * 算出电量值的增长值设置在数据中
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 15:48
     * @param energyMap it能耗map
     * @param beforeMonthMap 需要查询数据库的上月能耗map
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return it能耗新增数据
     */
    public <T extends EnergyBaseBean> List<T> compareSubEnergyList(Map<String, List<T>> energyMap, Map<String, String> beforeMonthMap, int startMonth, int endMonth) {
        List<T> returnItList = new ArrayList<>();
        Map<String, Map<Integer, String>> mapDiffInfo = new HashMap<>();
        for (String dataName : energyMap.keySet()) {
            Map<Integer, String> mapMonth = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (T energyCurrent : energyMap.get(dataName)) {
                mapMonth.put(energyCurrent.getMonth(), String.valueOf(energyCurrent.getAllElectricMeter()));
            }
            Map<Integer, String> mapMeter = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (int i = startMonth ; i <= endMonth ; i++ ) {
                //上一个月的电量值
                double beforeMonthMeter = 0;
                //这个月的电量值
                double monthMeter = 0;
                if (startMonth == i) {
                    //上个月的电量值
                    if (null != beforeMonthMap.get(dataName)) {
                        beforeMonthMeter = Double.valueOf(beforeMonthMap.get(dataName));
                    }
                    //这个月的电量值
                    monthMeter = Double.valueOf(mapMonth.get(i));
                } else {
                    //上个月的电量值
                    beforeMonthMeter = Double.valueOf(mapMonth.get(i - 1));
                    //这个月的电量值
                    monthMeter = Double.valueOf(mapMonth.get(i));
                }
                double diffMonthMeter = monthMeter - beforeMonthMeter;
                //记录每个月的修改值
                mapMeter.put(i, String.valueOf(diffMonthMeter));
            }
            mapDiffInfo.put(dataName, mapMeter);
        }

        for (String dataName : energyMap.keySet()) {
            for (T energyCurrentOne : energyMap.get(dataName)) {
                if (!ObjectUtils.isEmpty(mapDiffInfo)) {
                    if (!ObjectUtils.isEmpty(mapDiffInfo.get(dataName))) {
                        if (!ObjectUtils.isEmpty(mapDiffInfo.get(dataName).get(energyCurrentOne.getMonth()))) {
                            double growthElectricMeter = Double.valueOf(mapDiffInfo.get(dataName).get(energyCurrentOne.getMonth()));
                            energyCurrentOne.setGrowthElectricMeter(growthElectricMeter);
                        }
                    }
                }
                returnItList.add(energyCurrentOne);
            }
        }
        return returnItList;
    }


    /**
     * 获取年份和标志位
     * @author hedongwei@wistronits.com
     * @date  2019/11/14 16:25
     * @param beforeYear 上年
     * @param flag 标志位
     * @param dataYear 数据年份
     * @param map 数据map
     */
    public <T extends EnergyBaseBean> void getYearAndFlag(int beforeYear, String flag, int dataYear, Map<String, List<T>> map) {
        //判断最新的数据的年份和当前的年份的大小
        if (!ObjectUtils.isEmpty(map)) {
            dataYear = map.get("1").get(0).getYear();
            if (dataYear < beforeYear) {
                //代表当前年份小于最新数据的年份
                flag = "1";
            } else if (dataYear == beforeYear) {
                //当前年份等于数据库最新数据的年份
                flag = "2";
            } else if (dataYear > beforeYear) {
                //当前年份大于数据库最新数据的年份
                flag = "3";
            }
        } else {
            flag = "4";
        }
    }

    /**
     * 获取最新的数据存在的月份信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 14:09
     * @param map 根据月份统计的数据信息
     * @return 返回最新的数据月份信息
     */
    public <T extends  EnergyBaseBean> Integer getNewDataMonth(Map<String, List<T>> map) {
        if (ObjectUtils.isEmpty(map)) {
            return 0;
        }
        for (int i = 12 ; i >= 0 ; i --) {
            List<T> energyCurrentList = map.get(i + "");
            for (T energyCurrentOne : energyCurrentList) {
                double allMeter = energyCurrentOne.getAllElectricMeter();
                if (!ObjectUtils.isEmpty(allMeter) && allMeter > 0) {
                    return energyCurrentOne.getMonth();
                }
            }
        }
        return 0;
    }

    /**
     * 获取电流值map
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:53
     * @param dtoList 电流值参数
     * @return 电流值map
     */
    public <T extends EnergyBaseBean> Map<String, String> getMeterMap(List<T> dtoList) {
        Map<String, String> meterMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(dtoList)) {
            for (T energyCurrentDto : dtoList) {
                if (!ObjectUtils.isEmpty(energyCurrentDto.getAllElectricMeter())) {
                    meterMap.put(energyCurrentDto.getDataCode(), String.valueOf(energyCurrentDto.getAllElectricMeter()));
                }
            }
        }
        return meterMap;
    }
}
