package com.example.king.serviceImpl;

import com.example.king.DTO.GameResultCreateDTO;
import com.example.king.DTO.GameResultListDTO;
import com.example.king.Entity.GameResultEntity;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.GameResultRepository;
import com.example.king.constant.GameKind;
import com.example.king.service.GameResultService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class GameResultServiceImpl implements GameResultService {

    private GameResultRepository gameResultRepository;

//    @Override
//    public List<GameResultListDTO> getGameResultList() {
//        List<GameResultEntity> entityList = gameResultRepository.findAll();
//        List<GameResultListDTO> dtoList = new ArrayList<>();
//
//        for(GameResultEntity entity : entityList){
//            GameResultListDTO dto = new GameResultListDTO();
//            dto.setGame_id( entity.getGame_id());
//            dto.setGame_kind( entity.getGameKind().name());
//            dto.setWinner_id( entity.getWinner().getId());
//            dto.setLoser_id( entity.getLoser().getId());
//            dto.setGamedate( entity.getGamedate());
//
//            dtoList.add(dto);
//        }
//
//        return dtoList;
//    }

    private List<GameResultListDTO> entityToDTO( List<GameResultEntity> entityList){
        List<GameResultListDTO> dtoList = new ArrayList<>();

        for(GameResultEntity entity : entityList){
            GameResultListDTO dto = new GameResultListDTO();
            dto.setGame_id( entity.getGame_id());
            dto.setGame_kind( entity.getGameKind().name());
            dto.setWinner_id( entity.getWinner().getId());
            dto.setLoser_id( entity.getLoser().getId());
            dto.setGamedate( entity.getGamedate());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public List<GameResultListDTO> getGameResultList(String userId, String gameKind){
        log.info("**** getGameResultList@GameResultServiceImpl : user id , gameKind" + userId + ", " + gameKind);

        List<GameResultEntity> entityList = new ArrayList<>();

        if(userId.isEmpty()){
            if(gameKind.equals("not_selected")) entityList = gameResultRepository.findAll();
            else entityList =  gameResultRepository.findAllByGameKind(GameKind.valueOf(gameKind));
        } else {
            if(gameKind.equals("not_selected")) entityList =  gameResultRepository.findAllByIdContains(userId);
            else entityList = gameResultRepository.findAllBy(userId, GameKind.valueOf(gameKind));
        }

        return entityToDTO(entityList);
    }

    @Override
    public void createGameResult(GameResultCreateDTO gameResultCreateDTO) {
        log.info("***** createGameResult@GameResultServiceImpl : gameREusltCreateDTO = " + gameResultCreateDTO.toString());

        GameResultEntity entity = new GameResultEntity();

        entity.setGameKind(GameKind.valueOf( gameResultCreateDTO.getGame_kind() ));

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(gameResultCreateDTO.getWinner_id());
        entity.setWinner( memberEntity);

        memberEntity = new MemberEntity();
        memberEntity.setId(gameResultCreateDTO.getLoser_id());
        entity.setLoser( memberEntity);

        try{
            gameResultRepository.save(entity);
        } catch(Exception e){
            log.error("createGameResult@GamerResultServiceImpl : " + e.getMessage() + entity.getGameKind() + entity.getWinner().getId() + entity.getLoser().getId());
        }

    }
}
