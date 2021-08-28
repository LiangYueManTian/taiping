package com.taiping.read.cabinet;

import com.taiping.bean.capacity.cabinet.dto.ItEnergyCurrentImportDto;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabinet.ItEnergyCurrent;
import com.taiping.enums.cabinet.ItEnergySheetEnum;
import com.taiping.enums.cabinet.ItEnergySheetStartRowEnum;
import com.taiping.enums.cabinet.ItModuleAndDataEnum;
import com.taiping.enums.cabinet.ItTypeAndDataEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * 导入能耗数据列
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class ItEnergyExcelImportRead extends AbstractExcelImportRead {


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
        if (ItEnergySheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            ItEnergyCurrentImportDto itEnergyCurrentDto = new ItEnergyCurrentImportDto();
            getItEnergyCurrent(row, itEnergyCurrentDto);
            return itEnergyCurrentDto;
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
        if (ItEnergySheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(ItEnergySheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        if (ItEnergySheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            ItEnergyCurrentImportDto itEnergyCurrentDto = new ItEnergyCurrentImportDto();
            getItEnergyCurrentMergedRegion(sheet, row, itEnergyCurrentDto);
            return itEnergyCurrentDto;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 业务数据转换成存储数据
     */
    private void getItEnergyCurrent(Row row, ItEnergyCurrentImportDto dto) {
        String dataName = this.getCellValueToString(row, 0);
        String name = this.getCellValueToString(row, 1);
        String electricMeter = this.getCellValueToString(row, 2);
        //获取年份
        String year = this.getCellValueToString(row, 14);
        if (ObjectUtils.isEmpty(dataName) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(electricMeter)) {
            dto.setIsReturn(1);
            return;
        }

        double meter = 0;
        //年份
        Calendar date = Calendar.getInstance();
        int localYear = date.get(Calendar.YEAR);
        //月份
        int month = 0;
        Long collectionTime = 0L;
        List<ItEnergyCurrent> itEnergyCurrentList = new ArrayList<>();
        for (int i = 2 ; i <= 13; i++) {
            month = i - 1;
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-", "");
            ItEnergyCurrent itEnergyCurrent = new ItEnergyCurrent();
            itEnergyCurrent.setItEnergyId(uuid);
            String allElectricMeter = this.getCellValueToString(row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getCellValueToInteger(row, 14);
            } else {
                setYear = localYear;
            }
            itEnergyCurrent.setYear(setYear);
            itEnergyCurrent.setMonth(i - 1);
            itEnergyCurrent.setDataName(dataName);
            itEnergyCurrent.setName(name);
            itEnergyCurrent.setModule(ItModuleAndDataEnum.getModuleByData(dataName));
            itEnergyCurrent.setType(ItTypeAndDataEnum.getTypeByData(dataName));
            itEnergyCurrent.setAllElectricMeter(meter);
            Long nowDate = System.currentTimeMillis();
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            itEnergyCurrent.setDataCollectionTime(collectionTime);
            itEnergyCurrent.setCreateTime(nowDate);
            itEnergyCurrentList.add(itEnergyCurrent);
        }
        dto.setItEnergyCurrentList(itEnergyCurrentList);

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
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getItEnergyCurrentMergedRegion(Sheet sheet, int row, ItEnergyCurrentImportDto dto) {
        String dataName = this.getNotNullMergedCellValueToString(sheet, row, 0);
        String name = this.getNotNullMergedCellValueToString(sheet, row, 1);
        //获取年份
        String year = this.getMergedCellValueToString(sheet, row, 14);
        Long collectionTime = 0L;

        if (ObjectUtils.isEmpty(dataName) && ObjectUtils.isEmpty(name)) {
            dto.setIsReturn(1);
            return;
        }

        double meter = 0;
        //年份
        Calendar date = Calendar.getInstance();
        int localYear = date.get(Calendar.YEAR);
        //月份
        int month = 0;
        List<ItEnergyCurrent> itEnergyCurrentList = new ArrayList<>();
        for (int i = 2 ; i <= 13; i++) {
            month = i - 1;
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-", "");
            String allElectricMeter = this.getMergedCellValueToString(sheet, row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getMergedCellValueToInteger(sheet, row, 14);
            } else {
                setYear = localYear;
            }
            ItEnergyCurrent itEnergyCurrent = new ItEnergyCurrent();
            itEnergyCurrent.setYear(setYear);
            itEnergyCurrent.setMonth(i - 1);
            itEnergyCurrent.setItEnergyId(uuid);
            itEnergyCurrent.setDataName(dataName);
            itEnergyCurrent.setName(name);
            itEnergyCurrent.setAllElectricMeter(meter);
            itEnergyCurrent.setModule(ItModuleAndDataEnum.getModuleByData(dataName));
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            itEnergyCurrent.setType(ItTypeAndDataEnum.getTypeByData(dataName));
            Long nowDate = System.currentTimeMillis();
            itEnergyCurrent.setDataCollectionTime(collectionTime);
            itEnergyCurrent.setCreateTime(nowDate);
            itEnergyCurrentList.add(itEnergyCurrent);
        }
        dto.setItEnergyCurrentList(itEnergyCurrentList);
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
