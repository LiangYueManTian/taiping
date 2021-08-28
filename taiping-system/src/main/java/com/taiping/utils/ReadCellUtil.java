package com.taiping.utils;

import com.taiping.constant.CommonResultCode;
import com.taiping.constant.CommonResultMsg;
import com.taiping.constant.DateConstant;
import com.taiping.constant.ReadCellConstant;
import com.taiping.exception.BizException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.ParseException;

/**
 * Excel表格单元格获取工具类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-05
 */
public class ReadCellUtil {

    /**
     * 获取单元格数据
     * @param row 行数据
     * @param cellNum 单元格位置
     * @return 单元格数据
     */
    public static String getCellString(Row row, int cellNum) {
        return ExcelReadUtils.getCellValueToString(row.getCell(cellNum));
    }

    /**
     * 获取单元格数据
     * @param row 行数据
     * @param cellNum 单元格位置
     * @return 单元格数据
     */
    public static String getCellStringNot(Row row, int cellNum) {
        String cellValue = ExcelReadUtils.getCellValueToString(row.getCell(cellNum));
        return checkStringNull(row.getRowNum(), cellNum, cellValue);
    }

    /**
     * 获取单元格数据
     * @param sheet     sheet页数据
     * @param row       行标识
     * @param cellNum 单元格位置
     * @return 单元格数据
     */
    public static String getCellString(Sheet sheet, int row, int cellNum) {
        return ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNum);
    }

    /**
     * 获取单元格数据
     * @param sheet     sheet页数据
     * @param row       行标识
     * @param cellNum 单元格位置
     * @return 单元格数据
     */
    public static String getCellStringNot(Sheet sheet, int row, int cellNum) {
        String cellValue = ExcelReadUtils.getMergedRegionValueToString(sheet, row, cellNum);
        return checkStringNull(row, cellNum, cellValue);
    }


    /**
     * 校验飞空
     * @param value 值
     */
    public static String checkStringNull(int row, int cellNum, String value) {
        if (StringUtils.isBlank(value)) {
            String msg = getReplaceMsg(CommonResultMsg.ROW_CELL_ERROR, row, cellNum);
            throw new BizException(CommonResultCode.ROW_CELL_ERROR, msg);
        }
        return value.trim();
    }

    /**
     * 获取替换错误信息
     * @param msg 错误信息
     * @param row 行标识
     * @param cellNum 单元格标识
     * @return 替换错误信息
     */
    public static String getReplaceMsg(String msg, int row, int cellNum) {
        msg = msg.replace(ReadCellConstant.ROW_REPLACE, String.valueOf(row + 1));
        msg = msg.replace(ReadCellConstant.CELL_REPLACE, String.valueOf(cellNum + 1));
        return msg;
    }


    /**
     * 获取替换错误信息
     * @param msg 错误信息
     * @param row 行标识
     * @param name 替换文字
     * @return 替换错误信息
     */
    public static String getReplaceMsg(String msg, int row, String name) {
        msg = msg.replace(ReadCellConstant.ROW_REPLACE, String.valueOf(row + 1));
        msg = msg.replace(ReadCellConstant.NAME_REPLACE, name);
        return msg;
    }

    /**
     * 获取替换错误信息
     * @param msg 错误信息
     * @param row 行标识
     * @param cell 替换文字
     * @param name 替换文字
     * @return 替换错误信息
     */
    public static String getReplaceMsg(String msg, int row, String cell, String name) {
        msg = msg.replace(ReadCellConstant.ROW_REPLACE, String.valueOf(row + 1));
        msg = msg.replace(ReadCellConstant.CELL_REPLACE, cell);
        msg = msg.replace(ReadCellConstant.NAME_REPLACE, name);
        return msg;
    }

    /**
     * 获取替换字符串
     * @param time 时间字符串
     * @return 替换字符串
     */
    public static String getReplaceTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        return time.replace("-", "/");
    }

    /**
     * 获取时间戳
     * @param date 日期
     * @param row 行
     * @param cellNum 单元格
     * @return 时间戳
     */
    public static long dateToLong(String date, int row, int cellNum) {
        if (StringUtils.isEmpty(date)) {
            return 0L;
        }
        try {
            return DateFormatUtils.dateStringToLong(DateConstant.FORMAT_STRING_TWO, date);
        } catch (ParseException e) {
            String msg = getReplaceMsg(CommonResultMsg.ROW_CELL_DATE_ERROR, row, cellNum);
            throw new BizException(CommonResultCode.ROW_CELL_DATE_ERROR, msg);
        }
    }

    /**
     * 获取字段长度错误信息
     *
     * @param row 行标识
     * @param cell 单元格名称
     * @param length 长度
     * @return 替换错误信息
     */
    public static String getLengthMsg(int row, String cell, String length) {
        String msg = CommonResultMsg.ROW_CELL_LENGTH_ERROR.replace(ReadCellConstant.ROW_REPLACE,
                String.valueOf(row + 1));
        msg = msg.replace(ReadCellConstant.CELL_REPLACE, cell);
        msg = msg.replace(ReadCellConstant.NAME_REPLACE, length);
        return msg;
    }
}
