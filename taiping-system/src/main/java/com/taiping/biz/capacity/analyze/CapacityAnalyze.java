package com.taiping.biz.capacity.analyze;

import com.taiping.bean.capacity.analyze.AdviceBean;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.entity.analyze.capacity.CapacityThresholdInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedInfo;
import com.taiping.entity.analyze.capacity.CapacityThresholdRelatedViewInfo;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.analyze.capacity.ThresholdTypeAndTemplateEnum;
import com.taiping.enums.analyze.capacity.ThresholdTypeEnum;
import com.taiping.enums.analyze.capacity.ThresholdViewTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ManageStatusEnum;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import com.taiping.utils.common.analyze.capacity.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 容量分析
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 10:49
 */
@Component
public class CapacityAnalyze {

    /**
     * 运维管理活动逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 计算同比的时间
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 减数
     * @param analysisMonth 被减数
     * @return 返回同比的时间
     */
    public static CapacityAnalyzeTime castYearOverYearTime(Integer analysisYear, Integer analysisMonth) {
        CapacityAnalyzeTime capacityAnalyzeTime = new CapacityAnalyzeTime();
        if (!ObjectUtils.isEmpty(analysisYear) && !ObjectUtils.isEmpty(analysisMonth)) {
            capacityAnalyzeTime.setYear(analysisYear - 1);
            capacityAnalyzeTime.setMonth(analysisMonth);
        }
        return capacityAnalyzeTime;
    }

    /**
     * 计算环比的时间
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param analysisYear 减数
     * @param analysisMonth 被减数
     * @return 返回环比的时间
     */
    public static CapacityAnalyzeTime castRingGrowthTime(Integer analysisYear, Integer analysisMonth) {
        CapacityAnalyzeTime capacityAnalyzeTime = new CapacityAnalyzeTime();
        if (!ObjectUtils.isEmpty(analysisYear) && !ObjectUtils.isEmpty(analysisMonth)) {
            capacityAnalyzeTime.setYear(analysisYear);
            capacityAnalyzeTime.setMonth(analysisMonth - 1);
        }
        return capacityAnalyzeTime;
    }



    /**
     * 获取预测数据
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 14:57
     * @param list 需要预测的数据集合
     * @param month 预测后面几个月
     * @param modulus 平滑系数
     * @param isPercent 百分比
     * @retutrn 获取预测数据
     */
    public static double getExpect(List<String> list, int month, double modulus, boolean isPercent) {
        if (modulus <= 0 || modulus >= 1) {
            return 0;
        }
        double modulusLeft = 1 - modulus;
        double lastIndex =  Double.valueOf(list.get(0));
        double lastSecIndex = Double.valueOf(list.get(0));
        for (String data : list) {
            double dataDouble = Double.valueOf(data);
            lastIndex = modulus * dataDouble + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        double a = 2 * lastIndex - lastSecIndex;
        double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        double count = a + b * month;
        int max = 100;
        if (isPercent && count > max) {
            count = 100;
        }
        return count;
    }


    /**
     * 计算同比环比比例信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/1 15:15
     * @param subtrahend 减数
     * @param minuend 被减数
     * @return 比例
     */
    public static double castPercentInfo(double subtrahend, double minuend, double divisor) {
        double remainder = subtrahend - minuend;
        return CalculateUtil.castPercent(remainder, divisor);
    }



    /**
     * 获取比较的值
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 10:41
     * @param viewInfo 查询阈值图的对象
     * @return 返回比较的值
     */
    public static String getCompareViewValue(CapacityThresholdRelatedViewInfo viewInfo) {
        String compareValue = viewInfo.getDataCode();
        return compareValue;
    }

    /**
     * 获取显示数据查询map
     * @author hedongwei@wistronits.com
     * @date  2019/11/8 15:29
     * @param viewInfoList 显示数据集合
     * @return 返回map
     */
    public static Map<String, List<CapacityThresholdRelatedViewInfo>> getDataViewMap(List<CapacityThresholdRelatedViewInfo> viewInfoList) {
        Map<String, List<CapacityThresholdRelatedViewInfo>> dataViewMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(viewInfoList)) {
            for (CapacityThresholdRelatedViewInfo viewInfo : viewInfoList) {
                String thresholdCode = viewInfo.getThresholdCode();
                List<CapacityThresholdRelatedViewInfo> relatedInfo = new ArrayList<>();
                if (!ObjectUtils.isEmpty(dataViewMap)) {
                    if (!ObjectUtils.isEmpty(dataViewMap.get(thresholdCode))) {
                        relatedInfo = dataViewMap.get(thresholdCode);
                    }
                }
                relatedInfo.add(viewInfo);
                dataViewMap.put(thresholdCode, relatedInfo);
            }
        }
        return dataViewMap;
    }


    /**
     * 获取关联显示信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/6 16:36
     * @param relatedInfoList 关联集合
     * @param capacityThresholdInfo 容量阈值信息
     * @param relatedInfo 关联显示信息
     * @return 关联信息
     */
    public static String getRelatedInfo(List<CapacityThresholdRelatedInfo> relatedInfoList, CapacityThresholdInfo capacityThresholdInfo, String relatedInfo) {
        if (!ObjectUtils.isEmpty(relatedInfoList)) {
            for (CapacityThresholdRelatedInfo capacityThresholdRelatedInfo : relatedInfoList) {
                if (capacityThresholdInfo.getThresholdCode().equals(capacityThresholdRelatedInfo.getThresholdCode())) {
                    if (ThresholdTypeEnum.FLOOR.getType().equals(capacityThresholdInfo.getType()) ||
                            ThresholdTypeEnum.PORT_TYPE.getType().equals(capacityThresholdInfo.getType())) {
                        String dataName = capacityThresholdRelatedInfo.getDataName();
                        double dataValue = capacityThresholdRelatedInfo.getValue();
                        List<String> templateList = new ArrayList<>();
                        templateList.add("name");
                        templateList.add("value");
                        Map<String, String> templateMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
                        templateMap.put("name" , dataName);
                        templateMap.put("value"  , dataValue + "");
                        String replaceDataName = TemplateUtil.parseTemplate(CapacityConstant.SPACE_FLOOR_DEVICE_ADVICE_INFO, templateList, templateMap);
                        if (!ObjectUtils.isEmpty(relatedInfo)) {
                            relatedInfo += "@@@@";
                        } else {
                            relatedInfo += "####";
                        }
                        relatedInfo += replaceDataName;
                    }
                }
            }
        }
        return relatedInfo;
    }


    /**
     * 生成运维管理活动项
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 17:50
     * @param capacityThresholdInfoList 阈值关联信息集合
     * @param thresholdTypeEnum 阈值类型
     * @return 运维管理活动项集合
     */
    public  List<ManageActivity> generateManageActivity(List<CapacityThresholdInfo> capacityThresholdInfoList, ThresholdTypeEnum thresholdTypeEnum) {
        List<ManageActivity> manageActivityList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                Long createTime = capacityThresholdInfo.getDataCollectionTime();
                String name = capacityThresholdInfo.getThresholdName();
                String code = capacityThresholdInfo.getThresholdData();
                ManageStatusEnum manageStatusEnum = ManageStatusEnum.UN_REDUCE;
                if (ThresholdTypeEnum.CABINET.getType().equals(thresholdTypeEnum.getType()) && ThresholdTypeEnum.PORT_TYPE.getType().equals(thresholdTypeEnum.getType())) {
                    manageStatusEnum = ManageStatusEnum.REDUCE;
                }
                ManageActivity manageOne = manageActivityService.createManageActivity(capacityThresholdInfo.getThresholdCode(), name, code, System.currentTimeMillis(),
                        capacityThresholdInfo.getAdvice(), capacityThresholdInfo.getCause(), manageStatusEnum, ManageSourceEnum.CAPACITY, createTime);
                manageActivityList.add(manageOne);
            }
        }
        return manageActivityList;
    }


    /**
     * 设置提示的消息信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 16:19
     * @param capacityThresholdInfoList 阈值信息表
     * @param relatedInfoList 阈值关联显示信息
     * @return 返回提示的消息信息
     */
    public static List<CapacityThresholdInfo> setAdviceToThreshold(List<CapacityThresholdInfo> capacityThresholdInfoList, List<CapacityThresholdRelatedInfo> relatedInfoList, ThresholdTypeEnum thresholdType) {
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                String name = capacityThresholdInfo.getThresholdName();
                String value = capacityThresholdInfo.getThresholdValue();
                String adviceType = capacityThresholdInfo.getAdviceType();
                String adviceInfo = capacityThresholdInfo.getAdviceInfo();
                String relatedInfo = "";
                String adviceName = "";
                String msg = "";
                if (!ObjectUtils.isEmpty(capacityThresholdInfo.getAdviceType())) {
                    //目前只有楼层有推荐数据
                    if (ThresholdTypeEnum.FLOOR.getType().equals(thresholdType.getType()) || ThresholdTypeEnum.PORT_TYPE.getType().equals(thresholdType.getType())) {
                        relatedInfo = CapacityAnalyze.getRelatedInfo(relatedInfoList, capacityThresholdInfo, relatedInfo);
                    }

                    List<String> templateList = new ArrayList<>();
                    templateList.add("name");
                    templateList.add("value");
                    templateList.add("relatedInfo");
                    templateList.add("msg");
                    Map<String, String> templateMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
                    templateMap.put("name" , name);
                    templateMap.put("value"  , value);
                    templateMap.put("relatedInfo"  , relatedInfo);
                    if (ThresholdTypeEnum.PORT_TYPE.getType().equals(thresholdType.getType())) {
                        msg += " " + adviceInfo + " ";
                    }
                    if (!ThresholdTypeEnum.MODULE.getType().equals(thresholdType.getType())
                            && !ThresholdTypeEnum.UPS.getType().equals(thresholdType.getType())
                            && !ThresholdTypeEnum.PDU.getType().equals(thresholdType.getType())
                            && !ThresholdTypeEnum.CABINET_COLUMN.getType().equals(thresholdType.getType())) {
                        if (CapacityConstant.ADVICE_TYPE_ADD.equals(adviceType)) {
                            msg += "建议:采购";
                        } else if (CapacityConstant.ADVICE_TYPE_DIFF.equals(adviceType)) {
                            msg += "建议:裁剪作业";
                        }
                    }
                    if (ThresholdTypeEnum.DEVICE_TYPE.getType().equals(thresholdType.getType())) {
                        msg += " " + adviceInfo;
                    }
                    templateMap.put("msg"  , msg);
                    adviceName = TemplateUtil.parseTemplate(CapacityConstant.CAPACITY_ADVICE_INFO, templateList, templateMap);
                }
                capacityThresholdInfo.setAdvice(adviceName);
            }
        }
        return capacityThresholdInfoList;
    }

    /**
     * 计算预测数据信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/4 15:46
     * @param viewMap 关联信息数据
     * @return 预测数据信息
     */
    public static List<CapacityThresholdRelatedViewInfo> calculateExpectInfo(Map<String, List<CapacityThresholdRelatedViewInfo>> viewMap) {
        //返回填充趋势后的数据
        List<CapacityThresholdRelatedViewInfo> returnViewInfoList = new ArrayList<>();
        //计算趋势信息
        if (!ObjectUtils.isEmpty(viewMap)) {
            for (String thresholdCode : viewMap.keySet()) {
                List<String> doubleList = new ArrayList<>();
                for (int i = 0; i < CapacityConstant.STATISTICS_MONTH ; i++) {
                    CapacityThresholdRelatedViewInfo viewInfo = viewMap.get(thresholdCode).get(i);
                    doubleList.add(viewInfo.getValue() + "");
                }
                //预测数据
                double modulus = 0.6;
                for (int i = 0; i < CapacityConstant.FORECAST_MONTH ; i++) {
                    boolean isPercent = true;
                    doubleList.add(CapacityAnalyze.getExpect(doubleList, i + 1 ,modulus , isPercent) + "");
                }

                //楼层map
                for (int i = 0 ; i < viewMap.get(thresholdCode).size() ; i++) {
                    //预测数据开始下标减一
                    int startIndex = CapacityConstant.STATISTICS_MONTH - 1;
                    for (int j = startIndex ; j < viewMap.get(thresholdCode).size(); j++) {
                        CapacityThresholdRelatedViewInfo viewInfoOne = viewMap.get(thresholdCode).get(j);
                        viewInfoOne.setValue(Double.valueOf(doubleList.get(j)));
                    }
                }
            }
        }

        //将预测的值生成数据信息
        for (String thresholdOne  : viewMap.keySet()) {
            returnViewInfoList.addAll(viewMap.get(thresholdOne));
        }
        return returnViewInfoList;
    }

    /**
     * 计算显示图像数据默认值
     * @author hedongwei@wistronits.com
     * @date  2019/11/2 15:41
     * @param analysisYear 统计年份
     * @param analysisMonth 统计月份
     * @param capacityThresholdInfoList 阈值信息集合
     * @return 返回显示图像数据
     */
    public static List<CapacityThresholdRelatedViewInfo> getDefaultViewData(Integer analysisYear, Integer analysisMonth,
                                                                     List<CapacityThresholdInfo> capacityThresholdInfoList) {
        //找到去年同期的数据
        CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(analysisYear, analysisMonth);
        List<CapacityThresholdRelatedViewInfo> viewInfoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(capacityThresholdInfoList)) {
            Map<String, CapacityThresholdInfo> dataMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            for (CapacityThresholdInfo capacityThresholdInfo : capacityThresholdInfoList) {
                dataMap.put(capacityThresholdInfo.getThresholdCode(), capacityThresholdInfo);
            }

            if (!ObjectUtils.isEmpty(dataMap)) {
                //设置默认值
                for (String dataOne : dataMap.keySet()) {
                    int allMonth = CapacityConstant.STATISTICS_MONTH;
                    int forecastMonth = CapacityConstant.FORECAST_MONTH;
                    int startYear = beforeYearDate.getYear();
                    int startMonth = beforeYearDate.getMonth();
                    //过滤掉同比去年的年月的数据
                    startMonth ++;
                    if (startMonth > 12) {
                        startYear ++;
                        startMonth = 1;
                    }

                    for (int i = 1; i <= allMonth + forecastMonth; i ++) {
                        CapacityThresholdRelatedViewInfo viewInfo = new CapacityThresholdRelatedViewInfo();
                        viewInfo.setThresholdRelatedViewId(NineteenUUIDUtils.uuid());
                        viewInfo.setThresholdCode(dataOne);
                        viewInfo.setDataCode(dataMap.get(dataOne).getThresholdData());
                        viewInfo.setModuleType(dataMap.get(dataOne).getModule());
                        viewInfo.setDataModule(dataMap.get(dataOne).getType());
                        Long dateTime = 0L;
                        dateTime = DateUtil.generateCollectionTime(startMonth, startYear, dateTime);
                        viewInfo.setDataTime(dateTime);
                        viewInfo.setDataModule(dataMap.get(dataOne).getType());
                        viewInfo.setModuleType(dataMap.get(dataOne).getModule());
                        if (i > allMonth) {
                            viewInfo.setViewType(ThresholdViewTypeEnum.Trend.getViewType());
                        } else {
                            viewInfo.setViewType(ThresholdViewTypeEnum.VIEW.getViewType());
                        }
                        viewInfo.setValue(0);
                        viewInfo.setYear(startYear);
                        viewInfo.setMonth(startMonth);
                        startMonth ++;
                        if (startMonth > 12) {
                            startYear ++;
                            startMonth = 1;
                        }
                        viewInfoList.add(viewInfo);
                    }
                }
            }
        }
        return viewInfoList;
    }

    /**
     * 获取提醒类型
     * @author hedongwei@wistronits.com
     * @date  2019/11/5 16:28
     * @param systemSettingList 系统集合
     * @param percent 比较的阈值信息
     * @return 获取提醒的类型
     */
    public static AdviceBean getAdviceType(List<SystemSetting> systemSettingList, double percent, ThresholdTypeEnum thresholdType) {
        AdviceBean adviceBean = new AdviceBean();
        //建议类型
        String adviceType = "";
        //运维活动原因
        String activityCause = "";
        //获取提示信息模板
        String template = ThresholdTypeAndTemplateEnum.getMsgByType(thresholdType.getType());
        //比较符号
        String operate = "";
        //比较的阈值
        double dataValue = 0 ;
        if (!ObjectUtils.isEmpty(systemSettingList)) {
            if (1 == systemSettingList.size()) {
                double value = Double.valueOf(systemSettingList.get(0).getValue().toString());
                //判断是否满足阈值条件
                if (percent >= value) {
                    dataValue = value;
                    operate = "大于等于";
                    adviceType = CapacityConstant.ADVICE_TYPE_ADD;
                }
            } else {
                for (int i = 0 ; i < systemSettingList.size(); i ++) {
                    double value = Double.valueOf(systemSettingList.get(i).getValue().toString());
                    if (i == 0) {
                        //判断是否满足阈值条件 (第一条数据)
                        if (percent <= value) {
                            dataValue = value;
                            operate = "小于等于";
                            adviceType = CapacityConstant.ADVICE_TYPE_DIFF;
                        }
                    } else if (i == systemSettingList.size() - 1) {
                        //判断是否满足阈值条件 (最后一条数据)
                        if (percent >= value) {
                            dataValue = value;
                            operate = "大于等于";
                            adviceType = CapacityConstant.ADVICE_TYPE_ADD;
                        }
                    } else {
                        //判断是否满足阈值条件 (中间的数据)
                        if (percent >= value) {
                            dataValue = value;
                            operate = "大于等于";
                            adviceType = CapacityConstant.ADVICE_TYPE_OTHER;
                        }
                    }
                }
            }
        }
        activityCause = CapacityAnalyze.parseActivityTemplate(template, operate, dataValue);
        adviceBean.setActivityCause(activityCause);
        adviceBean.setAdviceType(adviceType);
        return adviceBean;
    }

    /**
     * 解析运维管理活动模板
     * @author hedongwei@wistronits.com
     * @date  2019/11/11 15:49
     * @param template 模板
     * @param operate 操作
     * @param dataValue 值
     * @return 返回解析完的运维管理活动
     */
    public static String parseActivityTemplate(String template, String operate, double dataValue) {
        List<String> varNameList = new ArrayList<>();
        varNameList.add("operate");
        varNameList.add("value");
        Map<String, String> replaceMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        replaceMap.put("operate", operate);
        replaceMap.put("value", dataValue + "");
        return TemplateUtil.parseTemplate(template, varNameList, replaceMap);
    }

}
