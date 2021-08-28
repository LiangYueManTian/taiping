package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 复检结果枚举
 */
public enum  RecheckResultEnum {
    /**
     * 已解决
     */
    RESOLVED("已解决",1),
    /**
     * 未解决
     */
    UNSOLVED("未解决",2);
    /**
     * 结果
     */
    private String  result;
    /**
     * code
     */
    private Integer code;

    RecheckResultEnum(String result, Integer code) {
        this.result = result;
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public static Integer getCodeByName(String result) {
        for (RecheckResultEnum recheckResultEnum : RecheckResultEnum.values()) {
            if (recheckResultEnum.getResult().equals(result)) {
                return recheckResultEnum.getCode();
            }
        }
        return null;
    }
}
