package com.example.king.handler;

import com.example.king.service.MemberUserDetail;
import com.example.king.service.OAuth2UserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    OAuth2UserDetailService oAuth2UserDetailService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken){
            String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email");
            MemberUserDetail memberUserDetail = (MemberUserDetail) oAuth2UserDetailService.loadUserByUsername(email);

            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(memberUserDetail, memberUserDetail.getPassword(), memberUserDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }

        response.sendRedirect("/");
    }
}
