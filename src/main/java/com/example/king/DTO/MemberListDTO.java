package com.example.king.DTO;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Valid      /* Validation 추가 해야 됨*/
public class MemberListDTO {
    private String id;
    private String nickname;
    private String role;
    private LocalDateTime joindate;
}
