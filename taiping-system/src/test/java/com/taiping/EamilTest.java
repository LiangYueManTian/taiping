package com.taiping;

import com.taiping.enums.EmailTemplateEnum;
import com.taiping.utils.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 测试邮件发送
 *
 * @author liyj
 * @date 2019/9/9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EamilTest {

    @Autowired
    private EmailService emailUtil;

    @Test
    public void sendSimpleEmailTest() {
        //测试发送简单邮件
        String sendTo = "heimouyy@126.com";
        String title = "测试邮件!";
        String content = "这是一封测试邮件!";
        emailUtil.sendSimpleMail(sendTo, title, content);
    }

    @Test
    public void sendHtmlTemplateTest() {
        String sendTo = "heimouyy@126.com";
        String title = "测试邮件!";
        Context content = new Context();
        content.setVariable("num", 1);
        content.setVariable("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")));
        emailUtil.sendTemplateMail(sendTo, title, content, EmailTemplateEnum.SAMPLE_ONE);

    }

}
