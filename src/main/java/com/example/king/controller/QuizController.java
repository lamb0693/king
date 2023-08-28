package com.example.king.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quiz")
public class QuizController {
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
}
