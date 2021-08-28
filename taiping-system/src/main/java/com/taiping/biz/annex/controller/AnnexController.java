package com.taiping.biz.annex.controller;

import com.taiping.biz.annex.service.AnnexService;
import com.taiping.constant.annex.AnnexResultCode;
import com.taiping.constant.annex.AnnexResultMsg;
import com.taiping.entity.Result;
import com.taiping.entity.annex.Annex;
import com.taiping.utils.DateFormatUtils;
import com.taiping.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 附件管理控制层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-30
 */
@RestController
@RequestMapping("/taiping/annex")
public class AnnexController {

    @Autowired
    private AnnexService annexService;

    @GetMapping("/test")
    public Long test() {
        return DateFormatUtils.getDayStartForTime(System.currentTimeMillis());
    }

    /**
     * 校验附件名称
     * @param annex 附件
     * @return Result
     */
    @PostMapping("/checkAnnexName")
    public Result checkAnnexName(@RequestBody Annex annex) {
        if (annex == null || !annex.checkName()) {
            return ResultUtils.warn(AnnexResultCode.PARAM_ERROR,
                    AnnexResultMsg.PARAM_ERROR);
        }
        return annexService.checkAnnexName(annex);
    }

    /**
     * 上传附件
     * @param annex 关联ID
     * @param file 附件
     * @return Result
     */
    @PostMapping("/uploadAnnex")
    public Result uploadAnnex(Annex annex, @RequestBody MultipartFile file) {
        if (annex == null || file == null
                || !annex.check() || file.isEmpty()) {
            return ResultUtils.warn(AnnexResultCode.PARAM_ERROR,
                    AnnexResultMsg.PARAM_ERROR);
        }
        return annexService.uploadAnnex(annex, file);
    }

    /**
     * 删除附件
     * @param annexId 附件ID
     * @return Result
     */
    @GetMapping("/deleteAnnex/{annexId}")
    public Result deleteAnnex(@PathVariable String annexId) {
        if (StringUtils.isEmpty(annexId)) {
            return ResultUtils.warn(AnnexResultCode.PARAM_ERROR,
                    AnnexResultMsg.PARAM_ERROR);
        }
        return annexService.deleteAnnex(annexId);
    }
    /**
     * 查询附件列表
     * @param manageId 关联ID
     * @return Result
     */
    @GetMapping("/selectAnnexList/{manageId}")
    public Result selectAnnexList(@PathVariable String manageId) {
        if (StringUtils.isEmpty(manageId)) {
            return ResultUtils.warn(AnnexResultCode.PARAM_ERROR,
                    AnnexResultMsg.PARAM_ERROR);
        }
        return annexService.selectAnnexList(manageId);
    }

}
