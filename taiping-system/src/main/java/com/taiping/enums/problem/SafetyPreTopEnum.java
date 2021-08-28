package com.taiping.enums.problem;


/**
 * 故障单一级分类安全防范分类枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum SafetyPreTopEnum {

    /**
     * 防盗报警主机
     */
    BURGLAR_ALARM_HOST("防盗报警主机","5001"),
    /**
     * 烟雾检测
     */
    SMOKE_DETECTION("烟雾检测","5002"),
    /**
     * 门禁遥控器
     */
    ACCESS_CONTROL_REMOTE("门禁遥控器","5003"),
    /**
     * 红外检测
     */
    INFRARED_DETECTION("红外检测","5004"),
    /**
     * 综合保护装置
     */
    INTEGRATED_PROTECTION_DEVICE("综合保护装置","5005"),
    /**
     * 粉尘检测
     */
    DUST_DETECTION("粉尘检测","5006"),
    /**
     * 视频摄像头
     */
    VIDEO_CAMERA("视频摄像头","5007"),
    /**
     * 网络硬盘录像机
     */
    NETWORK_VIDEO_RECORDER("网络硬盘录像机","5008"),
    /**
     * 二氧化碳检测
     */
    CARBON_DIOXIDE_DETECTION("二氧化碳检测","5009"),
    /**
     * 氢气检测
     */
    HYDROGEN_DETECTION("氢气检测","5010"),
    /**
     * 视频矩阵
     */
    VIDEO_MATRIX("视频矩阵","5011"),
    /**
     * 及早期烟雾检测
     */
    EARLY_SMOKE_DETECTION("及早期烟雾检测","5012"),
    /**
     * 避雷针
     */
    LIGHTNING_ROD("避雷针","5013"),
    /**
     * 浪涌抑制器
     */
    SURGE_SUPPRESSOR("浪涌抑制器","5014");


    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 分类编码
     */
    private String sortCode;


    SafetyPreTopEnum(String sortName, String sortCode) {
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
