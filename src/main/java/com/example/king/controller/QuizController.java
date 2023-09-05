package com.example.king.controller;

import com.example.king.DTO.QuizDTO;
import com.example.king.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    @CrossOrigin(origins = "http://localhost:3002")
    @GetMapping("/token/getquiz")
    @ResponseBody
    public ResponseEntity<QuizDTO> getQuiz(){

        QuizDTO quizDTO = quizService.getOneQuiz();
        log.info(quizDTO);

        return ResponseEntity.ok(quizDTO);
    }

    @GetMapping("test")
    public String test(){

        QuizDTO quizDTO = quizService.getOneQuiz();
        log.info(quizDTO);

        return "quiz/testFetch";
    }
}
