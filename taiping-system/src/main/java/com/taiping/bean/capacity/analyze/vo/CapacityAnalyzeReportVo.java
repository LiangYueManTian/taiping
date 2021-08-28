package com.taiping.bean.capacity.analyze.vo;

import com.taiping.bean.capacity.analyze.CapacityViewData;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import lombok.Data;

import java.util.List;

/**
 * 容量分析报告返回数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 21:03
 */
@Data
public class CapacityAnalyzeReportVo {

    /**
     * 阈值集合
     */
    private List<CapacityThresholdInfo> dataList;

    /**
     * 显示图名称集合（年月）
     */
    private List<String> xDataList;

    /**
     * 显示图数据
     */
    private List<List<CapacityViewData>> yDataList;
}
