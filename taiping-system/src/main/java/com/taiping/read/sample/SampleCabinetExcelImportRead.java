package com.taiping.read.sample;


import com.taiping.entity.SampleCabinet;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.SampleIntegratedWiring;
import com.taiping.enums.SampleCabinetSheetEnum;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

/**
 * 导入机柜Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
@Component
public class SampleCabinetExcelImportRead extends AbstractExcelImportRead {

    private static final int CABINET_BEGIN_ROW = 4;

    private static final int INTEGRATED_WIRING_BEGIN_ROW = 3;

    private static final int SHEET_NUMBER = 2;


    /**
     * 校验sheet页数量
     *
     * @param numberOfSheets sheet页数量
     */
    @Override
    protected void checkSheetNumber(int numberOfSheets) {
        if (numberOfSheets != SHEET_NUMBER) {
            throw new RuntimeException();
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
        if (SampleCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            SampleCabinet sampleCabinet = new SampleCabinet();
            getCabinet(row, sampleCabinet);
            return sampleCabinet;
        } else if (SampleCabinetSheetEnum.SHEET_TWO.getSheetName().equals(sheetName)) {
            SampleIntegratedWiring sampleIntegratedWiring = new SampleIntegratedWiring();
            getIntegratedWiring(row, sampleIntegratedWiring);
            return sampleIntegratedWiring;
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
        if (SampleCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(CABINET_BEGIN_ROW);
        } else if (SampleCabinetSheetEnum.SHEET_TWO.getSheetName().equals(sheetName)) {
            excelImportReadBean.setBegin(INTEGRATED_WIRING_BEGIN_ROW);
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
        if (SampleCabinetSheetEnum.SHEET_ONE.getSheetName().equals(sheetName)) {
            SampleCabinet sampleCabinet = new SampleCabinet();
            getCabinetMergedRegion(sheet, row, sampleCabinet);
            return sampleCabinet;
        } else if (SampleCabinetSheetEnum.SHEET_TWO.getSheetName().equals(sheetName)) {
            SampleIntegratedWiring sampleIntegratedWiring = new SampleIntegratedWiring();
            getIntegratedWiringMergedRegion(sheet, row, sampleIntegratedWiring);
            return sampleIntegratedWiring;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getCabinet(Row row, SampleCabinet sampleCabinet) {
        sampleCabinet.setCabinetCode(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(0))));
        sampleCabinet.setFloor(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(1))));
        sampleCabinet.setCabinetRow(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(2))));
        sampleCabinet.setCabinetName(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(3))));
        sampleCabinet.setType(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(4))));
        sampleCabinet.setPracticalCapacity(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(5))));
        sampleCabinet.setOccupiedCapacity(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(6))));
        sampleCabinet.setSpareCapacity(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(7))));
        sampleCabinet.setOccupiedCapacityPercentage(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(8))));
        sampleCabinet.setPracticalElectricPower(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(9))));
        sampleCabinet.setOccupiedElectricPower(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(10))));
        sampleCabinet.setSpareElectricPower(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(11))));
        sampleCabinet.setOccupiedElectricPowerPercentage(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(12))));
    }
    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getCabinetMergedRegion(Sheet sheet, int row, SampleCabinet sampleCabinet) {
        sampleCabinet.setCabinetCode(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,0)));
        sampleCabinet.setFloor(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,1)));
        sampleCabinet.setCabinetRow(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,2)));
        sampleCabinet.setCabinetName(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,3)));
        sampleCabinet.setType(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,4)));
        sampleCabinet.setPracticalCapacity(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,5)));
        sampleCabinet.setOccupiedCapacity(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,6)));
        sampleCabinet.setSpareCapacity(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,7)));
        sampleCabinet.setOccupiedCapacityPercentage(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,8)));
        sampleCabinet.setPracticalElectricPower(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,9)));
        sampleCabinet.setOccupiedElectricPower(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,10)));
        sampleCabinet.setSpareElectricPower(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,11)));
        sampleCabinet.setOccupiedElectricPowerPercentage(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,12)));
    }
    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getIntegratedWiring(Row row, SampleIntegratedWiring sampleIntegratedWiring) {
        sampleIntegratedWiring.setOdfName(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(0))));
        sampleIntegratedWiring.setPortName(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(1))));
        sampleIntegratedWiring.setOdfCode(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(2))));
        sampleIntegratedWiring.setOdfPlace(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(3))));
        sampleIntegratedWiring.setBaseWiring(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(4))));
        sampleIntegratedWiring.setEndOdfName(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(5))));
        sampleIntegratedWiring.setEndOdfPort(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(6))));
        sampleIntegratedWiring.setEndOdfPlace(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(7))));
        sampleIntegratedWiring.setStatus(String.valueOf(ExcelReadUtils.getCellValue(row.getCell(8))));
    }
    /**
     * //TODO 根据需求添加参数校验,业务数据转换成存储数据
     */
    private void getIntegratedWiringMergedRegion(Sheet sheet, int row, SampleIntegratedWiring sampleIntegratedWiring) {
        sampleIntegratedWiring.setOdfName(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,0)));
        sampleIntegratedWiring.setPortName(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,1)));
        sampleIntegratedWiring.setOdfCode(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,2)));
        sampleIntegratedWiring.setOdfPlace(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,3)));
        sampleIntegratedWiring.setBaseWiring(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,4)));
        sampleIntegratedWiring.setEndOdfName(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,5)));
        sampleIntegratedWiring.setEndOdfPort(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,6)));
        sampleIntegratedWiring.setEndOdfPlace(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,7)));
        sampleIntegratedWiring.setStatus(String.valueOf(ExcelReadUtils.getMergedRegionValue(sheet, row,8)));
    }
}
