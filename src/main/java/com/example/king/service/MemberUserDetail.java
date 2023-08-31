package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MemberUserDetail implements UserDetails {
    String id;
    String password;

    @Getter
    String nickname;

    boolean locked;

    Collection<? extends GrantedAuthority> authorities;

    public MemberUserDetail(MemberAuthDTO memberAuthDTO) {
        this.id = memberAuthDTO.getId();
        this.password = memberAuthDTO.getPassword();
        this.nickname = memberAuthDTO.getNickname();
        this.locked= memberAuthDTO.isLocked();
        this.authorities = memberAuthDTO.getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "MemberUserDetail{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
