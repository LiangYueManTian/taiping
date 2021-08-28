package com.taiping;

import com.taiping.utils.FtpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author liyj
 * @date 2019/10/15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FtpUtilsTest {


    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.userName}")
    private String userName;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.homePath}")
    private String homePath;

    /**
     * 上传
     */
    @Test
    public void uploadFile() {
        File file = new File("D:\\DownLoadRecord.ini");
        homePath = homePath + "new File";
        boolean b = FtpUtils.uploadFile(host, userName, password, port, homePath, file);
        System.out.println(b);
    }

    /**
     * 下载
     */
    @Test
    public void downFile() {
        String localPath = "E://";
        String fileName = "vsftpd.conf";
        boolean b = FtpUtils.downloadFile(host, userName, password, port, homePath, localPath, fileName);
        System.out.println(b);
    }

}
