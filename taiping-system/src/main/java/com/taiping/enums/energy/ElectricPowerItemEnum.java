package com.taiping.enums.energy;

import org.springframework.util.ObjectUtils;

/**
 * 动力能耗电力分项枚举类
 *
 * @author hedongwei@wistronits.com
 * @since 2019-10-11
 */
public enum ElectricPowerItemEnum {

    /**
     *  第一个数据的模块
     */
    DATA_ONE("3AA1", "3AA1"),


    /**
     *  第二个数据的模块
     */
    DATA_TWO("3AA5", "3AA5"),

    /**
     *  第三个数据的模块
     */
    DATA_THREE("3AA6", "3AA6"),

    /**
     *  第四个数据的模块
     */
    DATA_FOUR("CH-A-1", "CH-A-1"),

    /**
     *  第五个数据的模块
     */
    DATA_FIVE("CH-A-2", "CH-A-2"),


    /**
     *  第六个数据的模块
     */
    DATA_SIX("CP-A-1", "CP-A-1"),

    /**
     *  第七个数据的模块
     */
    DATA_SEVEN("CP-A-2", "CP-A-2"),

    /**
     *  第八个数据的模块
     */
    DATA_EIGHT("CLP-A-1", "CLP-A-1"),


    /**
     *  第九个数据的模块
     */
    DATA_NINE("CLP-A-2", "CLP-A-2"),


    /**
     *  第十个数据的模块
     */
    DATA_TEN("2KTA-1", "2KTA-1"),


    /**
     *  第十一个数据的模块
     */
    DATA_ELEVEN("2KTA-2", "2KTA-2"),


    /**
     *  第十二个数据的模块
     */
    DATA_TWELVE("3KTA-1", "3KTA-1"),

    /**
     *  第十三个数据的模块
     */
    DATA_THIRTEEN("3KTA-2", "3KTA-2"),

    /**
     *  第十四个数据的模块
     */
    DATA_FOURTEEN("4KTA-1", "4KTA-1"),

    /**
     *  第十五个数据的模块
     */
    DATA_FIFTEEN("4KTA-2", "4KTA-2"),

    /**
     *  第十六个数据的模块
     */
    DATA_SIXTEEN("5KTA-1", "5KTA-1"),

    /**
     *  第十七个数据的模块
     */
    DATA_SEVENTEEN("5KTA-2", "5KTA-2"),

    /**
     *  第十八个数据的模块
     */
    DATA_EIGHTEEN("CT-A-1", "CT-A-1"),

    /**
     *  第十九个数据的模块
     */
    DATA_NINETEEN("CT-A-2", "CT-A-2"),


    /**
     *  第二十个数据的模块
     */
    DATA_TWENTY("4AA1", "4AA1"),


    /**
     *  第二十一个数据的模块
     */
    DATA_TWENTY_ONE("4AA5", "4AA5"),


    /**
     *  第二十二个数据的模块
     */
    DATA_TWENTY_TWO("4AA6", "4AA6"),

    /**
     *  第二十三个数据的模块
     */
    DATA_TWENTY_THREE("CH-A-3", "CH-A-3"),

    /**
     *  第二十四个数据的模块
     */
    DATA_TWENTY_FOUR("CH-A-4", "CH-A-4"),

    /**
     *  第二十五个数据的模块
     */
    DATA_TWENTY_FIVE("CP-A-3", "CP-A-3"),

    /**
     *  第二十六个数据的模块
     */
    DATA_TWENTY_SIX("CP-A-4", "CP-A-4"),

    /**
     *  第二十七个数据的模块
     */
    DATA_TWENTY_SEVEN("CLP-A-3", "CLP-A-3"),

    /**
     *  第二十八个数据的模块
     */
    DATA_TWENTY_EIGHT("CLP-A-4", "CLP-A-4"),

    /**
     *  第二十九个数据的模块
     */
    DATA_TWENTY_NINE("2KTB-1", "2KTB-1"),

    /**
     *  第三十个数据的模块
     */
    DATA_THIRTY("2KTB-2", "2KTB-2"),


    /**
     *  第三十一个数据的模块
     */
    DATA_THIRTY_ONE("3KTB-1", "3KTB-1"),


    /**
     *  第三十二个数据的模块
     */
    DATA_THIRTY_TWO("3KTB-2", "3KTB-2"),


    /**
     *  第三十三个数据的模块
     */
    DATA_THIRTY_THREE("4KTB-1", "4KTB-1"),



    /**
     *  第三十四个数据的模块
     */
    DATA_THIRTY_FOUR("4KTB-2", "4KTB-2"),

    /**
     *  第三十五个数据的模块
     */
    DATA_THIRTY_FIVE("5KTB-1", "5KTB-1"),

    /**
     *  第三十六个数据的模块
     */
    DATA_THIRTY_SIX("5KTB-2", "5KTB-2"),

    /**
     *  第三十七个数据的模块
     */
    DATA_THIRTY_SEVEN("CT-A-3", "CT-A-3"),

    /**
     *  第三十八个数据的模块
     */
    DATA_THIRTY_EIGHT("CT-A-4", "CT-A-4");


    private String dataName;

    private String typeName;


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param name 数据名称
     * @return 类型code
     */
    public static String getTypeByData(String name) {
        for (ElectricPowerItemEnum itTypeAndDataEnum : ElectricPowerItemEnum.values()) {
            if (itTypeAndDataEnum.getDataName().equals(name)) {
                return itTypeAndDataEnum.getTypeName();
            }
        }
        return "";
    }


    /**
     * 根据数据类型获取code
     * @author hedongwei@wistronits.com
     * @date  2019/10/29 10:10
     * @param realData 数据名称
     * @return 类型code
     */
    public static String getTypeByRealData(String realData) {
        if (!ObjectUtils.isEmpty(realData)) {
            for (ElectricPowerItemEnum itTypeAndDataEnum : ElectricPowerItemEnum.values()) {
                if (realData.indexOf(itTypeAndDataEnum.getDataName()) != -1) {
                    return itTypeAndDataEnum.getDataName();
                }
            }
        }
        return "";
    }

    ElectricPowerItemEnum(String dataName, String typeName) {
        this.dataName = dataName;
        this.typeName = typeName;
    }

    public String getDataName() {
        return dataName;
    }

    public String getTypeName() {
        return typeName;
    }
}
