package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 处理进度枚举
 */
public enum ProcessProgressEnum {
    /**
     * 未开始
     */
    NOT_STARTED("未开始",1),
    /**
     * 进行中
     */
    ONGOING("进行中",2),
    /**
     * 已完成
     */
    COMPLETED("已完成",3);
    /**
     * 进度
     */
    private String  progress;
    /**
     * code
     */
    private Integer code;

    ProcessProgressEnum(String progress, Integer code) {
        this.progress = progress;
        this.code = code;
    }

    public String getProgress() {
        return progress;
    }

    public Integer getCode() {
        return code;
    }

    public static Integer getCodeByName(String progress) {
        for (ProcessProgressEnum progressEnum : ProcessProgressEnum.values()) {
            if (progressEnum.getProgress().equals(progress)) {
                return progressEnum.getCode();
            }
        }
        return null;
    }
}
