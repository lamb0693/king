package com.example.king.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class MemberAuthDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;
}
