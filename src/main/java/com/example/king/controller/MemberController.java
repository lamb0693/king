package com.example.king.controller;

import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/viewMember")
    public String viewMembers(Model model){

        MemberEntity memberEntity = new MemberEntity();
        memberEntity = memberRepository.getMemberEntityById("ldw");

        System.out.println(memberEntity.getNickname());
        model.addAttribute("members", memberEntity);
        model.addAttribute("hello", "Hello World");

        return "member/viewMembers.html";
    }
}
