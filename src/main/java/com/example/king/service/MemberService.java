package com.example.king.service;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MemberService {

    public List<MemberListDTO> getMemberList();

    public int saveMember(MemberCreateDTO memberCreateDTO, PasswordEncoder passwordEncoder);

    public MemberAuthDTO getAuthDTO(String id);
}
