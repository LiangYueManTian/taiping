package com.taiping.enums.cabling;

/**
 * 线缆类型枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum GenericCablingStatusEnum {

    /**
     * 使用中
     */
    STATUS_ONE("使用中", "1"),

    /**
     * 预占用
     */
    STATUS_TWO("预占(变更)", "1"),

    /**
     * 空闲
     */
    STATUS_THREE("空闲", "2");

    private String statusName;

    private String status;

    GenericCablingStatusEnum(String statusName, String status) {
        this.statusName = statusName;
        this.status = status;
    }

    /**
     * 根据线缆类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 线缆类型名称
     * @return 线缆类型code
     */
    public static String getStatusByName(String name) {
        for (GenericCablingStatusEnum statusEnum : GenericCablingStatusEnum.values()) {
            if (statusEnum.getStatusName().equals(name)) {
                return statusEnum.getStatus();
            }
        }
        return "";
    }


    /**
     * 根据线缆类型获取名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param code 线缆类型code
     * @return 线缆类型名称
     */
    public static String getStatusByCode(String code) {
        for (GenericCablingStatusEnum statusEnum : GenericCablingStatusEnum.values()) {
            if (statusEnum.getStatus().equals(code)) {
                return statusEnum.getStatusName();
            }
        }
        return "";
    }


    public String getStatusName() {
        return statusName;
    }

    public String getStatus() {
        return status;
    }
}
