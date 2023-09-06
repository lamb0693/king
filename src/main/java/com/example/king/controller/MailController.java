package com.example.king.controller;

import com.example.king.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mail")
@AllArgsConstructor
@ResponseBody
public class MailController {
    EmailService emailService;
//    @GetMapping("/test")
//    public String sendTestMail(){
//        String retMessage = emailService.sendEmail("aaa@aaa.aaa", "test email", "test Email body");
//        if( retMessage.equals("success")) return "메일 전송이 성공하였습니다";
//        else return "메일 전송이 실패하였습니다. 관리자에게 문의하세요";
//    }
//
//    @GetMapping("/testHtml")
//    public String sendTestHtmlMail(){
//        String retMessage = emailService.sendHTMLEmail("aaa@aaa.aaa", "test email", "/email/resetPasswordTemplate");
//        if( retMessage.equals("success")) return "메일 전송이 성공하였습니다";
//        else return "메일 전송이 실패하였습니다. 관리자에게 문의하세요";
//    }

    @PostMapping("/resetPasswordEmail")
    public String sendResetPasswordMail(@RequestParam String toId){
        String retMessage = emailService.sendHTMLEmail(toId, "비밀 번호 수정 링크입니다", "/email/resetPasswordTemplate");
        if( retMessage.equals("success")) return "메일 전송이 성공하였습니다";
        else return "메일 전송이 실패하였습니다. 관리자에게 문의하세요";
    }
}
