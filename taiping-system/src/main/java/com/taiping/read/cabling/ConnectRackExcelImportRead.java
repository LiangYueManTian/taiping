package com.taiping.read.cabling;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabling.ConnectRack;
import com.taiping.enums.cabling.ConnectRackSheetStartRowEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

/**
 * 导入配线架Excel数据类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class ConnectRackExcelImportRead extends AbstractExcelImportRead {



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
        ConnectRack connectRack = new ConnectRack();
        getConnectRack(row, connectRack);
        return connectRack;
    }

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean excelImportReadBean = new ExcelImportReadBean();
        excelImportReadBean.setBegin(ConnectRackSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        ConnectRack connectRack = new ConnectRack();
        getConnectRackMergedRegion(sheet, row, connectRack);
        return connectRack;
    }

    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getConnectRack(Row row, ConnectRack connectRack) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        connectRack.setConnectRackId(uuid);
        connectRack.setConnectRackCode(this.getNotNullCellValueToString(row, 1));
        connectRack.setEicNumber(this.getCellValueToString(row, 2));
        connectRack.setSerialNumber(this.getNotNullCellValueToString(row, 3));
        connectRack.setConnectRackName(this.getNotNullCellValueToString(row, 4));
        connectRack.setDescription(this.getCellValueToString(row, 5));
        connectRack.setManufacture(this.getNotNullCellValueToString(row, 6));
        connectRack.setLogo(this.getNotNullCellValueToString(row, 7));
        connectRack.setSerialInfo(this.getNotNullCellValueToString(row, 8));
        connectRack.setType(this.getNotNullCellValueToString(row, 9));
        connectRack.setStatus(this.getNotNullCellValueToString(row, 10));
        connectRack.setLocation(this.getCellValueToString(row, 11));
        connectRack.setProject(this.getCellValueToString(row, 12));
        connectRack.setDepartment(this.getCellValueToString(row, 13));
        connectRack.setOwner(this.getCellValueToString(row, 14));
        connectRack.setWeight(this.getCellValueToDouble(row, 15));
        connectRack.setUsedWeight(this.getNotNullCellValueToInteger(row, 16));
        connectRack.setAssetCode(this.getNotNullCellValueToString(row, 17));
        connectRack.setRackingTime(this.getCellValueToLong(row, 18));
        Long nowDate = System.currentTimeMillis();
        connectRack.setDataCollectionTime(nowDate);
        connectRack.setCreateTime(nowDate);
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
     * 获取不为空的Integer类型的列元素
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
     * 获取Long类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param,cellNumber 一行中的某个单元格
     * @return 列的值
     */
    public Long getCellValueToLong(Row row, int cellNumber) {
        String value = ExcelReadUtils.getCellValueToString(row.getCell(cellNumber));
        if (!ObjectUtils.isEmpty(value)) {
            return Long.valueOf(value);
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
        return Integer.parseInt(value);
    }

    /**
     * 获取不为空的Integer类型的列元素
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
     * 获取Long类型的列元素
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 10:30
     * @param row 行
     * @param cellNumber 一行中的某个单元格
     * @param sheet sheet页
     * @return 列的值
     */
    public Long getMergedCellValueToLong(Sheet sheet, int row, int cellNumber) {
        String value = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNumber);
        if (!ObjectUtils.isEmpty(value)) {
            return Long.valueOf(value);
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
    private void getConnectRackMergedRegion(Sheet sheet, int row, ConnectRack connectRack) {

        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        connectRack.setConnectRackId(uuid);
        connectRack.setConnectRackCode(this.getNotNullMergedCellValueToString(sheet, row , 1));
        connectRack.setEicNumber(this.getMergedCellValueToString(sheet, row , 2));
        connectRack.setSerialNumber(this.getNotNullMergedCellValueToString(sheet, row , 3));
        connectRack.setConnectRackName(this.getNotNullMergedCellValueToString(sheet, row , 4));
        connectRack.setDescription(this.getMergedCellValueToString(sheet, row , 5));
        connectRack.setManufacture(this.getNotNullMergedCellValueToString(sheet, row , 6));
        connectRack.setLogo(this.getNotNullMergedCellValueToString(sheet, row , 7));
        connectRack.setSerialInfo(this.getNotNullMergedCellValueToString(sheet, row , 8));
        connectRack.setType(this.getNotNullMergedCellValueToString(sheet, row , 9));
        connectRack.setStatus(this.getNotNullMergedCellValueToString(sheet, row , 10));
        connectRack.setLocation(this.getMergedCellValueToString(sheet, row , 11));
        connectRack.setProject(this.getMergedCellValueToString(sheet, row , 12));
        connectRack.setDepartment(this.getMergedCellValueToString(sheet, row , 13));
        connectRack.setOwner(this.getMergedCellValueToString(sheet, row , 14));
        connectRack.setWeight(this.getMergedCellValueToDouble(sheet, row, 15));
        connectRack.setUsedWeight(this.getNotNullMergedCellValueToInteger(sheet, row, 16));
        connectRack.setAssetCode(this.getNotNullMergedCellValueToString(sheet, row , 17));
        connectRack.setRackingTime(this.getMergedCellValueToLong(sheet, row, 18));
        Long nowDate = System.currentTimeMillis();
        connectRack.setDataCollectionTime(nowDate);
        connectRack.setCreateTime(nowDate);
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
