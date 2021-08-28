package com.taiping.constant.annex;
/**
 * 附件返回信息
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
public class AnnexResultMsg {
    /**
     * 请求参数错误
     */
    public static final String PARAM_ERROR = "请求参数错误";
    /**
     * 上传附件失败
     */
    public static final String UPLOAD_ANNEX_FAIL = "上传附件失败";
    /**
     * 附件名称重复
     */
    public static final String ANNEX_NAME_ERROR = "该${mode}已有此名称附件";
    /**
     * 其他附件名称重复
     */
    public static final String ANNEX_NAME_FAIL = "其他${mode}有相同名称附件，确认上传将覆盖附件";
    /**
     * 附件已经被删除
     */
    public static final String ANNEX_IS_DELETED = "该附件已被删除";
    /**
     * 删除FTP服务器附件失败
     */
    public static final String ANNEX_FTP_FAIL = "删除FTP服务器附件失败";
}
