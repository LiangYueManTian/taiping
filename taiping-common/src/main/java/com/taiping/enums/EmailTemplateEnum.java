package com.taiping.enums;

/**
 * email 模板类型
 *
 * @author liyj
 * @date 2019/9/8
 */
public enum EmailTemplateEnum {
    /**
     * 模板示例1
     */
    SAMPLE_ONE("template01", "示例模板1"),
    /**
     * 太平模板
     */
    SAMPLE_TWO("taipingTemp", "太平系统模板"),
    /**
     * 维保预提醒模板
     */
    SAMPLE_THREE("template02", "维保预提醒模板"),
    /**
     * 运维管理活动提醒
     */
    MANAGE_REMINDER("manageTemp", "运维管理活动提醒模板");

    private String templateType;
    private String templateName;

    EmailTemplateEnum(String templateType, String templateName) {
        this.templateType = templateType;
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
