package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
@Log4j2
public class MemberUserDetailsService implements UserDetailsService {

    private MemberService memberService;
    
    //loadUserByUsername(String username의 변수를 바꾸면 formLogin()에서도 바꾸어 주어야 함
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if( id == null) log.error("*** loadUserByUsername@MemberUserDetailsService :  username null" );
        else if( id.isEmpty()) log.error("*** loadUserByUsername@MemberUserDetailsService :  username Empty" );

        log.info("*** loadUserByUsername@MemberUserDetailsService :  username :" + id );

        MemberAuthDTO memberAuthDTO = memberService.getAuthDTO(id);

        if(memberAuthDTO == null) throw new UsernameNotFoundException("No User with such id");

        MemberUserDetail memberUserDetail = new MemberUserDetail(memberAuthDTO);
        log.info("*****" + memberUserDetail.toString());
        return memberUserDetail;
    }
}
