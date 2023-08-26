package com.example.king.serviceImpl;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import com.example.king.constant.Role;
import com.example.king.service.MemberService;
import com.example.king.service.MemberUserDetail;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            memberAuthDTO.setNickname(member.get().getNickname());
            memberAuthDTO.setLocked(member.get().isLocked());
            log.info(" **** getAuthDTO:MemberServiceImpl : memberAuthDTO is set" + memberAuthDTO.toString());
            return memberAuthDTO;
        } else {
            log.info("getAtuhDTO@MemberServiceImpl : no result");
            return null;
        }
    }

    @Override
    public void deleteById(String id) throws IllegalArgumentException{
        try{
            memberRepository.deleteById(id);
        } catch(Exception e){
            log.error(e.getMessage()); 
            // 이 후에  어떻게 할지 추가해야
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

    @Override
    @Transactional
    public void saveNewPassword(String id,  String password) throws IllegalArgumentException, OptimisticEntityLockException {
        // joinat이 update 되지 않아야 한다  나중 확인
        log.info("  ****** saveNewPassword@MemberServiceImpl id=" + id);
        MemberCreateDTO dto = new MemberCreateDTO();
        MemberEntity memberEntity = new MemberEntity();
        Optional<MemberEntity> optional = memberRepository.findById(id);
        if(optional.isPresent()){
            memberEntity=optional.get();
            memberEntity.setPassword(password);
            memberRepository.save(memberEntity);
            log.info(" *****   saveNewPassword@MemberServiceImpl saved new Password");
        } else {
            log.error("error saveNewPassword@MemberServiceImpl : no result ");
        }

    }
}
