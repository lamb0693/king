package com.example.king.Repository;

import com.example.king.Entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

     Page<MemberEntity> findAll(Pageable pageable);
     Boolean existsByNickname(String nickname);
}
