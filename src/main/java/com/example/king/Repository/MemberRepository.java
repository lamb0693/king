package com.example.king.Repository;

import com.example.king.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    public MemberEntity getMemberEntityById(String id);

    Boolean existsByNickname(String nickname);
}
