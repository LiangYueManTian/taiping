package com.taiping.enums.cabinet;
/**
 * 机柜Excel表sheet标识枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum CabinetSheetStartRowEnum {

    /**
     * sheet 1开始行数
     */
    SHEET_ONE_START_ROW("sheet1", 1);

    private String sheet;

    private Integer sheetName;

    CabinetSheetStartRowEnum(String sheet, Integer sheetName) {
        this.sheet = sheet;
        this.sheetName = sheetName;
    }

    public String getSheet() {
        return sheet;
    }

    public Integer getSheetName() {
        return sheetName;
    }
}
