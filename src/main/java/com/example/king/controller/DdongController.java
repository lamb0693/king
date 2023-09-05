package com.example.king.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ddong")
@AllArgsConstructor
@Log4j2
public class DdongController {
    @GetMapping("/waitingroom")
    public String waitingRoom(){

        return "ddong/waitingRoom";
    }

    @GetMapping("/gameroom")
    public String gameRoom(@RequestParam String roomName, @RequestParam String player, Model model){
        model.addAttribute("roomName", roomName);
        model.addAttribute("player", player);

        return "ddong/gameRoom";
    }
}
