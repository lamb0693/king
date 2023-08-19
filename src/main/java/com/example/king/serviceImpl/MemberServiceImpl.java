package com.example.king.serviceImpl;

import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import com.example.king.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            memberListDTO.setJoindate(memberEntity.getJoindate());
            memberDTOList.add(memberListDTO);
        }

        return memberDTOList;
    }

    @Override
    public int saveMember(MemberCreateDTO memberCreateDTO) {
        log.info("setMember@MemberServiceImpl : memberCreateDTO" + memberCreateDTO.toString());
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId(memberCreateDTO.getId());
        memberEntity.setNickname(memberCreateDTO.getNickname());
        //** 나중 password Hash 추가 *//
        memberEntity.setPassword(memberCreateDTO.getPassword());

        MemberEntity resultEntity = null;
        try{
            resultEntity = memberRepository.save(memberEntity);
        } catch(Exception e){
            log.error("saveMember@MemberServiceImpl : fail to save");
            return -1;  //fail
        }

        return 0; // success
    }
}
