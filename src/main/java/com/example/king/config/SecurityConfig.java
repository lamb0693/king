package com.example.king.config;

import com.example.king.filter.JwtTokenCheckFilter;
import com.example.king.handler.CustomAuthenticationFailureHandler;
import com.example.king.handler.CustomFormLoginSuccessHandler;
import com.example.king.provider.CustomJwtAuthenticationProvider;
import com.example.king.service.MemberUserDetail;
import com.example.king.service.MemberUserDetailsService;
import com.example.king.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Log4j2
public class SecurityConfig {

    MemberUserDetailsService memberUserDetailsService;
    CustomJwtAuthenticationProvider customJwtAuthenticationProvider;
    TokenService tokenService;
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests( (request) -> {
            request.requestMatchers("/").permitAll()
                    .requestMatchers("/image/**", "/js/**", "/css/**").permitAll()
                    //.requestMatchers("/result/create").permitAll()
                    .requestMatchers("/member/create").permitAll()
                    .requestMatchers("/auth/login/error").permitAll()
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("/member/exist/id/**").permitAll()
                    .requestMatchers("/member/exist/nickname/**").permitAll()
                    .requestMatchers("/member/list", "/member/getJwtToken", "/result/list").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/logout").authenticated()
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
    @Order(1)
    public SecurityFilterChain tokenFilterChain(HttpSecurity http) throws Exception {
        log.info("####tokenFilterChain");
        http.securityMatcher(AntPathRequestMatcher.antMatcher("/quiz/token/getquiz"));
        http.authorizeHttpRequests((request) -> {
                    request.requestMatchers("/quiz/token/getquiz").authenticated();
                })
                .addFilterBefore(new JwtTokenCheckFilter(customJwtAuthenticationProvider, tokenService), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement( (session) -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)   ;
        });
        return http.build();
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
