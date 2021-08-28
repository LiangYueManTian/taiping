package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabinet.dto.CapacityDeviceTypeStatisticsDto;
import com.taiping.bean.capacity.cabinet.dto.CapacityFloorStatisticsDto;
import com.taiping.bean.capacity.cabinet.parameter.CabinetInfoListParameter;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.biz.capacity.service.cabinet.CabinetService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdCapacityEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
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
 * 空间楼层容量分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class FloorSpaceAnalyze extends AbstractCapacityAnalyze {

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
     * 计算推荐数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param capacityThresholdInfoList 阈值信息集合
     * @param deviceTypeStatisticsDtoList 楼层功能区统计信息集合
     * @return 返回推荐数据信息
     */
    protected List<CapacityThresholdRelatedInfo> calculateRecommendInfo(List<CapacityThresholdInfo> capacityThresholdInfoList, List<CapacityDeviceTypeStatisticsDto> deviceTypeStatisticsDtoList) {
        List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList = new ArrayList<>();
        //计算推荐数据
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                List<CapacityDeviceTypeStatisticsDto> recommendList = new ArrayList<>();
                String floor = capacityThresholdInfo.getThresholdData();
                for (CapacityDeviceTypeStatisticsDto deviceTypeStatisticsDto : deviceTypeStatisticsDtoList) {
                    if (floor.equals(deviceTypeStatisticsDto.getFloorUniqueName())) {
                        recommendList.add(deviceTypeStatisticsDto);
                    }
                }
                if (CapacityConstant.ADVICE_TYPE_DIFF.equals(capacityThresholdInfo.getAdviceType()) &&
                        !ObjectUtils.isEmpty(recommendList)) {
                    recommendList = recommendList.stream().sorted((s1, s2) -> spaceAnalyze.compareInfo(s2, s1)).collect(Collectors.toList());
                    //裁剪数据
                    for (int i = 0; i < CapacityConstant.RECOMMEND_COUNT ; i++) {
                        if (i + 1 <= recommendList.size()) {
                            CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = spaceAnalyze.getOneRelatedInfo(capacityThresholdInfo, recommendList.get(i), ThresholdTypeEnum.DEVICE_TYPE);
                            capacityThresholdRelatedInfoList.add(capacityThresholdRelatedInfo);
                        }
                    }
                } else if (CapacityConstant.ADVICE_TYPE_ADD.equals(capacityThresholdInfo.getAdviceType()) &&
                        !ObjectUtils.isEmpty(recommendList)) {
                    recommendList = recommendList.stream().sorted((s1, s2) -> spaceAnalyze.compareInfo(s1, s2)).collect(Collectors.toList());
                    //采购数据
                    for (int i = 0; i < CapacityConstant.RECOMMEND_COUNT ; i++) {
                        if (i + 1 <=  recommendList.size()) {
                            CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = spaceAnalyze.getOneRelatedInfo(capacityThresholdInfo, recommendList.get(i), ThresholdTypeEnum.DEVICE_TYPE);
                            capacityThresholdRelatedInfoList.add(capacityThresholdRelatedInfo);
                        }
                    }
                }
            }
        }

        //返回数据
        return capacityThresholdRelatedInfoList;
    }








    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param floorStatisticsDtoList 一年的数据
     * @return 返回同比
     */
    protected List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<CapacityFloorStatisticsDto> floorStatisticsDtoList) {
        //计算同比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = spaceAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, floorStatisticsDtoList, beforeYearDate, type, ThresholdTypeEnum.FLOOR);
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
    public static String getCompareValue(CapacityFloorStatisticsDto capacitySpaceStatisticsDto) {
        return capacitySpaceStatisticsDto.getFloorUniqueName();
    }

    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param floorStatisticsDtoList 一年的数据
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回环比
     */
    protected List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<CapacityFloorStatisticsDto> floorStatisticsDtoList) {
        //计算环比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = spaceAnalyze.castGroupInfo(analysisYear, analysisMonth, capacityThresholdInfoList, floorStatisticsDtoList, beforeDate, type, ThresholdTypeEnum.FLOOR);
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
        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(ThresholdCapacityEnum.FLOOR_SPACE_PERCENT.getThresholdCode());

        //查询当月的功能区数据信息
        List<CapacityDeviceTypeStatisticsDto> deviceStatisticsDtoList = spaceAnalyze.getSearchFloorAndDeviceList(analysisYear, analysisMonth);

        //查询一年每个楼层的空间容量占比
        List<CapacityFloorStatisticsDto> floorStatisticsDtoList = this.getSearchFloorStatisticsDtoList(analysisYear, analysisMonth);

        //算出每一个楼层的空间和电力容量占比
        floorStatisticsDtoList = SpaceAnalyze.setPercentToData(floorStatisticsDtoList);

        //当月数据
        List<CapacityFloorStatisticsDto> nowMonthList = floorStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //容量楼层阈值信息
        List<CapacityThresholdInfo> capacityThresholdInfoList = spaceAnalyze.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, ThresholdTypeEnum.FLOOR);
        //计算环比
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, floorStatisticsDtoList);
        //计算同比
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, floorStatisticsDtoList);
        //计算推荐或显示数据
        List<CapacityThresholdRelatedInfo> relatedInfoList = calculateRecommendInfo(capacityThresholdInfoList, deviceStatisticsDtoList);
        //计算图表显示信息
        List<CapacityThresholdRelatedViewInfo> viewInfoList = spaceAnalyze.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, floorStatisticsDtoList, ThresholdTypeEnum.FLOOR);
        //形成建议信息
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, ThresholdTypeEnum.FLOOR);
        //形成运维管理活动项
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, ThresholdTypeEnum.FLOOR);
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
    public List<CapacityFloorStatisticsDto> getSearchFloorStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {
        CabinetInfoListParameter parameter = SpaceAnalyze.generateSearchParam(analysisYear, analysisMonth);
        List<CapacityFloorStatisticsDto> floorStatisticsDtoList = cabinetService.queryCapacityByGroupByFloor(parameter);
        return floorStatisticsDtoList;
    }


}
