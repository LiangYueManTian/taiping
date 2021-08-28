package com.taiping.biz.energy.controller;

import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.biz.energy.service.EnergyService;
import com.taiping.entity.Result;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 能耗控制层
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 20:53
 */
@RestController
@RequestMapping("/taiping/energy")
public class EnergyController {


    /**
     * 自动注入能耗逻辑层
     */
    @Autowired
    private EnergyService energyService;



    /**
     * 导入动力能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:28
     * @param file 导入的文件
     * @return 返回导入数据的结果
     */
    @PostMapping("/importPowerEnergyInfo")
    public Result importPowerEnergyInfo(@RequestBody MultipartFile file) {
        return energyService.importPowerEnergyInfo(file);
    }


    /**
     * 导入总能耗的数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:28
     * @param file 导入的文件
     * @return 返回导入数据的结果
     */
    @PostMapping("/importAllEnergy")
    public Result importAllEnergy(@RequestBody MultipartFile file) {
        return energyService.importAllEnergy(file);
    }


    /**
     * 查询总能耗数据列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     */
    @PostMapping("/queryAllEnergyListNotPage")
    public Result queryAllEnergyListNotPage(@RequestBody ElectricInstrumentListParameter parameter) {
        return energyService.queryAllEnergyListNotPage(parameter);
    }


    /**
     * 查询动力能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/11/15 13:17
     * @param parameter 参数
     * @return 返回动力能耗列表数据
     */
    @PostMapping("/queryPowerEnergyListNotPage")
    public Result queryPowerEnergyListNotPage(@RequestBody PowerEnergyListParameter parameter) {
        return energyService.queryPowerEnergyListNotPage(parameter);
    }

    /**
     * 能耗分析
     * @author hedongwei@wistronits.com
     * @date  2019/11/20 9:42
     * @return 返回能耗分析的结果
     */
    @GetMapping("/energyAnalysis")
    public Result energyAnalysis() {
        return energyService.energyAnalysis();
    }

    /**
     * 返回预览数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/20 14:43
     * @result 预览数据
     */
    @GetMapping("/savePreviewData")
    public Result savePreviewData() {
        return energyService.savePreviewData();
    }

    /**
     * 查询能耗分析报告
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 11:03
     * @param energyAnalyzeInfo 能耗分析报告
     * @return  返回查询能耗分析报告的内容
     */
    @PostMapping("/queryEnergyAnalyzeReport")
    public Result queryEnergyAnalyzeReport(@RequestBody EnergyAnalyzeInfo energyAnalyzeInfo) {
        return energyService.queryEnergyAnalyzeReport(energyAnalyzeInfo);
    }

}
