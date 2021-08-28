package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 风险类型枚举
 */
public enum RiskTypeEnum {
    /**
     * 暖通系统
     */
    HVAC_SYSTEM("暖通系统",1),
    /**
     * 配电系统
     */
    DISTRIBUTION_SYSTEM("配电系统",2),
    /**
     * 日常运维
     */
    DAILY_OPERATION("日常运维",3);
    /**
     * 名称
     */
    private String typeName;
    /**
     * 类型code
     */
    private Integer typeCode;

    RiskTypeEnum(String typeName, Integer typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public static Integer getCodeByName(String levelName) {
        for (RiskTypeEnum riskTypeEnum : RiskTypeEnum.values()) {
            if (riskTypeEnum.getTypeName().equals(levelName)) {
                return riskTypeEnum.getTypeCode();
            }
        }
        return null;
    }

    public static String getNameByCode(Integer levelCode) {
        for (RiskTypeEnum riskTypeEnum : RiskTypeEnum.values()) {
            if (riskTypeEnum.getTypeCode().equals(levelCode)) {
                return riskTypeEnum.getTypeName();
            }
        }
        return null;
    }
}
