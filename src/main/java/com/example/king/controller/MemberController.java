package com.example.king.controller;

import com.example.king.DTO.MemberCreateDTO;
import com.example.king.DTO.MemberListDTO;
import com.example.king.DTO.MemberListPageDTO;
import com.example.king.DTO.PasswordResetDTO;
import com.example.king.service.EmailService;
import com.example.king.service.MemberService;
import com.example.king.service.MemberUserDetail;
import com.example.king.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private EmailService emailService;

    @GetMapping("/list")
    public String viewMembers(@RequestParam(value="page", defaultValue = "0") String page, Model model){
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        MemberListPageDTO memberListPageDTO = memberService.getMemberListWithPage(pageable);

        List<MemberListDTO> members = memberListPageDTO.getMemberListDTOList();
        int pageSize = memberListPageDTO.getPageSize();
        long totalElement = memberListPageDTO.getTotalElements();
        int currentPage = memberListPageDTO.getCurrentPage();

        if(members ==null || members.isEmpty()){
            log.info("viewMembers@MemberController : members is null or empty");
        }

        log.info("viewMembers@MemberController : memberListPageDTO" + memberListPageDTO.toString() );
        model.addAttribute("members", members);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElement", totalElement);
        model.addAttribute("currentPage", currentPage);

        return "member/viewMembers";
    }

    @GetMapping("blockId/{id}")
    public String blockId(@PathVariable String id){
        memberService.blockId(id);

        return "redirect:/member/list";
    }

    @GetMapping("freeId/{id}")
    public String freeId(@PathVariable String id){
        memberService.freeBlockedId(id);

        return "redirect:/member/list";
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
            return "redirect:/";
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

    @GetMapping("/getJwtToken")
    public String getJwtToken(@AuthenticationPrincipal MemberUserDetail memberUserDetail, Model model){
        log.info("getJwtToken@MemberController : id =>" + memberUserDetail.getUsername());
        String token = tokenService.createToken(60*24, memberUserDetail.getUsername());
        model.addAttribute("token", token);

        return "member/viewToken";
    }

    @GetMapping("/testToken")
    public String validateToken(@RequestParam String token){
        try{
            tokenService.getIdFromToken(token);
        } catch(Exception e){
            log.error(e.getMessage());
        }

        return "redirect:/";
    }

    // resetPasswordForm : Id와 nickname을 입력 => post로 보내서 확인
    @GetMapping("/forgotPassword")
    public String resetPassword(){
        return "member/forgotPasswordForm";
    }

    // id nick name을 받아 검증후 mail을 보냄.
    @PostMapping("/forgotPassword")
    public String sendResetPasswordEmail(@RequestParam String id, @RequestParam String nickname, RedirectAttributes redirectAttributes){
        if( memberService.existMember(id, nickname) ) {
            String retMessage = emailService.sendHTMLEmail(id, "비밀 번호 수정 링크입니다", "/email/resetPasswordTemplate");
            if( retMessage.equals("success")){
                redirectAttributes.addFlashAttribute("sendedEmail", "true");
                return "redirect:/";
            }
            else {
                redirectAttributes.addFlashAttribute("error", "메일 전송이 실패하였습니다. 관리자에게 문의하세요");
                return "redirect:/member/forgotPassword";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "member id와 nickname을 확인하세요");
            return "redirect:/member/forgotPassword";
        }
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String id, @RequestParam String token, Model model){
        log.info("##### resetPassword@MemberController : id + token : >");
        log.info(id);
        log.info(token);
        model.addAttribute("id", id);
        model.addAttribute("token", token);

        return "member/resetPasswordForm";
    }

    @PostMapping("/resetPassword/process")
    public ResponseEntity<String> resetPasswordProcessing(@RequestBody PasswordResetDTO passwordResetDTO, RedirectAttributes redirectAttributes){

        //**** filter를 다시 들어가서 login이 다시 불림.. 해결 해야 할 듯
        String id = passwordResetDTO.getId();
        String password = passwordEncoder.encode( passwordResetDTO.getPassword() );
        // tokenFilter 가 두번 돌면서 중간에 문제가 생김 통해서 온 것과 관련 있는지 addFlashAttribute가 먹지 않음??
        try{
            memberService.saveNewPassword(id, password);
            return ResponseEntity.ok().body("아이디가 변경되었습니다. 새로운 아이디로 로그인하세요");
        } catch (Exception e){
            log.error("resetPasswordProcessing@MemberController : " + e.getMessage() );
            return ResponseEntity.badRequest().body("아이디 변경이 실패 했습니다 ,관리자에게 문의하세요");
        }
    }

}
