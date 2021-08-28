package com.taiping.biz.operation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.taiping.bean.energy.dto.BigScreenDto;
import com.taiping.bean.operation.OperationPowerBean;
import com.taiping.biz.budget.dao.IBudgetDao;
import com.taiping.biz.budget.dto.BudgetTotalDto;
import com.taiping.biz.energy.dao.ElectricInstrumentDao;
import com.taiping.biz.operation.dao.CommunicateIssuesDao;
import com.taiping.biz.operation.dao.HealthyDao;
import com.taiping.biz.operation.dao.OperationDao;
import com.taiping.biz.operation.service.OperationAnalysisService;
import com.taiping.biz.problem.dao.TroubleTicketDao;
import com.taiping.biz.system.service.SystemService;
import com.taiping.constant.operation.OperationConstant;
import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.PageBean;
import com.taiping.entity.QueryCondition;
import com.taiping.entity.Result;
import com.taiping.entity.operation.ChaiFa;
import com.taiping.entity.operation.CommunicateIssues;
import com.taiping.entity.operation.DistHighCabinet;
import com.taiping.entity.operation.DistLowCabinet;
import com.taiping.entity.operation.FireReservoir;
import com.taiping.entity.operation.HealthyCard;
import com.taiping.entity.operation.HealthyParam;
import com.taiping.entity.operation.Transformer;
import com.taiping.entity.operation.WaterCooledUnit;
import com.taiping.entity.problem.TroubleTicket;
import com.taiping.entity.system.SystemSetting;
import com.taiping.enums.operation.HealthyCardEnum;
import com.taiping.enums.operation.HealthyParamEnum;
import com.taiping.enums.operation.OperationParamEnum;
import com.taiping.enums.operation.PowerSheetNameEnum;
import com.taiping.exception.BizException;
import com.taiping.read.operation.PowerHealthyExcelImportRead;
import com.taiping.utils.MpQueryHelper;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import com.taiping.utils.common.PercentageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * 运行情况分析服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
@Service
@Slf4j
public class OperationAnalysisServiceImpl implements OperationAnalysisService {

    @Autowired
    private HealthyDao healthyDao;
    @Autowired
    private OperationDao operationDao;
    @Autowired
    private ElectricInstrumentDao instrumentDao;
    @Autowired
    private IBudgetDao iBudgetDao;
    @Autowired
    private TroubleTicketDao troubleTicketDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private CommunicateIssuesDao communicateIssuesDao;


    @Autowired
    private PowerHealthyExcelImportRead importRead;

    /**
     * 查询健康卡
     *
     * @return Result
     */
    @Override
    public Result queryHealthyParam() {
        List<HealthyCard> healthyCardList = new ArrayList<>();
        double standardRuntime = OperationParamEnum.WATER_COOLED_UNIT.getValue();
        double standardWater = OperationParamEnum.FIRE_RESERVOIR.getValue();
        double standardSpace = OperationParamEnum.CABINET_SPACE.getValue();
        double standardPower = OperationParamEnum.CABINET_POWER.getValue();
        double standardEnergy = OperationParamEnum.ENERGY.getValue();
        double standardRate = OperationParamEnum.BUDGET.getValue();
        //获取系统参数设置
        List<SystemSetting> systemSettingList = systemService.querySystemValueByCode(
                OperationParamEnum.HEALTHY.getCode());
        for (SystemSetting systemSetting : systemSettingList) {
            Double value = (Double) systemSetting.getValue();
            if (value == null) {
                continue;
            }
            String code = systemSetting.getCode();
            if (OperationParamEnum.WATER_COOLED_UNIT.getCode().equals(code)) {
                standardRuntime = value;
            } else if (OperationParamEnum.FIRE_RESERVOIR.getCode().equals(code)) {
                standardWater = value;
            } else if (OperationParamEnum.CABINET_SPACE.getCode().equals(code)) {
                standardSpace = value;
            } else if (OperationParamEnum.CABINET_POWER.getCode().equals(code)) {
                standardPower = value;
            } else if (OperationParamEnum.ENERGY.getCode().equals(code)) {
                standardEnergy = value;
            } else if (OperationParamEnum.BUDGET.getCode().equals(code)) {
                standardRate = value;
            }
        }
        //计算水冷机组运行时长、消防蓄水池健康
        getHealthy(healthyCardList, standardRuntime, standardWater);
        //计算空间容量健康
        getSpaceHealthy(healthyCardList, standardSpace);
        //计算电力容量健康
        getPowerHealthy(healthyCardList, standardPower);
        //计算能耗pue健康
        getEnergyHealthy(healthyCardList, standardEnergy);
        //计算预采购健康
        getBudgetHealthy(healthyCardList, standardRate);
        //计算运行情况
        getTroubleHealthy(healthyCardList);
        return ResultUtils.success(healthyCardList);
    }

    /**
     * 运行情况 物业沟通会-配电系统
     * 4个sheet
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importPowerData(MultipartFile file) {
        try {
            //校验 是否4个sheet 已经导入过了
            Integer monthValue = LocalDate.now().getMonthValue();
            Integer yearValue = LocalDate.now().getYear();
            Integer allCount = operationDao.checkAllImportStatus(monthValue, yearValue);
            if (allCount != null && allCount == 4) {
                throw new BizException(OperationResultCode.POWER_DATA_EXIST,
                        OperationResultMsg.POWER_DATA_EXIST);
            }

            Map<String, List<ExcelReadBean>> listMap = importRead.readPowerExcel(file);
            //迭代 返回的数据
            if (listMap == null) {
                // 导入数据为空
                throw new BizException(OperationResultCode.FILE_TYPE_ERROR,
                        OperationResultMsg.FILE_EMPTY);
            }
            listMap.keySet().forEach(obj -> {
                handlerPowerData(obj, listMap.get(obj), monthValue, yearValue);
            });

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            //文件格式错误e
            log.error("文件({})格式错误:{}", file.getOriginalFilename(), e);
            throw new BizException(OperationResultCode.FILE_TYPE_ERROR,
                    OperationResultMsg.FILE_TYPE_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 查询运行情况-配电系统
     *
     * @param month 月份
     * @param year  年份
     * @return Result
     */
    @Override
    public Result queryOperationPower(Integer month, Integer year) {
        // 查询4个sheet 页中的数据
        List<OperationPowerBean> lists = Lists.newArrayList();

        // 高压柜
        lists.add(handlerDistHighData(month, year));
        // 低压柜
        lists.add(handlerDistLowData(month, year));
        // 柴发数据
        lists.add(handlerChaiFaData(month, year));
        // 变压器
        lists.add(handlerTransData(month, year));
        return ResultUtils.success(lists);
    }

    /**
     * 查询事项沟通
     *
     * @param queryCondition
     * @return
     */
    @Override
    public Result queryCommunicateData(QueryCondition<CommunicateIssues> queryCondition) {
        //处理构建查询条件
        Page page = MpQueryHelper.structureQueryCondition(queryCondition, "planTime", "desc");
        //查询
        List<CommunicateIssues> troubleTicketList = communicateIssuesDao.selectCommunicateIssuesList(queryCondition.getPageCondition(),
                queryCondition.getFilterConditions(), queryCondition.getSortCondition());
        //查询总条数
        Integer count = communicateIssuesDao.selectCommunicateIssuesListCount(queryCondition.getFilterConditions());
        //返回
        PageBean pageBean = MpQueryHelper.myBatiesBuildPageBean(page, count, troubleTicketList);
        return ResultUtils.pageSuccess(pageBean);
    }

    /**
     * 新增沟通事项
     *
     * @param communicateIssues
     * @return
     */
    @Override
    public Result saveCommunicateData(CommunicateIssues communicateIssues) {
        if (communicateIssues == null) {
            return ResultUtils.warn(1000000, "沟通事项参数不能为空！");
        }
        communicateIssues.setIssuesId(NineteenUUIDUtils.uuid());
        communicateIssuesDao.insertCommunicateIssues(communicateIssues);
        return ResultUtils.success();
    }

    /**
     * 删除沟通事项
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteCommunicateById(List<String> id) {

        communicateIssuesDao.deleteBatchCommunicateIssues(id);
        return ResultUtils.success();
    }

    /**
     * 修改沟通事项
     *
     * @param communicateIssues
     * @return
     */
    @Override
    public Result updateCommunicateIssues(CommunicateIssues communicateIssues) {
        if (communicateIssues == null) {
            return ResultUtils.warn(1000000, "沟通事项参数不能为空！");
        }
        communicateIssuesDao.updateCommunicateIssues(communicateIssues);
        return ResultUtils.success();
    }

    /**
     * 根据id 查询重要事项沟通
     *
     * @param issuesId
     * @return
     */
    @Override
    public Result queryCommunicateIssuesById(String issuesId) {
        CommunicateIssues communicateIssues = communicateIssuesDao.selectCommunicateIssuesById(issuesId);

        return ResultUtils.success(communicateIssues);
    }

    /**
     * 处理高压柜数据
     *
     * @param month
     * @param year
     * @return
     */
    @Async
    public OperationPowerBean handlerDistHighData(Integer month, Integer year) {
        OperationPowerBean powerBean = new OperationPowerBean();
        powerBean.setSheetType(PowerSheetNameEnum.DIST_HIGH_CABINET.ordinal());
        // 查询数据
        List<DistHighCabinet> highCabinets = operationDao.queryDistHighData(month, year);
        if (highCabinets == null) {
            return powerBean;
        }
        powerBean.setData(highCabinets);

        Integer countException = 0;
        List<String> details = Lists.newArrayList();
        // 遍历数据
        for (DistHighCabinet bean : highCabinets) {
            int counts = 0;
            // 状态项 为1 则未启动 跳过 不监测
            if (bean.getState() == 1) {

                continue;
            }
            StringBuffer allExceptionDetail = new StringBuffer();
            // 柜体是否有异响及异味
            StringBuffer sb = new StringBuffer();
            counts = counts + checkIntegerValueNull(bean.getCabinetSmellStatus(), "柜体有异响及异味", sb);

            // 柜面仪表指示灯是否正常
            counts = counts + checkIntegerValueNull(bean.getCabinetLampStatus(), "柜面仪表指示灯异常", sb);

            // 范围值监测  电压 电流 A路/B路 直流屏电池电压（V）
            counts = counts + checkDoubleValueRange(bean.getVoltageL1(), bean.getMaxVoltage(), bean.getMinVoltage(), "L1# 电压异常", sb);
            counts = counts + checkDoubleValueRange(bean.getVoltageL2(), bean.getMaxVoltage(), bean.getMinVoltage(), "L2# 电压异常", sb);
            counts = counts + checkDoubleValueRange(bean.getVoltageL3(), bean.getMaxVoltage(), bean.getMinVoltage(), "L3# 电压异常", sb);

            counts = counts + checkDoubleValueRange(bean.getElectricL1(), bean.getMaxElectric(), bean.getMinElectric(), "L1# 电流异常", sb);
            counts = counts + checkDoubleValueRange(bean.getElectricL2(), bean.getMaxElectric(), bean.getMinElectric(), "L2# 电流异常", sb);
            counts = counts + checkDoubleValueRange(bean.getElectricL3(), bean.getMaxElectric(), bean.getMinElectric(), "L3# 电流异常", sb);

            counts = counts + checkDoubleValueRange(bean.getDcPanelVoltageA(), bean.getMaxDcVoltage(), bean.getMinDcVoltage(), "A路直流屏电池电压 异常", sb);
            counts = counts + checkDoubleValueRange(bean.getDcPanelVoltageB(), bean.getMaxDcVoltage(), bean.getMinDcVoltage(), "B路直流屏电池电压 异常", sb);

            // 最终进行判断 总计
            if (counts != 0) {
                //含有超出范围的数据 最后加上名称 已 @@@区别
                allExceptionDetail.append(OperationConstant.OPER_EXCEPTION_NAME).append(bean.getLineName())
                        .append("-").append(bean.getPowerNum()).append("-")
                        .append(bean.getName()).append(sb);
                details.add(allExceptionDetail.toString());
                countException++;
            }
        }

        // 统计柴发数据中是否含有超出数据
        String distHighDetail = null;
        if (countException == 0) {
            distHighDetail = "高压柜设备运行良好";
            powerBean.setExceptionStatus(false);
        } else {
            distHighDetail = "高压柜设备运行状态有异常:具体如下";
            powerBean.setExceptionStatus(true);
        }
        powerBean.setTitle(distHighDetail);
        powerBean.setDetail(details);


        return powerBean;
    }

    /**
     * 处理低压柜数据
     *
     * @param month
     * @param year
     * @return
     */
    @Async
    public OperationPowerBean handlerDistLowData(Integer month, Integer year) {
        OperationPowerBean powerBean = new OperationPowerBean();
        powerBean.setSheetType(PowerSheetNameEnum.DIST_LOW_CABINET.ordinal());

        // 查询数据
        List<DistLowCabinet> lowCabinets = operationDao.queryDistLowData(month, year);
        if (lowCabinets == null) {
            return powerBean;
        }
        powerBean.setData(lowCabinets);

        Integer countException = 0;
        List<String> details = Lists.newArrayList();
        // 遍历数据
        for (DistLowCabinet bean : lowCabinets) {
            int counts = 0;
            StringBuffer allExceptionDetail = new StringBuffer();
            // 柜体是否有异响及异味
            StringBuffer sb = new StringBuffer();
            counts = counts + checkIntegerValueNull(bean.getCabinetSmellStatus(), "柜体有异响及异味", sb);
            // 柜面仪表指示灯是否正常
            counts = counts + checkIntegerValueNull(bean.getCabinetLampStatus(), "柜面仪表指示灯异常", sb);
            counts = counts + checkIntegerValueNull(bean.getSwitchingState(), "开关状态异常", sb);
            counts = counts + checkIntegerValueNull(bean.getLineTemperatureStatus(), "接线端子温度异常", sb);
            counts = counts + checkIntegerValueNull(bean.getEarthingStatus(), "接地异常", sb);
            counts = counts + checkIntegerValueNull(bean.getCabinetDoorStatus(), "配电柜门未关闭", sb);
            counts = counts + checkIntegerValueNull(bean.getEnvClearStatus(), "环境不整洁", sb);


            // 范围值监测  环境温度
            counts = counts + checkDoubleValueRange(bean.getEnvTemperature(), bean.getMaxEnvTemperature(), bean.getMinEnvTemperature(), "环境温度异常", sb);

            // 最终进行判断 总计
            if (counts != 0) {
                //含有超出范围的数据 最后加上名称 已 @@@区别
                allExceptionDetail.append(OperationConstant.OPER_EXCEPTION_NAME).append(bean.getPosition())
                        .append(",")
                        .append(bean.getPowerName()).append(sb);
                details.add(allExceptionDetail.toString());
                countException++;
            }


        }

        // 统计柴发数据中是否含有超出数据
        String distLowDetail = null;
        if (countException == 0) {
            distLowDetail = "低压柜设备运行良好";
            powerBean.setExceptionStatus(false);
        } else {
            distLowDetail = "低压柜设备运行状态有异常:具体如下";
            powerBean.setExceptionStatus(true);
        }
        powerBean.setTitle(distLowDetail);
        powerBean.setDetail(details);

        return powerBean;
    }

    /**
     * 处理变压器数据
     *
     * @param month
     * @param year
     * @return
     */
    @Async
    public OperationPowerBean handlerTransData(Integer month, Integer year) {
        OperationPowerBean powerBean = new OperationPowerBean();
        powerBean.setSheetType(PowerSheetNameEnum.TRANSFORMER.ordinal());

        // 查询数据
        List<Transformer> transformers = operationDao.queryTransFormerData(month, year);
        if (transformers == null) {
            return powerBean;
        }
        powerBean.setData(transformers);

        Integer countException = 0;
        List<String> details = Lists.newArrayList();
        // 遍历数据
        for (Transformer bean : transformers) {

            int counts = 0;
            StringBuffer allExceptionDetail = new StringBuffer();
            // 柜体是否有异响及异味
            StringBuffer sb = new StringBuffer();
            counts = counts + checkIntegerValueNull(bean.getCabinetSmellStatus(), "柜体有异响及异味", sb);

            // 柜面仪表指示灯是否正常
            counts = counts + checkIntegerValueNull(bean.getFanStatus(), "风机状态异常", sb);


            // 范围值监测  电压 电流 A路/B路 直流屏电池电压（V）
            counts = counts + checkDoubleValueRange(bean.getVoltageL1(), bean.getMaxVoltage(), bean.getMinVoltage(), "L1# 电压异常", sb);
            counts = counts + checkDoubleValueRange(bean.getVoltageL2(), bean.getMaxVoltage(), bean.getMinVoltage(), "L2# 电压异常", sb);
            counts = counts + checkDoubleValueRange(bean.getVoltageL3(), bean.getMaxVoltage(), bean.getMinVoltage(), "L3# 电压异常", sb);

            counts = counts + checkDoubleValueRange(bean.getElectricL1(), bean.getMaxElectric(), bean.getMinElectric(), "L1# 电流异常", sb);
            counts = counts + checkDoubleValueRange(bean.getElectricL2(), bean.getMaxElectric(), bean.getMinElectric(), "L2# 电流异常", sb);
            counts = counts + checkDoubleValueRange(bean.getElectricL3(), bean.getMaxElectric(), bean.getMinElectric(), "L3# 电流异常", sb);

            counts = counts + checkDoubleValueRange(bean.getCoreTemperatureA(), bean.getMaxCoreTemperatureOne(), bean.getMinCoreTemperatureOne(), "铁芯A温度 异常", sb);
            counts = counts + checkDoubleValueRange(bean.getCoreTemperatureB(), bean.getMaxCoreTemperatureOne(), bean.getMinCoreTemperatureOne(), "铁芯B温度 异常", sb);
            counts = counts + checkDoubleValueRange(bean.getCoreTemperatureC(), bean.getMaxCoreTemperatureOne(), bean.getMinCoreTemperatureOne(), "铁芯C温度 异常", sb);
            counts = counts + checkDoubleValueRange(bean.getCoreTemperatureD(), bean.getMaxCoreTemperatureTwo(), bean.getMinCoreTemperatureTwo(), "铁芯D温度 异常", sb);

            // 最终进行判断 总计
            if (counts != 0) {
                //含有超出范围的数据 最后加上名称 已 @@@区别
                allExceptionDetail.append(OperationConstant.OPER_EXCEPTION_NAME).append(bean.getTransformerName())
                        .append(sb);
                details.add(allExceptionDetail.toString());
                countException++;
            }

        }

        // 统计柴发数据中是否含有超出数据
        String transDetail = null;
        if (countException == 0) {
            transDetail = "变压器运行良好";
            powerBean.setExceptionStatus(false);
        } else {
            transDetail = "变压器运行状态有异常:具体如下";
            powerBean.setExceptionStatus(true);
        }
        powerBean.setTitle(transDetail);
        powerBean.setDetail(details);


        return powerBean;
    }


    /**
     * 柴发数据处理
     *
     * @param month
     * @param year
     */
    @Async
    public OperationPowerBean handlerChaiFaData(Integer month, Integer year) {
        OperationPowerBean operationPowerBean = new OperationPowerBean();
        operationPowerBean.setSheetType(PowerSheetNameEnum.CHAI_FA.ordinal());
        /**
         * 2个处理项 数据和超过范围值的记录下来
         *  冷却液水位
         *  水温
         *  机油是否正常
         *  供油管路
         *  蓄电池电压
         *  电池内阻
         *  日用油箱 油位
         *  室外油罐油位
         *  剩余柴油负载
         *  柴发控制状态
         *  柴发测试电压
         *  柴发测试电流
         *  供油油泵控制柜
         *  记录个数
         */
        List<ChaiFa> chaiFas = operationDao.queryChaiFaData(month, year);
        if (chaiFas == null) {
            return operationPowerBean;
        }
        operationPowerBean.setData(chaiFas);
        //柴发异常总数
        Integer countException = 0;
        List<String> details = Lists.newArrayList();
        for (ChaiFa obj : chaiFas) {
            int counts = 0;
            StringBuffer allExceptionDetail = new StringBuffer();
            StringBuffer sb = new StringBuffer();
            counts = counts + checkIntegerValueNull(obj.getCoolantLevelStatus(), "冷却液水位异常", sb);
            counts = counts + checkIntegerValueNull(obj.getOilLevelStatus(), "机油异常异常", sb);
            counts = counts + checkIntegerValueNull(obj.getOilSupplyLineStatus(), "供油管路异常", sb);

            counts = counts + checkIntegerValueNull(obj.getChaiFaControllerStatus(), "柴发控制柜状态异常", sb);
            counts = counts + checkIntegerValueNull(obj.getChaiFaTestVolStatus(), "柴发测试电压异常", sb);
            counts = counts + checkIntegerValueNull(obj.getChaiFaTestEleStatus(), "柴发测试电流异常", sb);
            counts = counts + checkIntegerValueNull(obj.getFuelFeediControllerStatus(), "供油油泵控制柜异常", sb);

            // 范围值 判断

            // 水温
            counts = counts + checkDoubleValueRange(obj.getWaterTemperature(), obj.getMaxWaterTemperature(), obj.getMinWaterTemperature(), "水温异常", sb);

            //蓄电池电压
            counts = counts + checkDoubleValueRange(obj.getBatteryVoltageA1(), obj.getMaxBatteryVoltage(), obj.getMinBatteryVoltage(), "A1# 蓄电池电压异常", sb);
            counts = counts + checkDoubleValueRange(obj.getBatteryVoltageA2(), obj.getMaxBatteryVoltage(), obj.getMinBatteryVoltage(), "A2# 蓄电池电压异常", sb);
            counts = counts + checkDoubleValueRange(obj.getBatteryVoltageB1(), obj.getMaxBatteryVoltage(), obj.getMinBatteryVoltage(), "B1# 蓄电池电压异常", sb);
            counts = counts + checkDoubleValueRange(obj.getBatteryVoltageB2(), obj.getMaxBatteryVoltage(), obj.getMinBatteryVoltage(), "B2# 蓄电池电压异常", sb);

            //蓄电池电阻异常
            counts = counts + checkDoubleValueRange(obj.getResistanceA1(), obj.getMaxResistance(), obj.getMinResistance(), "A1# 蓄电池电阻异常", sb);
            counts = counts + checkDoubleValueRange(obj.getResistanceA2(), obj.getMaxResistance(), obj.getMinResistance(), "A2# 蓄电池电阻异常", sb);
            counts = counts + checkDoubleValueRange(obj.getResistanceB1(), obj.getMaxResistance(), obj.getMinResistance(), "B1# 蓄电池电阻异常", sb);
            counts = counts + checkDoubleValueRange(obj.getResistanceB2(), obj.getMaxResistance(), obj.getMinResistance(), "B2# 蓄电池电阻异常", sb);

            //日用油箱油位异常
            counts = counts + checkDoubleValueRange(obj.getOilLevelDaily(), obj.getMaxOilLevelDaily(), obj.getMinOilLevelDaily(), "日用油箱油位异常", sb);

            //室外油罐油位异常
            counts = counts + checkDoubleValueRange(obj.getOilLevelOutdoorOne(), obj.getMaxOilLevelOutdoor(), obj.getMinOilLevelOutdoor(), "室外油罐1油位异常", sb);
            counts = counts + checkDoubleValueRange(obj.getOilLevelOutdoorTwo(), obj.getMaxOilLevelOutdoor(), obj.getMinOilLevelOutdoor(), "室外油罐2油位异常", sb);


            //当前负载剩余柴油可用时长不足
            counts = counts + checkDoubleValueRange(obj.getCurrLoadResidueOilTime(), obj.getMaxLoadResidueTime(), obj.getMinLoadResidueTime(), "当前负载剩余柴油可用时长不足", sb);

            //剩余柴油满载可用时长不足
            counts = counts + checkDoubleValueRange(obj.getResidueFullLoadOilTime(), obj.getMaxLoadResidueTime(), obj.getMinLoadResidueTime(), "剩余柴油满载可用时长不足", sb);

            // 最终进行判断 总计
            if (counts != 0) {
                //含有超出范围的数据 最后加上名称 已 @@@区别
                allExceptionDetail.append(OperationConstant.OPER_EXCEPTION_NAME).append(obj.getChaiFaName()).append(sb);
                details.add(allExceptionDetail.toString());
                countException++;
            }
        }
        // 统计柴发数据中是否含有超出数据
        String chaiDetail = null;
        if (countException == 0) {
            chaiDetail = "柴发设备运行良好";
            operationPowerBean.setExceptionStatus(false);
        } else {
            chaiDetail = "柴发设备运行状态有异常:具体如下";
            operationPowerBean.setExceptionStatus(true);
        }
        operationPowerBean.setTitle(chaiDetail);
        operationPowerBean.setDetail(details);

        return operationPowerBean;
    }

    /**
     * 检测状态值 是否选中  0 异常 1正常
     *
     * @param value
     * @return
     */
    private int checkIntegerValueNull(Integer value, String exceptionMsg, StringBuffer sb) {
        if (value == null || value == 0) {
            sb.append(OperationConstant.OPER_EXCEPTION_LINE).append(exceptionMsg);
            return 1;
        }
        return 0;
    }

    /**
     * 范围值检测 是否包含在内
     *
     * @param value
     * @param maxValue
     * @param minValue
     * @return
     */
    private int checkDoubleValueRange(Double value, Double maxValue, Double minValue, String exceptionMsg, StringBuffer sb) {
        if (maxValue == null || minValue == null || value == null) {
            // 没有范围值比较  不计入统计范围
            return 0;
        }
        if (value > maxValue || value < minValue) {
            sb.append(OperationConstant.OPER_EXCEPTION_LINE).append(exceptionMsg);
            return 1;
        }

        return 0;
    }


    /**
     * 处理数据
     *
     * @param sheetName
     * @param data
     */
    private void handlerPowerData(String sheetName, List<ExcelReadBean> data, Integer monthValue, Integer yearValue) {
        Integer sheetType;
        switch (sheetName) {
            case OperationConstant.OPER_SHEET_NAME_CHAI_FA:
                // 柴发数据转换
                List<ChaiFa> chaiFas = (List) data;
                operationDao.deleteChaiFaByCondition(monthValue, yearValue);
                operationDao.saveImportChaiFa(chaiFas);
                sheetType = PowerSheetNameEnum.CHAI_FA.ordinal();
                break;
            case OperationConstant.OPER_SHEET_NAME_TRANS:
                // 变压器
                List<Transformer> transformers = (List) data;
                operationDao.deleteTransByCondition(monthValue, yearValue);
                operationDao.saveImportTransformer(transformers);
                sheetType = PowerSheetNameEnum.TRANSFORMER.ordinal();
                break;
            case OperationConstant.OPER_SHEET_NAME_DIST_LOW:
                // 高压柜
                List<DistLowCabinet> distLowCabinets = (List) data;
                operationDao.deleteDistLowByCondition(monthValue, yearValue);
                operationDao.saveImportDistLowCabinet(distLowCabinets);
                sheetType = PowerSheetNameEnum.DIST_LOW_CABINET.ordinal();
                break;
            case OperationConstant.OPER_SHEET_NAME_DIST_HIGH:
                // 低压柜
                List<DistHighCabinet> distHighCabinetList = (List) data;
                operationDao.deleteDistHighByCondition(monthValue, yearValue);
                operationDao.saveImportDistHighCabinet(distHighCabinetList);
                sheetType = PowerSheetNameEnum.DIST_HIGH_CABINET.ordinal();
                break;
            default:
                throw new BizException(OperationResultCode.FILE_TYPE_ERROR,
                        OperationResultMsg.NOT_FIND_SHHET_NAME);
        }
        //检测是否含有对应的数据 并插入数据
        Integer checkStatus = operationDao.checkImportExcel(sheetType, monthValue, yearValue);
        if (checkStatus == null || checkStatus == 0) {
            operationDao.saveImportExcel(NineteenUUIDUtils.uuid(), sheetType, monthValue, yearValue);
        }
    }


    /**
     * 计算运行情况
     *
     * @param healthyCardList 健康卡
     */
    private void getTroubleHealthy(List<HealthyCard> healthyCardList) {
        //运行情况
        HealthyCard troubleCard = new HealthyCard();
        troubleCard.setCardId(HealthyCardEnum.SERVICE_AVAILABILITY.getType());
        troubleCard.setHealthyPercentage(PercentageUtil.HUNDRED_PERCENT);
        //查询故障单最早时间，服务中断总时长
        TroubleTicket troubleTicket = troubleTicketDao.queryTroubleTicketInterrupt();
        if (troubleTicket == null) {
            troubleCard.setHealthy(true);
            troubleCard.setActualPercentage(PercentageUtil.HUNDRED_PERCENT);
        } else {
            Double interruptDuration = troubleTicket.getInterruptDuration();
            if (interruptDuration == null || interruptDuration == 0) {
                troubleCard.setHealthy(true);
                troubleCard.setActualPercentage(PercentageUtil.HUNDRED_PERCENT);
            } else {
                troubleCard.setHealthy(false);
                double time = System.currentTimeMillis() - troubleTicket.getTroubleTime();
                double service = (time - interruptDuration) / time;
                troubleCard.setActualPercentage(PercentageUtil.changeDoubleToString(service));
            }
        }
        healthyCardList.add(troubleCard);
    }

    /**
     * 计算预采购健康
     *
     * @param healthyCardList 健康卡
     * @param standardRate    预采购进度标准
     */
    private void getBudgetHealthy(List<HealthyCard> healthyCardList, double standardRate) {
        //查询预采购进度
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        BudgetTotalDto budgetTotal = iBudgetDao.getBudgetTotal(String.valueOf(year));
        //预采购
        HealthyCard budgetCard = new HealthyCard();
        budgetCard.setCardId(HealthyCardEnum.BUDGET.getType());
        if (budgetTotal == null || budgetTotal.getRatio() == null) {
            budgetCard.setHealthy(true);
        } else {
            double ratio = budgetTotal.getRatio() * 100;
            budgetCard.setHealthy(ratio >= standardRate);
        }
        healthyCardList.add(budgetCard);
    }

    /**
     * 计算能耗pue健康
     *
     * @param healthyCardList 健康卡
     * @param standardEnergy  能耗pue标准
     */
    private void getEnergyHealthy(List<HealthyCard> healthyCardList, double standardEnergy) {
        //查询能耗pue
        BigScreenDto energy = instrumentDao.getBidScreenByPueValue();
        //能耗pue
        HealthyCard energyCard = new HealthyCard();
        energyCard.setCardId(HealthyCardEnum.ENERGY.getType());
        if (energy == null) {
            energyCard.setHealthy(true);
        } else {
            double energyRate = energy.getPueValue();
            energyCard.setHealthy(energyRate <= standardEnergy);
        }
        healthyCardList.add(energyCard);
    }

    /**
     * 计算电力容量健康
     *
     * @param healthyCardList 健康卡
     * @param standardPower   电力容量标准
     */
    private void getPowerHealthy(List<HealthyCard> healthyCardList, double standardPower) {
        //查询电力容量情况
        BigScreenDto power = instrumentDao.getBidScreenByUps();
        //电力容量
        HealthyCard powerCard = new HealthyCard();
        powerCard.setCardId(HealthyCardEnum.CABINET_POWER.getType());
        if (power == null || power.getUsePower() == null) {
            powerCard.setHealthy(true);
        } else {
            double powerUse = power.getUsePower() / power.getAllPower() * 100;
            powerCard.setHealthy(powerUse <= standardPower);
        }
        healthyCardList.add(powerCard);
    }

    /**
     * 计算空间容量健康
     *
     * @param healthyCardList 健康卡
     * @param standardSpace   空间容量标准
     */
    private void getSpaceHealthy(List<HealthyCard> healthyCardList, double standardSpace) {
        //查询空间容量情况
        BigScreenDto space = instrumentDao.getBidScreenByCabinet();
        //空间容量
        HealthyCard spaceCard = new HealthyCard();
        spaceCard.setCardId(HealthyCardEnum.CABINET_SPACE.getType());
        if (space == null || space.getUseSpace() == null) {
            spaceCard.setHealthy(true);
        } else {
            double spaceUse = space.getUseSpace() / (space.getUnUseSpace() + space.getUseSpace()) * 100;
            spaceCard.setHealthy(spaceUse <= standardSpace);
        }
        healthyCardList.add(spaceCard);
    }

    /**
     * 计算水冷机组运行时长、消防蓄水池健康
     *
     * @param healthyCardList 健康卡
     * @param standardRuntime 水冷机组运行时长标准
     * @param standardWater   消防蓄水池标准
     */
    private void getHealthy(List<HealthyCard> healthyCardList, double standardRuntime, double standardWater) {
        //查询水冷机组运行时长、消防蓄水池
        List<HealthyParam> healthyParamList = healthyDao.queryHealthyParamAll();
        //水冷机组运行时长
        HealthyCard runtimeCard = new HealthyCard();
        runtimeCard.setCardId(HealthyCardEnum.WATER_COOLED_UNIT.getType());
        //消防蓄水池
        HealthyCard waterCard = new HealthyCard();
        waterCard.setCardId(HealthyCardEnum.FIRE_RESERVOIR.getType());
        //是否有数据
        if (CollectionUtils.isEmpty(healthyParamList)) {
            runtimeCard.setHealthy(true);
            waterCard.setHealthy(true);
        } else {
            double maxRuntime = 0;
            double minRuntime = -1;
            double water = 0;
            for (HealthyParam healthyParam : healthyParamList) {
                String paramType = healthyParam.getParamType();
                if (HealthyParamEnum.WATER_COOLED_UNIT.getType().equals(paramType)) {
                    WaterCooledUnit cooledUnit = JSON.parseObject(healthyParam.getParamValue(), WaterCooledUnit.class);
                    Double endRuntime = cooledUnit.getEndRuntime();
                    maxRuntime = Math.max(endRuntime, maxRuntime);
                    if (minRuntime < 0) {
                        minRuntime = endRuntime;
                    } else {
                        minRuntime = Math.min(endRuntime, minRuntime);
                    }
                } else if (HealthyParamEnum.FIRE_RESERVOIR.getType().equals(paramType)) {
                    FireReservoir fireReservoir = JSON.parseObject(healthyParam.getParamValue(), FireReservoir.class);
                    Double waterSupplement = fireReservoir.getWaterSupplement();
                    water = Math.max(waterSupplement, water);
                }
            }
            double runtime = maxRuntime - minRuntime;
            runtimeCard.setHealthy(runtime <= standardRuntime);
            waterCard.setHealthy(water >= standardWater);
        }
        healthyCardList.add(runtimeCard);
        healthyCardList.add(waterCard);
    }


}
