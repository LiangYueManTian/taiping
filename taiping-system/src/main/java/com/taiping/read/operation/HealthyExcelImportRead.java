package com.taiping.read.operation;

import com.taiping.constant.operation.OperationResultCode;
import com.taiping.constant.operation.OperationResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.operation.FireReservoir;
import com.taiping.entity.operation.WaterCooledUnit;
import com.taiping.enums.operation.HealthyExcelRowEnum;
import com.taiping.enums.operation.HealthySheetEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import static com.taiping.utils.ReadCellUtil.getCellStringNot;
import static com.taiping.utils.ReadCellUtil.getReplaceMsg;

/**
 * 导入健康卡消防蓄水池和水冷机组运行时长Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
@Component
public class HealthyExcelImportRead extends AbstractExcelImportRead {

    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 2;

    /**
     * 校验sheet页数量
     *
     * @param numberOfSheets sheet页数量
     */
    @Override
    protected void checkSheetNumber(int numberOfSheets) {
        if (numberOfSheets != SHEET_NUMBER) {
            //Excel文件sheet页数量错误
            throw new BizException(OperationResultCode.SHEET_ERROR, OperationResultMsg.SHEET_ERROR);
        }
    }

    /**
     * 行数据转实体
     *
     * @param sheetName sheet页标识
     * @param row       行数据
     * @return 实体
     */
    @Override
    protected ExcelReadBean getBean(String sheetName, Row row) {
        Integer rowNumber = row.getRowNum();
        if (HealthySheetEnum.WATER_COOLED_UNIT.getSheetName().equals(sheetName)) {
            String name = getCellStringNot(row, HealthyExcelRowEnum.UNIT_NAME.getCellNum());
            String beginRuntime = getCellStringNot(row, HealthyExcelRowEnum.BEGIN_RUNTIME.getCellNum());
            String currentRuntime = getCellStringNot(row, HealthyExcelRowEnum.CURRENT_RUNTIME.getCellNum());
            String endRuntime = getCellStringNot(row, HealthyExcelRowEnum.END_RUNTIME.getCellNum());
            return getWaterCooledUnit(rowNumber, name, beginRuntime, currentRuntime, endRuntime);
        } else if (HealthySheetEnum.FIRE_RESERVOIR.getSheetName().equals(sheetName)) {
            String name = getCellStringNot(row, HealthyExcelRowEnum.NAME.getCellNum());
            String waterCapacity = getCellStringNot(row, HealthyExcelRowEnum.WATER_CAPACITY.getCellNum());
            String waterLevel = getCellStringNot(row, HealthyExcelRowEnum.WATER_LEVEL.getCellNum());
            String waterSupplement = getCellStringNot(row, HealthyExcelRowEnum.WATER_SUPPLEMENT.getCellNum());
            String naturalCooling = getCellStringNot(row, HealthyExcelRowEnum.NATURAL_COOLING.getCellNum());
            String electricRefCurrent = getCellStringNot(row, HealthyExcelRowEnum.ELECTRIC_REF_CURRENT.getCellNum());
            String electricRefFull = getCellStringNot(row, HealthyExcelRowEnum.ELECTRIC_REF_FULL.getCellNum());
            FireReservoir fireReservoir = getFireReservoir(rowNumber, waterCapacity, waterSupplement, naturalCooling,
                    electricRefCurrent, electricRefFull);
            return getFireReservoir(fireReservoir, name, rowNumber, waterLevel);
        } else {
            return null;
        }
    }

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean excelImportReadBean;
        if (HealthySheetEnum.WATER_COOLED_UNIT.getSheetName().equals(sheetName)) {
            excelImportReadBean = HealthySheetEnum.WATER_COOLED_UNIT.getBean();
        } else if (HealthySheetEnum.FIRE_RESERVOIR.getSheetName().equals(sheetName)) {
            excelImportReadBean = HealthySheetEnum.FIRE_RESERVOIR.getBean();
        } else {
            return null;
        }
        return excelImportReadBean;
    }

    /**
     * 行数据转实体
     *
     * @param sheet     sheet页数据
     * @param sheetName sheet页标识
     * @param row       行标识
     * @return 实体
     */
    @Override
    protected ExcelReadBean getBeanHaveMergedRegion(Sheet sheet, String sheetName, int row) {
        if (HealthySheetEnum.WATER_COOLED_UNIT.getSheetName().equals(sheetName)) {
            String name = getCellStringNot(sheet, row, HealthyExcelRowEnum.UNIT_NAME.getCellNum());
            String beginRuntime = getCellStringNot(sheet, row, HealthyExcelRowEnum.BEGIN_RUNTIME.getCellNum());
            String currentRuntime = getCellStringNot(sheet, row, HealthyExcelRowEnum.CURRENT_RUNTIME.getCellNum());
            String endRuntime = getCellStringNot(sheet, row, HealthyExcelRowEnum.END_RUNTIME.getCellNum());
            return getWaterCooledUnit(row, name, beginRuntime, currentRuntime, endRuntime);
        } else if (HealthySheetEnum.FIRE_RESERVOIR.getSheetName().equals(sheetName)) {
            String name = getCellStringNot(sheet, row, HealthyExcelRowEnum.NAME.getCellNum());
            String waterCapacity = getCellStringNot(sheet, row, HealthyExcelRowEnum.WATER_CAPACITY.getCellNum());
            String waterLevel = getCellStringNot(sheet, row, HealthyExcelRowEnum.WATER_LEVEL.getCellNum());
            String waterSupplement = getCellStringNot(sheet, row, HealthyExcelRowEnum.WATER_SUPPLEMENT.getCellNum());
            String naturalCooling = getCellStringNot(sheet, row, HealthyExcelRowEnum.NATURAL_COOLING.getCellNum());
            String electricRefCurrent  = getCellStringNot(sheet, row, HealthyExcelRowEnum.ELECTRIC_REF_CURRENT.getCellNum());
            String electricRefFull  = getCellStringNot(sheet, row, HealthyExcelRowEnum.ELECTRIC_REF_FULL.getCellNum());
            FireReservoir fireReservoir = getFireReservoir(row, waterCapacity, waterSupplement, naturalCooling,
                    electricRefCurrent, electricRefFull);
            return getFireReservoir(fireReservoir, name, row, waterLevel);
        } else {
            return null;
        }
    }

    /**
     *  组装
     * @param rowNumber 行标识
     * @param name 水冷机组名称
     * @param beginRuntime 运行时长（月初始累计）
     * @param currentRuntime 运行时长
     * @param endRuntime 运行时长（月末累计）
     * @return WaterCooledUnit
     */
    private WaterCooledUnit getWaterCooledUnit(Integer rowNumber, String name, String beginRuntime,
                                               String currentRuntime, String endRuntime) {
        String nameStr = HealthySheetEnum.WATER_COOLED_UNIT.getSheet();
        WaterCooledUnit cooledUnit = new WaterCooledUnit();
        cooledUnit.setUnitName(name);
        cooledUnit.setBeginRuntime(getDouble(nameStr, rowNumber, beginRuntime));
        cooledUnit.setCurrentRuntime(getDouble(nameStr, rowNumber, currentRuntime));
        cooledUnit.setEndRuntime(getDouble(nameStr, rowNumber, endRuntime));
        return cooledUnit;
    }

    /**
     * 组装
     * @param fireReservoir 消防蓄水池
     * @param name 名称
     * @param rowNumber 行标识
     * @param waterLevel 水位
     * @return FireReservoir
     */
    private FireReservoir getFireReservoir(FireReservoir fireReservoir, String name,
                                           Integer rowNumber, String waterLevel) {
        fireReservoir.setName(name);
        fireReservoir.setRowNumber(rowNumber);
        fireReservoir.setWaterLevel(waterLevel);
        return fireReservoir;
    }
    /**
     * 组装
     * @param rowNumber 行标识
     * @param waterCapacity 消防蓄水池可储水量(合计)
     * @param waterSupplement 消防主备蓄水池可供水冷机组冷却系统补水量
     * @param naturalCooling 自然冷却模式（板换运行）可供冷却系统补水量持续使用时长（h）
     * @param electricRefCurrent 电制冷模式（冷机运行）当前负载 可供冷却系统补水量持续使用时长（h）
     * @param electricRefFull 电制冷模式（冷机运行）满载 可供冷却系统补水量持续使用时长（h）
     * @return FireReservoir
     */
    private FireReservoir getFireReservoir(int rowNumber, String waterCapacity, String waterSupplement,
                                           String naturalCooling, String electricRefCurrent, String electricRefFull) {
        String name = HealthySheetEnum.FIRE_RESERVOIR.getSheet();
        FireReservoir fireReservoir = new FireReservoir();
        fireReservoir.setWaterCapacity(getDouble(name, rowNumber, waterCapacity));
        fireReservoir.setWaterSupplement(getDouble(name, rowNumber, waterSupplement));
        fireReservoir.setNaturalCooling(getDouble(name, rowNumber, naturalCooling));
        fireReservoir.setElectricRefCurrent(getDouble(name, rowNumber, electricRefCurrent));
        fireReservoir.setElectricRefFull(getDouble(name, rowNumber, electricRefFull));
        return fireReservoir;
    }

    /**
     *
     * @param name 表明
     * @param row 行标识
     * @param string 数值
     * @return double
     */
    private double getDouble(String name, int row, String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            String msg = getReplaceMsg(OperationResultMsg.DOUBLE_ERROR, row, string, name);
            throw new BizException(OperationResultCode.DOUBLE_ERROR, msg);
        }
    }

}
