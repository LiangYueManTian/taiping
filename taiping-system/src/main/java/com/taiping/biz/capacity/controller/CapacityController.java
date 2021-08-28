package com.taiping.biz.capacity.controller;

import com.taiping.biz.capacity.service.CapacityService;
import com.taiping.entity.Result;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 容量控制层
 * @author hedongwei@wistronits.com
 * @date 2019/10/10 16:23
 */
@RestController
@RequestMapping("/taiping/capacity")
public class CapacityController {

    /**
     * 容量逻辑层
     */
    @Autowired
    private CapacityService capacityService;

    /**
     * 分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return 返回分析数据结果
     */
    @PostMapping("/capacityAnalysis")
    public Result capacityAnalysis() {
        return capacityService.capacityAnalysis();
    }



    /**
     * 保存预览数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @return 返回保存预览数据结果
     */
    @PostMapping("/savePreviewData")
    public Result savePreviewData() {
        return capacityService.savePreviewData();
    }

    /**
     * 查询分析报告数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/31 10:42
     * @param capacityThresholdInfo 分析报告查询参数
     * @return 返回分析报告数据
     */
    @PostMapping("/queryCapacityAnalyzeReport")
    public Result queryCapacityAnalyzeReport(@RequestBody CapacityThresholdInfo capacityThresholdInfo) {
        return capacityService.queryCapacityAnalyzeReport(capacityThresholdInfo);
    }
}
