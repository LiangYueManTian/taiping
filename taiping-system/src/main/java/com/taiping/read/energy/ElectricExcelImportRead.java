package com.taiping.read.energy;

import com.taiping.bean.energy.dto.ElectricInstrumentImportDto;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.energy.ElectricInstrument;
import com.taiping.enums.energy.ElectricSheetEnum;
import com.taiping.enums.energy.ElectricSheetStartRowEnum;
import com.taiping.enums.energy.ElectricTypeAndDataEnum;
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
import java.util.UUID;

/**
 * 总能耗数据解析
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class ElectricExcelImportRead extends AbstractExcelImportRead {

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
        if (ElectricSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            ElectricInstrumentImportDto electricImportDto = new ElectricInstrumentImportDto();
            getElectricInstrument(row, electricImportDto);
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
        if (ElectricSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(ElectricSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        ElectricInstrumentImportDto electricImportDto = new ElectricInstrumentImportDto();
        if (ElectricSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            getElectricInstrumentMergedRegion(sheet, row, electricImportDto);
        } else {
            throw new RuntimeException();
        }
        return electricImportDto;
    }

    /**
     * 业务数据转换成存储数据
     */
    private void getElectricInstrument(Row row, ElectricInstrumentImportDto electricImportDto) {
        String dataName = this.getCellValueToString(row, 0);
        String name = this.getCellValueToString(row, 1);
        String electricMeter = this.getCellValueToString(row, 2);
        //获取年份
        String year = this.getCellValueToString(row, 16);
        if (ObjectUtils.isEmpty(dataName) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(electricMeter)) {
            electricImportDto.setIsReturn(1);
            return;
        }

        double meter = 0;
        //年份
        Calendar date = Calendar.getInstance();
        int localYear = date.get(Calendar.YEAR);
        //月份
        int month = 0;
        Long collectionTime = 0L;
        List<ElectricInstrument> electricInstrumentList = new ArrayList<>();
        for (int i = 3 ; i <= 14; i++) {
            month = i - 2;
            String uuid = NineteenUUIDUtils.uuid();
            ElectricInstrument electricInstrument = new ElectricInstrument();
            electricInstrument.setElectricInstrumentId(uuid);
            String allElectricMeter = this.getCellValueToString(row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getCellValueToInteger(row, 16);
            } else {
                setYear = localYear;
            }
            electricInstrument.setYear(setYear);
            electricInstrument.setMonth(month);
            electricInstrument.setDataName(dataName);
            electricInstrument.setElectricMeter(electricMeter);
            //类型
            electricInstrument.setType(ElectricTypeAndDataEnum.getTypeByData(name));
            electricInstrument.setName(name);
            electricInstrument.setDataCode(name);
            electricInstrument.setAllElectricMeter(meter);
            Long nowDate = System.currentTimeMillis();
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            electricInstrument.setDataCollectionTime(collectionTime);
            electricInstrument.setCreateTime(nowDate);
            electricInstrumentList.add(electricInstrument);
        }
        electricImportDto.setElectricInstrumentList(electricInstrumentList);
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
    private void getElectricInstrumentMergedRegion(Sheet sheet, int row, ElectricInstrumentImportDto dto) {
        String dataName = this.getNotNullMergedCellValueToString(sheet, row, 0);
        String name = this.getNotNullMergedCellValueToString(sheet, row, 1);
        String electricMeter = this.getNotNullMergedCellValueToString(sheet, row, 2);
        //获取年份
        String year = this.getMergedCellValueToString(sheet, row, 16);
        Long collectionTime = 0L;

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
        List<ElectricInstrument> electricInstrumentList = new ArrayList<>();
        for (int i = 3 ; i <= 14; i++) {
            month = i - 2;
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-", "");
            String allElectricMeter = this.getMergedCellValueToString(sheet, row, i);
            if (!ObjectUtils.isEmpty(allElectricMeter)) {
                meter = Double.valueOf(allElectricMeter);
            }
            int setYear = 0;
            if (!ObjectUtils.isEmpty(year)) {
                setYear = this.getMergedCellValueToInteger(sheet, row, 16);
            } else {
                setYear = localYear;
            }
            ElectricInstrument electricInstrument = new ElectricInstrument();
            electricInstrument.setYear(setYear);
            electricInstrument.setMonth(month);
            electricInstrument.setElectricInstrumentId(uuid);
            electricInstrument.setDataName(dataName);
            electricInstrument.setElectricMeter(electricMeter);
            //类型
            electricInstrument.setType(ElectricTypeAndDataEnum.getTypeByData(name));
            electricInstrument.setName(name);
            electricInstrument.setDataCode(name);
            electricInstrument.setAllElectricMeter(meter);
            collectionTime = DateUtil.generateCollectionTime(month, setYear, collectionTime);
            Long nowDate = System.currentTimeMillis();
            electricInstrument.setDataCollectionTime(collectionTime);
            electricInstrument.setCreateTime(nowDate);
            electricInstrumentList.add(electricInstrument);
        }
        dto.setElectricInstrumentList(electricInstrumentList);
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
