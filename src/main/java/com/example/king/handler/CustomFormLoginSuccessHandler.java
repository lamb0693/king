package com.example.king.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Log4j2
public class CustomFormLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("***** I am in onAuthenticationSuccess@CustomFormLoginSuccessHandler");
//        String pageFrom = (String) request.getSession().getAttribute("pageFrom");
//        log.info("***** onAuthenticationSuccess@CustomFormLoginSuccessHandler " + pageFrom);
//        if(pageFrom != null) request.getSession().removeAttribute("pageFrom"); // 세션에서 없애줌

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(targetUrl);
        } else {
            response.sendRedirect("/");
        }

//        if(pageFrom == null || pageFrom.isEmpty()) response.sendRedirect("/");
//        else response.sendRedirect(pageFrom);
    }
}
