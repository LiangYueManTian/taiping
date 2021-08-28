package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.CapacityCabinetStatisticsDto;
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
import com.taiping.utils.common.analyze.capacity.AbstractCapacityAnalyze;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 空间机柜容量分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class CabinetSpaceAnalyze extends AbstractCapacityAnalyze {

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
     * 空间容量分析类
     */
    @Autowired
    private SpaceAnalyze spaceAnalyze;


    /**
     * 容量分析
     */
    @Autowired
    private CapacityAnalyze capacityAnalyze;





    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param cabinetStatisticsDtoList 一年的数据
     * @return 返回同比
     */
    protected List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<CapacityCabinetStatisticsDto> cabinetStatisticsDtoList) {
        //计算同比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = spaceAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, cabinetStatisticsDtoList, beforeYearDate, type, ThresholdTypeEnum.CABINET);
        }
        return capacityThresholdInfoList;
    }


    /**
     * 获取比较的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:02
     * @param capacitySpaceStatisticsDto 空间楼层容量的参数
     * @return 比较的值
     */
    public static String getCompareValue(CapacityCabinetStatisticsDto capacitySpaceStatisticsDto) {
        return capacitySpaceStatisticsDto.getCabinetUniqueName();
    }

    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param cabinetStatisticsDtoList 一年的数据
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回环比
     */
    protected List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<CapacityCabinetStatisticsDto> cabinetStatisticsDtoList) {
        //计算环比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = spaceAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, cabinetStatisticsDtoList, beforeDate, type, ThresholdTypeEnum.CABINET);
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
        //获取机柜的阈值信息
        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(ThresholdCapacityEnum.CABINET_SPACE_PERCENT.getThresholdCode());

        //查询一年每个机柜的空间容量占比
        List<CapacityCabinetStatisticsDto> cabinetStatisticsDtoList = this.getSearchCabinetStatisticsDtoList(analysisYear, analysisMonth);

        //算出每一个机柜的空间和电力容量占比
        cabinetStatisticsDtoList = SpaceAnalyze.setPercentToData(cabinetStatisticsDtoList);

        //当月数据
        List<CapacityCabinetStatisticsDto> nowMonthList = cabinetStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //容量机柜阈值信息
        List<CapacityThresholdInfo> capacityThresholdInfoList = spaceAnalyze.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, ThresholdTypeEnum.CABINET);
        //计算环比
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, cabinetStatisticsDtoList);
        //计算同比
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, cabinetStatisticsDtoList);
        //计算推荐或显示数据
        List<CapacityThresholdRelatedInfo> relatedInfoList = new ArrayList<>();
        //计算图表显示信息
        List<CapacityThresholdRelatedViewInfo> viewInfoList = spaceAnalyze.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, cabinetStatisticsDtoList, ThresholdTypeEnum.CABINET);
        //形成建议信息
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, ThresholdTypeEnum.CABINET);
        //形成运维管理活动项
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, ThresholdTypeEnum.CABINET);
        CapacityAnalyzeData capacityAnalyzeData = new CapacityAnalyzeData();
        capacityAnalyzeData.setThresholdInfoList(capacityThresholdInfoList);
        capacityAnalyzeData.setRelatedInfoList(relatedInfoList);
        capacityAnalyzeData.setRelatedViewInfoList(viewInfoList);
        capacityAnalyzeData.setManageActivityList(manageActivityList);
        return capacityAnalyzeData;
    }

    /**
     * 容量楼层数据查询当前月份和去年同期月份的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<CapacityCabinetStatisticsDto> getSearchCabinetStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = SpaceAnalyze.generateSearchParam(analysisYear, analysisMonth);
        List<CapacityCabinetStatisticsDto> cabinetStatisticsDtoList = cabinetService.queryCapacityByCabinet(parameter);
        return cabinetStatisticsDtoList;
    }


}
