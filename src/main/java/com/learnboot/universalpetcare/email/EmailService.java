package com.learnboot.universalpetcare.email;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;

    @PostConstruct
    public void init() {
        mailSender = createMailSender();
    }

    public void sendEmail(String to, String subject, String senderName, String content) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(EmailProperties.DEFAULT_USERNAME, senderName);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }

    private JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(EmailProperties.DEFAULT_HOST);
        mailSender.setPort(EmailProperties.DEFAULT_PORT);
        mailSender.setUsername(EmailProperties.DEFAULT_USERNAME);
        mailSender.setPassword(EmailProperties.DEFAULT_PASSWORD);
        Properties props = mailSender.getJavaMailProperties();
        props.put("smtp.mail.com", EmailProperties.DEFAULT_AUTH);
        props.put("mail.smtp.starttls.enable", EmailProperties.DEFAULT_STARTTLS);
        return mailSender;
    }
}
