package com.example.king.controller;

import com.example.king.DTO.GameResultCreateDTO;
import com.example.king.DTO.GameResultListDTO;
import com.example.king.DTO.GameResultListPageDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.constant.GameKind;
import com.example.king.service.GameResultService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String gameResultList(@RequestParam(value="page", defaultValue = "0") String page,
                                @RequestParam(value="search_id", defaultValue="") String search_id,
                                 @RequestParam(value="game_kind", defaultValue = "not_selected") String game_kind, Model model){
        log.info("****** gameResultList@GameResultController : search_id, game_kind : " + search_id + "," + game_kind);

        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        GameResultListPageDTO gameResultListPageDTO = gameResultService.getGameResultList(search_id, game_kind, pageable);

        List<GameResultListDTO> results = gameResultListPageDTO.getGameResultListDTOList();
        int pageSize = gameResultListPageDTO.getPageSize();
        long totalElement = gameResultListPageDTO.getTotalElements();
        int currentPage = gameResultListPageDTO.getCurrentPage();

        model.addAttribute("results", results);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("search_id", search_id);
        model.addAttribute("game_kind", game_kind);

        return "result/viewGameResult";
    }

    @PostMapping("/list")
    public String gameResultListByPost(@RequestParam(value="search_id", defaultValue="") String search_id,
                                 @RequestParam(value="game_kind", defaultValue = "not_selected") String game_kind, Model model){
        log.info("****** gameResultListByPost@GameResultController : search_id, game_kind : " + search_id + "," + game_kind);

        Pageable pageable = PageRequest.of(0, 10); // post로 오는것은 curPage가 없음
        GameResultListPageDTO gameResultListPageDTO = gameResultService.getGameResultList(search_id, game_kind, pageable);

        List<GameResultListDTO> results = gameResultListPageDTO.getGameResultListDTOList();
        int pageSize = gameResultListPageDTO.getPageSize();
        long totalElement = gameResultListPageDTO.getTotalElements();
        int currentPage = gameResultListPageDTO.getCurrentPage();

        model.addAttribute("results", results);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("currentPage", currentPage);
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
