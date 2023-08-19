package com.example.king.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Valid
public class MemberCreateDTO {
    /* Validation 추가 해야 됨*/
    @NotEmpty
    private String id;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String password;

    @Override
    public String toString() {
        return "MemberCreateDTO{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
