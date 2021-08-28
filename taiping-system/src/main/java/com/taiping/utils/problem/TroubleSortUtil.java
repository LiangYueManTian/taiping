package com.taiping.utils.problem;

import com.taiping.constant.problem.ProblemConstant;
import com.taiping.enums.problem.*;

/**
 * 故障单二级分类获取code工具类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-04
 */
public class TroubleSortUtil {
    /**
     * 根据名称获取一级分类code
     *
     * @param sortName 一级分类名称
     * @return String 一级分类code
     */
    public static String getCodeForName(String sortName) {
        sortName = sortName.trim();
        for (TroubleSortEnum topEnum : TroubleSortEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据名称获取一级分类code
     *
     * @param sortName 一级分类名称
     * @return String 一级分类code
     */
    public static String getNameForCode(String sortCode) {
        for (TroubleSortEnum topEnum : TroubleSortEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }

    /**
     * 根据一级分类code、名称获取二级分类code
     * @param sortCode 一级分类code
     * @param sortName 二级分类名称
     * @return  String  二级分类code
     */
    public static String getCodeForName(String sortCode, String sortName) {
        sortName = sortName.trim();
        if (TroubleSortEnum.CABINET.getSortCode().equals(sortCode)) {
            return getCodeOfCabinet(sortName);
        }
        if (TroubleSortEnum.MONITORING_COLLECTION.getSortCode().equals(sortCode)) {
            return getCodeOfMonCon(sortName);
        }
        if (TroubleSortEnum.POWER_DISTRIBUTION.getSortCode().equals(sortCode)) {
            return getCodeOfPowerDist(sortName);
        }
        if (TroubleSortEnum.ENVIRONMENTAL_AIR_CONDITIONER.getSortCode().equals(sortCode)) {
            return getCodeOfEnvAirCon(sortName);
        }
        if (TroubleSortEnum.SAFETY_PRECAUTION.getSortCode().equals(sortCode)) {
            return getCodeOfSafetyPre(sortName);
        }
        return "";
    }
    /**
     * 根据一级分类code、二级分类code获取分类名称
     * @param topCode 一级分类code
     * @param sortCode 二级分类code
     * @return  String  分类名称
     */
    public static String getNameForCode(String topCode, String sortCode) {
        StringBuilder troubleType = new StringBuilder(ProblemConstant.TROUBLE_TYPE_START);
        if (TroubleSortEnum.CABINET.getSortCode().equals(topCode)) {
            troubleType.append(TroubleSortEnum.CABINET.getSortName())
                    .append(ProblemConstant.TROUBLE_TYPE_CONNECT);
            return troubleType.append(getNameOfCabinet(sortCode)).toString();
        }
        if (TroubleSortEnum.MONITORING_COLLECTION.getSortCode().equals(topCode)) {
            troubleType.append(TroubleSortEnum.MONITORING_COLLECTION.getSortName())
                    .append(ProblemConstant.TROUBLE_TYPE_CONNECT);
            return troubleType.append(getNameOfMonCon(sortCode)).toString();
        }
        if (TroubleSortEnum.POWER_DISTRIBUTION.getSortCode().equals(topCode)) {
            troubleType.append(TroubleSortEnum.POWER_DISTRIBUTION.getSortName())
                    .append(ProblemConstant.TROUBLE_TYPE_CONNECT);
            return troubleType.append(getNameOfPowerDist(sortCode)).toString();
        }
        if (TroubleSortEnum.ENVIRONMENTAL_AIR_CONDITIONER.getSortCode().equals(topCode)) {
            troubleType.append(TroubleSortEnum.ENVIRONMENTAL_AIR_CONDITIONER.getSortName())
                    .append(ProblemConstant.TROUBLE_TYPE_CONNECT);
            return troubleType.append(getNameOfEnvAirCon(sortCode)).toString();
        }
        if (TroubleSortEnum.SAFETY_PRECAUTION.getSortCode().equals(topCode)) {
            troubleType.append(TroubleSortEnum.SAFETY_PRECAUTION.getSortName())
                    .append(ProblemConstant.TROUBLE_TYPE_CONNECT);
            return troubleType.append(getNameOfSafetyPre(sortCode)).toString();
        }
        return "";
    }

    /**
     * 根据一级分类code、二级分类code获取分类名称
     * @param topCode 一级分类code
     * @param sortCode 二级分类code
     * @return  String  分类名称
     */
    public static String getSecondaryNameForCode(String topCode, String sortCode) {
        if (TroubleSortEnum.CABINET.getSortCode().equals(topCode)) {
            return getNameOfCabinet(sortCode);
        }
        if (TroubleSortEnum.MONITORING_COLLECTION.getSortCode().equals(topCode)) {
            return getNameOfMonCon(sortCode);
        }
        if (TroubleSortEnum.POWER_DISTRIBUTION.getSortCode().equals(topCode)) {
            return getNameOfPowerDist(sortCode);
        }
        if (TroubleSortEnum.ENVIRONMENTAL_AIR_CONDITIONER.getSortCode().equals(topCode)) {
            return getNameOfEnvAirCon(sortCode);
        }
        if (TroubleSortEnum.SAFETY_PRECAUTION.getSortCode().equals(topCode)) {
            return getNameOfSafetyPre(sortCode);
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类名称获取二级分类code
     *
     * @param sortName 二级分类名称
     * @return String 二级分类code
     */
    private static String getCodeOfCabinet(String sortName) {
        for (CabinetTopEnum topEnum : CabinetTopEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据一级分类环境空调二级分类名称获取二级分类code
     *
     * @param sortName 二级分类名称
     * @return String 二级分类code
     */
    private static String getCodeOfEnvAirCon(String sortName) {
        for (EnvAirConTopEnum topEnum : EnvAirConTopEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据一级分类监控采集二级分类名称获取二级分类code
     *
     * @param sortName 二级分类名称
     * @return String 二级分类code
     */
    private static String getCodeOfMonCon(String sortName) {
        for (MonConTopEnum topEnum : MonConTopEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据一级分类供电配电二级分类名称获取二级分类code
     *
     * @param sortName 二级分类名称
     * @return String 二级分类code
     */
    private static String getCodeOfPowerDist(String sortName) {
        for (PowerDistTopEnum topEnum : PowerDistTopEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据一级分类安全防范二级分类名称获取二级分类code
     *
     * @param sortName 二级分类名称
     * @return String 二级分类code
     */
    private static String getCodeOfSafetyPre(String sortName) {
        for (SafetyPreTopEnum topEnum : SafetyPreTopEnum.values()) {
            if (topEnum.getSortName().equals(sortName)) {
                return topEnum.getSortCode();
            }
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类code获取二级分类名称
     *
     * @param sortCode 二级分类code
     * @return String  二级分类名称
     */
    private static String getNameOfCabinet(String sortCode) {
        for (CabinetTopEnum topEnum : CabinetTopEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类code获取二级分类名称
     *
     * @param sortCode 二级分类code
     * @return String  二级分类名称
     */
    private static String getNameOfEnvAirCon(String sortCode) {
        for (EnvAirConTopEnum topEnum : EnvAirConTopEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类code获取二级分类名称
     *
     * @param sortCode 二级分类code
     * @return String  二级分类名称
     */
    private static String getNameOfMonCon(String sortCode) {
        for (MonConTopEnum topEnum : MonConTopEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类code获取二级分类名称
     *
     * @param sortCode 二级分类code
     * @return String  二级分类名称
     */
    private static String getNameOfPowerDist(String sortCode) {
        for (PowerDistTopEnum topEnum : PowerDistTopEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }
    /**
     * 根据一级分类机柜二级分类code获取二级分类名称
     *
     * @param sortCode 二级分类code
     * @return String  二级分类名称
     */
    private static String getNameOfSafetyPre(String sortCode) {
        for (SafetyPreTopEnum topEnum : SafetyPreTopEnum.values()) {
            if (topEnum.getSortCode().equals(sortCode)) {
                return topEnum.getSortName();
            }
        }
        return "";
    }
}
