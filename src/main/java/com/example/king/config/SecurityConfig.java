package com.example.king.config;

import com.example.king.filter.JwtTokenCheckFilter;
import com.example.king.handler.CustomAuthenticationFailureHandler;
import com.example.king.handler.CustomFormLoginSuccessHandler;
import com.example.king.provider.CustomJwtAuthenticationProvider;
import com.example.king.service.MemberUserDetail;
import com.example.king.service.MemberUserDetailsService;
import com.example.king.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                    //.requestMatchers("/mail/test", "/mail/testHtml").permitAll()
                    .requestMatchers("/member/forgotPassword", "/member/resetPassword").permitAll()
                    .requestMatchers("/member/create").permitAll()
                    //.requestMatchers("/auth/login/error").permitAll()
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
        http.csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher("/quiz/token/getquiz", "/member/resetPassword/process");
        //http.securityMatcher(AntPathRequestMatcher.antMatcher("/quiz/token/getquiz"));
        //http.securityMatcher(AntPathRequestMatcher.antMatcher("/member/resetPassword/process"));
        http.authorizeHttpRequests((request) -> {
                    request.anyRequest().authenticated();
//                    request.requestMatchers("/quiz/token/getquiz", "/member/resetPassword/process").authenticated();
                });
        http.addFilterBefore(new JwtTokenCheckFilter(customJwtAuthenticationProvider, tokenService), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement( (session) -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)   ;
        });
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource((corsConfigurationSource()));
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3002"));
        configuration.setAllowedMethods(Arrays.asList("GET"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/quiz/token/getquiz", configuration);
        return source;
    }
}
