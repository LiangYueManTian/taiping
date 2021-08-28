package com.taiping.read.productivity;

import com.taiping.constant.DateConstant;
import com.taiping.constant.productivity.ProductivityResultCode;
import com.taiping.constant.productivity.ProductivityResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.productivity.Schedule;
import com.taiping.entity.productivity.ScheduleDto;
import com.taiping.enums.productivity.ProductivityExcelRowEnum;
import com.taiping.enums.productivity.ScheduleTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.NineteenUUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.taiping.utils.ReadCellUtil.*;

/**
 * 导入常白班排班和倒班排班Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Component
public class ScheduleExcelImportRead extends AbstractExcelImportRead {
    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 1;
    /**
     * 数据开始行数
     */
    private static final int BEGIN_ROW = 10;
    /**
     * 值班人员分隔符
     */
    private static final String DUTY_OFFICER_SPLIT = ",";

    /**
     * 校验sheet页数量
     *
     * @param numberOfSheets sheet页数量
     */
    @Override
    protected void checkSheetNumber(int numberOfSheets) {
        if (numberOfSheets != SHEET_NUMBER) {
            //Excel文件sheet页数量错误
            throw new BizException(ProductivityResultCode.SHEET_ERROR, ProductivityResultMsg.SHEET_ERROR);
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
        String typeName = getCellStringNot(row, ProductivityExcelRowEnum.TYPE_NAME.getCellNum());
        String date = getCellStringNot(row, ProductivityExcelRowEnum.DATE.getCellNum());
        String startTime = getCellStringNot(row, ProductivityExcelRowEnum.START_TIME.getCellNum());
        String endTime = getCellStringNot(row, ProductivityExcelRowEnum.END_TIME.getCellNum());
        String dutyOfficer = getCellStringNot(row, ProductivityExcelRowEnum.DUTY_OFFICER.getCellNum());
        String specialDescription = getCellString(row, ProductivityExcelRowEnum.SPECIAL_DESCRIPTION.getCellNum());
        Schedule schedule = getSchedule(row.getRowNum(), typeName, date, startTime,
                endTime, dutyOfficer, specialDescription);
        return getExcelReadBean(dutyOfficer, schedule);
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
        String typeName = getCellStringNot(sheet, row, ProductivityExcelRowEnum.TYPE_NAME.getCellNum());
        String date = getCellStringNot(sheet, row, ProductivityExcelRowEnum.DATE.getCellNum());
        String startTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.START_TIME.getCellNum());
        String endTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.END_TIME.getCellNum());
        String dutyOfficer = getCellStringNot(sheet, row, ProductivityExcelRowEnum.DUTY_OFFICER.getCellNum());
        String specialDescription = getCellString(sheet, row, ProductivityExcelRowEnum.SPECIAL_DESCRIPTION.getCellNum());
        Schedule schedule = getSchedule(row, typeName, date, startTime, endTime, dutyOfficer, specialDescription);
        return getExcelReadBean(dutyOfficer, schedule);
    }


    /**
     * 根据班次类型返回参数
     * @param dutyOfficer 值班人员
     * @param schedule 排班数据
     * @return ExcelReadBean
     */
    private ExcelReadBean getExcelReadBean(String dutyOfficer, Schedule schedule) {
        return getScheduleDto(schedule, dutyOfficer);
    }

    /**
     * 组装
     * @param typeName 班次名称
     * @param date 排班日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param dutyOfficer 值班人员
     * @param specialDescription 特殊情况说明
     * @return Schedule
     */
    private Schedule getSchedule(int row, String typeName, String date, String startTime,
                                 String endTime, String dutyOfficer, String specialDescription) {
        Schedule schedule = new Schedule();
        String type = ScheduleTypeEnum.getTypeByName(typeName);
        // 类型错误
        if (type == null) {
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_TYPE_ERROR, row, typeName);
            throw new BizException(ProductivityResultCode.ROW_TYPE_ERROR, msg);
        }
        try {
            long toLong = DateFormatUtils.dateStringToLong(DateConstant.FORMAT_STRING_ONE, date);
            schedule.setScheduleDate(toLong);
        } catch (ParseException e) {
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_DATE_ERROR, row, date);
            throw new BizException(ProductivityResultCode.ROW_DATE_ERROR, msg);
        }
        schedule.setScheduleId(NineteenUUIDUtils.uuid());
        schedule.setScheduleType(type);
        schedule.setScheduleName(typeName);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setDutyOfficer(dutyOfficer);
        schedule.setSpecialDescription(specialDescription);
        return schedule;
    }

    /**
     * 排班数据值班人拆分
     * @param schedule 排班数据
     * @param dutyOfficer 值班人
     * @return 排班数据
     */
    private ScheduleDto getScheduleDto(Schedule schedule, String dutyOfficer) {
        ScheduleDto scheduleDto = new ScheduleDto();
        List<Schedule> scheduleList = new ArrayList<>();
        if (dutyOfficer.contains(DUTY_OFFICER_SPLIT)) {
            String[] dutyOfficerList = dutyOfficer.split(DUTY_OFFICER_SPLIT);
            for (String s : dutyOfficerList) {
                if (StringUtils.isBlank(s)) {
                    continue;
                }
                Schedule scheduleTemp = new Schedule();
                BeanUtils.copyProperties(schedule, scheduleTemp);
                scheduleTemp.setDutyOfficer(s.trim());
                scheduleTemp.setScheduleId(NineteenUUIDUtils.uuid());
                scheduleList.add(scheduleTemp);
            }
        } else {
            scheduleList.add(schedule);
        }
        scheduleDto.setScheduleList(scheduleList);
        scheduleDto.setScheduleDate(schedule.getScheduleDate());
        scheduleDto.setScheduleType(schedule.getScheduleType());
        return scheduleDto;
    }

}
