package com.example.king.controller;

import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.Entity.MemberEntity;
import com.example.king.Repository.MemberRepository;
import com.example.king.service.MemberService;
import com.example.king.service.MemberUserDetail;
import com.example.king.serviceImpl.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public String viewMembers(Model model){
        List<MemberListDTO> members = memberService.getMemberList();

        if(members ==null || members.isEmpty()){
            log.info("viewMembers@MemberController : members is null or empty");
        }

        model.addAttribute("members", members);

        return "member/viewMembers.html";
    }

    @GetMapping("/create")
    public String createMemberForm(){
        return "member/createMember";
    }

    // Validation 추가
    // Errors 매개변수를 넣어주면 Validation Errors를 사용가능 ,setting 되면 errors setting후 createForm return
    // createForm에서 error 표시
    @PostMapping("/create")
    public String createMember(@Valid @ModelAttribute MemberCreateDTO memberCreateDTO, Errors errors,
                               Model model, RedirectAttributes redirectAttributes){
        log.info(memberCreateDTO);

        if(errors.hasErrors()){
            log.info("createMember@MemberController : errors is set");
            log.info(errors);

            // error 에서 field(변수 이름) 이름과 내용을 구해 modelAttribute에 추가한다
            // createMemberForm에서 사용
            List<FieldError> fieldErrors = errors.getFieldErrors();
            for(FieldError error : fieldErrors){
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }

            model.addAttribute("errors", errors);
            model.addAttribute("previousValue", memberCreateDTO);
            return "/member/createMember.html";
        } else {
            if( memberService.saveMember(memberCreateDTO, passwordEncoder) == -1 ){
                //*********** 실패했을 때 어떻게 할지 나중에 추가
            };
            //성공하면 "saveResult"를 redirectResult로 setting 후 member/list로 redirect한다
            //***** 나중에 viewMembers.html에 처리 과정 추가 ****
            redirectAttributes.addFlashAttribute("saveResult", "saveSuccess");
            return "redirect:/member/list";
        }
    }

    @GetMapping("/modify")
    public String modifyInfo(){


        return "/member/modifyInfo";
    }

    @PostMapping("/modifyPasword")
    public String modifyPassword(@AuthenticationPrincipal MemberUserDetail userDetail, @RequestParam String password1, HttpServletRequest request
        ,RedirectAttributes redirectAttributes){

        memberService.saveNewPassword(userDetail.getUsername(), passwordEncoder.encode(password1));
        request.getSession().invalidate();

        redirectAttributes.addFlashAttribute("passwordModified", "true");

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteMember(@AuthenticationPrincipal MemberUserDetail memberUserDetail, HttpServletRequest request
            ,RedirectAttributes redirectAttributes){
        log.info("deleteMember@MemberController memberUserDetail" + memberUserDetail.toString());

        memberService.deleteById(memberUserDetail.getUsername());
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("withdrawn", "true");

        return "redirect:/";
    }

    @GetMapping("/exist/id/{id}")
    public ResponseEntity<Boolean> checkIdExist(@PathVariable String id){
        //log.info(" ******** checkIdExist@LoginController return OK + :" +memberService.checkIdExist(id) );

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(memberService.checkIdExist(id));
    }

    @GetMapping("/exist/nickname/{nickname}")
    public ResponseEntity<Boolean> checkNicknameExist(@PathVariable String nickname){
        log.info(" ******** checkIdExist@LoginController return OK + :" +memberService.checkIdExist(nickname) );

        return ResponseEntity.ok(memberService.checkNicknameExist(nickname));
    }

}
