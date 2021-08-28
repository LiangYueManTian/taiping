package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 风险处理进度枚举
 */
public enum ResolveStatusEnum {
    /**
     * 已解决
     */
    RESOLVED("已解决",1),
    /**
     * 未解决
     */
    UNSOLVED("未解决",2);
    /**
     * 状态
     */
    private String  status;
    /**
     * code
     */
    private Integer code;

    ResolveStatusEnum(String status, Integer code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public static Integer getCodeByName(String status) {
        for (ResolveStatusEnum resolveStatusEnum : ResolveStatusEnum.values()) {
            if (resolveStatusEnum.getStatus().equals(status)) {
                return resolveStatusEnum.getCode();
            }
        }
        return null;
    }
}
