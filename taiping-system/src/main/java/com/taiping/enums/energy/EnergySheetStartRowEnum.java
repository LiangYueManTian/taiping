package com.taiping.enums.energy;

/**
 * 配线架Excel表sheet标识枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum EnergySheetStartRowEnum {

    /**
     * sheet 1开始行数
     */
    SHEET_ONE_START_ROW("sheet1", 2),

    /**
     * sheet 2开始行数
     */
    SHEET_TWO_START_ROW("sheet1", 2),

    /**
     * sheet 3开始行数
     */
    SHEET_THREE_START_ROW("sheet1", 2);

    private String sheet;

    private Integer sheetName;

    EnergySheetStartRowEnum(String sheet, Integer sheetName) {
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
