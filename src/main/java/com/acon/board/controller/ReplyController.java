package com.acon.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.acon.board.dto.Paging;
import com.acon.board.dto.Reply;
import com.acon.board.dto.ReplyPrefer;
import com.acon.board.dto.User;
import com.acon.board.mapper.ReplyMapper;
import com.acon.board.mapper.ReplyPreferMapper;

@Controller
@RequestMapping("/reply")
public class ReplyController {
	@Autowired
	ReplyMapper replyMapper;
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	@Autowired
	private ReplyPreferMapper replyPreferMapper;
	
	@RequestMapping("/list/{boardNo}/{page}")
	public String list(
			@PathVariable int boardNo,
			@PathVariable int page,
			@SessionAttribute(required = false) User loginUser,
			Model model) {
		int row=5;
		int startRow=(page-1)*row;
		String url="/reply/list/"+boardNo;
		List<Reply> replys=null;
		String loginUserId=(loginUser!=null)?loginUser.getUser_id() : null;
		try {
			int rowCount=replyMapper.selectBoardNoCount(boardNo);
			Paging paging=new Paging(page, rowCount, url, row);
			replys=replyMapper.selectBoardNoPage(boardNo, startRow, row, loginUserId);
			model.addAttribute("paging", paging);
			model.addAttribute("replys", replys);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "/board/replyList";
	}
	
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
	@GetMapping("/delete/{replyNo}")
	public String delete(
			@PathVariable int replyNo,
			@SessionAttribute(required = false) User loginUser,
			HttpSession session) {
		Reply reply=replyMapper.selectOne(replyNo);
		System.out.println(reply);
		if(loginUser!=null&& loginUser.getUser_id().equals(reply.getUser().getUser_id())) {
			int delete=0;
			try {
				if(reply.getImg_path()!=null) {
					File file=new File(savePath+"/"+reply.getImg_path());
					boolean del=file.delete();
					System.out.println("댓글 이미지 삭제 :"+del);
				}
				delete=replyMapper.deleteOne(replyNo);
			} catch (Exception e) {e.printStackTrace();}
			if(delete>0) {
				session.setAttribute("msg", "댓글 삭제 성공!");
			}else {
				session.setAttribute("msg", "댓글 삭제 실패!");
			}
			return "redirect:/board/detail/"+reply.getBoard_no();			
		}else {
			return "redirect:/user/login.do";			
		}
		
	}
	
	@PutMapping("/prefer/{replyNo}/{prefer}")
	public String replyPerferUpdate(
				@PathVariable int replyNo,
				@PathVariable boolean prefer,
				@SessionAttribute(required = true) User loginUser,
				Model model) {
		System.out.println("put 호출");
		int update=0;
		Reply reply=null;
		try {
			ReplyPrefer replyPrefer=new ReplyPrefer();
			replyPrefer.setReply_no(replyNo);
			replyPrefer.setPrefer(prefer);
			replyPrefer.setUser_id(loginUser.getUser_id());
			update=replyPreferMapper.updateOne(replyPrefer);
			
			reply=replyMapper.selectOnePrefers(replyNo);
			model.addAttribute("reply", reply);
			if(update>0) {
				reply.setPrefer_active(prefer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/replyDetail";
	}
	@PostMapping("/prefer/{replyNo}/{prefer}")
	public String replyPerferInsert(
				@PathVariable int replyNo,
				@PathVariable boolean prefer,
				@SessionAttribute(required = true) User loginUser,
				Model model) {
		System.out.println("post 호출");
		Reply reply=null;
		int insert=0;
		try {
			ReplyPrefer replyPrefer=new ReplyPrefer();
			replyPrefer.setReply_no(replyNo);
			replyPrefer.setUser_id(loginUser.getUser_id());
			replyPrefer.setPrefer(prefer);
			insert=replyPreferMapper.insertOne(replyPrefer);						
			reply=replyMapper.selectOnePrefers(replyNo);
			if(insert>0) {
				reply.setPrefer_active(prefer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("reply",reply);
		return "/board/replyDetail";
	}
	@DeleteMapping("/prefer/{replyNo}")
	public String replyPerferDelete(
				@PathVariable int replyNo,
				@SessionAttribute(required = true) User loginUser,
				Model model) {
		System.out.println("delete 호출");
		int delete=0;
		Reply reply=null;
		try {
			ReplyPrefer replyPrefer=new ReplyPrefer();
			replyPrefer.setReply_no(replyNo);
			replyPrefer.setUser_id(loginUser.getUser_id());
			
			delete=replyPreferMapper.deleteOne(replyPrefer);
			
			reply=replyMapper.selectOnePrefers(replyNo);
			model.addAttribute("reply",reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/replyDetail";
	}
}











