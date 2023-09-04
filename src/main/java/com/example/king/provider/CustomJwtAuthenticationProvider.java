package com.example.king.provider;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Log4j2
public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        log.info("#### I am in authenticate@CustomJwtAuthenticatonProvider : credentials => " + token );
        return null;
    }

    public Authentication getAuthentication(String strToken){
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                strToken, "", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("############ I am in supports@CustomAuthentication Provier");
        log.info("############ supports@CustomAuthenticationProvier : " + authentication.toString());
        //return false; 원래
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication); // 로그인 시도시 정보가 authentication 으로 주입됨
    }
}
