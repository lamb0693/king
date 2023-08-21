package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberAuthDTO memberAuthDTO = memberService.getAuthDTO(username);

        if(memberAuthDTO == null) throw new UsernameNotFoundException("No User with such id");

        return new MemberUserDetail(memberAuthDTO);
    }
}
