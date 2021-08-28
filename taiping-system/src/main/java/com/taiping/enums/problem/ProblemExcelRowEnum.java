package com.taiping.enums.problem;
/**
 * 问题分析Excel表行数据枚举类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-12
 */
public enum ProblemExcelRowEnum {
    /**
     * 故障单Excel行数据
     *
     * 故障单号
     */
    TICKET_CODE("故障单号", 0),
    /**
     * 报障人
     */
    REPORT_PERSON("报障人", 1),
    /**
     * 故障来源
     */
    TROUBLE_SOURCE("故障来源", 2),
    /**
     * 状态
     */
    STATUS("状态", 3),
    /**
     * 创建人
     */
    CREATE_PERSON("创建人", 4),
    /**
     * 创建时间
     */
    CREATE_TIME("创建时间", 5),
    /**
     * 故障发生时间
     */
    TROUBLE_TIME("故障发生时间", 6),
    /**
     * 故障发生地点
     */
    TROUBLE_LOCATION("故障发生地点", 7),
    /**
     * 故障分类
     */
    TROUBLE_TYPE("故障分类", 8),
    /**
     * 故障名称
     */
    TROUBLE_NAME("故障名称", 9),
    /**
     * 描述
     */
    DESCRIPTION("description", 10),
    /**
     * 影响度
     */
    INFLUENCE("影响度", 11),
    /**
     * 紧急度
     */
    URGENCY("紧急度", 12),
    /**
     * 故障级别
     */
    TROUBLE_LEVEL("故障级别", 13),
    /**
     * 故障处理描述
     */
    HANDLE_DESCRIPTION("故障处理描述", 14),
    /**
     * 故障解决描述
     */
    SOLVE_DESCRIPTION("故障解决描述", 15),
    /**
     * 处理组
     */
    HANDLE_GROUP("处理组", 16),
    /**
     * 处理人
     */
    HANDLE_PERSON("处理人", 17),
    /**
     * 故障原因定位
     */
    TROUBLE_CAUSE("故障原因定位", 18),
    /**
     * 解决人
     */
    SOLVE_PERSON("解决人", 19),
    /**
     * 解决时间
     */
    SOLVE_TIME("解决时间", 20),
    /**
     * 关闭类型
     */
    CLOSE_TYPE("关闭类型", 21),
    /**
     * 关闭人
     */
    CLOSE_PERSON("关闭人", 22),
    /**
     * 关闭时间
     */
    CLOSE_TIME("关闭时间", 23),
    /**
     * 更新时间
     */
    UPDATE_TIME("更新时间", 24),
    /**
     * 造成服务中断时长
     */
    INTERRUPT_DURATION("造成服务中断时长", 25),
    /**
     *  故障单所属（0物业1太平）
     */
    TICKET_TYPE("故障单所属", 26),
    /**
     * 停水停电记录Excel行数据
     *
     * 序号
     */
    SERIAL_NUMBER("序号", 0),
    /**
     * 日期
     */
    OFF_DATE("offDate", 1),
    /**
     * 原因(停水原因/停电原因)
     */
    CAUSE("cause", 2),
    /**
     * 造成影响
     */
    FLASH_OFF_INFLUENCE("influence", 3),
    /**
     * 停水时间/停电时间
     */
    START_TIME("startTime", 4),
    /**
     * 来水时间/来电时间
     */
    END_TIME("endTime", 5),
    /**
     * 备注
     */
    REMARK("remark", 6);

    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 单元格位置
     */
    private int cellNum;

    ProblemExcelRowEnum(String propertyName, int cellNum) {
        this.propertyName = propertyName;
        this.cellNum = cellNum;
    }


    public int getCellNum() {
        return cellNum;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
