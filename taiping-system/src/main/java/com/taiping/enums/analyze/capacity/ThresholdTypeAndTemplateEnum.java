package com.taiping.enums.analyze.capacity;

/**
 * 阈值关系类型模板
 * @author hedongwei@wistronits.com
 * @date 2019/11/11 15:08
 */

public enum ThresholdTypeAndTemplateEnum {

    /**
     *  楼层分析
     */
    FLOOR(ThresholdTypeEnum.FLOOR.getType(), "楼层空间占比${operate} ${value}%"),


    /**
     *  机柜分析
     */
    CABINET(ThresholdTypeEnum.CABINET.getType(), "机柜空间占比${operate} ${value}%"),

    /**
     *  功能区分析
     */
    DEVICE_TYPE(ThresholdTypeEnum.DEVICE_TYPE.getType(), "功能区机柜占比${operate} ${value}%"),

    /**
     *  模块分析
     */
    MODULE(ThresholdTypeEnum.MODULE.getType(), "模块电力占比${operate} ${value}%"),

    /**
     *  ups分析
     */
    UPS(ThresholdTypeEnum.UPS.getType(), "ups电力占比${operate} ${value}%"),


    /**
     *  列头柜电力占比
     */
    CABINET_COLUMN(ThresholdTypeEnum.CABINET_COLUMN.getType(), "列头柜电力占比${operate} ${value}%"),

    /**
     *  第七个数据的模块
     */
    PDU(ThresholdTypeEnum.PDU.getType(), "PDU电力占比${operate} ${value}%"),

    /**
     *  第八个数据的模块
     */
    PORT_TYPE(ThresholdTypeEnum.PORT_TYPE.getType(), "综合布线已使用端口占比${operate} ${value}%");


    /**
     * 阈值类型
     */
    private String thresholdType;

    /**
     * 消息
     */
    private String msg;

    public String getThresholdType() {
        return thresholdType;
    }

    public String getMsg() {
        return msg;
    }

    ThresholdTypeAndTemplateEnum(String thresholdType, String msg) {
        this.thresholdType = thresholdType;
        this.msg = msg;
    }


    /**
     * 根据类型名称
     * @param type 模块类型
     * @return 类型名称
     */
    public static String getMsgByType(String type) {
        for (ThresholdTypeAndTemplateEnum typeEnum : ThresholdTypeAndTemplateEnum.values()) {
            if (typeEnum.getThresholdType().equals(type)) {
                return typeEnum.getMsg();
            }
        }
        return ThresholdTypeAndTemplateEnum.FLOOR.getMsg();
    }
}
