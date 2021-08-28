package com.taiping.enums.manage;
/**
 * 类型参数枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public enum ParamTypeEnum {
    /**
     * 初始化
     */
    INITIALIZE("初始化", "0"),
    /**
     * 已分析
     */
    ANALYZE("已分析", "1"),
    /**
     * 已预览
     */
    PREVIEW("已预览", "2"),
    /**
     * 提交审批
     */
    SUBMIT("提交审批", "3"),
    /**
     * 审批通过
     */
    APPROVAL_YES("审批通过", "4"),
    /**
     * 审批未通过
     */
    APPROVAL_NO("审批未通过", "5");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    ParamTypeEnum(String name, String type) {
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
