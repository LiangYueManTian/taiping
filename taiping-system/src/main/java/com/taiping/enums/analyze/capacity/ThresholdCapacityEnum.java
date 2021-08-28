package com.taiping.enums.analyze.capacity;

/**
 * 容量阈值枚举类
 * @author hedongwei@wistronits.com
 * @date 2019/11/5 15:32
 */
public enum ThresholdCapacityEnum {

    /**
     * 功能区空间占比
     */
    DEVICE_SPACE_PERCENT("功能区空间占比", "1010"),

    /**
     * 楼层空间占比
     */
    FLOOR_SPACE_PERCENT("楼层空间占比", "1020"),

    /**
     * 机柜空间占比
     */
    CABINET_SPACE_PERCENT("机柜空间占比", "1030"),


    /**
     * 模块电力占比
     */
    MODULE_ELECTRIC_PERCENT("模块电力占比", "1040"),

    /**
     * ups电力占比
     */
    UPS_ELECTRIC_PERCENT("ups电力占比", "1050"),

    /**
     * 列头柜电力占比
     */
    ARRAY_CABINET_ELECTRIC("列头柜电力占比", "1060"),

    /**
     * pdu电力占比
     */
    PDU_ELECTRIC_PERCENT("pdu电力占比", "1070"),

    /**
     * 综合布线已使用端口占比
     */
    GENERIC_CABLING("综合布线已使用端口占比", "1080"),

    /**
     * it能耗负荷总量配置
     */
    ALL_NUMBER_SETTING("it能耗负荷总量配置", "1090");

    /**
     * 阈值名称
     */
    private String thresholdName;

    /**
     * 阈值code
     */
    private String thresholdCode;

    ThresholdCapacityEnum(String thresholdName, String thresholdCode) {
        this.thresholdName = thresholdName;
        this.thresholdCode = thresholdCode;
    }

    public String getThresholdName() {
        return thresholdName;
    }

    public String getThresholdCode() {
        return thresholdCode;
    }

    /**
     * 根据获取阈值code
     * @param thresholdName 阈值Name
     * @return 阈值code
     */
    public static String getCode(String thresholdName) {
        for (ThresholdCapacityEnum thresholdModuleEnum : ThresholdCapacityEnum.values()) {
            if (thresholdModuleEnum.getThresholdName().equals(thresholdName)) {
                return thresholdModuleEnum.getThresholdCode();
            }
        }
        return ThresholdCapacityEnum.FLOOR_SPACE_PERCENT.getThresholdCode();
    }


    /**
     * 根据获取阈值名称
     * @param thresholdCode 阈值code
     * @return 阈值名称
     */
    public static String getName(String thresholdCode) {
        for (ThresholdCapacityEnum thresholdModuleEnum : ThresholdCapacityEnum.values()) {
            if (thresholdModuleEnum.getThresholdCode().equals(thresholdCode)) {
                return thresholdModuleEnum.getThresholdName();
            }
        }
        return ThresholdCapacityEnum.FLOOR_SPACE_PERCENT.getThresholdName();
    }
}
