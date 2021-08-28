package com.taiping.enums.productivity;
/**
 * 团对负荷资源状态类别枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-12
 */
public enum WorkloadTypeEnum {

    /**
     * 空闲
     */
    FREE("空闲", "1"),
    /**
     * 正常
     */
    NORMAL("正常", "2"),
    /**
     * 满负荷
     */
    FULL_LOAD("满负荷", "3"),
    /**
     * 实际值
     */
    CURRENT("实际值", "0"),
    /**
     * 预测值
     */
    FORECAST("预测值", "1");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    WorkloadTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
