package com.taiping.enums.cabinet;

/**
 * 线缆类型枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum CableTypeEnum {

    /**
     * 铜缆
     */
    CABLE_TYPE_ONE("铜缆", "1"),

    /**
     * 光纤
     */
    CABLE_TYPE_TWO("光纤", "2");

    private String cableTypeName;

    private String cableType;

    CableTypeEnum(String cableTypeName, String cableType) {
        this.cableTypeName = cableTypeName;
        this.cableType = cableType;
    }

    /**
     * 根据线缆类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 线缆类型名称
     * @return 线缆类型code
     */
    public static String getCableTypeByName(String name) {
        for (CableTypeEnum cableTypeEnum : CableTypeEnum.values()) {
            if (cableTypeEnum.getCableTypeName().equals(name)) {
                return cableTypeEnum.getCableType();
            }
        }
        return "";
    }

    /**
     * 根据线缆类型获取名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 线缆类型code
     * @return 线缆类型名称
     */
    public static String getCableTypeByCode(String code) {
        for (CableTypeEnum cableTypeEnum : CableTypeEnum.values()) {
            if (cableTypeEnum.getCableType().equals(code)) {
                return cableTypeEnum.getCableTypeName();
            }
        }
        return "";
    }


    public String getCableTypeName() {
        return cableTypeName;
    }

    public String getCableType() {
        return cableType;
    }
}
