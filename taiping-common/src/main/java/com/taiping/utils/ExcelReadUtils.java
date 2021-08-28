package com.taiping.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 读取Excel数据工具类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
public class ExcelReadUtils {

    /**
     * 获取单元格的值
     * @param sheet sheet页数据
     * @param row 行下标
     * @param column 列下标
     * @return 单元格的值
     */
    public static Object getMergedRegionValue(Sheet sheet , int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();
        Row fRow;
        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    fRow = sheet.getRow(firstRow);
                    return getCellValue(fRow.getCell(firstColumn));
                }
            }
        }
        fRow = sheet.getRow(row);
        return getCellValue(fRow.getCell(column)) ;
    }


    /**
     * poi 获取单元格的值
     * @param cell 单元格
     * @return 单元格的值
     */
    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getRichStringCellValue().getString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//            return cell.getCellFormula();
            try {
                return cell.getNumericCellValue();
            } catch (IllegalStateException e) {
                return cell.getRichStringCellValue();
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            return null;
        }
    }

    /**
     * poi 获取单元格的值
     * @param cell 单元格
     * @return 单元格的值
     */
    public static String getCellValueToString(Cell cell) {
        Object object = getCellValue(cell);
        if (object == null) {
            return null;
        }
        return String.valueOf(object);
    }
    /**
     * 获取单元格的值
     * @param sheet sheet页数据
     * @param row 行下标
     * @param column 列下标
     * @return 单元格的值
     */
    public static String getMergedRegionValueToString(Sheet sheet , int row , int column) {
        Object object = getMergedRegionValue(sheet, row, column);
        if (object == null) {
            return null;
        }
        return String.valueOf(object);
    }
}
