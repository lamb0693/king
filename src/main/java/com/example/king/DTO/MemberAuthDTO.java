package com.example.king.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class MemberAuthDTO {
    // 추가하면 UserDetail 과 getAuthoDTO@MemberServicdImp 손봐야
    
    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private boolean locked;

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
