package com.commons.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 本类测试简单邮件 直接用邮件发送<br>
 * 建议不要用QQ作为发送者, 需要跑到QQ邮箱中设置开启相关服务.<br>
 * 带附件发送, 详见博客: <a href="http://blog.csdn.net/is_zhoufeng/article/details/12004073">  点击跳转... <a/>
 *
 * @author Administrator
 */
public class SpringSendMail {


    public static void sendMail(String... to) {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost("smtp.163.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo(to);
        mailMessage.setFrom("yqw882007@163.com");
        mailMessage.setSubject("测试简单文本邮件发送！");
        mailMessage.setText("测试我的简单邮件发送机制！！");

        senderImpl.setUsername("yqw882007@163.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("xx"); // 根据自己的情况, 设置password

        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println("邮件发送成功..");
    }

    public static void main(String[] args) {
        sendMail("314196705@qq.com", "314370204@qq.com");
    }
}