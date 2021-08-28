package com.taiping.biz.capacity.service.impl.cabinet;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.bean.capacity.cabinet.dto.BaseCabinetDto;
import com.taiping.biz.capacity.dao.cabinet.BaseCabinetDao;
import com.taiping.biz.capacity.service.cabinet.BaseCabinetService;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabinet.BaseCabinet;
import com.taiping.enums.cabinet.CabinetSheetEnum;
import com.taiping.exception.BizException;
import com.taiping.read.cabinet.BaseCabinetExcelImportRead;
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
public class BaseCabinetServiceImpl implements BaseCabinetService {

    /**
     * 机柜基础信息导入数据类
     */
    @Autowired
    private BaseCabinetExcelImportRead cabinetExcelImportRead;

    /**
     * 机柜基础信息持久层
     */
    @Autowired
    private BaseCabinetDao baseCabinetDao;


    /**
     * 查询最新的机柜基础数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 14:05
     * @return 返回最新的机柜基础数据
     */
    @Override
    public BaseCabinetDto queryTopDataByTime() {
        return baseCabinetDao.queryTopDataByTime();
    }

    /**
     * 查询基础机柜信息列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息列表数据
     */
    @Override
    public List<BaseCabinetDto> queryBaseCabinetList(QueryCondition queryCondition) {
        return baseCabinetDao.queryBaseCabinetList(queryCondition);
    }


    /**
     * 查询基础机柜信息个数
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 14:53
     * @param queryCondition 查询条件
     * @return 基础机柜信息个数
     */
    @Override
    public int queryBaseCabinetCount(QueryCondition queryCondition) {
        return baseCabinetDao.queryBaseCabinetCount(queryCondition);
    }

    /**
     * 导入机柜基础数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:54
     * @param file 机柜基础数据文件
     * @return 返回机柜基础导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importBaseCabinet(MultipartFile file) {

        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<BaseCabinet> baseCabinets = new ArrayList<>();
        try {
            //读取机柜excel数据
            stringListMap = cabinetExcelImportRead.readExcel(file);
            //获取机柜基础excel数据
            baseCabinets = (List) stringListMap.get(CabinetSheetEnum.SHEET_ONE.getSheetName());
            for (BaseCabinet baseCabinetOne : baseCabinets) {
                System.out.println(baseCabinetOne.toString());
            }

            //删除机柜基础数据数据
            baseCabinetDao.deleteAllBaseCabinet();

            if (!ObjectUtils.isEmpty(baseCabinets)) {
                //批量新增机柜基础数据
                baseCabinetDao.insertBaseCabinetBatch(baseCabinets);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(CapacityResultCode.IMPORT_BASE_CABINET_ERROR, CapacityResultMsg.IMPORT_BASE_CABINET_ERROR);
        }
        return ResultUtils.success();
    }


    /**
     * 根据机柜唯一标识修改机柜基础信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/11 10:11
     * @param baseCabinet 机柜父级信息表
     * @return 返回机柜基础信息修改结果
     */
    @Override
    public int updateBaseCabinetByCabinetName(BaseCabinet baseCabinet) {
        return baseCabinetDao.updateBaseCabinetByCabinetName(baseCabinet);
    }


    /**
     * 机柜基础数据列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回机柜基础数据列表的信息
     */
    @Override
    public Result cabinetBaseList(QueryCondition condition) {
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
        List<BaseCabinetDto> baseCabinetDtoList = this.queryBaseCabinetList(condition);
        //查询数据个数
        Integer count = this.queryBaseCabinetCount(condition);
        // 构造返回结果
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, baseCabinetDtoList);
        // 返回数据
        return ResultUtils.pageSuccess(pageBean);
    }
}
