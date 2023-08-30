package com.example.king.controller;

import com.example.king.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/auth")
public class LoginController {
    MemberService memberService;

    @GetMapping("/login")
    public String login(HttpServletRequest request){
        log.info("****** login@LoginController");

        // 여기로 보낸 page를 session에 저장한다
        String fromUri = request.getHeader("Referer");
        log.info(" ****** login@LoginController  Referer = " + fromUri);
        request.getSession().setAttribute("pageFrom",fromUri );

        return "/login/loginForm" ;
    }

    // spring security에서 logout은 post로 옴
    @GetMapping("/logout")
    public String logoutForm(){
        return "/login/logoutForm";
    }

    @GetMapping("/login/error")
    public String loginError(@RequestParam String error, @RequestParam String exception, Model model){
        log.info("****** loginError@LoginController");

        model.addAttribute("error", error);
        model.addAttribute("message", exception);

        return "/login/loginForm" ;
    }
    
    // PostMapping(/login)은 spring security 에서 담당
}
