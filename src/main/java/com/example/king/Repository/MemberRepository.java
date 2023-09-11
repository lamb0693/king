package com.example.king.Repository;

import com.example.king.Entity.MemberEntity;
import com.example.king.constant.GameKind;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

     //Page<MemberEntity> findAll(Pageable pageable);
     Boolean existsByNickname(String nickname);

     @Query("select m.nickname, count(r.winner) as winCount from MemberEntity m left outer join GameResultEntity  r ON ( m.id = r.winner.id  and r.gameKind = :gameKind ) group by m.nickname order by winCount desc limit 5")
     List<Object[]> findTop5Players(@Param("gameKind") GameKind gameKind);

     @Query("select m.nickname as nickName, count(r.winner) as winCount from MemberEntity m left outer join GameResultEntity  r ON ( m.id = r.winner.id  and r.gameKind = :gameKind ) group by m.nickname")
     Page<Object[]> findAllRanker(@Param("gameKind") GameKind gameKind, Pageable pageable);

     @Query("select count(r.winner) as winCount from GameResultEntity r where r.winner.id = :id  and r.gameKind = :gameKind" )
     int getWinCount(@Param("gameKind") GameKind gameKind, @Param("id") String id);

     @Query("select count(r.loser) as winCount from GameResultEntity r where r.loser.id = :id  and r.gameKind = :gameKind" )
     int getLoseCount(@Param("gameKind") GameKind gameKind, @Param("id") String id);

}
