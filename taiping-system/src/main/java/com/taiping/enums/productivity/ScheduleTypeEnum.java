package com.taiping.enums.productivity;
/**
 * 常白班排班和倒班排班班次类别枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-11
 */
public enum ScheduleTypeEnum {
    /**
     * 白班
     */
    ONE_DAY("常白班", "常白班排班", "1"),
    /**
     * 白班
     */
    DAY("白班", "倒班排班", "2"),
    /**
     * 晚班
     */
    NIGHT("晚班", "倒班排班", "3");

    /**
     * 名称
     */
    private String name;
    /**
     * 名称
     */
    private String scheduleName;
    /**
     * 类型
     */
    private String type;

    ScheduleTypeEnum(String name, String scheduleName, String type) {
        this.name = name;
        this.scheduleName = scheduleName;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static String getTypeByName(String name) {
        for (ScheduleTypeEnum scheduleTypeEnum : ScheduleTypeEnum.values()) {
            if (scheduleTypeEnum.getName().equals(name)) {
                return scheduleTypeEnum.getType();
            }
        }
        return null;
    }

    public String getScheduleName() {
        return scheduleName;
    }
}
