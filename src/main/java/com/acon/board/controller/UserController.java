package com.acon.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.acon.board.dto.IdCheck;
import com.acon.board.dto.Paging;
import com.acon.board.dto.User;
import com.acon.board.mapper.UserMapper;
import com.acon.board.service.UserService;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	// 로그인, 회원 관리 리스트(패이징), 상세, 글쓴이력 상세,회원가입(등록),개인정보수정(수정)
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list/{page}")
	public String list(@PathVariable int page, Model  model) {
		int row=7;
		int startRow=(page-1)*row;
		List<User> userList=userMapper.selectPageAll(startRow,row);
		int rowCount=userMapper.selectPageAllCount();
		
		Paging paging=new Paging(page, rowCount, "/user/list/",row);
		model.addAttribute("paging",paging);
		model.addAttribute("userList",userList);

		
		
		model.addAttribute("row",row);
		model.addAttribute("rowCount",rowCount);
		model.addAttribute("page",page);
		System.out.println(userList);
		return "/user/list";
	}
	@GetMapping("/detail/{userId}")
	public String detail(@PathVariable String userId,Model model) {
		User user=userMapper.selectOne(userId);
		model.addAttribute(user);
		System.out.println(user);
		return "/user/detail";
	}
	@PostMapping("/update.do")
	public String update(User user) {
		int update=0;
		update=userMapper.updateOne(user);
		if(update>0) {
			return "redirect:/user/list/1";
		}else {
			return "redirect:/user/detail/"+user.getUser_id();
		}
	}
	@GetMapping("/delete/{userId}")
	public String delete(
			@PathVariable String userId
			) {
		int delete=0;
		try {
			delete=userService.removeUser(userId);
		}catch (Exception e) {e.printStackTrace();}
		if(delete>0) {
			return "redirect:/user/list/1";
		}else {
			return "redirect:/user/detail/"+userId;
		}
	}
	@GetMapping("/insert.do")
	public void insert() {};
	@PostMapping("/insert.do")
	public String insert(User user) {
		int insert=0;
		try {
			insert=userMapper.insertOne(user);
		} catch (Exception e) {e.printStackTrace();}
		if(insert>0) {
			return "redirect:/user/list/1";
		}else {
			return "redirect:/user/insert.do";
		}
	};
	@GetMapping("/login.do")
	public void login(HttpServletRequest req) {
		System.out.println(req.getHeader("Referer"));
	}
	@PostMapping("/login.do")
	public String login(
			@RequestParam(value = "user_id")String userId,
			@RequestParam(value = "pw") String pw,
			HttpSession session
			){
		User user=null;
		try {
			user=userMapper.selectPwOne(userId, pw);
		} catch (Exception e) {e.printStackTrace();}
		System.out.println(user);
		if(user!=null) {
			session.setAttribute("loginUser", user);
			Object redirectPage=session.getAttribute("redirectPage");
			session.removeAttribute("redirectPage");
			if(redirectPage!=null) {
				return "redirect:"+redirectPage;
			}else {
				return "redirect:/";				
			}
		}else {
			return "redirect:/user/login.do";
		}
	}
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		//session.invalidate();
		session.removeAttribute("loginUser");
		return "redirect:/";
	}
	
	@GetMapping("/signup.do")
	public void signup() {}
	
	
	
	@PostMapping("/signup.do")
	public void signup(User user) {
		System.out.println(user);
	}
	//@ResponseBody User : 응답을 json으로 하는데 해당 객체를 json으로 변경해서 반환한다. 
	@GetMapping("/idCheck/{userId}")
	public @ResponseBody IdCheck idCheck(@PathVariable String userId) {
		IdCheck idCheck=new IdCheck();
		System.out.println(userId);
		User user=userMapper.selectOne(userId);
		if(user!=null) {
			idCheck.idCheck=true;
			idCheck.user=user;
		}
		return idCheck;
	}
}




















