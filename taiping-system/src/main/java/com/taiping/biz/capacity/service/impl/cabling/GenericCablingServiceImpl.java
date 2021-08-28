package com.taiping.biz.capacity.service.impl.cabling;

import com.baomidou.mybatisplus.plugins.Page;
import com.taiping.bean.capacity.cabling.dto.CapacityCablingStatisticsDto;
import com.taiping.bean.capacity.cabling.dto.GenericCablingDto;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.biz.capacity.dao.cabling.GenericCablingDao;
import com.taiping.biz.capacity.service.cabling.GenericCablingService;
import com.taiping.constant.capacity.CapacityResultCode;
import com.taiping.constant.capacity.CapacityResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.cabling.GenericCabling;
import com.taiping.enums.cabling.GenericCablingSheetEnum;
import com.taiping.exception.BizException;
import com.taiping.read.cabling.GenericCablingExcelImportRead;
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
 * 综合布线逻辑实现类
 * @author hedongwei@wistronits.com
 * @date 2019/10/12 9:35
 */
@Service
@Slf4j
public class GenericCablingServiceImpl implements GenericCablingService {

    /**
     * 自动注入综合布线导入数据
     */
    @Autowired
    private GenericCablingExcelImportRead genericCablingExcelImportRead;

    /**
     * 自动注入综合布线持久层
     */
    @Autowired
    private GenericCablingDao genericCablingDao;


    /**
     * 综合布线最新数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @return 综合布线最新数据
     */
    @Override
    public GenericCablingDto queryTopDataByTime() {
        return genericCablingDao.queryTopDataByTime();
    }


    /**
     * 查询综合布线配线架统计数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @param parameter 参数
     * @return 综合布线最新数据
     */
    @Override
    public List<CapacityCablingStatisticsDto> queryCablingGroupByStatusAndRack(GenericCablingListParameter parameter) {
        return genericCablingDao.queryCablingGroupByStatusAndRack(parameter);
    }


    /**
     * 查询综合布线分析统计数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 11:39
     * @param parameter 参数
     * @return 综合布线最新数据
     */
    @Override
    public List<CapacityCablingStatisticsDto> queryCablingGroupByStatusAndDate(GenericCablingListParameter parameter) {
        return genericCablingDao.queryCablingGroupByStatusAndDate(parameter);
    }

    /**
     * 综合布线列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 16:02
     * @param condition 条件
     * @return 返回综合布线列表的内容
     */
    @Override
    public List<GenericCablingDto> queryGenericCablingList(QueryCondition condition) {
        return genericCablingDao.queryGenericCablingList(condition);
    }


    /**
     * 综合布线列表数据数量
     * @author hedongwei@wistronits.com
     * @date  2019/10/28 16:02
     * @param condition 条件
     * @return 返回综合布线列表数据数量
     */
    @Override
    public int queryGenericCablingCount(QueryCondition condition) {
        return genericCablingDao.queryGenericCablingCount(condition);
    }


    /**
     * 查询综合布线列表
     * @author hedongwei@wistronits.com
     * @date  2019/10/24 13:54
     * @param condition 条件
     * @return 返回综合布线列表
     */
    @Override
    public Result genericCablingList(QueryCondition condition) {
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
        List<GenericCablingDto> genericCablingDtoList = this.queryGenericCablingList(condition);
        //查询数据个数
        Integer count = this.queryGenericCablingCount(condition);
        // 构造返回结果
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, genericCablingDtoList);
        // 返回数据
        return ResultUtils.pageSuccess(pageBean);
    }


    /**
     * 导入综合布线数据
     *
     * @param file 导入的文件
     * @return 返回导入的结果
     * @author hedongwei@wistronits.com
     * @date 2019/10/12 9:17
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importGenericCabling(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<GenericCabling> genericCablingList = new ArrayList<>();
        try {
            //获取综合布线表格map
            stringListMap = genericCablingExcelImportRead.readExcel(file);
            //获取综合布线数据
            genericCablingList = (List) stringListMap.get(GenericCablingSheetEnum.SHEET_ONE.getSheetName());

            if (!ObjectUtils.isEmpty(genericCablingList)) {
                //删除导入数据当年当月的已经存在的综合布线数据
                genericCablingDao.deleteGenericCabling(genericCablingList.get(0));
                List<GenericCabling> insertDataList = new ArrayList<>();
                for (int i = 0 ; i < genericCablingList.size(); i ++) {
                    insertDataList.add(genericCablingList.get(i));
                    if (insertDataList.size() >= 1000 || i == genericCablingList.size() - 1) {
                        //批量新增综合布线数据
                        genericCablingDao.insertGenericCablingBatch(insertDataList);
                        insertDataList = new ArrayList<>();
                    }
                }
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(CapacityResultCode.IMPORT_GENERIC_CABLING_ERROR, CapacityResultMsg.IMPORT_GENERIC_CABLING_ERROR);
        }
        return ResultUtils.success();
    }
}
