package com.taiping.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ftp配置
 *
 * @author chaofang@wistronits.com
 * @since 2019-10-29
 */
@Component
@Data
public class FtpSetting {
    /**
     * 文件下载路径（待替换）
     */
    private static final String STORE_PATH = "ftp://${host}:${port}${path}${fileName}";
    /**
     * 地址替换符
     */
    private static final String HOST_REPLACE = "${host}";
    /**
     * 端口替换符
     */
    private static final String PORT_REPLACE = "${port}";
    /**
     * 文件目录替换符
     */
    private static final String PATH_REPLACE = "${path}";
    /**
     * 文件名称替换符
     */
    private static final String FILE_NAME_REPLACE = "${fileName}";

    /**
     * 地址
     */
    @Value("${ftp.host}")
    private String host;
    /**
     * 用户名
     */
    @Value("${ftp.userName}")
    private String userName;
    /**
     * 密码
     */
    @Value("${ftp.password}")
    private String password;
    /**
     * 端口
     */
    @Value("${ftp.port}")
    private int port;
    /**
     * 文件目录
     */
    @Value("${ftp.homePath}")
    private String homePath;
    /**
     * 文件目录
     */
    @Value("${ftp.managePath}")
    private String managePath;
    /**
     * 文件目录
     */
    @Value("${ftp.riskPath}")
    private String riskPath;
    /**
     * 文件目录
     */
    @Value("${ftp.maintenancePlanPath}")
    private String maintenancePlanPath;
    /**
     * 文件目录
     */
    @Value("${ftp.assetInfoPath}")
    private String assetInfoPath;
    /**
     * 文件目录
     */
    @Value("${ftp.purchasePath}")
    private String purchasePath;

    /**
     * 获取下载路由
     * @param fileName 文件名称
     * @return 下载路由
     */
    public String getStorePath(String path, String fileName) {
        String storePath = STORE_PATH.replace(HOST_REPLACE, host).replace(PORT_REPLACE, String.valueOf(port));
        return storePath.replace(PATH_REPLACE, path).replace(FILE_NAME_REPLACE, fileName);
    }
}
