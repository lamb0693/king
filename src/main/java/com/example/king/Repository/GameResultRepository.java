package com.example.king.Repository;

import com.example.king.DTO.GameResultListDTO;
import com.example.king.Entity.GameResultEntity;
import com.example.king.constant.GameKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResultEntity, Long> {

//    @Query(value = "select g from GameResultEntity as g where (g.winner.id like %:userId%  or g.loser.id like %:userId%) and g.gameKind=:gameKind")
//    List<GameResultEntity> findAllBy(String userId, GameKind gameKind);

    @Query(value = "select g from GameResultEntity as g where (g.winner.id like %:userId%  or g.loser.id like %:userId%) and g.gameKind=:gameKind")
    Page<GameResultEntity> findAllBy(String userId, GameKind gameKind, Pageable pageable);

    @Query(value = "select g from GameResultEntity as g where g.winner.id like %:userId%  or g.loser.id like %:userId%")
    Page<GameResultEntity> findAllByIdContains(String userId, Pageable pageable);

    Page<GameResultEntity> findAllByGameKind(GameKind gameKind, Pageable pageable);
}
