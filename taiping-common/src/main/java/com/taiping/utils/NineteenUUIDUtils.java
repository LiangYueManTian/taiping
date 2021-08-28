package com.taiping.utils;

import java.util.UUID;

/**
 * uuid 唯一值
 *
 * @author liyj
 * @date 2019/9/20
 */
public class NineteenUUIDUtils {
    public NineteenUUIDUtils() {
    }

    private static String digits(long val, int digits) {
        long hi = 1L << digits * 4;
        return BinaryConversionUtils.toString(hi | val & hi - 1L, BinaryConversionUtils.MAX_RADIX).substring(1);
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        stringBuilder.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        stringBuilder.append(digits(uuid.getMostSignificantBits(), 4));
        stringBuilder.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        stringBuilder.append(digits(uuid.getLeastSignificantBits(), 12));
        return stringBuilder.toString();
    }

}
