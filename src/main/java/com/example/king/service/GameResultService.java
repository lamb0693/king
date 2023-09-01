package com.example.king.service;

import com.example.king.DTO.GameResultCreateDTO;
import com.example.king.DTO.GameResultListDTO;
import com.example.king.DTO.GameResultListPageDTO;
import com.example.king.Entity.GameResultEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public interface GameResultService {
    //public List<GameResultListDTO> getGameResultList();
    public GameResultListPageDTO getGameResultList(String userId, String gameKind, Pageable pageable);

    public void createGameResult(GameResultCreateDTO gameResultCreateDTO);
}
