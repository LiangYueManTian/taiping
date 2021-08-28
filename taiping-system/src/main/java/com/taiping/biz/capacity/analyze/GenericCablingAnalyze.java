package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeData;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.capacity.cabling.dto.CapacityCablingStatisticsDto;
import com.taiping.bean.capacity.cabling.parameter.GenericCablingListParameter;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedInfoService;
import com.taiping.biz.capacity.service.analyze.CapacityThresholdRelatedViewInfoService;
import com.taiping.biz.capacity.service.cabling.GenericCablingService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdCapacityEnum;
import com.taiping.enums.analyze.capacity.ThresholdModuleEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.analyze.capacity.ThresholdViewTypeEnum;
import com.taiping.enums.cabinet.CableTypeEnum;
import com.taiping.enums.cabling.GenericCablingStatusEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.AbstractCapacityAnalyze;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 综合布线容量分析类
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class GenericCablingAnalyze extends AbstractCapacityAnalyze {

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
     * 综合布线逻辑层
     */
    @Autowired
    private GenericCablingService genericCablingService;

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
     * @param rackStatisticsDtoList 配线架功能区统计信息集合
     * @return 返回推荐数据信息
     */
    protected List<CapacityThresholdRelatedInfo> calculateRecommendInfo(List<CapacityThresholdInfo> capacityThresholdInfoList, List<CapacityCablingStatisticsDto> rackStatisticsDtoList) {
        List<CapacityThresholdRelatedInfo> capacityThresholdRelatedInfoList = new ArrayList<>();
        //计算推荐数据
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                List<CapacityCablingStatisticsDto> recommendList = new ArrayList<>();
                String data  = capacityThresholdInfo.getThresholdData();
                for (CapacityCablingStatisticsDto rackOne : rackStatisticsDtoList) {
                    String compareData = this.getThresholdData(rackOne);
                    if (data.equals(compareData)) {
                        if (0 == rackOne.getPortPercent()) {
                            recommendList.add(rackOne);
                        }
                    }
                }

                if (CapacityConstant.ADVICE_TYPE_DIFF.equals(capacityThresholdInfo.getAdviceType()) &&
                        !ObjectUtils.isEmpty(recommendList)) {
                    //裁剪数据
                    for (int i = 0; i < CapacityConstant.RECOMMEND_COUNT ; i++) {
                        if (i + 1 <= recommendList.size()) {
                            CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = this.getOneRelatedInfo(capacityThresholdInfo, recommendList.get(i));
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
     * 获取单个推荐信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:40
     * @param capacityThresholdInfo 容量阈值信息表
     * @param recommend 容量功能区统计信息
     * @return 获取单个推荐信息
     */
    public CapacityThresholdRelatedInfo getOneRelatedInfo(CapacityThresholdInfo capacityThresholdInfo, CapacityCablingStatisticsDto recommend) {
        CapacityThresholdRelatedInfo capacityThresholdRelatedInfo = new CapacityThresholdRelatedInfo();
        capacityThresholdRelatedInfo.setThresholdCode(capacityThresholdInfo.getThresholdCode());
        capacityThresholdRelatedInfo.setThresholdRelatedId(NineteenUUIDUtils.uuid());
        capacityThresholdRelatedInfo.setType(ThresholdViewTypeEnum.VIEW.getViewType());
        String name = recommend.getConnectRackCode();
        capacityThresholdRelatedInfo.setName(name);
        capacityThresholdRelatedInfo.setDataName(name);
        capacityThresholdRelatedInfo.setValue(0);
        return capacityThresholdRelatedInfo;
    }






    /**
     * 计算显示图像数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param cablingStatisticsDtoList 综合布线的数据
     * @return 返回显示图像数据
     */
    protected List<CapacityThresholdRelatedViewInfo> calculateViewInfo(Integer analysisYear, Integer analysisMonth,
                                                                      List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                      List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {

        //因为查询出的数据今年当月的数据到去年同期的数据，所以统计的去年同期的数据需要删除
        List<CapacityThresholdRelatedViewInfo> viewInfoList = CapacityAnalyze.getDefaultViewData(analysisYear, analysisMonth, capacityThresholdInfoList);
        //需要进行趋势预测的数据信息
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        //需要往默认的数据中塞值
        for (CapacityCablingStatisticsDto capacityStatisticsDto : cablingStatisticsDtoList) {
            Integer dataYear = capacityStatisticsDto.getYear();
            Integer dataMonth = capacityStatisticsDto.getMonth();
            String value = this.getCompareKey(capacityStatisticsDto);
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                Integer thresholdYear = viewInfo.getYear();
                Integer thresholdMonth = viewInfo.getMonth();
                String thresholdValue = CapacityAnalyze.getCompareViewValue(viewInfo);
                if (dataYear.equals(thresholdYear) && dataMonth.equals(thresholdMonth) && value.equals(thresholdValue)) {
                    if (ThresholdModuleEnum.GENERIC_CABLING.getModule().equals(viewInfo.getModuleType())) {
                        //获取数据的端口占用率
                        double portPercent = capacityStatisticsDto.getPortPercent();
                        viewInfo.setValue(portPercent);
                    }
                }
            }
        }

        dataViewMap = CapacityAnalyze.getDataViewMap(viewInfoList);
        //返回填充预测数据之后的值
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = CapacityAnalyze.calculateExpectInfo(dataViewMap);
        return returnViewInfoList;
    }



    /**
     * 计算同比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @param cablingStatisticsDtoList 一年的数据
     * @return 返回同比
     */
    protected List<CapacityThresholdInfo> calculateYearOverYear(Integer analysisYear, Integer analysisMonth,
                                                                List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                                List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {
        //计算同比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
            String type = "1";
            capacityThresholdInfoList = this.castGroupInfo(capacityThresholdInfoList, cablingStatisticsDtoList, beforeYearDate, type);
        }
        return capacityThresholdInfoList;
    }


    /**
     * 计算环比
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 年份
     * @param analysisMonth 月份
     * @param cablingStatisticsDtoList 一年的数据
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回环比
     */
    protected List<CapacityThresholdInfo> calculateRingGrowth(Integer analysisYear, Integer analysisMonth,
                                                              List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                              List<CapacityCablingStatisticsDto> cablingStatisticsDtoList) {
        //计算环比
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            //去年同期数据
            CapacityAnalyzeTime beforeDate = CapacityAnalyze.castRingGrowthTime(analysisYear, analysisMonth);
            String type = "2";
            capacityThresholdInfoList = this.castGroupInfo(capacityThresholdInfoList, cablingStatisticsDtoList, beforeDate, type);
        }
        return capacityThresholdInfoList;
    }


    /**
     * 返回需要计算同比和环比的阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:40
     * @param capacityThresholdInfoList  容量阈值信息集合
     * @param dataStatisticsDtoList 查询容量数据集合
     * @param beforeDate 需要统计的时间那一年那一月
     * @param type 类型
     * @return 返回计算同比和环比的阈值信息
     */
    public List<CapacityThresholdInfo> castGroupInfo(List<CapacityThresholdInfo> capacityThresholdInfoList,
                                                    List<CapacityCablingStatisticsDto> dataStatisticsDtoList,
                                                    CapacityAnalyzeTime beforeDate, String type) {
        List<CapacityCablingStatisticsDto> beforeList = dataStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(beforeDate.getMonth()) && c.getYear().equals(beforeDate.getYear())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(beforeList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                double usedPort = capacityThresholdInfo.getUsedNumber();
                String dataValue = capacityThresholdInfo.getThresholdData();
                //增长率
                double growthPercent = 0;
                for (CapacityCablingStatisticsDto capacitySpaceStatisticsDto : beforeList) {
                    String compareDataValue = this.getThresholdData(capacitySpaceStatisticsDto);
                    if (dataValue.equals(compareDataValue)) {
                        double usedNumber = capacitySpaceStatisticsDto.getUsedPortNumber();
                        growthPercent = CapacityAnalyze.castPercentInfo(usedPort, usedNumber, usedNumber);
                        break;
                    }
                }
                if ("1".equals(type)) {
                    //同比
                    capacityThresholdInfo.setYearOverYearPercent(growthPercent);
                } else if ("2".equals(type)) {
                    //环比
                    capacityThresholdInfo.setRingGrowth(growthPercent);
                }
            }
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
        //获取综合布线的阈值信息
        List<SystemSetting> systemSettingList  = searchCapacityThresholdList.get(ThresholdCapacityEnum.GENERIC_CABLING.getThresholdCode());

        //查询当月的配线架信息
        List<CapacityCablingStatisticsDto> rackStatisticsDtoList = this.getSearchMonthRackList(analysisYear, analysisMonth);
        String rackType = "2";
        //设置配线架的端口占用率和端口数
        rackStatisticsDtoList = this.setPercentToData(rackStatisticsDtoList, rackType);

        //查询一年每个综合综合的占用端口数
        List<CapacityCablingStatisticsDto> cablingStatisticsDtoList = this.getSearchCablingStatisticsDtoList(analysisYear, analysisMonth);

        //算出每个综合布线和线缆类型的端口占用率和端口数
        String cablingType = "1";
        cablingStatisticsDtoList = this.setPercentToData(cablingStatisticsDtoList, cablingType);

        //当月数据
        List<CapacityCablingStatisticsDto> nowMonthList = cablingStatisticsDtoList.stream().
                filter(c -> c.getMonth().equals(analysisMonth) && c.getYear().equals(analysisYear)).collect(Collectors.toList());

        //容量楼层阈值信息
        List<CapacityThresholdInfo> capacityThresholdInfoList = this.getThresholdList(nowMonthList, analysisMonth, analysisYear, systemSettingList, ThresholdTypeEnum.PORT_TYPE);
        //计算环比
        capacityThresholdInfoList = calculateRingGrowth(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //计算同比
        capacityThresholdInfoList = calculateYearOverYear(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //计算推荐或显示数据
        List<CapacityThresholdRelatedInfo> relatedInfoList = calculateRecommendInfo(capacityThresholdInfoList, rackStatisticsDtoList);
        //计算图表显示信息
        List<CapacityThresholdRelatedViewInfo> viewInfoList = this.calculateViewInfo(analysisYear, analysisMonth, capacityThresholdInfoList, cablingStatisticsDtoList);
        //形成建议信息
        capacityThresholdInfoList = CapacityAnalyze.setAdviceToThreshold(capacityThresholdInfoList, relatedInfoList, ThresholdTypeEnum.PORT_TYPE);
        //形成运维管理活动项
        List<ManageActivity> manageActivityList = capacityAnalyze.generateManageActivity(capacityThresholdInfoList, ThresholdTypeEnum.PORT_TYPE);
        CapacityAnalyzeData capacityAnalyzeData = new CapacityAnalyzeData();
        capacityAnalyzeData.setThresholdInfoList(capacityThresholdInfoList);
        capacityAnalyzeData.setRelatedInfoList(relatedInfoList);
        capacityAnalyzeData.setRelatedViewInfoList(viewInfoList);
        capacityAnalyzeData.setManageActivityList(manageActivityList);
        return capacityAnalyzeData;
    }


    /**
     * 获取阈值信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 18:04
     * @param nowMonthList 当前数据的时间
     * @param analysisMonth 月份
     * @param analysisYear 年份
     * @param systemSettingList 系统设置
     * @param thresholdTypeEnum 阈值类型
     * @return 返回阈值信息
     */
    public List<CapacityThresholdInfo> getThresholdList(List<CapacityCablingStatisticsDto> nowMonthList, Integer analysisMonth, Integer analysisYear, List<SystemSetting> systemSettingList, ThresholdTypeEnum thresholdTypeEnum) {
        List<CapacityThresholdInfo> capacityThresholdInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(nowMonthList)) {
            for (CapacityCablingStatisticsDto capacityCablingStatisticsDto : nowMonthList) {
                //端口占用率
                double portPercent = capacityCablingStatisticsDto.getPortPercent();
                AdviceBean adviceBean = CapacityAnalyze.getAdviceType(systemSettingList, portPercent, thresholdTypeEnum);
                if (!ObjectUtils.isEmpty(adviceBean)) {
                    if (!ObjectUtils.isEmpty(adviceBean.getAdviceType())) {
                        CapacityThresholdInfo capacityThresholdInfo = this.getOneThreshold(analysisMonth, analysisYear, capacityCablingStatisticsDto, adviceBean, thresholdTypeEnum);
                        capacityThresholdInfoList.add(capacityThresholdInfo);
                    }
                }
            }
        }
        return capacityThresholdInfoList;
    }


    /**
     * 获取单个阈值
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 23:04
     * @param analysisMonth 统计月份
     * @param analysisYear  统计年份
     * @param dataStatisticsDto 数据
     * @param adviceBean 建议对象
     * @return 返回单个阈值信息
     */
    public CapacityThresholdInfo getOneThreshold(Integer analysisMonth, Integer analysisYear, CapacityCablingStatisticsDto dataStatisticsDto, AdviceBean adviceBean, ThresholdTypeEnum thresholdType) {
        CapacityThresholdInfo capacityThresholdInfo = new CapacityThresholdInfo();
        capacityThresholdInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
        capacityThresholdInfo.setMonth(analysisMonth);
        capacityThresholdInfo.setYear(analysisYear);
        capacityThresholdInfo.setType(thresholdType.getType());
        capacityThresholdInfo.setModule(ThresholdModuleEnum.GENERIC_CABLING.getModule());
        capacityThresholdInfo.setThresholdCode(NineteenUUIDUtils.uuid());
        String data = this.getThresholdData(dataStatisticsDto);
        capacityThresholdInfo.setThresholdData(data);
        String name = dataStatisticsDto.getGenericCablingType();
        name += "-";
        name += CableTypeEnum.getCableTypeByCode(dataStatisticsDto.getCableType());
        capacityThresholdInfo.setThresholdName(name);
        capacityThresholdInfo.setThresholdValue(String.valueOf(dataStatisticsDto.getPortPercent()));
        capacityThresholdInfo.setUsedNumber(dataStatisticsDto.getUsedPortNumber().intValue());
        capacityThresholdInfo.setAllNumber(dataStatisticsDto.getAllPortNumber().intValue());
        capacityThresholdInfo.setUsedCable(dataStatisticsDto.getUsedPortNumber().intValue());
        capacityThresholdInfo.setAllCable(dataStatisticsDto.getAllPortNumber().intValue());
        capacityThresholdInfo.setAdviceType(adviceBean.getAdviceType());
        capacityThresholdInfo.setCause(adviceBean.getActivityCause());
        String adviceInfo = capacityThresholdInfo.getUsedCable() + "";
        adviceInfo += "/";
        adviceInfo += capacityThresholdInfo.getAllCable();
        capacityThresholdInfo.setAdviceInfo(adviceInfo);
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, collectionTime);
        capacityThresholdInfo.setDataCollectionTime(collectionTime);
        Long nowDate = System.currentTimeMillis();
        capacityThresholdInfo.setCreateTime(nowDate);
        return capacityThresholdInfo;
    }

    /**
     * 获取阈值数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 21:44
     * @param dataStatisticsDto 数据对象
     * @return 阈值数据
     */
    public String getThresholdData(CapacityCablingStatisticsDto dataStatisticsDto) {
        String data = dataStatisticsDto.getGenericCablingType();
        data += "-";
        data += dataStatisticsDto.getCableType();
        return data;
    }

    /**
     * 设置数据中的百分比的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:16
     * @param cablingStatisticsDtoList 综合布线集合
     * @param type 类型  1 综合布线类型和线缆 2 配线架
     * @return 返回数据信息
     */
    public List<CapacityCablingStatisticsDto> setPercentToData(List<CapacityCablingStatisticsDto> cablingStatisticsDtoList, String type) {
        List<CapacityCablingStatisticsDto> dtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(cablingStatisticsDtoList)) {
            Map<String, List<CapacityCablingStatisticsDto>> dtoMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (CapacityCablingStatisticsDto capacityCablingStatisticsDto : cablingStatisticsDtoList) {
                String key = "";
                if ("1".equals(type)) {
                    //综合布线类型和配线架类型
                    key = this.getKey(capacityCablingStatisticsDto);
                } else if ("2".equals(type)) {
                    //配线架
                    key = this.getRackKey(capacityCablingStatisticsDto);
                }
                List<CapacityCablingStatisticsDto> value = new ArrayList<>();
                if (!ObjectUtils.isEmpty(dtoMap)) {
                    if (dtoMap.containsKey(key)) {
                        value = dtoMap.get(key);
                    }
                }
                value.add(capacityCablingStatisticsDto);
                dtoMap.put(key, value);
            }

            if (!ObjectUtils.isEmpty(dtoMap)) {
                for (String key : dtoMap.keySet()) {
                    List<CapacityCablingStatisticsDto> value = dtoMap.get(key);
                    if (!ObjectUtils.isEmpty(value)) {
                        Integer usedPortNumber = 0;
                        Integer unusedPortNumber = 0;
                        for (CapacityCablingStatisticsDto dto : value) {
                            if (GenericCablingStatusEnum.STATUS_ONE.getStatus().equals(dto.getStatus())) {
                                //已占用
                                usedPortNumber += dto.getStatusNumber();
                            } else if (GenericCablingStatusEnum.STATUS_THREE.getStatus().equals(dto.getStatus())) {
                                //空闲
                                unusedPortNumber += dto.getStatusNumber();
                            }
                        }
                        //总端口数量
                        Integer allPortNumber = usedPortNumber + unusedPortNumber;
                        //端口占用率
                        double percent = CalculateUtil.castPercent(usedPortNumber, allPortNumber);
                        CapacityCablingStatisticsDto info = value.get(0);
                        info.setUsedPortNumber(usedPortNumber);
                        info.setAllPortNumber(allPortNumber);
                        info.setPortPercent(percent);
                        dtoList.add(info);
                    }
                }
            }
        }
        return dtoList;
    }

    /**
     * 获取综合布线类型的key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto 综合布线对象参数
     * @return 返回综合布线类型的key
     */
    public String getCompareKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key  += capacityCablingStatisticsDto.getCableType();
        return key;
    }


    /**
     * 获取综合布线类型的key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto 综合布线对象参数
     * @return 返回综合布线类型的key
     */
    public String getKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key += capacityCablingStatisticsDto.getCableType();
        key += "-";
        key += capacityCablingStatisticsDto.getYear();
        key += "-";
        key += capacityCablingStatisticsDto.getMonth();
        return key;
    }

    /**
     * 获取综合布线类型的key
     * @author hedongwei@wistronits.com
     * @date  2019/11/9 20:17
     * @param capacityCablingStatisticsDto 综合布线对象参数
     * @return 返回综合布线类型的key
     */
    public String getRackKey(CapacityCablingStatisticsDto capacityCablingStatisticsDto) {
        String key = capacityCablingStatisticsDto.getGenericCablingType();
        key += "-";
        key += capacityCablingStatisticsDto.getCableType();
        key += "-";
        key += capacityCablingStatisticsDto.getConnectRackCode();
        return key;
    }



    /**
     * 综合布线查询当前月份配线架的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份配线架的数据
     */
    public List<CapacityCablingStatisticsDto> getSearchMonthRackList(Integer analysisYear, Integer analysisMonth) {
        GenericCablingListParameter parameter = new GenericCablingListParameter();
        parameter.setYear(analysisYear);
        parameter.setMonth(analysisMonth);
        List<CapacityCablingStatisticsDto> dtoList = genericCablingService.queryCablingGroupByStatusAndRack(parameter);
        return dtoList;
    }

    /**
     * 综合布线查询当前月份和去年同期月份的数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 18:09
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @return  当前月份和去年同期月份的数据
     */
    public List<CapacityCablingStatisticsDto> getSearchCablingStatisticsDtoList(Integer analysisYear, Integer analysisMonth) {

        CapacityAnalyzeTime capacityAnalyzeTime = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        Long nowDate = 0L;
        Long beforeYearDate = 0L;
        //当前年份月份的时间
        nowDate = DateUtil.generateCollectionTime(analysisMonth, analysisYear, nowDate);
        //去年年份月份的时间
        beforeYearDate = DateUtil.generateCollectionTime(capacityAnalyzeTime.getMonth(), capacityAnalyzeTime.getYear(), nowDate);
        GenericCablingListParameter parameter = new GenericCablingListParameter();
        parameter.setStartTime(beforeYearDate);
        parameter.setEndTime(nowDate);
        List<CapacityCablingStatisticsDto> cablingStatisticsDtoList = genericCablingService.queryCablingGroupByStatusAndDate(parameter);
        return cablingStatisticsDtoList;
    }


}
