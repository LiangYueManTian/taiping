package com.taiping.enums.operation;

import com.taiping.entity.ExcelImportReadBean;

/**
 * 健康卡消防蓄水池和水冷机组运行时长Excel表sheet标识枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
public enum HealthySheetEnum {
    /**
     * sheet  水冷机组运行时长
     */
    WATER_COOLED_UNIT("水冷机组运行时长", "0", 4, 7),
    /**
     * sheet 消防蓄水池
     */
    FIRE_RESERVOIR("消防蓄水池", "1", 3, 5);

    private String sheet;

    private String sheetName;

    private Integer beginRow;

    private Integer endRow;

    HealthySheetEnum(String sheet, String sheetName, Integer beginRow, Integer endRow) {
        this.sheet = sheet;
        this.sheetName = sheetName;
        this.beginRow = beginRow;
        this.endRow = endRow;
    }

    public String getSheet() {
        return sheet;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelImportReadBean getBean() {
        ExcelImportReadBean importReadBean = new ExcelImportReadBean();
        importReadBean.setBegin(beginRow);
        importReadBean.setEnd(endRow);
        return importReadBean;
    }
}
