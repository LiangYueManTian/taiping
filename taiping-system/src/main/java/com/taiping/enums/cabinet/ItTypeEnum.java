package com.taiping.enums.cabinet;

/**
 * it类型枚举值
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ItTypeEnum {

    /**
     * 变压器
     */
    TYPE_ONE("变压器", "1"),

    /**
     * UPS
     */
    TYPE_TWO("UPS", "2");

    private String typeName;

    private String type;

    ItTypeEnum(String typeName, String type) {
        this.typeName = typeName;
        this.type = type;
    }

    /**
     * 根据模块获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 模块名称
     * @return 模块code
     */
    public static String getTypeByName(String name) {
        for (ItTypeEnum itTypeEnum : ItTypeEnum.values()) {
            if (itTypeEnum.getTypeName().equals(name)) {
                return itTypeEnum.getType();
            }
        }
        return "";
    }


    /**
     * 根据code模块获取name
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 模块code
     * @return 模块名称
     */
    public static String getTypeByCode(String code) {
        for (ItTypeEnum itTypeEnum : ItTypeEnum.values()) {
            if (itTypeEnum.getType().equals(code)) {
                return itTypeEnum.getTypeName();
            }
        }
        return "";
    }

    public String getTypeName() {
        return typeName;
    }

    public String getType() {
        return type;
    }
}
