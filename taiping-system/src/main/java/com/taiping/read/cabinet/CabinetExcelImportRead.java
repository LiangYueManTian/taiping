package com.taiping.read.cabinet;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabinet.Cabinet;
import com.taiping.enums.cabinet.CabinetSheetEnum;
import com.taiping.enums.cabinet.CabinetSheetStartRowEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.common.CalculateUtil;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.UUID;

/**
 * 导入机柜Excel数据类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class CabinetExcelImportRead extends AbstractExcelImportRead {



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
        if (CabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            Cabinet cabinet = new Cabinet();
            getCabinet(row, cabinet);
            return cabinet;
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
        if (CabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(CabinetSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        if (CabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            Cabinet cabinet = new Cabinet();
            getCabinetMergedRegion(sheet, row, cabinet);
            return cabinet;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getCabinet(Row row, Cabinet cabinet) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        cabinet.setCabinetId(uuid);
        String cabinetUniqueName = this.getNotNullCellValueToString(row, 2);
        String floor = "";
        String cabinetColumn = "";
        String cabinetName = this.getNotNullCellValueToString(row, 1);
        if (!ObjectUtils.isEmpty(cabinetUniqueName)) {
            String [] cabinetUniqueNameList = cabinetUniqueName.split("-");
            if (cabinetUniqueNameList.length == 3) {
                floor = cabinetUniqueNameList[0];
                cabinetColumn = floor + "-" + cabinetUniqueNameList[1];
            } else {
                throw new RuntimeException();
            }
        }
        cabinet.setCabinetUniqueName(cabinetUniqueName);
        cabinet.setFloor(floor);
        cabinet.setCabinetColumn(cabinetColumn);
        cabinet.setCabinetName(cabinetName);
        cabinet.setCabinetLocation(this.getNotNullCellValueToString(row, 3));
        Integer designSpaceCapacity = this.getNotNullCellValueToInteger(row, 4);
        Integer usedSpaceCapacity = this.getNotNullCellValueToInteger(row, 5);
        Integer unusedSpaceCapacity = designSpaceCapacity - usedSpaceCapacity;
        cabinet.setDesignSpaceCapacity(designSpaceCapacity);
        cabinet.setUsedSpaceCapacity(usedSpaceCapacity);
        cabinet.setUnusedSpaceCapacity(unusedSpaceCapacity);
        double usedSpaceCapacityPercent = CalculateUtil.castPercent(usedSpaceCapacity, designSpaceCapacity);
        cabinet.setUsedSpaceCapacityPercent(usedSpaceCapacityPercent);
        cabinet.setRatedPower(this.getNotNullCellValueToDouble(row, 6));
        cabinet.setUsedRatedPower(this.getNotNullCellValueToDouble(row, 7));
        cabinet.setUsedRatedPercent(this.getNotNullCellValueToDouble(row, 8));
        cabinet.setUsedActualPower(this.getNotNullCellValueToDouble(row, 9));
        cabinet.setUsedActualPercent(this.getNotNullCellValueToDouble(row, 10));
        Long nowDate = System.currentTimeMillis();
        Calendar nowDateInfo = Calendar.getInstance();
        //月份
        cabinet.setMonth(nowDateInfo.get(Calendar.MONTH) + 1);
        //年份
        cabinet.setYear(nowDateInfo.get(Calendar.YEAR));
        Long collectionTime = 0L;
        collectionTime = DateUtil.generateCollectionTime(cabinet.getMonth(), cabinet.getYear(), collectionTime);
        cabinet.setDataCollectionTime(collectionTime);
        cabinet.setCreateTime(nowDate);
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
        value = this.checkStringNull(value);
        return Double.valueOf(value);
    }


    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getCabinetMergedRegion(Sheet sheet, int row, Cabinet cabinet) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        cabinet.setCabinetId(uuid);
        String cabinetUniqueName = this.getNotNullMergedCellValueToString(sheet, row, 2);
        String floor = "";
        String cabinetColumn = "";
        String cabinetName = this.getNotNullMergedCellValueToString(sheet, row, 1);
        if (!ObjectUtils.isEmpty(cabinetUniqueName)) {
            String [] cabinetUniqueNameList = cabinetUniqueName.split("-");
            if (cabinetUniqueNameList.length == 3) {
                floor = cabinetUniqueNameList[0];
                cabinetColumn = floor + "-" + cabinetUniqueNameList[1];
            } else {
                throw new RuntimeException();
            }
        }
        cabinet.setCabinetUniqueName(cabinetUniqueName);
        cabinet.setFloor(floor);
        cabinet.setCabinetColumn(cabinetColumn);
        cabinet.setCabinetName(cabinetName);
        cabinet.setCabinetLocation(this.getNotNullMergedCellValueToString(sheet, row, 3));
        Integer designSpaceCapacity = this.getNotNullMergedCellValueToInteger(sheet, row, 4);
        Integer usedSpaceCapacity = this.getNotNullMergedCellValueToInteger(sheet, row, 5);
        Integer unusedSpaceCapacity = designSpaceCapacity - usedSpaceCapacity;
        cabinet.setDesignSpaceCapacity(designSpaceCapacity);
        cabinet.setUsedSpaceCapacity(usedSpaceCapacity);
        cabinet.setUnusedSpaceCapacity(unusedSpaceCapacity);
        double usedSpaceCapacityPercent = CalculateUtil.castPercent(usedSpaceCapacity, designSpaceCapacity);
        cabinet.setUsedSpaceCapacityPercent(usedSpaceCapacityPercent);
        cabinet.setRatedPower(this.getNotNullMergedCellValueToDouble(sheet, row, 6));
        cabinet.setUsedRatedPower(this.getNotNullMergedCellValueToDouble(sheet, row, 7));
        cabinet.setUsedRatedPercent(this.getNotNullMergedCellValueToDouble(sheet, row, 8));
        cabinet.setUsedActualPower(this.getNotNullMergedCellValueToDouble(sheet, row, 9));
        cabinet.setUsedActualPercent(this.getNotNullMergedCellValueToDouble(sheet, row, 10));
        Long nowDate = System.currentTimeMillis();
        Long collectionTime = 0L;
        Calendar nowDateInfo = Calendar.getInstance();
        //月份
        cabinet.setMonth(nowDateInfo.get(Calendar.MONTH) + 1);
        //年份
        cabinet.setYear(nowDateInfo.get(Calendar.YEAR));
        collectionTime = DateUtil.generateCollectionTime(cabinet.getMonth(), cabinet.getYear(), collectionTime);
        cabinet.setDataCollectionTime(collectionTime);
        cabinet.setCreateTime(nowDate);
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
