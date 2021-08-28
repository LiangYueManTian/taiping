package com.taiping.read.cabinet;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.cabinet.BaseCabinet;
import com.taiping.enums.cabinet.BaseCabinetSheetEnum;
import com.taiping.enums.cabinet.BaseCabinetSheetStartRowEnum;
import com.taiping.enums.cabinet.DeviceTypeEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

/**
 * 导入机柜Excel数据类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-09-05
 */
@Component
public class BaseCabinetExcelImportRead extends AbstractExcelImportRead {



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
        if (BaseCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            BaseCabinet baseCabinet = new BaseCabinet();
            getBaseCabinet(row, baseCabinet);
            return baseCabinet;
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
        if (BaseCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(BaseCabinetSheetStartRowEnum.SHEET_ONE_START_ROW.getSheetName());
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
        if (BaseCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            BaseCabinet baseCabinet = new BaseCabinet();
            getCabinetMergedRegion(sheet, row, baseCabinet);
            return baseCabinet;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getBaseCabinet(Row row, BaseCabinet baseCabinet) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        baseCabinet.setCabinetBaseId(uuid);
        String cabinetUniqueName = "";
        String floor = this.getNotNullCellValueToString(row, 1);
        String cabinetName = this.getNotNullCellValueToString(row, 2);
        String floorUniqueName = "";
        String cabinetColumn = "";
        //获取机柜列和机柜唯一标识
        baseCabinet = this.getCabinetColumnAndUniqueName(floorUniqueName, cabinetName, floor, cabinetColumn, cabinetUniqueName, baseCabinet);
        baseCabinet.setFloor(floor);
        baseCabinet.setCabinetName(cabinetName);
        String deviceType = this.getNotNullCellValueToString(row, 3);
        String deviceTypeCode = DeviceTypeEnum.getDeviceTypeByName(deviceType);
        baseCabinet.setDeviceType(deviceTypeCode);
        baseCabinet.setRatedPower(this.getNotNullCellValueToDouble(row, 4));
        baseCabinet.setArrayCabinetDesignLoad(this.getNotNullCellValueToDouble(row, 5));
        baseCabinet.setArrayCabinetName(this.getNotNullCellValueToString(row, 6));
        baseCabinet.setElectricReserve(String.valueOf(this.getNotNullCellValueToInteger(row, 7)));
        Long nowDate = System.currentTimeMillis();
        baseCabinet.setDataCollectionTime(nowDate);
        baseCabinet.setCreateTime(nowDate);
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
    private void getCabinetMergedRegion(Sheet sheet, int row, BaseCabinet baseCabinet) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        baseCabinet.setCabinetBaseId(uuid);
        String cabinetUniqueName = "";
        String floor = this.getNotNullMergedCellValueToString(sheet, row, 1);;
        String cabinetName = this.getNotNullMergedCellValueToString(sheet, row, 2);;
        String floorUniqueName = "";
        String cabinetColumn = "";
        //获取机柜列和机柜唯一标识
        baseCabinet = this.getCabinetColumnAndUniqueName(floorUniqueName, cabinetName, floor, cabinetColumn, cabinetUniqueName, baseCabinet);
        baseCabinet.setFloor(floor);
        baseCabinet.setCabinetName(cabinetName);
        String deviceType = this.getNotNullMergedCellValueToString(sheet, row, 3);
        String deviceTypeCode = DeviceTypeEnum.getDeviceTypeByName(deviceType);
        baseCabinet.setDeviceType(deviceTypeCode);
        baseCabinet.setRatedPower(this.getNotNullMergedCellValueToDouble(sheet, row, 4));
        baseCabinet.setArrayCabinetDesignLoad(this.getNotNullMergedCellValueToDouble(sheet, row, 5));
        baseCabinet.setArrayCabinetName(this.getNotNullMergedCellValueToString(sheet, row, 6));
        baseCabinet.setElectricReserve(String.valueOf(this.getMergedCellValueToInteger(sheet, row, 7)));
        Long nowDate = System.currentTimeMillis();
        baseCabinet.setDataCollectionTime(nowDate);
        baseCabinet.setCreateTime(nowDate);



    }

    /**
     * 获取楼层唯一标识
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 16:53
     * @param floor 楼层
     * @return  楼层唯一标识
     */
    public String getFloorUniqueName(String floor) {
        String floorUniqueName = "";
        if (!ObjectUtils.isEmpty(floor)) {
            String floorName = "DC";
            String floorString = floor.split("F")[0];
            Integer floorInteger = Integer.parseInt(floorString) - 1;
            floorUniqueName = floorName + floorInteger;
        }
        return floorUniqueName;
    }

    /**
     * 获取机柜列
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 16:53
     * @param cabinetColumn 机柜列
     * @param cabinetName 机柜名
     * @param floorUniqueName 楼层唯一标识
     * @return  机柜列
     */
    public String getCabinetColumn(String cabinetColumn, String cabinetName, String floorUniqueName) {
        String [] cabinetNameList = cabinetName.split("-");
        if (cabinetNameList.length == 2) {
            cabinetColumn = floorUniqueName + "-" + cabinetNameList[0];
        } else {
            throw new RuntimeException();
        }
        return cabinetColumn;
    }

    /**
     * 获取机柜唯一标识
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 16:53
     * @param cabinetUniqueName 机柜唯一标识
     * @param cabinetName 机柜名
     * @param floorUniqueName 楼层唯一标识
     * @return  机柜唯一标识
     */
    public String getCabinetUniqueName(String cabinetUniqueName ,String cabinetName, String floorUniqueName) {
        if (!ObjectUtils.isEmpty(cabinetName) && !ObjectUtils.isEmpty(floorUniqueName)) {
            String [] cabinetNameList = cabinetName.split("-");
            cabinetUniqueName = floorUniqueName + "-" + cabinetName;
        }
        return cabinetUniqueName;
    }

    /**
     * 机柜信息
     * @author hedongwei@wistronits.com
     * @date  2019/10/11 16:23
     * @param floorUniqueName 楼层唯一标识
     * @param cabinetName 机柜名称
     * @param floor 楼层
     * @param cabinetColumn 机柜列
     * @param cabinetUniqueName 机柜唯一标识
     * @param baseCabinet 基础机柜信息
     * @return 机柜信息
     */
    public BaseCabinet getCabinetColumnAndUniqueName(String floorUniqueName, String cabinetName, String floor, String cabinetColumn, String cabinetUniqueName, BaseCabinet baseCabinet) {
        floorUniqueName = this.getFloorUniqueName(floor);
        cabinetColumn = this.getCabinetColumn(cabinetColumn, cabinetName, floorUniqueName);
        cabinetUniqueName = this.getCabinetUniqueName(cabinetUniqueName , cabinetName, floorUniqueName);
        baseCabinet.setFloorUniqueName(floorUniqueName);
        baseCabinet.setCabinetColumn(cabinetColumn);
        baseCabinet.setCabinetUniqueName(cabinetUniqueName);
        return baseCabinet;
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
