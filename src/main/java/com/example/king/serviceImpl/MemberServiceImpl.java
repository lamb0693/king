package com.example.king.serviceImpl;

import com.example.king.DTO.*;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import com.example.king.constant.GameKind;
import com.example.king.constant.GradeCons;
import com.example.king.constant.Role;
import com.example.king.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private MemberListDTO entityToDTO(MemberEntity memberEntity){
        MemberListDTO memberListDTO = new MemberListDTO();
        memberListDTO.setId(memberEntity.getId());
        memberListDTO.setNickname(memberEntity.getNickname());
        memberListDTO.setRole(memberEntity.getRole().name());
        memberListDTO.setJoindate(memberEntity.getJoindate());
        memberListDTO.setLocked(memberEntity.isLocked());
        return memberListDTO;
    }

    @Override
    public MemberListPageDTO getMemberListWithPage(Pageable pageable) {
        List<MemberListDTO> memberDTOList = new ArrayList<>();

        Page<MemberEntity> memberEntityList = memberRepository.findAll(pageable);

        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add( entityToDTO(memberEntity) );
        }

        MemberListPageDTO memberListPageDTO = new MemberListPageDTO();
        memberListPageDTO.setMemberListDTOList(memberDTOList);
        memberListPageDTO.setCurrentPage(memberEntityList.getNumber());
        memberListPageDTO.setPageSize(memberEntityList.getTotalPages());
        memberListPageDTO.setTotalElements(memberEntityList.getTotalElements());

        return memberListPageDTO;
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

    // 수정
    @Override
    public MemberAuthDTO getAuthDTO(String id) {
        MemberAuthDTO memberAuthDTO = null;

        Optional<MemberEntity> member = memberRepository.findById(id);
        if( member.isPresent() ){
            memberAuthDTO = new MemberAuthDTO(
                    member.get().getId(),
                    member.get().getPassword(),
                    member.get().getNickname(),
                    member.get().isLocked(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.get().getRole()))
            );

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

    @Override
    public List<RankingDTO> getRanker(GameKind gameKind) {
        List<Object[]> resultList = memberRepository.findTop5Players(gameKind);
        List<RankingDTO> dtoList = new ArrayList<>();

        for( Object[] result :  resultList){
            RankingDTO dto = new RankingDTO();
            dto.setNickname( (String) result[0]);
            dto.setWinCount( (long) result[1]);
            if(dto.getWinCount() > 10) dto.setGrade(GradeCons.GRADE_KING);
            else if(dto.getWinCount() > 5 ) dto.setGrade(GradeCons.GRADE_WANG);
            else if(dto.getWinCount() > 2 ) dto.setGrade(GradeCons.GRADE_ZZANG);
            else dto.setGrade(GradeCons.GRACE_GEN);
            dtoList.add(dto);
        }

        log.info("**** getLanker@MemberController return dtoList " + dtoList.toString());

        return dtoList;
    }

    @Override
    public RankingListPageDTO getTotalRanker(GameKind gameKind, String orderBy, Pageable pageable) {
        RankingListPageDTO rankingListPageDTO = new RankingListPageDTO();

        Page<Object[]> resultListwithPage = memberRepository.findAllRanker(gameKind, pageable);

        List<RankingDTO> dtoList = new ArrayList<>();

        int i = resultListwithPage.getNumber()*10 + 1;
        for( Object[] result :  resultListwithPage){
            RankingDTO dto = new RankingDTO();
            dto.setNickname( (String) result[0]);
            dto.setWinCount( (long) result[1]);
            switch (gameKind.toString()){
                case "PING" : dto.setGameName("Pingpong"); break;
                case "LADDER" : dto.setGameName("낙하물 피하기"); break;
                case "QUIZ" : dto.setGameName("상식 퀴즈 대결");
            }
            if(dto.getWinCount()>10 ) dto.setGrade(GradeCons.GRADE_KING);
            else if(dto.getWinCount()>5) dto.setGrade(GradeCons.GRADE_WANG);
            else if(dto.getWinCount()>2) dto.setGrade(GradeCons.GRADE_ZZANG);
            else dto.setGrade(GradeCons.GRACE_GEN);
            dto.setRank(i);
            dtoList.add(dto);
            i++;
        }

        rankingListPageDTO.setRankingDTOList(dtoList);
        rankingListPageDTO.setCurrentPage(resultListwithPage.getNumber());
        rankingListPageDTO.setPageSize(resultListwithPage.getTotalPages());
        rankingListPageDTO.setTotalElements(resultListwithPage.getTotalElements());

        log.info("**** getTotalRanker@MemberController return dtoList " + dtoList.toString());

        return rankingListPageDTO;
    }

    @Override
    public void blockId(String id) throws IllegalArgumentException, OptimisticEntityLockException{
        Optional<MemberEntity> optional = memberRepository.findById(id);

        MemberEntity memberEntity = optional.orElseThrow();
        memberEntity.setLocked(true);

        memberRepository.save(memberEntity);
    }

    @Override
    public void freeBlockedId(String id) {
        Optional<MemberEntity> optional = memberRepository.findById(id);

        MemberEntity memberEntity = optional.orElseThrow();
        memberEntity.setLocked(false);

        memberRepository.save(memberEntity);
    }

    @Override
    public boolean existMember(String id, String nickname) {
        Optional<MemberEntity> optional = memberRepository.findById(id);
        MemberEntity memberEntity = optional.orElse(null);
        if(memberEntity == null )return false;

        if(memberEntity.getNickname().equals(nickname)) return true;
        else return false;
    }

    private int getGrade(int count){
        if(count>10) return GradeCons.GRADE_KING;
        else if(count>5) return GradeCons.GRADE_WANG;
        else if(count>2) return GradeCons.GRADE_ZZANG;
        else return GradeCons.GRACE_GEN;
    }

    @Override
    public MyInfoDTO getMyResultInfo(String id) {
        MyInfoDTO myInfoDTO = new MyInfoDTO();

        myInfoDTO.setPingWinCount( memberRepository.getWinCount(GameKind.valueOf("PING"), id) );
        myInfoDTO.setPingGrade(  getGrade( myInfoDTO.getPingWinCount() ) );
        myInfoDTO.setDdongWinCount( memberRepository.getWinCount(GameKind.valueOf("LADDER"), id) );
        myInfoDTO.setDdongGrade(  getGrade( myInfoDTO.getDdongWinCount() ) );
        myInfoDTO.setQuizWinCount( memberRepository.getWinCount(GameKind.valueOf("QUIZ"), id) );
        myInfoDTO.setQuizGrade( getGrade( myInfoDTO.getQuizWinCount() ) );
        myInfoDTO.setPingLoseCount( memberRepository.getLoseCount(GameKind.valueOf("PING"), id) );
        myInfoDTO.setDdongLoseCount( memberRepository.getLoseCount(GameKind.valueOf("LADDER"), id) );
        myInfoDTO.setQuizLoseCount( memberRepository.getLoseCount(GameKind.valueOf("QUIZ"), id) );


        log.info("#####getMyResultInfo@MemberServiceImpl : myInfoDTO : {}" , myInfoDTO);
        return myInfoDTO;
    }

    @Override
    @Transactional
    public void modifyNickname(String username, String nickname) throws IllegalArgumentException, OptimisticEntityLockException {

        log.info("  ****** modifyNickname@MemberServiceImpl id=" + username);

        MemberCreateDTO dto = new MemberCreateDTO();
        MemberEntity memberEntity = new MemberEntity();

        Optional<MemberEntity> optional = memberRepository.findById(username);
        if(optional.isPresent()){
            memberEntity=optional.get();
            memberEntity.setNickname(nickname);
            memberRepository.save(memberEntity);
            log.info(" *****   modifyNickname@MemberServiceImpl saved new Nickname");
        } else {
            log.error("error modifyNickname@MemberServiceImpl : no result ");
        }
    }

}
