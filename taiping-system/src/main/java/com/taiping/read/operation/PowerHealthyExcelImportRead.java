package com.taiping.read.operation;

import com.taiping.biz.operation.service.impl.AbstractImportExcelOper;
import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.operation.ChaiFa;
import com.taiping.entity.operation.DistHighCabinet;
import com.taiping.entity.operation.DistLowCabinet;
import com.taiping.entity.operation.Transformer;
import com.taiping.exception.BizException;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ReadCellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taiping.constant.operation.OperationConstant.OPER_SHEET_NAME_CHAI_FA;
import static com.taiping.constant.operation.OperationConstant.OPER_SHEET_NAME_DIST_HIGH;
import static com.taiping.constant.operation.OperationConstant.OPER_SHEET_NAME_DIST_LOW;
import static com.taiping.constant.operation.OperationConstant.OPER_SHEET_NAME_TRANS;

/**
 * 运行情况-物业沟通-配电系统
 *
 * @author: liyj
 * @date: 2019/12/9 16:01
 **/
@Component
public class PowerHealthyExcelImportRead extends AbstractImportExcelOper {

    /***
     * TODO: 2019/12/9 导入逻辑
     * 1 根据sheet 名称 switch
     * 2 解析固定的数据 转变成ExcelReadBean
     * 3 存入数据库 后续查询和处理数据逻辑
     */

    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 4;
    private static final Double MAX_LOAD_RESIDUETIME = 10000.0;


    private static final String CHECK_STATE = "√";
    private static final String SPIT_ONE = "-";
    private static final String SPIT_TWO = "～";

    /**
     * 检测sheet 页名称
     *
     * @param numberOfSheets sheet页数量
     */
    @Override
    protected void checkSheetNumber(int numberOfSheets) {

    }

    /**
     * 行数据转实体
     *
     * @param sheetName sheet页标识
     * @param row       行数据
     * @return
     */
    @Override
    protected ExcelReadBean getBean(String sheetName, Row row) {
        return null;
    }

    /**
     * 根据SheetName 获取实际数据开始行数和结束行数
     *
     * @param sheetName sheet页标识
     * @return
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean readBean = new ExcelImportReadBean();
        switch (sheetName) {
            case OPER_SHEET_NAME_DIST_HIGH:
                readBean.setBegin(4);
                break;
            case OPER_SHEET_NAME_DIST_LOW:
                readBean.setBegin(4);
                break;
            case OPER_SHEET_NAME_TRANS:
                readBean.setBegin(4);
                break;
            case OPER_SHEET_NAME_CHAI_FA:
                readBean.setBegin(4);
                readBean.setEnd(9);
                break;
            default:
                System.out.println("没有找到对应的Excel,请检测导入的Excel...");
                throw new BizException(100000000, "没有找到对应的Excel,请检测导入的Excel...");
        }
        return readBean;
    }

    /**
     * 数据处理
     * 含有合并单元格
     *
     * @param sheet     sheet页数据
     * @param sheetName sheet页标识
     * @param row       行标识
     * @return
     */
    @Override
    protected ExcelReadBean getBeanHaveMergedRegion(Sheet sheet, String sheetName, int row) {
        return null;
    }


    /**
     * 获取范围数据
     *
     * @param sheet
     * @param sheetName
     * @param row
     * @return
     */
    @Override
    protected ExcelReadBean getRangeBean(Sheet sheet, String sheetName, int row) {
        ExcelReadBean readBean = null;

        switch (sheetName) {
            case OPER_SHEET_NAME_DIST_HIGH:
                readBean = getDistHighCabinetRange(sheet, row);
                break;
            case OPER_SHEET_NAME_DIST_LOW:
                readBean = getDistLowCabinetRange(sheet, row);
                break;
            case OPER_SHEET_NAME_TRANS:
                readBean = getTransformerRange(sheet, row);
                break;
            case OPER_SHEET_NAME_CHAI_FA:
                readBean = getCharFaRange(sheet, row);
                break;
            default:
                System.out.println("没有找到对应的Excel,请检测导入的Excel...");
                throw new BizException(100000000, "没有找到对应的Excel,请检测导入的Excel...");
        }

        return readBean;
    }

    @Override
    protected ExcelReadBean getBeanHaveMergedRegion(Sheet sheet, String sheetName, int row, ExcelReadBean rangeBean) {
        ExcelReadBean readBean = null;
        Row sheetRow = sheet.getRow(row);
        switch (sheetName) {
            case OPER_SHEET_NAME_DIST_HIGH:
                // 这里需要获取合并单元格的数据
                // 获取合并单元格的起始行
                Integer orderNum = new Double(ExcelReadUtils.getMergedRegionValueToString(sheet, row, 0)).intValue();
                String lineName = ExcelReadUtils.getMergedRegionValueToString(sheet, row, 1);
                readBean = getCommonDistHighCabinet(sheetRow, orderNum, lineName, (DistHighCabinet) rangeBean);
                break;
            case OPER_SHEET_NAME_DIST_LOW:
                readBean = getCommonDistLowCabinet(sheetRow, (DistLowCabinet) rangeBean);
                break;
            case OPER_SHEET_NAME_TRANS:
                readBean = getCommonTransformer(sheetRow, (Transformer) rangeBean);
                break;
            case OPER_SHEET_NAME_CHAI_FA:
                // 合并单元格的数据
                ChaiFa rangeBean1 = (ChaiFa) rangeBean;
                rangeBean1.setOilLevelDaily(getDoubleValueByMeager(sheet, row, 15));
                rangeBean1.setOilLevelOutdoorOne(getDoubleValueByMeager(sheet, row, 16));
                rangeBean1.setOilLevelOutdoorTwo(getDoubleValueByMeager(sheet, row, 17));
                rangeBean1.setCurrLoadResidueOilTime(getDoubleValueByMeager(sheet, row, 18));
                rangeBean1.setResidueFullLoadOilTime(getDoubleValueByMeager(sheet, row, 19));

                readBean = getCommonChaiFa(sheetRow, rangeBean1, false);
                break;
            default:
                System.out.println("没有找到对应的Excel,请检测导入的Excel...");
                throw new BizException(OperationResultCode.FILE_TYPE_ERROR, OperationResultMsg.NOT_FIND_SHHET_NAME);
        }

        return readBean;
    }

    @Override
    protected ExcelReadBean getBean(String sheetName, Row row, ExcelReadBean rangeBean) {
        ExcelReadBean readBean = null;
        switch (sheetName) {
            case OPER_SHEET_NAME_DIST_HIGH:
                Integer orderNum = Integer.valueOf(ReadCellUtil.getCellString(row, 0));
                String lineName = ReadCellUtil.getCellString(row, 1);
                readBean = getCommonDistHighCabinet(row, orderNum, lineName, (DistHighCabinet) rangeBean);
                break;
            case OPER_SHEET_NAME_DIST_LOW:
                readBean = getCommonDistLowCabinet(row, (DistLowCabinet) rangeBean);
                break;
            case OPER_SHEET_NAME_TRANS:
                readBean = getCommonTransformer(row, (Transformer) rangeBean);
                break;
            case OPER_SHEET_NAME_CHAI_FA:
                readBean = getCommonChaiFa(row, (ChaiFa) rangeBean, true);
                break;
            default:
                System.out.println("没有找到对应的Excel,请检测导入的Excel...");
                throw new BizException(OperationResultCode.FILE_TYPE_ERROR, OperationResultMsg.NOT_FIND_SHHET_NAME);
        }
        return readBean;
    }


    //====================================================高压柜 start==========================================================================

    /**
     * 获取高压柜公共数据
     *
     * @param row      行数据
     * @param orderNum 序号
     * @param lineName 线路名称
     * @return
     */
    private DistHighCabinet getCommonDistHighCabinet(Row row, Integer orderNum, String lineName, DistHighCabinet distHighCabinet) {

        distHighCabinet.setTId(NineteenUUIDUtils.uuid());
        distHighCabinet.setOrderNum(orderNum);
        distHighCabinet.setLineName(lineName);
        //配电柜编号
        distHighCabinet.setPowerNum(ReadCellUtil.getCellString(row, 2));
        //名称
        distHighCabinet.setName(ReadCellUtil.getCellString(row, 3));
        //状态
        String state = ReadCellUtil.getCellString(row, 4);
        if (state != null) {
            distHighCabinet.setState(state.contains("未启用") ? 1 : 0);
        }

        //  L1 电压
        distHighCabinet.setVoltageL1(getValueByRow(row, 5));
        //  L2 电压
        distHighCabinet.setVoltageL2(getValueByRow(row, 6));
        //  L3 电压
        distHighCabinet.setVoltageL3(getValueByRow(row, 7));
        //  L1 电流
        distHighCabinet.setElectricL1(getValueByRow(row, 8));
        //  L2 电流
        distHighCabinet.setElectricL2(getValueByRow(row, 9));
        //  L3 电流
        distHighCabinet.setElectricL3(getValueByRow(row, 10));

        //  柜体是否有异响及异味
        distHighCabinet.setCabinetSmellStatus(checkStatus(ReadCellUtil.getCellString(row, 11)));
        //  柜面仪表指示灯是否正常
        distHighCabinet.setCabinetLampStatus(checkStatus(ReadCellUtil.getCellString(row, 12)));


        //A路直流屏电池电压序号
        distHighCabinet.setDcPanelVoltageAOrderNum(ReadCellUtil.getCellString(row, 13));
        //A路直流屏电池电压（V）
        distHighCabinet.setDcPanelVoltageA(getValueByRow(row, 14));
        //A路直流屏电池内阻（mΩ）
        distHighCabinet.setDcPanelInternalA(getValueByRow(row, 15));

        //B路直流屏电池电压序号
        distHighCabinet.setDcPanelVoltageBOrderNum(ReadCellUtil.getCellString(row, 16));
        //B路直流屏电池电压（V）
        distHighCabinet.setDcPanelVoltageB(getValueByRow(row, 17));
        //B路直流屏电池内阻（mΩ）
        distHighCabinet.setDcPanelInternalB(getValueByRow(row, 18));

        //备注
        distHighCabinet.setRemark(ReadCellUtil.getCellString(row, 19));
        // 导入时间
        distHighCabinet.setCreateTime(System.currentTimeMillis());
        // 月份
        distHighCabinet.setMonth(LocalDate.now().getMonthValue());
        // 年
        distHighCabinet.setYear(LocalDate.now().getYear());
        return distHighCabinet;
    }

    /**
     * 含有合并单元格的数据
     * 获取高压柜所有范围值
     * 电压  Row =4 Low=6-8
     * 电流  Row=4  Low=9-12
     * DC  直流屏电池电压 Row=4 Low =14-19
     *
     * @return
     */
    private DistHighCabinet getDistHighCabinetRange(Sheet sheet, int row) {
        DistHighCabinet distHighCabinet = new DistHighCabinet();
        //  电压范围
        String voltageRange = ReadCellUtil.getCellString(sheet, row, 6);
        //电流范围
        String eleRange = ReadCellUtil.getCellString(sheet, row, 9);
        // Dc 直流电池电压
        String dcEleRange = ReadCellUtil.getCellString(sheet, row, 14);

        distHighCabinet.setMaxVoltage(getMaxValueByRange(voltageRange, SPIT_TWO));
        distHighCabinet.setMaxElectric(getMaxValueByRange(eleRange, SPIT_TWO));
        distHighCabinet.setMaxDcVoltage(getMaxValueByRange(dcEleRange, SPIT_TWO));
        distHighCabinet.setMinDcVoltage(getMinValueByRange(dcEleRange, SPIT_TWO));
        distHighCabinet.setMinVoltage(getMinValueByRange(voltageRange, SPIT_TWO));
        distHighCabinet.setMinElectric(getMinValueByRange(eleRange, SPIT_TWO));

        return distHighCabinet;
    }

    //====================================================高压柜 End==========================================================================


    //====================================================低压柜 Start==========================================================================

    /**
     * 获取低压柜所有范围值
     *
     * @return
     */
    private DistLowCabinet getDistLowCabinetRange(Sheet sheet, int row) {
        DistLowCabinet distLowCabinet = new DistLowCabinet();

        String envTemperatures = ReadCellUtil.getCellString(sheet, row, 3);
        //环境温度
        distLowCabinet.setMinEnvTemperature(getMinValueByRange(envTemperatures, SPIT_TWO));
        distLowCabinet.setMaxEnvTemperature(getMaxValueByRange(envTemperatures, SPIT_TWO));
        return distLowCabinet;
    }

    /**
     * 获取低压柜公共数据
     *
     * @param row 行数据
     * @return
     */
    private DistLowCabinet getCommonDistLowCabinet(Row row, DistLowCabinet distLowCabinet) {
        //
        distLowCabinet.setTId(NineteenUUIDUtils.uuid());

        distLowCabinet.setOrderNum(new Double(ReadCellUtil.getCellString(row, 0)).intValue());
        distLowCabinet.setPosition(ReadCellUtil.getCellString(row, 1));
        distLowCabinet.setPowerName(ReadCellUtil.getCellString(row, 2));
        distLowCabinet.setEnvTemperature(getValueByRow(row, 3));
        distLowCabinet.setCabinetSmellStatus(checkStatus(ReadCellUtil.getCellString(row, 4)));
        distLowCabinet.setSwitchingState(checkStatus(ReadCellUtil.getCellString(row, 5)));
        distLowCabinet.setCabinetLampStatus(checkStatus(ReadCellUtil.getCellString(row, 6)));
        distLowCabinet.setLineTemperatureStatus(checkStatus(ReadCellUtil.getCellString(row, 7)));
        distLowCabinet.setEarthingStatus(checkStatus(ReadCellUtil.getCellString(row, 8)));
        distLowCabinet.setCabinetDoorStatus(checkStatus(ReadCellUtil.getCellString(row, 9)));
        distLowCabinet.setEnvClearStatus(checkStatus(ReadCellUtil.getCellString(row, 10)));
        distLowCabinet.setRemark(ReadCellUtil.getCellString(row, 11));

        // 导入时间
        distLowCabinet.setCreateTime(System.currentTimeMillis());
        // 月份
        distLowCabinet.setMonth(LocalDate.now().getMonthValue());
        // 年
        distLowCabinet.setYear(LocalDate.now().getYear());

        return distLowCabinet;
    }


    //====================================================低压柜 End============================================================================


    //====================================================变压器 Start==========================================================================

    /**
     * 获取变压器所有的范围值
     *
     * @return
     */
    private Transformer getTransformerRange(Sheet sheet, int row) {
        Transformer transformer = new Transformer();
        // 电压
        String voltages = ReadCellUtil.getCellString(sheet, row, 3);
        transformer.setMaxVoltage(getMaxValueByRange(voltages, SPIT_TWO));
        transformer.setMinVoltage(getMinValueByRange(voltages, SPIT_TWO));

        // 电流
        String ele = ReadCellUtil.getCellString(sheet, row, 6);
        transformer.setMaxElectric(getMaxValueByRange(ele, SPIT_TWO));
        transformer.setMinElectric(getMinValueByRange(ele, SPIT_TWO));

        // 铁芯温度1
        String coreTemp1 = ReadCellUtil.getCellString(sheet, row, 11);
        transformer.setMaxCoreTemperatureOne(getMaxValueByRange(coreTemp1, SPIT_TWO));
        transformer.setMinCoreTemperatureOne(getMinValueByRange(coreTemp1, SPIT_TWO));

        // 铁芯温度2
        String coreTemp2 = ReadCellUtil.getCellString(sheet, row, 14);
        transformer.setMaxCoreTemperatureTwo(getMaxValueByRange(coreTemp2, SPIT_TWO));
        transformer.setMinCoreTemperatureTwo(getMinValueByRange(coreTemp2, SPIT_TWO));

        return transformer;
    }

    /**
     * 获取变压器公共数据
     *
     * @param row 行数据
     * @return
     */
    private Transformer getCommonTransformer(Row row, Transformer transformer) {

        transformer.setTId(NineteenUUIDUtils.uuid());

        transformer.setOrderNum(new Double(ReadCellUtil.getCellString(row, 0)).intValue());
        // 变压器名称
        transformer.setTransformerName(ReadCellUtil.getCellString(row, 1));

        // 电压
        transformer.setVoltageL1(getValueByRow(row, 3));
        transformer.setVoltageL2(getValueByRow(row, 4));
        transformer.setVoltageL3(getValueByRow(row, 5));

        //电流
        transformer.setElectricL1(getValueByRow(row, 6));
        transformer.setElectricL2(getValueByRow(row, 7));
        transformer.setElectricL3(getValueByRow(row, 8));

        // 有功
        transformer.setActivePower(getValueByRow(row, 9));
        // 负荷率
        transformer.setLoadRate(getValueByRow(row, 10));

        // 铁芯温度
        transformer.setCoreTemperatureA(getValueByRow(row, 11));
        transformer.setCoreTemperatureB(getValueByRow(row, 12));
        transformer.setCoreTemperatureC(getValueByRow(row, 13));
        transformer.setCoreTemperatureD(getValueByRow(row, 14));

        //风机状态
        transformer.setFanStatus(checkStatus(ReadCellUtil.getCellString(row, 15)));
        transformer.setCabinetSmellStatus(checkStatus(ReadCellUtil.getCellString(row, 16)));
        transformer.setRemark(ReadCellUtil.getCellString(row, 17));


        // 导入时间
        transformer.setCreateTime(System.currentTimeMillis());
        // 月份
        transformer.setMonth(LocalDate.now().getMonthValue());
        // 年
        transformer.setYear(LocalDate.now().getYear());

        return transformer;
    }

    //====================================================变压器 End============================================================================


    //====================================================柴发 Start==========================================================================

    /**
     * 获取柴发的所有范围值
     *
     * @return
     */
    private ChaiFa getCharFaRange(Sheet sheet, int row) {
        ChaiFa chaiFa = new ChaiFa();
        // 冷却液水位是否正常
        chaiFa.setCoolantRange(ReadCellUtil.getCellString(sheet, row, 3));

        //水温
        String waterTemp = ReadCellUtil.getCellString(sheet, row, 4);
        chaiFa.setMaxWaterTemperature(getMaxValueByRange(waterTemp, SPIT_TWO));
        chaiFa.setMinWaterTemperature(getMinValueByRange(waterTemp, SPIT_TWO));

        //蓄电池电压
        String batteryVoltage = ReadCellUtil.getCellString(sheet, row, 8);
        chaiFa.setMaxBatteryVoltage(getMaxValueByRange(batteryVoltage, SPIT_TWO));
        chaiFa.setMinBatteryVoltage(getMinValueByRange(batteryVoltage, SPIT_TWO));

        //电池内阻
        String resistance = ReadCellUtil.getCellString(sheet, row, 12);
        chaiFa.setMaxResistance(getMaxValueByRange(resistance, SPIT_TWO));
        chaiFa.setMinResistance(getMinValueByRange(resistance, SPIT_TWO));

        //日用油箱油位
        String oilLevelDaily = ReadCellUtil.getCellString(sheet, row, 15);
        chaiFa.setMaxOilLevelDaily(getMaxValueByRange(oilLevelDaily, SPIT_TWO));
        chaiFa.setMinOilLevelDaily(getMinValueByRange(oilLevelDaily, SPIT_TWO));

        //室外油罐油位
        String OilLevelOutdoor = ReadCellUtil.getCellString(sheet, row, 16);
        chaiFa.setMaxOilLevelOutdoor(getMaxValueByRange(OilLevelOutdoor, SPIT_TWO));
        chaiFa.setMinOilLevelOutdoor(getMinValueByRange(OilLevelOutdoor, SPIT_TWO));

        //当前负载剩余柴油  (特殊字符  超过12H)
        String loadResidueTime = ReadCellUtil.getCellString(sheet, row, 19);
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(loadResidueTime);
        String trim = m.replaceAll("").trim();
        if (trim != null) {
            Double value = Double.valueOf(trim);
            chaiFa.setMinLoadResidueTime(value);
            chaiFa.setMaxLoadResidueTime(MAX_LOAD_RESIDUETIME);
        }

        //柴发测试时电压是否正常
        chaiFa.setTestVolRange(ReadCellUtil.getCellString(sheet, row, 21));
        //柴发测试时电流是否正常
        chaiFa.setTestEleRange(ReadCellUtil.getCellString(sheet, row, 22));

        return chaiFa;
    }

    /**
     * 获取变压器公共数据
     *
     * @param row 行数据
     * @return
     */
    private ChaiFa getCommonChaiFa(Row row, ChaiFa chaiFa, boolean mergeFlag) {
        chaiFa.setTId(NineteenUUIDUtils.uuid());
        chaiFa.setOrderNum(new Double(ReadCellUtil.getCellString(row, 0)).intValue());
        // 柴发名称
        chaiFa.setChaiFaName(ReadCellUtil.getCellString(row, 1));
        //冷却液水位是否正常
        chaiFa.setCoolantLevelStatus(checkStatus(ReadCellUtil.getCellString(row, 3)));
        // 水温
        chaiFa.setWaterTemperature(getValueByRow(row, 4));
        //机油油位状态 供油管路及机组是否渗漏
        chaiFa.setOilLevelStatus(checkStatus(ReadCellUtil.getCellString(row, 5)));
        chaiFa.setOilSupplyLineStatus(checkStatus(ReadCellUtil.getCellString(row, 6)));
        //蓄电池电压
        chaiFa.setBatteryVoltageA1(getValueByRow(row, 7));
        chaiFa.setBatteryVoltageA2(getValueByRow(row, 8));
        chaiFa.setBatteryVoltageB1(getValueByRow(row, 9));
        chaiFa.setBatteryVoltageB2(getValueByRow(row, 10));

        // 电阻
        chaiFa.setResistanceA1(getValueByRow(row, 11));
        chaiFa.setResistanceA2(getValueByRow(row, 12));
        chaiFa.setResistanceB1(getValueByRow(row, 13));
        chaiFa.setResistanceB2(getValueByRow(row, 14));

        //合并单元格
        //日用油箱油位
        if (mergeFlag) {
            chaiFa.setOilLevelDaily(getValueByRow(row, 15));
            chaiFa.setOilLevelOutdoorOne(getValueByRow(row, 16));
            chaiFa.setOilLevelOutdoorTwo(getValueByRow(row, 17));
            chaiFa.setCurrLoadResidueOilTime(getValueByRow(row, 18));
            chaiFa.setResidueFullLoadOilTime(getValueByRow(row, 19));
        }

        //4种状态
        chaiFa.setChaiFaControllerStatus(checkStatus(ReadCellUtil.getCellString(row, 20)));
        chaiFa.setChaiFaTestVolStatus(checkStatus(ReadCellUtil.getCellString(row, 21)));
        chaiFa.setChaiFaTestEleStatus(checkStatus(ReadCellUtil.getCellString(row, 22)));
        chaiFa.setFuelFeediControllerStatus(checkStatus(ReadCellUtil.getCellString(row, 23)));

        chaiFa.setRemark(ReadCellUtil.getCellString(row, 24));

        // 导入时间
        chaiFa.setCreateTime(System.currentTimeMillis());
        // 月份
        chaiFa.setMonth(LocalDate.now().getMonthValue());
        // 年
        chaiFa.setYear(LocalDate.now().getYear());
        return chaiFa;
    }
    //====================================================柴发 End============================================================================


    /**
     * 特殊字符检测
     * 是否含有  √
     *
     * @param status
     * @return 0 未选择 1 选择
     */
    private Integer checkStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return 0;
        }
        return status.contains(CHECK_STATE) ? 1 : 0;
    }


    private Double getValueByRow(Row row, int cellNum) {
        String cellString = ReadCellUtil.getCellString(row, cellNum);
        return StringUtils.isEmpty(cellString) ? null : Double.valueOf(cellString);

    }

    private Double getDoubleValueByMeager(Sheet sheet, int row, int cellNum) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNum);
        return StringUtils.isEmpty(value) ? null : Double.valueOf(value);
    }

    /**
     * @param range
     * @param spit
     */
    private Double getMaxValueByRange(String range, String spit) {
        if (StringUtils.isEmpty(range)) {
            return null;
        }
        if (range.contains(SPIT_ONE)) {
            spit = SPIT_ONE;
        }
        List<String> list = Arrays.asList(range.split(spit));
        Double start = Double.valueOf(list.get(0));
        Double end = Double.valueOf(list.get(1));

        return start >= end ? start : end;
    }

    /**
     * @param range
     * @param spit
     */
    private Double getMinValueByRange(String range, String spit) {
        if (StringUtils.isEmpty(range)) {
            return null;
        }
        if (range.contains(SPIT_ONE)) {
            spit = SPIT_ONE;
        }
        List<String> list = Arrays.asList(range.split(spit));
        Double start = Double.valueOf(list.get(0));
        Double end = Double.valueOf(list.get(1));

        return start <= end ? start : end;
    }

}
