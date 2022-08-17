package com.acon.board.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("LoginCheck.preHandle : 해당 url을 요청하기 전");
		String prevPage=request.getHeader("Referer");//요청한 페이지의 이전 페이지(로그인하면 되돌아갈 페이지)
		//로그인이 되어있지 않으면 강제로 로그인페이지로 이동
		HttpSession session=request.getSession();
		Object loginUser_obj=session.getAttribute("loginUser");
		if(loginUser_obj!=null) {
			return true;			
		}else {
			session.setAttribute("msg", "로그인 하셔야 이용하실 수 있습니다!(LoginCheckInterceptor)");
			session.setAttribute("redirectPage", prevPage); //로그인 성공 시 이동할 페이지
			response.sendRedirect("/user/login.do");
			return false; //요청한 controller로 전달하지 않음
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("LoginCheck.postHandle : 해당 url이 요청이 완료됨(응답 직전)");

	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("LoginCheck.afterCompletion : 해당 url의 응답이 완료된 후(tymeleaf 동적 파일이 실행 완료)");
	}
}
