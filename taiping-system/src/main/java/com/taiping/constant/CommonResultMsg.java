package com.taiping.constant;
/**
 * 公共服务返回信息
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-1
 */
public class CommonResultMsg {

    /**
     * 行数据为空
     */
    public static final String ROW_CELL_ERROR = "第${row}行第(${cell})格数据错误，获取为空";
    /**
     * 行数据日期格式错误
     */
    public static final String ROW_CELL_DATE_ERROR = "第${row}行第(${cell})格日期格式错误";
    /**
     * 行数据日期格式错误
     */
    public static final String ROW_CELL_LENGTH_ERROR = "第${row}行数据${cell}长度超过${name}";
}
