package com.taiping.biz.operation.service.impl;

import com.alibaba.fastjson.JSON;
import com.taiping.biz.operation.dao.HealthyDao;
import com.taiping.biz.operation.service.HealthyService;
import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.Result;
import com.taiping.entity.operation.FireReservoir;
import com.taiping.entity.operation.HealthyParam;
import com.taiping.entity.operation.WaterCooledUnit;
import com.taiping.enums.operation.HealthyParamEnum;
import com.taiping.enums.operation.HealthySheetEnum;
import com.taiping.exception.BizException;
import com.taiping.read.operation.HealthyExcelImportRead;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 健康卡服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Slf4j
@Service
public class HealthyServiceImpl implements HealthyService {

    @Autowired
    private HealthyDao healthyDao;

    @Autowired
    private HealthyExcelImportRead healthyExcelImportRead;

    /**
     * 导入健康卡消防蓄水池和水冷机组运行时长Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importWaterAndCooled(MultipartFile file) {
        Map<String, List<ExcelReadBean>> map;
        try {
            map = healthyExcelImportRead.readExcel(file);
        } catch (IOException | InvalidFormatException e) {
            //文件格式错误
            log.error("文件({})格式错误:{}", file.getOriginalFilename(), e);
            throw new BizException(OperationResultCode.FILE_TYPE_ERROR,
                    OperationResultMsg.FILE_TYPE_ERROR);
        }
        List<HealthyParam> healthyParamList = new ArrayList<>();
        List<WaterCooledUnit> cooledUnitList = (List)map.get(HealthySheetEnum.WATER_COOLED_UNIT.getSheetName());
        for (WaterCooledUnit cooledUnit : cooledUnitList) {
            HealthyParam healthyParam = new HealthyParam();
            healthyParam.setParamId(NineteenUUIDUtils.uuid());
            healthyParam.setParamType(HealthyParamEnum.WATER_COOLED_UNIT.getType());
            healthyParam.setParamValue(JSON.toJSONString(cooledUnit));
            healthyParamList.add(healthyParam);
        }
        List<FireReservoir> fireReservoirList = (List)map.get(HealthySheetEnum.FIRE_RESERVOIR.getSheetName());
        for (FireReservoir fireReservoir : fireReservoirList) {
            HealthyParam healthyParam = new HealthyParam();
            healthyParam.setParamId(NineteenUUIDUtils.uuid());
            healthyParam.setParamType(HealthyParamEnum.FIRE_RESERVOIR.getType());
            healthyParam.setParamValue(JSON.toJSONString(fireReservoir));
            healthyParamList.add(healthyParam);
        }
        healthyDao.deleteHealthyParam();
        healthyDao.addHealthyParamBatch(healthyParamList);
        return ResultUtils.success();
    }

    /**
     * 查询健康卡消防蓄水池
     *
     * @return Result
     */
    @Override
    public Result queryWaterCooledUnit() {
        List<HealthyParam> healthyParamList = healthyDao.queryHealthyParam(HealthyParamEnum.WATER_COOLED_UNIT.getType());
        List<WaterCooledUnit> cooledUnitList = new ArrayList<>();
        for (HealthyParam healthyParam : healthyParamList) {
            WaterCooledUnit cooledUnit = JSON.parseObject(healthyParam.getParamValue(), WaterCooledUnit.class);
            cooledUnitList.add(cooledUnit);
        }
        cooledUnitList.sort(Comparator.comparing(WaterCooledUnit::getUnitName));
        return ResultUtils.success(cooledUnitList);
    }

    /**
     * 查询健康卡消防蓄水池
     *
     * @return Result
     */
    @Override
    public Result queryFireReservoir() {
        List<HealthyParam> healthyParamList = healthyDao.queryHealthyParam(HealthyParamEnum.FIRE_RESERVOIR.getType());
        List<FireReservoir> fireReservoirList = new ArrayList<>();
        for (HealthyParam healthyParam : healthyParamList) {
            FireReservoir fireReservoir = JSON.parseObject(healthyParam.getParamValue(), FireReservoir.class);
            fireReservoirList.add(fireReservoir);
        }
        fireReservoirList.sort(Comparator.comparing(FireReservoir::getRowNumber));
        return ResultUtils.success(fireReservoirList);
    }
}
