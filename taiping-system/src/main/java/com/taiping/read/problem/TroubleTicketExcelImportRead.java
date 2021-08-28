package com.taiping.read.problem;

import com.taiping.constant.problem.ProblemConstant;
import com.taiping.constant.problem.ProblemResultCode;
import com.taiping.constant.problem.ProblemResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.problem.TroubleTicket;
import com.taiping.enums.problem.ProblemExcelRowEnum;
import com.taiping.enums.problem.TroubleLevelEnum;
import com.taiping.enums.problem.TroubleTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.problem.TroubleSortUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import static com.taiping.utils.ReadCellUtil.*;

/**
 * 导入故障单Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Component
public class TroubleTicketExcelImportRead extends AbstractExcelImportRead {
    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 1;
    /**
     * 数据开始行数
     */
    private static final int BEGIN_ROW = 1;

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
        //获取每个字段
        String ticketCode = getCellStringNot(row, ProblemExcelRowEnum.TICKET_CODE.getCellNum());
        String reportPerson = getCellStringNot(row, ProblemExcelRowEnum.REPORT_PERSON.getCellNum());
        String troubleSource = getCellStringNot(row, ProblemExcelRowEnum.TROUBLE_SOURCE.getCellNum());
        String status = getCellString(row, ProblemExcelRowEnum.STATUS.getCellNum());
        String createPerson = getCellStringNot(row, ProblemExcelRowEnum.CREATE_PERSON.getCellNum());
        String createTime = getCellStringNot(row, ProblemExcelRowEnum.CREATE_TIME.getCellNum());
        String troubleTime = getCellStringNot(row, ProblemExcelRowEnum.TROUBLE_TIME.getCellNum());
        String troubleLocation = getCellString(row, ProblemExcelRowEnum.TROUBLE_LOCATION.getCellNum());
        String troubleType = getCellStringNot(row, ProblemExcelRowEnum.TROUBLE_TYPE.getCellNum());
        String troubleName = getCellString(row, ProblemExcelRowEnum.TROUBLE_NAME.getCellNum());
        String description = getCellString(row, ProblemExcelRowEnum.DESCRIPTION.getCellNum());
        String influence = getCellStringNot(row, ProblemExcelRowEnum.INFLUENCE.getCellNum());
        String urgency = getCellStringNot(row, ProblemExcelRowEnum.URGENCY.getCellNum());
        String troubleLevel = getCellStringNot(row, ProblemExcelRowEnum.TROUBLE_LEVEL.getCellNum());
        String handleDescription = getCellString(row, ProblemExcelRowEnum.HANDLE_DESCRIPTION.getCellNum());
        String solveDescription = getCellString(row, ProblemExcelRowEnum.SOLVE_DESCRIPTION.getCellNum());
        String handleGroup = getCellString(row, ProblemExcelRowEnum.HANDLE_GROUP.getCellNum());
        String handlePerson = getCellString(row, ProblemExcelRowEnum.HANDLE_PERSON.getCellNum());
        String troubleCause = getCellString(row, ProblemExcelRowEnum.TROUBLE_CAUSE.getCellNum());
        String solvePerson = getCellString(row, ProblemExcelRowEnum.SOLVE_PERSON.getCellNum());
        String solveTime = getCellString(row, ProblemExcelRowEnum.SOLVE_TIME.getCellNum());
        String closeType = getCellString(row, ProblemExcelRowEnum.CLOSE_TYPE.getCellNum());
        String closePerson = getCellString(row, ProblemExcelRowEnum.CLOSE_PERSON.getCellNum());
        String closeTime = getCellString(row, ProblemExcelRowEnum.CLOSE_TIME.getCellNum());
        String updateTime = getCellString(row, ProblemExcelRowEnum.UPDATE_TIME.getCellNum());
        String interruptDuration = getCellString(row, ProblemExcelRowEnum.INTERRUPT_DURATION.getCellNum());
        String ticketType = getCellStringNot(row, ProblemExcelRowEnum.TICKET_TYPE.getCellNum());
        //创建Bean
        TroubleTicket troubleTicket = getTroubleTicket(ticketCode, reportPerson,
                troubleSource, status, createPerson);
        //级别转换为code编码
        influence = getLevelCode(influence, row.getRowNum(), ProblemExcelRowEnum.INFLUENCE.getPropertyName());
        urgency = getLevelCode(urgency, row.getRowNum(), ProblemExcelRowEnum.URGENCY.getPropertyName());
        troubleLevel = getLevelCode(troubleLevel, row.getRowNum(), ProblemExcelRowEnum.TROUBLE_LEVEL.getPropertyName());
        setTroubleTicketStepFirst(troubleType, influence, urgency, troubleLevel, troubleTicket);
        //拆分一级分类，二级分类
        setTroubleTicketType(troubleTicket, troubleType, row.getRowNum());
        //填充Bean数据
        setTroubleTicketStepSecond(troubleLocation, troubleName, handleDescription, solveDescription, troubleTicket);
        setTroubleTicketStepThird(handleGroup, handlePerson, description, troubleCause, troubleTicket);
        setTroubleTicketStepFourth(solvePerson, closeType, closePerson, troubleTicket);
        //处理日期
        Long time = dateToLong(troubleTime, row.getRowNum(), ProblemExcelRowEnum.TROUBLE_TIME.getCellNum());
        setTroubleTicketTime(createTime, time, solveTime, closeTime, updateTime, troubleTicket);
        troubleTicket.setInterruptDuration(getDouble(row.getRowNum(), interruptDuration));
        //获取故障单所属类型
        getTroubleTicketType(row.getRowNum(), ticketType, troubleTicket);
        return troubleTicket;
    }

    /**
     * 获取故障单所属类型
     * @param row 行标识
     * @param ticketType 故障单类型
     * @param troubleTicket 故障单
     */
    private void getTroubleTicketType(int row, String ticketType, TroubleTicket troubleTicket) {
        if (TroubleTypeEnum.PROPERTY.getTypeName().equals(ticketType)) {
            troubleTicket.setTicketType(TroubleTypeEnum.PROPERTY.getType());
        } else if (TroubleTypeEnum.TAI_PING.getTypeName().equals(ticketType)) {
            troubleTicket.setTicketType(TroubleTypeEnum.TAI_PING.getType());
        } else {
            String msg = getReplaceMsg(ProblemResultMsg.TICKET_TYPE_ERROR, row, ticketType);
            throw new BizException(ProblemResultCode.TICKET_TYPE_ERROR, msg);
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
        //获取每个字段
        String ticketCode = getCellStringNot(sheet, row, ProblemExcelRowEnum.TICKET_CODE.getCellNum());
        String reportPerson = getCellStringNot(sheet, row, ProblemExcelRowEnum.REPORT_PERSON.getCellNum());
        String troubleSource = getCellStringNot(sheet, row, ProblemExcelRowEnum.TROUBLE_SOURCE.getCellNum());
        String status = getCellString(sheet, row, ProblemExcelRowEnum.STATUS.getCellNum());
        String createPerson = getCellStringNot(sheet, row, ProblemExcelRowEnum.CREATE_PERSON.getCellNum());
        String createTime = getCellStringNot(sheet, row, ProblemExcelRowEnum.CREATE_TIME.getCellNum());
        String troubleTime = getCellStringNot(sheet, row, ProblemExcelRowEnum.TROUBLE_TIME.getCellNum());
        String troubleLocation = getCellString(sheet, row, ProblemExcelRowEnum.TROUBLE_LOCATION.getCellNum());
        String troubleType = getCellStringNot(sheet, row, ProblemExcelRowEnum.TROUBLE_TYPE.getCellNum());
        String troubleName = getCellString(sheet, row, ProblemExcelRowEnum.TROUBLE_NAME.getCellNum());
        String description = getCellString(sheet, row, ProblemExcelRowEnum.DESCRIPTION.getCellNum());
        String influence = getCellStringNot(sheet, row, ProblemExcelRowEnum.INFLUENCE.getCellNum());
        String urgency = getCellStringNot(sheet, row, ProblemExcelRowEnum.URGENCY.getCellNum());
        String troubleLevel = getCellStringNot(sheet, row, ProblemExcelRowEnum.TROUBLE_LEVEL.getCellNum());
        String handleDescription = getCellString(sheet, row, ProblemExcelRowEnum.HANDLE_DESCRIPTION.getCellNum());
        String solveDescription = getCellString(sheet, row, ProblemExcelRowEnum.SOLVE_DESCRIPTION.getCellNum());
        String handleGroup = getCellString(sheet, row, ProblemExcelRowEnum.HANDLE_GROUP.getCellNum());
        String handlePerson = getCellString(sheet, row, ProblemExcelRowEnum.HANDLE_PERSON.getCellNum());
        String troubleCause = getCellString(sheet, row, ProblemExcelRowEnum.TROUBLE_CAUSE.getCellNum());
        String solvePerson = getCellString(sheet, row, ProblemExcelRowEnum.SOLVE_PERSON.getCellNum());
        String solveTime = getCellString(sheet, row, ProblemExcelRowEnum.SOLVE_TIME.getCellNum());
        String closeType = getCellString(sheet, row, ProblemExcelRowEnum.CLOSE_TYPE.getCellNum());
        String closePerson = getCellString(sheet, row, ProblemExcelRowEnum.CLOSE_PERSON.getCellNum());
        String closeTime = getCellString(sheet, row, ProblemExcelRowEnum.CLOSE_TIME.getCellNum());
        String updateTime = getCellString(sheet, row, ProblemExcelRowEnum.UPDATE_TIME.getCellNum());
        String interruptDuration = getCellString(sheet, row, ProblemExcelRowEnum.INTERRUPT_DURATION.getCellNum());
        String ticketType = getCellStringNot(sheet, row, ProblemExcelRowEnum.TICKET_TYPE.getCellNum());
        //创建Bean
        TroubleTicket troubleTicket = getTroubleTicket(ticketCode, reportPerson,
                troubleSource, status, createPerson);
        //级别转换为code编码
        influence = getLevelCode(influence, row, ProblemExcelRowEnum.INFLUENCE.getPropertyName());
        urgency = getLevelCode(urgency, row, ProblemExcelRowEnum.URGENCY.getPropertyName());
        troubleLevel = getLevelCode(troubleLevel, row, ProblemExcelRowEnum.TROUBLE_LEVEL.getPropertyName());
        setTroubleTicketStepFirst(troubleType, influence, urgency, troubleLevel, troubleTicket);
        //拆分一级分类，二级分类
        setTroubleTicketType(troubleTicket, troubleType, row);
        //填充Bean数据
        setTroubleTicketStepSecond(troubleLocation, troubleName, handleDescription, solveDescription, troubleTicket);
        setTroubleTicketStepThird(handleGroup, handlePerson, description, troubleCause, troubleTicket);
        setTroubleTicketStepFourth(solvePerson, closeType, closePerson, troubleTicket);
        //处理日期
        Long time = dateToLong(troubleTime, row, ProblemExcelRowEnum.TROUBLE_TIME.getCellNum());
        setTroubleTicketTime(createTime, time, solveTime, closeTime, updateTime, troubleTicket);
        troubleTicket.setInterruptDuration(getDouble(row, interruptDuration));
        //获取故障单所属类型
        getTroubleTicketType(row, ticketType, troubleTicket);
        return troubleTicket;
    }

    /**
     * 拆分故障分类
     * @param troubleTicket 故障单
     * @param troubleType 故障分类
     * @param row 行标识
     */
    private void setTroubleTicketType(TroubleTicket troubleTicket, String troubleType, int row) {
        //分割
        String[] type = troubleType.split(ProblemConstant.TROUBLE_TYPE_SPLIT);
        //判断分类长度
        if (type.length != ProblemConstant.TROUBLE_TYPE_LENGTH) {
            String msg = getReplaceMsg(ProblemResultMsg.ROW_TYPE_ERROR, row, troubleType);
            throw new BizException(ProblemResultCode.ROW_TYPE_ERROR, msg);
        }
        //一级分类
        String topCode = TroubleSortUtil.getCodeForName(type[ProblemConstant.TROUBLE_TYPE_TOP]);
        if (topCode == null) {
            String msg = getReplaceMsg(ProblemResultMsg.ROW_TYPE_ERROR, row, troubleType);
            throw new BizException(ProblemResultCode.ROW_TYPE_ERROR, msg);
        }
        troubleTicket.setTopType(topCode);
        //二级分类
        String secondaryCode = TroubleSortUtil.getCodeForName(topCode, type[ProblemConstant.TROUBLE_TYPE_SECONDARY]);
        if (secondaryCode == null) {
            String msg = getReplaceMsg(ProblemResultMsg.ROW_TYPE_ERROR, row, troubleType);
            throw new BizException(ProblemResultCode.ROW_TYPE_ERROR, msg);
        }
        troubleTicket.setSecondaryType(secondaryCode);
    }

    /**
     *
     * @param row 行标识
     * @param string 数值
     * @return double
     */
    private Double getDouble(int row, String string) {
        if (StringUtils.isEmpty(string)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            String msg = getReplaceMsg(ProblemResultMsg.ROW_DOUBLE_ERROR, row, string);
            throw new BizException(ProblemResultCode.ROW_DOUBLE_ERROR, msg);
        }
    }


    /**
     * 获取级别对应code
     * @param level 级别
     * @param row 行标识
     * @param name 级别名称
     * @return String code
     */
    private String getLevelCode(String level, int row, String name) {
        String code = TroubleLevelEnum.getCodeForName(level);
        if (code == null) {
            String msg = getReplaceMsg(ProblemResultMsg.ROW_LEVEL_ERROR, row, name);
            throw new BizException(ProblemResultCode.ROW_LEVEL_ERROR, msg);
        }
        return code;
    }


    private TroubleTicket getTroubleTicket(String ticketCode, String reportPerson, String troubleSource,
                                           String status, String createPerson) {
        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketId(NineteenUUIDUtils.uuid());
        troubleTicket.setTicketCode(ticketCode);
        troubleTicket.setReportPerson(reportPerson);
        troubleTicket.setTroubleSource(troubleSource);
        troubleTicket.setStatus(status);
        troubleTicket.setCreatePerson(createPerson);
        return troubleTicket;
    }
    private void setTroubleTicketTime(String createTime, Long troubleTime, String solveTime,
                                      String closeTime, String updateTime, TroubleTicket troubleTicket) {
        troubleTicket.setCreateTime(getReplaceTime(createTime));
        troubleTicket.setTroubleTime(troubleTime);
        troubleTicket.setSolveTime(getReplaceTime(solveTime));
        troubleTicket.setCloseTime(getReplaceTime(closeTime));
        troubleTicket.setUpdateTime(getReplaceTime(updateTime));
    }
    private void setTroubleTicketStepFirst(String troubleType, String influence, String urgency, String troubleLevel,
                                       TroubleTicket troubleTicket) {
        troubleTicket.setTroubleType(troubleType);
        troubleTicket.setInfluence(influence);
        troubleTicket.setUrgency(urgency);
        troubleTicket.setTroubleLevel(troubleLevel);
    }
    private void setTroubleTicketStepSecond(String troubleLocation, String troubleName, String handleDescription,
                                            String solveDescription, TroubleTicket troubleTicket) {
        troubleTicket.setTroubleLocation(troubleLocation);
        troubleTicket.setTroubleName(troubleName);
        troubleTicket.setHandleDescription(handleDescription);
        troubleTicket.setSolveDescription(solveDescription);
    }
    private void setTroubleTicketStepThird(String handleGroup, String handlePerson, String description,
                                       String troubleCause, TroubleTicket troubleTicket) {
        troubleTicket.setDescription(description);
        troubleTicket.setHandleGroup(handleGroup);
        troubleTicket.setHandlePerson(handlePerson);
        troubleTicket.setTroubleCause(troubleCause);
    }
    private void setTroubleTicketStepFourth(String solvePerson, String closeType, String closePerson,
                                            TroubleTicket troubleTicket) {
        troubleTicket.setSolvePerson(solvePerson);
        troubleTicket.setCloseType(closeType);
        troubleTicket.setClosePerson(closePerson);
    }
}
