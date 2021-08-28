package com.taiping.biz.capacity.service.impl.cabinet;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.bean.capacity.cabinet.dto.*;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.biz.capacity.dao.cabinet.CabinetDao;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.Cabinet;
import com.taiping.enums.cabinet.CabinetSheetEnum;
import com.taiping.exception.BizException;
import com.taiping.read.cabinet.CabinetExcelImportRead;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.CheckConditionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 机柜逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 17:03
 */
@Service
@Slf4j
public class CabinetServiceImpl implements CabinetService {

    /**
     * 机柜信息导入数据类
     */
    @Autowired
    private CabinetExcelImportRead cabinetExcelImportRead;

    /**
     * 机柜信息持久层
     */
    @Autowired
    private CabinetDao cabinetDao;


    /**
     * 查询最新的机柜信息数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/30 11:00
     * @return 查询最新的机柜信息数据
     */
    @Override
    public CabinetDto queryTopDataByTime() {
        return cabinetDao.queryTopDataByTime();
    }



    /**
     * 查询容量根据列头柜分组
     * @author hedongwei@wistronits.com
     * @date  2019/11/10 9:21
     * @param parameter 筛选条件
     * @return 返回列头柜分组的数据
     */
    @Override
    public List<CapacityColumnStatisticsDto> queryCapacityGroupByColumn(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityGroupByColumn(parameter);
    }


    /**
     * 根据楼层分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数
     * @return 返回楼层分组
     */
    @Override
    public List<CapacityFloorStatisticsDto> queryCapacityByGroupByFloor(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityByGroupByFloor(parameter);
    }

    /**
     * 根据功能区分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数为功能区筛选条件
     * @return 返回功能区分组
     */
    @Override
    public List<CapacityDeviceTypeStatisticsDto> queryCapacityByGroupByDevice(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityByGroupByDevice(parameter);
    }

    /**
     * 根据楼层和功能区分组
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 17:06
     * @param parameter 参数
     * @return 返回楼层和功能区分组
     */
    @Override
    public List<CapacityDeviceTypeStatisticsDto> queryCapacityByGroupByFloorAndDevice(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityByGroupByFloorAndDevice(parameter);
    }

    /**
     * 查询根据机柜查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return 机柜容量信息
     */
    @Override
    public List<CapacityCabinetStatisticsDto> queryCapacityByCabinet(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityByCabinet(parameter);
    }


    /**
     * 查询根据pdu查询容量信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:34
     * @param parameter 参数
     * @return pdu容量信息
     */
    @Override
    public List<CapacityPduStatisticsDto> queryCapacityByPdu(CabinetInfoListParameter parameter) {
        return cabinetDao.queryCapacityByPdu(parameter);
    }


    /**
     * 查询机柜信息列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 机柜信息列表数据
     */
    @Override
    public List<CabinetDto> queryCabinetList(QueryCondition queryCondition) {
        return cabinetDao.queryCabinetList(queryCondition);
    }


    /**
     * 查询机柜信息个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 机柜信息个数
     */
    @Override
    public int queryCabinetCount(QueryCondition queryCondition) {
        return cabinetDao.queryCabinetCount(queryCondition);
    }



    /**
     * 导入机柜数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:59
     * @param file 机柜数据文件
     * @return 返回机柜数据结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importCabinet(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<Cabinet> cabinets = new ArrayList<>();
        try {
            //读取机柜excel信息
            stringListMap = cabinetExcelImportRead.readExcel(file);
            //读取机柜信息
            cabinets = (List) stringListMap.get(CabinetSheetEnum.SHEET_ONE.getSheetName());

            if (!ObjectUtils.isEmpty(cabinets)) {
                //删除导入数据当年当月的机柜数据
                cabinetDao.deleteCabinet(cabinets.get(0));

                //批量新增机柜数据
                cabinetDao.insertCabinetBatch(cabinets);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(CapacityResultCode.IMPORT_CABINET_ERROR, CapacityResultMsg.IMPORT_CABINET_ERROR);
        }
        return ResultUtils.success();
    }


    /**
     * 机柜数据列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜数据列表的信息
     */
    @Override
    public Result cabinetList(QueryCondition condition) {
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
        List<CabinetDto> baseCabinetDtoList = this.queryCabinetList(condition);
        //查询数据个数
        Integer count = this.queryCabinetCount(condition);
        // 构造返回结果
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, baseCabinetDtoList);
        // 返回数据
        return ResultUtils.pageSuccess(pageBean);
    }


}
