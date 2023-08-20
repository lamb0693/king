package com.example.king.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ping")
public class PingController {

    @GetMapping("/waitingroom")
    public String waitingRoom(){
        return "ping/waitingRoom.html";
    }

    @GetMapping("/gameroom")
    public String gameRoom(@RequestParam String roomName, @RequestParam String player,  Model model){
        model.addAttribute("roomName", roomName);
        model.addAttribute("player", player);

        return "ping/gameRoom.html";
    }

}
