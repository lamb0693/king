package com.example.king.service;

import com.example.king.DTO.GameResultCreateDTO;
import com.example.king.DTO.GameResultListDTO;
import org.springframework.ui.Model;

import java.util.List;

public interface GameResultService {
    //public List<GameResultListDTO> getGameResultList();
    public List<GameResultListDTO> getGameResultList(String userId, String gameKind);

    public void createGameResult(GameResultCreateDTO gameResultCreateDTO);
}
