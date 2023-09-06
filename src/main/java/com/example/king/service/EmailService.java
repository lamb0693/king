package com.example.king.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class EmailService {
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    TokenService tokenService;

    public String sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try{
            mailSender.send(message);
            return "success";
        } catch(Exception e){
            log.info(e.getMessage());
            return e.getMessage();
        }
    }

    public String sendHTMLEmail(String toId, String subject, String templateName){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Map<String, Object> contextMap = new HashMap<>();
        String href="http://localhost:8080/member/resetPassword?id=" + toId + "&token=";
        href += tokenService.createToken(5, toId);
        contextMap.put("email_id", toId);
        contextMap.put("email_href", href);
        Context context = new Context(Locale.KOREA, contextMap);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_NO, StandardCharsets.UTF_8.name() );

            mimeMessageHelper.setTo(toId);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("admin@kiwnwangzzang.test"); // Set the sender's email address

            String htmlContent = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlContent, true); // Set the HTML content as true

            mailSender.send(mimeMessage);
            return "success";
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }

    }
}
