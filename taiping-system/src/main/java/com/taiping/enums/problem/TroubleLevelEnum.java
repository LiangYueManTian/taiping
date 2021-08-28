package com.taiping.enums.problem;
/**
 * 故障单级别枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public enum TroubleLevelEnum {
    /**
     * 极低
     */
    VERY_LOW("极低", "01", "极低级别故障", "100301"),
    /**
     * 低
     */
    LOW("低", "02", "低级别故障", "100302"),
    /**
     * 中
     */
    MODERATE("中", "03", "中级别故障", "100303"),
    /**
     * 高
     */
    HIGH("高", "04", "高级别故障", "100304"),
    /**
     * 极高
     */
    VERY_HIGH("极高", "05", "极高级别故障", "100305");
    /**
     * 级别名称
     */
    private String levelName;
    /**
     * 类型编码
     */
    private String levelCode;
    /**
     * 运维管理活动来源对象名称
     */
    private String manageName;
    /**
     * 运维管理活动来源对象code
     */
    private String manageCode;

    TroubleLevelEnum(String levelName, String levelCode, String manageName, String manageCode) {
        this.levelName = levelName;
        this.levelCode = levelCode;
        this.manageName = manageName;
        this.manageCode = manageCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public static String getCodeForName(String levelName) {
        for (TroubleLevelEnum levelEnum : TroubleLevelEnum.values()) {
            if (levelEnum.getLevelName().equals(levelName)) {
                return levelEnum.getLevelCode();
            }
        }
        return null;
    }

    public String getManageName() {
        return manageName;
    }

    public String getManageCode() {
        return manageCode;
    }
}
