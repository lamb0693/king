package com.example.king.controller;

import com.example.king.DTO.MemberAuthDTO;
import com.example.king.DTO.MyInfoDTO;
import com.example.king.DTO.RankingDTO;
import com.example.king.constant.GameKind;
import com.example.king.service.MemberService;
import com.example.king.service.MemberUserDetail;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@Log4j2
public class HomeController {
    MemberService memberService;
    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "social", defaultValue = "false") String social){

        List<RankingDTO> pingRankList = memberService.getRanker(GameKind.valueOf("PING"));
        List<RankingDTO> ladderRankList = memberService.getRanker(GameKind.valueOf("LADDER"));
        List<RankingDTO> quizRankList = memberService.getRanker(GameKind.valueOf("QUIZ"));

        model.addAttribute("ladderRankList", ladderRankList);
        model.addAttribute("pingRankList", pingRankList);
        model.addAttribute("quizRankList", quizRankList);

        model.addAttribute("social", social);

        return "home";
    }
}
