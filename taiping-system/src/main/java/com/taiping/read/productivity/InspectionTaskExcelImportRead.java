package com.taiping.read.productivity;

import com.taiping.constant.productivity.ProductivityResultCode;
import com.taiping.constant.productivity.ProductivityResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.productivity.InspectionTask;
import com.taiping.enums.productivity.ProductivityExcelRowEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.NineteenUUIDUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import static com.taiping.utils.ReadCellUtil.*;

/**
 * 导入巡检工单数据Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-15
 */
@Component
public class InspectionTaskExcelImportRead extends AbstractExcelImportRead {
    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 1;
    /**
     * 数据开始行数
     */
    private static final int BEGIN_ROW = 2;
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
        String inspectionCode = getCellStringNot(row, ProductivityExcelRowEnum.INSPECTION_CODE.getCellNum());
        String status = getCellStringNot(row, ProductivityExcelRowEnum.STATUS.getCellNum());
        String taskName = getCellStringNot(row, ProductivityExcelRowEnum.TASK_NAME.getCellNum());
        String location = getCellStringNot(row, ProductivityExcelRowEnum.LOCATION.getCellNum());
        String frequency = getCellStringNot(row, ProductivityExcelRowEnum.FREQUENCY.getCellNum());

        InspectionTask inspectionTask = getInspectionTaskStepFirst(inspectionCode, status,
                taskName, location, frequency);

        String inspectionGroup = getCellStringNot(row, ProductivityExcelRowEnum.INSPECTION_GROUP.getCellNum());
        String executor = getCellStringNot(row, ProductivityExcelRowEnum.EXECUTOR.getCellNum());
        String planUseTime = getCellStringNot(row, ProductivityExcelRowEnum.PLAN_USE_TIME.getCellNum());
        String actualUseTime = getCellString(row, ProductivityExcelRowEnum.ACTUAL_USE_TIME.getCellNum());

        getInspectionTaskStepSecond(inspectionTask, inspectionGroup, executor, planUseTime, actualUseTime);

        String createTime = getCellStringNot(row, ProductivityExcelRowEnum.CREATE_TIME.getCellNum());
        String planStartTime = getCellStringNot(row, ProductivityExcelRowEnum.PLAN_START_TIME.getCellNum());
        String planEndTime = getCellStringNot(row, ProductivityExcelRowEnum.PLAN_END_TIME.getCellNum());
        String allowLeadTime = getCellStringNot(row, ProductivityExcelRowEnum.ALLOW_LEAD_TIME.getCellNum());
        String allowDelayTime = getCellStringNot(row, ProductivityExcelRowEnum.ALLOW_DELAY_TIME.getCellNum());
        String actualStartTime = getCellString(row, ProductivityExcelRowEnum.ACTUAL_START_TIME.getCellNum());
        String actualEndTime = getCellString(row, ProductivityExcelRowEnum.ACTUAL_END_TIME.getCellNum());

        getInspectionTaskStepThird(inspectionTask, createTime, planStartTime, planEndTime);

        getInspectionTaskStepFourth(row.getRowNum(), inspectionTask, allowLeadTime,
                allowDelayTime, actualStartTime, actualEndTime);

        return inspectionTask;
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
        String inspectionCode = getCellStringNot(sheet, row, ProductivityExcelRowEnum.INSPECTION_CODE.getCellNum());
        String status = getCellStringNot(sheet, row, ProductivityExcelRowEnum.STATUS.getCellNum());
        String taskName = getCellStringNot(sheet, row, ProductivityExcelRowEnum.TASK_NAME.getCellNum());
        String location = getCellStringNot(sheet, row, ProductivityExcelRowEnum.LOCATION.getCellNum());
        String frequency = getCellStringNot(sheet, row, ProductivityExcelRowEnum.FREQUENCY.getCellNum());

        InspectionTask inspectionTask = getInspectionTaskStepFirst(inspectionCode, status,
                taskName, location, frequency);

        String inspectionGroup = getCellStringNot(sheet, row, ProductivityExcelRowEnum.INSPECTION_GROUP.getCellNum());
        String executor = getCellStringNot(sheet, row, ProductivityExcelRowEnum.EXECUTOR.getCellNum());
        String planUseTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.PLAN_USE_TIME.getCellNum());
        String actualUseTime = getCellString(sheet, row, ProductivityExcelRowEnum.ACTUAL_USE_TIME.getCellNum());

        getInspectionTaskStepSecond(inspectionTask, inspectionGroup, executor, planUseTime, actualUseTime);

        String createTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.CREATE_TIME.getCellNum());
        String planStartTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.PLAN_START_TIME.getCellNum());
        String planEndTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.PLAN_END_TIME.getCellNum());
        String allowLeadTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.ALLOW_LEAD_TIME.getCellNum());
        String allowDelayTime = getCellStringNot(sheet, row, ProductivityExcelRowEnum.ALLOW_DELAY_TIME.getCellNum());
        String actualStartTime = getCellString(sheet, row, ProductivityExcelRowEnum.ACTUAL_START_TIME.getCellNum());
        String actualEndTime = getCellString(sheet, row, ProductivityExcelRowEnum.ACTUAL_END_TIME.getCellNum());

        getInspectionTaskStepThird(inspectionTask, createTime, planStartTime, planEndTime);

        getInspectionTaskStepFourth(row, inspectionTask, allowLeadTime,
                allowDelayTime, actualStartTime, actualEndTime);
        return inspectionTask;
    }

    private InspectionTask getInspectionTaskStepFirst(String inspectionCode, String status,
                                                      String taskName, String location, String frequency) {
        InspectionTask inspectionTask = new InspectionTask();
        inspectionTask.setInspectionId(NineteenUUIDUtils.uuid());
        inspectionTask.setInspectionCode(inspectionCode);
        inspectionTask.setStatus(status);
        inspectionTask.setTaskName(taskName);
        inspectionTask.setLocation(location);
        inspectionTask.setFrequency(frequency);
        return inspectionTask;
    }
    private void getInspectionTaskStepSecond(InspectionTask inspectionTask, String inspectionGroup,
                                             String executor, String planUseTime, String actualUseTime) {
        inspectionTask.setInspectionGroup(inspectionGroup);
        inspectionTask.setExecutor(executor);
        inspectionTask.setPlanUseTime(planUseTime);
        inspectionTask.setActualUseTime(actualUseTime);
    }
    private void getInspectionTaskStepThird(InspectionTask inspectionTask, String createTime,
                                            String planStartTime, String planEndTime) {
        inspectionTask.setCreateTime(getReplaceTime(createTime));
        inspectionTask.setPlanStartTime(getReplaceTime(planStartTime));
        inspectionTask.setPlanEndTime(getReplaceTime(planEndTime));
    }
    private void getInspectionTaskStepFourth(int row, InspectionTask inspectionTask, String allowLeadTime,
                                             String allowDelayTime, String actualStartTime, String actualEndTime) {
        inspectionTask.setAllowLeadTime(getReplaceTime(allowLeadTime));
        inspectionTask.setAllowDelayTime(getReplaceTime(allowDelayTime));
        inspectionTask.setActualStartTime(getReplaceTime(actualStartTime));
        inspectionTask.setActualEndTime(dateToLong(actualEndTime, row,
                ProductivityExcelRowEnum.ACTUAL_END_TIME.getCellNum()));
    }
}
