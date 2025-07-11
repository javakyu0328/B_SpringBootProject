package com.tel.member.controller;

import com.tel.member.dto.MemberDTO;
import com.tel.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입
    private  final MemberService memberService;

   
    //회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm(){
        System.out.printf("겟 메서드 진입");
        return "save";
    }

    @PostMapping("member/save")
    public String save(@ModelAttribute MemberDTO memberDTO ){

        memberService.save(memberDTO);

        return "login";
    }

    //로그인 페이지 출력 요청
    @GetMapping("/member/login")
    public String loginForm(){

        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){

        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null){
            //login성공
            log.info("로그인 성공 진입");
            session.setAttribute("loginEmail",loginResult.getMemberEmail());
            return "main";
        }else {
            //login 실패
            log.info("실패 진입");
            return "login";
        }
    }

    //회원리스트
    @GetMapping("/member/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOLIST =  memberService.findAll();
        //어떠한 html로 가져갈 데이터가 있다면 model 사용
        model.addAttribute("memberList",memberDTOLIST);

        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);

        return "detail";
    }
    //정보 수정 페이지
    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        String myEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember",memberDTO);

        return "update";
    }

    //정보 수정 요청
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);

        return "redirect:/member/"+memberDTO.getId();
    }

    //회원 삭제
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id){
        memberService.deleteById(id);

        return "redirect:/member/";
    }

    //로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

    //이메일 유효성 검사(ajax)
    @PostMapping("/member/email-check")
    public @ResponseBody String emailcheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail="+memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);

        return checkResult;
    }
}
