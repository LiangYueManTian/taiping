package com.taiping.utils;

import com.taiping.enums.EmailTemplateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author liyj
 * @date 2019/9/8
 */
@Component
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送简单邮件
     *
     * @param sendTo  收件人
     * @param title   邮件标题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String sendTo, String title, String content) {
        SimpleMailMessage mimeMessage = init(sendTo, title);
        mimeMessage.setText(content);
        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            System.out.println("发送失败>>>>>>>>异常：" + e.getMessage());
        }
    }

    private SimpleMailMessage init(String sendTo, String title) {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();
        mimeMessage.setFrom(sender);
        mimeMessage.setTo(sendTo);
        mimeMessage.setSubject(title);

        return mimeMessage;
    }

    /**
     * 发送模板邮件
     *
     * @param sendTo       收件人
     * @param title        邮件标题
     * @param content      参数 Map
     * @param templateEnum 模板
     */
    public void sendTemplateMail(String sendTo, String title, Context content, EmailTemplateEnum templateEnum) {
        //根据templateType 获取模板
        String process = templateEngine.process(templateEnum.getTemplateType(), content);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(sendTo);
            helper.setFrom(sender);
            helper.setSubject(title);
            helper.setText(process, true);
            mailSender.send(message);
            System.out.println("发送" + templateEnum.getTemplateName() + "邮件成功!");
        } catch (MessagingException e) {
            System.out.println("发送" + templateEnum.getTemplateName() + "邮件失败>>>>>" + e.getMessage());
        }

    }
}
