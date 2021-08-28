package com.taiping.biz.energy.service.impl;

import com.google.common.collect.Lists;
import com.taiping.bean.capacity.analyze.CapacityAnalyzeTime;
import com.taiping.bean.energy.analyze.EnergyViewData;
import com.taiping.bean.energy.analyze.vo.EnergyAnalyzeReportVo;
import com.taiping.bean.energy.dto.ElectricInstrumentImportDto;
import com.taiping.bean.energy.dto.ElectricInstrumentNotPageDto;
import com.taiping.bean.energy.dto.ElectricPowerImportDto;
import com.taiping.bean.energy.dto.PowerEnergyNotPageDto;
import com.taiping.bean.energy.parameter.ElectricInstrumentListParameter;
import com.taiping.bean.energy.parameter.PowerEnergyListParameter;
import com.taiping.bean.energy.parameter.analyze.*;
import com.taiping.biz.capacity.analyze.CapacityAnalyze;
import com.taiping.biz.capacity.service.impl.CapacityServiceImpl;
import com.taiping.biz.energy.analyze.AllEnergyAnalyze;
import com.taiping.biz.energy.analyze.PowerEnergyAnalyze;
import com.taiping.biz.energy.component.EnergyComponent;
import com.taiping.biz.energy.service.ElectricInstrumentService;
import com.taiping.biz.energy.service.EnergyService;
import com.taiping.biz.energy.service.PowerEnergyItemService;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeInfoService;
import com.taiping.biz.energy.service.analyze.EnergyAnalyzeRelatedViewInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdInfoService;
import com.taiping.biz.energy.service.analyze.EnergyThresholdRelatedViewInfoService;
import com.taiping.biz.manage.service.ManageActivityService;
import com.taiping.biz.manage.service.ParamManageService;
import com.taiping.constant.capacity.CapacityConstant;
import com.taiping.constant.energy.EnergyConstant;
import com.taiping.constant.energy.EnergyResultCode;
import com.taiping.constant.energy.EnergyResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.Result;
import com.taiping.entity.ResultCode;
import com.taiping.entity.analyze.energy.EnergyAnalyzeInfo;
import com.taiping.entity.analyze.energy.EnergyAnalyzeRelatedViewInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdInfo;
import com.taiping.entity.analyze.energy.EnergyThresholdRelatedViewInfo;
import com.taiping.entity.energy.ElectricInstrument;
import com.taiping.entity.energy.PowerEnergyItem;
import com.taiping.entity.manage.ManageActivity;
import com.taiping.entity.manage.ParamManage;
import com.taiping.enums.analyze.energy.EnergyStatisticalTypeEnum;
import com.taiping.enums.cabinet.ItEnergySheetEnum;
import com.taiping.enums.energy.ElectricPowerIsChildEnum;
import com.taiping.enums.energy.EnergyTypeEnum;
import com.taiping.enums.manage.ManageSourceEnum;
import com.taiping.enums.manage.ParamTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.read.energy.ElectricExcelImportRead;
import com.taiping.read.energy.ElectricPowerExcelImportRead;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import com.taiping.utils.common.analyze.capacity.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 能耗逻辑实现类
 *
 * @author hedongwei@wistronits.com
 * @date 2019/10/14 21:00
 */
@Service
@Slf4j
public class EnergyServiceImpl implements EnergyService {

    /**
     * 自动注入电量仪表逻辑层
     */
    @Autowired
    private ElectricInstrumentService electricInstrumentService;

    /**
     * 自动注入动力能耗分项逻辑层
     */
    @Autowired
    private PowerEnergyItemService powerEnergyItemService;

    /**
     * 电量数据导入类
     */
    @Autowired
    private ElectricExcelImportRead electricExcelImportRead;

    /**
     * 电量分项导入类
     */
    @Autowired
    private ElectricPowerExcelImportRead electricPowerExcelImportRead;

    /**
     * 能耗组件
     */
    @Autowired
    private EnergyComponent energyComponent;

    /**
     * 动力能耗分析
     */
    @Autowired
    private PowerEnergyAnalyze powerEnergyAnalyze;


    /**
     * 类型参数管理服务层
     */
    @Autowired
    private ParamManageService paramManageService;


    /**
     * 运维管理活动逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;


    /**
     * 能耗分析逻辑层
     */
    @Autowired
    private EnergyThresholdInfoService energyThresholdInfoService;

    /**
     * 能耗分析图的数据
     */
    @Autowired
    private EnergyThresholdRelatedViewInfoService relatedViewInfoService;


    /**
     * 自动注入能耗分析数据信息
     */
    @Autowired
    private EnergyAnalyzeInfoService energyAnalyzeInfoService;

    /**
     * 自动注入能耗图的数据信息
     */
    @Autowired
    private EnergyAnalyzeRelatedViewInfoService analyzeViewInfoService;

    /**
     * 容量逻辑层
     */
    @Autowired
    private CapacityServiceImpl capacityService;

    /**
     * 总能耗分析
     */
    @Autowired
    private AllEnergyAnalyze allEnergyAnalyze;

    /**
     * 导入动力能耗数据
     *
     * @param file 能耗数据文件
     * @return 返回能耗数据导入结果
     * @author hedongwei@wistronits.com
     * @date 2019/10/10 16:54
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importPowerEnergyInfo(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<PowerEnergyItem> powerEnergyList = new ArrayList<>();
        List<ElectricPowerImportDto> importEnergyList = new ArrayList<>();
        Map<String, List<PowerEnergyItem>> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        try {
            stringListMap = electricPowerExcelImportRead.readExcel(file);
            //获取it电流能耗集合
            importEnergyList = (List) stringListMap.get(ItEnergySheetEnum.SHEET_ONE.getSheetName());

            //获取当前数据库最新的时间的数据
            PowerEnergyItem powerEnergyItem = powerEnergyItemService.queryTopDataByTime();
            int beforeYear = 0;
            int beforeMonth = 0;
            if (!ObjectUtils.isEmpty(powerEnergyItem)) {
                beforeYear = powerEnergyItem.getYear();
                beforeMonth = powerEnergyItem.getMonth();
            }

            //需要转换成对应的每个月份的数据
            if (!ObjectUtils.isEmpty(importEnergyList)) {
                for (ElectricPowerImportDto electricPowerImportDto : importEnergyList) {
                    map = energyComponent.setMonthData(map, electricPowerImportDto.getPowerEnergyItemList());
                }
            }

            String flag = "0";
            int dataYear = 0;
            energyComponent.getYearAndFlag(beforeYear, flag, dataYear, map);
            //获取新增动力能耗数据
            powerEnergyList = energyComponent.getIsInsertEnergyList(flag, map, dataYear, beforeMonth, EnergyTypeEnum.POWER_ENERGY);
            //批量新增动力能耗数据
            if (!ObjectUtils.isEmpty(powerEnergyList)) {
                powerEnergyItemService.insertPowerEnergyItemBatch(powerEnergyList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(EnergyResultCode.IMPORT_POWER_ITEM_ERROR, EnergyResultMsg.IMPORT_POWER_ITEM_ERROR);
        }
        return ResultUtils.success();
    }


    /**
     * 导入全部的能耗数据
     *
     * @param file 文件
     * @return 返回导入全部的能耗数据的结果
     * @author hedongwei@wistronits.com
     * @date 2019/11/12 17:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importAllEnergy(MultipartFile file) {
        Map<String, List<ExcelReadBean>> stringListMap = null;
        List<ElectricInstrument> allEnergyList = new ArrayList<>();
        List<ElectricInstrumentImportDto> importEnergyList = new ArrayList<>();
        Map<String, List<ElectricInstrument>> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        try {
            stringListMap = electricExcelImportRead.readExcel(file);
            //获取总能耗集合
            importEnergyList = (List) stringListMap.get(ItEnergySheetEnum.SHEET_ONE.getSheetName());

            //获取当前数据库最新的时间的数据
            ElectricInstrument electricInstrument = electricInstrumentService.queryTopDataByTime();
            int beforeYear = 0;
            int beforeMonth = 0;
            if (!ObjectUtils.isEmpty(electricInstrument)) {
                beforeYear = electricInstrument.getYear();
                beforeMonth = electricInstrument.getMonth();
            }
            //需要转换成对应的每个月份的数据
            if (!ObjectUtils.isEmpty(importEnergyList)) {
                for (ElectricInstrumentImportDto electricInstrumentImportDto : importEnergyList) {
                    map = energyComponent.setMonthData(map, electricInstrumentImportDto.getElectricInstrumentList());
                }
            }

            String flag = "0";
            int dataYear = 0;
            energyComponent.getYearAndFlag(beforeYear, flag, dataYear, map);
            //获取新增总能耗数据
            allEnergyList = energyComponent.getIsInsertEnergyList(flag, map, dataYear, beforeMonth, EnergyTypeEnum.ALL_ENERGY);
            //批量新增总能耗数据
            if (!ObjectUtils.isEmpty(allEnergyList)) {
                electricInstrumentService.insertElectricInstrumentBatch(allEnergyList);
            }
        } catch (Exception e) {
            log.error("file read exception", e);
            throw new BizException(EnergyResultCode.IMPORT_ALL_ENERGY_ERROR, EnergyResultMsg.IMPORT_ALL_ENERGY_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 查询总能耗数据列表不分页
     *
     * @param parameter 参数
     * @return 总能耗数据列表不分页数据
     * @author hedongwei@wistronits.com
     * @date 2019/10/29 21:47
     */
    @Override
    public Result queryAllEnergyListNotPage(ElectricInstrumentListParameter parameter) {
        List<ElectricInstrumentNotPageDto> data = electricInstrumentService.queryAllEnergyListNotPage(parameter);
        return ResultUtils.success(data);
    }

    /**
     * 查询动力能耗列表不分页
     *
     * @param parameter 参数
     * @return 返回动力能耗列表数据
     * @author hedongwei@wistronits.com
     * @date 2019/11/15 13:17
     */
    @Override
    public Result queryPowerEnergyListNotPage(PowerEnergyListParameter parameter) {
        List<PowerEnergyNotPageDto> data = powerEnergyItemService.queryPowerEnergyListNotPage(parameter);
        return ResultUtils.success(data);
    }


    /**
     * 能耗分析
     *
     * @return 返回能耗分析的结果
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 9:42
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result energyAnalysis() {
        //查询最新的动力分项能耗分析数据
        PowerEnergyItem powerEnergyItem = powerEnergyItemService.queryTopDataByTime();
        boolean isAbleAnalysis = this.checkIsAbleAnalysis(powerEnergyItem);
        //显示提示信息
        if (!isAbleAnalysis) {
            return ResultUtils.warn(EnergyResultCode.ENERGY_ANALYZE_ERROR, EnergyResultMsg.ENERGY_ANALYZE_ERROR);
        }

        Integer analysisYear = powerEnergyItem.getYear();
        Integer analysisMonth = powerEnergyItem.getMonth();

        Long dateTime = 0L;
        dateTime = DateUtil.generateCollectionTime(analysisMonth, analysisYear, dateTime);

        //调用分析方法
        paramManageService.checkAnalyze(ManageSourceEnum.ENERGY, dateTime);

        //分析动力能耗分项
        powerEnergyAnalyze.analyzeData(analysisYear, analysisMonth);

        //分析总能耗
        allEnergyAnalyze.analyzeData(analysisYear, analysisMonth);

        //分析数据完成后调用修改状态的方法
        paramManageService.updateParamManage(ManageSourceEnum.ENERGY, ParamTypeEnum.ANALYZE, dateTime);

        return ResultUtils.success(ResultCode.SUCCESS, EnergyResultMsg.ENERGY_ANALYZE_OK);
    }


    /**
     * 保存预览数据
     *
     * @return 返回能耗分析的结果
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 9:42
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result savePreviewData() {
        //查询是否能够预览数据
        paramManageService.checkPreview(ManageSourceEnum.ENERGY);
        //查询最新已经分析的数据
        ParamManage paramManage = paramManageService.queryParamManage(ManageSourceEnum.ENERGY);
        Long analyzeTime = paramManage.getNearTime();
        CapacityAnalyzeTime capacityAnalyzeTime = DateUtil.getTimeYearAndMonth(analyzeTime);
        //删除需要分析的月份的分析报告数据
        this.deleteBatch(capacityAnalyzeTime);

        //查询运维管理活动的数据
        List<ManageActivity> manageActivityList = manageActivityService.queryManageActivityForMode(ManageSourceEnum.ENERGY);

        if (!ObjectUtils.isEmpty(manageActivityList)) {
            Map<String, ManageActivity> manageActivityMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
            List<String> manageIdList = new ArrayList<>();
            for (ManageActivity manageActivityOne : manageActivityList) {
                manageActivityMap.put(manageActivityOne.getManageId(), manageActivityOne);
                manageIdList.add(manageActivityOne.getManageId());
            }

            //根据运维管理活动项id查询显示图的数据
            List<EnergyThresholdRelatedViewInfo> viewInfoList = this.getViewList(manageIdList);

            //根据运维管理活动项id查询分析的数据
            EnergyThresholdInfoListParameter parameter = new EnergyThresholdInfoListParameter();
            parameter.setThresholdCodeList(manageIdList);
            List<EnergyThresholdInfo> thresholdList = energyThresholdInfoService.queryThresholdInfoList(parameter);

            //查询父级code为运维管理活动项id的数据
            parameter = new EnergyThresholdInfoListParameter();
            parameter.setParentCodeList(manageIdList);
            List<EnergyThresholdInfo> childThresholdList = energyThresholdInfoService.queryThresholdInfoList(parameter);
            Map<String, List<EnergyThresholdInfo>> childMap = this.getChildMap(childThresholdList);

            //获取分析的数据
            List<EnergyAnalyzeInfo> energyAnalyzeList = this.getAddEnergyAnalyzeList(capacityAnalyzeTime, thresholdList, childMap, manageActivityMap, analyzeTime);

            //获取分析的图表信息
            List<EnergyAnalyzeRelatedViewInfo> energyViewList = this.getAddEnergyViewList(capacityAnalyzeTime, analyzeTime, viewInfoList);

            //新增能耗分析表的数据
            this.insertAnalyzeInfoBatch(energyAnalyzeList);

            //新增能耗分析表图显示的数据
            this.insertAnalyzeViewInfoBatch(energyViewList);

            //将状态更改为已预览
            paramManageService.updateParamManage(ManageSourceEnum.ENERGY, ParamTypeEnum.PREVIEW, null);
        }

        return ResultUtils.success(ResultCode.SUCCESS, EnergyResultMsg.SAVE_PREVIEW_DATA_OK);
    }


    /**
     * 查询能耗分析报告
     *
     * @param energyAnalyzeInfo 能耗分析参数
     * @return 返回分析报告的数据
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 11:05
     */
    @Override
    public Result queryEnergyAnalyzeReport(EnergyAnalyzeInfo energyAnalyzeInfo) {
        EnergyAnalyzeReportVo reportVo = new EnergyAnalyzeReportVo();
        //查询最新的分析报告数据
        EnergyAnalyzeInfo energyAnalyze = energyAnalyzeInfoService.queryTopDataByTime();
        if (!ObjectUtils.isEmpty(energyAnalyze)) {
            Integer year = energyAnalyze.getYear();
            Integer month = energyAnalyze.getMonth();
            //查询数据信息
            if (ObjectUtils.isEmpty(energyAnalyzeInfo)) {
                energyAnalyzeInfo = new EnergyAnalyzeInfo();
            }
            if (ObjectUtils.isEmpty(energyAnalyzeInfo.getYear())) {
                energyAnalyzeInfo.setYear(year);
            }
            if (ObjectUtils.isEmpty(energyAnalyzeInfo.getMonth())) {
                energyAnalyzeInfo.setMonth(month);
            }
            //查询统计当月的阈值信息
            EnergyAnalyzeInfoListParameter parameter = new EnergyAnalyzeInfoListParameter();
            BeanUtils.copyProperties(energyAnalyzeInfo, parameter);
            List<EnergyAnalyzeInfo> energyAnalyzeInfoList = energyAnalyzeInfoService.queryThresholdInfoList(parameter);
            if (!ObjectUtils.isEmpty(energyAnalyzeInfoList)) {
                List<EnergyAnalyzeInfo> dataList = Lists.newArrayList();
                //=============================================================
                //todo: 增加递归 拼树返回给前端
                energyAnalyzeInfoList.forEach(obj -> {
                    if (StringUtils.isEmpty(obj.getParentCode())) {
                        //这里需要拆分建议 以 '####' 拆分
                        String advice = obj.getAdvice();
                        if (advice.contains("####")) {
                            advice = advice.substring(0, advice.indexOf("####"));
                            obj.setAdvice(advice);
                        }
                        dataList.add(getChild(obj, energyAnalyzeInfoList));
                    }
                });
                reportVo.setDataList(dataList);
            }

            //页面显示的x轴数据信息
            List<String> xDataList = this.getDefaultDateList(year, month);
            if (!ObjectUtils.isEmpty(xDataList)) {
                reportVo.setXDataList(xDataList);
            }

            //页面显示的y轴数据信息
            List<List<EnergyViewData>> yDataList = new ArrayList<>();
            //根据时间查询显示的图信息
            if (!ObjectUtils.isEmpty(reportVo.getDataList())) {
                EnergyAnalyzeRelatedViewListParameter parameterInfo = new EnergyAnalyzeRelatedViewListParameter();
                parameterInfo.setAnalyzeYear(year);
                parameterInfo.setAnalyzeMonth(month);
                List<EnergyAnalyzeRelatedViewInfo> viewInfoList = analyzeViewInfoService.queryViewInfoList(parameterInfo);
                if (!ObjectUtils.isEmpty(viewInfoList)) {
                    for (EnergyAnalyzeInfo energyAnalyzeInfoOne : reportVo.getDataList()) {
                        List<EnergyViewData> viewDataList = new ArrayList<>();
                        for (EnergyAnalyzeRelatedViewInfo viewInfoOne : viewInfoList) {
                            if (energyAnalyzeInfoOne.getThresholdCode().equals(viewInfoOne.getThresholdCode())) {
                                EnergyViewData capacityViewData = new EnergyViewData();
                                capacityViewData.setName(energyAnalyzeInfoOne.getThresholdName());
                                capacityViewData.setValue(viewInfoOne.getValue());
                                viewDataList.add(capacityViewData);
                            }
                        }
                        yDataList.add(viewDataList);
                    }
                }
            }
            if (!ObjectUtils.isEmpty(yDataList)) {
                reportVo.setYDataList(yDataList);
            }
        }
        return ResultUtils.success(reportVo);
    }


    /**
     * 获取子集
     *
     * @param parentCode
     * @param allData
     * @return
     */
    private EnergyAnalyzeInfo getChild(EnergyAnalyzeInfo parentCode, List<EnergyAnalyzeInfo> allData) {
        allData.forEach(obj -> {
            if (parentCode.getThresholdCode().equals(obj.getParentCode())) {
                if (parentCode.getChildList() == null) {
                    parentCode.setChildList(new ArrayList<>());
                }
                parentCode.getChildList().add(getChild(obj, allData));
            }
        });
        return parentCode;
    }


    /**
     * 批量新增能耗统计信息
     *
     * @param energyAnalyzeList 能耗分析集合
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:53
     */
    public void insertAnalyzeInfoBatch(List<EnergyAnalyzeInfo> energyAnalyzeList) {
        if (!ObjectUtils.isEmpty(energyAnalyzeList)) {
            energyAnalyzeInfoService.insertThresholdInfoBatch(energyAnalyzeList);
        }
    }

    /**
     * 批量新增能耗图统计信息
     *
     * @param energyViewList 能耗分析图集合
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:53
     */
    public void insertAnalyzeViewInfoBatch(List<EnergyAnalyzeRelatedViewInfo> energyViewList) {
        if (!ObjectUtils.isEmpty(energyViewList)) {
            analyzeViewInfoService.insertThresholdRelatedViewInfoBatch(energyViewList);
        }
    }


    /**
     * 获取查询集合信息
     *
     * @param manageIdList 运维管理活动项id集合
     * @return 返回查询的集合信息
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:38
     */
    public List<EnergyThresholdRelatedViewInfo> getViewList(List<String> manageIdList) {
        EnergyRelatedViewListParameter parameter = new EnergyRelatedViewListParameter();
        List<EnergyThresholdRelatedViewInfo> viewInfoList = relatedViewInfoService.queryViewInfoList(parameter);
        return viewInfoList;
    }

    /**
     * 获取新增能耗图像集合
     *
     * @param capacityAnalyzeTime 查询年和月
     * @param viewInfoList        查询集合
     * @param analyzeTime         查询时间
     * @return 获取新增能耗图像集合
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:46
     */
    public List<EnergyAnalyzeRelatedViewInfo> getAddEnergyViewList(CapacityAnalyzeTime capacityAnalyzeTime, Long analyzeTime, List<EnergyThresholdRelatedViewInfo> viewInfoList) {
        List<EnergyAnalyzeRelatedViewInfo> energyAnalyzeList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(viewInfoList)) {
            for (EnergyThresholdRelatedViewInfo viewInfoOne : viewInfoList) {
                EnergyAnalyzeRelatedViewInfo analyzeRelatedViewInfo = new EnergyAnalyzeRelatedViewInfo();
                BeanUtils.copyProperties(viewInfoOne, analyzeRelatedViewInfo);
                analyzeRelatedViewInfo.setAnalyzeTime(analyzeTime);
                analyzeRelatedViewInfo.setAnalyzeMonth(capacityAnalyzeTime.getMonth());
                analyzeRelatedViewInfo.setAnalyzeYear(capacityAnalyzeTime.getYear());
                energyAnalyzeList.add(analyzeRelatedViewInfo);
            }
        }
        return energyAnalyzeList;
    }

    /**
     * 获取需要新增的能耗分析集合信息
     *
     * @param capacityAnalyzeTime 统计的时间段
     * @param thresholdList       分析的信息
     * @param childMap            分析的子项信息
     * @param manageActivityMap   运维管理活动项map
     * @param analyzeTime         分析时间
     * @return 返回需要新增的能耗分析集合信息
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:21
     */
    public List<EnergyAnalyzeInfo> getAddEnergyAnalyzeList(CapacityAnalyzeTime capacityAnalyzeTime, List<EnergyThresholdInfo> thresholdList,
                                                           Map<String, List<EnergyThresholdInfo>> childMap, Map<String, ManageActivity> manageActivityMap,
                                                           Long analyzeTime) {
        Integer year = capacityAnalyzeTime.getYear();
        Integer month = capacityAnalyzeTime.getMonth();
        List<EnergyAnalyzeInfo> energyAnalyzeList = new ArrayList<>();
        //去掉重复的子项数据
        Map<String, EnergyAnalyzeInfo> map = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(thresholdList)) {
            //动力能耗数据
            String powerReason = "";
            //it能耗数据
            String itReason = "";
            //将需要生成分析报告的数据增加运维管理活动的相关信息
            for (EnergyThresholdInfo thresholdInfo : thresholdList) {
                EnergyAnalyzeInfo analyze = new EnergyAnalyzeInfo();
                String thresholdCode = thresholdInfo.getThresholdCode();
                String isChild = thresholdInfo.getIsChild();
                String type = thresholdInfo.getType();
                String adviceName = thresholdInfo.getAdvice();
                ManageActivity manageActivityOne = manageActivityMap.get(thresholdCode);
                if (ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode().equals(isChild)) {
                    if (EnergyStatisticalTypeEnum.POWER_ENERGY.getType().equals(type)) {
                        //动力能耗
                        powerReason = manageActivityOne.getSolveInstruction();
                    } else if (EnergyStatisticalTypeEnum.IT_ENERGY.getType().equals(type)) {
                        //IT能耗
                        itReason = manageActivityOne.getSolveInstruction();
                    }
                }
                BeanUtils.copyProperties(thresholdInfo, analyze);
                analyze.setThresholdInfoId(NineteenUUIDUtils.uuid());
                analyze.setManageType(manageActivityOne.getManageType());
                analyze.setSolveInstruction(manageActivityOne.getSolveInstruction());
                analyze.setActivityType(manageActivityOne.getActivityType());
                if (childMap.containsKey(thresholdCode)) {
                    adviceName += "####";
                    String itemInfo = "";
                    for (EnergyThresholdInfo energyThresholdInfo : childMap.get(thresholdCode)) {
                        if (!ObjectUtils.isEmpty(itemInfo)) {
                            itemInfo += "@@@@";
                        }
                        itemInfo += energyThresholdInfo.getAdvice();
                        //增加子项数据
                        EnergyAnalyzeInfo analyzeChildInfo = new EnergyAnalyzeInfo();
                        BeanUtils.copyProperties(energyThresholdInfo, analyzeChildInfo);
                        analyzeChildInfo.setThresholdInfoId(NineteenUUIDUtils.uuid());
                        analyzeChildInfo.setAnalyzeMonth(month);
                        analyzeChildInfo.setAnalyzeYear(year);
                        map.put(analyzeChildInfo.getThresholdCode(), analyzeChildInfo);
                    }
                    adviceName += itemInfo;
                }
                analyze.setIsChild(ElectricPowerIsChildEnum.NOT_IS_CHILD.getCode());
                analyze.setAdvice(adviceName);
                analyze.setAnalyzeMonth(month);
                analyze.setAnalyzeYear(year);
                analyze.setAnalyzeTime(analyzeTime);
                energyAnalyzeList.add(analyze);
            }
            energyAnalyzeList = this.setAdviceToPueInfo(energyAnalyzeList, itReason, powerReason);
        }

        if (!ObjectUtils.isEmpty(map)) {
            for (String thresholdCode : map.keySet()) {
                energyAnalyzeList.add(map.get(thresholdCode));
            }
        }
        return energyAnalyzeList;
    }

    /**
     * 删除需要统计的年月的分析数据
     *
     * @param capacityAnalyzeTime 分析的时间年和月
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:08
     */
    public void deleteBatch(CapacityAnalyzeTime capacityAnalyzeTime) {
        Integer year = capacityAnalyzeTime.getYear();
        Integer month = capacityAnalyzeTime.getMonth();
        EnergyAnalyzeDeleteParameter parameter = new EnergyAnalyzeDeleteParameter();
        parameter.setAnalyzeMonth(month);
        parameter.setAnalyzeYear(year);
        //删除能耗分析表数据信息
        energyAnalyzeInfoService.deleteAnalyzeBatch(parameter);

        EnergyAnalyzeDeleteViewParameter viewParameter = new EnergyAnalyzeDeleteViewParameter();
        BeanUtils.copyProperties(parameter, viewParameter);
        //删除能耗分析图数据信息
        analyzeViewInfoService.deleteAnalyzeRelatedInfoBatch(viewParameter);
    }

    /**
     * 设置建议信息到pue信息
     *
     * @param energyAnalyzeList 能耗分析集合
     * @param itReason          it原因
     * @param powerReason       动力能耗原因
     * @return 设置建议信息到pue信息
     * @author hedongwei@wistronits.com
     * @date 2019/11/21 10:49
     */
    public List<EnergyAnalyzeInfo> setAdviceToPueInfo(List<EnergyAnalyzeInfo> energyAnalyzeList, String itReason, String powerReason) {
        for (EnergyAnalyzeInfo thresholdInfo : energyAnalyzeList) {
            String type = thresholdInfo.getType();
            if (EnergyStatisticalTypeEnum.PUE.getType().equals(type)) {
                List<String> templateList = new ArrayList<>();
                templateList.add("itReason");
                templateList.add("powerReason");
                Map<String, String> templateMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
                templateMap.put("itReason", itReason);
                templateMap.put("powerReason", powerReason);
                String adviceName = thresholdInfo.getAdvice();
                //拆分pue值的数据
                adviceName = TemplateUtil.parseTemplate(adviceName, templateList, templateMap);
                thresholdInfo.setAdvice(adviceName);
            }
        }
        return energyAnalyzeList;
    }

    /**
     * 获取子数据的map
     *
     * @param childThresholdList 子数据的集合
     * @return 子数据的map
     * @author hedongwei@wistronits.com
     * @date 2019/11/20 17:51
     */
    public Map<String, List<EnergyThresholdInfo>> getChildMap(List<EnergyThresholdInfo> childThresholdList) {
        Map<String, List<EnergyThresholdInfo>> childMap = new HashMap<>(CapacityConstant.MAP_INIT_SIZE);
        if (!ObjectUtils.isEmpty(childThresholdList)) {
            for (EnergyThresholdInfo energyThresholdInfo : childThresholdList) {
                List<EnergyThresholdInfo> thresholdInfoMapOne = new ArrayList<>();
                String code = energyThresholdInfo.getParentCode();
                if (!ObjectUtils.isEmpty(childMap)) {
                    if (childMap.containsKey(code)) {
                        thresholdInfoMapOne = childMap.get(code);
                    }
                }
                thresholdInfoMapOne.add(energyThresholdInfo);
                childMap.put(code, thresholdInfoMapOne);
            }
        }
        return childMap;
    }


    /**
     * 判断是否能够分析数据
     *
     * @param powerEnergyItem 最新的动力能耗分项信息
     * @return 返回是否能够分析数据
     * @author hedongwei@wistronits.com
     * @date 2019/11/1 14:15
     */
    public boolean checkIsAbleAnalysis(PowerEnergyItem powerEnergyItem) {
        //查询最新的总能耗信息
        ElectricInstrument electricInstrument = electricInstrumentService.queryTopDataByTime();
        boolean isAbleAnalysis = false;
        //判断每组数据的时间是否都在一个月上
        if (!ObjectUtils.isEmpty(powerEnergyItem) && !ObjectUtils.isEmpty(electricInstrument)) {
            Integer powerYear = powerEnergyItem.getYear();
            Integer powerMonth = powerEnergyItem.getMonth();
            Integer allYear = electricInstrument.getYear();
            Integer allMonth = electricInstrument.getMonth();

            if (powerYear.equals(allYear) && powerMonth.equals(allMonth)) {
                isAbleAnalysis = true;
            }
        }
        return isAbleAnalysis;
    }


    /**
     * 显示默认的统计的时间段
     * @author hedongwei@wistronits.com
     * @date  2019/11/3 22:45
     * @param year 年份
     * @param month 月份
     * @return 获取默认时间段
     */
    public List<String> getDefaultDateList(Integer year, Integer month) {
        List<String> xDataList = new ArrayList<>();
        //找到去年同期的数据
        CapacityAnalyzeTime beforeYearDate = CapacityAnalyze.castYearOverYearTime(year, month);
        int startYear = beforeYearDate.getYear();
        int startMonth = beforeYearDate.getMonth();

        int allEnergyMonth = EnergyConstant.STATISTICS_MONTH;
        int forecastEnergyMonth = EnergyConstant.FORECAST_MONTH;
        for (int i = 1; i <= allEnergyMonth + forecastEnergyMonth; i ++) {
            xDataList.add(startYear + "-" + startMonth);
            startMonth ++;
            if (startMonth > 12) {
                startYear ++;
                startMonth = 1;
            }
        }
        return xDataList;
    }
}
