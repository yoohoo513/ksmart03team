package ksmart.mybatis.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import ksmart.mybatis.dto.LoginHistory;
import ksmart.mybatis.dto.LoginInfo;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;
import ksmart.mybatis.mapper.MemberMapper;
import ksmart.mybatis.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	private final MemberService memberService;
	private final MemberMapper memberMapper;
	
	public MemberController(MemberService memberService, MemberMapper memberMapper) {
		this.memberService = memberService;
		this.memberMapper =memberMapper;
	}

	@GetMapping("/memberList")
	public String getMemberList(Model model
							   ,@RequestParam(value = "searchKey" ,required = false) String searchKey
							   ,@RequestParam(value = "searchValue" ,required = false, defaultValue = "") String searchValue) {
		
		List<Member> memberList = memberService.getMemberList(searchKey, searchValue);
		
		model.addAttribute("memberList",memberList);
		model.addAttribute("title","회원목록");
		
		return "member/memberList";
	}
	
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();
		
		model.addAttribute("memberLevelList",memberLevelList);
		model.addAttribute("title","회원가입");
		
		return "member/addMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		
		log.info("회원가입 쿼리파라미터: {}", member);
		memberService.addMember(member);
		
		return "redirect:/member/memberList";
	}
	
	@GetMapping("/checkId")
	@ResponseBody
	public boolean checkMemberId(@RequestParam(value="memberId") String memberId) {
		
		boolean isChecked =false;
		
		isChecked = memberMapper.checkMemberId(memberId);
		
		return isChecked;
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(value = "memberId", required = false) String memberId, Model model ) {
		
		Member memberInfo = memberService.getMemberInfoById(memberId);
		List<MemberLevel> memberLevelList = memberService.getMemberLevelList();

		model.addAttribute("title","회원 수정");
		model.addAttribute("memberInfo",memberInfo);
		model.addAttribute("memberLevelList",memberLevelList);
		
		return "member/modifyMember";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		
		log.info("회원수정 : {}",member);
		
		memberService.modifyMember(member);
		
		return "redirect:/member/memberList";
	}
	
	@GetMapping("/removeMember/{memberId}")
	public String removeMember(@PathVariable(value = "memberId") String memberId
							  ,@RequestParam(value = "msg", required =  false) String msg
							  ,Model model) {
		
		model.addAttribute("title","회원탈퇴");
		model.addAttribute("memberId", memberId);
		if(msg != null) model.addAttribute("msg", msg);
		
		return "member/removeMember";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(value = "memberId") String memberId
							  ,@RequestParam(value = "memberPw") String memberPw
							  ,RedirectAttributes reAttr) {
		
		log.info("memberId : {}, memberPw : {}", memberId, memberPw);
		
		Map<String, Object> checkResult = memberService.checkPwMymemberId(memberId,memberPw);
		boolean isChecked = (boolean) checkResult.get("result");
		
		// 1. 비밀번호가 일치하지 않을 시 회원탈퇴 폼으로 리다이렉트
		// /member/removeMember?memberId=id001
		String redirectURI = "redirect:/member/memberList";
		
		if(!isChecked) {
			redirectURI="redirect:/member/removeMember/"+memberId;
			//reAttr.addAttribute("memberId",memberId);
			reAttr.addAttribute("msg","입력하신 회원의 정보가 일치하지 않습니다.");
		}else {
			// 2. 비밀번호가 일치 시 관계 테이블을 고려하여 탈퇴 진행 (Service계층에서 탈퇴 프로세스 완성(because 트랜잭션))
			Member member = (Member) checkResult.get("memberInfo");
			memberService.removeMember(member);
		}
		
		return redirectURI;
	}
	
	@GetMapping("/loginHistory")
	public String loginHistory(@RequestParam(value="currentPage", defaultValue = "1", required = false) int currentPage
							  ,Model model) {
		
		Map<String, Object> paramMap = memberService.getLoginHistory(currentPage);
		int lastPage = (int) paramMap.get("lastPage");
		List<LoginHistory> loginHistory = (List<LoginHistory>) paramMap.get("loginHistory");
		
		int startPageNum = (int) paramMap.get("startPageNum");
		int endPageNum = (int) paramMap.get("endPageNum");
		
		model.addAttribute("title","로그인기록");
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("loginHistory",loginHistory);
		model.addAttribute("lastPage",lastPage);
		
		return "login/loginHistory";
	}
	
	@GetMapping("/login")
	public String login(Model model
						,@RequestParam(value="msg", required = false) String msg) {
		
		model.addAttribute("title","로그인");
		
		if(msg !=null) model.addAttribute("msg",msg);
		
		return "login/login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam(name="memberId") String memberId
						,@RequestParam(name="memberPw") String memberPw
						,RedirectAttributes reAttr
						,HttpSession session
						,HttpServletRequest request
						,HttpServletResponse response) {
		
		
		log.info("memberId : {}, memberPw : {}", memberId, memberPw);
		
		Map<String, Object> checkResult = memberService.checkPwMymemberId(memberId,memberPw);
		boolean isChecked = (boolean) checkResult.get("result");
		
		// 1. 비밀번호가 일치하지 않을 시 로그인 폼으로 리다이렉트
		// /member/removeMember?memberId=id001
		String redirectURI = "redirect:/";
		
		if(!isChecked) {
			redirectURI="redirect:/member/login";
			reAttr.addAttribute("msg","입력하신 회원의 정보가 일치하지 않습니다.");
		}else {
			// 2. 비밀번호가 일치 시 세션 저장
			Member member = (Member) checkResult.get("memberInfo");
			/*
			 * session.setAttribute("SID", memberId); session.setAttribute("SLEVEL",
			 * member.getMemberLevelName()); session.setAttribute("SNAME",
			 * member.getMemberName());
			 */
			LoginInfo loginInfo = new LoginInfo(memberId, member.getMemberName(), member.getMemberLevel());
			
			session.setAttribute("S_MEMBER_INFO", loginInfo);
			
			Cookie cookie = new Cookie("loginKeepId", memberId);
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
		}
		
		return redirectURI;
		
		
		//return "redirect:/login/login";
		//return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/member/login";
	}
}
