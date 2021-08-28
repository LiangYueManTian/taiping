package com.taiping.enums.energy;

/**
 * 动力能耗统计枚举类
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerStatisticalEnum {

    /**
     *  第一个数据的模块
     */
    DATA_ONE(ElectricPowerItemEnum.DATA_TEN.getDataName(), true),


    /**
     *  第二个数据的模块
     */
    DATA_TWO(ElectricPowerItemEnum.DATA_ELEVEN.getDataName(), true),


    /**
     *  第三个数据的模块
     */
    DATA_THREE(ElectricPowerItemEnum.DATA_TWELVE.getDataName(), true),

    /**
     *  第四个数据的模块
     */
    DATA_FOUR(ElectricPowerItemEnum.DATA_THIRTEEN.getDataName(), true),

    /**
     *  第五个数据的模块
     */
    DATA_FIVE(ElectricPowerItemEnum.DATA_FOURTEEN.getDataName(), false),

    /**
     *  第六个数据的模块
     */
    DATA_SIX(ElectricPowerItemEnum.DATA_FIFTEEN.getDataName(), false),

    /**
     *  第七个数据的模块
     */
    DATA_SEVEN(ElectricPowerItemEnum.DATA_SIXTEEN.getDataName(), false),

    /**
     *  第八个数据的模块
     */
    DATA_EIGHT(ElectricPowerItemEnum.DATA_SEVENTEEN.getDataName(), false),

    /**
     *  第九个数据的模块
     */
    DATA_NINE(ElectricPowerItemEnum.DATA_TWENTY_NINE.getDataName(), true),

    /**
     *  第十个数据的模块
     */
    DATA_TEN(ElectricPowerItemEnum.DATA_THIRTY.getDataName(), true),


    /**
     *  第十一个数据的模块
     */
    DATA_ELEVEN(ElectricPowerItemEnum.DATA_THIRTY_ONE.getDataName(), true),


    /**
     *  第十二个数据的模块
     */
    DATA_TWELVE(ElectricPowerItemEnum.DATA_THIRTY_TWO.getDataName(), true),


    /**
     *  第十三个数据的模块
     */
    DATA_THIRTEEN(ElectricPowerItemEnum.DATA_THIRTY_THREE.getDataName(), false),



    /**
     *  第十四个数据的模块
     */
    DATA_FOURTEEN(ElectricPowerItemEnum.DATA_THIRTY_FOUR.getDataName(), false),

    /**
     *  第十五个数据的模块
     */
    DATA_FIFTEEN(ElectricPowerItemEnum.DATA_THIRTY_FIVE.getDataName(), false),

    /**
     *  第十六个数据的模块
     */
    DATA_SIXTEEN(ElectricPowerItemEnum.DATA_THIRTY_SIX.getDataName(), false);


    private String dataName;

    private boolean result;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static boolean getResultByName(String name) {
        for (ElectricPowerStatisticalEnum resultEnum : ElectricPowerStatisticalEnum.values()) {
            if (resultEnum.getDataName().equals(name)) {
                return resultEnum.getResult();
            }
        }
        return false;
    }


    /**
     * 根据数据返回获取数据名称
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param result 数据result
     * @return 数据名称
     */
    public static String getDataNameByResult(boolean result) {
        for (ElectricPowerStatisticalEnum resultEnum : ElectricPowerStatisticalEnum.values()) {
            if (resultEnum.getResult() == result) {
                return resultEnum.getDataName();
            }
        }
        return "";
    }

    ElectricPowerStatisticalEnum(String dataName, boolean result) {
        this.dataName = dataName;
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public String getDataName() {
        return dataName;
    }
}
