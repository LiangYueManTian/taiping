package com.taiping.enums.analyze.capacity;

/**
 * 阈值类型枚举值
 * @author hedongwei@wistronits.com
 * @date  2019/11/1 16:52
 */
public enum ThresholdTypeEnum {

    /**
     * 楼层
     */
    FLOOR("楼层", "1"),
    /**
     * 机柜
     */
    CABINET("机柜", "2"),

    /**
     * 功能区
     */
    DEVICE_TYPE("功能区", "3"),

    /**
     * 模块
     */
    MODULE("模块", "4"),

    /**
     * UPS
     */
    UPS("UPS", "5"),

    /**
     * 列头柜
     */
    CABINET_COLUMN("列头柜", "6"),

    /**
     * PDU(机柜电力额定负荷)
     */
    PDU("PDU", "7"),

    /**
     * 路由类型
     */
    PORT_TYPE("路由类型", "8");

    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型
     */
    private String type;

    ThresholdTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据类型名称
     * @param type 模块类型
     * @return 类型名称
     */
    public static String getName(String type) {
        for (ThresholdTypeEnum annexTypeEnum : ThresholdTypeEnum.values()) {
            if (annexTypeEnum.getTypeName().equals(type)) {
                return annexTypeEnum.getType();
            }
        }
        return ThresholdTypeEnum.FLOOR.getType();
    }
}
