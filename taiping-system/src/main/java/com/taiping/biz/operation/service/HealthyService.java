package com.taiping.biz.operation.service;

import com.taiping.entity.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 健康卡服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public interface HealthyService {
    /**
     * 导入健康卡消防蓄水池和水冷机组运行时长Excel表
     * @param file Excel表
     * @return Result
     */
    Result importWaterAndCooled(MultipartFile file);

    /**
     * 查询健康卡消防蓄水池
     * @return Result
     */
    Result queryWaterCooledUnit();

    /**
     * 查询健康卡消防蓄水池
     * @return Result
     */
    Result queryFireReservoir();
}
