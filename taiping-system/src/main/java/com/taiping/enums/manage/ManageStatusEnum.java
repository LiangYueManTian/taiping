package com.taiping.enums.manage;
/**
 * 运维管理活动状态枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public enum ManageStatusEnum {
    /**
     * 提醒
     */
    REMIND("提醒", "0"),
    /**
     * 不提醒
     */
    NO_REMIND("已完成", "1"),
    /**
     * 未完成
     */
    INCOMPLETE("未完成", "0"),
    /**
     * 已完成
     */
    COMPLETED("已完成", "1"),
    /**
     * 可以裁剪
     */
    REDUCE("可以裁剪", "0"),
    /**
     * 不能裁剪
     */
    UN_REDUCE("不能裁剪", "1"),
    /**
     * 未复核
     */
    NO_REVIEW("未复核", "0"),
    /**
     * 复核不通过
     */
    REVIEW_NO("复核不通过", "1"),
    /**
     * 复核通过
     */
    REVIEW_YES("复核通过", "2");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    ManageStatusEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /**
     * 校验完成状态
     * @param type 类型
     * @return true 存在，false 不存在
     */
    public static boolean checkCompletion(String type) {
        return COMPLETED.type.equals(type) || INCOMPLETE.type.equals(type);
    }
    /**
     * 校验复核状态
     * @param type 类型
     * @return true 存在，false 不存在
     */
    public static boolean checkApproval(String type) {
        for (ManageStatusEnum manageStatusEnum : ManageStatusEnum.values()) {
            if (manageStatusEnum.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
