package com.example.king.serviceImpl;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import com.example.king.constant.Role;
import com.example.king.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    @Override
    public List<MemberListDTO> getMemberList() {
        List<MemberListDTO> memberDTOList = new ArrayList<>();

        List<MemberEntity> memberEntityList = memberRepository.findAll();

        if(memberEntityList.isEmpty()){
            log.info("getMemberList@MemberServiceImpl return null List");
            return memberDTOList;
        }

        for(MemberEntity memberEntity : memberEntityList){
            MemberListDTO memberListDTO = new MemberListDTO();
            memberListDTO.setId(memberEntity.getId());
            memberListDTO.setNickname(memberEntity.getNickname());
            memberListDTO.setRole(memberEntity.getRole().name());
            memberListDTO.setJoindate(memberEntity.getJoindate());
            memberDTOList.add(memberListDTO);
        }

        return memberDTOList;
    }

    @Override
    public int saveMember(MemberCreateDTO memberCreateDTO, PasswordEncoder passwordEncoder) {
        log.info("setMember@MemberServiceImpl : memberCreateDTO" + memberCreateDTO.toString());
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId(memberCreateDTO.getId());
        memberEntity.setNickname(memberCreateDTO.getNickname());
        //** 나중 password Hash 추가 *//
        memberEntity.setPassword(passwordEncoder.encode( memberCreateDTO.getPassword() ));
        memberEntity.setRole(Role.USER);

        MemberEntity resultEntity = null;
        try{
            resultEntity = memberRepository.save(memberEntity);
        } catch(Exception e){
            log.error("saveMember@MemberServiceImpl : fail to save");
            return -1;  //fail
        }

        return 0; // success
    }

    @Override
    public MemberAuthDTO getAuthDTO(String id) {
        MemberAuthDTO memberAuthDTO = new MemberAuthDTO();

        Optional<MemberEntity> member = memberRepository.findById(id);
        if( member.isPresent() ){
            memberAuthDTO.setId(member.get().getId());
            memberAuthDTO.setPassword(member.get().getPassword());
            return memberAuthDTO;
        } else {
            log.info("getAtuhDTO@MemberServiceImpl : no result");
            return null;
        }
    }

    @Override
    public Boolean checkIdExist(String id) {
        return memberRepository.existsById(id);
    }

    @Override
    public Boolean checkNicknameExist(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
