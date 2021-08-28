package com.taiping.read.cabling;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabling.GenericCabling;
import com.taiping.enums.cabinet.CableTypeEnum;
import com.taiping.enums.cabling.GenericCablingStatusEnum;
import com.taiping.enums.cabling.GenericCablingSheetStartRowEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.common.analyze.capacity.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.UUID;

/**
 * 导入综合布线Excel数据类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class GenericCablingExcelImportRead extends AbstractExcelImportRead {



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
        GenericCabling genericCabling = new GenericCabling();
        getGenericCabling(row, genericCabling);
        return genericCabling;
    }

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean excelImportReadBean = new ExcelImportReadBean();
        excelImportReadBean.setBegin(GenericCablingSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        GenericCabling genericCabling = new GenericCabling();
        getGenericCablingMergedRegion(sheet, row, genericCabling);
        return genericCabling;
    }

    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getGenericCabling(Row row, GenericCabling genericCabling) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        genericCabling.setGenericCablingId(uuid);
        String cableType = this.getNotNullCellValueToString(row, 1);
        String cableTypeCode = CableTypeEnum.getCableTypeByName(cableType);
        genericCabling.setCableType(cableTypeCode);
        genericCabling.setGenericCablingType(this.getNotNullCellValueToString(row, 2));
        genericCabling.setCabinetUniqueName(this.getNotNullCellValueToString(row, 3));
        genericCabling.setConnectRackCode(this.getNotNullCellValueToString(row, 4));
        genericCabling.setUsedWeight(this.getNotNullCellValueToInteger(row, 5));
        genericCabling.setLocation(this.getCellValueToString(row, 6));
        genericCabling.setPort(this.getNotNullCellValueToString(row, 7));
        genericCabling.setOppositeEndCabinetName(this.getNotNullCellValueToString(row, 8));
        genericCabling.setOppositeEndRackCode(this.getNotNullCellValueToString(row, 9));
        genericCabling.setOppositeUsedWeight(this.getNotNullCellValueToInteger(row, 10));
        genericCabling.setOppositeLocation(this.getCellValueToString(row, 11));
        genericCabling.setOppositeEndPort(this.getNotNullCellValueToString(row, 12));
        genericCabling.setExtent(this.getCellValueToInteger(row, 13));
        String status = this.getNotNullCellValueToString(row, 14);
        String statusCode = GenericCablingStatusEnum.getStatusByName(status);
        genericCabling.setStatus(statusCode);
        Long nowDate = System.currentTimeMillis();
        Long collectionTime = 0L;
        Calendar nowDateInfo = Calendar.getInstance();
        //月份
        genericCabling.setMonth(nowDateInfo.get(Calendar.MONTH) + 1);
        //年份
        genericCabling.setYear(nowDateInfo.get(Calendar.YEAR));
        collectionTime = DateUtil.generateCollectionTime(genericCabling.getMonth(), genericCabling.getYear(), collectionTime);
        genericCabling.setDataCollectionTime(collectionTime);
        genericCabling.setCreateTime(nowDate);
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
        if (!ObjectUtils.isEmpty(value)) {
            value = value.split("[.]")[0];
        }
        if (!ObjectUtils.isEmpty(value)) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }

    /**
     * 获取不为空Integer类型的列元素
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
        if (!ObjectUtils.isEmpty(value)) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
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
        if (!ObjectUtils.isEmpty(value)) {
            value = this.checkStringNull(value);
            return Integer.parseInt(value);
        } else {
            return null;
        }
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
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getGenericCablingMergedRegion(Sheet sheet, int row, GenericCabling genericCabling) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        genericCabling.setGenericCablingId(uuid);
        String cableType = this.getNotNullMergedCellValueToString(sheet, row , 1);
        String cableTypeCode = CableTypeEnum.getCableTypeByName(cableType);
        genericCabling.setCableType(cableTypeCode);
        genericCabling.setGenericCablingType(this.getNotNullMergedCellValueToString(sheet, row , 2));
        genericCabling.setCabinetUniqueName(this.getNotNullMergedCellValueToString(sheet, row , 3));
        genericCabling.setConnectRackCode(this.getNotNullMergedCellValueToString(sheet, row , 4));
        genericCabling.setUsedWeight(this.getNotNullMergedCellValueToInteger(sheet, row, 5));
        genericCabling.setLocation(this.getMergedCellValueToString(sheet, row , 6));
        genericCabling.setPort(this.getNotNullMergedCellValueToString(sheet, row , 7));
        genericCabling.setOppositeEndCabinetName(this.getNotNullMergedCellValueToString(sheet, row , 8));
        genericCabling.setOppositeEndRackCode(this.getNotNullMergedCellValueToString(sheet, row , 9));
        genericCabling.setOppositeUsedWeight(this.getNotNullMergedCellValueToInteger(sheet, row, 10));
        genericCabling.setOppositeLocation(this.getMergedCellValueToString(sheet, row , 11));
        genericCabling.setOppositeEndPort(this.getNotNullMergedCellValueToString(sheet, row , 12));
        genericCabling.setExtent(this.getMergedCellValueToInteger(sheet, row, 13));
        String status = this.getNotNullMergedCellValueToString(sheet, row , 14);
        String statusCode = GenericCablingStatusEnum.getStatusByName(status);
        genericCabling.setStatus(statusCode);
        Long nowDate = System.currentTimeMillis();
        Long collectionTime = 0L;
        Calendar nowDateInfo = Calendar.getInstance();
        //月份
        genericCabling.setMonth(nowDateInfo.get(Calendar.MONTH) + 1);
        //年份
        genericCabling.setYear(nowDateInfo.get(Calendar.YEAR));
        collectionTime = DateUtil.generateCollectionTime(genericCabling.getMonth(), genericCabling.getYear(), collectionTime);
        genericCabling.setDataCollectionTime(collectionTime);
        genericCabling.setCreateTime(nowDate);
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
