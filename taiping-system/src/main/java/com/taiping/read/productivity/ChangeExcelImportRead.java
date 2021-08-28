package com.taiping.read.productivity;

import com.taiping.constant.DateConstant;
import com.taiping.constant.ReadCellConstant;
import com.taiping.constant.productivity.ProductivityResultCode;
import com.taiping.constant.productivity.ProductivityResultMsg;
import com.taiping.entity.ExcelImportReadBean;
import com.taiping.entity.ExcelReadBean;
import com.taiping.entity.productivity.ChangePeople;
import com.taiping.entity.productivity.ChangeRead;
import com.taiping.enums.productivity.ProductivityExcelRowEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.AbstractExcelImportRead;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.NineteenUUIDUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.taiping.utils.ReadCellUtil.getCellString;
import static com.taiping.utils.ReadCellUtil.getCellStringNot;
import static com.taiping.utils.ReadCellUtil.getReplaceMsg;

/**
 * 导入变更单Excel数据类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
@Component
public class ChangeExcelImportRead extends AbstractExcelImportRead {
    /**
     * sheet页数量
     */
    private static final int SHEET_NUMBER = 1;
    /**
     * 数据开始行数
     */
    private static final int BEGIN_ROW = 1;
    /**
     * 人员分隔符
     */
    private static final String OFFICER_SPLIT = "\u3001";
    /**
     * 变更单号项目名称分隔符
     */
    private static final String CHANGE_SPLIT = "-";
    /**
     * 变更单号项目名称数量
     */
    private static final int CHANGE_NAME_CODE = 2;


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
        String changeType = getCellStringNot(row, ProductivityExcelRowEnum.CHANGE_TYPE.getCellNum());
        String people = getCellStringNot(row, ProductivityExcelRowEnum.PEOPLE.getCellNum());
        String workload = getCellStringNot(row, ProductivityExcelRowEnum.WORKLOAD.getCellNum());
        String startDate = getCellStringNot(row, ProductivityExcelRowEnum.START_DATE.getCellNum());
        String endDate = getCellStringNot(row, ProductivityExcelRowEnum.END_DATE.getCellNum());
        String handoverPeople = getCellStringNot(row, ProductivityExcelRowEnum.HANDOVER_PEOPLE.getCellNum());
        String changeNameCode = getCellStringNot(row, ProductivityExcelRowEnum.CHANGE_NAME_CODE.getCellNum());
        String projectStatus = getCellString(row, ProductivityExcelRowEnum.PROJECT_STATUS.getCellNum());
        return getChangeRead(row.getRowNum(), changeType, people, workload,
                startDate, endDate, handoverPeople, changeNameCode, projectStatus);
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
        String changeType = getCellStringNot(sheet, row, ProductivityExcelRowEnum.CHANGE_TYPE.getCellNum());
        String people = getCellStringNot(sheet, row, ProductivityExcelRowEnum.PEOPLE.getCellNum());
        String workload = getCellStringNot(sheet, row, ProductivityExcelRowEnum.WORKLOAD.getCellNum());
        String startDate = getCellStringNot(sheet, row, ProductivityExcelRowEnum.START_DATE.getCellNum());
        String endDate = getCellStringNot(sheet, row, ProductivityExcelRowEnum.END_DATE.getCellNum());
        String handoverPeople = getCellStringNot(sheet, row, ProductivityExcelRowEnum.HANDOVER_PEOPLE.getCellNum());
        String changeNameCode = getCellStringNot(sheet, row, ProductivityExcelRowEnum.CHANGE_NAME_CODE.getCellNum());
        String projectStatus = getCellString(sheet, row, ProductivityExcelRowEnum.PROJECT_STATUS.getCellNum());
        return getChangeRead(row, changeType, people, workload,
                startDate, endDate, handoverPeople, changeNameCode, projectStatus);
    }

    /**
     * 组装变更单对象
     * @param row 行标识
     * @param changeType 变更单类型
     * @param people 人数
     * @param workload 设备U数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param handoverPeople 历史交接人
     * @param changeNameCode 变更单号 项目名称
     * @return ChangeRead
     */
    private ChangeRead getChangeRead(int row, String changeType, String people, String workload, String startDate,
                                   String endDate, String handoverPeople, String changeNameCode, String projectStatus) {
        //生成变更单对象
        ChangeRead changeRead = new ChangeRead();
        changeRead.setChangeId(NineteenUUIDUtils.uuid());
        changeRead.setChangeType(changeType);
        changeRead.setProjectStatus(projectStatus);
        //数值类型字段转换
        Integer numberPeople = getNumber(row, ProductivityExcelRowEnum.PEOPLE.getPropertyName(), people);
        Integer numberWorkload = getNumber(row, ProductivityExcelRowEnum.WORKLOAD.getPropertyName(), workload);
        changeRead.setPeople(numberPeople);
        changeRead.setWorkload(numberWorkload);
        //日期类型解析
        Long dateStart = getDate(row, ProductivityExcelRowEnum.START_DATE.getPropertyName(), startDate);
        Long dateEnd = getDate(row, ProductivityExcelRowEnum.END_DATE.getPropertyName(), endDate);
        changeRead.setStartDate(dateStart);
        changeRead.setEndDate(dateEnd);
        //拆分历史交接人
        List<ChangePeople> changePeopleList = new ArrayList<>();
        if (handoverPeople.contains(OFFICER_SPLIT)) {
            String[]  peopleArray = handoverPeople.split(OFFICER_SPLIT);
            double workloadPeople = Double.parseDouble(workload)/peopleArray.length;
            for (String s : peopleArray) {
                ChangePeople changePeople = getChangePeople(changeRead, workloadPeople, s);
                changePeopleList.add(changePeople);
            }
        } else {
            ChangePeople changePeople = getChangePeople(changeRead, Double.valueOf(workload), handoverPeople);
            changePeopleList.add(changePeople);
        }
        changeRead.setChangePeopleList(changePeopleList);
        changeRead.setHandoverPeople(handoverPeople);
        //拆分变更单号和项目名称
        if (!changeNameCode.contains(CHANGE_SPLIT)) {
            //不含分隔符数据错误
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_CELL_ERROR, row,
                    ProductivityExcelRowEnum.CHANGE_NAME_CODE.getPropertyName(), changeNameCode);
            throw new BizException(ProductivityResultCode.ROW_CELL_ERROR, msg);
        }
        String[] name = changeNameCode.split(CHANGE_SPLIT);
        if (name.length != CHANGE_NAME_CODE) {
            //数据错误
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_CELL_ERROR, row,
                    ProductivityExcelRowEnum.CHANGE_NAME_CODE.getPropertyName(), changeNameCode);
            throw new BizException(ProductivityResultCode.ROW_CELL_ERROR, msg);
        }
        changeRead.setChangeCode(name[0]);
        changeRead.setChangeName(name[1]);
        return changeRead;
    }

    /**
     * 创建ChangePeople
     * @param changeRead ChangeRead
     * @param workloadPeople 工作量
     * @param s 人
     * @return ChangePeople
     */
    private ChangePeople getChangePeople(ChangeRead changeRead, double workloadPeople, String s) {
        ChangePeople changePeople = new ChangePeople();
        changePeople.setChangeId(changeRead.getChangeId());
        changePeople.setId(NineteenUUIDUtils.uuid());
        changePeople.setPeople(s);
        changePeople.setWorkload(workloadPeople);
        changePeople.setStartDate(changeRead.getStartDate());
        changePeople.setEndDate(changeRead.getEndDate());
        return changePeople;
    }

    /**
     * 获取整数单元格值
     * @param row 行标识
     * @param name 单元格名称
     * @param number 内容
     * @return Integer
     */
    private Integer getNumber(int row, String name, String number) {
        if (number.matches(ReadCellConstant.NUMBER_REGEX)) {
            Double doubleNum = Double.parseDouble(number);
            return doubleNum.intValue();
        } else {
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_CELL_ERROR, row, name, number);
            throw new BizException(ProductivityResultCode.ROW_CELL_ERROR, msg);
        }
    }


    /**
     * 获取日期单元格值
     * @param row 行标识
     * @param name 单元格名称
     * @param date 时间
     * @return Integer
     */
    private Long getDate(int row, String name, String date) {
        try {
            return DateFormatUtils.dateStringToLong(DateConstant.FORMAT_STRING_ONE, date);
        } catch (ParseException e) {
            String msg = getReplaceMsg(ProductivityResultMsg.ROW_CELL_ERROR, row, name, date);
            throw new BizException(ProductivityResultCode.ROW_CELL_ERROR, msg);
        }
    }

}
