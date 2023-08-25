package com.example.king.config;

import com.example.king.handler.CustomAuthenticationFailureHandler;
import com.example.king.handler.CustomFormLoginSuccessHandler;
import com.example.king.service.MemberUserDetail;
import com.example.king.service.MemberUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    MemberUserDetailsService memberUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests( (request) -> {
            request.requestMatchers("/").permitAll()
                    .requestMatchers("/result/create").permitAll()
                    .requestMatchers("/member/create").permitAll()
                    .requestMatchers("/auth/login/error").permitAll()
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("/member/exist/id/**").permitAll()
                    .requestMatchers("/member/exist/nickname/**").permitAll()
                    .anyRequest().authenticated();
        })
        .userDetailsService(memberUserDetailsService)
        .formLogin( (form) -> {
            form.loginPage("/auth/login") //PostMappeing("/auth/login")으로 login 설정
                .usernameParameter("id") // loadByUserName 의 parameter를 지정 default=username
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler());
        })
        .logout((logout) -> {
            logout.logoutUrl("/auth/logout")  //PostMappeing("/auth/logout")으로 logout 설정
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
        });


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers ("/image/**", "/js/**", "/css/**");
        // configure Web security...
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomFormLoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
}
