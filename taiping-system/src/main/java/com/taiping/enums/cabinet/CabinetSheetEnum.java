package com.taiping.enums.cabinet;

/**
 * 机柜Excel表sheet标识枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum CabinetSheetEnum {

    /**
     * sheet 1
     */
    SHEET_ONE("sheet1", "0");

    private String sheet;

    private String sheetName;

    CabinetSheetEnum(String sheet, String sheetName) {
        this.sheet = sheet;
        this.sheetName = sheetName;
    }

    public String getSheet() {
        return sheet;
    }

    public String getSheetName() {
        return sheetName;
    }
}
