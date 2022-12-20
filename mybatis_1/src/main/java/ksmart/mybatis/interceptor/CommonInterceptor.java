package ksmart.mybatis.interceptor;

import java.util.Set;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CommonInterceptor implements HandlerInterceptor{
	//주소 요청에 대한 제어권을 가로채는게 인터셉터다.
	
	private static final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
	
	/**
	 * HandlerMapping이 핸들러 객체를 결정한 후 HandlerAdapter가 핸들러를 호출하기 전에 호출되는 메소드
	 * @return true(controller 진입 허용) false(controller 진입 불허)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//memberId=id001&memberPw=pw001
		//{"memberId","memberPw"}
		Set<String> paramKeySet = request.getParameterMap().keySet();
		
		//memberId:id001, memberPw:pw001
		StringJoiner param = new StringJoiner(", ");
		
		for(String paramKey : paramKeySet) {
			param.add(paramKey + ": " + request.getParameter(paramKey));
		}
		
		//사용자 요청 시 정보
		log.info("ACCESS INFO ==============================================");
		log.info("PORT ::::: {}", request.getLocalPort());
		log.info("ServerName ::::: {}", request.getServerName());
		log.info("HTTP Method ::::: {}", request.getMethod());
		log.info("URI ::::: {}", request.getRequestURI());
		log.info("Client IP ::::: {}", request.getRemoteAddr());
		log.info("Parameter ::::: {}", param);
		log.info("==========================================================");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	/**
	 * HandlerAdapter가 실제로 핸들러 호출할 후 DispatcherServlet이 뷰를 렌더링하기 전에 호출되는 메소드
	 * ModelAndView ? 핸들러 메소드가 실행된 후 뷰에 전달되는 객체
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	/**
	 * DispatcherServlet이 뷰를 렌더링하기 후에 호출되는 메소드
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
