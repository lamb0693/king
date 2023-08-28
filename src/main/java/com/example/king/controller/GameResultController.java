package com.example.king.controller;

import com.example.king.DTO.GameResultCreateDTO;
import com.example.king.DTO.GameResultListDTO;
import com.example.king.constant.GameKind;
import com.example.king.service.GameResultService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/result")
@AllArgsConstructor
@Log4j2
public class GameResultController {

    private GameResultService gameResultService;

    @GetMapping("/list")
    public String gameResultList(@RequestParam(value="search_id", defaultValue="") String search_id,
                                 @RequestParam(value="game_kind", defaultValue = "not_selected") String game_kind, Model model){
        log.info("****** gameResultList@GameResultController : search_id, game_kind : " + search_id + "," + game_kind);

        List<GameResultListDTO> dtoList = gameResultService.getGameResultList(search_id, game_kind);

        model.addAttribute("dtoList", dtoList);

        return "result/viewGameResult";
    }

    @PostMapping("/list")
    public String gameResultListByPost(@RequestParam(value="search_id", defaultValue="") String search_id,
                                 @RequestParam(value="game_kind", defaultValue = "not_selected") String game_kind, Model model){
        log.info("****** gameResultListByPost@GameResultController : search_id, game_kind : " + search_id + "," + game_kind);

        List<GameResultListDTO> dtoList = gameResultService.getGameResultList(search_id, game_kind);

        model.addAttribute("dtoList", dtoList);
        model.addAttribute("search_id", search_id);
        model.addAttribute("game_kind", game_kind);

        return "result/viewGameResult";
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> createGameResult(@RequestBody GameResultCreateDTO gameResultCreateDTO){
        log.info("****** createGameResult@GameResultController : gameResultCreateDTO = " + gameResultCreateDTO.toString() );
        gameResultService.createGameResult( gameResultCreateDTO );

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(true);
    }

    //test endpoint  ==> success
    @GetMapping("/create")
    @ResponseBody
    public ResponseEntity<Boolean> createGameResult(){
        log.info("**********I am in createGameResult@GameREsultController");
        GameResultCreateDTO gameResultCreateDTO = new GameResultCreateDTO();
        gameResultCreateDTO.setGame_kind("QUIZ");
        gameResultCreateDTO.setWinner_id("ccc@ddd.eee");
        gameResultCreateDTO.setLoser_id("aaa@bbb.ccc");

        gameResultService.createGameResult( gameResultCreateDTO );

        return ResponseEntity.ok(true);
    }


}
