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
import java.net.URLEncoder;

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

            String socialMessage = URLEncoder.encode("소셜로그인으로 로그인 되었습니다. \n개인정보수정에서 탈퇴,닉네임,패스워드 변경이 가능합니다, \n비밀번호 수정후 아이디 비밀번호로도 이용 가능합니다", "UTF-8");
            response.sendRedirect("/?social=" + socialMessage);
        }

    }
}
