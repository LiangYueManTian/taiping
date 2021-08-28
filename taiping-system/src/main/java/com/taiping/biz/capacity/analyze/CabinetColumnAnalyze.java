package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.CapacityColumnStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdCapacityEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.AbstractCapacityAnalyze;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 列头柜容量分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class CabinetColumnAnalyze extends AbstractCapacityAnalyze {

    /**
     * 容量逻辑层实现类
     */
    @Autowired
    private CabinetService cabinetService;


    /**
     * 运维管理活动项逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;


    /**
     * 容量阈值信息逻辑层
     */
    @Autowired
    private CapacityThresholdInfoService thresholdInfoService;

    /**
     * 容量阈值关联信息逻辑层
     */
    @Autowired
    private CapacityThresholdRelatedInfoService relatedInfoService;


    /**
     * 容量阈值关联显示信息逻辑层
     */
    @Autowired
    private CapacityThresholdRelatedViewInfoService viewInfoService;


    /**
     * 电力容量分析类
     */
    @Autowired
    private ElectricAnalyze electricAnalyze;

    /**
     * 容量分析
     */
    @Autowired
    private CapacityAnalyze capacityAnalyze;




    /**
     * 获取比较的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:02
     * @param capacityColumnStatisticsDto 电力容量列头柜的参数
     * @return 比较的值
     */
    public static String getCompareValue(CapacityColumnStatisticsDto capacityColumnStatisticsDto) {
        return capacityColumnStatisticsDto.getArrayCabinetName();
    }

    /**
     * 获取总数的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:02
     * @param capacityColumnStatisticsDto 电力容量列头柜的参数
     * @return 总数的值
     */
    public static double getAllValue(CapacityColumnStatisticsDto capacityColumnStatisticsDto) {
        return capacityColumnStatisticsDto.getArrayCabinetDesignLoad();
    }

    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param columnStatisticsDtoList 一年的数据
     * @return 返回同比
     */
    protected List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<CapacityColumnStatisticsDto> columnStatisticsDtoList) {
        //计算同比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = electricAnalyze.castGroupInfo(capacityThresholdInfoList, columnStatisticsDtoList, beforeYearDate, type, ThresholdTypeEnum.CABINET_COLUMN);
        }
        return capacityThresholdInfoList;
    }

    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param columnStatisticsDtoList 一年的数据
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回环比
     */
    protected List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<CapacityColumnStatisticsDto> columnStatisticsDtoList) {
        //计算环比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = electricAnalyze.castGroupInfo(capacityThresholdInfoList, columnStatisticsDtoList, beforeDate, type, ThresholdTypeEnum.CABINET_COLUMN);
        }
        return capacityThresholdInfoList;
    }

    /**
     * 新增运维管理活动信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param manageActivityList 运维管理活动
     */
    @Override
    protected void insertManageActivityBatch(List<ManageActivity> manageActivityList) {
        manageActivityService.insertManageActivity(ManageSourceEnum.CAPACITY, manageActivityList);
    }

    /**
     * 新增容量阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdInfoList 容量阈值
     */
    @Override
    protected void insertThresholdInfoBatch(List<CapacityThresholdInfo> capacityThresholdInfoList) {
        thresholdInfoService.insertThresholdInfoBatch(capacityThresholdInfoList);
    }

    /**
     * 新增容量阈值关联信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedInfoList 容量阈值关联集合
     */
    @Override
    protected void insertThresholdRelatedInfo(List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList) {
        relatedInfoService.insertThresholdRelatedBatch(capacityThresholdRelatedInfoList);
    }

    /**
     * 新增容量阈值关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param capacityThresholdRelatedViewInfoList 容量阈值关联显示信息集合
     */
    @Override
    protected void insertThresholdRelatedViewInfo(List<CapacityThresholdRelatedViewInfo> capacityThresholdRelatedViewInfoList) {
        viewInfoService.insertThresholdRelatedViewInfoBatch(capacityThresholdRelatedViewInfoList);
    }

    /**
     * 自定义实现分析数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:24
     * @param searchCapacityThresholdList 阈值信息
     * @param analysisYear 分析的年份
     * @param analysisMonth 分析的月份
     * @return 返回需要新增的分析数据信息
     */
    @Override
    protected CapacityAnalyzeData analyzeDataRealize(Map<String, List<SystemSetting>> searchCapacityThresholdList, Integer analysisYear, Integer analysisMonth) {
        //获取楼层的阈值信息
        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(ThresholdCapacityEnum.ARRAY_CABINET_ELECTRIC.getThresholdCode());

        //查询一年每个列头柜的电力容量占比
        List<CapacityColumnStatisticsDto> columnStatisticsDtoList = this.getSearchColumnStatisticsDtoList(analysisYear, analysisMonth);

        //算出每一个列头柜的空间和电力容量占比
        columnStatisticsDtoList = this.setPercentToData(columnStatisticsDtoList);

        //当月数据
        List<CapacityColumnStatisticsDto> nowMonthList = columnStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //容量楼层阈值信息
        List<CapacityThresholdInfo> capacityThresholdInfoList = electricAnalyze.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, ThresholdTypeEnum.CABINET_COLUMN);
        //计算环比
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, columnStatisticsDtoList);
        //计算同比
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, columnStatisticsDtoList);
        //计算推荐或显示数据
        List<CapacityThresholdRelatedInfo> relatedInfoList = new ArrayList<>();
        //计算图表显示信息
        List<CapacityThresholdRelatedViewInfo> viewInfoList = electricAnalyze.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, columnStatisticsDtoList, ThresholdTypeEnum.CABINET_COLUMN);
        //形成建议信息
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, ThresholdTypeEnum.CABINET_COLUMN);
        //形成运维管理活动项
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, ThresholdTypeEnum.CABINET_COLUMN);
        CapacityAnalyzeData capacityAnalyzeData = new CapacityAnalyzeData();
        capacityAnalyzeData.setThresholdInfoList(capacityThresholdInfoList);
        capacityAnalyzeData.setRelatedInfoList(relatedInfoList);
        capacityAnalyzeData.setRelatedViewInfoList(viewInfoList);
        capacityAnalyzeData.setManageActivityList(manageActivityList);
        return capacityAnalyzeData;
    }


    /**
     * 算出电力机柜列容量的占比
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:33
     * @param columnDtoList column集合
     * @return 返回统计数据
     */
    public List<CapacityColumnStatisticsDto> setPercentToData(List<CapacityColumnStatisticsDto> columnDtoList) {
        if (!ObjectUtils.isEmpty(columnDtoList)) {
            for (CapacityColumnStatisticsDto capacityColumnStatisticsDto : columnDtoList) {
                double usedPower = capacityColumnStatisticsDto.getUsedActualPower();
                double allRatedPower = capacityColumnStatisticsDto.getArrayCabinetDesignLoad();
                //计算列头柜电力容量占比
                double powerPercent = CalculateUtil.castPercent(usedPower, allRatedPower);
                capacityColumnStatisticsDto.setUsedActualPercent(powerPercent);
            }
        }
        return columnDtoList;
    }

    /**
     * 容量楼层数据查询当前月份和去年同期月份的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<CapacityColumnStatisticsDto> getSearchColumnStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = ElectricAnalyze.generateSearchParam(analysisYear, analysisMonth);
        List<CapacityColumnStatisticsDto> columnStatisticsDtoList = cabinetService.queryCapacityGroupByColumn(parameter);
        return columnStatisticsDtoList;
    }
}
