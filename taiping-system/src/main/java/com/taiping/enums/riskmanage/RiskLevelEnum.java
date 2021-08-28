package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/28
 * 风险登记枚举
 */
public enum RiskLevelEnum {
    /**
     * 高
     */
    HIGH("高",1),
    /**
     * 中
     */
    MIDDLE("中",2),
    /**
     * 低
     */
    LOW("低",3);
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 等级code
     */
    private Integer levelCode;

    RiskLevelEnum(String levelName, Integer levelCode) {
        this.levelName = levelName;
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public Integer getLevelCode() {
        return levelCode;
    }

//    public static Integer getCodeByName(String levelName) {
//        for (RiskLevelEnum riskLevelEnum : RiskLevelEnum.values()) {
//            if (riskLevelEnum.getLevelName().equals(levelName)) {
//                return riskLevelEnum.getLevelCode();
//            }
//        }
//        return null;
//    }
    public static String getNameByCode(Integer levelCode) {
        for (RiskLevelEnum riskLevelEnum: RiskLevelEnum.values()) {
            if (riskLevelEnum.getLevelCode().equals(levelCode)) {
                return riskLevelEnum.getLevelName();
            }
        }
        return null;
    }
}
