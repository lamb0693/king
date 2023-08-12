package com.example.king.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/")
    public String waiting(){
        return "ping/waitingRoom.html";
    }

    @GetMapping("/gameroom")
    public String gameRoom(@RequestParam String roomName, @RequestParam String player,  Model model){
        model.addAttribute("roomName", roomName);
        model.addAttribute("player", player);

        return "ping/gameRoom.html";
    }

}
