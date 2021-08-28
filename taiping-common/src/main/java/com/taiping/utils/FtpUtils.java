package com.taiping.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.SocketException;

/**
 * ftpUtils 工具类
 *
 * @author liyj
 * @date 2019/10/14
 */
@Slf4j
@Component
public class FtpUtils {
    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP主机服务器
     * @param ftpPassword FTP 登录密码
     * @param ftpUserName FTP登录用户名
     * @param ftpPort     FTP端口 默认为21
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                         String ftpPassword, int ftpPort) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            // 登陆FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                log.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            log.error("FTP的IP地址可能错误，请正确配置。", e);
        } catch (IOException e) {
            log.error("FTP的端口错误,请正确配置。", e);
        }
        return ftpClient;
    }


    /**
     * 下载文件
     *
     * @param ftpHost     ftp服务器地址
     * @param ftpUserName anonymous匿名用户登录，不需要密码。administrator指定用户登录
     * @param ftpPassword 指定用户密码
     * @param ftpPort     ftp服务员器端口号
     * @param ftpPath     ftp文件存放物理路径
     * @param fileName    文件路径
     * @return 是否下载ok
     */
    public static boolean downloadFile(String ftpHost, String ftpUserName,
                                       String ftpPassword, int ftpPort, String ftpPath, String localPath,
                                       String fileName) {
        FTPClient ftpClient = null;
        boolean downFlag = false;
        try (OutputStream os = new FileOutputStream(new File(localPath + File.separatorChar + fileName))) {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            // 编码设置为中文
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);
            downFlag = ftpClient.retrieveFile(fileName, os);
            ftpClient.logout();
            log.info("文件下载成功!");
        } catch (FileNotFoundException e) {
            log.error("没有找到" + ftpPath + "文件", e);
        } catch (SocketException e) {
            log.error("连接FTP失败.", e);
        } catch (IOException e) {
            log.error("文件读取错误。", e);
        }
        return downFlag;
    }

    /**
     * 上传文件
     *
     * @param ftpHost     ftp服务器地址
     * @param ftpUserName anonymous匿名用户登录，不需要密码。administrator指定用户登录
     * @param ftpPassword 指定用户密码
     * @param ftpPort     ftp服务员器端口号
     * @param ftpPath     ftp文件存放物理路径
     * @param file        文件输入流，即从本地服务器读取文件的IO输入流
     * @return 是否上传ok
     */
    public static boolean uploadFile(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String ftpPath,
                                     File file) {

        boolean success = false;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            String name = file.getName();
            FTPClient ftp = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            String fileName = setFTPClient(ftpPath, name, ftp);
            success = ftp.storeFile(fileName, bis);
            if (success) {
                log.info("成功上传文件(" + file.getName() + ")至FTP服务器(" + ftpHost + ")");
            } else {
                log.info("上传文件(" + file.getName() + ")至FTP服务器(" + ftpHost + ")失败");
            }
            ftp.logout();
        } catch (FileNotFoundException e) {
            log.error("未找到文件，文件名：" + file.getName(), e);
        } catch (IOException e) {
            log.error("上传文件失败，文件名：" + file.getName(), e);
        }
        return success;
    }


    /**
     * 上传文件
     *
     * @param ftpHost     ftp服务器地址
     * @param ftpUserName anonymous匿名用户登录，不需要密码。administrator指定用户登录
     * @param ftpPassword 指定用户密码
     * @param ftpPort     ftp服务员器端口号
     * @param ftpPath     ftp文件存放物理路径
     * @param file        前台文件流
     * @return 是否上传ok
     */
    public static boolean uploadFile(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String ftpPath,
                                     MultipartFile file) {

        boolean success = false;
        try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream())) {
            FTPClient ftp = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            String name = file.getOriginalFilename();
            String fileName = setFTPClient(ftpPath, name, ftp);
            success = ftp.storeFile(fileName, bis);
            if (success) {
                log.info("成功上传文件(" + file.getOriginalFilename() + ")至FTP服务器(" + ftpHost + ")");
            } else {
                log.info("上传文件(" + file.getOriginalFilename() + ")至FTP服务器(" + ftpHost + ")失败");
            }
            ftp.logout();
        } catch (FileNotFoundException e) {
            log.error("未找到文件，文件名：" + file.getOriginalFilename(), e);
        } catch (IOException e) {
            log.error("上传文件失败，文件名：" + file.getOriginalFilename(), e);
        }
        return success;
    }


    public static boolean deleteFile(String ftpHost, String ftpUserName, String ftpPassword,
                                     int ftpPort, String ftpPath, String fileName) {
        boolean deleteFile =  false;
        try {
            FTPClient ftp = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            // 编码设置为中文
            ftp.setControlEncoding("UTF-8");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(ftpPath);
            deleteFile = ftp.deleteFile(fileName);
            if (deleteFile) {
                log.info("从FTP服务器({})删除文件({})成功", fileName , ftpHost);
            } else {
                log.warn("从FTP服务器({})删除文件({})失败", fileName , ftpHost);
            }
            ftp.logout();
        } catch (IOException e) {
            log.error("从FTP服务器(" + ftpHost + ")删除文件发生错误，文件名：" + fileName, e);
        }
        return deleteFile;
    }

    /**
     * 设置FTPClient
     *
     * @param ftpPath ftp文件存放物理路径
     * @param name 文件名称
     * @param ftp FTPClient
     * @return String 文件名称
     * @throws IOException
     */
    private static String setFTPClient(String ftpPath, String name, FTPClient ftp) throws IOException {
        String localCharset = "UTF-8";
        String serverCharset = "iso-8859-1";
        ftp.setControlEncoding(localCharset);
        // 设置为主动模式 客户端自己发送一个端口 让服务端和这个端口连接 然后上传文件
        ftp.enterLocalPassiveMode();
        boolean changeResult = ftp.changeWorkingDirectory(ftpPath);
        if (!changeResult) {
            String[] childDirs = ftpPath.split("/");
            StringBuffer endDir = new StringBuffer();
            for (int i = 0; i < childDirs.length; i++) {
                endDir.append("/").append(childDirs[i]);
                changeResult = ftp.changeWorkingDirectory(endDir.toString());
                if (!changeResult) {
                    ftp.makeDirectory(endDir.toString());
                }
            }
            ftp.changeWorkingDirectory(ftpPath);
        }
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        return new String(name.getBytes(localCharset), serverCharset);
    }

    /**
     * 测试是否可以下载和上传
     *
     * @param args
     */
//    public static void main(String[] args) {
//        /***
//         * 这里用windows7 的时候需要注意的是 关闭防火墙 不然会抱 perssion is rev
//         * win10 待测试是否需要关闭防火墙
//         */
//
////        FTPClient ftpClient = getFTPClient("10.5.24.171", "anonymous", "", 21);
//        // 上传文件
//        String ftpHost = "10.5.24.171";
//        String ftpUserName = "anonymous";
//        String ftpPassword = "";
//        int ftpPort = 21;
//        String ftpPath = "/pub/";
//        String fileName = "vsftpd.conf";
//        File file = new File("D:/vsftpd.con");
////        uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, file);
//
//        //下载文件
//        String downFilePath = "E://";
////        downloadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, downFilePath, fileName);
//    }

}
