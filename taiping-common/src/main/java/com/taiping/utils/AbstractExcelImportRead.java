package com.taiping.utils;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入Excel数据抽象类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-05
 */
public abstract class AbstractExcelImportRead {

    /**
     * 解析Excel
     * @param file Excel文件
     * @return 解析数据
     * @throws IOException 文件异常
     * @throws InvalidFormatException 文件读取异常
     */
    public Map<String, List<ExcelReadBean>> readExcel(MultipartFile file) throws IOException, InvalidFormatException {
        Map<String, List<ExcelReadBean>>  excelResult = new HashMap<>(16);
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        int numberOfSheets = workbook.getNumberOfSheets();
        checkSheetNumber(numberOfSheets);
        for (int i = 0; i < numberOfSheets; i++) {
            List<ExcelReadBean> excelReadBeans = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = String.valueOf(i);
            if (sheet != null) {
                ExcelImportReadBean importReadBean = getBeginAndEndRow(sheetName);
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                if (importReadBean.getBegin() != null) {
                    firstRowNum = importReadBean.getBegin();
                }
                if (importReadBean.getEnd() != null) {
                    lastRowNum = importReadBean.getEnd();
                }
                if (firstRowNum <= lastRowNum) {
                    for (int j = firstRowNum; j <= lastRowNum; j++) {
                        Row row = sheet.getRow(j);
                        if (row == null) {
                            continue;
                        }
                        ExcelReadBean excelReadBean;
                        if (hasMergedRegion(sheet, j)) {
                            excelReadBean = getBeanHaveMergedRegion(sheet, sheetName, j);
                        } else {
                            excelReadBean = getBean(sheetName, row);
                        }
                        //用户想自定义跳出某个sheet页的数据的时候
                        if (!ObjectUtils.isEmpty(excelReadBean) && !ObjectUtils.isEmpty(excelReadBean.getIsReturn())) {
                            break;
                        }
                        //改行是数据行
                        if (excelReadBean != null) {
                            excelReadBeans.add(excelReadBean);
                        }
                    }
                }
            }
            excelResult.put(sheetName, excelReadBeans);
        }
        return excelResult;
    }

    /**
     * 校验sheet页数量
     * @param numberOfSheets sheet页数量
     */
    protected abstract void checkSheetNumber(int numberOfSheets);
    /**
     * 行数据转实体
     * @param sheetName sheet页标识
     * @param row 行数据
     * @return 实体
     */
    protected abstract ExcelReadBean getBean(String sheetName, Row row);

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    protected abstract ExcelImportReadBean getBeginAndEndRow(String sheetName);

    /**
     * 行数据转实体
     * @param sheet sheet页数据
     * @param sheetName sheet页标识
     * @param row 行标识
     * @return 实体
     */
    protected abstract ExcelReadBean getBeanHaveMergedRegion(Sheet sheet, String sheetName, int row);


    /**
     * 判断指定的行是否是有合并单元格
     * @param sheet sheet页数据
     * @param row 行下标
     * @return true有合并单元格 false没有合并单元格
     */
    private  boolean hasMergedRegion(Sheet sheet, int row) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                return true;
            }
        }
        return false;
    }
}
