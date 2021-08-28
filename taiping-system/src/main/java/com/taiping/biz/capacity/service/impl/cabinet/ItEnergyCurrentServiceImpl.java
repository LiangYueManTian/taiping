package com.taiping.biz.capacity.service.impl.cabinet;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyBaseStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentImportDto;
import com.taiping.bean.capacity.cabinet.dto.ItEnergyNotPageDto;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyInfoParameter;
import com.taiping.bean.capacity.cabinet.parameter.ItEnergyListParameter;
import com.taiping.biz.capacity.dao.cabinet.ItEnergyCurrentDao;
import com.taiping.biz.capacity.service.cabinet.ItEnergyCurrentService;
import com.taiping.biz.energy.component.EnergyComponent;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import com.taiping.enums.cabinet.ItEnergySheetEnum;
import com.taiping.enums.energy.EnergyTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.read.cabinet.ItEnergyExcelImportRead;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.CheckConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * it能耗逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 16:53
 */
@Service
@Slf4j
public class ItEnergyCurrentServiceImpl implements ItEnergyCurrentService {


    /**
     * it能耗持久层
     */
    @Autowired
    private ItEnergyCurrentDao itEnergyCurrentDao;

    /**
     * it能耗导入方法
     */
    @Autowired
    private ItEnergyExcelImportRead itEnergyExcelImportRead;

    /**
     * 能耗组件
     */
    @Autowired
    private EnergyComponent energyComponent;


    /**
     * 查询it能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 21:18
     * @param parameter 参数
     * @return it能耗列表信息
     */
    @Override
    public List<ItEnergyNotPageDto> queryItEnergyListNotPage(ItEnergyListParameter parameter) {
        return itEnergyCurrentDao.queryItEnergyListNotPage(parameter);
    }

    /**
     * 导入it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:54
     * @param file 能耗数据文件
     * @return 返回能耗数据导入结果
     */
    @Override
    public Result importItEnergy(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<ItEnergyCurrent> itEnergyList = new ArrayList<>();
        List<ItEnergyCurrentImportDto> itEnergyCurrentList = new ArrayList<>();
        Map<String, List<ItEnergyCurrent>> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        try {
            stringListMap = itEnergyExcelImportRead.readExcel(file);
            //获取it电流能耗集合
            itEnergyCurrentList = (List) stringListMap.get(ItEnergySheetEnum.SHEET_ONE.getSheetName());


            //获取当前数据库最新的时间的数据
            ItEnergyCurrent itEnergyCurrent = this.queryTopDataByTime();
            int beforeYear = 0;
            int beforeMonth = 0;
            if (!ObjectUtils.isEmpty(itEnergyCurrent)) {
                beforeYear = itEnergyCurrent.getYear();
                beforeMonth = itEnergyCurrent.getMonth();
            }

            //需要转换成对应的每个月份的数据
            if (!ObjectUtils.isEmpty(itEnergyCurrentList)) {
                for (ItEnergyCurrentImportDto itEnergyCurrentImportDto : itEnergyCurrentList) {
                    map = energyComponent.setMonthData(map, itEnergyCurrentImportDto.getItEnergyCurrentList());
                }
            }

            String flag = "0";
            int dataYear = 0;
            //判断最新的数据的年份和当前的年份的大小
            energyComponent.getYearAndFlag(beforeYear, flag, dataYear, map);
            //获取新增it电流数据
            itEnergyList = energyComponent.getIsInsertEnergyList(flag, map, dataYear, beforeMonth, EnergyTypeEnum.IT_ENERGY);
            //批量新增it电流数据
            if (!ObjectUtils.isEmpty(itEnergyList)) {
                itEnergyCurrentDao.insertItEnergyCurrentBatch(itEnergyList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(CapacityResultCode.IMPORT_IT_ENERGY_ERROR, CapacityResultMsg.IMPORT_IT_ENERGY_ERROR);
        }
        return ResultUtils.success();
    }



    /**
     * 查询it能耗集合信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return it能耗集合信息
     */
    @Override
    public List<ItEnergyCurrentDto> queryItEnergyInfoList(ItEnergyInfoParameter parameter) {
        return itEnergyCurrentDao.queryItEnergyInfoList(parameter);
    }


    /**
     * 查询最新的it能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 11:00
     * @return 查询最新的it能耗数据
     */
    @Override
    public ItEnergyCurrent queryTopDataByTime() {
        return itEnergyCurrentDao.queryTopDataByTime();
    }

    /**
     * 查询it能耗集合
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:45
     * @param condition 查询it能耗条件
     * @return it能耗数据集合
     */
    @Override
    public List<ItEnergyCurrentDto> queryItEnergyList(QueryCondition condition) {
        return itEnergyCurrentDao.queryItEnergyList(condition);
    }


    /**
     * 查询it能耗信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 16:29
     * @param itEnergyListParameter it能耗集合参数
     * @return 查询it能耗信息数据
     */
    @Override
    public List<ItEnergyBaseStatisticsDto> queryItEnergyGroupByModuleAndDate(ItEnergyListParameter itEnergyListParameter) {
        return itEnergyCurrentDao.queryItEnergyGroupByModuleAndDate(itEnergyListParameter);
    }


    /**
     * 查询it能耗总信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/7 16:29
     * @param itEnergyListParameter it能耗集合参数
     * @return 查询it能耗信息数据
     */
    @Override
    public List<ItEnergyBaseStatisticsDto> queryItEnergyInfoGroupByDate(ItEnergyListParameter itEnergyListParameter) {
        return itEnergyCurrentDao.queryItEnergyInfoGroupByDate(itEnergyListParameter);
    }

    /**
     * 查询it能耗数量
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:45
     * @param condition 查询it能耗条件
     * @return it能耗数量
     */
    @Override
    public int queryItEnergyCount(QueryCondition condition) {
        return itEnergyCurrentDao.queryItEnergyCount(condition);
    }

    /**
     * 查询it能耗列表数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 17:54
     * @param condition 查询条件
     * @return it能耗列表
     */
    @Override
    public Result itEnergyList(QueryCondition condition) {
        //校验参数是否正确
        if (null != CheckConditionUtil.checkQueryConditionParam(condition)) {
            return CheckConditionUtil.checkQueryConditionParam(condition);
        }
        //设置默认条件
        condition = CheckConditionUtil.filterQueryConditionByAsc(condition);

        //设置分页beginNum
        Integer beginNum = (condition.getPageCondition().getPageNum() - 1) * condition.getPageCondition().getPageSize();
        condition.getPageCondition().setBeginNum(beginNum);
        // 构造分页条件
        Page page = MpQueryHelper.myBatiesBuildPage(condition);
        //查询数据结果
        List<ItEnergyCurrentDto> itEnergyCurrentDtoList = this.queryItEnergyList(condition);
        //查询数据个数
        Integer count = this.queryItEnergyCount(condition);
        // 构造返回结果
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, itEnergyCurrentDtoList);
        // 返回数据
        return ResultUtils.pageSuccess(pageBean);
    }
}
