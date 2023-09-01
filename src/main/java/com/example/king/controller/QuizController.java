package com.example.king.controller;

import com.example.king.DTO.QuizDTO;
import com.example.king.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quiz")
@AllArgsConstructor
@Log4j2
public class QuizController {
    private QuizService quizService;
    @GetMapping("/waitingroom")
    public String waitingRoom(){

        return "quiz/waitingRoom";
    }

    @GetMapping("/gameroom")
    public String gameRoom(@RequestParam String roomName, @RequestParam String player, Model model){
        model.addAttribute("roomName", roomName);
        model.addAttribute("player", player);

        return "quiz/gameRoom";
    }

    @GetMapping("/getquiz")
    public String getQuiz(){

        QuizDTO quizDTO = quizService.getOneQuiz();
        log.info(quizDTO);

        return "redirect:/";
    }
}
