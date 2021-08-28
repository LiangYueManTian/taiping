package com.taiping.read.problem;

import com.taiping.constant.DateConstant;
import com.taiping.constant.problem.ProblemResultCode;
import com.taiping.constant.problem.ProblemResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.problem.FlashOff;
import com.taiping.enums.problem.FlashOffSheetEnum;
import com.taiping.enums.problem.FlashOffTypeEnum;
import com.taiping.enums.problem.ProblemExcelRowEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.NineteenUUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static com.taiping.utils.ReadCellUtil.getCellString;
import static com.taiping.utils.ReadCellUtil.getCellStringNot;
import static com.taiping.utils.ReadCellUtil.getReplaceMsg;

/**
 * 导入停水停电记录Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-14
 */
@Slf4j
@Component
public class FlashOffExcelImportRead extends AbstractExcelImportRead {
    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 2;
    /**
     * 数据开始行数
     */
    private static final int BEGIN_ROW = 2;
    /**
     * 序号正则
     */
    private static final String SERIAL_NUMBER_REGEX = "^[\\d.]+$";

    /**
     * 校验sheet页数量
     *
     * @param numberOfSheets sheet页数量
     */
    @Override
    protected void checkSheetNumber(int numberOfSheets) {
        if (numberOfSheets != SHEET_NUMBER) {
            //Excel文件sheet页数量错误
            throw new BizException(ProblemResultCode.SHEET_ERROR, ProblemResultMsg.SHEET_ERROR);
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
        String number = getCellStringNot(row, ProblemExcelRowEnum.SERIAL_NUMBER.getCellNum());
        if (!number.matches(SERIAL_NUMBER_REGEX)) {
            return null;
        }
        String offData = getCellStringNot(row, ProblemExcelRowEnum.OFF_DATE.getCellNum());
        String cause = getCellStringNot(row, ProblemExcelRowEnum.CAUSE.getCellNum());
        String influence = getCellStringNot(row, ProblemExcelRowEnum.FLASH_OFF_INFLUENCE.getCellNum());
        String startTime = getCellStringNot(row, ProblemExcelRowEnum.START_TIME.getCellNum());
        String endTime = getCellStringNot(row, ProblemExcelRowEnum.END_TIME.getCellNum());
        String remark = getCellString(row, ProblemExcelRowEnum.REMARK.getCellNum());
        return getFlashOff(row.getRowNum(), sheetName, offData, cause, influence, startTime, endTime, remark);
    }

    /**
     * 获取实际数据开始行数和结束行数
     * @param sheetName sheet页标识
     * @return 实际数据开始行数和结束行数
     */
    @Override
    protected ExcelImportReadBean getBeginAndEndRow(String sheetName) {
        ExcelImportReadBean excelImportReadBean = new ExcelImportReadBean();
        excelImportReadBean.setBegin(BEGIN_ROW);
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
        String number = getCellStringNot(sheet, row, ProblemExcelRowEnum.SERIAL_NUMBER.getCellNum());
        if (!number.matches(SERIAL_NUMBER_REGEX)) {
            return null;
        }
        String offData = getCellStringNot(sheet, row, ProblemExcelRowEnum.OFF_DATE.getCellNum());
        String cause = getCellStringNot(sheet, row, ProblemExcelRowEnum.CAUSE.getCellNum());
        String influence = getCellStringNot(sheet, row, ProblemExcelRowEnum.FLASH_OFF_INFLUENCE.getCellNum());
        String startTime = getCellStringNot(sheet, row, ProblemExcelRowEnum.START_TIME.getCellNum());
        String endTime = getCellStringNot(sheet, row, ProblemExcelRowEnum.END_TIME.getCellNum());
        String remark = getCellString(sheet, row, ProblemExcelRowEnum.REMARK.getCellNum());
        return getFlashOff(row, sheetName, offData, cause, influence, startTime, endTime, remark);
    }

    /**
     * 组装
     * @param sheetName sheet页标识
     * @param offData 日期
     * @param cause 原因
     * @param influence 造成影响
     * @param startTime 停水时间
     * @param endTime 来水时间
     * @param remark 备注
     * @return FlashOff
     */
    private FlashOff getFlashOff(int row, String sheetName, String offData, String cause, String influence,
                                 String startTime, String endTime, String remark) {
        offData = getDate(offData, row);
        startTime = getTime(startTime);
        endTime = getTime(endTime);
        FlashOff flashOff = new FlashOff();
        flashOff.setId(NineteenUUIDUtils.uuid());
        flashOff.setOffDate(DateFormatUtils.dateStringToLongShow(DateConstant.FORMAT_STRING_ONE, offData));
        flashOff.setCause(cause);
        flashOff.setInfluence(influence);
        flashOff.setStartTime(startTime);
        flashOff.setEndTime(endTime);
        flashOff.setRemark(remark);
        String type;
        if (FlashOffSheetEnum.WATER.getSheetName().equals(sheetName)) {
            type = FlashOffTypeEnum.WATER_CUT.getTypeCode();
        } else {
            if (cause.contains(FlashOffTypeEnum.POWER_FLASH_OFF.getTypeName())) {
                type = FlashOffTypeEnum.POWER_FLASH_OFF.getTypeCode();
            } else {
                type = FlashOffTypeEnum.POWER_CUT.getTypeCode();
            }
        }
        flashOff.setOffType(type);
        return flashOff;
    }

    /**
     * 日期格式化
     * @param offData 日期
     * @param row 行表示
     * @return String 日期
     */
    private String getDate(String offData, int row) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateConstant.FORMAT_STRING_ONE);
            offData = sdf.format(HSSFDateUtil.getJavaDate(Double.parseDouble(offData)));
            return offData;
        } catch (Exception e) {
            //日期格式错误
            String msg = getReplaceMsg(ProblemResultMsg.ROW_CELL_DATE_ERROR, row, offData);
            throw new BizException(ProblemResultCode.ROW_CELL_DATE_ERROR, msg);
        }
    }

    /**
     * 获取时间
     * @param time 时间
     * @return 时间
     */
    private String getTime(String time) {
        if (time.matches(SERIAL_NUMBER_REGEX)) {
            SimpleDateFormat format = new SimpleDateFormat(DateConstant.FORMAT_STRING_THREE);
            time = format.format(HSSFDateUtil.getJavaDate(Double.parseDouble(time)));
        }
        return time;
    }

}
