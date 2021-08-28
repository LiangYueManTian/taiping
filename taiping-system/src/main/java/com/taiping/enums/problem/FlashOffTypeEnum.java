package com.taiping.enums.problem;
/**
 * 停水停电记录闪断类型枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-14
 */
public enum FlashOffTypeEnum {
    /**
     * 停水
     */
    WATER_CUT("停水", "01"),
    /**
     * 停电
     */
    POWER_CUT("断电", "02"),
    /**
     * 市电骤降闪断
     */
    POWER_FLASH_OFF("闪断", "03");
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型编码
     */
    private String typeCode;

    FlashOffTypeEnum(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public static String getNameForCode(String typeCode) {
        for (FlashOffTypeEnum flashOffTypeEnum : FlashOffTypeEnum.values()) {
            if (flashOffTypeEnum.getTypeCode().equals(typeCode)) {
                return flashOffTypeEnum.getTypeName();
            }
        }
        return "";
    }
}
