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

    private TokenService tokenService;
    private CustomJwtAuthenticationProvider authProvicer;
    public JwtTokenCheckFilter(CustomJwtAuthenticationProvider customJwtAuthenticationProvider, TokenService tokenService){
        this.authProvicer = customJwtAuthenticationProvider;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("###########doFilterInternal@TokenCheckFilter");
        String strAuthHeader = (String) request.getHeader("Authorization");

        if(strAuthHeader==null || strAuthHeader.length()<8) log.info("##### doFilterInternal@BaseController : invalid token");

        //if(!strToken.equals("Bearer")) throw new RuntimeException("token kind not acceptable");
        try{
            String msg;
            String strTokenName = strAuthHeader.substring(0,6);
            String strToken = strAuthHeader.substring(7);
            log.info("##### doFilterInternal@JWTTokenFilter : strTokenName, strToken => " + strTokenName + ", " + strToken);

            msg =  tokenService.getIdFromToken(strToken);
            log.info("##### doFilterInternal@JWTTokenFilter : id =>" + msg);

            Authentication authentication= authProvicer.getAuthentication(msg);
            if(authentication!=null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch( Exception e){
            log.error("##### doFilterInternal@JWTTokenFilter  {}", e.getMessage());
            filterChain.doFilter(request, response);
        }

    }
}
