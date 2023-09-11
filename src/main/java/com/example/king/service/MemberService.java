package com.example.king.service;

import com.example.king.DTO.*;
import com.example.king.constant.GameKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface MemberService {

//    public List<MemberListDTO> getMemberList();

    public MemberListPageDTO getMemberListWithPage(Pageable pageable);

    public int saveMember(MemberCreateDTO memberCreateDTO, PasswordEncoder passwordEncoder);

    public MemberAuthDTO getAuthDTO(String id);

    public void deleteById(String id);

    Boolean checkIdExist(String id);

    Boolean checkNicknameExist(String nickname);

    void saveNewPassword(String id, String password);

    List<RankingDTO> getRanker(GameKind gameKind);

    RankingListPageDTO getTotalRanker(GameKind gameKind, String orderBy, Pageable pageable);

    void blockId(String id);

    void freeBlockedId(String id);

    boolean existMember(String id, String nickname);

    MyInfoDTO getMyResultInfo(String id);

}
