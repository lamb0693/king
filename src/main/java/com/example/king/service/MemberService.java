package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface MemberService {

    public List<MemberListDTO> getMemberList();

    public int saveMember(MemberCreateDTO memberCreateDTO, PasswordEncoder passwordEncoder);

    public MemberAuthDTO getAuthDTO(String id);

    public void deleteById(String id);

    Boolean checkIdExist(String id);

    Boolean checkNicknameExist(String nickname);

    void saveNewPassword(String id, String password);
}
