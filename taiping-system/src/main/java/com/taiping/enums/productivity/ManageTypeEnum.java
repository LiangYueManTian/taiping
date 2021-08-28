package com.taiping.enums.productivity;
/**
 * 产生运维管理活动枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-12
 */
public enum  ManageTypeEnum {

    /**
     * 空闲
     */
    FREE("空闲", "101", "团队负荷空闲占比"),
    /**
     * 正常
     */
    NORMAL("正常", "201", "团队负荷正常占比"),
    /**
     * 满负荷
     */
    FULL_LOAD("满负荷", "301", "团队负荷满负荷占比"),
    /**
     * 空闲预测
     */
    FREE_NEXT("空闲预测", "102", "团队负荷空闲预测占比"),
    /**
     * 正常预测
     */
    NORMAL_NEXT("正常预测", "201", "团队负荷正常预测占比"),
    /**
     * 满负荷预测
     */
    FULL_LOAD_NEXT("满负荷预测", "302", "团队负荷满负荷预测占比");

    /**
     * 名称
     */
    private String name;
    /**
     * code
     */
    private String code;
    /**
     * 原因
     */
    private String cause;

    ManageTypeEnum(String name, String code, String cause) {
        this.name = name;
        this.code = code;
        this.cause = cause;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCause() {
        return cause;
    }
}
