package com.taiping.biz.annex.service.impl;

import com.taiping.biz.annex.dao.AnnexDao;
import com.taiping.biz.annex.service.AnnexService;
import com.taiping.constant.annex.AnnexResultCode;
import com.taiping.constant.annex.AnnexResultMsg;
import com.taiping.entity.FtpSetting;
import com.taiping.entity.Result;
import com.taiping.entity.annex.Annex;
import com.taiping.enums.annex.AnnexTypeEnum;
import com.taiping.exception.BizException;
import com.taiping.utils.FtpUtils;
import com.taiping.utils.NineteenUUIDUtils;
import com.taiping.utils.ResultUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件管理服务实现层
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-24
 */
@Service
public class AnnexServiceImpl implements AnnexService {

    @Autowired
    private AnnexDao annexDao;

    @Autowired
    private FtpSetting ftpSetting;
    /**
     * 错误替换
     */
    private static final String ERROR_MSG_REPLACE = "${mode}";

    /**
     * 校验附件名称
     *
     * @param annex@return Result
     */
    @Override
    public Result checkAnnexName(Annex annex) {
        Annex annex1 = annexDao.queryAnnexByNameAndId(annex);
        if (annex1 != null && annex1.getAnnexId() != null) {
            return ResultUtils.warn(AnnexResultCode.ANNEX_NAME_ERROR,
                    getMsg(AnnexResultMsg.ANNEX_NAME_ERROR, annex.getMode()));
        }
        int annexNum = annexDao.queryAnnexByName(annex);
        if (annexNum > 0) {
            return ResultUtils.warn(AnnexResultCode.ANNEX_NAME_FAIL,
                    getMsg(AnnexResultMsg.ANNEX_NAME_FAIL, annex.getMode()));
        }
        return ResultUtils.success();
    }

    /**
     * 上传附件
     *
     * @param annex 关联ID 模块
     * @param file     附件
     * @return Result
     */
    @Override
    public Result uploadAnnex(Annex annex, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        annex.setAnnexId(NineteenUUIDUtils.uuid());
        annex.setAnnexName(fileName);
        annex.setUploadTime(System.currentTimeMillis());
        String path = getPath(annex.getMode());
        boolean uploadFile = FtpUtils.uploadFile(ftpSetting.getHost(), ftpSetting.getUserName(),
                ftpSetting.getPassword(), ftpSetting.getPort(), path, file);
        if (uploadFile) {
            annex.setAnnexPath(ftpSetting.getStorePath(path, fileName));
        } else {
            return ResultUtils.warn(AnnexResultCode.UPLOAD_ANNEX_FAIL,
                    AnnexResultMsg.UPLOAD_ANNEX_FAIL);
        }
        try {
            annexDao.addAnnex(annex);
        } catch (Exception e) {
            return ResultUtils.warn(AnnexResultCode.UPLOAD_ANNEX_FAIL,
                    AnnexResultMsg.UPLOAD_ANNEX_FAIL);
        }
        return ResultUtils.success();
    }

    /**
     * 删除附件
     *
     * @param annexId 附件ID
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteAnnex(String annexId) {
        Annex annex = annexDao.findAnnexById(annexId);
        if (annex == null) {
            return ResultUtils.warn(AnnexResultCode.ANNEX_IS_DELETED,
                    AnnexResultMsg.ANNEX_IS_DELETED);
        }
        String annexName = annex.getAnnexName();
        int count = annexDao.queryAnnexByName(annex);
        annexDao.deleteAnnex(annexId);
        boolean deleteFile = true;
        String path = getPath(annex.getMode());
        if (count == 1) {
            deleteFile = FtpUtils.deleteFile(ftpSetting.getHost(), ftpSetting.getUserName(),
                    ftpSetting.getPassword(), ftpSetting.getPort(), path, annexName);
        }
        if (!deleteFile) {
            throw new BizException(AnnexResultCode.ANNEX_FTP_FAIL,
                    AnnexResultMsg.ANNEX_FTP_FAIL);
        }
        return ResultUtils.success();
    }

    /**
     * 查询附件列表
     *
     * @param manageId 关联ID
     * @return Result
     */
    @Override
    public Result selectAnnexList(String manageId) {
        List<Annex> annexList = annexDao.selectAnnexList(manageId);
        if (CollectionUtils.isEmpty(annexList)) {
            annexList = new ArrayList<>();
        }
        return  ResultUtils.success(annexList);
    }

    /**
     * 获取对应模块FTP文件路径
     * @param mode 模块
     * @return FTP文件路径
     */
    private String getPath(String mode) {
        if (AnnexTypeEnum.RISK_MANAGE.getType().equals(mode)) {
            return ftpSetting.getRiskPath();
        } else if (AnnexTypeEnum.MANAGE.getType().equals(mode)) {
            return ftpSetting.getManagePath();
        } else if (AnnexTypeEnum.MAINTENANCE_PLAN.getType().equals(mode)) {
            return ftpSetting.getMaintenancePlanPath();
        } else if (AnnexTypeEnum.ASSET_INFO.getType().equals(mode)) {
            return ftpSetting.getAssetInfoPath();
        } else {
            return ftpSetting.getPurchasePath();
        }
    }

    /**
     * 获取错误信息
     * @param msg 错误信息
     * @param mode 模块
     * @return 错误信息
     */
    private String getMsg(String msg, String mode) {
        String name = AnnexTypeEnum.getName(mode);
        return msg.replace(ERROR_MSG_REPLACE,name);
    }
}
