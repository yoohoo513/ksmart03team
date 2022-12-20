package ksmart.mybatis.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksmart.mybatis.dto.LoginInfo;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.mapper.MemberMapper;

@Component
public class LoginInterceptor implements HandlerInterceptor{
	
	private final MemberMapper memberMapper;
	
	public LoginInterceptor(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	/**
	 * HandlerMapping이 핸들러 객체를 결정한 후 HandlerAdapter가 핸들러를 호출하기 전에 호출되는 메소드
	 * @return true(controller 진입 허용) false(controller 진입 불허)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		LoginInfo loginInfo = (LoginInfo) session.getAttribute("S_MEMBER_INFO");
		
		//cookie 하루동안 로그인
		Cookie cookie = WebUtils.getCookie(request, "loginKeepId");
		
		if(cookie !=null && loginInfo == null) {
			String loginKeepId = cookie.getValue();
			Member member = memberMapper.getMemberInfoById(loginKeepId);
			if(member != null) {
				loginInfo = new LoginInfo(loginKeepId, member.getMemberName(), member.getMemberLevel());
				
				session.setAttribute("S_MEMBER_INFO", loginInfo);
			}
		}
		
		if(loginInfo == null) {
			response.sendRedirect("/member/login");
			return false;
		}else {
			//판매자일 경우 주소 (해당 주소 요청 시 메인화면으로 전환되게 조치)
			String requestURI = request.getRequestURI();
			int loginLevel = loginInfo.getLoginLevel();
			
			if(loginLevel == 2) { 
				if(  requestURI.indexOf("/member/memberList") > -1
				   ||requestURI.indexOf("/member/add") > -1
				   ||requestURI.indexOf("/member/remove") > -1
				   ||requestURI.indexOf("/member/loginHistory") > -1
				   ||requestURI.indexOf("/goods/sellerList") > -1
				   ||requestURI.indexOf("/member/remove") > -1
				   ||requestURI.indexOf("/member/modify") > -1) {
						
					msgRedirect(response);
					//response.sendRedirect("/");
					return false;
				}
			}
			if(loginLevel == 3) { 
				if(  requestURI.indexOf("/goods/remove") > -1
				   ||requestURI.indexOf("/goods/modify") > -1
				   ||requestURI.indexOf("/goods/add") > -1
				   ||requestURI.indexOf("/member/add") > -1
				   ||requestURI.indexOf("/member/loginHistory") > -1
				   ||requestURI.indexOf("/goods/sellerList") > -1
				   ||requestURI.indexOf("/member/modify") > -1
				   ||requestURI.indexOf("/member/remove") > -1
				   ||requestURI.indexOf("/member/memberList") > -1) {
					
					msgRedirect(response);
					
					return false;
				}
			}
			//구매자일 경우 주소 (해당 주소 요청 시 메인화면으로 전환되게 조치)
		}
		
		return true;
	}
	
	public static void msgRedirect(HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=utf-8;");
			
			PrintWriter writer = response.getWriter();
			
			writer.write("<script>alert('비정상적인 접근입니다.'); location.href='/';</script>");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
