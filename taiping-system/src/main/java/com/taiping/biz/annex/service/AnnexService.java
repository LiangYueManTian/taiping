package com.taiping.biz.annex.service;

import com.taiping.entity.Result;
import com.taiping.entity.annex.Annex;
import org.springframework.web.multipart.MultipartFile;

/**
 * 附件管理活动服务层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
public interface AnnexService {

    /**
     * 校验附件名称
     * @param annex 附件
     * @return Result
     */
    Result checkAnnexName(Annex annex);

    /**
     * 上传附件
     * @param annex 关联ID 模块
     * @param file 附件
     * @return Result
     */
    Result uploadAnnex(Annex annex, MultipartFile file);

    /**
     * 删除附件
     * @param annexId 附件ID
     * @return Result
     */
    Result deleteAnnex(String annexId);
    /**
     * 查询附件列表
     * @param manageId 关联ID
     * @return Result
     */
    Result selectAnnexList(String manageId);
}
