package com.taiping.enums.analyze.energy;

/**
 * 能耗分析模块枚举类
 * @author hedongwei@wistronits.com
 * @date 2019/11/5 15:32
 */
public enum EnergyStatisticalModelEnum {

    /**
     * 能耗分项
     */
    POWER_METER("能耗分项", "1"),

    /**
     * 总能耗
     */
    ALL_METER("总能耗", "2"),

    /**
     * PUE值
     */
    PUE("PUE值", "3");


    /**
     *
     */
    private String modelName;

    /**
     * 模块code
     */
    private String model;

    EnergyStatisticalModelEnum(String modelName, String model) {
        this.modelName = modelName;
        this.model = model;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModel() {
        return model;
    }

    /**
     * 根据获取模块
     * @param modelName 模块Name
     * @return 模块
     */
    public static String getModel(String modelName) {
        for (EnergyStatisticalModelEnum modelEnum : EnergyStatisticalModelEnum.values()) {
            if (modelEnum.getModelName().equals(modelName)) {
                return modelEnum.getModel();
            }
        }
        return EnergyStatisticalModelEnum.POWER_METER.getModel();
    }


    /**
     * 根据获取模块名称
     * @param model 模块
     * @return 模块名称
     */
    public static String getModelName(String model) {
        for (EnergyStatisticalModelEnum modelEnum : EnergyStatisticalModelEnum.values()) {
            if (modelEnum.getModel().equals(model)) {
                return modelEnum.getModelName();
            }
        }
        return EnergyStatisticalModelEnum.POWER_METER.getModelName();
    }
}
