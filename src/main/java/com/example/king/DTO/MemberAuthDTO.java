package com.example.king.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Valid
public class MemberAuthDTO extends User {
    // 추가하면 UserDetail 과 getAuthoDTO@MemberServicdImp 손봐야
    
    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private boolean locked;


    public MemberAuthDTO(String username, String password, String nickname, boolean locked,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password,authorities);
        this.id= username;
        this.nickname = nickname;
        this.password = password;
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "MemberAuthDTO{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", locked=" + locked +
                '}';
    }
}
