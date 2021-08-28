package com.taiping.enums.problem;


/**
 * 故障单一级分类机柜分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum CabinetTopEnum{

     /**
     * 服务器机柜
     */
    SERVER_CABINET("服务器机柜","1001"),
     /**
     * 服务器机柜
     */
    CONSOLE_CABINET("服务器机柜","1002"),
     /**
     * 网络机柜
     */
    NETWORK_CABINET("网络机柜","1003"),
     /**
     * 网络服务器机柜
     */
    NETWORK_SERVER_CABINET("网络服务器机柜","1004");


    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 分类编码
     */
    private String sortCode;


    CabinetTopEnum(String sortName, String sortCode) {
        this.sortName = sortName;
        this.sortCode = sortCode;
    }

    public String getSortName() {
        return sortName;
    }

    public String getSortCode() {
        return sortCode;
    }
}
