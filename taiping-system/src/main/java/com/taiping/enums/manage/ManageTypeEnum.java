package com.taiping.enums.manage;
/**
 * 运维管理活动来源枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
public enum ManageTypeEnum {
    /**
     * 本月生成
     */
    CURRENT("当前", "1"),
    /**
     * 上月生成本月生成出现相同对象（可选择取消）
     */
    CAN_CANCEL("可取消", "2"),
    /**
     * 历史
     */
    HISTORY("历史", "3"),
    /**
     * 已取消
     */
    CANCELLED("已取消", "4"),
    /**
     * 用户新增
     */
    USER_CREATE("用户新增", "5");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    ManageTypeEnum(String name, String type) {
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
