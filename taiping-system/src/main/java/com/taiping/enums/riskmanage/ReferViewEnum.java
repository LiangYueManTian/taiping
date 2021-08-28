package com.taiping.enums.riskmanage;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 涉及界面枚举
 */
public enum ReferViewEnum {
    /**
     * 金科
     */
    JINKE("金科",1),
    /**
     * 物业
     */
    PROPERTY("物业",2);
    /**
     * 界面名称
     */
    private String  viewName;
    /**
     * 界面code
     */
    private Integer viewCode;

    ReferViewEnum(String viewName, Integer viewCode) {
        this.viewName = viewName;
        this.viewCode = viewCode;
    }

    public String getViewName() {
        return viewName;
    }

    public Integer getViewCode() {
        return viewCode;
    }

    public static Integer getCodeByName(String viewName) {
        for (ReferViewEnum referViewEnum : ReferViewEnum.values()) {
            if (referViewEnum.getViewName().equals(viewName)) {
                return referViewEnum.getViewCode();
            }
        }
        return null;
    }
}
