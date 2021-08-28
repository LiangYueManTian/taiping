package com.taiping.biz.energy.service;

import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.entity.Result;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 能耗逻辑层
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 20:59
 */
public interface EnergyService {

    /**
     * 导入动力能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/10/10 16:54
     * @param file 能耗数据文件
     * @return 返回能耗数据导入结果
     */
    Result importPowerEnergyInfo(@RequestBody MultipartFile file);


    /**
     * 导入全部的能耗数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/12 17:07
     * @param file 文件
     * @return  返回导入全部的能耗数据的结果
     */
    Result importAllEnergy(@RequestBody MultipartFile file);


    /**
     * 查询总能耗数据列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 21:47
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     */
    Result queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter);


    /**
     * 查询动力能耗列表不分页
     * @author hedongwei@wistronits.com
     * @date  2019/11/15 13:17
     * @param parameter 参数
     * @return 返回动力能耗列表数据
     */
    Result queryPowerEnergyListNotPage(PowerEnergyListParameter parameter);


    /**
     * 能耗分析
     * @author hedongwei@wistronits.com
     * @date  2019/11/20 9:42
     * @return 返回能耗分析的结果
     */
    Result energyAnalysis();

    /**
     * 保存预览数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/20 9:42
     * @return 返回能耗分析的结果
     */
    Result savePreviewData();


    /**
     * 查询能耗分析报告
     * @author hedongwei@wistronits.com
     * @date  2019/11/21 11:05
     * @param energyAnalyzeInfo 能耗分析参数
     * @return 返回分析报告的数据
     */
    Result queryEnergyAnalyzeReport(EnergyAnalyzeInfo energyAnalyzeInfo);


}
