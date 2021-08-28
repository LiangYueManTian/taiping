package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/11/5
 * 风险创建方式枚举
 */
public enum CreatingModeEnum {
    /**
     * 手动新增
     */
    MANUALLY_ADDED("手动新增",0),
    /**
     * 运维管理活动生成
     */
    MANAGE_CREATE("运维管理活动生成",1);

    /**
     * 来源
     */
    private String creatingMode;
    /**
     * code
     */
    private Integer code;

    CreatingModeEnum(String creatingMode, Integer code) {
        this.creatingMode = creatingMode;
        this.code = code;
    }

    public String getCreatingMode() {
        return creatingMode;
    }

    public Integer getCode() {
        return code;
    }
}
