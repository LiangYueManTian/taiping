package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 涉及系统枚举
 */
public enum ReferSystemEnum {
    /**
     * 暖通
     */
    HVAC("暖通",1),
    /**
     * 电气
     */
    ELECTRICAL("电气",2),
    /**
     * 弱电
     */
    WEAK_CURRENT("弱电",3),
    /**
     * 流程管理
     */
    PROCESS_MANAGE("流程管理",4),
    /**
     * 装饰
     */
    DECORATE("装饰",5);
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 系统类型
     */
    private Integer systemCode;

    ReferSystemEnum(String systemName, Integer systemCode) {
        this.systemName = systemName;
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public Integer getSystemCode() {
        return systemCode;
    }

    public static Integer getCodeByName(String systemName) {
        for (ReferSystemEnum referSystemEnum : ReferSystemEnum.values()) {
            if (referSystemEnum.getSystemName().equals(systemName)) {
                return referSystemEnum.getSystemCode();
            }
        }
        return null;
    }
}
