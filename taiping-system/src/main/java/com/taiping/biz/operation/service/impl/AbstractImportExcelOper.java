package com.taiping.biz.operation.service.impl;

import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.ExcelReadUtils;
import com.taiping.utils.ReadCellUtil;
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
 * 物业沟通-配电系统运行情况分析
 * 该类 只针对 4个sheet处理
 *
 * @author: liyj
 * @date: 2019/12/10 16:28
 **/
public abstract class AbstractImportExcelOper extends AbstractExcelImportRead {

    /**
     * 获取 范围数据
     *
     * @param sheet
     * @param sheetName
     * @param row
     * @return
     */
    protected abstract ExcelReadBean getRangeBean(Sheet sheet, String sheetName, int row);

    /**
     * 获取组合数据
     *
     * @param sheet
     * @param sheetName
     * @param row
     * @param rangeBean
     * @return
     */
    protected abstract ExcelReadBean getBeanHaveMergedRegion(Sheet sheet, String sheetName, int row, ExcelReadBean rangeBean);

    /**
     * 行数据转实体
     *
     * @param sheetName sheet页标识
     * @param row       行数据
     * @return 实体
     */
    protected abstract ExcelReadBean getBean(String sheetName, Row row, ExcelReadBean rangeBean);


    /**
     * 解析运行情况-物业沟通-配电运行情况
     *
     * @return Map<sheetName,List<ExcelReadBean>>
     * @throws IOException
     * @throws InvalidFormatException
     */
    public Map<String, List<ExcelReadBean>> readPowerExcel(MultipartFile file) throws IOException, InvalidFormatException {
        /***
         *  这里的解析分为2步
         *  1 解析范围值
         *  2 解析里面的数据
         *  校验逻辑
         *  根据sheetName 来处理返回不同的Bean
         *
         */
        InputStream inputStream = file.getInputStream();
        Map<String, List<ExcelReadBean>> excelResult = new HashMap<>(16);
        // 创建workbook 对象
        Workbook workbook = WorkbookFactory.create(inputStream);
        int sheetTotal = workbook.getNumberOfSheets();
        // 每个sheet页 处理
        for (int i = 0; i < sheetTotal; i++) {
            // 获取数据
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            String sheetName = sheet.getSheetName();
            // 这里校验是否
            excelResult.put(sheetName, readExcelBySheetName(sheet, sheetName));
        }
        return excelResult;
    }

    /**
     * 解析ExcelDataBySheet
     *
     * @param sheet 整个sheet 对象
     * @return
     */
    private List<ExcelReadBean> readExcelBySheetName(Sheet sheet, String sheetName) {
        List<ExcelReadBean> excelReadBeans = new ArrayList<>();
        // 根据sheetName 获取开始行数 最后行 需要判断 col 序号列为空 则退出循环
        // 默认第一行均为范围行
        ExcelImportReadBean importReadBean = getBeginAndEndRow(sheetName);
        // 获取范围行数据
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        if (importReadBean.getBegin() != null) {
            firstRowNum = importReadBean.getBegin();
        }
        if (importReadBean.getEnd() != null) {
            lastRowNum = importReadBean.getEnd();
        }

        // 获取范围数据
        //获取Excel 中的数据
        if (firstRowNum + 1 <= lastRowNum) {
            for (int j = firstRowNum + 1; j <= lastRowNum; j++) {
                // 这里判断新的一行 序号是否为空
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                String cellString = ReadCellUtil.getCellString(row, 0);
                if (cellString != null && cellString.contains("备注")) {
                    // 柴发最后一格为备注 不需要读取
                    continue;

                }
                // 是否合并单元格 取序号
                boolean mergeFlag = hasMergedRegion(sheet, j);
                Integer orderNum = null;
                if (mergeFlag) {
                    String value = ExcelReadUtils.getMergedRegionValueToString(sheet, j, 0);
                    orderNum = value == null ? 0 : new Double(value).intValue();
                } else {
                    String string = ReadCellUtil.getCellString(row, 0);
                    orderNum = string == null ? 0 : new Double(string).intValue();
                }
                if (orderNum == null || orderNum == 0) {
                    continue;
                }

                // 将范围值 塞到Bean 里去
                ExcelReadBean rangeBean = getRangeBean(sheet, sheetName, firstRowNum);
                ExcelReadBean excelReadBean;
                // 这里 判断 是否有合并单元格的 有合并单元格的 进行处理
                if (mergeFlag) {
                    // 含有合并d单元格 获取 row 行数
                    excelReadBean = getBeanHaveMergedRegion(sheet, sheetName, j, rangeBean);
                } else {
                    excelReadBean = getBean(sheetName, row, rangeBean);
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
        return excelReadBeans;
    }


    /**
     * 判断指定的行是否是有合并单元格
     *
     * @param sheet sheet页数据
     * @param row   行下标
     * @return true有合并单元格 false没有合并单元格
     */
    private boolean hasMergedRegion(Sheet sheet, int row) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                return true;
            }
        }
        return false;
    }


}
