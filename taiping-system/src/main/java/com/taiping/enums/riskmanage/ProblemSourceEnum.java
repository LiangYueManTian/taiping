package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 问题来源枚举
 */
public enum  ProblemSourceEnum {
    /**
     * 日常巡检
     */
    ROUTINEINSPECTION("日常巡检",1),
    /**
     * 风险专项排查
     */
    SPECIAL_RISK("风险专项排查",2);
    /**
     * 来源
     */
    private String source;
    /**
     * code
     */
    private Integer code;

    ProblemSourceEnum(String source, Integer code) {
        this.source = source;
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public Integer getCode() {
        return code;
    }

    public static Integer getCodeByName(String source) {
        for (ProblemSourceEnum problemSourceEnum : ProblemSourceEnum.values()) {
            if (problemSourceEnum.getSource().equals(source)) {
                return problemSourceEnum.getCode();
            }
        }
        return null;
    }
}
