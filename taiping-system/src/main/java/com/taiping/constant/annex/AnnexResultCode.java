package com.taiping.constant.annex;
/**
 * 附件返回码
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
public class AnnexResultCode {
    /**
     * 传入参数错误
     */
    public static final Integer PARAM_ERROR = 170001;
    /**
     * 上传附件失败
     */
    public static final Integer UPLOAD_ANNEX_FAIL = 170101;
    /**
     * 附件名称重复
     */
    public static final Integer ANNEX_NAME_ERROR = 170102;
    /**
     * 其他附件名称重复
     */
    public static final Integer ANNEX_NAME_FAIL = 170103;
    /**
     * 附件已经被删除
     */
    public static final Integer ANNEX_IS_DELETED = 170104;
    /**
     * 删除FTP服务器附件失败
     */
    public static final Integer ANNEX_FTP_FAIL = 170105;
}
