package com.acon.board.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.acon.board.dto.Reply;
import com.acon.board.dto.User;
import com.acon.board.mapper.ReplyMapper;

@Controller
@RequestMapping("/reply")
public class ReplyController {
	@Autowired
	ReplyMapper replyMapper;
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
	
	@PostMapping("/insert.do")
	public String insert
		(	Reply reply,
			@SessionAttribute(required = false) User loginUser,
			HttpSession session,
			MultipartFile imgFile) {
		
		if(loginUser!=null && loginUser.getUser_id().equals(reply.getUser().getUser_id())) {
			int insert=0;
			String msg="댓글 등록 실패";
			try {
				if(!imgFile.isEmpty()) {
					String[] types=imgFile.getContentType().split("/");//"image/png".split("/") =>{"image","png"}
					if(types[0].equals("image")) {
						String newFileName="reply_"+System.nanoTime()+"."+types[1];
						Path path=Paths.get(savePath+"/"+newFileName);
						imgFile.transferTo(path);
						reply.setImg_path(newFileName);
					}
				}				
				insert=replyMapper.insertOne(reply);				
			} catch (Exception e) {//참조키(보드가 삭제된 경우) 문자열이너무길거나 ...
				msg="db 댓글 등록 에러! 새로 고치고 다시 시도하세요.";
				e.printStackTrace();
			}
			if(insert>0) {
				msg="댓글 등록 성공";
			}
			session.setAttribute("msg", msg);
			return "redirect:/board/detail/"+reply.getBoard_no();			
		}else {
			session.setAttribute("msg", "댓글을 등록하려면 로그인을 하세요!");
			return "redirect:/user/login.do";			
		}
	}
	@PostMapping("/update.do")
	public String update(
			Reply reply,
			@SessionAttribute(required = false) User loginUser,
			MultipartFile imgFile,
			HttpSession session) {
		if(loginUser!=null && loginUser.getUser_id().equals(reply.getUser().getUser_id())) {
			int update=0;
			String msg="";
			try {
				if(imgFile!=null && !imgFile.isEmpty()) { //기존의 img_path가 있으면 삭제하고 새로 등록
					String[] types=imgFile.getContentType().split("/");
					if(types[0].equals("image")) {
						if(reply.getImg_path()!=null) {
							File file=new File(savePath+"/"+reply.getImg_path());
							boolean del=file.delete();
							System.out.println("기존 이미지 삭제:"+del);
						}
						String newFileName="reply_"+System.nanoTime()+"."+types[1];
						Path path=Paths.get(savePath+"/"+newFileName);
						imgFile.transferTo(path);
						reply.setImg_path(newFileName);
					}
				}
				update=replyMapper.updateOne(reply);				
			}catch (Exception e) {e.printStackTrace();}
			
			msg=(update>0)?"수정 성공!":"수정 실패!";
			session.setAttribute("msg", msg);
			return "redirect:/board/detail/"+reply.getBoard_no();			
		}else {
			return "redirect:/user/login.do";			
		}
	}
	@GetMapping("/delete/{replyNo}/{boardNo}/{userId}")
	public String delete(
			@PathVariable int replyNo,
			@PathVariable int boardNo,
			@PathVariable String userId,
			@SessionAttribute(required = false) User loginUser,
			HttpSession session,
			Model model) {
		if(loginUser!=null&& loginUser.getUser_id().equals(userId)) {
			int delete=0;
			delete=replyMapper.deleteOne(replyNo);
			if(delete>0) {
				session.setAttribute("msg", "댓글 삭제 성공!");
			}else {
				session.setAttribute("msg", "댓글 삭제 실패!");
			}
			return "redirect:/board/detail/"+boardNo;			
		}else {
			return "redirect:/user/login.do";			
		}
		
	}
}











