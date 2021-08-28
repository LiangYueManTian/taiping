package com.taiping.biz.operation.controller;

import com.taiping.biz.operation.service.HealthyService;
import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.Result;
import com.taiping.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 健康卡控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@RestController
@RequestMapping("/taiping/healthy")
public class HealthyController {

    @Autowired
    private HealthyService healthyService;


    /**
     * 导入健康卡消防蓄水池和水冷机组运行时长Excel表
     *
     * @param file Excel表
     * @return Result
     */
    @PostMapping("/importWaterAndCooled")
    public Result importWaterAndCooled(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.warn(OperationResultCode.PARAM_ERROR,
                    OperationResultMsg.PARAM_ERROR);
        }
        return healthyService.importWaterAndCooled(file);
    }

    /**
     * 查询健康卡消防蓄水池
     * @return Result
     */
    @GetMapping("/queryWaterCooledUnit")
    public Result queryWaterCooledUnit() {
        return healthyService.queryWaterCooledUnit();
    }

    /**
     * 查询健康卡消防蓄水池
     * @return Result
     */
    @GetMapping("/queryFireReservoir")
    public Result queryFireReservoir() {
        return healthyService.queryFireReservoir();
    }
}
