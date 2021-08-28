package com.taiping.biz.capacity.service;

import com.taiping.entity.Result;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;

/**
 * 容量逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:43
 */
public interface CapacityService {

    /**
     * 返回分析数据结果
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return 返回分析数据结果
     */
    Result capacityAnalysis();


    /**
     * 保存预览数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return 返回保存预览数据结果
     */
    Result savePreviewData();


    /**
     * 查询容量分析报告
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:46
     * @param capacityThresholdInfo 阈值信息
     * @return 返回容量的分析报告数据
     */
     Result queryCapacityAnalyzeReport(CapacityThresholdInfo capacityThresholdInfo);
}
