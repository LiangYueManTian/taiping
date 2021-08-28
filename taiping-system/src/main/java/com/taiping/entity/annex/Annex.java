package com.taiping.entity.annex;

import com.taiping.constant.annex.AnnexConstant;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 附件实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-25
 */
@Data
public class Annex {
    /**
     * 主键id
     */
    private String annexId;
    /**
     * 附件名称
     */
    private String annexName;
    /**
     * 关联id
     */
    private String manageId;
    /**
     * 所属模块
     */
    private String mode;
    /**
     * 附件文件下载路径
     */
    private String annexPath;
    /**
     * 附件上传时间
     */
    private Long uploadTime;

    /**
     * 校验参数
     * @return true 正确 false错误
     */
    public boolean checkName() {
        if (StringUtils.isEmpty(manageId) || StringUtils.isEmpty(annexName)
                || StringUtils.isEmpty(mode)) {
            return false;
        }
        return annexName.length() <= AnnexConstant.ANNEX_NAME_LENGTH;
    }

    /**
     * 校验参数
     * @return true 正确 false错误
     */
    public boolean check() {
        return !StringUtils.isEmpty(manageId) && !StringUtils.isEmpty(mode);
    }


}
