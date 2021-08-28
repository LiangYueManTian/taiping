package com.taiping.enums.problem;

/**
 * 停水停电记录Excel表sheet标识枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-10
 */
public enum FlashOffSheetEnum {
    /**
     * sheet  停水
     */
    WATER("停水", "0"),
    /**
     * sheet 停电
     */
    POWER("停电", "1");

    private String sheet;

    private String sheetName;

    FlashOffSheetEnum(String sheet, String sheetName) {
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
