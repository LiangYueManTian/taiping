package com.taiping.bean.riskmanage;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangliangyu
 * @since 2019/10/29
 * 附件上传实体
 */
public class UploadAnnexVo {
    /**
     * 风险项id
     */
    private String riskItemId;
    /**
     * 附件
     */
    private MultipartFile file;

    public String getRiskItemId() {
        return riskItemId;
    }

    public void setRiskItemId(String riskItemId) {
        this.riskItemId = riskItemId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
