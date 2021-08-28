package com.taiping.enums.operation;

/**
 * 运行情况-物业沟通-配电系统 sheetName
 *
 * @author: liyj
 * @date: 2019/12/10 13:30
 **/
public enum PowerSheetNameEnum {

    DIST_HIGH_CABINET("高压柜"),
    DIST_LOW_CABINET("低压柜"),
    CHAI_FA("柴发"),
    TRANSFORMER("变压器");

    private String sheetName;

    PowerSheetNameEnum(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    /**
     * 根据sheetName 获取 下标
     *
     * @param sheetName
     * @return
     */
    public static Integer getOrdinalBySheetName(String sheetName) {
        for (PowerSheetNameEnum sheetNameEnum : PowerSheetNameEnum.values()) {
            if (sheetName.trim().equals(sheetNameEnum.getSheetName())) {
                return sheetNameEnum.ordinal();
            }
        }
        return null;
    }

}
