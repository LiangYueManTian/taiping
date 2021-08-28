package com.taiping.read.energy;

import com.taiping.bean.energy.dto.ElectricPowerImportDto;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.energy.PowerEnergyItem;
import com.taiping.enums.energy.*;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 总能耗数据解析
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class ElectricPowerExcelImportRead extends AbstractExcelImportRead {

    /**
     * 校验sheet页数量
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
     * @return 实体
     */
    @Override
    protected ExcelReadBean getBean(String sheetName, Row row) {
        if (ElectricPowerSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            ElectricPowerImportDto electricImportDto = new ElectricPowerImportDto();
            getElectricPower(row, electricImportDto);
            return electricImportDto;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean excelImportReadBean = new ExcelImportReadBean();
        if (ElectricPowerSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(ElectricPowerSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
        } else {
            throw new RuntimeException();
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
        ElectricPowerImportDto powerImportDto = new ElectricPowerImportDto();
        if (ElectricSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            getElectricPowerMergedRegion(sheet, row, powerImportDto);
        } else {
            throw new RuntimeException();
        }
        return powerImportDto;
    }

    /**
     * 业务数据转换成存储数据
     */
    private void getElectricPower(Row row, ElectricPowerImportDto electricPowerImportDto) {
        String dataName = this.getCellValueToString(row, 0);
        String electricMeter = this.getCellValueToString(row, 1);
        String name = this.getCellValueToString(row, 2);
        String meterInfo = this.getCellValueToString(row, 3);
        //获取年份
        String year = this.getCellValueToString(row, 15);
        if (ObjectUtils.isEmpty(dataName) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(electricMeter) && ObjectUtils.isEmpty(meterInfo)) {
            electricPowerImportDto.setIsReturn(1);
            return;
        }

        //找到电力设备的唯一标识
        boolean isElectricMeter = false;
        if (!ObjectUtils.isEmpty(ElectricPowerItemEnum.getTypeByData(electricMeter))) {
            isElectricMeter = true;
        }

        double meter = 0;
        //年份
        Calendar date = Calendar.getInstance();
        int localYear = date.get(Calendar.YEAR);
        //月份
        int month = 0;
        Long collectionTime = 0L;
        List<PowerEnergyItem> electricPowerList = new ArrayList<>();
        for (int i = 3 ; i <= 14; i++) {
            month = i - 2;
            String uuid = NineteenUUIDUtils.uuid();
            PowerEnergyItem powerEnergyItem = new PowerEnergyItem();
            powerEnergyItem.setPowerEnergyId(uuid);
            String allElectricMeter = this.getCellValueToString(row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getCellValueToInteger(row, 15);
            } else {
                setYear = localYear;
            }
            powerEnergyItem.setYear(setYear);
            powerEnergyItem.setMonth(month);
            powerEnergyItem.setDataName(dataName);
            powerEnergyItem.setElectricMeter(electricMeter);
            powerEnergyItem.setName(name);

            //类型
            String dataInfo = this.getDataInfo(powerEnergyItem, isElectricMeter);
            powerEnergyItem.setType(ElectricPowerItemTypeEnum.getTypeByData(dataInfo));
            if (ElectricPowerTypeEnum.COOLING_TOWER.getType().equals(ElectricPowerItemTypeEnum.getTypeByData(dataInfo))) {
                meter = meter / 2 ;
            }
            powerEnergyItem.setAllElectricMeter(meter);
            powerEnergyItem.setIsHeatItem(ElectricPowerTypeHeatEnum.getTypeByName(powerEnergyItem.getType()));
            powerEnergyItem.setRoute(ElectricPowerRouteAndDataEnum.getTypeByData(dataInfo));
            powerEnergyItem.setDataCode(dataInfo);
            Long nowDate = System.currentTimeMillis();
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            powerEnergyItem.setDataCollectionTime(collectionTime);
            powerEnergyItem.setCreateTime(nowDate);
            electricPowerList.add(powerEnergyItem);
        }
        electricPowerImportDto.setPowerEnergyItemList(electricPowerList);
    }


    /**
     * 获取数据信息
     * @author hedongwei@wistronits.com
     * @date  2019/11/13 17:28
     * @param powerEnergyItem 能耗分项
     * @param isElectricMeter 是否是取电量仪
     * @return 返回数据信息
     */
    public String getDataInfo(PowerEnergyItem powerEnergyItem, boolean isElectricMeter) {
        String dataInfo = "";
        if (!ObjectUtils.isEmpty(powerEnergyItem)) {
            if (isElectricMeter) {
                dataInfo = powerEnergyItem.getElectricMeter();
            } else {
                dataInfo = ElectricPowerItemEnum.getTypeByRealData(powerEnergyItem.getName());
            }
        }
        return dataInfo;
    }


    /**
     * 获取String类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public String getCellValueToString(Row row, int cellNumber) {
        return ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
    }

    /**
     * 获取不为空的String类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public String getNotNullCellValueToString(Row row, int cellNumber) {
        String value = this.getCellValueToString(row, cellNumber);
        return checkStringNull(value);
    }

    /**
     * 获取Integer类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param,cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public Integer getCellValueToInteger(Row row, int cellNumber) {
        String value = ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
        value = value.split("[.]")[0];
        return Integer.parseInt(value);
    }

    /**
     * 获取Integer类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param,cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public Integer getNotNullCellValueToInteger(Row row, int cellNumber) {
        String value = ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
        value = value.split("[.]")[0];
        value = this.checkStringNull(value);
        return Integer.parseInt(value);
    }


    /**
     * 获取Double类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public double getCellValueToDouble(Row row, int cellNumber) {
        String value = ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
        value = value.split("%")[0];
        return Double.valueOf(value);
    }

    /**
     * 获取不为空Double类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public double getNotNullCellValueToDouble(Row row, int cellNumber) {
        String value = ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
        value = value.split("%")[0];
        if (ObjectUtils.isEmpty(value)) {
            value = "0";
        }
        value = this.checkStringNull(value);
        return Double.valueOf(value);
    }

    /**
     * 获取String类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public String getMergedCellValueToString(Sheet sheet, int row , int cellNumber) {
        return ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
    }

    /**
     * 获取不为空String类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public String getNotNullMergedCellValueToString(Sheet sheet, int row , int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        return checkStringNull(value);
    }

    /**
     * 获取Integer类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public Integer getMergedCellValueToInteger(Sheet sheet, int row, int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        if (!ObjectUtils.isEmpty(value)) {
            value = value.split("[.]")[0];
        }
        return Integer.parseInt(value);
    }


    /**
     * 获取Integer类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public Integer getNotNullMergedCellValueToInteger(Sheet sheet, int row, int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        if (!ObjectUtils.isEmpty(value)) {
            value = value.split("[.]")[0];
        }
        value = this.checkStringNull(value);
        return Integer.parseInt(value);
    }

    /**
     * 获取Double类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public double getMergedCellValueToDouble(Sheet sheet, int row, int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        if (!ObjectUtils.isEmpty(value)) {
            value = value.split("%")[0];
        }
        return Double.valueOf(value);
    }

    /**
     * 获取不为空Double类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public double getNotNullMergedCellValueToDouble(Sheet sheet, int row, int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        if (!ObjectUtils.isEmpty(value)) {
            value = value.split("%")[0];
        }
        if (ObjectUtils.isEmpty(value)) {
            value = "0";
        }
        value = this.checkStringNull(value);
        return Double.valueOf(value);
    }


    /**
     * 业务数据转换成存储数据
     */
    private void getElectricPowerMergedRegion(Sheet sheet, int row, ElectricPowerImportDto dto) {
        String dataName = this.getNotNullMergedCellValueToString(sheet, row, 0);
        String electricMeter = this.getNotNullMergedCellValueToString(sheet, row, 1);
        String name = this.getNotNullMergedCellValueToString(sheet, row, 2);
        String meterInfo = this.getNotNullMergedCellValueToString(sheet, row, 3);
        //获取年份
        String year = this.getMergedCellValueToString(sheet, row, 15);
        Long collectionTime = 0L;

        if (ObjectUtils.isEmpty(dataName) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(electricMeter) && ObjectUtils.isEmpty(meterInfo)) {
            dto.setIsReturn(1);
            return;
        }


        //找到电力设备的唯一标识
        boolean isElectricMeter = false;
        if (!ObjectUtils.isEmpty(ElectricPowerItemEnum.getTypeByData(electricMeter))) {
            isElectricMeter = true;
        }

        double meter = 0;
        //年份
        Calendar date = Calendar.getInstance();
        int localYear = date.get(Calendar.YEAR);
        //月份
        int month = 0;
        List<PowerEnergyItem> powerEnergyList = new ArrayList<>();
        for (int i = 3 ; i <= 14; i++) {
            month = i - 2;
            String uuid = NineteenUUIDUtils.uuid();
            String allElectricMeter = this.getMergedCellValueToString(sheet, row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getMergedCellValueToInteger(sheet, row, 15);
            } else {
                setYear = localYear;
            }
            PowerEnergyItem powerEnergyItem = new PowerEnergyItem();
            powerEnergyItem.setYear(setYear);
            powerEnergyItem.setMonth(month);
            powerEnergyItem.setPowerEnergyId(uuid);
            powerEnergyItem.setDataName(dataName);
            powerEnergyItem.setName(name);
            powerEnergyItem.setElectricMeter(electricMeter);

            //类型
            String dataInfo = this.getDataInfo(powerEnergyItem, isElectricMeter);
            powerEnergyItem.setType(ElectricPowerItemTypeEnum.getTypeByData(dataInfo));
            if (ElectricPowerTypeEnum.COOLING_TOWER.getType().equals(ElectricPowerItemTypeEnum.getTypeByData(dataInfo))) {
                meter = meter / 2 ;
            }
            powerEnergyItem.setAllElectricMeter(meter);
            powerEnergyItem.setIsHeatItem(ElectricPowerTypeHeatEnum.getTypeByName(powerEnergyItem.getType()));
            powerEnergyItem.setRoute(ElectricPowerRouteAndDataEnum.getTypeByData(dataInfo));
            powerEnergyItem.setDataCode(dataInfo);
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            Long nowDate = System.currentTimeMillis();
            powerEnergyItem.setDataCollectionTime(collectionTime);
            powerEnergyItem.setCreateTime(nowDate);
            powerEnergyList.add(powerEnergyItem);
        }
        dto.setPowerEnergyItemList(powerEnergyList);
    }

    /**
     * 校验非空
     * @param value 值
     */
    private String checkStringNull(String value) {
        if (StringUtils.isBlank(value)) {
            throw new RuntimeException();
        }
        return value.trim();
    }
}
