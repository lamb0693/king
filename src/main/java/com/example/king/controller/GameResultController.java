package com.example.king.controller;

import com.example.king.DTO.*;
import com.example.king.constant.GameKind;
import com.example.king.service.GameResultService;
import com.example.king.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private MemberService memberService;

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

    @GetMapping("/listRanker")
    public String listRanker(@RequestParam(value="page", defaultValue = "0") String strPage,
                             @RequestParam(value="order_by", defaultValue = "winCount") String order_by,
                             @RequestParam(value="order", defaultValue = "DESC") String order,
                             @RequestParam(value="game_kind", defaultValue = "PING") String game_kind,Model model){

        log.info("####### listRankerWithParameter@GameResultController : order_by, order, game_kind ");
        log.info(strPage, order_by, order, game_kind);

        Sort sort = null;
        if(order.equals("ASC")){
            sort = Sort.by(order_by).ascending();
        } else {
            sort = Sort.by(order_by).descending();
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(strPage), 10, sort);
        //default는 낙하물로
        RankingListPageDTO rankingListPageDTO = memberService.getTotalRanker(GameKind.valueOf(game_kind), order_by, pageable);
        List<RankingDTO> rankers = rankingListPageDTO.getRankingDTOList();
        int pageSize = rankingListPageDTO.getPageSize();
        long totalElement = rankingListPageDTO.getTotalElements();
        int currentPage = rankingListPageDTO.getCurrentPage();

        log.info("listRanker@MemberController : memberListPageDTO" + rankingListPageDTO.toString() );
        model.addAttribute("rankers", rankers);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("game_kind", game_kind);
        model.addAttribute("order_by", order_by);
        model.addAttribute("order", order);

        return "result/searchRankerForm";

    }

    @PostMapping("/listRanker")
    public String listRankerWithParameter( @RequestParam String order_by, @RequestParam String order,
                @RequestParam String game_kind,  Model model){

        log.info("####### listRankerWithParameter@GameResultController : order_by, order, game_kind ");

        Sort sort = null;
        if(order.equals("ASC")){
            sort = Sort.by(order_by).ascending();
        } else {
            sort = Sort.by(order_by).descending();
        }
        Pageable pageable = PageRequest.of(0, 10, sort);

        RankingListPageDTO rankingListPageDTO = memberService.getTotalRanker(GameKind.valueOf(game_kind), order_by, pageable);

        List<RankingDTO> rankers = rankingListPageDTO.getRankingDTOList();
        int pageSize = rankingListPageDTO.getPageSize();
        long totalElement = rankingListPageDTO.getTotalElements();
        int currentPage = rankingListPageDTO.getCurrentPage();

        log.info("listRanker@MemberController : memberListPageDTO" + rankingListPageDTO.toString() );
        model.addAttribute("rankers", rankers);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("game_kind", game_kind);
        model.addAttribute("order_by", order_by);
        model.addAttribute("order", order);

        return "result/searchRankerForm";
    }

}
