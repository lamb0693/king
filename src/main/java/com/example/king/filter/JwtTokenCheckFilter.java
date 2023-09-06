package com.example.king.filter;

import com.example.king.provider.CustomJwtAuthenticationProvider;
import com.example.king.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class JwtTokenCheckFilter extends OncePerRequestFilter {

    TokenService tokenService;
    CustomJwtAuthenticationProvider authProvicer;
    public JwtTokenCheckFilter(CustomJwtAuthenticationProvider customJwtAuthenticationProvider, TokenService tokenService){
        this.authProvicer = customJwtAuthenticationProvider;
        this.tokenService = tokenService;
    }

    // Exception 이 catch 되지 않으면 다음 filter로 진행되는 듯
    // Excetptin 이 catch 되면 Authetnticatin 성공 => endpoint로 진행
    //      Auth 실패 상태면 response_ok만 보내는 결과
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("###########doFilterInternal@TokenCheckFilter");
        String strAuthHeader = (String) request.getHeader("Authorization");
        log.info("##### doFilterInternal@BaseController : strAuthHeader " + strAuthHeader);
        if(strAuthHeader==null || strAuthHeader.length()<8){
            log.info("##### doFilterInternal@BaseController : invalid token");
            //Exception 처리 해 주어야
        }
        String strTokenName = null;
        String strToken = null;
        try{
            strTokenName = strAuthHeader.substring(0,6);
            strToken = strAuthHeader.substring(7);
        } catch( Exception e){
            log.info(e.getMessage());
        }
        log.info("##### doFilterInternal@JWTTokenFilter : strTokenName, strToken => " + strTokenName + ", " + strToken);
        //if(!strToken.equals("Bearer")) throw new RuntimeException("token kind not acceptable");

        String msg;
        Authentication authentication = null;
        try{
            msg =  tokenService.getIdFromToken(strToken);
            log.info("##### doFilterInternal@JWTTokenFilter : id =>" + msg);

            authentication= authProvicer.getAuthentication(msg);

        } catch (Exception e){
            log.info("##### doFilterInternal@JWTTokenFilter: invalid token excetpion : " + e.getMessage());
            filterChain.doFilter(request, response);
        }

        if(authentication!=null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }

    }

}
