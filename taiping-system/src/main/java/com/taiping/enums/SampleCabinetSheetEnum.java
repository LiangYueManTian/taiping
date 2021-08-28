package com.taiping.enums;
/**
 * 机柜Excel表sheet标识枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-09-09
 */
public enum SampleCabinetSheetEnum {
    /**
     * sheet 1
     */
    SHEET_ONE("sheet1", "0"),
    /**
     * sheet 2
     */
    SHEET_TWO("sheet2", "1");

    private String sheet;

    private String sheetName;

    SampleCabinetSheetEnum(String sheet, String sheetName) {
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
