package com.taiping.bean.energy.analyze.vo;

import com.taiping.bean.energy.analyze.EnergyViewData;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import lombok.Data;

import java.util.List;

/**
 * 能耗分析报告返回数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 21:03
 */
@Data
public class EnergyAnalyzeReportVo {

    /**
     * 分析数据集合
     */
    private List<EnergyAnalyzeInfo> dataList;

    /**
     * 显示图名称集合（年月）
     */
    private List<String> xDataList;

    /**
     * 显示图数据
     */
    private List<List<EnergyViewData>> yDataList;
}
