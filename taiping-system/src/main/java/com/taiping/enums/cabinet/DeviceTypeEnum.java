package com.taiping.enums.cabinet;

/**
 * 设备功能区类型枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum DeviceTypeEnum {

    /**
     * pc服务器
     */
    DEVICE_TYPE_ONE("PC服务器", "1"),

    /**
     * 网络
     */
    DEVICE_TYPE_TWO("网络", "2"),

    /**
     * 标准存储
     */
    DEVICE_TYPE_THREE("标准存储", "3"),

    /**
     * 刀片
     */
    DEVICE_TYPE_FOUR("刀片", "4"),

    /**
     * 非标设备
     */
    DEVICE_TYPE_FIVE("非标设备", "5"),

    /**
     * 小型机
     */
    DEVICE_TYPE_SIX("小型机", "6");

    private String deviceTypeName;

    private String deviceType;

    DeviceTypeEnum(String deviceTypeName, String deviceType) {
        this.deviceTypeName = deviceTypeName;
        this.deviceType = deviceType;
    }

    /**
     * 根据功能区类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 功能区名称
     * @return 功能区类型code
     */
    public static String getDeviceTypeByName(String name) {
        for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
            if (deviceTypeEnum.getDeviceTypeName().equals(name)) {
                return deviceTypeEnum.getDeviceType();
            }
        }
        return "";
    }

    /**
     * 根据功能区类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param deviceType 功能区类型
     * @return 功能区类型code
     */
    public static String getDeviceTypeNameByCode(String deviceType) {
        for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
            if (deviceTypeEnum.getDeviceType().equals(deviceType)) {
                return deviceTypeEnum.getDeviceTypeName();
            }
        }
        return "";
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
