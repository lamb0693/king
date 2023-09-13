package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MemberCreateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@Log4j2
public class OAuth2UserDetailService implements UserDetailsService {
    MemberService memberService;
    MyPasswordEncoder myPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberAuthDTO memberAuthDTO = null;

        // 있으면 원래 정보로
        // 없으면 새로운 멤버로 저장
        if(memberService.checkIdExist(username)) {
            memberAuthDTO = memberService.getAuthDTO(username);
            log.info("####loadUserByUsername@OAuth2UserDetailService from DB : memberAuthDTO : {}", memberAuthDTO);
        } else {
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            String strNick = "i" + System.currentTimeMillis();
            String strPassword = String.format("%08d", (long) Math.floor( Math.random()*100000000));
            memberAuthDTO = new MemberAuthDTO(username, strPassword, strNick, true, authorities);
            log.info("####loadUserByUsername@OAuth2UserDetailService new memberAuthDTO : {}", memberAuthDTO);
            // 저장 - 회원으로
            MemberCreateDTO memberCreateDTO = new MemberCreateDTO();
            memberCreateDTO.setId(memberAuthDTO.getId());
            memberCreateDTO.setPassword(memberAuthDTO.getPassword());
            memberCreateDTO.setNickname(memberAuthDTO.getNickname());
            log.info("####loadUserByUsername@OAuth2UserDetailService new memberCreateDTO : {}", memberCreateDTO);
            memberService.saveMember(memberCreateDTO, myPasswordEncoder.passwordEncoder());
        }

        MemberUserDetail memberUserDetail = new MemberUserDetail(memberAuthDTO);

        return memberUserDetail;
    }
}
